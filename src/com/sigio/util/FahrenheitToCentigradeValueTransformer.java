/*
 * Copyright © 2015 Jason J.A. Stephenson
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
 * A <code>Double</code> value transformer to convert temperatures
 * from Fahrenheit to Centigrade and to do the reverse.
 *
 * @author Jason J.A. Stephenson
 * @version 2.0
 */

public class FahrenheitToCentigradeValueTransformer extends ValueTransformer<Double> {

  /**
   * {@inheritDoc}
   *
   * @return always returns <code>true</code>
   */
  @Override
  public boolean allowsReverseTransformation() { return true; }

  /**
   * Transforms a <code>Double</code> with an assumed value of degrees
   * Fahrenheit into a <code>Double</code> with a value in degrees
   * Centrigrade.
   *
   * @param f {@inheritDoc}
   * @return {@inheritDoc}
   */
  @Override
  public Double transformValue(Double f) {
    double c = (f.doubleValue() - 32.0) * 5.0 / 9.0;
    return new Double(c);
  }

  /**
   * Transforms a <code>Double</code> with an assumed value of degrees
   * Centigrade into a <code>Double</code> with a value in degrees
   * Fahrenheit.
   *
   * @param c <code>Double</code> with value to be transformed
   * @return <code>Double</code> with the transformed value of c
   */
  @Override
  public Double reverseTransformValue(Double c) {
    double f = c.doubleValue() * 9.0 / 5.0 + 32.0;
    return new Double(f);
  }

}
