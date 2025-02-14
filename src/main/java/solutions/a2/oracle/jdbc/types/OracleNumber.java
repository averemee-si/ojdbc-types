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


/**
 * 
 * oracle.sql.NUMBER conversions
 * 
 */
public class OracleNumber {

	public static final byte[] LONG_MAX_VALUE = {(byte) 0xca, 0x0a, 0x17, 0x22, 0x49, 0x04, 0x45, 0x37, 0x4e, 0x3b, 0x08};
	public static final byte[] LONG_MIN_VALUE = {0x35, 0x5c, 0x4f, 0x44, 0x1d, 0x62, 0x21, 0x2f, 0x18, 0x2b, 0x5d, 0x66};
	public static final byte[] INT_MAX_VALUE = {(byte) 0xc5, 0x16, 0x30, 0x31, 0x25, 0x30};
	public static final byte[] INT_MIN_VALUE = {0x3a, 0x50, 0x36, 0x35, 0x41, 0x35, 0x66};
	public static final byte[] SHORT_MAX_VALUE = {(byte) 0xc3, 0x04, 0x1c, 0x44};
	public static final byte[] SHORT_MIN_VALUE = {0x3c, 0x62, 0x4a, 0x21, 0x66};
	public static final byte[] BYTE_MAX_VALUE = {(byte) 0xc2, 0x02, 0x1c};
	public static final byte[] BYTE_MIN_VALUE = {0x3d, 0x64, 0x49, 0x66};

	private static final byte ZERO_BYTE = -128;

	/**
	 * Converts an oracle.sql.NUMBER represented by byte array into a Java long. 
	 * 
	 * @param oraNumber - oracle.sql.NUMBER in byte array format
	 * @return a Java long value
	 */
	public static long toLong(final byte[] oraNumber) {
		return toLong(oraNumber, 0, oraNumber.length);
	}

	/**
	 * Converts an oracle.sql.NUMBER represented by byte array into a Java long. 
	 * 
	 * @param oraNumber - the byte array containing oracle.sql.NUMBER
	 * @param offset - the index of the first byte of oracle.sql.NUMBER
	 * @param length - the number of bytes representing oracle.sql.NUMBER
	 * @return a Java long value
	 */
	public static long toLong(final byte[] oraNumber, final int offset, final int length) {
		if (zero(oraNumber, offset, length)) {
			return 0;
		} else {
			return recalcNumber(oraNumber, offset, length);
		}
	}

	/**
	 * Converts an oracle.sql.NUMBER represented by byte array into a Java int. 
	 * 
	 * @param oraNumber - oracle.sql.NUMBER in byte array format
	 * @return a Java int value
	 */
	public static int toInt(final byte[] oraNumber) {
		return toInt(oraNumber, 0, oraNumber.length);
	}

	/**
	 * Converts an oracle.sql.NUMBER represented by byte array into a Java int. 
	 * 
	 * @param oraNumber - the byte array containing oracle.sql.NUMBER
	 * @param offset - the index of the first byte of oracle.sql.NUMBER
	 * @param length - the number of bytes representing oracle.sql.NUMBER
	 * @return a Java long value
	 */
	public static int toInt(final byte[] oraNumber, final int offset, final int length) {
		if (zero(oraNumber, offset, length)) {
			return 0;
		} else {
			return (int) recalcNumber(oraNumber, offset, length);
		}
	}

	/**
	 * Converts an oracle.sql.NUMBER represented by byte array into a Java short. 
	 * 
	 * @param oraNumber - oracle.sql.NUMBER in byte array format
	 * @return a Java short value
	 */
	public static short toShort(final byte[] oraNumber) {
		return toShort(oraNumber, 0, oraNumber.length);
	}

	/**
	 * Converts an oracle.sql.NUMBER represented by byte array into a Java short. 
	 * 
	 * @param oraNumber - the byte array containing oracle.sql.NUMBER
	 * @param offset - the index of the first byte of oracle.sql.NUMBER
	 * @param length - the number of bytes representing oracle.sql.NUMBER
	 * @return a Java short value
	 */
	public static short toShort(final byte[] oraNumber, final int offset, final int length) {
		if (zero(oraNumber, offset, length)) {
			return 0;
		} else {
			return (short) recalcNumber(oraNumber, offset, length);
		}
	}

