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
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Class to implement a JSON object as defined in RFC4627.
 */
public class JSONObject extends HashMap<String,Object> {

  /**
   * Construct an empty JSONObject with a default initial capacity
   * and load factor.
   */
  public JSONObject() {
    super();
  }

  /**
   * Construct a JSONObject with an initial capacity and default
   * load factor.
   *
   * @param initialCapacity initial capacity of the map
   */
  public JSONObject(int initialCapacity) {
    super(initialCapacity);
  }

  /**
   * Constructs a JSONObject with an initial capacity and load
   * factor.
   */
  public JSONObject(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
  }

  /**
   * Construct a JSONObject from an existing map of objects.
   */
  public JSONObject(Map<? extends String, ? extends Object> map) {
    super(map);
  }
  
  /**
   * Private method to check if the passed in object is a suitable
   * JSON value class.
   */
  private void checkInstance(Object o) throws ClassCastException {
    if (o == null)
      o = JSONLiteral.NULL;
    if (!JSONValue.isInstance(o)) {
      ResourceBundle bundle = com.sigio.json.BundleLoader.getBundle();
      String message = String.format(bundle.getString("INVALID_VALUE"), o.getClass().getName());
      throw new ClassCastException(message);
    }
  }

  /**
   * Associates the specified value with the specified key in this
   * object.
   *
   * @param key JSON object field name with which the specified
   * value is to be associated
   * @param value value for the JSON field
   * @return the previous value associated with {@code key}, or
   * {@code null} if there was no mapping for {@code key}.
   * @throws NullPointerException if the {@code key} is null
   * @throws ClassCastException if the passed in value is not a
   * suitable JSON object
   */
  @Override
  public Object put(String key, Object value) throws ClassCastException {
    if (key == null)
      throw new NullPointerException();
    this.checkInstance(value);
    return super.put(key, value);
  }

  /**
   * Copies all of the mappings from the specified map to this map.
   *
   * @param map mappings to be stored in this map
   * @throws NullPointerException if the specified map is null or contains null keys
   * @throws ClassCastException if the passed in value is not a
   * suitable JSON object
   */
  @Override
  public void putAll(Map<? extends String, ? extends Object> map) throws ClassCastException {
    Set<? extends String> keys = map.keySet();
    for (String key : keys) {
      if (key == null)
        throw new NullPointerException();
      this.checkInstance(map.get(key));
    }
    super.putAll(map);
  }

}
