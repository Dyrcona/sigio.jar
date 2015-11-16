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
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A simple class to convert a Number or one of its subclasses to a
 * Double. It makes a nice base class for many numeric value
 * transformers.
 */

public class NumberToDoubleValueTransformer extends ValueTransformer {

  /**
   * A convenience method for when an instance of this class or one
   * of its children needs to throw an IllegalArgumentException,
   * which they do if the parameter to one of the transformation
   * methods is not an instance of Number or one of its subclasses.
   *
   * @param methodName the name of the method where the exception occurred
   * @throws IllegalArgumentException because that is what it does
   */
  protected final void throwIllegalArgumentException(String methodName) {
    String myClassName = this.getClass().getName();
    String paramClassName = this.transformedValueParamClass().getName();
    String message = null;
    try {
      ResourceBundle b = ResourceBundle.getBundle("com.sigio.util.NumberToDoubleValueTransformer");
      message = String.format(b.getString("msgformat"), methodName, myClassName, paramClassName);
    }
    catch (MissingResourceException e) {
      message = String.format("Method %1$s of %2$s requires instance of %3$s.", methodName, myClassName, paramClassName);
    }
    throw new IllegalArgumentException(message);
  }

  /**
   * {@inheritDoc}
   *
   * @return Class<Double> because that's what we do
   */
  public final Class<Double> transformedValueReturnClass() { return Double.class; }

  /**
   * {@inheritDoc}
   *
   * @return Class<Number> because that's what we do
   */
  public final Class<Number> transformedValueParamClass() { return Number.class; }

  /**
   * Transforms an instance of Number (or one of its subclasses)
   * into an instance of Double. Not very useful in and of itself,
   * but it makes a nice base to build on.
   *
   * @param o <code>java.lang.Number</code> to be transformed
   * @return <code>java.lang.Double</code> with the transformed
   * value of o
   * @throws IllegalArgumentException if o is not an instance of
   * <code>java.lang.Number</code>
   */
  public Object transformedValue(Object o) {
    if (o instanceof Number) {
      Number n = (Number) o;
      return new Double(n.doubleValue());
    }
    else throwIllegalArgumentException("transformedValue");
    return null;
  }

}
