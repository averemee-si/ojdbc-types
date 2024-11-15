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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 *  
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class RowIdTest {


	@Test
	public void test() {
		final String asString = "AAAqvfABcAAEXtqAAN";
		final RowId rowIdFromString = new RowId("AAAqvfABcAAEXtqAAN");
		final RowId rowIdFromObjBlkRow = new RowId(rowIdFromString.dataObj(), rowIdFromString.dataBlk(), rowIdFromString.rowNum());
		
		assertEquals(rowIdFromString.afn(), rowIdFromObjBlkRow.afn());
		assertEquals(rowIdFromString.hashCode(), rowIdFromObjBlkRow.hashCode());

		assertTrue(rowIdFromString.equals(rowIdFromObjBlkRow));
		assertTrue(rowIdFromObjBlkRow.equals(rowIdFromString));

		assertTrue(asString.equals(rowIdFromString.toString()));
		assertTrue(asString.equals(rowIdFromObjBlkRow.toString()));

		final byte[] ba = rowIdFromString.toByteArray();
		assertEquals(new RowId(ba), rowIdFromObjBlkRow);
	}
}
