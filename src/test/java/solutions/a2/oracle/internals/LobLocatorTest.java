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

import static solutions.a2.oracle.internals.LobLocator.BLOB;
import static solutions.a2.oracle.internals.LobLocator.CLOB;
import static solutions.a2.oracle.internals.LobLocator.NCLOB;
import static solutions.a2.oracle.utils.BinaryUtils.hexToRaw;

/**
 *  
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class LobLocatorTest {


	@Test
	public void test() {
		LobLocator ll;
		
		//
		// BasicFile
		//
		// Empty BLOB, row storage enabled, 36 bytes
		ll = new LobLocator(hexToRaw(
					"00700001010c000000010000000100002fdafeb100100900000000000000000000000000"
						));
		assertEquals(BLOB, ll.type());
		assertFalse(ll.secureFile());
		assertEquals(0, ll.dataLength());
		assertFalse(ll.dataInRow());
		// Empty BLOB,  row storage disabled, 20 bytes
		ll = new LobLocator(hexToRaw(
					"007000010108000000010000000100002fdafeb2"
						));
		assertEquals(BLOB, ll.type());
		assertFalse(ll.secureFile());
		assertEquals(0, ll.dataLength());
		assertFalse(ll.dataInRow());
		// Empty CLOB, row storage enabled, 36 bytes
		ll = new LobLocator(hexToRaw(
					"00700001020c800000020000000100002fdafeb300100900000000000000000000000000"
						));
		assertEquals(CLOB, ll.type());
		assertFalse(ll.secureFile());
		assertEquals(0, ll.dataLength());
		assertFalse(ll.dataInRow());
		// Empty CLOB, row storage disabled, 20 bytes
		ll = new LobLocator(hexToRaw(
					"007000010208800000020000000100002fdafeb4"
						));
		assertEquals(CLOB, ll.type());
		assertFalse(ll.secureFile());
		assertEquals(0, ll.dataLength());
		assertFalse(ll.dataInRow());
		// Empty NCLOB, row storage enabled, 36 bytes
		ll = new LobLocator(hexToRaw(
					"00700001040c000000020000000100002fdafeb500100900000000000000000000000000"
						));
		assertEquals(NCLOB, ll.type());
		assertFalse(ll.secureFile());
		assertEquals(0, ll.dataLength());
		assertFalse(ll.dataInRow());
		// Empty NCLOB, row storage disabled, 20 bytes
		ll = new LobLocator(hexToRaw(
					"007000010408000000020000000100002fdafeb6"
						));
		assertEquals(NCLOB, ll.type());
		assertFalse(ll.secureFile());
		assertEquals(0, ll.dataLength());
		assertFalse(ll.dataInRow());
		// BLOB with 100 bytes of data , row storage enabled
		ll = new LobLocator(hexToRaw(
					"00700001010c00000001000000010000001060ff0074090000000000006400000000000145454545454545454545454545454545454545454545454545454545454545454545454545454545454545454545454545454545454545454545454545454545454545454545454545454545454545454545454545454545454545454545454545454545"
						));
		assertEquals(BLOB, ll.type());
		assertFalse(ll.secureFile());
		assertEquals(0x64, ll.dataLength());
		assertTrue(ll.dataInRow());
		// BLOB with 100 bytes of data , row storage disabled
		ll = new LobLocator(hexToRaw(
					"0070000101080000000100000001000000106100"
						));
		assertEquals(BLOB, ll.type());
		assertFalse(ll.secureFile());
		assertEquals(0, ll.dataLength());
		assertFalse(ll.dataInRow());
		// CLOB with 100 chars of data , row storage enabled
		ll = new LobLocator(hexToRaw(
					"00700001020c800000020000000100000010610100d809000000000000c80000000000010045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045"
						));
		assertEquals(CLOB, ll.type());
		assertFalse(ll.secureFile());
		assertEquals(0xC8, ll.dataLength());
		assertTrue(ll.dataInRow());
		// CLOB with 100 chars of data , row storage disabled
		ll = new LobLocator(hexToRaw(
					"0070000102088000000200000001000000106102"
						));
		assertEquals(CLOB, ll.type());
		assertFalse(ll.secureFile());
		assertEquals(0, ll.dataLength());
		assertFalse(ll.dataInRow());
		// NCLOB with 100 chars of data , row storage enabled
		ll = new LobLocator(hexToRaw(
					"00700001040c000000020000000100000010610300d809000000000000c80000000000010045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045004500450045"
						));
		assertEquals(NCLOB, ll.type());
		assertFalse(ll.secureFile());
		assertEquals(0xC8, ll.dataLength());
		assertTrue(ll.dataInRow());
		// NCLOB with 100 chars of data , row storage disabled
		ll = new LobLocator(hexToRaw(
					"0070000104080000000200000001000000106104"
						));
		assertEquals(NCLOB, ll.type());
		assertFalse(ll.secureFile());
		assertEquals(0, ll.dataLength());
		assertFalse(ll.dataInRow());
		//
		// SecureFile
		//
		// Empty BLOB, row storage enabled, 30 bytes
		ll = new LobLocator(hexToRaw(
				"00700001010c0080000100000001000000106527000a4890000400000000"
					));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertEquals(0, ll.dataLength());
		assertTrue(ll.dataInRow());
		// Empty BLOB, row storage disabled, 30 bytes
		ll = new LobLocator(hexToRaw(
				"00700001010c0080000100000001000000106528000a4090000420000000"
					));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertEquals(0, ll.dataLength());
		assertFalse(ll.dataInRow());
		// Empty CLOB, row storage enabled, 30 bytes
		ll = new LobLocator(hexToRaw(
				"00700001020c8080000200000001000000106529000a4890000400000000"
					));
		assertEquals(CLOB, ll.type());
		assertTrue(ll.secureFile());
		assertEquals(0, ll.dataLength());
		assertTrue(ll.dataInRow());
		// Empty CLOB, row storage disabled, 30 bytes
		ll = new LobLocator(hexToRaw(
				"00700001020c808000020000000100000010652a000a4090000420000000"
					));
		assertEquals(CLOB, ll.type());
		assertTrue(ll.secureFile());
		assertEquals(0, ll.dataLength());
		assertFalse(ll.dataInRow());
		// Empty NCLOB, row storage enabled, 30 bytes
		ll = new LobLocator(hexToRaw(
				"00700001040c008000020000000100000010652b000a4890000400000000"
					));
		assertEquals(NCLOB, ll.type());
		assertTrue(ll.secureFile());
		assertEquals(0, ll.dataLength());
		assertTrue(ll.dataInRow());
		// Empty NCLOB, row storage disabled, 30 bytes
		ll = new LobLocator(hexToRaw(
				"00700001040c008000020000000100000010652c000a4090000420000000"
					));
		assertEquals(NCLOB, ll.type());
		assertTrue(ll.secureFile());
		assertEquals(0, ll.dataLength());
		assertFalse(ll.dataInRow());
		// Empty BLOB, row storage disabled, NOCOMPRESS, 30 bytes
		ll = new LobLocator(hexToRaw(
				"00700001010c008000010000000100000010652d000a4090000420000000"
					));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertEquals(0, ll.dataLength());
		assertFalse(ll.dataInRow());
		// Empty BLOB, row storage disabled, COMPRESS, 30 bytes
		ll = new LobLocator(hexToRaw(
				"00700001010c008000010000000100000010652e000a4090000420000000"
					));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertEquals(0, ll.dataLength());
		assertFalse(ll.dataInRow());
		// Empty BLOB, row storage disabled, COMPRESS MEDIUM, 30 bytes
		ll = new LobLocator(hexToRaw(
				"00700001010c008000010000000100000010652f000a4090000420000000"
					));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertEquals(0, ll.dataLength());
		assertFalse(ll.dataInRow());
		// Empty BLOB, row storage disabled, COMPRESS HIGH, 30 bytes
		ll = new LobLocator(hexToRaw(
				"00700001010c0080000100000001000000106530000a4090000420000000"
					));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertEquals(0, ll.dataLength());
		assertFalse(ll.dataInRow());
		// BLOB with 100 bytes of data , row storage enabled
		ll = new LobLocator(hexToRaw(
				"00700001010c0080000100000001000000106531006e489000680000640147474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747"
					));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertFalse(ll.compressionEnabled());
		assertEquals(0x64, ll.dataLength());
		assertTrue(ll.dataInRow());
		// BLOB with 100 bytes of data , row storage disabled
		ll = new LobLocator(hexToRaw(
				"00700001010c008000010000000100000010653200114090000b200064010000030002f301"
					));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertFalse(ll.compressionEnabled());
		assertEquals(0x64, ll.dataLength());
		assertFalse(ll.dataInRow());
		// CLOB with 100 chars of data , row storage enabled
		ll = new LobLocator(hexToRaw(
				"00700001020c808000020000000100000010653300d2489000cc0000c8010047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047"
					));
		assertEquals(CLOB, ll.type());
		assertTrue(ll.secureFile());
		assertFalse(ll.compressionEnabled());
		assertEquals(0xC8, ll.dataLength());
		assertTrue(ll.dataInRow());
		// CLOB with 100 chars of data , row storage disabled
		ll = new LobLocator(hexToRaw(
				"00700001020c808000020000000100000010653400114090000b2000c8010000030006a301"
					));
		assertEquals(CLOB, ll.type());
		assertTrue(ll.secureFile());
		assertFalse(ll.compressionEnabled());
		assertEquals(0xC8, ll.dataLength());
		assertFalse(ll.dataInRow());
		// NCLOB with 100 chars of data , row storage enabled
		ll = new LobLocator(hexToRaw(
				"00700001040c008000020000000100000010653500d2489000cc0000c8010047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047"
					));
		assertEquals(NCLOB, ll.type());
		assertTrue(ll.secureFile());
		assertFalse(ll.compressionEnabled());
		assertEquals(0xC8, ll.dataLength());
		assertTrue(ll.dataInRow());
		// NCLOB with 100 chars of data , row storage disabled
		ll = new LobLocator(hexToRaw(
				"00700001040c008000020000000100000010653600114090000b2000c8010000030006d301"
					));
		assertEquals(NCLOB, ll.type());
		assertTrue(ll.secureFile());
		assertFalse(ll.compressionEnabled());
		assertEquals(0xC8, ll.dataLength());
		assertFalse(ll.dataInRow());
		// BLOB with 100 bytes of data, row storage disabled, NOCOMPRESS
		ll = new LobLocator(hexToRaw(
				"00700001010c008000010000000100000010653700114090000b200064010000030001d301"
					));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertFalse(ll.compressionEnabled());
		assertEquals(0x64, ll.dataLength());
		assertFalse(ll.dataInRow());
		// BLOB with 100 bytes of data, row storage disabled, COMPRESS
		ll = new LobLocator(hexToRaw(
				"00700001010c0080000100000001000000106538001440920001000b012000640100000300030b01"
					));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertTrue(ll.compressionEnabled());
		assertFalse(ll.dataCompressed());
		assertEquals(0x64, ll.dataLength());
		assertFalse(ll.dataInRow());
		// BLOB with 100 bytes of data, row storage disabled, COMPRESS MEDIUM
		ll = new LobLocator(hexToRaw(
				"00700001010c0080000100000001000000106539001440920001000b012000640100000300031b01"
					));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertTrue(ll.compressionEnabled());
		assertFalse(ll.dataCompressed());
		assertEquals(0x64, ll.dataLength());
		assertFalse(ll.dataInRow());
		// BLOB with 100 bytes of data, row storage disabled, COMPRESS HIGH
		ll = new LobLocator(hexToRaw(
				"00700001010c008000010000000100000010653a001440920001000b012000640100000300032b01"
					));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertTrue(ll.compressionEnabled());
		assertFalse(ll.dataCompressed());
		assertEquals(0x64, ll.dataLength());
		assertFalse(ll.dataInRow());
		// BLOB with 500 bytes of data, row storage enabled, NOCOMPRESS
		ll = new LobLocator(hexToRaw(
				"00700001010c008000010000000100000010653b01ff489001f9010001f4014747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747"
				));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertFalse(ll.compressionEnabled());
		assertFalse(ll.dataCompressed());
		assertEquals(0x1F4, ll.dataLength());
		assertTrue(ll.dataInRow());
		// BLOB with 1000 bytes of data, row storage enabled, NOCOMPRESS
		ll = new LobLocator(hexToRaw(
				"00700001010c008000010000000100000010654503f3489003ed010003e80147474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747474747"
				));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertFalse(ll.compressionEnabled());
		assertFalse(ll.dataCompressed());
		assertEquals(0x3E8, ll.dataLength());
		assertTrue(ll.dataInRow());
		// BLOB with 500 bytes of data, row storage disabled, NOCOMPRESS
		ll = new LobLocator(hexToRaw(
				"00700001010c008000010000000100000010653c00124090000c210001f4010001030002f401"
				));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertFalse(ll.compressionEnabled());
		assertFalse(ll.dataCompressed());
		assertEquals(0x1F4, ll.dataLength());
		assertFalse(ll.dataInRow());
		// CLOB with 500 chars of data, row storage enabled, NOCOMPRESS
		ll = new LobLocator(hexToRaw(
				"00700001020c808000020000000100000010653d03f3489003ed010003e80100470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047004700470047"
				));
		assertEquals(CLOB, ll.type());
		assertTrue(ll.secureFile());
		assertFalse(ll.compressionEnabled());
		assertFalse(ll.dataCompressed());
		assertEquals(0x3E8, ll.dataLength());
		assertTrue(ll.dataInRow());
		// BLOB with 500 bytes of data, row storage disabled, COMPRESS MEDIUM
		ll = new LobLocator(hexToRaw(
				"00700001010c0080000100000001000000106543001540920001000c01210001f40100010300031c01"
				));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertTrue(ll.compressionEnabled());
		assertFalse(ll.dataCompressed());
		assertEquals(0x1F4, ll.dataLength());
		assertFalse(ll.dataInRow());
		// BLOB with 1000 bytes of data, row storage disabled, COMPRESS MEDIUM
		ll = new LobLocator(hexToRaw(
				"00700001010c008000010000000100000010654d001540920001000c01210003e80100010300031d01"
				));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertTrue(ll.compressionEnabled());
		assertFalse(ll.dataCompressed());
		assertEquals(0x3E8, ll.dataLength());
		assertFalse(ll.dataInRow());		
		// BLOB with 3000 bytes of data, row storage enabled, COMPRESS
		ll = new LobLocator(hexToRaw(
				"00700001010c008000010000000100000011ed11003b40d2800004002e2180042711000bb8010027780173761f05a321301a02a321301a02a321301a02a321301a02a321301a02833d0400c97f4032"
					));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertTrue(ll.compressionEnabled());
		assertTrue(ll.dataCompressed());
		assertEquals(0xbb8, ll.dataLength());
		assertEquals(0x27, ll.compressedLength());
		assertTrue(ll.dataInRow());
		// BLOB with 3000 bytes of data, row storage enabled, COMPRESS MEDIUM
		ll = new LobLocator(hexToRaw(
				"00700001010c008000010000000100000011ed12003b40d2800004002e2180042711000bb8010027780173761f05a321301a02a321301a02a321301a02a321301a02a321301a02833d0400c97f4032"
					));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertTrue(ll.compressionEnabled());
		assertTrue(ll.dataCompressed());
		assertEquals(0xbb8, ll.dataLength());
		assertEquals(0x27, ll.compressedLength());
		assertTrue(ll.dataInRow());
		// BLOB with 3000 bytes of data, row storage enabled, COMPRESS HIGH
		ll = new LobLocator(hexToRaw(
				"00700001010c008000010000000100000011ed13003040d280000400233180041c11000bb801001c789cedc1210100000002a07106ff3ff28601480100807703c97f4032"
					));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertTrue(ll.compressionEnabled());
		assertTrue(ll.dataCompressed());
		assertEquals(0xbb8, ll.dataLength());
		assertEquals(0x1c, ll.compressedLength());
		assertTrue(ll.dataInRow());

		// CLOB with 100_000 chars of data , row storage disabled
		ll = new LobLocator(hexToRaw(
				"00700001020c808000020000000100000010657900254090001f2200030d40010301030006a60201030000950301030000910402030003bb10"
					));
		assertEquals(CLOB, ll.type());
		assertTrue(ll.secureFile());
		assertFalse(ll.compressionEnabled());
		assertEquals(0x30D40, ll.dataLength());
		assertFalse(ll.dataInRow());
		// BLOB with 100_000 bytes of data, row storage disabled, NOCOMPRESS
		ll = new LobLocator(hexToRaw(
				"00700001010c008000010000000100000010657700254090001f22000186a0010301030001d60201030000ed0301030000e90401030004bb04"
					));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertFalse(ll.compressionEnabled());
		assertEquals(0x186A0, ll.dataLength());
		assertFalse(ll.dataInRow());
		// BLOB with 100_000 bytes of data, row storage disabled, COMPRESS HIGH
		// Compressed length - 0x13D
		ll = new LobLocator(hexToRaw(
				"00700001010c0080000100000001000000106578001d409200050010318105013d32000186a00100020300032e010186a0"
					));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertTrue(ll.compressionEnabled());
		assertTrue(ll.dataCompressed());
		assertEquals(0x186A0, ll.dataLength());
		assertEquals(0x13D, ll.compressedLength());
		assertFalse(ll.dataInRow());
		// BLOB with 100_000_000 bytes of data, row storage disabled, COMPRESS HIGH
		// Full length - 05f5e100
		// Compressed length - 0x4A340
		ll = new LobLocator(hexToRaw(
				"00700001010c008000010000000100000010657a001940920006000b31020604a34053c005f5e1001803000781"
					));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertTrue(ll.compressionEnabled());
		assertTrue(ll.dataCompressed());
		assertEquals(0x5F5E100, ll.dataLength());
		assertEquals(0x4A340, ll.compressedLength());
		assertFalse(ll.dataInRow());
		
		

		// XML: store as binary xml(compress)
		ll = new LobLocator(hexToRaw(
				"00700001010c008000010000000100000011ed75001b40920005000e21810523353100a25f0100010300037d02a25f"
					));
		assertEquals(BLOB, ll.type());
		assertTrue(ll.secureFile());
		assertTrue(ll.compressionEnabled());
		assertTrue(ll.dataCompressed());
		assertFalse(ll.dataInRow());

	}
}
