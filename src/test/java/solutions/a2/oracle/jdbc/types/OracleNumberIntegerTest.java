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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;

import oracle.sql.NUMBER;

/**
 *  
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class OracleNumberIntegerTest {

	private static final int OFFSET = 3;

	@Test
	public void test() {
		final Random random = new Random();

		final byte[] ba_ZERO = new NUMBER(0).getBytes();
		final byte[] ba_ONE = new NUMBER(1).getBytes();
		final byte[] ba_MINUSONE = new NUMBER(-1).getBytes();
		final byte[] ba_SEVEN = new NUMBER(7).getBytes();
		final byte[] ba_MINUSSEVEN = new NUMBER(-7).getBytes();
		final byte[] ba_LONG_MAX = new NUMBER(Long.MAX_VALUE).getBytes();
		assertEquals(new NUMBER(Long.MAX_VALUE), new NUMBER(OracleNumber.LONG_MAX_VALUE));
		final byte[] ba_LONG_MIN = new NUMBER(Long.MIN_VALUE).getBytes();
		assertEquals(new NUMBER(Long.MIN_VALUE), new NUMBER(OracleNumber.LONG_MIN_VALUE));
		final byte[] ba_INT_MAX = new NUMBER(Integer.MAX_VALUE).getBytes();
		assertEquals(new NUMBER(Integer.MAX_VALUE), new NUMBER(OracleNumber.INT_MAX_VALUE));
		final byte[] ba_INT_MIN = new NUMBER(Integer.MIN_VALUE).getBytes();
		assertEquals(new NUMBER(Integer.MIN_VALUE), new NUMBER(OracleNumber.INT_MIN_VALUE));
		final byte[] ba_SHORT_MAX = new NUMBER(Short.MAX_VALUE).getBytes();
		assertEquals(new NUMBER(Short.MAX_VALUE), new NUMBER(OracleNumber.SHORT_MAX_VALUE));
		final byte[] ba_SHORT_MIN = new NUMBER(Short.MIN_VALUE).getBytes();
		assertEquals(new NUMBER(Short.MIN_VALUE), new NUMBER(OracleNumber.SHORT_MIN_VALUE));
		final byte[] ba_BYTE_MAX = new NUMBER(Byte.MAX_VALUE).getBytes();
		assertEquals(new NUMBER(Byte.MAX_VALUE), new NUMBER(OracleNumber.BYTE_MAX_VALUE));
		final byte[] ba_BYTE_MIN = new NUMBER(Byte.MIN_VALUE).getBytes();
		assertEquals(new NUMBER(Byte.MIN_VALUE), new NUMBER(OracleNumber.BYTE_MIN_VALUE));

		final byte[] ba_shf_ZERO = new byte[ba_ZERO.length + OFFSET];
		System.arraycopy(ba_ZERO, 0, ba_shf_ZERO, OFFSET, ba_ZERO.length);
		final byte[] ba_shf_ONE = new byte[ba_ONE.length + OFFSET];
		System.arraycopy(ba_ONE, 0, ba_shf_ONE, OFFSET, ba_ONE.length);
		final byte[] ba_shf_MINUSONE = new byte[ba_MINUSONE.length + OFFSET];
		System.arraycopy(ba_MINUSONE, 0, ba_shf_MINUSONE, OFFSET, ba_MINUSONE.length);
		final byte[] ba_shf_SEVEN = new byte[ba_SEVEN.length + OFFSET];
		System.arraycopy(ba_SEVEN, 0, ba_shf_SEVEN, OFFSET, ba_SEVEN.length);
		final byte[] ba_shf_MINUSSEVEN = new byte[ba_MINUSSEVEN.length + OFFSET];
		System.arraycopy(ba_MINUSSEVEN, 0, ba_shf_MINUSSEVEN, OFFSET, ba_MINUSSEVEN.length);
		final byte[] ba_shf_LONG_MAX = new byte[ba_LONG_MAX.length + OFFSET];
		System.arraycopy(ba_LONG_MAX, 0, ba_shf_LONG_MAX, OFFSET, ba_LONG_MAX.length);
		final byte[] ba_shf_LONG_MIN = new byte[ba_LONG_MIN.length + OFFSET];
		System.arraycopy(ba_LONG_MIN, 0, ba_shf_LONG_MIN, OFFSET, ba_LONG_MIN.length);
		final byte[] ba_shf_INT_MAX = new byte[ba_INT_MAX.length + OFFSET];
		System.arraycopy(ba_INT_MAX, 0, ba_shf_INT_MAX, OFFSET, ba_INT_MAX.length);
		final byte[] ba_shf_INT_MIN = new byte[ba_INT_MIN.length + OFFSET];
		System.arraycopy(ba_INT_MIN, 0, ba_shf_INT_MIN, OFFSET, ba_INT_MIN.length);
		final byte[] ba_shf_SHORT_MAX = new byte[ba_SHORT_MAX.length + OFFSET];
		System.arraycopy(ba_SHORT_MAX, 0, ba_shf_SHORT_MAX, OFFSET, ba_SHORT_MAX.length);
		final byte[] ba_shf_SHORT_MIN = new byte[ba_SHORT_MIN.length + OFFSET];
		System.arraycopy(ba_SHORT_MIN, 0, ba_shf_SHORT_MIN, OFFSET, ba_SHORT_MIN.length);
		final byte[] ba_shf_BYTE_MAX = new byte[ba_BYTE_MAX.length + OFFSET];
		System.arraycopy(ba_BYTE_MAX, 0, ba_shf_BYTE_MAX, OFFSET, ba_BYTE_MAX.length);
		final byte[] ba_shf_BYTE_MIN = new byte[ba_BYTE_MIN.length + OFFSET];
		System.arraycopy(ba_BYTE_MIN, 0, ba_shf_BYTE_MIN, OFFSET, ba_BYTE_MIN.length);

		assertTrue(OracleNumber.compare(ba_ONE, ba_ZERO) > 0);
		assertTrue(OracleNumber.compare(ba_ZERO, ba_ONE) < 0);
		assertTrue(OracleNumber.compare(ba_ZERO, ba_ZERO) == 0);
		assertTrue(OracleNumber.compare(ba_ONE, ba_ONE) == 0);

		// long
		for (int i = 0; i < 1000; i++) {
			LongStream randomLongs = random.longs(1, Long.MAX_VALUE);
			long l = randomLongs.findAny().getAsLong();
			NUMBER n = new NUMBER(l);
			assertTrue(OracleNumber.compare(ba_ZERO, n.getBytes()) < 0);
			assertTrue(OracleNumber.compare(n.getBytes(), ba_ZERO) > 0);
			assertTrue(OracleNumber.compare(n.getBytes(), ba_LONG_MAX) <= 0);
			assertTrue(OracleNumber.compare(n.getBytes(), ba_LONG_MIN) > 0);
			assertEquals(OracleNumber.toLong(n.getBytes(), 0, n.getBytes().length), l);
		}
		assertEquals(OracleNumber.toLong(ba_ZERO, 0, ba_ZERO.length), 0l);
		assertEquals(OracleNumber.toLong(ba_ONE, 0, ba_ONE.length), 1l);
		assertEquals(OracleNumber.toLong(ba_MINUSONE, 0, ba_MINUSONE.length), -1l);
		assertEquals(OracleNumber.toLong(ba_SEVEN, 0, ba_SEVEN.length), 7l);
		assertEquals(OracleNumber.toLong(ba_MINUSSEVEN, 0, ba_MINUSSEVEN.length), -7l);
		assertEquals(OracleNumber.toLong(ba_LONG_MAX, 0, ba_LONG_MAX.length), Long.MAX_VALUE);
		assertEquals(OracleNumber.toLong(ba_LONG_MIN, 0, ba_LONG_MIN.length), Long.MIN_VALUE);
		assertEquals(OracleNumber.toLong(ba_INT_MAX, 0, ba_INT_MAX.length), Integer.MAX_VALUE);
		assertEquals(OracleNumber.toLong(ba_INT_MIN, 0, ba_INT_MIN.length), Integer.MIN_VALUE);
		assertEquals(OracleNumber.toLong(ba_SHORT_MAX, 0, ba_SHORT_MAX.length), Short.MAX_VALUE);
		assertEquals(OracleNumber.toLong(ba_SHORT_MIN, 0, ba_SHORT_MIN.length), Short.MIN_VALUE);
		assertEquals(OracleNumber.toLong(ba_BYTE_MAX, 0, ba_BYTE_MAX.length), Byte.MAX_VALUE);
		assertEquals(OracleNumber.toLong(ba_BYTE_MIN, 0, ba_BYTE_MIN.length), Byte.MIN_VALUE);

		assertEquals(OracleNumber.toLong(ba_shf_ZERO, OFFSET, ba_ZERO.length), 0l);
		assertEquals(OracleNumber.toLong(ba_shf_ONE, OFFSET, ba_ONE.length), 1l);
		assertEquals(OracleNumber.toLong(ba_shf_MINUSONE, OFFSET, ba_MINUSONE.length), -1l);
		assertEquals(OracleNumber.toLong(ba_shf_SEVEN, OFFSET, ba_SEVEN.length), 7l);
		assertEquals(OracleNumber.toLong(ba_shf_MINUSSEVEN, OFFSET, ba_MINUSSEVEN.length), -7l);
		assertEquals(OracleNumber.toLong(ba_shf_LONG_MAX, OFFSET, ba_LONG_MAX.length), Long.MAX_VALUE);
		assertEquals(OracleNumber.toLong(ba_shf_LONG_MIN, OFFSET, ba_LONG_MIN.length), Long.MIN_VALUE);
		assertEquals(OracleNumber.toLong(ba_shf_INT_MAX, OFFSET, ba_INT_MAX.length), Integer.MAX_VALUE);
		assertEquals(OracleNumber.toLong(ba_shf_INT_MIN, OFFSET, ba_INT_MIN.length), Integer.MIN_VALUE);
		assertEquals(OracleNumber.toLong(ba_shf_SHORT_MAX, OFFSET, ba_SHORT_MAX.length), Short.MAX_VALUE);
		assertEquals(OracleNumber.toLong(ba_shf_SHORT_MIN, OFFSET, ba_SHORT_MIN.length), Short.MIN_VALUE);
		assertEquals(OracleNumber.toLong(ba_shf_BYTE_MAX, OFFSET, ba_BYTE_MAX.length), Byte.MAX_VALUE);
		assertEquals(OracleNumber.toLong(ba_shf_BYTE_MIN, OFFSET, ba_BYTE_MIN.length), Byte.MIN_VALUE);

		// int
		for (int i = 0; i < 1000; i++) {
			int l = random.nextInt(Integer.MAX_VALUE - 1) + 1;
			NUMBER n = new NUMBER(l);
			assertTrue(OracleNumber.compare(ba_ZERO, n.getBytes()) < 0);
			assertTrue(OracleNumber.compare(n.getBytes(), ba_ZERO) > 0);
			assertTrue(OracleNumber.compare(n.getBytes(), ba_LONG_MAX) <= 0);
			assertTrue(OracleNumber.compare(n.getBytes(), ba_LONG_MIN) > 0);
			assertEquals(OracleNumber.toInt(n.getBytes(), 0, n.getBytes().length), l);
		}
		assertEquals(OracleNumber.toInt(ba_ZERO, 0, ba_ZERO.length), 0l);
		assertEquals(OracleNumber.toInt(ba_ONE, 0, ba_ONE.length), 1l);
		assertEquals(OracleNumber.toInt(ba_MINUSONE, 0, ba_MINUSONE.length), -1l);
		assertEquals(OracleNumber.toInt(ba_SEVEN, 0, ba_SEVEN.length), 7l);
		assertEquals(OracleNumber.toInt(ba_MINUSSEVEN, 0, ba_MINUSSEVEN.length), -7l);
		assertEquals(OracleNumber.toInt(ba_INT_MAX, 0, ba_INT_MAX.length), Integer.MAX_VALUE);
		assertEquals(OracleNumber.toInt(ba_INT_MIN, 0, ba_INT_MIN.length), Integer.MIN_VALUE);
		assertEquals(OracleNumber.toInt(ba_SHORT_MAX, 0, ba_SHORT_MAX.length), Short.MAX_VALUE);
		assertEquals(OracleNumber.toInt(ba_SHORT_MIN, 0, ba_SHORT_MIN.length), Short.MIN_VALUE);
		assertEquals(OracleNumber.toInt(ba_BYTE_MAX, 0, ba_BYTE_MAX.length), Byte.MAX_VALUE);
		assertEquals(OracleNumber.toInt(ba_BYTE_MIN, 0, ba_BYTE_MIN.length), Byte.MIN_VALUE);

		assertEquals(OracleNumber.toInt(ba_shf_MINUSONE, OFFSET, ba_MINUSONE.length), -1l);
		assertEquals(OracleNumber.toInt(ba_shf_SEVEN, OFFSET, ba_SEVEN.length), 7l);
		assertEquals(OracleNumber.toInt(ba_shf_MINUSSEVEN, OFFSET, ba_MINUSSEVEN.length), -7l);
		assertEquals(OracleNumber.toInt(ba_shf_INT_MAX, OFFSET, ba_INT_MAX.length), Integer.MAX_VALUE);
		assertEquals(OracleNumber.toInt(ba_shf_INT_MIN, OFFSET, ba_INT_MIN.length), Integer.MIN_VALUE);
		assertEquals(OracleNumber.toInt(ba_shf_SHORT_MAX, OFFSET, ba_SHORT_MAX.length), Short.MAX_VALUE);
		assertEquals(OracleNumber.toInt(ba_shf_SHORT_MIN, OFFSET, ba_SHORT_MIN.length), Short.MIN_VALUE);
		assertEquals(OracleNumber.toInt(ba_shf_BYTE_MAX, OFFSET, ba_BYTE_MAX.length), Byte.MAX_VALUE);
		assertEquals(OracleNumber.toInt(ba_shf_BYTE_MIN, OFFSET, ba_BYTE_MIN.length), Byte.MIN_VALUE);

		// short
		for (int i = 0; i < 1000; i++) {
			short l = (short) (random.nextInt(Short.MAX_VALUE - 1) + 1);
			NUMBER n = new NUMBER(l);
			assertTrue(OracleNumber.compare(ba_ZERO, n.getBytes()) < 0);
			assertTrue(OracleNumber.compare(n.getBytes(), ba_ZERO) > 0);
			assertTrue(OracleNumber.compare(n.getBytes(), ba_LONG_MAX) <= 0);
			assertTrue(OracleNumber.compare(n.getBytes(), ba_LONG_MIN) > 0);
			assertEquals(OracleNumber.toShort(n.getBytes(), 0, n.getBytes().length), l);
		}
		assertEquals(OracleNumber.toShort(ba_ZERO, 0, ba_ZERO.length), 0l);
		assertEquals(OracleNumber.toShort(ba_ONE, 0, ba_ONE.length), 1l);
		assertEquals(OracleNumber.toShort(ba_MINUSONE, 0, ba_MINUSONE.length), -1l);
		assertEquals(OracleNumber.toShort(ba_SEVEN, 0, ba_SEVEN.length), 7l);
		assertEquals(OracleNumber.toShort(ba_MINUSSEVEN, 0, ba_MINUSSEVEN.length), -7l);
		assertEquals(OracleNumber.toShort(ba_SHORT_MAX, 0, ba_SHORT_MAX.length), Short.MAX_VALUE);
		assertEquals(OracleNumber.toShort(ba_SHORT_MIN, 0, ba_SHORT_MIN.length), Short.MIN_VALUE);
		assertEquals(OracleNumber.toShort(ba_BYTE_MAX, 0, ba_BYTE_MAX.length), Byte.MAX_VALUE);
		assertEquals(OracleNumber.toShort(ba_BYTE_MIN, 0, ba_BYTE_MIN.length), Byte.MIN_VALUE);

		assertEquals(OracleNumber.toShort(ba_shf_MINUSONE, OFFSET, ba_MINUSONE.length), -1l);
		assertEquals(OracleNumber.toShort(ba_shf_SEVEN, OFFSET, ba_SEVEN.length), 7l);
		assertEquals(OracleNumber.toShort(ba_shf_MINUSSEVEN, OFFSET, ba_MINUSSEVEN.length), -7l);
		assertEquals(OracleNumber.toShort(ba_shf_SHORT_MAX, OFFSET, ba_SHORT_MAX.length), Short.MAX_VALUE);
		assertEquals(OracleNumber.toShort(ba_shf_SHORT_MIN, OFFSET, ba_SHORT_MIN.length), Short.MIN_VALUE);
		assertEquals(OracleNumber.toShort(ba_shf_BYTE_MAX, OFFSET, ba_BYTE_MAX.length), Byte.MAX_VALUE);
		assertEquals(OracleNumber.toShort(ba_shf_BYTE_MIN, OFFSET, ba_BYTE_MIN.length), Byte.MIN_VALUE);

		// byte
		for (int i = 0; i < 1000; i++) {
			byte l = (byte) (random.nextInt(Byte.MAX_VALUE - 1) + 1);
			NUMBER n = new NUMBER(l);
			assertTrue(OracleNumber.compare(ba_ZERO, n.getBytes()) < 0);
			assertTrue(OracleNumber.compare(n.getBytes(), ba_ZERO) > 0);
			assertTrue(OracleNumber.compare(n.getBytes(), ba_LONG_MAX) <= 0);
			assertTrue(OracleNumber.compare(n.getBytes(), ba_LONG_MIN) > 0);
			assertEquals(OracleNumber.toByte(n.getBytes(), 0, n.getBytes().length), l);
		}
		assertEquals(OracleNumber.toByte(ba_ZERO, 0, ba_ZERO.length), 0l);
		assertEquals(OracleNumber.toByte(ba_ONE, 0, ba_ONE.length), 1l);
		assertEquals(OracleNumber.toByte(ba_MINUSONE, 0, ba_MINUSONE.length), -1l);
		assertEquals(OracleNumber.toByte(ba_SEVEN, 0, ba_SEVEN.length), 7l);
		assertEquals(OracleNumber.toByte(ba_MINUSSEVEN, 0, ba_MINUSSEVEN.length), -7l);
		assertEquals(OracleNumber.toByte(ba_BYTE_MAX, 0, ba_BYTE_MAX.length), Byte.MAX_VALUE);
		assertEquals(OracleNumber.toByte(ba_BYTE_MIN, 0, ba_BYTE_MIN.length), Byte.MIN_VALUE);

		assertEquals(OracleNumber.toByte(ba_shf_MINUSONE, OFFSET, ba_MINUSONE.length), -1l);
		assertEquals(OracleNumber.toByte(ba_shf_SEVEN, OFFSET, ba_SEVEN.length), 7l);
		assertEquals(OracleNumber.toByte(ba_shf_MINUSSEVEN, OFFSET, ba_MINUSSEVEN.length), -7l);
		assertEquals(OracleNumber.toByte(ba_shf_BYTE_MAX, OFFSET, ba_BYTE_MAX.length), Byte.MAX_VALUE);
		assertEquals(OracleNumber.toByte(ba_shf_BYTE_MIN, OFFSET, ba_BYTE_MIN.length), Byte.MIN_VALUE);

	}
}
