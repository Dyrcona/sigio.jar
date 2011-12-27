/*
 * Copyright © 2011 Jason J.A. Stephenson
 * 
 * This file is part of sigio.jar.
 * 
 * sigio.jar is free software: you can redistribute it and/or modify it
 * under the terms of the Lesser GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * sigio.jar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 * 
 * You should have received a copy of the Lesser GNU General Public License
 * along with sigio.jar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sigio.json;
import java.io.PushbackReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ResourceBundle;

/**
 * Reader subclass to read JSON data and create JSON objects.
 */
public class JSONReader extends PushbackReader {

	/**
	 * A special Character constant to return if the reader hits EOF.
	 */
	public static final Character EOF = new Character((char)-1);

	private ResourceBundle bundle = null;

	/*
	 * Internal state variables.
	 */
	// Our position in the input.
	private int index = 0;

	/**
	 * Create a new JSONReader with a default pushback buffer. The
	 * default can pushback 1 character at a time.
	 */
	public JSONReader(Reader in) {
		super(in);
		bundle = com.sigio.json.BundleLoader.getBundle();
	}

	/**
	 * Create a new JSONReader with a specified Pushback buffer size.
	 */
	public JSONReader(Reader in, int size) {
		super(in, size);
		bundle = com.sigio.json.BundleLoader.getBundle();
	}

	/**
	 * Specialized read method to read JSON objects from the input
	 * data.
	 *
	 * @return object representing the JSON value read from the input
	 * @throws IOException if a read error occurs.
	 * @throws JSONException if the input is not properly formed JSON
	 * according to RFC4627
	 */
	public Object readValue() throws IOException, JSONException {
		int c = this.skipWSRead();

		switch(c) {
		case -1:
			return JSONReader.EOF;
		case JSON.BEGIN_ARRAY:
			return this.readArray();			
		case JSON.BEGIN_OBJECT:
			return this.readObject();
		case JSON.QUOTE_CHAR:
			return this.readString();
		default:
			this.unread(c);
			return this.readLiteralOrNumber();
		}
	}

	/**
	 * Reads a string from a JSON input. This is used by
	 * <code>readValue</code> and is not intended to be used by client
	 * code.
	 *
	 * @return string read from the inpu
	 * @throws IOException if a read error occurs.
	 * @throws JSONException if the input is not properly formed JSON
	 * according to RFC4627
	 */
	private String readString() throws IOException, JSONException {
		StringBuffer sb = new StringBuffer();

		while (true) {
			int c = this.read();
			switch (c) {
			case -1:
			case '\n':
			case '\r':
				throw this.syntaxException(this.bundle.getString("UNTERMINATED_STRING"));
			case JSON.ESCAPE_CHAR:
				c = this.read();
				switch (c) {
				case JSON.QUOTE_CHAR:
				case JSON.ESCAPE_CHAR:
				case '/':
					sb.append((char)c);
					break;
				case 'b':
					sb.append('\b');
					break;
				case 'f':
					sb.append('\f');
					break;
				case 'n':
					sb.append('\n');
					break;
				case 'r':
					sb.append('\r');
					break;
				case 't':
					sb.append('\t');
					break;
				case 'u':
					char buf[] = new char[4];
					if (this.read(buf, 0, 4) < 4) {
						String message = String.format(this.bundle.getString("INVALID_ESCAPE"), "\\u" + new String(buf));
						throw this.syntaxException(message);
					}
					sb.append((char)Integer.parseInt(new String(buf), 16));
					break;
				default:
					String message = String.format(this.bundle.getString("INVALID_ESCAPE"), "\\" + (char)c);
					throw this.syntaxException(message);
				}
				break;
			default:
				if (c == JSON.QUOTE_CHAR)
					return sb.toString();
				else
					sb.append((char)c);
				break;
			}
		}
		
	}

