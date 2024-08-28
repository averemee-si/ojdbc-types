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
	 * Creates OracleTimestamp from a int array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a>
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @throws SQLException if the int array does not contain exactly 11 values
	 */
	public OracleTimestamp(final int[] value) throws SQLException {
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
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle TIMESTAMP with length = " + value.length);
		}
		return LocalDateTime
				.of(
					(Byte.toUnsignedInt(value[0]) - 100) *100 +		// 1st byte century - 100
						(Byte.toUnsignedInt(value[1]) - 100),		// 2nd byte year - 100
					Byte.toUnsignedInt(value[2]),
					Byte.toUnsignedInt(value[3]),
					Byte.toUnsignedInt(value[4]) - 1,
					Byte.toUnsignedInt(value[5]) - 1,
					Byte.toUnsignedInt(value[6]) - 1,
					RawDataUtilities.decodeOraBytes(value, 7));
	}

	/**
	 * Converts a int array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> to a {@link java.time.LocalDateTime LocalDateTime}
	 * 
	 * @param value a int array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @return {@link java.time.LocalDateTime LocalDateTime} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @throws SQLException if the int array does not contain exactly 11 values
	 */
	public static LocalDateTime toLocalDateTime(final int[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle TIMESTAMP with length = " + value.length);
		}
		return LocalDateTime
				.of(
					(value[0] - 100) *100 +		// 1st byte century - 100
						(value[1] - 100),	// 2nd byte year - 100
					value[2],
					value[3],
					value[4] - 1,
					value[5] - 1,
					value[6] - 1,
					RawDataUtilities.decodeOraBytes(value, 7));
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
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle TIMESTAMP with length = " + value.length);
		}
		return ZonedDateTime
				.of(
					(Byte.toUnsignedInt(value[0]) - 100) *100 +		// 1st byte century - 100
						(Byte.toUnsignedInt(value[1]) - 100),		// 2nd byte year - 100
					Byte.toUnsignedInt(value[2]),
					Byte.toUnsignedInt(value[3]),
					Byte.toUnsignedInt(value[4]) - 1,
					Byte.toUnsignedInt(value[5]) - 1,
					Byte.toUnsignedInt(value[6]) - 1,
					RawDataUtilities.decodeOraBytes(value, 7),
					zoneId);
	}

	/**
	 * Converts a int array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> to a {@link java.time.ZonedDateTime ZonedDateTime}
	 * 
	 * @param value a int array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @param zoneId timezone
	 * @return {@link java.time.ZonedDateTime ZonedDateTime} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @throws SQLException if the int array does not contain exactly 11 values
	 */
	public static ZonedDateTime toZonedDateTime(final int[] value, final ZoneId zoneId) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle TIMESTAMP with length = " + value.length);
		}
		return ZonedDateTime
				.of(
					(value[0] - 100) *100 +		// 1st byte century - 100
						(value[1] - 100),		// 2nd byte year - 100
					value[2],
					value[3],
					value[4] - 1,
					value[5] - 1,
					value[6] - 1,
					RawDataUtilities.decodeOraBytes(value, 7),
					zoneId);
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> to a {@link java.sql.Timestamp Timestamp}
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @return {@link java.sql.Timestamp Timestamp} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @throws SQLException if the int array does not contain exactly 11 values
	 */
	public static Timestamp toTimestamp(final byte[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle TIMESTAMP with length = " + value.length);
		}
		return Timestamp.valueOf(toLocalDateTime(value));
	}

	/**
	 * Converts a int array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> to a {@link java.sql.Timestamp Timestamp}
	 * 
	 * @param value a int array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @return {@link java.sql.Timestamp Timestamp} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @throws SQLException if the int array does not contain exactly 11 values
	 */
	public static Timestamp toTimestamp(final int[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle TIMESTAMP with length = " + value.length);
		}
		return Timestamp.valueOf(toLocalDateTime(value));
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> to a string in <a href="https://en.wikipedia.org/wiki/ISO_8601#Combined_date_and_time_representations">ISO-8601</a> format
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @return String representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @throws SQLException if the byte array does not contain exactly 11 values
	 */
	public static String toString(final byte[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle TIMESTAMP with length = " + value.length);
		}
		return toLocalDateTime(value).toString();
	}

	/**
	 * Converts a int array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> to a string in <a href="https://en.wikipedia.org/wiki/ISO_8601#Combined_date_and_time_representations">ISO-8601</a> format
	 * 
	 * @param value a int array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @return String representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMP.html">TIMESTAMP</a> object
	 * @throws SQLException if the int array does not contain exactly 11 values
	 */
	public static String toString(final int[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle TIMESTAMP with length = " + value.length);
		}
		return toLocalDateTime(value).toString();
	}

}
