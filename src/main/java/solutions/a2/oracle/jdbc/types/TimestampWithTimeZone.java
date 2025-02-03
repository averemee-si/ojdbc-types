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
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRules;


/**
 * Oracle <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> to Java Classes Conversions
 *     For Oracle format description please see <a href="https://support.oracle.com/rs?type=doc&id=69028.1">How does Oracle store the DATE datatype internally? (Doc ID 69028.1)</a>
 * Oracle Type 181 - TIMESTAMP WITH TIME ZONE
 */
public class TimestampWithTimeZone implements Serializable {

	private static final long serialVersionUID = 8489462773353366769L;
	private static final int TS_OFFSET_HOUR = 20;
	private static final int TS_OFFSET_MINUTE = 60;
	private static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");

	/**
	 * Oracle Type 181 length
	 */
	public static final int DATA_LENGTH = 13;

	private final ZonedDateTime zdt;

	/**
	 * Creates TimestampWithTimeZone from a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a>
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @throws SQLException if the byte array does not contain exactly 13 values
	 */
	public TimestampWithTimeZone(final byte[] value) throws SQLException {
		zdt = toZonedDateTime(value);
	}

	/**
	 * Creates TimestampWithTimeZone from a int array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a>
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @throws SQLException if the int array does not contain exactly 11 values
	 */
	public TimestampWithTimeZone(final int[] value) throws SQLException {
		zdt = toZonedDateTime(value);
	}