	/**
	 * Reads a JSONLiteral or a Number from a JSON input. This is used
	 * by <code>readValue</code> and is not intended to be used by
	 * client code.
	 *
	 * @return object read from the input
	 * @throws IOException if a read error occurs.
	 * @throws JSONException if the input is not properly formed JSON
	 * according to RFC4627
	 */
	private Object readLiteralOrNumber() throws IOException, JSONException {
		StringBuffer sb = new StringBuffer();

		boolean cont = true;

		while (cont) {
			int c = this.read();
			switch (c) {
			case JSON.BEGIN_ARRAY:
			case JSON.BEGIN_OBJECT:
			case JSON.QUOTE_CHAR:
			case JSON.ESCAPE_CHAR:
			case JSON.NAME_SEPARATOR:
				String message = String.format(this.bundle.getString("ILLEGAL_CHARACTER"), (char) c);
				throw this.syntaxException(message);
			case JSON.VALUE_SEPARATOR:
			case JSON.END_ARRAY:
			case JSON.END_OBJECT:
				this.unread(c); // Deliberate fall thru.
			case -1:
				cont = false;
				break;
			default:
				if (JSON.isWhiteSpace(c))
					cont = false;
				else
					sb.append((char)c);
				break;
			}
		}

		String str = sb.toString();
		if (JSON.isNumber(str)) {
			if (str.matches(".*?(?:\\.|e|E).*?")) {
				try {
					Double d = Double.valueOf(str);
					return d;
				} catch (NumberFormatException e) {
					this.index -= str.length();
					JSONException exc = this.syntaxException("NumberFormatException " + e.getMessage());
					this.index += str.length();
					throw exc;
				}
			} else {
				try {
					Long l = Long.valueOf(str);
					return l;
				} catch (NumberFormatException e) {
					this.index -= str.length();
					JSONException exc = this.syntaxException("NumberFormatException " + e.getMessage());
					this.index += str.length();
					throw exc;
				}
			}
		} else {
			try {
				return JSONLiteral.fromString(str);
			} catch (JSONException e) {
				this.index -= str.length();
				JSONException exc = this.syntaxException(e.getMessage());
				this.index += str.length();
				throw exc;
			}
		}
	}

	/**
	 * Reads a JSON array member from a JSON input. This is used by
	 * <code>readArray</code> and is not intended to be used by client
	 * code.
	 *
	 * @return object read from the input
	 * @throws IOException if a read error occurs.
	 * @throws JSONException if the input is not properly formed JSON
	 * according to RFC4627
	 */
	private Object readArrayValue() throws IOException, JSONException {
		Object obj = this.readValue();
		int c = this.skipWSRead();
		if (c == JSON.VALUE_SEPARATOR || c == JSON.END_ARRAY) {
			if (c == JSON.END_ARRAY)
				this.unread(c);
			return obj;
		}
		else {
			String message = String.format(this.bundle.getString("ILLEGAL_CHARACTER"), (char)c);
			throw this.syntaxException(message);
		}
	}

	/**
	 * Reads a JSONArray from a JSON input. This is used by
	 * <code>readValue</code> and is not intended to be used by client
	 * code.
	 *
	 * @return JSONArray read from the input
	 * @throws IOException if a read error occurs.
	 * @throws JSONException if the input is not properly formed JSON
	 * according to RFC4627
	 */
	private JSONArray readArray() throws IOException, JSONException {
		JSONArray jsonArray = new JSONArray();
		while (this.peek() != JSON.END_ARRAY) {
			jsonArray.add(this.readArrayValue());
		}
		this.read();
		return jsonArray;
	}

	/**
	 * Reads a JSONObject from a JSON input. This is used by
	 * <code>readValue</code> and is not intended to be used by client
	 * code.
	 *
	 * @return JSONObject read from the input
	 * @throws IOException if a read error occurs.
	 * @throws JSONException if the input is not properly formed JSON
	 * according to RFC4627
	 */
	private JSONObject readObject() throws IOException, JSONException {
		JSONObject jsonObject = new JSONObject();
		while (this.peek() != JSON.END_OBJECT) {
			String key = this.readObjectFieldName();
			Object value = this.readObjectFieldValue();
			jsonObject.put(key, value);
		}
		this.read();
		return jsonObject;
	}

	/**
	 * Reads a field name for a JSON object member from a JSON
	 * input. This is used by <code>readObject</code> and is not
	 * intended to be used by client code.
	 *
	 * @return string read from the input
	 * @throws IOException if a read error occurs.
	 * @throws JSONException if the input is not properly formed JSON
	 * according to RFC4627
	 */
	private String readObjectFieldName() throws IOException, JSONException {
		int c = this.skipWSRead();
		if (c != JSON.QUOTE_CHAR)
			throw this.syntaxException("Illegal character " + (char) c);
		String name = this.readString();
		c = this.skipWSRead();
		if (c == JSON.NAME_SEPARATOR)
			return name;
		else {
			String message = String.format(this.bundle.getString("ILLEGAL_CHARACTER"), (char)c);
			throw this.syntaxException(message);
		}
	}

