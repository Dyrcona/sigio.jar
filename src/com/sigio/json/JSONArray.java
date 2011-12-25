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

import java.util.ArrayList;
import java.util.Collection;

public class JSONArray extends ArrayList<Object> {

	public JSONArray() {
		super();
	}

	public JSONArray(Collection<Object> collection) {
		this();
		this.addAll(collection);
	}

	public JSONArray(int size) {
		super(size);
	}

	private void checkInstance(Object o) {
		if (!JSONValue.isInstance(o))
			throw new ClassCastException(o.getClass().getName() + " is not a valid JSON value");
	}

	@Override
	public boolean add(Object o) {
		this.checkInstance(o);
		return super.add(o);
	}

	@Override
	public void add(int idx, Object o) {
		this.checkInstance(o);
		super.add(idx, o);
	}

	@Override
	public boolean addAll(Collection<? extends Object> collection) {
		for (Object o : collection)
			this.checkInstance(o);
		return super.addAll(collection);
	}

	@Override
	public boolean addAll(int idx, Collection<? extends Object> collection) {
		for (Object o : collection)
			this.checkInstance(o);
		return super.addAll(idx, collection);
	}

	@Override
	public Object set(int idx, Object o) {
		this.checkInstance(o);
		return super.set(idx, o);
	}

}
