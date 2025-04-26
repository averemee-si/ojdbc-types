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

	static final char[] HEX_CHARS_UPPER = new char[]
			{0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46};

	/**
	 * 
	 * Converts {@code hex} containing hexadecimal digits to a byte array.
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexToRaw(final String hex) {
		final int len = hex.length();
		final byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) +
									Character.digit(hex.charAt(i+1), 16));
		}
		return data;
	}

	/**
	 *
	 * Converts {@code raw} to a character value containing its hexadecimal representation.
	 * 
	 * @param raw
	 * @return
	 */
	public static String rawToHex(final byte[] raw) {
		final char[] data = new char[raw.length * 2];
		for (int i = 0; i < raw.length; i++) {
			data[i << 1] = HEX_CHARS_UPPER[(raw[i] >> 4) & 0xF];
			data[(i << 1) + 1] = HEX_CHARS_UPPER[(raw[i] & 0xF)];
		}
		return new String(data);
	}

	/**
	 * 
	 * Converts byte array to unsigned int
	 * 
	 * @param buffer
	 * @param offset
	 * @return
	 */
	public static int getU32BE(final byte[] buffer, final int offset) {
		return
				Byte.toUnsignedInt(buffer[offset])     << 24           |
				Byte.toUnsignedInt(buffer[offset + 1]) << 16           |
				Byte.toUnsignedInt(buffer[offset + 2]) << 8            |
				Byte.toUnsignedInt(buffer[offset + 3]);
	}

	/**
	 * 
	 * Converts byte array to unsigned int
	 * 
	 * @param buffer
	 * @param offset
	 * @return
	 */
	public static int getU24BE(final byte[] buffer, final int offset) {
		return
				Byte.toUnsignedInt(buffer[offset])     << 16           |
				Byte.toUnsignedInt(buffer[offset + 1]) << 8            |
				Byte.toUnsignedInt(buffer[offset + 2]);
	}

	/**
	 * 
	 * Converts byte array to unsigned short
	 * 
	 * @param buffer
	 * @param offset
	 * @return
	 */
	public static short getU16BE(final byte[] buffer, final int offset) {
		return
				(short) (Byte.toUnsignedInt(buffer[offset]) << 8	|
						Byte.toUnsignedInt(buffer[offset + 1]));
	}

}
