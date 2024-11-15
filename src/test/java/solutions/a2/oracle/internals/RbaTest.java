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

import org.junit.jupiter.api.Test;

/**
 *  
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class RbaTest {


	@Test
	public void test() {
		
		final String withSpaces = " 0x003004.00414b19.0034 ";
		final String noSpaces = "0x003004.00414b19.0034";

		final RedoByteAddress rba1  = RedoByteAddress.fromLogmnrContentsRs_Id(withSpaces);
		final RedoByteAddress rba2  = RedoByteAddress.fromLogmnrContentsRs_Id(noSpaces);
		
		assertEquals(rba1, rba2);
		assertEquals(rba1.hashCode(), rba2.hashCode());

		final RedoByteAddress rba3  = new RedoByteAddress(rba1.sqn(), rba1.blk(), rba1.offset());
		assertEquals(rba3, rba1);
		assertEquals(rba3, rba2);
		assertEquals(rba3.hashCode(), rba1.hashCode());
		assertEquals(rba3.hashCode(), rba2.hashCode());

	}
}
