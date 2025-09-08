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
package solutions.a2.oracle.utils;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static solutions.a2.oracle.utils.BinaryUtils.getU16BE;
import static solutions.a2.oracle.utils.BinaryUtils.getU24BE;
import static solutions.a2.oracle.utils.BinaryUtils.putU16;
import static solutions.a2.oracle.utils.BinaryUtils.putU24;


/**
 *  
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class BinaryUtilsPutGetTest {


	@Test
	public void test() {
		
		Random random = new Random(System.nanoTime());

		for (int i = 0; i < 100; i++) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			short s = (short) random.nextInt(1 << 16);
			try {
				putU16(baos, s);
			} catch (IOException ioe) {}
			assertEquals(s, getU16BE(baos.toByteArray(), 0));
		}

		for (int i = 0; i < 100; i++) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int j = random.nextInt(1 << 24);
			try {
				putU24(baos, j);
			} catch (IOException ioe) {}
			assertEquals(j, getU24BE(baos.toByteArray(), 0));
		}
	}

}