	/**
	 * Converts an oracle.sql.NUMBER represented by byte array into a Java byte. 
	 * 
	 * @param oraNumber - oracle.sql.NUMBER in byte array format
	 * @return a Java byte value
	 */
	public static byte toByte(final byte[] oraNumber) {
		return toByte(oraNumber, 0, oraNumber.length);
	}

	/**
	 * Converts an oracle.sql.NUMBER represented by byte array into a Java byte. 
	 * 
	 * @param oraNumber - the byte array containing oracle.sql.NUMBER
	 * @param offset - the index of the first byte of oracle.sql.NUMBER
	 * @param length - the number of bytes representing oracle.sql.NUMBER
	 * @return a Java byte value
	 */
	public static byte toByte(final byte[] oraNumber, final int offset, final int length) {
		if (zero(oraNumber, offset, length)) {
			return 0;
		} else {
			return (byte) recalcNumber(oraNumber, offset, length);
		}
	}

	/**
	 * Converts an oracle.sql.NUMBER represented by byte array into a Java double. 
	 * 
	 * @param oraNumber - oracle.sql.NUMBER in byte array format
	 * @return a Java double value
	 */
	public static double toDouble(final byte[] oraNumber) {
		return toDouble(oraNumber, 0, oraNumber.length);
	}

	/**
	 * Converts an oracle.sql.NUMBER represented by byte array into a Java double. 
	 * 
	 * @param oraNumber - the byte array containing oracle.sql.NUMBER
	 * @param offset - the index of the first byte of oracle.sql.NUMBER
	 * @param length - the number of bytes representing oracle.sql.NUMBER
	 * @return a Java double value
	 */
	public static double toDouble(final byte[] oraNumber, final int offset, final int length) {
		if (zero(oraNumber, offset, length)) {
			return 0.0d;
		} else if (posInfinity(oraNumber, offset, length)) {
			return Double.POSITIVE_INFINITY;
		} else if (negInfinity(oraNumber, offset, length)) {
			return Double.NEGATIVE_INFINITY;
		} else {
			return Double.parseDouble(decimalString(oraNumber, offset, length));
		}
	}

	/**
	 * Converts an oracle.sql.NUMBER represented by byte array into a Java float. 
	 * 
	 * @param oraNumber - oracle.sql.NUMBER in byte array format
	 * @return a Java float value
	 */
	public static float toFloat(final byte[] oraNumber) {
		return toFloat(oraNumber, 0, oraNumber.length);
	}

	/**
	 * Converts an oracle.sql.NUMBER represented by byte array into a Java float. 
	 * 
	 * @param oraNumber - the byte array containing oracle.sql.NUMBER
	 * @param offset - the index of the first byte of oracle.sql.NUMBER
	 * @param length - the number of bytes representing oracle.sql.NUMBER
	 * @return a Java float value
	 */
	public static float toFloat(final byte[] oraNumber, final int offset, final int length) {
		if (zero(oraNumber, offset, length)) {
			return 0.0f;
		} else if (posInfinity(oraNumber, offset, length)) {
			return Float.POSITIVE_INFINITY;
		} else if (negInfinity(oraNumber, offset, length)) {
			return Float.NEGATIVE_INFINITY;
		} else {
			return Float.parseFloat(decimalString(oraNumber, offset, length));
		}
	}

	/**
	 * 
	 * Compares two byte arrays representing oracle.sql.NUMBER
	 * 
	 * @param x - the first byte array to compare
	 * @param y - the second byte array to compare
	 * @return    the value {@code 0} if {@code x == y};
	 *            a value less than {@code 0} if {@code x < y};
	 *            and a value greater than {@code 0} if {@code x > y}
	 */
	public static int compare(final byte[] x, final byte[] y) {
		final int lengthX = x.length;
		final int lengthY = y.length;
		int i = 0;
		while (i < Math.min(lengthX, lengthY)) {
			final int compare = Byte.compareUnsigned(x[i], y[i]);
			if (compare == 0)
				i++;
			else
				return compare;
		}
		if (lengthX == lengthY)
			return 0;
		else if (lengthX > lengthY)
			return 1;
		else
			return -1;
	}

