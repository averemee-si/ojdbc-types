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

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;


/**
 * Oracle <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> to Java Classes Conversions
 *     For Oracle format description please see <a href="https://support.oracle.com/rs?type=doc&id=69028.1">How does Oracle store the DATE datatype internally? (Doc ID 69028.1)</a>
 * Oracle Type 12 - DATE
 */
public class UnsignedLong implements Serializable {

	public static final String MAX_64_BIT_AS_DECIMAL_STRING = "18446744073709551615";
	public static final long MAX_VALUE = Long.parseUnsignedLong(MAX_64_BIT_AS_DECIMAL_STRING);
	public static final long MIN_VALUE = 0L;

	private static final long serialVersionUID = -964283654772800228L;
	private static final byte ZERO_BYTE = -128;

	private final long unsignedLong;
	private final byte[] oraNumber;


	/**
	 * Creates UnsignedLong from a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/NUMBER.html">NUMBER</a>
	 * 
	 * @param oraNumber a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/NUMBER.html">NUMBER</a> object
	 * @throws SQLException if the byte array represents negative value
	 */
	public UnsignedLong(final byte[] oraNumber) throws SQLException {
		if (oraNumber.length == 1 && oraNumber[0] == ZERO_BYTE) {
			this.unsignedLong = 0;
			this.oraNumber = oraNumber;
		} else {
			final int length = oraNumber.length;
			final byte[] exponentAndMantissa;
			// The high-order bit of the exponent byte is the sign bit;
			// it is set for positive numbers and it is cleared for negative numbers
			if ((oraNumber[0] & 0xFFFFFF80) != 0) {
				exponentAndMantissa = new byte[length];
				// To calculate the decimal exponent, add 65 to the base-100 exponent
				//  and add another 128 if the number is positive.
				exponentAndMantissa[0] = (byte)((oraNumber[0] & 0xFFFFFF7F) - 65);
				for (int i = 1; i < length; ++i) {
					exponentAndMantissa[i] = (byte)(oraNumber[i] - 1);
				}
				this.unsignedLong = recalcNumber(exponentAndMantissa);
				this.oraNumber = oraNumber;
			} else {
				throw new SQLException("Negative NUMBER passed!");
			}
		}
	}

	/**
	 * Creates UnsignedLong from a long representing unsigned value
	 * 
	 * @param unsignedLong
	 * @throws SQLException
	 */
	public UnsignedLong(final long unsignedLong) throws SQLException {
		if (unsignedLong == 0) {
			this.unsignedLong = unsignedLong;
			this.oraNumber = new byte[] { ZERO_BYTE };
		} else {
			this.unsignedLong = unsignedLong;
			this.oraNumber = encodeUnsigned(unsignedLong);
		}
	}

	private long recalcNumber(final byte[] exponentAndMantissa) {
		long result = 0L;
		final byte exponent = exponentAndMantissa[0];
		final byte size = (byte)(exponentAndMantissa.length - 1);
		for (int j = (size > exponent + 1) ? (exponent + 1) : size, i = 0; i < j; ++i) {
			result = result * 100L + exponentAndMantissa[i + 1];
		}
		for (int k = exponent - size; k >= 0; --k) {
			result *= 100L;
		}
		return result;
	}


	/**
	 * Long representation of UnsignedLong
	 * 
	 * @return Long representation of UnsignedLong
	 */
	public long toLong() {
		return unsignedLong;
	}

	/**
	 * <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/Datum.html">oracle.sql.Datum</a> representation of UnsignedLong
	 * 
	 * @return <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/Datum.html">oracle.sql.Datum</a> representation of UnsignedLong
	 */
	public byte[] toByteArray() {
		return oraNumber;
	}


	/**
	 * 
	 * @return {@link java.math.BigInteger BigInteger} representing this UnsignedLong object
	 */
	public BigInteger toBigInteger() {
		if (unsignedLong >= 0L) {
			return BigInteger.valueOf(unsignedLong);
		} else {
			int upper = (int) (unsignedLong >>> 32);
			int lower = (int) unsignedLong;
			// return (upper << 32) + lower
			return (BigInteger
						.valueOf(Integer.toUnsignedLong(upper)))
						.shiftLeft(32)
						.add(BigInteger.valueOf(Integer.toUnsignedLong(lower)));
		}
	}


