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
 * A value transformer to convert yard values to meters and vice
 * versa.
 */
public class YardToMeterValueTransformer extends NumberToDoubleValueTransformer {

	/**
	 * {@inheritDoc}
	 *
	 * @return always returns <code>true</code>
	 */
	public boolean allowsReverseTransformation() { return true; }

	/**
	 * Transforms a Number instance with an assumed value in yard to a
	 * Double with a value in meters.
	 *
	 * @param o <cod>java.lang.Number</code> to be transformed
	 * @return <code>java.lang.Double</code> with the value o
	 * divided by 1.0936
	 * @throws IllegalArgumentException {@inheritDoc}
	 */
	public Object transformedValue(Object o) {
		if (o instanceof Number) {
			Number y = (Number) o;
			double m = y.doubleValue() / 1.0936;
			return new Double(m);
		}
		else throwIllegalArgumentException("transformedValue");
		return null;
	}

	/**
	 * Transforms a Number instance with an assumed value in
	 * meters to a Double with a value in yards.
	 *
	 * @param o number to be transformed
	 * @return <code>java.lang.Double</code> with the value of o
	 * multiplied by 1.0936
	 * @throws IllegalArgumentException if o is not an instance of
	 * <code>java.lang.Number</code>
	 */
	public Object reverseTransformedValue(Object o) {
		if (o instanceof Number) {
			Number m = (Number) o;
			double y = m.doubleValue() * 1.0936;
			return new Double(y);
		}
		else throwIllegalArgumentException("reverseTransformedValue");
		return null;
	}

}
