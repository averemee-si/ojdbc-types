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
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import oracle.sql.NUMBER;

/**
 *  
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class UnsignedLongTest {


	@Test
	public void test() {
		try {

			final Random random = new Random(System.nanoTime());

			System.out.println("\n===========\nStep 1: Check various (2**64 - 1) representations");
			final NUMBER SCN_MAX_19_5_NUMBER = new NUMBER(UnsignedLong.MAX_64_BIT_AS_DECIMAL_STRING);
			final long SCN_MAX_19_5_LONG = Long.parseUnsignedLong(UnsignedLong.MAX_64_BIT_AS_DECIMAL_STRING);
			final UnsignedLong SCN_MAX_19_5_UL = new UnsignedLong(SCN_MAX_19_5_NUMBER.getBytes()); 
			assertEquals(
					SCN_MAX_19_5_NUMBER.stringValue(),
					Long.toUnsignedString(SCN_MAX_19_5_LONG),
					"Strings must be the same!");
			assertEquals(
					SCN_MAX_19_5_NUMBER.stringValue(),
					SCN_MAX_19_5_UL.toString(),
					"Strings must be the same!");
			assertEquals(
					Long.toUnsignedString(SCN_MAX_19_5_LONG),
					SCN_MAX_19_5_UL.toString(),
					"Strings must be the same!");
			assertEquals(
					SCN_MAX_19_5_LONG,
					SCN_MAX_19_5_UL.toLong(),
					"Values must be the same!");

			System.out.println("\n===========\nStep 2: Check Some values greater than Long.MAX_VALUE");
			long unsignedLongTestValue = Long.MAX_VALUE;
			long counter = 0;
			long elapsed = System.currentTimeMillis();

			for (long i = 0; i < 100_000_000; i += 13) {
				counter++;
				unsignedLongTestValue += i;
				final String asString = Long.toUnsignedString(unsignedLongTestValue);
				NUMBER testNUMBER = new NUMBER(asString);
				UnsignedLong testUl = new UnsignedLong(testNUMBER.getBytes());
				assertEquals(
						testNUMBER.stringValue(),
						testUl.toString(),
						"Strings must be the same!");
				assertEquals(
						unsignedLongTestValue,
						testUl.toLong(),
						"Values must be the same!");
			}
			elapsed = System.currentTimeMillis() - elapsed;
			System.out.println("Successfully tested " + counter + " unsigned longs in " + elapsed + " ms.");

			System.out.println("\n===========\nStep 3: Back conversion of 10_000_000 random long's in range 0..Long.MAX_VALUE to NUMBER");
			elapsed = System.currentTimeMillis();
			counter = 0;
			for (long i = 0; i < 10_000_000L; i++) {
				counter++;
				long randomLong = random.nextLong();
				randomLong = randomLong == Long.MIN_VALUE ? 0 : randomLong < 0 ? -randomLong : randomLong;
				final UnsignedLong testUl = new UnsignedLong(randomLong);
				final NUMBER testNumber = new NUMBER(testUl.toByteArray());
				assertEquals(
						testNumber.longValue(),
						testUl.toLong(),
						"Values must be the same!");
			}
			elapsed = System.currentTimeMillis() - elapsed;
			System.out.println("Successfully tested " + counter + " unsigned longs in " + elapsed + " ms.");
			
			System.out.println("\n===========\nStep 4: Back conversion of longs greater than Long.MAX_VALUE to NUMBER");
			unsignedLongTestValue = Long.MAX_VALUE;
			elapsed = System.currentTimeMillis();
			counter = 0;
			for (long i = 0; i < 10_000_000_000_000_000L; i += 433_494_437) {
				counter++;
				unsignedLongTestValue = Long.MAX_VALUE + i;
				final UnsignedLong testUl = new UnsignedLong(unsignedLongTestValue);
				final NUMBER testNumber = new NUMBER(testUl.toBigInteger());
				assertEquals(
						testNumber.stringValue(),
						testUl.toString(),
						"Values must be the same!");
			}
			elapsed = System.currentTimeMillis() - elapsed;
			System.out.println("Successfully tested " + counter + " unsigned longs in " + elapsed + " ms.");
			System.out.println("Reached " + Long.toUnsignedString(unsignedLongTestValue));

			System.out.println("\n===========\nStep 5: Back conversion of longs between 18446744073700000000 and 18446744073709551615 to NUMBER");
			unsignedLongTestValue = Long.parseUnsignedLong("18446744073700000000");
			elapsed = System.currentTimeMillis();
			counter = 0;
			while (Long.compare(unsignedLongTestValue, UnsignedLong.MAX_VALUE) < 0) {
				final UnsignedLong testUl = new UnsignedLong(unsignedLongTestValue);
				final NUMBER testNumber = new NUMBER(testUl.toByteArray());
				assertEquals(
						testNumber.stringValue(),
						testUl.toString(),
						"Values must be the same!");
				counter++;
				unsignedLongTestValue = unsignedLongTestValue + 1;
			}
			elapsed = System.currentTimeMillis() - elapsed;
			System.out.println("Successfully tested " + counter + " unsigned longs in " + elapsed + " ms.");
			System.out.println("Reached " + Long.toUnsignedString(unsignedLongTestValue));
	
	
			System.out.println("\n===========\nStep 6: Back conversion of 0, 100, 1000, Long.MAX_VALUE, and UnsignedLong.MAX_VALUE to NUMBER");
			final UnsignedLong ulZero = new UnsignedLong(0);
			System.out.println((new NUMBER(ulZero.toByteArray())).stringValue());
			assertEquals(
					(new NUMBER(ulZero.toByteArray())).stringValue(),
					ulZero.toString(),
					"Values must be the same!");
			final UnsignedLong ulHundred = new UnsignedLong(100);
			System.out.println((new NUMBER(ulHundred.toByteArray())).stringValue());
			assertEquals(
					(new NUMBER(ulHundred.toByteArray())).stringValue(),
					ulHundred.toString(),
					"Values must be the same!");
			final UnsignedLong ulThousand = new UnsignedLong(1000);
			System.out.println((new NUMBER(ulThousand.toByteArray())).stringValue());
			assertEquals(
					(new NUMBER(ulThousand.toByteArray())).stringValue(),
					ulThousand.toString(),
					"Values must be the same!");
			final UnsignedLong ulSignedMaxValue = new UnsignedLong(Long.MAX_VALUE);
			System.out.println((new NUMBER(ulSignedMaxValue.toByteArray())).stringValue());
			assertEquals(
					(new NUMBER(ulSignedMaxValue.toByteArray())).stringValue(),
					ulSignedMaxValue.toString(),
					"Values must be the same!");
			final UnsignedLong ulUnsignedMaxValue = new UnsignedLong(UnsignedLong.MAX_VALUE);
			System.out.println((new NUMBER(ulUnsignedMaxValue.toByteArray())).stringValue());
			assertEquals(
					(new NUMBER(ulUnsignedMaxValue.toByteArray())).stringValue(),
					ulUnsignedMaxValue.toString(),
					"Values must be the same!");

		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception " + e.getMessage());
		}
	}

}
