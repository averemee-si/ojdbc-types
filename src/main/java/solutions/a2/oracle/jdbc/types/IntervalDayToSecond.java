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
import java.time.Duration;

/**
 * Oracle <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALDS.html">INTERVALDS</a> to Java Classes Conversions
 *     For Oracle format description please see <a href="https://www.orafaq.com/wiki/Interval">Interval</a>
 * 
 * Oracle Type 183
 */
public class IntervalDayToSecond implements Serializable {

	private static final long serialVersionUID = 4962903459011562735L;
	
	/**
	 * Length of INTERVALDS
	 */
	public static final int DATA_LENGTH = 11;

	/**
	 * The number of days.
	 */
	private final int days;
	/**
	 * The number of hours.
	 */
	private final int hours;
	/**
	 * The number of minutes.
	 */
	private final int minutes;
	/**
	 * The number of seconds.
	 */
	private final int seconds;
	/**
	 * The number of nanoseconds.
	 */
	private final int nanos;

	/**
	 * Creates IntervalDayToSecond from a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALDS.html">INTERVALDS</a>
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALDS.html">INTERVALDS</a> object
	 * @throws SQLException if the byte array does not contain exactly 11 values
	 */
	public IntervalDayToSecond(final byte[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle INTERVALDS with length = " + value.length);
		}
		days = RawDataUtilities.decodeOraBytes4I(value, 0);
		hours = RawDataUtilities.unsigned(value[4]) - RawDataUtilities.ORA_INTERVAL_OFFSET;
		minutes = RawDataUtilities.unsigned(value[5]) - RawDataUtilities.ORA_INTERVAL_OFFSET;
		seconds = RawDataUtilities.unsigned(value[6]) - RawDataUtilities.ORA_INTERVAL_OFFSET;
		nanos = RawDataUtilities.decodeOraBytes4I(value, 7);
	}

	/**
	 * Creates IntervalDayToSecond from a int array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALDS.html">INTERVALDs</a>
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALDS.html">INTERVALDS</a> object
	 * @throws SQLException if the int array does not contain exactly 11 values
	 */
	public IntervalDayToSecond(final int[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle INTERVALDS with length = " + value.length);
		}
		days = RawDataUtilities.decodeOraBytes4I(value, 0);
		hours = value[4] - RawDataUtilities.ORA_INTERVAL_OFFSET;
		minutes = value[5] - RawDataUtilities.ORA_INTERVAL_OFFSET;
		seconds = value[6] - RawDataUtilities.ORA_INTERVAL_OFFSET;
		nanos = RawDataUtilities.decodeOraBytes4I(value, 7);
	}

	/**
	 * Number of days
	 * 
	 * @return number of days
	 */
	public int getDays() {
		return days;
	}

	/**
	 * Number of hours
	 * 
	 * @return number of hours
	 */
	public int getHours() {
		return hours;
	}

	/**
	 * Number of minutes
	 * 
	 * @return number of minutes
	 */
	public int getMinutes() {
		return minutes;
	}

	/**
	 * Number of seconds
	 * 
	 * @return number of seconds
	 */
	public int getSeconds() {
		return seconds;
	}

	/**
	 * Number of nanos
	 * 
	 * @return number of nanos
	 */
	public int getNanos() {
		return nanos;
	}

