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


/**
 * Oracle <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> to Java Classes Conversions
 *     For Oracle format description please see <a href="https://support.oracle.com/rs?type=doc&id=69028.1">How does Oracle store the DATE datatype internally? (Doc ID 69028.1)</a>
 * Oracle Type 12 - DATE
 */
public class OracleDate implements Serializable {

	private static final long serialVersionUID = 2965271366055770056L;
	/**
	 * Oracle Type 12 length
	 */
	public static final int DATA_LENGTH = 7;

	protected LocalDateTime ldt;

	protected OracleDate() {}

	/**
	 * Creates OracleDate from a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a>
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> object
	 * @throws SQLException if the byte array does not contains at least 7 values
	 */
	public OracleDate(final byte[] value) throws SQLException {
		ldt = toLocalDateTime(value);		
	}

	/**
	 * LocalDateTime representation of OracleDate
	 * 
	 * @return LocalDateTime representation of OracleDate
	 */
	public LocalDateTime toLocalDateTime() {
		return ldt;
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> to a {@link java.time.LocalDateTime LocalDateTime}
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> object
	 * @return {@link java.time.LocalDateTime LocalDateTime} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> object
	 * @throws SQLException if the byte array does not contains at least 7 values
	 */
	public static LocalDateTime toLocalDateTime(final byte[] value) throws SQLException {
		if (value.length < DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle DATE with length = " + value.length);
		}
		return LocalDateTime.of(
				(Byte.toUnsignedInt(value[0]) - 100) *100 +		// 1st byte century - 100
						(Byte.toUnsignedInt(value[1]) - 100),	// 2nd byte year - 100
				Byte.toUnsignedInt(value[2]),
				Byte.toUnsignedInt(value[3]),
				Byte.toUnsignedInt(value[4]) - 1,
				Byte.toUnsignedInt(value[5]) - 1,
				Byte.toUnsignedInt(value[6]) - 1);
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> to a {@link java.time.LocalDateTime LocalDateTime}
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> object
	 * @param offset the index of the first byte representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> object
	 * @return {@link java.time.LocalDateTime LocalDateTime} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> object
	 * @throws SQLException if the byte array does not contains at least 7 values
	 */
	public static LocalDateTime toLocalDateTime(final byte[] value, final int offset) throws SQLException {
		if (offset + DATA_LENGTH > value.length) {
			throw new SQLException("Not enough data for DATE in array with length = " + value.length);
		}
		return LocalDateTime.of(
				(Byte.toUnsignedInt(value[offset]) - 100) *100 +		// 1st byte century - 100
						(Byte.toUnsignedInt(value[offset + 1]) - 100),	// 2nd byte year - 100
				Byte.toUnsignedInt(value[offset + 2]),
				Byte.toUnsignedInt(value[offset + 3]),
				Byte.toUnsignedInt(value[offset + 4]) - 1,
				Byte.toUnsignedInt(value[offset + 5]) - 1,
				Byte.toUnsignedInt(value[offset + 6]) - 1);
	}

	/**
	 * 
	 * @return {@link java.sql.Timestamp Timestamp} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> object
	 */
	public Timestamp toTimestamp() {
		return Timestamp.valueOf(ldt);
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> to a {@link java.sql.Timestamp Timestamp}
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> object
	 * @return {@link java.sql.Timestamp Timestamp} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> object
	 * @throws SQLException if the int array does not contains at least 7 values
	 */
	public static Timestamp toTimestamp(final byte[] value) throws SQLException {
		return Timestamp.valueOf(toLocalDateTime(value));
	}


	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> to a {@link java.sql.Timestamp Timestamp}
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> object
	 * @param offset the index of the first byte representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> object
	 * @return {@link java.sql.Timestamp Timestamp} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> object
	 * @throws SQLException if the int array does not contains at least 7 values
	 */
	public static Timestamp toTimestamp(final byte[] value, final int offset) throws SQLException {
		return Timestamp.valueOf(toLocalDateTime(value, offset));
	}

	@Override
	public String toString() {
		return ldt.toString();
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> to a string in <a href="https://en.wikipedia.org/wiki/ISO_8601#Combined_date_and_time_representations">ISO-8601</a> format
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> object
	 * @return String representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/DATE.html">DATE</a> object
	 * @throws SQLException if the byte array does not contains at least 7 values
	 */
	public static String toString(final byte[] value) throws SQLException {
		if (value.length < DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle DATE with length = " + value.length);
		}
		return toLocalDateTime(value).toString();
	}

}
