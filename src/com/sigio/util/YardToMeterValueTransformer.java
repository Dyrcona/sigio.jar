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
 * A double value transformer to convert yard values to meters and
 * vice versa.
 *
 * @author Jason J.A. Stephenson
 * @version 2.0
 */
public class YardToMeterValueTransformer extends ValueTransformer<Double> {

  // Conversion factor for yards to meters.
  private static final double CONVERSION_FACTOR = 1.0936;

  /**
   * {@inheritDoc}
   *
   * @return always returns <code>true</code>
   */
  @Override
  public boolean allowsReverseTransformation() { return true; }

  /**
   * Transforms a <code>Double</code> instance with an assumed value
   * in yards to a <code>Double</code> with a value in meters.
   *
   * @param y <cod>java.lang.Double</code> to be transformed
   * @return <code>java.lang.Double</code> with the value y
   * divided by 1.0936
   */
  @Override
  public Double transformValue(Double y) {
    double m = y.doubleValue() / CONVERSION_FACTOR;
    return new Double(m);
  }

  /**
   * Transforms a <code>Double</code> instance with an assumed value
   * in meters to a <code>Double</code> with a value in yards.
   *
   * @param m <code>java.lang.Double</code> to be transformed
   * @return <code>java.lang.Double</code> with the value of m
   * multiplied by 1.0936
   */
  @Override
  public Double reverseTransformValue(Double m) {
    double y = m.doubleValue() * CONVERSION_FACTOR;
    return new Double(y);
  }

}
