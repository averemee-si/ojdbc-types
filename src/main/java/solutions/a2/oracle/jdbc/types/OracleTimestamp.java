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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static solutions.a2.oracle.utils.BinaryUtils.getU32BE;

/**
 * Oracle <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> to Java Classes Conversions
 *     For Oracle format description please see <a href="https://support.oracle.com/rs?type=doc&id=69028.1">How does Oracle store the DATE datatype internally? (Doc ID 69028.1)</a>
 * Oracle Type 180 - TIMESTAMP
 */
public class OracleTimestamp extends OracleDate implements Serializable {

	private static final long serialVersionUID = 2048882980275026308L;

	/**
	 * Oracle Type 180 length
	 */
	public static final int DATA_LENGTH = 11;
	public static final int DATA_LENGTH_NOFRACTION = OracleDate.DATA_LENGTH;


	/**
	 * Creates OracleTimestamp from a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a>
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @throws SQLException if the byte array does not contain exactly 11 values
	 */
	public OracleTimestamp(final byte[] value) throws SQLException {
		ldt = toLocalDateTime(value);
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> to a {@link java.time.LocalDateTime LocalDateTime}
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @return {@link java.time.LocalDateTime LocalDateTime} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @throws SQLException if the byte array does not contain exactly 11 values
	 */
	public static LocalDateTime toLocalDateTime(final byte[] value) throws SQLException {
		return toLocalDateTime(value, 0, value.length);
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> to a {@link java.time.LocalDateTime LocalDateTime}
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @param offset the index of the first byte representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @param length - the number of bytes representing oracle.sql.TIMESTAMP
	 * @return {@link java.time.LocalDateTime LocalDateTime} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @throws SQLException if the byte array does not contain exactly 11 values
	 */
	public static LocalDateTime toLocalDateTime(final byte[] value, final int offset, final int length) throws SQLException {
		if (offset + length > value.length) {
			throw new SQLException("Not enough data to convert in array with length " + value.length);
		} else if (length == DATA_LENGTH_NOFRACTION) {
			return LocalDateTime
					.of(
						(Byte.toUnsignedInt(value[offset]) - 100) *100 +		// 1st byte century - 100
							(Byte.toUnsignedInt(value[offset + 1]) - 100),		// 2nd byte year - 100
						Byte.toUnsignedInt(value[offset + 2]),
						Byte.toUnsignedInt(value[offset + 3]),
						Byte.toUnsignedInt(value[offset + 4]) - 1,
						Byte.toUnsignedInt(value[offset + 5]) - 1,
						Byte.toUnsignedInt(value[offset + 6]) - 1);
		} else if (length == DATA_LENGTH) {
			return LocalDateTime
					.of(
						(Byte.toUnsignedInt(value[offset]) - 100) *100 +		// 1st byte century - 100
							(Byte.toUnsignedInt(value[offset + 1]) - 100),		// 2nd byte year - 100
						Byte.toUnsignedInt(value[offset + 2]),
						Byte.toUnsignedInt(value[offset + 3]),
						Byte.toUnsignedInt(value[offset + 4]) - 1,
						Byte.toUnsignedInt(value[offset + 5]) - 1,
						Byte.toUnsignedInt(value[offset + 6]) - 1,
						getU32BE(value, offset + 7));
		} else {
			throw new SQLException("Wrong representation of Oracle TIMESTAMP with length = " + value.length);
		}
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> to a {@link java.time.ZonedDateTime ZonedDateTime}
	 * 
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @param zoneId timezone
	 * @return {@link java.time.ZonedDateTime ZonedDateTime} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @throws SQLException if the byte array does not contain exactly 11 values
	 */
	public static ZonedDateTime toZonedDateTime(final byte[] value, final ZoneId zoneId) throws SQLException {
		return toZonedDateTime(value, 0, value.length, zoneId);
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> to a {@link java.time.ZonedDateTime ZonedDateTime}
	 * 
	 * @param value a int array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @param offset the index of the first byte representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @param length - the number of bytes representing oracle.sql.TIMESTAMP
	 * @param zoneId timezone
	 * @return {@link java.time.ZonedDateTime ZonedDateTime} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @throws SQLException if the int array does not contain exactly 11 values
	 */
	public static ZonedDateTime toZonedDateTime(final byte[] value, final int offset, final int length, final ZoneId zoneId) throws SQLException {
		if (offset + length > value.length) {
			throw new SQLException("Not enough data to convert in array with length " + value.length);
		} else if (length == DATA_LENGTH_NOFRACTION) {
			return ZonedDateTime
					.of(
						(Byte.toUnsignedInt(value[offset]) - 100) *100 +		// 1st byte century - 100
							(Byte.toUnsignedInt(value[offset + 1]) - 100),		// 2nd byte year - 100
						Byte.toUnsignedInt(value[offset + 2]),
						Byte.toUnsignedInt(value[offset + 3]),
						Byte.toUnsignedInt(value[offset + 4]) - 1,
						Byte.toUnsignedInt(value[offset + 5]) - 1,
						Byte.toUnsignedInt(value[offset + 6]) - 1,
						0,
						zoneId);
		} else if (length == DATA_LENGTH) {
			return ZonedDateTime
					.of(
						(Byte.toUnsignedInt(value[offset]) - 100) *100 +		// 1st byte century - 100
							(Byte.toUnsignedInt(value[offset + 1]) - 100),		// 2nd byte year - 100
						Byte.toUnsignedInt(value[offset + 2]),
						Byte.toUnsignedInt(value[offset + 3]),
						Byte.toUnsignedInt(value[offset + 4]) - 1,
						Byte.toUnsignedInt(value[offset + 5]) - 1,
						Byte.toUnsignedInt(value[offset + 6]) - 1,
						getU32BE(value, offset + 7),
						zoneId);
		} else {
			throw new SQLException("Wrong representation of Oracle TIMESTAMP with length = " + value.length);
		}
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> to a {@link java.sql.Timestamp Timestamp}
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @return {@link java.sql.Timestamp Timestamp} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @throws SQLException if the int array does not contain exactly 11 values
	 */
	public static Timestamp toTimestamp(final byte[] value) throws SQLException {
		return Timestamp.valueOf(toLocalDateTime(value, 0, value.length));
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> to a {@link java.sql.Timestamp Timestamp}
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @param offset the index of the first byte representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @param length - the number of bytes representing oracle.sql.TIMESTAMP
	 * @return {@link java.sql.Timestamp Timestamp} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @throws SQLException if the int array does not contain exactly 11 values
	 */
	public static Timestamp toTimestamp(final byte[] value, final int offset, final int length) throws SQLException {
		return Timestamp.valueOf(toLocalDateTime(value, offset, length));
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> to a string in <a href="https://en.wikipedia.org/wiki/ISO_8601#Combined_date_and_time_representations">ISO-8601</a> format
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @return String representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @throws SQLException if the byte array does not contain exactly 11 values
	 */
	public static String toString(final byte[] value) throws SQLException {
		return toLocalDateTime(value, 0, value.length).toString();
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> to a string in <a href="https://en.wikipedia.org/wiki/ISO_8601#Combined_date_and_time_representations">ISO-8601</a> format
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @param offset the index of the first byte representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @param length - the number of bytes representing oracle.sql.TIMESTAMP
	 * @return String representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @throws SQLException if the byte array does not contain exactly 11 values
	 */
	public static String toString(final byte[] value, final int offset, final int length) throws SQLException {
		return toLocalDateTime(value, offset, length).toString();
	}


}
