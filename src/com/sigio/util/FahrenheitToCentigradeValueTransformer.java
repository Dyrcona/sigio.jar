/*
 * Copyright © 2011 Jason J.A. Stephenson
 * 
 * This file is part of sigio.jar.
 * 
 * sigio.jar is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * sigio.jar is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 * 
 * You should have received a copy of the Lesser GNU General Public License
 * along with sigio.jar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sigio.util;

/**
 * A value transformer to convert temperatures from Fahrenheit to
 * Centigrade and to do the reverse.
 */

public class FahrenheitToCentigradeValueTransformer extends NumberToDoubleValueTransformer {

  /**
   * {@inheritDoc}
   *
   * @return always returns <code>true</code>
   */
  public boolean allowsReverseTransformation() { return true; }

  /**
   * Transforms a number with an assumed value of degrees Fahrenheit
   * into a double with a value in degrees Centrigrade.
   *
   * @param o {@inheritDoc}
   * @return {@inheritDoc}
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public Object transformedValue(Object o) {
    if (o instanceof Number) {
      Number f = (Number) o;
      double c = (f.doubleValue() - 32.0) * 5.0 / 9.0;
      return new Double(c);
    }
    else throwIllegalArgumentException("transformedValue");
    return null;
  }

  /**
   * Transforms a number with an assumed value of degrees Centigrade
   * into a double with a value in degrees Fahrenheit.
   *
   * @param o <code>java.lang.Number</code> with value to be
   * transformed
   * @return <code>java.lang.Double</code> with the transformed
   * value of o
   * @throws IllegalArgumentException if o is not an instance of
   * <code>java.lang.Number</code>
   */
  public Object reverseTransformedValue(Object o) {
    if (o instanceof Number) {
      Number c = (Number) o;
      double f = c.doubleValue() * 9.0 / 5.0 + 32.0;
      return new Double(f);
    }
    else throwIllegalArgumentException("reverseTransformedValue");
    return null;
  }

}
