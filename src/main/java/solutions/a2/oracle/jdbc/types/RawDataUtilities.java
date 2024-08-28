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

package solutions.a2.oracle.jdbc.types;

class RawDataUtilities {

	static final int ORA_INTERVAL_OFFSET = 60;

	static int decodeOraBytes(final byte[] array, final int msb) {
		return
			Byte.toUnsignedInt(array[msb + 3]) | Byte.toUnsignedInt(array[msb + 2]) << 8 |
			Byte.toUnsignedInt(array[msb + 1]) << 16 | Byte.toUnsignedInt(array[msb]) << 24;
	}

	static int decodeOraBytes(final int[] array, final int msb) {
		return
			array[msb + 3] | array[msb + 2] << 8 | array[msb + 1] << 16 | array[msb] << 24;
	}

	static int decodeOraBytes4I(final byte[] array, final int msb) {
		return -(Integer.MIN_VALUE - decodeOraBytes(array, msb));
	}

	static int decodeOraBytes4I(final int[] array, final int msb) {
		return -(Integer.MIN_VALUE - decodeOraBytes(array, msb));
	}

	static int getHighOrderBits(final int value) {
		return (value & 0x7F) << 6;
	}

	static int getLowOrderBits(final int value) {
		return (value & 0xFC) >> 2;
	}

	static int setHighOrderBits(final int value) {
		return (value & 0x1FC0) >> 6;
	}

	static int setLowOrderBits(final int value) {
		return (value & 0x3F) << 2;
	}

}
