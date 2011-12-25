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

public enum JSONValue {

	OBJECT(JSONObject.class),
	ARRAY(JSONArray.class),
	NUMBER(Number.class),
	STRING(String.class),
	LITERAL(JSONLiteral.class);

	private final Class<?> klass;

	JSONValue(Class<?> k) {
		this.klass = k;
	}

	public Class<?> valueClass() {
		return this.klass;
	}

	@Override
	public String toString() {
		return this.klass.getName();
	}

	public static boolean isAssignableFrom(Class<?> k) {
		for (JSONValue v : JSONValue.values()) {
			if (v.valueClass().isAssignableFrom(k))
				return true;
		}
		return false;
	}

	public static boolean isInstance(Object obj) {
		for (JSONValue v : JSONValue.values()) {
			if (v.valueClass().isInstance(obj))
				return true;
		}
		return false;
	}

}
