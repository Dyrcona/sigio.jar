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

public class JSONReader extends PushbackReader {

	public static final Character EOF = new Character((char)-1);

	/*
	 * Internal state variables.
	 */
	private int index = 0;

	public JSONReader(Reader in) {
		super(in);
	}

	public JSONReader(Reader in, int size) {
		super(in, size);
	}

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

	public String readString() throws IOException, JSONException {
		StringBuffer sb = new StringBuffer();

		while (true) {
			int c = this.read();
			switch (c) {
			case -1:
			case '\n':
			case '\r':
				throw this.syntaxException("Unterminated String");
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
					if (this.read(buf, 0, 4) < 4)
						throw this.syntaxException("Invalid unicode escape");
					sb.append((char)Integer.parseInt(new String(buf), 16));
					break;
				default:
					throw this.syntaxException("Invalid escape sequence \\" +(char)c);
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

	public Object readLiteralOrNumber() throws IOException, JSONException {
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
				throw this.syntaxException("Illegal character " + (char) c);
			case JSON.VALUE_SEPARATOR:
			case JSON.END_ARRAY:
			case JSON.END_OBJECT:
				this.unread(c); // Deliberate fall thru.
			case -1:
				cont = false;
				break;
			default:
				if (this.isWhiteSpace(c))
					cont = false;
				else
					sb.append((char)c);
				break;
			}
		}

		String str = sb.toString();
		if (this.looksLikeNumber(str)) {
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

	public Object readArrayValue() throws IOException, JSONException {
		Object obj = this.readValue();
		int c = this.skipWSRead();
		if (c == JSON.VALUE_SEPARATOR || c == JSON.END_ARRAY) {
			if (c == JSON.END_ARRAY)
				this.unread(c);
			return obj;
		}
		else
			throw this.syntaxException("Illegal character " + (char) c);
	}

	private JSONArray readArray() throws IOException, JSONException {
		JSONArray jsonArray = new JSONArray();
		while (this.peek() != JSON.END_ARRAY) {
			jsonArray.add(this.readArrayValue());
		}
		this.read();
		return jsonArray;
	}

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

	private String readObjectFieldName() throws IOException, JSONException {
		int c = this.skipWSRead();
		if (c != JSON.QUOTE_CHAR)
			throw this.syntaxException("Illegal character " + (char) c);
		String name = this.readString();
		c = this.skipWSRead();
		if (c == JSON.NAME_SEPARATOR)
			return name;
		else
			throw this.syntaxException("Illegal character " + (char) c);
	}

	private Object readObjectFieldValue() throws IOException, JSONException {
		Object value = this.readValue();
		int c = this.skipWSRead();
		if (c == JSON.VALUE_SEPARATOR || c == JSON.END_OBJECT) {
			if (c == JSON.END_OBJECT)
				this.unread(c);
			return value;
		}
		else
			throw this.syntaxException("Illegal character " + (char) c);
	}

	public int peek() throws IOException {
		int c = this.read();
		this.unread(c);
		return c;
	}

	@Override
	public int read() throws IOException {
		int c = super.read();
		this.index++;
		return c;
	}

	@Override
	public int read(char buf[], int off, int len) throws IOException {
		int n = super.read(buf, off, len);
		if (n > -1)
			this.index += n;
		return n;
	}

	@Override
	public void unread(int c) throws IOException {
		super.unread(c);
		this.index--;
	}

	@Override
	public void unread(char buf[], int off, int len) throws IOException {
		super.unread(buf, off, len);
		this.index -= len;
	}

	@Override
	public void unread(char buf[]) throws IOException {
		super.unread(buf);
		this.index -= buf.length;
	}

	private int skipWSRead() throws IOException {
		int c = this.read();
		while (this.isWhiteSpace(c))
			c = this.read();
		return c;
	}

	private JSONException syntaxException(String message) {
		String location = new String(" at character " + this.index);
		return new JSONException(message + location);
	}

	private boolean isWhiteSpace(int c) {
		for (int x : JSON.WHITESPACE) {
			if (c == x)
				return true;
		}
		return false;
	}

	private boolean looksLikeNumber(String str) {
		return str.matches("-?[0-9]+(?:\\.[0-9]+)?(?:[eE][-+]?[0-9]+)?");
	}

}
