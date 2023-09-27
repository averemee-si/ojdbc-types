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
import java.time.Period;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import oracle.sql.INTERVALYM;

/**
 *  
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class IntervalYearToMonthTest {

	private static final int YEARS = 3;
	private static final int MONTHS = 7;

	@Test
	public void test() {
		// Period of 3 years and 7 months...
		// As int array
		final int[] intArray = new int[IntervalYearToMonth.DATA_LENGTH];
		intArray[0] = 128;
		intArray[1] = 0;
		intArray[2] = 0;
		intArray[3] = YEARS;
		intArray[4] = RawDataUtilities.ORA_INTERVAL_OFFSET + MONTHS;
		// As byte array
		final byte[] byteArray = new byte[IntervalYearToMonth.DATA_LENGTH];
		for (int i = 0; i < byteArray.length; i++) {
			byteArray[i] = (byte) intArray[i];
		}

		try {
			final Period period = Period.of(YEARS, MONTHS, 0);
			final INTERVALYM oraPeriod = new INTERVALYM(byteArray);
			final String[] yearAndMonth = oraPeriod.toString().split("-");
			final int years = Integer.valueOf(yearAndMonth[0]);
			final int months = Integer.valueOf(yearAndMonth[1]);
			assertEquals(years, YEARS, "Years from INTERVALYM must equal " + YEARS);
			assertEquals(months, MONTHS, "Months from INTERVALYM must equal " + MONTHS);

			final IntervalYearToMonth iymInt = new IntervalYearToMonth(intArray);
			System.out.println(iymInt);
			assertEquals(years, iymInt.getYears(), "Years from IntervalYearToMonth must equal " + YEARS);
			assertEquals(months, iymInt.getMonths(), "Months from IntervalYearToMonth must equal " + MONTHS);
			assertTrue(iymInt.toString().equals(period.toString()));
			assertTrue(IntervalYearToMonth.toPeriod(intArray).toString().equals(period.toString()));

			final IntervalYearToMonth iymByte = new IntervalYearToMonth(byteArray);
			System.out.println(iymByte);
			assertEquals(years, iymByte.getYears(), "Years from IntervalYearToMonth must equal " + YEARS);
			assertEquals(months, iymByte.getMonths(), "Months from IntervalYearToMonth must equal " + MONTHS);
			assertTrue(iymByte.toString().equals(period.toString()));
			assertTrue(IntervalYearToMonth.toPeriod(byteArray).toString().equals(period.toString()));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}
}
