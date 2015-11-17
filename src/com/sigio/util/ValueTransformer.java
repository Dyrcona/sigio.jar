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
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Abstract class to provide a base for other value transformer
 * implementations.
 *
 * <p>A value transformer is an object that takes an instance of some
 * object and returns a new object of the same class with the input
 * object's value transformed in some way from the original. An
 * example of this might be a class to convert temperatures from
 * Fahrenheit to Centigrade.</p>
 *
 * @author Jason J.A. Stephenson
 * @version 2.0
 */
public abstract class ValueTransformer<T> {

  /**
   * Returns true if the instance can do a reverse transformation. The
   * inherited implementation always returns <code>false</code>.
   *
   * @return <code>true</code> if a reverse transformation is
   * supported; <code>false</code> if it is not
   */
  public boolean allowsReverseTransformation() { return false; }

  /**
   * Performs a reverse transformation. The inherited implementation
   * always throws an <code>UnsupportedOperationException</code>
   * unless the child class overrides
   * <code>allowsReverseTransformation</code> to return
   * <code>true</code>. If such a child does not also override
   * <code>reverseTransformValue</code>, the default performs
   * <code>transformValue</code>.
   *
   * @param o the instance object to reverse transform
   * @return new object with the transformed value
   * @throws UnsupportedOperationException when a reverse
   * transformation is not supported
   */
  public T reverseTransformValue(T o) {
    if (this.allowsReverseTransformation())
      return this.transformValue(o);
    else {
      UnsupportedOperationException ex;
      String exceptionFormat;
      String className = this.getClass().getName();
      try {
        ResourceBundle b = ResourceBundle.getBundle("com.sigio.util.ValueTransformer");
        exceptionFormat = b.getString("exceptionformat");
      }
      catch (MissingResourceException e) {
        exceptionFormat = "Class %s does not implement reverse transformation.";
      }
      String message = String.format(exceptionFormat, className);
      ex = new UnsupportedOperationException(message);
      throw ex;
    }
  }

  /**
   * Transforms an object's value.
   *
   * @param o object whose value is to be transformed
   * @return new object with the transformed value
   */
  public abstract T transformValue(T o);

}
