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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JSONObject extends HashMap<String,Object> {

	public JSONObject() {
		super();
	}

	public JSONObject(int initialCapacity) {
		super(initialCapacity);
	}

	public JSONObject(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public JSONObject(Map<? extends String, ? extends Object> map) {
		super(map);
	}
	
	private void checkInstance(Object o) {
		if (!JSONValue.isInstance(o))
			throw new ClassCastException(o.getClass().getName() + " is not a valid JSON value");
	}

	public Object put(String key, Object value) {
		this.checkInstance(value);
		return super.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Object> map) {
		Set<? extends String> keys = map.keySet();
		for (String key : keys)
			this.checkInstance(map.get(key));
		super.putAll(map);
	}

}
