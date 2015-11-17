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
 * A double value transformer to convert inch values to millimeters
 * and vice versa.
 *
 * @author Jason J.A. Stephenson
 * @version 2.0
 */

public class InchToMillimeterValueTransformer extends ValueTransformer<Double> {

  /**
   * {@inheritDoc}
   *
   * @return always returns <code>true</code>
   */
  @Override
  public boolean allowsReverseTransformation() { return true; }

  /**
   * Transforms a <code>Double</code> with an assumed value in inches
   * to a <code>Double</code> with a value in millimeters.
   *
   * @param i <cod>Double</code> to be transformed
   * @return <code>Double</code> with the value of i multiplied by
   * 25.4
   */
  @Override
  public Double transformValue(Double i) {
    double mm = i.doubleValue() * 25.4;
    return new Double(mm);
  }

  /**
   * Transforms a <code>Double</code> with an assumed value in
   * millimeters into a <code>Double</code> with a value in inches.
   *
   * @param mm <code>Double</code> to be transformed
   * @return <code>Double</code> with the value of mm divided by 25.4
   */
  @Override
  public Double reverseTransformValue(Double mm) {
    double i = mm.doubleValue() / 25.4;
    return new Double(i);
  }

}
