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
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import oracle.sql.TIMESTAMPTZ;

/**
 *  
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class TimestampWithTimeZoneTest {


	@Test
	public void test() {
		try {
			final ZonedDateTime zdt = ZonedDateTime.now();
			final OffsetDateTime odt = zdt.toOffsetDateTime();
			final TIMESTAMPTZ oraDate = new TIMESTAMPTZ(zdt);
			final Timestamp ts = Timestamp.from(zdt.toInstant());
			System.out.println("Setting all TIMESTAMPTZ related information to " + zdt.toString());
	
			final byte[] byteArray = new byte[TimestampWithTimeZone.DATA_LENGTH];
			for (int i = 0; i < byteArray.length; i++) {
				byteArray[i] = oraDate.getBytes()[i];
			}
			final int[] intArray = new int[TimestampWithTimeZone.DATA_LENGTH];
			for (int i = 0; i < intArray.length; i++) {
				intArray[i] = RawDataUtilities.unsigned(byteArray[i]);
			}

			final TimestampWithTimeZone otInt = new TimestampWithTimeZone(intArray);
			final TimestampWithTimeZone otByte = new TimestampWithTimeZone(byteArray);

			assertTrue(zdt.isEqual(otInt.toZonedDateTime()));
			assertTrue(zdt.isEqual(otByte.toZonedDateTime()));
			assertTrue(zdt.isEqual(TimestampWithTimeZone.toZonedDateTime(intArray)));
			assertTrue(zdt.isEqual(TimestampWithTimeZone.toZonedDateTime(byteArray)));

			assertTrue(ts.equals(otInt.toTimestamp()));
			assertTrue(ts.equals(otByte.toTimestamp()));
			assertTrue(ts.equals(TimestampWithTimeZone.toTimestamp(intArray)));
			assertTrue(ts.equals(TimestampWithTimeZone.toTimestamp(byteArray)));

			assertEquals(zdt.toString(), otInt.toString(), "Strings must be the same!");
			assertEquals(zdt.toString(), otByte.toString(), "Strings must be the same!");
			assertEquals(zdt.toString(), TimestampWithTimeZone.toString(intArray), "Strings must be the same!");
			assertEquals(zdt.toString(), TimestampWithTimeZone.toString(byteArray), "Strings must be the same!");

			assertTrue(odt.isEqual(otInt.toOffsetDateTime()));
			assertTrue(odt.isEqual(otByte.toOffsetDateTime()));
			assertTrue(odt.isEqual(TimestampWithTimeZone.toOffsetDateTime(intArray)));
			assertTrue(odt.isEqual(TimestampWithTimeZone.toOffsetDateTime(byteArray)));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}
}
