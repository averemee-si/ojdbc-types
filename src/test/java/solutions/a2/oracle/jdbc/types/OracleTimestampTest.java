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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import oracle.sql.TIMESTAMP;

/**
 *  
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class OracleTimestampTest {


	@Test
	public void test() {
		try {
			final LocalDateTime ldt = LocalDateTime.now();
			final Timestamp ts = Timestamp.valueOf(ldt);			
			final TIMESTAMP oraDate = new TIMESTAMP(ts);
			System.out.println("Setting all TIMESTAMP related information to " + ldt.toString());
	
			final byte[] byteArray = new byte[OracleTimestamp.DATA_LENGTH];
			for (int i = 0; i < byteArray.length; i++) {
				byteArray[i] = oraDate.getBytes()[i];
			}
			final int[] intArray = new int[OracleTimestamp.DATA_LENGTH];
			for (int i = 0; i < intArray.length; i++) {
				intArray[i] = RawDataUtilities.unsigned(byteArray[i]);
			}

			final OracleTimestamp otInt = new OracleTimestamp(intArray);
			final OracleTimestamp otByte = new OracleTimestamp(byteArray);

			assertTrue(ldt.isEqual(otInt.toLocalDateTime()));
			assertTrue(ldt.isEqual(otByte.toLocalDateTime()));
			assertTrue(ldt.isEqual(OracleTimestamp.toLocalDateTime(intArray)));
			assertTrue(ldt.isEqual(OracleTimestamp.toLocalDateTime(byteArray)));

			assertTrue(ts.equals(otInt.toTimestamp()));
			assertTrue(ts.equals(otByte.toTimestamp()));
			assertTrue(ts.equals(OracleTimestamp.toTimestamp(intArray)));
			assertTrue(ts.equals(OracleTimestamp.toTimestamp(byteArray)));
			
			assertTrue(oraDate.timestampValue().equals(otInt.toTimestamp()));
			assertTrue(oraDate.timestampValue().equals(otByte.toTimestamp()));
			assertTrue(oraDate.timestampValue().equals(OracleTimestamp.toTimestamp(intArray)));
			assertTrue(oraDate.timestampValue().equals(OracleTimestamp.toTimestamp(byteArray)));

			assertEquals(ldt.toString(), otInt.toString(), "Strings must be the same!");
			assertEquals(ldt.toString(), otByte.toString(), "Strings must be the same!");
			assertEquals(ldt.toString(), OracleTimestamp.toString(intArray), "Strings must be the same!");
			assertEquals(ldt.toString(), OracleTimestamp.toString(byteArray), "Strings must be the same!");

		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}
}
