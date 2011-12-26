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

import java.text.Normalizer;

/**
 * Class to output strings as valid JSON strings. It exists mainly for
 * the static, to JSONString method.
 */
public class JSONStringAdapter {

	private static char[] quote_seq = { JSON.ESCAPE_CHAR, JSON.QUOTE_CHAR };
	private static char[] backspace_seq = { JSON.ESCAPE_CHAR, 'b' };
	private static char[] tab_seq = { JSON.ESCAPE_CHAR, 't' };
	private static char[] nl_seq = { JSON.ESCAPE_CHAR, 'n' };
	private static char[] ff_seq = { JSON.ESCAPE_CHAR, 'f' };
	private static char[] cr_seq = { JSON.ESCAPE_CHAR, 'r' };
	private static char[] solidus_seq = { JSON.ESCAPE_CHAR, '/' };

	/**
	 * Converts content of a java.lang.String to a format suitable for
	 * JSON.
	 */
	public static String toJSONString(String str) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < str.length(); i++) {
			int c = str.codePointAt(i);
			switch (c) {
			case 0x0008:
				sb.append(JSONStringAdapter.backspace_seq);
				break;
			case 0x0009:
				sb.append(JSONStringAdapter.tab_seq);
				break;
			case 0x000A:
				sb.append(JSONStringAdapter.nl_seq);
				break;
			case 0x000C:
				sb.append(JSONStringAdapter.ff_seq);
				break;
			case 0x000D:
				sb.append(JSONStringAdapter.cr_seq);
				break;
			case 0x002F:
				sb.append(JSONStringAdapter.solidus_seq);
				break;
			case 0x005C:
				sb.append(JSON.ESCAPE_CHAR);
				break;
			case JSON.QUOTE_CHAR:
				sb.append(JSONStringAdapter.quote_seq);
				break;
			default:
				if (c >= 0x0020)
					sb.append((char) c);
				break;	
			}
		}

		// Surround the string with quotes:
		sb.insert(0, JSON.QUOTE_CHAR);
		sb.append(JSON.QUOTE_CHAR);

		// Canonical Decomposition of Unicode (NFD).
		String string = Normalizer.normalize(sb.toString(), Normalizer.Form.NFD);

		return string;
	}

	// Private constructor, because we don't ever need one.
	private JSONStringAdapter() { super(); }
}
