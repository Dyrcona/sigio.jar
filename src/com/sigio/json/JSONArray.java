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
/**
 * Class to implement a JSON array as defined in RFC4627. We inherit
 * from ArrayList<Object> to take advantage of existing functionality.
 */
public class JSONArray extends ArrayList<Object> {
	/**
	 * Construct a default JSONArray.
	 */
	public JSONArray() {
		super();
	}
	/**
	 * Construct a JSONArray and fill it with object from a collection.
	 *
	 * @param collection the collection of objects to fill the array
	 * @throws ClassCastException if one of the array members is not a
	 * suitable JSON object
	 */
	public JSONArray(Collection<Object> collection) {
		this();
		this.addAll(collection);
	}
	/**
	 * Construct a JSONArray with an initial size
	 *
	 * @param size the initial capacity of the array
	 */
	public JSONArray(int size) {
		super(size);
	}
	/*
	 * Helper method to check if the parameter object's class is a
	 * suitable JSON object.
	 */
	private void checkInstance(Object o) {
		if (!JSONValue.isInstance(o))
			throw new ClassCastException(o.getClass().getName() + " is not a valid JSON value");
	}
	/**
	 * {@inheritDoc}
	 * @throws ClassCastException if the passed in Object is not a
	 * suitable JSON object
	 */
	@Override
	public boolean add(Object o) {
		this.checkInstance(o);
		return super.add(o);
	}
	/**
	 * {@inheritDoc}
	 * @throws ClassCastException if the passed in Object is not a
	 * suitable JSON object
	 */
	@Override
	public void add(int idx, Object o) {
		this.checkInstance(o);
		super.add(idx, o);
	}

	/**
	 * {@inheritDoc}
	 * @throws ClassCastException if the passed in Object is not a
	 * suitable JSON object
	 */
	@Override
	public boolean addAll(Collection<? extends Object> collection) {
		for (Object o : collection)
			this.checkInstance(o);
		return super.addAll(collection);
	}

	/**
	 * {@inheritDoc}
	 * @throws ClassCastException if any of the members of the
	 * collection is not a suitable JSON object
	 */
	@Override
	public boolean addAll(int idx, Collection<? extends Object> collection) {
		for (Object o : collection)
			this.checkInstance(o);
		return super.addAll(idx, collection);
	}

	/**
	 * {@inheritDoc}
	 * @throws ClassCastException if any members of the collection is
	 * not a suitable JSON object
	 */
	@Override
	public Object set(int idx, Object o) {
		this.checkInstance(o);
		return super.set(idx, o);
	}

}
