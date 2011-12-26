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
 * Enum for the allowed JSON literals as defined in RFC4627.
 */
public enum JSONLiteral {
	/**
	 * The null literal string.
	 */
	NULL("null"),
	/**
	 * The true literal string. Our implementation returns this
	 * instead of Boolean.TRUE.
	 */
	TRUE("true"),
	/**
	 * The false literal string. Our implementation returns this
	 * instead of Boolean.FALSE.
	 */
	FALSE("false");

	private final String literalValue;

	/**
	 * Our hidden constructor.
	 */
	JSONLiteral(String str) {
		this.literalValue = str;
	}

	/**
	 * Get our literal value.
	 *
	 * @return string with the literal JSON value
	 */
	public String value() {
		return this.literalValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.literalValue;
	}

	/**
	 * Check if a passed in string matches the literal's value.
	 *
	 * @param str string to check
	 * @return <code>true</code> if the string matches,
	 * <code>false</code> if not
	 */
	public boolean equals(String str) {
		if (str.equals(this.literalValue))
			return true;
		return false;
	}

	/**
	 * Get a JSONLiteral from a string.
	 *
	 * @param str string containing a JSON literal value
	 * @return the JSONLiteral whose value matches the string
	 * @throws JSONException if the string does not match a JSON
	 * literal value
	 */
	public static JSONLiteral fromString(String str) throws JSONException {
		for (JSONLiteral l : JSONLiteral.values()) {
			if (l.equals(str))
				return l;
		}
		throw new JSONException(str + " is not a JSON literal value");
	}

}
