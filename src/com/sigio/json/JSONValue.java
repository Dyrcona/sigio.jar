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
 * Enum of valid java classes for JSON values.
 */
public enum JSONValue {

	/**
	 * Class for JSON objects.
	 */
	OBJECT(JSONObject.class),
	/**
	 * Class for JSON arrays.
	 */
	ARRAY(JSONArray.class),
	/**
	 * Class for JSON numbers.
	 */
	NUMBER(Number.class),
	/**
	 * Class for JSON strings.
	 */
	STRING(String.class),
	/**
	 * Class (enum) for JSON literal values.
	 */
	LITERAL(JSONLiteral.class);

	private final Class<?> klass;

	/**
	 * Our hidden constructor.
	 */
	JSONValue(Class<?> k) {
		this.klass = k;
	}

	/**
	 * Accessor for the class member.
	 *
	 * @return Class<?> of the specific enum value.
	 */
	public Class<?> valueClass() {
		return this.klass;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return The name of class of the specific enum value
	 */
	@Override
	public String toString() {
		return this.klass.getName();
	}

	/**
	 * Check if a Class can be used as a JSON value.
	 *
	 * @param k class to check
	 * @return <code>true</code> if the class is a viable JSON value
	 * class, <code>false</code> if not
	 */
	public static boolean isAssignableFrom(Class<?> k) {
		for (JSONValue v : JSONValue.values()) {
			if (v.valueClass().isAssignableFrom(k))
				return true;
		}
		return false;
	}

	/**
	 * Check if an instantiated object is a valid JSON value.
	 *
	 * @param obj object to check
	 * @return <code>true</code> if the object is a viable JSON value,
	 * <code>false</code> if not
	 */
	public static boolean isInstance(Object obj) {
		for (JSONValue v : JSONValue.values()) {
			if (v.valueClass().isInstance(obj))
				return true;
		}
		return false;
	}

}
