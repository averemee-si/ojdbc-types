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
}
