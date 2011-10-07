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
 * A value transformer to convert inch values to millimeters and vice
 * versa.
 */

public class InchToMillimeterValueTransformer extends NumberToDoubleValueTransformer {

	/**
	 * {@inheritDoc}
	 *
	 * @return always returns <code>true</code>
	 */
	public boolean allowsReverseTransformation() { return true; }

	/**
	 * Transforms a Number instance with an assumed value in inches to
	 * a Double with a value in millimeters.
	 *
	 * @param o <cod>java.lang.Number</code> to be transformed
	 * @return <code>java.lang.Double</code> with the value o
	 * multiplied by 25.4
	 * @throws IllegalArgumentException {@inheritDoc}
	 */
	public Object transformedValue(Object o) {
		if (o instanceof Number) {
			Number i = (Number) o;
			double mm = i.doubleValue() * 25.4;
			return new Double(mm);
		}
		else throwIllegalArgumentException("transformedValue");
		return null;
	}

	/**
	 * Transforms a Number instance with an assumed value in
	 * millimeters to a Double with a value in inches.
	 *
	 * @param o number to be transformed
	 * @return <code>java.lang.Double</code> with the value of o
	 * divided by 25.4
	 * @throws IllegalArgumentException if o is not an instance of
	 * <code>java.lang.Number</code>
	 */
	public Object reverseTransformedValue(Object o) {
		if (o instanceof Number) {
			Number mm = (Number) o;
			double i = mm.doubleValue() / 25.4;
			return new Double(i);
		}
		else throwIllegalArgumentException("reverseTransformedValue");
		return null;
	}

}