	private static String decimalString(final byte[] oraNumber, final int offset, final int length) {
		final StringBuilder sb = new StringBuilder(0x30);
		int exponent = 0;
		if (oraNumber[offset + length - 1] != 0x66) {
			exponent = Byte.toUnsignedInt(oraNumber[offset]) - 0xC1;
			sb.append('.');
			for (int i = 1; i < length; i++) {
				addDecimalDigits(sb, Byte.toUnsignedInt(oraNumber[offset + i]) - 1);
			}
		} else {
			exponent = 0x3E - Byte.toUnsignedInt(oraNumber[offset]);
			sb.append("-.");
			for (int i = 1; i < length - 1; i++) {
				addDecimalDigits(sb, 0x65 - Byte.toUnsignedInt(oraNumber[offset + i]));
			}
		}
		exponent = exponent * 2 + 2;
		sb
			.append('E')
			.append(exponent);
		return new String(sb);
	}

	private static void addDecimalDigits(final StringBuilder sb, final int n) {
		final int quotient = n / 10;
		final int remainder = n % 10;
		sb.append((char)(quotient + 0x30));
		sb.append((char)(remainder + 0x30));
	}

	private static boolean zero(final byte[] oraNumber, final int offset, final int length) {
		return (length == 1 && oraNumber[offset] == ZERO_BYTE);
	}

	private static boolean infinity(final byte[] oraNumber, final int offset, final int length) {
		return posInfinity(oraNumber, offset, length) ||
				negInfinity(oraNumber, offset, length);
	}

	private static boolean posInfinity(final byte[] oraNumber, final int offset, final int length) {
		return length == 2 && oraNumber[offset] == -1  && oraNumber[offset + 1] == 0x65;
	}

	private static boolean negInfinity(final byte[] oraNumber, final int offset, final int length) {
		return length == 1 && oraNumber[offset] == 0;
	}


	private static long recalcNumber(final byte[] oraNumber, final int offset, final int length) {
		if (infinity(oraNumber, offset, length))
			throw new ArithmeticException("The passed value is infinity!");
		final boolean positive = (oraNumber[offset] & ZERO_BYTE) != 0;
		long result = 0L;
		final byte[] exponentAndMantissa;
		if (positive) {
			exponentAndMantissa = new byte[length];
			// To calculate the decimal exponent, add 65 to the base-100 exponent
			//  and add another 128 if the number is positive.
			exponentAndMantissa[0] = (byte)((oraNumber[offset] & 0xFFFFFF7F) - 0x41);
			for (int i = 1; i < exponentAndMantissa.length; i++) {
				exponentAndMantissa[i] = (byte)(oraNumber[offset + i] - 1);
			}
		} else {
			if (length - 1 == 0x14 && oraNumber[offset + length - 1] != 0x66) {
				exponentAndMantissa = new byte[length];
			} else {
				exponentAndMantissa = new byte[length - 1];
			}
			exponentAndMantissa[0] = (byte)((~oraNumber[offset] & 0xFFFFFF7F) - 0x41);
			for (int i = 1; i < exponentAndMantissa.length; ++i) {
				exponentAndMantissa[i] = (byte)(0x65 - oraNumber[offset + i]);
			}
		}

		final byte exponent = exponentAndMantissa[0];
		final byte size = (byte)(exponentAndMantissa.length - 1);
		for (int j = (size > exponent + 1) ? (exponent + 1) : size, i = 0; i < j; ++i) {
			result = result * 100L + exponentAndMantissa[i + 1];
		}
		for (int k = exponent - size; k >= 0; --k) {
			result *= 100L;
		}
		return positive ? result : (-result);
	}

}
