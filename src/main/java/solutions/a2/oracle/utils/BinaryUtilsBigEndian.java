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

import solutions.a2.oracle.jdbc.types.UnsignedLong;

/**
 * 
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 *
 */

public class BinaryUtilsBigEndian implements BinaryUtils {
	
	@Override
	public long getScn(final byte[] buffer, final int offset) {
		if (buffer[offset] == FF_BYTE &&
				buffer[offset + 1] == FF_BYTE &&
				buffer[offset + 2] == FF_BYTE &&
				buffer[offset + 3] == FF_BYTE &&
				buffer[offset + 4] == FF_BYTE &&
				buffer[offset + 5] == FF_BYTE) {
			return UnsignedLong.MAX_VALUE;
		} else {
			if ((buffer[offset + 4] & 0x80) == 0x80) {
				return
						Byte.toUnsignedLong(buffer[offset + 3])        |
						Byte.toUnsignedLong(buffer[offset + 2]) << 8   |
						Byte.toUnsignedLong(buffer[offset + 1]) << 16  |
						Byte.toUnsignedLong(buffer[offset])     << 24  |
						Byte.toUnsignedLong(buffer[offset + 7]) << 32  |
						Byte.toUnsignedLong(buffer[offset + 6]) << 40  |
						Byte.toUnsignedLong(buffer[offset + 5]) << 48  |
						Byte.toUnsignedLong((byte)(buffer[offset + 4] & 0x7F)) << 56;
			} else {
				return
						Byte.toUnsignedLong(buffer[offset + 3])        |
						Byte.toUnsignedLong(buffer[offset + 2]) << 8   |
						Byte.toUnsignedLong(buffer[offset + 1]) << 16  |
						Byte.toUnsignedLong(buffer[offset])     << 24  |
						Byte.toUnsignedLong(buffer[offset + 5]) << 32  |
						Byte.toUnsignedLong(buffer[offset + 4]) << 40;
			}
		}
	}
	
	@Override
	public long getScn4Record(final byte[] buffer, final int offset) {
		if (buffer[offset] == FF_BYTE &&
				buffer[offset + 1] == FF_BYTE &&
				buffer[offset + 2] == FF_BYTE &&
				buffer[offset + 3] == FF_BYTE &&
				buffer[offset + 4] == FF_BYTE &&
				buffer[offset + 5] == FF_BYTE) {
			return UnsignedLong.MAX_VALUE;
		} else {
			if ((buffer[offset] & 0x80) == 0x80) {
				return
						Byte.toUnsignedLong(buffer[offset + 5])        |
						Byte.toUnsignedLong(buffer[offset + 4]) << 8   |
						Byte.toUnsignedLong(buffer[offset + 3]) << 16  |
						Byte.toUnsignedLong(buffer[offset + 2]) << 24  |
						Byte.toUnsignedLong(buffer[offset + 1]) << 48  |
						Byte.toUnsignedLong((byte)(buffer[offset] & 0x7F)) << 56;
			} else {
				return
						Byte.toUnsignedLong(buffer[offset + 5])        |
						Byte.toUnsignedLong(buffer[offset + 4]) << 8   |
						Byte.toUnsignedLong(buffer[offset + 3]) << 16  |
						Byte.toUnsignedLong(buffer[offset + 2]) << 24  |
						Byte.toUnsignedLong(buffer[offset + 1]) << 32  |
						Byte.toUnsignedLong(buffer[offset]) << 40;
			}
		} 
	}
	
	@Override
	public int getU32(final byte[] buffer, final int offset) {
		return
				Byte.toUnsignedInt(buffer[offset])     << 24           |
				Byte.toUnsignedInt(buffer[offset + 1]) << 16           |
				Byte.toUnsignedInt(buffer[offset + 2]) << 8            |
				Byte.toUnsignedInt(buffer[offset + 3]);
	}
	
	@Override
	public short getU16(final byte[] buffer, final int offset) {
		return (short) (
				Byte.toUnsignedInt(buffer[offset])     << 8            |
				Byte.toUnsignedInt(buffer[offset + 1]));
	}
	
	@Override
	public short getU16Special(final byte[] buffer, final int offset) {
		return (short) (
				Byte.toUnsignedInt((byte)(buffer[offset] & 0x7F)) << 8 |
				Byte.toUnsignedInt(buffer[offset + 1]));
	}
	
	@Override
	public boolean isLittleEndian() {
		return false;
	}

}
