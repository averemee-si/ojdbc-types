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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static solutions.a2.oracle.jdbc.types.OracleDate.DATA_LENGTH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import oracle.sql.DATE;

/**
 *  
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class OracleDateTest {

	private static final int OFFSET = 2;


	@Test
	public void test() {
		try {
			final LocalDateTime ldt = LocalDateTime
					.now()
					.truncatedTo(ChronoUnit.SECONDS);
			final Timestamp ts = Timestamp.valueOf(ldt);			
			final DATE oraDate = new DATE(ts);
			System.out.println("Setting all DATE related information to " + ldt.toString());
	
			final byte[] byteArray = new byte[DATA_LENGTH];
			for (int i = 0; i < byteArray.length; i++) {
				byteArray[i] = oraDate.getBytes()[i];
			}
			final byte[] byteArrayWithOffset = new byte[DATA_LENGTH + OFFSET * 2];
			System.arraycopy(byteArray, 0, byteArrayWithOffset, OFFSET, DATA_LENGTH);

			final OracleDate odByte = new OracleDate(byteArray);

			assertTrue(ldt.isEqual(odByte.toLocalDateTime()));
			assertTrue(ldt.isEqual(OracleDate.toLocalDateTime(byteArray)));
			assertTrue(ldt.isEqual(OracleDate.toLocalDateTime(byteArrayWithOffset, OFFSET)));

			assertTrue(ts.equals(odByte.toTimestamp()));
			assertTrue(ts.equals(OracleDate.toTimestamp(byteArray)));
			assertTrue(ts.equals(OracleDate.toTimestamp(byteArrayWithOffset, OFFSET)));
			
			assertTrue(oraDate.timestampValue().equals(odByte.toTimestamp()));
			assertTrue(oraDate.timestampValue().equals(OracleDate.toTimestamp(byteArray)));
			assertTrue(oraDate.timestampValue().equals(OracleDate.toTimestamp(byteArrayWithOffset, OFFSET)));

			assertEquals(ldt.toString(), odByte.toString(), "Strings must be the same!");
			assertEquals(ldt.toString(), OracleDate.toString(byteArray), "Strings must be the same!");

		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}
}
