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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.Random;

import org.junit.jupiter.api.Test;

import oracle.sql.NUMBER;

/**
 *  
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class OracleNumberFPTest {

	private static final int OFFSET = 3;

	@Test
	public void test() throws NumberFormatException, SQLException {
		final Random random = new Random();

		final byte[] ba_ZERO = new NUMBER(Double.parseDouble("0.0")).getBytes();
		assertEquals(NUMBER.toDouble(ba_ZERO), OracleNumber.toDouble(ba_ZERO));
		assertEquals(NUMBER.toFloat(ba_ZERO), OracleNumber.toFloat(ba_ZERO));
		final byte[] ba_ONE = new NUMBER(Double.parseDouble(".1E1")).getBytes();
		assertEquals(NUMBER.toDouble(ba_ONE), OracleNumber.toDouble(ba_ONE));
		assertEquals(NUMBER.toFloat(ba_ONE), OracleNumber.toFloat(ba_ONE));
		final byte[] ba_MINUSONE = new NUMBER(Double.parseDouble("-0.1E1")).getBytes();
		assertEquals(NUMBER.toDouble(ba_MINUSONE), OracleNumber.toDouble(ba_MINUSONE));
		assertEquals(NUMBER.toFloat(ba_MINUSONE), OracleNumber.toFloat(ba_MINUSONE));
		final byte[] ba_PI = new NUMBER(Math.PI).getBytes();
		assertEquals(NUMBER.toDouble(ba_PI), OracleNumber.toDouble(ba_PI));
		assertEquals(NUMBER.toFloat(ba_PI), OracleNumber.toFloat(ba_PI));
		final byte[] ba_MINUSPI = new NUMBER(((double)-1) * Math.PI).getBytes();
		assertEquals(NUMBER.toDouble(ba_MINUSPI), OracleNumber.toDouble(ba_MINUSPI));
		assertEquals(NUMBER.toFloat(ba_MINUSPI), OracleNumber.toFloat(ba_MINUSPI));
	
		final byte[] ba_shf_ZERO = new byte[ba_ZERO.length + OFFSET];
		System.arraycopy(ba_ZERO, 0, ba_shf_ZERO, OFFSET, ba_ZERO.length);
		assertEquals(NUMBER.toDouble(ba_ZERO), OracleNumber.toDouble(ba_shf_ZERO, OFFSET, ba_ZERO.length));
		assertEquals(NUMBER.toFloat(ba_ZERO), OracleNumber.toFloat(ba_shf_ZERO, OFFSET, ba_ZERO.length));
		final byte[] ba_shf_ONE = new byte[ba_ONE.length + OFFSET];
		System.arraycopy(ba_ONE, 0, ba_shf_ONE, OFFSET, ba_ONE.length);
		assertEquals(NUMBER.toDouble(ba_ONE), OracleNumber.toDouble(ba_shf_ONE, OFFSET, ba_ONE.length));
		assertEquals(NUMBER.toFloat(ba_ONE), OracleNumber.toFloat(ba_shf_ONE, OFFSET, ba_ONE.length));
		final byte[] ba_shf_MINUSONE = new byte[ba_MINUSONE.length + OFFSET];
		System.arraycopy(ba_MINUSONE, 0, ba_shf_MINUSONE, OFFSET, ba_MINUSONE.length);
		assertEquals(NUMBER.toDouble(ba_MINUSONE), OracleNumber.toDouble(ba_shf_MINUSONE, OFFSET, ba_MINUSONE.length));
		assertEquals(NUMBER.toFloat(ba_MINUSONE), OracleNumber.toFloat(ba_shf_MINUSONE, OFFSET, ba_MINUSONE.length));
		final byte[] ba_shf_PI = new byte[ba_PI.length + OFFSET];
		System.arraycopy(ba_PI, 0, ba_shf_PI, OFFSET, ba_PI.length);
		assertEquals(NUMBER.toDouble(ba_PI), OracleNumber.toDouble(ba_shf_PI, OFFSET, ba_PI.length));
		assertEquals(NUMBER.toFloat(ba_PI), OracleNumber.toFloat(ba_shf_PI, OFFSET, ba_PI.length));
		final byte[] ba_shf_MINUSPI = new byte[ba_MINUSPI.length + OFFSET];
		System.arraycopy(ba_MINUSPI, 0, ba_shf_MINUSPI, OFFSET, ba_MINUSPI.length);
		assertEquals(NUMBER.toDouble(ba_MINUSPI), OracleNumber.toDouble(ba_shf_MINUSPI, OFFSET, ba_MINUSPI.length));
		assertEquals(NUMBER.toFloat(ba_MINUSPI), OracleNumber.toFloat(ba_shf_MINUSPI, OFFSET, ba_MINUSPI.length));

		for (int i = 0; i < 100_000; i++) {
			double d = random.nextDouble();

			double d0 = d * random.nextInt();
			double d1 = d0 * -1.0d;
			float f0 = (float)d * random.nextInt();
			float f1 = f0 * -1.0f;

			byte[] bd0 = new NUMBER(d0).getBytes();
			byte[] bd1 = new NUMBER(d1).getBytes();
			byte[] bf0 = new NUMBER(f0).getBytes();
			byte[] bf1 = new NUMBER(f1).getBytes();

			byte[] sd0 = new byte[bd0.length + OFFSET * 2];
			System.arraycopy(bd0, 0, sd0, OFFSET, bd0.length);
			byte[] sd1 = new byte[bd1.length + OFFSET * 2];
			System.arraycopy(bd1, 0, sd1, OFFSET, bd1.length);
			byte[] sf0 = new byte[bf0.length + OFFSET * 2];
			System.arraycopy(bf0, 0, sf0, OFFSET, bf0.length);
			byte[] sf1 = new byte[bf1.length + OFFSET * 2];
			System.arraycopy(bf1, 0, sf1, OFFSET, bf1.length);

			assertEquals(NUMBER.toDouble(bd0), OracleNumber.toDouble(bd0));
			assertEquals(NUMBER.toDouble(bd1), OracleNumber.toDouble(bd1));
			assertEquals(NUMBER.toFloat(bf0), OracleNumber.toFloat(bf0));
			assertEquals(NUMBER.toFloat(bf1), OracleNumber.toFloat(bf1));
			assertEquals(NUMBER.toDouble(bd0), OracleNumber.toDouble(sd0, OFFSET, bd0.length));
			assertEquals(NUMBER.toDouble(bd1), OracleNumber.toDouble(sd1, OFFSET, bd1.length));
			assertEquals(NUMBER.toFloat(bf0), OracleNumber.toFloat(sf0, OFFSET, bf0.length));
			assertEquals(NUMBER.toFloat(bf1), OracleNumber.toFloat(sf1, OFFSET, bf1.length));
		}

	}
}