	/**
	 * Reads a value of a JSON object member from a JSON input. This
	 * is used by <code>readObject</code> and is not intended to be
	 * used by client code.
	 *
	 * @return object read from the input
	 * @throws IOException if a read error occurs.
	 * @throws JSONException if the input is not properly formed JSON
	 * according to RFC4627
	 */
	private Object readObjectFieldValue() throws IOException, JSONException {
		Object value = this.readValue();
		int c = this.skipWSRead();
		if (c == JSON.VALUE_SEPARATOR || c == JSON.END_OBJECT) {
			if (c == JSON.END_OBJECT)
				this.unread(c);
			return value;
		}
		else {
			String message = String.format(this.bundle.getString("ILLEGAL_CHARACTER"), (char)c);
			throw this.syntaxException(message);
		}
	}

	/**
	 * A special method to take advantage of the features of our
	 * parent PushbackWriter. It will read the next character from the
	 * input, push it back on the buffer and then return it.
	 *
	 * @return integer representing the character read from the input
	 * @throws IOException if a read error occurs.
	 */
	public int peek() throws IOException {
		int c = this.read();
		this.unread(c);
		return c;
	}

	/**
	 * Reads a single character.
	 *
	 * @return The character read, or -1 if the end of stream has been
	 * reached.
	 * @throws IOException If an I/O error occurs
	 */
	@Override
	public int read() throws IOException {
		int c = super.read();
		this.index++;
		return c;
	}

	/**
	 * Reads characters into a portion of an array.
	 *
	 * @param buf Destination buffer
	 * @param off Offset at which to start writing characters
	 * @param len Maximum number of characters to read
	 * @return The number of characters read, or -1 if the end of the
	 * stream has been reached
	 * @throws IOException If an I/O error occurs
	 */
	@Override
	public int read(char buf[], int off, int len) throws IOException {
		int n = super.read(buf, off, len);
		if (n > -1)
			this.index += n;
		return n;
	}

	/**
	 * Pushes back a single character by copying it to the front of
	 * the pushback buffer. After this method returns, the next
	 * character to be read will have the value {@code (char)c}.
	 *
	 * @param c The int value representing a character to be pushed back
	 * @throws If the pushback buffer is full, or if some other I/O
	 * error occurs
	 */
	@Override
	public void unread(int c) throws IOException {
		super.unread(c);
		this.index--;
	}

	/**
	 * Pushes back a portion of an array of characters by copying it
	 * to the front of the pushback buffer. After this method returns,
	 * the next character to be read will have the value {@code
	 * cbuf[off]}, the character after that will have the value {@code
	 * cbuf[off+1]}, and so forth.
	 *
	 * @param buf Character array to push back
	 * @param off Offset of first character to push back
	 * @param len Number of characters to push back
	 * @throws IOException If there is insufficient room in the
	 * pushback buffer, or if some other I/O error occurs
	 */
	@Override
	public void unread(char buf[], int off, int len) throws IOException {
		super.unread(buf, off, len);
		this.index -= len;
	}

	/**
	 * Pushes back an array of characters by copying it to the front
	 * of the pushback buffer. After this method returns, the next
	 * character to be read will have the value {@code buf[0]}, the
	 * character after that will have the value {@code buf[1]}, and so
	 * forth.
	 *
	 * @param buf Character array to push back
	 * @throws IOException If there is insufficient room in the
	 * pushback buffer, or if some other I/O error occurs
	 */
	@Override
	public void unread(char buf[]) throws IOException {
		super.unread(buf);
		this.index -= buf.length;
	}

	/*
	 * Implementation method to read characters from the input until a
	 * non-whitespace character is found.
	 */
	private int skipWSRead() throws IOException {
		int c = this.read();
		while (JSON.isWhiteSpace(c))
			c = this.read();
		return c;
	}

	/*
	 * Helper method to construct a JSONException to be thrown when an
	 * error is detected in the input.
	 */
	private JSONException syntaxException(String message) {
		String location = String.format(this.bundle.getString("AT"), this.index);
		return new JSONException(message + " " + location);
	}

}