	@Override
	public String toString() {
		return Long.toUnsignedString(unsignedLong);
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/NUMBER.html">NUMBER</a> to a decimal string
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/NUMBER.html">NUMBER</a> object
	 * @return String representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/NUMBER.html">NUMBER</a> object
	 * @throws SQLException
	 */
	public static String toString(final byte[] value) throws SQLException {
		return (new UnsignedLong(value)).toString();
	}

	/**
	 * Retrieves the value of the designated column in the current row of the given ResultSet object as a UnsignedLong 
	 * 
	 * @param resultSet    {@link java.sql.ResultSet ResultSet} ResultSet to read data
	 * @param columnLabel  the label for the column specified with the SQL AS clause. If the SQL AS clause was not specified, then the label is the name of the column
	 * @return
	 * @throws SQLException
	 */
	public static UnsignedLong getFromResultSet(final ResultSet resultSet, final String columnLabel) throws SQLException {
		final byte[] ba = resultSet.getBytes(columnLabel);
		if (resultSet.wasNull()) {
			return null;
		} else {
			return new UnsignedLong(ba);
		}
	}

	/**
	 * Retrieves the value of the designated column in the current row of the given ResultSet object as a UnsignedLong 
	 * 
	 * @param resultSet    {@link java.sql.ResultSet ResultSet} ResultSet to read data
	 * @param columnIndex  the first column is 1, the second is 2, ...
	 * @return
	 * @throws SQLException
	 */
	public static UnsignedLong getFromResultSet(final ResultSet resultSet, final int columnIndex) throws SQLException {
		final byte[] ba = resultSet.getBytes(columnIndex);
		if (resultSet.wasNull()) {
			return null;
		} else {
			return new UnsignedLong(ba);
		}
	}

	/**
	 * Sets the value of the designated parameter using this object.
	 * 
	 * @param statement       {@link java.sql.PreparedStatement PreparedStatement} PreparedStatement to set parameter
	 * @param parameterIndex  the first parameter is 1, the second is 2, ...
	 * @throws SQLException
	 */
	public void setAsParameter(final PreparedStatement statement, final int parameterIndex) throws SQLException {
		statement.setObject(parameterIndex, this.toBigInteger(), Types.NUMERIC);
	}

	private byte[] encodeUnsigned(final long unsigned) {
		byte[] ora = null;
		if (Long.compareUnsigned(unsigned, 100) < 0) {
			ora = new byte[] { -63, (byte)(unsigned + 1) };
		} else if (Long.compareUnsigned(unsigned, 10_000) < 0) {
			final int remainder = (int) (unsigned % 100);
			if (remainder != 0) {
				ora = new byte[] { 0, 0, (byte)(remainder + 1) };
			} else {
				ora = new byte[2];
			}
			ora[0] = -62;
			ora[1] = (byte)(unsigned / 100 + 1);
		} else if (Long.compareUnsigned(unsigned, 1_000_000) < 0) {
			final int remainder = (int) (unsigned % 100);
			if (remainder != 0) {
				ora = new byte[] {
						0,
						0,
						(byte)(unsigned % 10_000 / 100 + 1),
						(byte)(remainder + 1) };
			} else {
				final int secondRemainder = (int) (unsigned % 10_000 / 100);
				if (secondRemainder != 0) {
					ora = new byte[] { 0, 0, (byte)(secondRemainder + 1) };
				} else {
					ora = new byte[2];
				}
			}
			ora[0] = -61;
			ora[1] = (byte)(unsigned / 10_000 + 1);
		} else if (Long.compareUnsigned(unsigned, 100_000_000) < 0) {
			final int remainder = (int) (unsigned % 100);
			if (remainder != 0) {
				ora = new byte[] {
						0,
						0,
						(byte)(unsigned % 1_000_000 / 10_000 + 1),
						(byte)(unsigned % 10_000 / 100 + 1),
						(byte)(remainder + 1) };
			} else {
				final int secondRemainder = (int) (unsigned % 10_000 / 100);
				if (secondRemainder != 0) {
					ora = new byte[] {
							0,
							0,
							(byte)(unsigned % 1_000_000 / 10_000 + 1),
							(byte)(secondRemainder + 1) };
				} else {
					final int thirdRemainder = (int) (unsigned % 1_000_000 / 10_000);
					if (thirdRemainder != 0) {
						ora = new byte[] { 0, 0, (byte)(thirdRemainder + 1) };
					} else {
						ora = new byte[2];
					}					
				}
			}
			ora[0] = -60;
			ora[1] = (byte)(unsigned / 1_000_000 + 1);
		} else if (Long.compareUnsigned(unsigned, 10_000_000_000L) < 0) {
			final int remainder = (int) (unsigned % 100);
			if (remainder != 0) {
				ora = new byte[] {
						0,
						0,
						(byte)(unsigned % 100_000_000 / 1_000_000 + 1),
						(byte)(unsigned % 1_000_000 / 10_000 + 1),
						(byte)(unsigned % 10_000 / 100 + 1),
						(byte)(remainder + 1) };
			} else {
				final int secondRemainder = (int) (unsigned % 10_000 / 100);
				if (secondRemainder != 0) {
					ora = new byte[] {
							0,
							0,
							(byte)(unsigned % 100_000_000 / 1_000_000 + 1),
							(byte)(unsigned % 1_000_000 / 10_000 + 1),
							(byte)(secondRemainder + 1) };
				} else {
					final int thirdRemainder = (int) (unsigned % 1_000_000 / 10_000);
					if (thirdRemainder != 0) {
						ora = new byte[] {
								0,
								0,
								(byte)(unsigned % 100_000_000 / 1_000_000 + 1),
								(byte)(thirdRemainder + 1) };
					} else {
						final int fourthRemainder = (int) (unsigned % 100_000_000 / 1_000_000);
						if (fourthRemainder != 0) {
							ora = new byte[] {
									0,
									0,
									(byte)(fourthRemainder + 1) };
						} else {
							ora = new byte[2];
						}
					}
				}
			}
			ora[0] = -59;
			ora[1] = (byte)(unsigned / 100_000_000 + 1);
		} else if (Long.compareUnsigned(unsigned, 1_000_000_000_000L) < 0) {
			final int remainder = (int) (unsigned % 100);
			if (remainder != 0) {
				ora = new byte[] {
						0,
						0,
						(byte)(unsigned % 10_000_000_000L / 100_000_000 + 1),
						(byte)(unsigned % 100_000_000 / 1_000_000 + 1),
						(byte)(unsigned % 1_000_000 / 10_000 + 1),
						(byte)(unsigned % 10_000 / 100 + 1),
						(byte)(remainder + 1) };
			} else {
				final int secondRemainder = (int) (unsigned % 10_000 / 100);
				if (secondRemainder != 0) {
					ora = new byte[] {
							0,
							0,
							(byte)(unsigned % 10_000_000_000L / 100_000_000 + 1),
							(byte)(unsigned % 100_000_000 / 1_000_000 + 1),
							(byte)(unsigned % 1_000_000 / 10_000 + 1),
							(byte)(secondRemainder + 1) };
				} else {
					final int thirdRemainder = (int) (unsigned % 1_000_000 / 10_000);
					if (thirdRemainder != 0) {
						ora = new byte[] {
								0,
								0,
								(byte)(unsigned % 10_000_000_000L / 100_000_000 + 1),
								(byte)(unsigned % 100_000_000 / 1_000_000 + 1),
								(byte)(thirdRemainder + 1) };
					} else {
						final int fourthRemainder = (int) (unsigned % 100_000_000 / 1_000_000);
						if (fourthRemainder != 0) {
							ora = new byte[] {
									0,
									0,
									(byte)(unsigned % 10_000_000_000L / 100_000_000 + 1),
									(byte)(fourthRemainder + 1) };
						} else {
							final int fifthRemainder = (int) (unsigned % 10_000_000_000L / 100_000_000);
							if (fifthRemainder != 0) {
								ora = new byte[] { 0, 0, (byte)(fifthRemainder + 1) };
							} else {
								ora = new byte[2];
							}
						}
					}
				}
			}
			ora[0] = -58;
			ora[1] = (byte)(unsigned / 10_000_000_000L + 1);
		} else if (Long.compareUnsigned(unsigned, 100_000_000_000_000L) < 0) {
			final int remainder = (int) (unsigned % 100);
			if (remainder != 0) {
				ora = new byte[] {
						0,
						0,
						(byte)(unsigned % 1_000_000_000_000L / 10_000_000_000L + 1),
						(byte)(unsigned % 10_000_000_000L / 100_000_000 + 1),
						(byte)(unsigned % 100_000_000 / 1_000_000 + 1),
						(byte)(unsigned % 1_000_000 / 10_000 + 1),
						(byte)(unsigned % 10_000 / 100 + 1),
						(byte)(remainder + 1) };
			} else {
				final int secondRemainder = (int) (unsigned % 10_000 / 100);
				if (secondRemainder != 0) {
					ora = new byte[] {
							0,
							0,
							(byte)(unsigned % 1_000_000_000_000L / 10_000_000_000L + 1),
							(byte)(unsigned % 10_000_000_000L / 100_000_000 + 1),
							(byte)(unsigned % 100_000_000 / 1_000_000 + 1),
							(byte)(unsigned % 1_000_000 / 10_000 + 1),
							(byte)(secondRemainder + 1) };
				} else {
					final int thirdRemainder = (int) (unsigned % 1_000_000 / 10_000);
					if (thirdRemainder != 0) {
						ora = new byte[] {
								0,
								0,
								(byte)(unsigned % 1_000_000_000_000L / 10_000_000_000L + 1),
								(byte)(unsigned % 10_000_000_000L / 100_000_000 + 1),
								(byte)(unsigned % 100_000_000 / 1_000_000 + 1),
								(byte)(thirdRemainder + 1) };
					} else {
						final int fourthRemainder = (int) (unsigned % 100_000_000 / 1_000_000);
						if (fourthRemainder != 0) {
							ora = new byte[] {
									0,
									0,
									(byte)(unsigned % 1_000_000_000_000L / 10_000_000_000L + 1),
									(byte)(unsigned % 10_000_000_000L / 100_000_000 + 1),
									(byte)(fourthRemainder + 1) };
						} else {
							final int fifthRemainder = (int) (unsigned % 10_000_000_000L / 100_000_000);
							if (fifthRemainder != 0) {
								ora = new byte[] {
										0,
										0,
										(byte)(unsigned % 1_000_000_000_000L / 10_000_000_000L + 1),
										(byte)(fifthRemainder + 1) };
							} else {
								final int sixthRemainder = (int) (unsigned % 1_000_000_000_000L / 10_000_000_000L);
								if (sixthRemainder != 0) {
									ora = new byte[] { 0, 0, (byte)(sixthRemainder + 1) };
								} else {
									ora = new byte[2];
								}
							}
						}
					}
				}
			}
			ora[0] = -57;
			ora[1] = (byte)(unsigned / 1_000_000_000_000L + 1);
		} else if (Long.compareUnsigned(unsigned, 10_000_000_000_000_000L) < 0) {
			final int remainder = (int) (unsigned % 100);
			if (remainder != 0) {
				ora = new byte[] {
						0,
						0,
						(byte)(unsigned % 100_000_000_000_000L / 1_000_000_000_000L + 1),
						(byte)(unsigned % 1_000_000_000_000L / 10_000_000_000L + 1),
						(byte)(unsigned % 10_000_000_000L / 100_000_000 + 1),
						(byte)(unsigned % 100_000_000 / 1_000_000 + 1),
						(byte)(unsigned % 1_000_000 / 10_000 + 1),
						(byte)(unsigned % 10_000 / 100 + 1),
						(byte)(remainder + 1) };
			} else {
				final int secondRemainder = (int) (unsigned % 10_000 / 100);
				if (secondRemainder != 0) {
					ora = new byte[] {
							0,
							0,
							(byte)(unsigned % 100_000_000_000_000L / 1_000_000_000_000L + 1),
							(byte)(unsigned % 1_000_000_000_000L / 10_000_000_000L + 1),
							(byte)(unsigned % 10_000_000_000L / 100_000_000 + 1),
							(byte)(unsigned % 100_000_000 / 1_000_000 + 1),
							(byte)(unsigned % 1_000_000 / 10_000 + 1),
							(byte)(secondRemainder + 1) };
				} else {
					final int thirdRemainder = (int) (unsigned % 1_000_000 / 10_000);
					if (thirdRemainder != 0) {
						ora = new byte[] {
								0,
								0,
								(byte)(unsigned % 100_000_000_000_000L / 1_000_000_000_000L + 1),
								(byte)(unsigned % 1_000_000_000_000L / 10_000_000_000L + 1),
								(byte)(unsigned % 10_000_000_000L / 100_000_000 + 1),
								(byte)(unsigned % 100_000_000 / 1_000_000 + 1),
								(byte)(thirdRemainder + 1) };
					} else {
						final int fourthRemainder = (int) (unsigned % 100_000_000 / 1_000_000);
						if (fourthRemainder != 0) {
							ora = new byte[] {
									0,
									0,
									(byte)(unsigned % 100_000_000_000_000L / 1_000_000_000_000L + 1),
									(byte)(unsigned % 1_000_000_000_000L / 10_000_000_000L + 1),
									(byte)(unsigned % 10_000_000_000L / 100_000_000 + 1),
									(byte)(fourthRemainder + 1) };
						} else {
							final int fifthRemainder = (int) (unsigned % 10_000_000_000L / 100_000_000);
							if (fifthRemainder != 0) {
								ora = new byte[] {
										0,
										0,
										(byte)(unsigned % 100_000_000_000_000L / 1_000_000_000_000L + 1),
										(byte)(unsigned % 1_000_000_000_000L / 10_000_000_000L + 1),
										(byte)(fifthRemainder + 1) };
							} else {
								final int sixthRemainder = (int) (unsigned % 1_000_000_000_000L / 10_000_000_000L);
								if (sixthRemainder != 0) {
									ora = new byte[] {
											0,
											0,
											(byte)(unsigned % 100_000_000_000_000L / 1_000_000_000_000L + 1),
											(byte)(sixthRemainder + 1) };
								} else {
									final int seventhReminder = (int) (unsigned % 100_000_000_000_000L / 1_000_000_000_000L);
									if (seventhReminder != 0) {
										ora = new byte[] { 0, 0, (byte)(seventhReminder + 1) };
									} else {
										ora = new byte[2];
									}
								}
							}
						}
					}
				}
			}
			ora[0] = -56;
			ora[1] = (byte)(unsigned / 100_000_000_000_000L + 1);
		} else if (Long.compareUnsigned(unsigned, 1_000_000_000_000_000_000L) < 0) {
			final int remainder = (int) (unsigned % 100);
			if (remainder != 0) {
				ora = new byte[] {
						0,
						0,
						(byte)(unsigned % 10_000_000_000_000_000L / 100_000_000_000_000L + 1),
						(byte)(unsigned % 100_000_000_000_000L / 1_000_000_000_000L + 1),
						(byte)(unsigned % 1_000_000_000_000L / 10_000_000_000L + 1),
						(byte)(unsigned % 10_000_000_000L / 100_000_000 + 1),
						(byte)(unsigned % 100_000_000 / 1_000_000 + 1),
						(byte)(unsigned % 1_000_000 / 10_000 + 1),
						(byte)(unsigned % 10_000 / 100 + 1),
						(byte)(remainder + 1) };
			} else {
				final int secondRemainder = (int) (unsigned % 10_000 / 100);
				if (secondRemainder != 0) {
					ora = new byte[] {
							0,
							0,
							(byte)(unsigned % 10_000_000_000_000_000L / 100_000_000_000_000L + 1),
							(byte)(unsigned % 100_000_000_000_000L / 1_000_000_000_000L + 1),
							(byte)(unsigned % 1_000_000_000_000L / 10_000_000_000L + 1),
							(byte)(unsigned % 10_000_000_000L / 100_000_000 + 1),
							(byte)(unsigned % 100_000_000 / 1_000_000 + 1),
							(byte)(unsigned % 1_000_000 / 10_000 + 1),
							(byte)(secondRemainder + 1) };
				} else {
					final int thirdRemainder = (int) (unsigned % 1_000_000 / 10_000);
					if (thirdRemainder != 0) {
						ora = new byte[] {
								0,
								0,
								(byte)(unsigned % 10_000_000_000_000_000L / 100_000_000_000_000L + 1),
								(byte)(unsigned % 100_000_000_000_000L / 1_000_000_000_000L + 1),
								(byte)(unsigned % 1_000_000_000_000L / 10_000_000_000L + 1),
								(byte)(unsigned % 10_000_000_000L / 100_000_000 + 1),
								(byte)(unsigned % 100_000_000 / 1_000_000 + 1),
								(byte)(thirdRemainder + 1) };
					} else {
						final int fourthRemainder = (int) (unsigned % 100_000_000 / 1_000_000);
						if (fourthRemainder != 0) {
							ora = new byte[] {
									0,
									0,
									(byte)(unsigned % 10_000_000_000_000_000L / 100_000_000_000_000L + 1),
									(byte)(unsigned % 100_000_000_000_000L / 1_000_000_000_000L + 1),
									(byte)(unsigned % 1_000_000_000_000L / 10_000_000_000L + 1),
									(byte)(unsigned % 10_000_000_000L / 100_000_000 + 1),
									(byte)(fourthRemainder + 1) };
						} else {
							final int fifthRemainder = (int) (unsigned % 10_000_000_000L / 100_000_000);
							if (fifthRemainder != 0) {
								ora = new byte[] {
										0,
										0,
										(byte)(unsigned % 10_000_000_000_000_000L / 100_000_000_000_000L + 1),
										(byte)(unsigned % 100_000_000_000_000L / 1_000_000_000_000L + 1),
										(byte)(unsigned % 1_000_000_000_000L / 10_000_000_000L + 1),
										(byte)(fifthRemainder + 1) };
							} else {
								final int sixthRemainder = (int) (unsigned % 1_000_000_000_000L / 10_000_000_000L);
								if (sixthRemainder != 0) {
									ora = new byte[] {
											0,
											0,
											(byte)(unsigned % 10_000_000_000_000_000L / 100_000_000_000_000L + 1),
											(byte)(unsigned % 100_000_000_000_000L / 1_000_000_000_000L + 1),
											(byte)(sixthRemainder + 1) };
								} else {
									final int seventhReminder = (int) (unsigned % 100_000_000_000_000L / 1_000_000_000_000L);
									if (seventhReminder != 0) {
										ora = new byte[] {
												0,
												0,
												(byte)(unsigned % 10_000_000_000_000_000L / 100_000_000_000_000L + 1),
												(byte)(seventhReminder + 1) };
									} else {
										final int eighthRemainder = (int) (unsigned % 10_000_000_000_000_000L / 100_000_000_000_000L);
										if (eighthRemainder != 0) {
											ora = new byte[] { 0, 0, (byte)(eighthRemainder + 1) };
										} else {
											ora = new byte[2];
										}
									}
								}
							}
						}
					}
				}
			}
			ora[0] = -55;
			ora[1] = (byte)(unsigned / 10_000_000_000_000_000L + 1);
		} else {
			// From:  1_000_000_000_000_000_000
			// To:   18_446_744_073_709_551_615
			final int remainder = (int) Long.remainderUnsigned(unsigned, 100);
			if (remainder != 0) {
				ora = new byte[] {
						0,
						0,
						(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 1_000_000_000_000_000_000L), 10_000_000_000_000_000L) + 1),
						(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 10_000_000_000_000_000L), 100_000_000_000_000L) + 1),
						(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 100_000_000_000_000L), 1_000_000_000_000L) + 1),
						(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 1_000_000_000_000L), 10_000_000_000L) + 1),
						(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 10_000_000_000L), 100_000_000) + 1),
						(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 100_000_000), 1_000_000) + 1),
						(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 1_000_000), 10_000) + 1),
						(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 10_000), 100) + 1),
						(byte)(remainder + 1) };
			} else {
				final int secondRemainder = (int) Long.divideUnsigned(Long.remainderUnsigned(unsigned, 10_000), 100);
				if (secondRemainder != 0) {
					ora = new byte[] {
							0,
							0,
							(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 1_000_000_000_000_000_000L), 10_000_000_000_000_000L) + 1),
							(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 10_000_000_000_000_000L), 100_000_000_000_000L) + 1),
							(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 100_000_000_000_000L), 1_000_000_000_000L) + 1),
							(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 1_000_000_000_000L), 10_000_000_000L) + 1),
							(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 10_000_000_000L), 100_000_000) + 1),
							(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 100_000_000), 1_000_000) + 1),
							(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 1_000_000), 10_000) + 1),
							(byte)(secondRemainder + 1) };
				} else {
					final int thirdRemainder = (int) Long.divideUnsigned(Long.remainderUnsigned(unsigned, 1_000_000), 10_000);
					if (thirdRemainder != 0) {
						ora = new byte[] {
								0,
								0,
								(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 1_000_000_000_000_000_000L), 10_000_000_000_000_000L) + 1),
								(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 10_000_000_000_000_000L), 100_000_000_000_000L) + 1),
								(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 100_000_000_000_000L), 1_000_000_000_000L) + 1),
								(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 1_000_000_000_000L), 10_000_000_000L) + 1),
								(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 10_000_000_000L), 100_000_000) + 1),
								(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 100_000_000), 1_000_000) + 1),
								(byte)(thirdRemainder + 1) };
					} else {
						final int fourthRemainder = (int) Long.divideUnsigned(Long.remainderUnsigned(unsigned, 100_000_000), 1_000_000);
						if (fourthRemainder != 0) {
							ora = new byte[] {
									0,
									0,
									(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 1_000_000_000_000_000_000L), 10_000_000_000_000_000L) + 1),
									(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 10_000_000_000_000_000L), 100_000_000_000_000L) + 1),
									(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 100_000_000_000_000L), 1_000_000_000_000L) + 1),
									(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 1_000_000_000_000L), 10_000_000_000L) + 1),
									(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 10_000_000_000L), 100_000_000) + 1),
									(byte)(fourthRemainder + 1) };
						} else {
							final int fifthRemainder = (int) Long.divideUnsigned(Long.remainderUnsigned(unsigned, 10_000_000_000L), 100_000_000);
							if (fifthRemainder != 0) {
								ora = new byte[] {
										0,
										0,
										(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 1_000_000_000_000_000_000L), 10_000_000_000_000_000L) + 1),
										(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 10_000_000_000_000_000L), 100_000_000_000_000L) + 1),
										(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 100_000_000_000_000L), 1_000_000_000_000L) + 1),
										(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 1_000_000_000_000L), 10_000_000_000L) + 1),
										(byte)(fifthRemainder + 1) };
							} else {
								final int sixthRemainder = (int) Long.divideUnsigned(Long.remainderUnsigned(unsigned, 1_000_000_000_000L), 10_000_000_000L);
								if (sixthRemainder != 0) {
									ora = new byte[] {
											0,
											0,
											(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 1_000_000_000_000_000_000L), 10_000_000_000_000_000L) + 1),
											(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 10_000_000_000_000_000L), 100_000_000_000_000L) + 1),
											(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 100_000_000_000_000L), 1_000_000_000_000L) + 1),
											(byte)(sixthRemainder + 1) };
								} else {
									final int seventhReminder = (int) Long.divideUnsigned(Long.remainderUnsigned(unsigned, 100_000_000_000_000L), 1_000_000_000_000L);
									if (seventhReminder != 0) {
										ora = new byte[] {
												0,
												0,
												(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 1_000_000_000_000_000_000L), 10_000_000_000_000_000L) + 1),
												(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 10_000_000_000_000_000L), 100_000_000_000_000L) + 1),
												(byte)(seventhReminder + 1) };
									} else {
										final int eighthRemainder = (int) Long.divideUnsigned(Long.remainderUnsigned(unsigned, 10_000_000_000_000_000L), 100_000_000_000_000L);
										if (eighthRemainder != 0) {
											ora = new byte[] {
													0,
													0,
													(byte)(Long.divideUnsigned(Long.remainderUnsigned(unsigned, 1_000_000_000_000_000_000L), 10_000_000_000_000_000L) + 1),
													(byte)(eighthRemainder + 1) };
										} else {
											final int ninthRemainder = (int) Long.divideUnsigned(Long.remainderUnsigned(unsigned, 1_000_000_000_000_000_000L), 10_000_000_000_000_000L);
											if (ninthRemainder != 0) {
												ora = new byte[] { 0, 0, (byte)(ninthRemainder + 1) };
											} else {
												ora = new byte[2];
											}
										}
									}
								}
							}
						}
					}
				}
			}
			ora[0] = -54;
			ora[1] = (byte)(Long.divideUnsigned(unsigned, 1_000_000_000_000_000_000L) + 1);
		}
		return ora;
	}


}
