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
 * Abstract class to provide a base for other value transformer
 * implementations.
 *
 * <p>A value transformer is a class that can take an instance of an
 * object and creates a new object with the paramater object's value
 * transformed in some way from the original. An example of this might
 * be a class to convert temperatures from Fahrenheit to
 * Centigrade.</p>
 *
 * <p>The concept of this class was basically swiped from OpenStep's
 * NSValueTransformer.</p>
 */
public abstract class ValueTransformer {

	/**
	 * Check if the instance can do a reverse transformation. The
	 * inherited implementation always returns <code>false</code>.
	 *
	 * @return <code>true</code> if a reverse transformation is
	 * supported; <code>false</code> if it is not
	 */
	public boolean allowsReverseTransformation() { return false; }

	/**
	 * Perform a reverse transformation. The inherited implementation
	 * always throws an UnsupportedOperationException.
	 *
	 * @param o the instance object to reverse transform
	 * @return new object with the transformed value
	 * @throws UnsupportedOperationException when a reverse
	 * transformation is not supported
	 */
	public Object reverseTransformedValue(Object o) {
		if (this.allowsReverseTransformation())
			return this.transformedValue(o);
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
	 * Transform an object's value. It may throw a RuntimeException or
	 * one of its subclasses if certain implementation specific
	 * details are not met. This is, however, at the discretion of the
	 * subclass implementations.
	 *
	 * <p>At a minimum, implementations should throw an
	 * IllegalArgumentException if the parameter object is not an
	 * instance of the class returned by
	 * <code>transformedValueParamClass()</code>.</p>
	 *
	 * @param o object whose value is to be transformed
	 * @return new object with the transformed value
	 */
	public abstract Object transformedValue(Object o);

	/**
	 * Get the class of the object returned by the transformation methods.
	 *
	 * @return class of the objects returned by transformedValue and
	 * reverseTransformedValue
	 */
	public abstract Class<?> transformedValueReturnClass();

	/**
	 * Get the class (or one of its subclasses) required as the
	 * parameter to the transformation methods.
	 *
	 * @return class of the objects accepted as parameters by
	 * transformedValue and reverseTransformedValue
	 */
	public abstract Class<?> transformedValueParamClass();
}