	/**
	 * Converts this object to {@link java.time.Duration Duration}
	 * 
	 * @return {@link java.time.Duration Duration} representing this object
	 */
	public Duration toDuration() {
		return toDuration(days, hours, minutes, seconds, nanos);
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALDS.html">INTERVALDS</a> to a {@link java.time.Duration Duration}
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALDS.html">INTERVALDS</a> object
	 * @return {@link java.time.Duration Duration} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALDS.html">INTERVALDS</a> object
	 * @throws SQLException if the byte array does not contain exactly 11 values
	 */
	public static Duration toDuration(final byte[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle INTERVALDS with length = " + value.length);
		}
		final int days = RawDataUtilities.decodeOraBytes4I(value, 0);
		final int hours = RawDataUtilities.unsigned(value[4]) - RawDataUtilities.ORA_INTERVAL_OFFSET;
		final int minutes = RawDataUtilities.unsigned(value[5]) - RawDataUtilities.ORA_INTERVAL_OFFSET;
		final int seconds = RawDataUtilities.unsigned(value[6]) - RawDataUtilities.ORA_INTERVAL_OFFSET;
		final int nanos = RawDataUtilities.decodeOraBytes4I(value, 7);
		return toDuration(days, hours, minutes, seconds, nanos);
	}

	/**
	 * Converts a int array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALDS.html">INTERVALDS</a> to a {@link java.time.Duration Duration}
	 * 
	 * @param value a int array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALDS.html">INTERVALDS</a> object
	 * @return {@link java.time.Duration Duration} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALDS.html">INTERVALDS</a> object
	 * @throws SQLException if the int array does not contain exactly 11 values
	 */
	public static Duration toDuration(final int[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle INTERVALDS with length = " + value.length);
		}
		final int days = RawDataUtilities.decodeOraBytes4I(value, 0);
		final int hours = value[4] - RawDataUtilities.ORA_INTERVAL_OFFSET;
		final int minutes = value[5] - RawDataUtilities.ORA_INTERVAL_OFFSET;
		final int seconds = value[6] - RawDataUtilities.ORA_INTERVAL_OFFSET;
		final int nanos = RawDataUtilities.decodeOraBytes4I(value, 7);
		return toDuration(days, hours, minutes, seconds, nanos);
	}

	private static Duration toDuration(
			final int days, final int hours, final int minutes, final int seconds, final int nanos) {
		Duration duration = Duration.ZERO;
		if (days != 0) {
			duration = duration.plusDays(days);
		}
		if (hours != 0) {
			duration = duration.plusHours(hours);
		}
		if (minutes != 0) {
			duration = duration.plusMinutes(minutes);
		}
		if (seconds != 0) {
			duration = duration.plusSeconds(seconds);
		}
		if (nanos != 0) {
			duration = duration.plusNanos(nanos);
		}
		return duration;
	}

	@Override
	public String toString() {
		return toString(days, hours, minutes, seconds, nanos);
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALDS.html">INTERVALDS</a> to a string in <a href="https://en.wikipedia.org/wiki/ISO_8601#Durations">ISO-8601</a> format
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALDS.html">INTERVALDS</a> object
	 * @return String representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALDS.html">INTERVALDS</a> object
	 * @throws SQLException if the byte array does not contain exactly 11 values
	 */
	public static String toString(final byte[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle INTERVALDS with length = " + value.length);
		}
		final int days = RawDataUtilities.decodeOraBytes4I(value, 0);
		final int hours = RawDataUtilities.unsigned(value[4]) - RawDataUtilities.ORA_INTERVAL_OFFSET;
		final int minutes = RawDataUtilities.unsigned(value[5]) - RawDataUtilities.ORA_INTERVAL_OFFSET;
		final int seconds = RawDataUtilities.unsigned(value[6]) - RawDataUtilities.ORA_INTERVAL_OFFSET;
		final int nanos = RawDataUtilities.decodeOraBytes4I(value, 7);
		return toString(days, hours, minutes, seconds, nanos);
	}

	/**
	 * Converts a int array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALDS.html">INTERVALDS</a> to a string in <a href="https://en.wikipedia.org/wiki/ISO_8601#Durations">ISO-8601</a> format
	 * 
	 * @param value a int array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALDS.html">INTERVALDS</a> object
	 * @return String representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALDS.html">INTERVALDS</a> object
	 * @throws SQLException if the int array does not contain exactly 11 values
	 */
	public static String toString(final int[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle INTERVALDS with length = " + value.length);
		}
		final int days = RawDataUtilities.decodeOraBytes4I(value, 0);
		final int hours = value[4] - RawDataUtilities.ORA_INTERVAL_OFFSET;
		final int minutes = value[5] - RawDataUtilities.ORA_INTERVAL_OFFSET;
		final int seconds = value[6] - RawDataUtilities.ORA_INTERVAL_OFFSET;
		final int nanos = RawDataUtilities.decodeOraBytes4I(value, 7);
		return toString(days, hours, minutes, seconds, nanos);
	}

	private static String toString(
			final int days, final int hours, final int minutes, final int seconds, final int nanos) {
		final StringBuilder sb = new StringBuilder();
		sb.append("P");
		if (days != 0) {
			sb
				.append(days)
				.append("D");
		}
		if (hours != 0 || minutes != 0 || seconds != 0 || nanos != 0) {
			sb.append("T");
			if (hours != 0) {
				sb
					.append(hours)
					.append("H");
			}
			if (minutes != 0) {
				sb
					.append(minutes)
					.append("M");
			}
			if (seconds != 0 || nanos != 0) {
				if (seconds != 0) {
					sb.append(seconds);
				}
				if (nanos != 0) {
					sb
						.append(".")
						.append(String.format("%09d",nanos));
				}
				sb.append("S");
			}
		}
		return sb.toString();
	}

}
