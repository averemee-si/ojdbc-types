/**
 * Copyright (c) 2018-present, A2 Rešitve d.o.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package solutions.a2.oracle.utils;

/**
 * 
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 *
 */

public class FormattingUtils {

	public static void leftPad(final StringBuilder sb, final short value, final int length) {
		leftPad(sb, Short.toUnsignedInt(value), length);
	}

	public static void leftPad(final StringBuilder sb, final int value, final int length) {
		final String s = Integer.toUnsignedString(value, 0x10);
		for (int i = 0; i < length - s.length(); i++) {
			sb.append('0');
		}
		sb.append(s);
	}

	public static void leftPad(final StringBuilder sb, final long value, final int length) {
		final String s = Long.toUnsignedString(value, 0x10);
		for (int i = 0; i < length - s.length(); i++) {
			sb.append('0');
		}
		sb.append(s);
	}

	public static String leftPad(final short value, final int length) {
		return leftPad(Short.toUnsignedInt(value), length);
	}

	public static String leftPad(final int value, final int length) {
		final StringBuilder sb = new StringBuilder();
		leftPad(sb, value, length);
		return sb.toString();
	}

	public static String leftPad(final long value, final int length) {
		final StringBuilder sb = new StringBuilder(18);
		leftPad(sb, value, length);
		return sb.toString();
	}

}
