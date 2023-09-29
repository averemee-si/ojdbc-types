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
import java.time.Period;

/**
 * Oracle <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALYM.html">INTERVALYM</a> to Java Classes Conversions
 *     For Oracle format description please see <a href="https://www.orafaq.com/wiki/Interval">Interval</a>
 * 
 * Oracle Type 182
 */
public class IntervalYearToMonth implements Serializable {

	private static final long serialVersionUID = -4521533224944486365L;

	/**
	 * Length of INTERVALYM
	 */
	public static final int DATA_LENGTH = 5;

	/**
	 * The number of years.
	 */
	private final int years;
	/**
	 * The number of months.
	 */
	private final int months;

	/**
	 * Creates IntervalYearToMonth from a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALYM.html">INTERVALYM</a>
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALYM.html">INTERVALYM</a> object
	 * @throws SQLException if the byte array does not contain exactly 5 values
	 */
	public IntervalYearToMonth(final byte[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle INTERVALYM with length = " + value.length);
		}
		years = RawDataUtilities.decodeOraBytes4I(value, 0);
		months = RawDataUtilities.unsigned(value[4]) - RawDataUtilities.ORA_INTERVAL_OFFSET;
	}

	/**
	 * Creates IntervalYearToMonth from a int array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALYM.html">INTERVALYM</a>
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALYM.html">INTERVALYM</a> object
	 * @throws SQLException if the int array does not contain exactly 5 values
	 */
	public IntervalYearToMonth(final int[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle INTERVALYM with length = " + value.length);
		}
		years = RawDataUtilities.decodeOraBytes4I(value, 0);
		months = value[4] - RawDataUtilities.ORA_INTERVAL_OFFSET;
	}

	/**
	 * Number of years
	 * 
	 * @return number of years
	 */
	public int getYears() {
		return years;
	}

	/**
	 * Number of months
	 * 
	 * @return number of months
	 */
	public int getMonths() {
		return months;
	}

	/**
	 * Converts this object to {@link java.time.Period  Period}
	 * 
	 * @return {@link java.time.Period  Period} representing this object
	 */
	public Period toPeriod() {
		return toPeriod(years, months);
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALYM.html">INTERVALYM</a> to a {@link java.time.Period Period}
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALYM.html">INTERVALYM</a> object
	 * @return {@link java.time.Period  Period} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALYM.html">INTERVALYM</a> object
	 * @throws SQLException if the byte array does not contain exactly 5 values
	 */
	public static Period toPeriod(final byte[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle INTERVALYM with length = " + value.length);
		}
		final int years = RawDataUtilities.decodeOraBytes4I(value, 0);
		final int months = RawDataUtilities.unsigned(value[4]) - RawDataUtilities.ORA_INTERVAL_OFFSET;
		return toPeriod(years, months);
	}

	/**
	 * Converts a int array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALYM.html">INTERVALYM</a> to a {@link java.time.Period  Period}
	 * 
	 * @param value a int array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALYM.html">INTERVALYM</a> object
	 * @return {@link java.time.Period  Period} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALYM.html">INTERVALYM</a> object
	 * @throws SQLException if the int array does not contain exactly 5 values
	 */
	public static Period toPeriod(final int[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle INTERVALYM with length = " + value.length);
		}
		final int years = RawDataUtilities.decodeOraBytes4I(value, 0);
		final int months = value[4] - RawDataUtilities.ORA_INTERVAL_OFFSET;
		return toPeriod(years, months);
	}

	private static Period toPeriod(final int years, final int months) {
		return Period.of(years, months, 0);
	}

	@Override
	public String toString() {
		return toString(years, months);
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALYM.html">INTERVALYM</a> to a string in <a href="https://en.wikipedia.org/wiki/ISO_8601#Durations">ISO-8601</a> format
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALYM.html">INTERVALYM</a> object
	 * @return String representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALYM.html">INTERVALYM</a> object
	 * @throws SQLException if the byte array does not contain exactly 5 values
	 */
	public static String toString(final byte[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle INTERVALYM with length = " + value.length);
		}
		final int years = RawDataUtilities.decodeOraBytes4I(value, 0);
		final int months = RawDataUtilities.unsigned(value[4]) - RawDataUtilities.ORA_INTERVAL_OFFSET;
		return toString(years, months);
	}

	/**
	 * Converts a int array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALYM.html">INTERVALYM</a> to a string in <a href="https://en.wikipedia.org/wiki/ISO_8601#Durations">ISO-8601</a> format
	 * 
	 * @param value a int array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALYM.html">INTERVALYM</a> object
	 * @return String representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/INTERVALYM.html">INTERVALYM</a> object
	 * @throws SQLException if the int array does not contain exactly 5 values
	 */
	public static String toString(final int[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle INTERVALYM with length = " + value.length);
		}
		final int years = RawDataUtilities.decodeOraBytes4I(value, 0);
		final int months = value[4] - RawDataUtilities.ORA_INTERVAL_OFFSET;
		return toString(years, months);
	}

	private static String toString(final int years, final int months) {
		final StringBuilder sb = new StringBuilder();
		sb.append("P");
		if (years != 0) {
			sb
				.append(years)
				.append("Y");
		}
		if (months != 0) {
			sb
				.append(months)
				.append("M");
		}
		return sb.toString();
	}

}
