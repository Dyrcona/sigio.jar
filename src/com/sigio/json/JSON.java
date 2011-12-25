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

public class JSON {
	/*
	 * JSON Character Constants based on RFC4627.
	 */
	public static final int BEGIN_ARRAY = '[';
	public static final int END_ARRAY = ']';
	public static final int BEGIN_OBJECT = '{';
	public static final int END_OBJECT = '}';
	public static final int QUOTE_CHAR = '"';
	public static final int NAME_SEPARATOR = ':';
	public static final int VALUE_SEPARATOR = ',';
	public static final int[] WHITESPACE = { ' ', '\t', '\r', '\n' };
	public static final int ESCAPE_CHAR = '\\';

}