	/**
	 * ZonedDateTime representation of TimestampWithTimeZone
	 * 
	 * @return {@link java.time.ZonedDateTime ZonedDateTime} representation of TimestampWithTimeZone
	 */
	public ZonedDateTime toZonedDateTime() {
		return zdt;
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> to a {@link java.time.ZonedDateTime ZonedDateTime}
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @return {@link java.time.ZonedDateTime ZonedDateTime} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @throws SQLException if the byte array does not contain exactly 13 values
	 */
	public static ZonedDateTime toZonedDateTime(final byte[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle TIMESTAMPTZ with length = " + value.length);
		}
        final ZonedDateTime zdt = ZonedDateTime
				.of(
						(Byte.toUnsignedInt(value[0]) - 100) *100 +		// 1st byte century - 100
							(Byte.toUnsignedInt(value[1]) - 100),		// 2nd byte year - 100
						Byte.toUnsignedInt(value[2]),
						Byte.toUnsignedInt(value[3]),
						Byte.toUnsignedInt(value[4]) - 1,
						Byte.toUnsignedInt(value[5]) - 1,
						Byte.toUnsignedInt(value[6]) - 1,
						RawDataUtilities.decodeOraBytes(value, 7),
						UTC_ZONE_ID);

		return zdt.withZoneSameInstant(getZoneId(
							Byte.toUnsignedInt(value[11]),
							Byte.toUnsignedInt(value[12])));
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> to a {@link java.time.ZonedDateTime ZonedDateTime}
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @param offset the index of the first byte representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @return {@link java.time.ZonedDateTime ZonedDateTime} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @throws SQLException if the byte array does not contain exactly 13 values
	 */
	public static ZonedDateTime toZonedDateTime(final byte[] value, final int offset) throws SQLException {
		if (offset + DATA_LENGTH > value.length) {
			throw new SQLException("Not enough data for TIMESTAMPTZ in array with length = " + value.length);
		}
        final ZonedDateTime zdt = ZonedDateTime
				.of(
						(Byte.toUnsignedInt(value[offset]) - 100) *100 +		// 1st byte century - 100
							(Byte.toUnsignedInt(value[offset + 1]) - 100),		// 2nd byte year - 100
						Byte.toUnsignedInt(value[offset + 2]),
						Byte.toUnsignedInt(value[offset + 3]),
						Byte.toUnsignedInt(value[offset + 4]) - 1,
						Byte.toUnsignedInt(value[offset + 5]) - 1,
						Byte.toUnsignedInt(value[offset + 6]) - 1,
						RawDataUtilities.decodeOraBytes(value, offset + 7),
						UTC_ZONE_ID);

		return zdt.withZoneSameInstant(getZoneId(
							Byte.toUnsignedInt(value[offset + 11]),
							Byte.toUnsignedInt(value[offset + 12])));
	}

	/**
	 * Converts a int array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> to a {@link java.time.ZonedDateTime ZonedDateTime}
	 * 
	 * @param value a int array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @return {@link java.time.ZonedDateTime ZonedDateTime} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @throws SQLException if the int array does not contain exactly 13 values
	 */
	public static ZonedDateTime toZonedDateTime(final int[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle TIMESTAMPTZ with length = " + value.length);
		}
        final ZonedDateTime zdt = ZonedDateTime
				.of(
					(value[0] - 100) *100 +		// 1st byte century - 100
						(value[1] - 100),		// 2nd byte year - 100
					value[2],
					value[3],
					value[4] - 1,
					value[5] - 1,
					value[6] - 1,
					RawDataUtilities.decodeOraBytes(value, 7),
					UTC_ZONE_ID);
        return zdt.withZoneSameInstant(getZoneId(value[11], value[12]));
	}

	/**
	 * OffsetDateTime representation of TimestampWithTimeZone
	 * 
	 * @return {@link java.time.OffsetDateTime OffsetDateTime} representation of TimestampWithTimeZone
	 */
	public OffsetDateTime toOffsetDateTime() {
		return zdt.toOffsetDateTime();
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> to a {@link java.time.OffsetDateTime OffsetDateTime}
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @return {@link java.time.OffsetDateTime OffsetDateTime} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @throws SQLException if the byte array does not contain exactly 13 values
	 */
	public static OffsetDateTime toOffsetDateTime(final byte[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle TIMESTAMPTZ with length = " + value.length);
		}
        final OffsetDateTime odt = OffsetDateTime
				.of(
						(Byte.toUnsignedInt(value[0]) - 100) *100 +		// 1st byte century - 100
							(Byte.toUnsignedInt(value[1]) - 100),		// 2nd byte year - 100
						Byte.toUnsignedInt(value[2]),
						Byte.toUnsignedInt(value[3]),
						Byte.toUnsignedInt(value[4]) - 1,
						Byte.toUnsignedInt(value[5]) - 1,
						Byte.toUnsignedInt(value[6]) - 1,
						RawDataUtilities.decodeOraBytes(value, 7),
						ZoneOffset.UTC);
		return odt.withOffsetSameInstant(getZoneOffset(
							Byte.toUnsignedInt(value[11]),
							Byte.toUnsignedInt(value[12]),
							odt.toInstant()));
	}

	/**
	 * Converts a int array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> to a {@link java.time.OffsetDateTime OffsetDateTime}
	 * 
	 * @param value a int array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @return {@link java.time.OffsetDateTime OffsetDateTime} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @throws SQLException if the byte array does not contain exactly 13 values
	 */
	public static OffsetDateTime toOffsetDateTime(final int[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle TIMESTAMPTZ with length = " + value.length);
		}
        final OffsetDateTime odt = OffsetDateTime
				.of(
					(value[0] - 100) *100 +		// 1st byte century - 100
						(value[1] - 100),		// 2nd byte year - 100
					value[2],
					value[3],
					value[4] - 1,
					value[5] - 1,
					value[6] - 1,
					RawDataUtilities.decodeOraBytes(value, 7),
					ZoneOffset.UTC);
		return odt.withOffsetSameInstant(getZoneOffset(
							value[11], value[12], odt.toInstant()));
	}

	/**
	 * 
	 * @return {@link java.sql.Timestamp Timestamp} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 */
	public Timestamp toTimestamp() {
		return Timestamp.from(zdt.toInstant());
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> to a {@link java.sql.Timestamp Timestamp}
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @return {@link java.sql.Timestamp Timestamp} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @throws SQLException if the int array does not contain exactly 13 values
	 */
	public static Timestamp toTimestamp(final byte[] value) throws SQLException {
		return Timestamp.from(toZonedDateTime(value).toInstant());
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> to a {@link java.sql.Timestamp Timestamp}
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @param offset the index of the first byte representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @return {@link java.sql.Timestamp Timestamp} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @throws SQLException if the int array does not contain exactly 13 values
	 */
	public static Timestamp toTimestamp(final byte[] value, final int offset) throws SQLException {
		return Timestamp.from(toZonedDateTime(value, offset).toInstant());
	}

	/**
	 * Converts a int array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> to a {@link java.sql.Timestamp Timestamp}
	 * 
	 * @param value a int array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @return {@link java.sql.Timestamp Timestamp} representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @throws SQLException if the int array does not contain exactly 13 values
	 */
	public static Timestamp toTimestamp(final int[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle TIMESTAMPTZ with length = " + value.length);
		}
		return Timestamp.from(toZonedDateTime(value).toInstant());
	}

	@Override
	public String toString() {
		return zdt.toString();
	}

	/**
	 * Converts a byte array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> to a string in <a href="https://en.wikipedia.org/wiki/ISO_8601#Combined_date_and_time_representations">ISO-8601</a> format
	 * 
	 * @param value a byte array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @return String representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @throws SQLException if the byte array does not contain exactly 13 values
	 */
	public static String toString(final byte[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle TIMESTAMPTZ with length = " + value.length);
		}
		return toZonedDateTime(value).toString();
	}

	/**
	 * Converts a int array representing of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> to a string in <a href="https://en.wikipedia.org/wiki/ISO_8601#Combined_date_and_time_representations">ISO-8601</a> format
	 * 
	 * @param value a int array representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @return String representing the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/jajdb/oracle/sql/TIMESTAMPTZ.html">TIMESTAMPTZ</a> object
	 * @throws SQLException if the int array does not contain exactly 13 values
	 */
	public static String toString(final int[] value) throws SQLException {
		if (value.length != DATA_LENGTH) {
			throw new SQLException("Wrong representation of Oracle TIMESTAMPTZ with length = " + value.length);
		}
		return toZonedDateTime(value).toString();
	}

	private static ZoneId getZoneId(final int byte11, final int byte12) throws SQLException {
		final ZoneId zoneId;
		if ((byte11 & 0x80) == 0x0) {
			final ZoneOffset offset = ZoneOffset.ofHoursMinutes(
					byte11 - TS_OFFSET_HOUR,
					byte12 - TS_OFFSET_MINUTE);
			zoneId = ZoneId.of(offset.toString());
		} else {
			final int zoneNumericId = 
					RawDataUtilities.getHighOrderBits(byte11) + 
					RawDataUtilities.getLowOrderBits(byte12);
			final String zoneName = TimeZoneMap.getName(zoneNumericId);
			if (zoneName == null) {
				throw new SQLException("Unable to find timezone name for ID=" + zoneNumericId);
			}
			zoneId = ZoneId.of(zoneName);
		}
		return zoneId;
	}

	private static ZoneOffset getZoneOffset(final int byte11, final int byte12, final Instant instant) throws SQLException {
		final ZoneOffset zoneOffset;
		if ((byte11 & 0x80) == 0x0) {
			zoneOffset = ZoneOffset.ofHoursMinutes(
					byte11 - TS_OFFSET_HOUR,
					byte12 - TS_OFFSET_MINUTE);
		} else {
			final int zoneNumericId = 
					RawDataUtilities.getHighOrderBits(byte11) + 
					RawDataUtilities.getLowOrderBits(byte12);
			final String zoneName = TimeZoneMap.getName(zoneNumericId);
			if (zoneName == null) {
				throw new SQLException("Unable to find timezone name for ID=" + zoneNumericId);
			}
			final ZoneRules rules = ZoneId.of(zoneName).getRules();
			zoneOffset = rules.getOffset(instant);
		}
		return zoneOffset;
	}

}
