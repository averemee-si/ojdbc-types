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

import java.nio.ByteOrder;
import java.time.LocalDateTime;

/**
 * 
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 *
 */

public interface BinaryUtils {

	static final byte FF_BYTE = (byte) 0xFF;

	long getScn(final byte[] buffer, final int offset);
	long getScn4Record(final byte[] buffer, final int offset);
	long getU56(final byte[] buffer, final int offset);
	int getU32(final byte[] buffer, final int offset);
	short getU16(final byte[] buffer, final int offset);
	short getU16Special(final byte[] buffer, final int offset);
	boolean isLittleEndian();

	public static LocalDateTime parseTimestamp(final int encoded) {
		long t = Integer.toUnsignedLong(encoded);
		final int seconds = (int) (t % 60);
		t = (t - seconds)/60;
		final int minutes = (int) (t % 60);
		t = (t - minutes)/60;
		final int hours = (int) (t % 24);
		t = (t - hours)/24;
		final int day = (int) (t % 31);
		t = (t - day)/31;
		final int month = (int) (t % 12);
		t = (t - month)/12;
		int year = (int) (t + 1988);
		return LocalDateTime.of(year, (month + 1), (day + 1), hours, minutes, seconds);
	}

	public static BinaryUtils get(final ByteOrder byteOrder) {
		if (byteOrder.equals(ByteOrder.LITTLE_ENDIAN)) {
			return new BinaryUtilsLittleEndian();
		} else {
			return new BinaryUtilsBigEndian();
		}
	}

	public static BinaryUtils get(final boolean littleEndian) {
		if (littleEndian) {
			return new BinaryUtilsLittleEndian();
		} else {
			return new BinaryUtilsBigEndian();
		}
	}

	/**
	 * 
	 * Returns the start position of the first occurrence of the specified {@code search} within
	 * {@code array} starting from position {@code start}, or {@code -1} if there is no such occurrence.
	 * 
	 * @param array  the array to search for the sequence {@code search}
	 * @param start  the position of {@code array} to start search
	 * @param search the array to search for as a sub-sequence of {@code array}
	 * @return
	 */
	public static int indexOf(byte[] array, int start, byte[] search) {
		if (start >= array.length) {
			return -1;
		} else {
			loop:
			for (int index = start; index < array.length - search.length + 1; index++) {
				for (int i = 0; i < search.length; i++) {
					if (array[index + i] != search[i]) {
						continue loop;
					}
				}
				return index;
			}
			return -1;
		}
	}

	/**
	 * 
	 * Returns the start position of the first occurrence of the specified {@code search} within
	 * {@code array}, or {@code -1} if there is no such occurrence.
	 * 
	 * @param array  the array to search for the sequence {@code search}
	 * @param search the array to search for as a sub-sequence of {@code array}
	 */
	public static int indexOf(byte[] array, byte[] search) {
		return indexOf(array, 0, search);
	}

	/**
	 * 
	 * Returns the start position of the first occurrence of the specified {@code search} within
	 * {@code array} starting from position {@code start}, or {@code -1} if there is no such occurrence.
	 * 
	 * @param array  the array to search for the sequence {@code search}
	 * @param start  the position of {@code array} to start search
	 * @param search the array to search for as a sub-sequence of {@code array}
	 * @return
	 */
	public static int indexOf(byte[] array, int start, byte search) {
		if (start >= array.length) {
			return -1;
		} else {
			int index;
			boolean found = false;
			for (index = start; index < array.length; index++) {
				if (array[index] == search) {
					found = true;
					break;
				}
			}
			return found ? index : -1;
		}
	}

	/**
	 * 
	 * Returns the start position of the first occurrence of the specified {@code search} within
	 * {@code array}, or {@code -1} if there is no such occurrence.
	 * 
	 * @param array  the array to search for the sequence {@code search}
	 * @param search the array to search for as a sub-sequence of {@code array}
	 */
	public static int indexOf(byte[] array, byte search) {
		return indexOf(array, 0, search);
	}

}
