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

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

/**
 * Writer class to write certain java objects as JSON value strings.
 */
public class JSONWriter extends FilterWriter {

  /**
   * Construct a new JSONWriter.
   */
  public JSONWriter(Writer out) {
    super(out);
  }

  /**
   * Write a java object as a JSON value string.  Classes are
   * written as follows:
   * 
   * <ul>
   * <li>JSONLiteral, null, and Boolean are written as literal
   * strings.</li>
   *
   * <li>Instances of java.util.Map, including JSONObject, are
   * written as JSON objects.</li>
   *
   * <li>Instances of java.util.Collection, including JSONArray, are
   * written as JSON arrays.</li>
   *
   * <li>Instances of Number are written as JSON numbers
   * (i.e. literal string representation of the numeric value).</li>
   *
   * <li>Instances of String are written as JSON strings.</li>
   *
   * <li>All other classes have their toString() method called and
   * the result is written as a JSON string.</li>
   *
   * </ul>
   *
   * @param o Object to write
   * @throws IOException If an I/O error occurs
   */
  public void write(Object o) throws IOException {
    if (o == null) {
      super.out.write(JSONLiteral.NULL.toString());
    } else if (o instanceof JSONLiteral) {
      JSONLiteral literal = (JSONLiteral) o;
      super.out.write(literal.toString());
    } else if (o instanceof Boolean) {
      Boolean b = (Boolean) o;
      if (b == Boolean.TRUE)
        super.out.write(JSONLiteral.TRUE.toString());
      else
        super.out.write(JSONLiteral.FALSE.toString());
    } else if (o instanceof java.util.Map) {
      Map<?,?> map = (Map<?,?>) o;
      int count = 0;
      super.out.write(JSON.BEGIN_OBJECT);
      for (Object key : map.keySet()) {
        if (count++ > 0) super.out.write(JSON.VALUE_SEPARATOR);
        this.write(key);
        super.out.write(JSON.NAME_SEPARATOR);
        this.write(map.get(key));
      }
      super.out.write(JSON.END_OBJECT);
    } else if (o instanceof java.util.Collection) {
      int count = 0;
      Collection<?> collection = (Collection<?>) o;
      super.out.write(JSON.BEGIN_ARRAY);
      for (Object obj : collection) {
        if (count++ > 0) super.out.write(JSON.VALUE_SEPARATOR);
        this.write(obj);
      }
      super.out.write(JSON.END_ARRAY);
    } else if (o instanceof java.lang.String) {
      String str = (String) o;
      this.write(str);
    } else if (o instanceof java.lang.Number) {
      Number num = (Number) o;
      super.out.write(num.toString());
    } else {
      this.write(o.toString());
    }
  }

  /**
   * Writes a string as a JSON string.
   *
   * @param str String to write
   * @throws IOException If an I/O error occurs
   */
  @Override
  public void write(String str) throws IOException {
    String out = JSONStringAdapter.toJSONString(str);
    super.out.write(out);
  }
  /**
   * Writes a portion of a string as a JSON string.
   *
   * @param str String to be written
   * @param off Offset from which to start reading characters
   * @param len Number of characters to write
   * @throws IOException If an I/O error occurs
   * @throws IndexOutOfBoundsException If {@code off} or {@code len}
   * are negative or if {@code off + len} exceed the length of str
   */
  @Override
  public void write(String str, int off, int len) throws IOException, IndexOutOfBoundsException {
    this.write(str.substring(off, (off + len)));
  }

}
