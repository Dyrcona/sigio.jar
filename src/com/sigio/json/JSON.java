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

/**
 * JSON character constants and character utility routines based on
 * RFC4627.
 */
public class JSON {
	/**
	 * Character that opens a JSON array sequence.
	 */
	public static final int BEGIN_ARRAY = '[';
	/**
	 * Character that closes a JSON array sequence.
	 */
	public static final int END_ARRAY = ']';
	/**
	 * Character that opens a JSON object sequence.
	 */
	public static final int BEGIN_OBJECT = '{';
	/**
	 * Character that closes a JSON object sequence.
	 */
	public static final int END_OBJECT = '}';
	/**
	 * Character that opens and closes JSON strings.
	 */
	public static final int QUOTE_CHAR = '"';
	/**
	 * Character that separates an object field name from the value.
	 */
	public static final int NAME_SEPARATOR = ':';
	/**
	 * Character that separates fields in an array or object.
	 */
	public static final int VALUE_SEPARATOR = ',';
	/**
	 * Array of whitespace characters in a JSON format.
	 */
	public static final int[] WHITESPACE = { ' ', '\t', '\r', '\n' };
	/**
	 * Character that escapes special characters.
	 */
	public static final int ESCAPE_CHAR = '\\';

	/**
	 * Check if a character is whitespace according to the standard.
	 *
	 * @param c int value of character to check.
	 * @return <code>true</code> if the character is whitespace,
	 * <code>false</code> if not
	 */
	public static boolean isWhiteSpace(int c) {
		for (int x : JSON.WHITESPACE) {
			if (c == x)
				return true;
		}
		return false;
	}

	/**
	 * Check if a string represents a number according to RFC4627.
	 *
	 * @param str the string to check
	 * @return <code>true</code> if the string matches the JSON
	 * specification for a number, <code>false</code> if not
	 */
	public static boolean isNumber(String str) {
		return str.matches("-?[0-9]+(?:\\.[0-9]+)?(?:[eE][-+]?[0-9]+)?");
	}

}
