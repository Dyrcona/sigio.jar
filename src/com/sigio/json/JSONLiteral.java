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

public enum JSONLiteral {

	NULL("null"),
	TRUE("true"),
	FALSE("false");

	private final String literalValue;

	JSONLiteral(String str) {
		this.literalValue = str;
	}

	public String value() {
		return this.literalValue;
	}

	@Override
	public String toString() {
		return this.literalValue;
	}

	public boolean equals(String str) {
		if (str.equals(this.literalValue))
			return true;
		return false;
	}

	public static JSONLiteral fromString(String str) throws JSONException {
		for (JSONLiteral l : JSONLiteral.values()) {
			if (l.equals(str))
				return l;
		}
		throw new JSONException(str + " is not a JSON literal value");
	}

}
