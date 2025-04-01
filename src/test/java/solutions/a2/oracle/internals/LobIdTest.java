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
package solutions.a2.oracle.internals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import static solutions.a2.oracle.utils.BinaryUtils.hexToRaw;

/**
 *  
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class LobIdTest {


	@Test
	public void test() {
		LobId lid01 = new LobId(
				hexToRaw(
						"00 70 00 01 01 0c 00 00 00 01 00 00 00 01 00 00 2f da fe b1 00 10 09 00 00 00 00 00 00 00 00 00 00 00 00 00"
							.replace(" ", "")), 0xA, 0xA);
		LobId lid02 = new LobId(
				hexToRaw(
						"00 00 00 01 00 00 2f da fe b1"
							.replace(" ", "")));
		assertTrue(lid01.equals(lid02));
		assertEquals(lid01.hashCode(), lid02.hashCode());
		assertEquals(lid01, lid02);

		LobId lid03 = new LobId(
				hexToRaw(
						"00 00 00 01 00 00 2f da fe b2"
							.replace(" ", "")));
		assertTrue(lid03.equals(lid03));
		assertFalse(lid03.equals(lid01));
		assertFalse(lid03.equals(lid02));
	}
}
