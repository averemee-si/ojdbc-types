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

import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import oracle.sql.INTERVALDS;

/**
 *  
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class IntervalDayToSecondTest {

	private static final int DAYS = 77;
	private static final int HOURS = 14;
	private static final int MINUTES = 33;
	private static final int SECONDS = 15;
	private final static int OFFSET = 7;

	@Test
	public void test() {
		// Duration of 77 days, 14 hours, 33 minutes, and 15 seconds...
		final byte[] byteArray = new byte[IntervalDayToSecond.DATA_LENGTH];
		byteArray[0] = (byte) 0x80;
		byteArray[1] = 0;
		byteArray[2] = 0;
		byteArray[3] = DAYS;
		byteArray[4] = Interval.ORA_INTERVAL_OFFSET + HOURS;
		byteArray[5] = Interval.ORA_INTERVAL_OFFSET + MINUTES;
		byteArray[6] = Interval.ORA_INTERVAL_OFFSET + SECONDS;
		byteArray[7] = (byte) 0x80;
		byteArray[8] = 0;
		byteArray[9] = 0;
		byteArray[10] = 0;
		final byte[] byteArrayWithOffset = new byte[byteArray.length + OFFSET * 3];
		System.arraycopy(byteArray, 0, byteArrayWithOffset, OFFSET, byteArray.length);

		try {
			final Duration duration = Duration.ZERO
					.plusDays(DAYS)
					.plusHours(HOURS)
					.plusMinutes(MINUTES)
					.plusSeconds(SECONDS);
			final INTERVALDS oraDuration = new INTERVALDS(byteArray);
			final int days = Integer.valueOf(oraDuration.toString().split(" ")[0]);
			final String[] timeValues = oraDuration.toString().split(" ")[1].split(":");
			final int hours = Integer.valueOf(timeValues[0]);
			final int minutes = Integer.valueOf(timeValues[1]);
			final int seconds = Integer.valueOf(timeValues[2].substring(0, timeValues[2].indexOf(".")));
			assertEquals(days, DAYS, "Days from INTERVALDS must equal " + DAYS);
			assertEquals(hours, HOURS, "Hours from INTERVALDS must equal " + HOURS);
			assertEquals(minutes, MINUTES, "Minutes from INTERVALDS must equal " + MINUTES);
			assertEquals(seconds, SECONDS, "Seconds from INTERVALDS must equal " + SECONDS);

			final IntervalDayToSecond idsByte = new IntervalDayToSecond(byteArray);
			System.out.println(idsByte);
			assertEquals(days, idsByte.getDays(), "Days from IntervalDayToSecond must equal " + DAYS);
			assertEquals(hours, idsByte.getHours(), "Hours from IntervalDayToSecond must equal " + HOURS);
			assertEquals(minutes, idsByte.getMinutes(), "Minutes from IntervalDayToSecond must equal " + MINUTES);
			assertEquals(seconds, idsByte.getSeconds(), "Seconds from IntervalDayToSecond must equal " + SECONDS);
			assertTrue(IntervalDayToSecond.toDuration(byteArray).toString().equals(duration.toString()));
			assertTrue(Duration.parse(idsByte.toString()).equals(duration));

			final IntervalDayToSecond idsByteOffset = new IntervalDayToSecond(byteArrayWithOffset, OFFSET);
			System.out.println(idsByteOffset);
			assertEquals(days, idsByteOffset.getDays(), "Days from IntervalDayToSecond must equal " + DAYS);
			assertEquals(hours, idsByteOffset.getHours(), "Hours from IntervalDayToSecond must equal " + HOURS);
			assertEquals(minutes, idsByteOffset.getMinutes(), "Minutes from IntervalDayToSecond must equal " + MINUTES);
			assertEquals(seconds, idsByteOffset.getSeconds(), "Seconds from IntervalDayToSecond must equal " + SECONDS);
			assertTrue(IntervalDayToSecond.toDuration(byteArrayWithOffset, OFFSET).toString().equals(duration.toString()));
			assertTrue(Duration.parse(idsByteOffset.toString()).equals(duration));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}
}
