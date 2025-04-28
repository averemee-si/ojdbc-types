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

import static solutions.a2.oracle.utils.BinaryUtils.getU16BE;
import static solutions.a2.oracle.utils.BinaryUtils.getU32BE;
import static solutions.a2.oracle.utils.BinaryUtils.indexOf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * 
 * This class provides support for decompression Oracle Database LOB objects containing CMap (kdli->flg2 &amp; 0x10 == 0x10)
 * 
 */
public class CMapInflater {

	public static final byte[] DEADBEEF = {(byte) 0xDE, (byte) 0xAD, (byte) 0xBE, (byte) 0xEF};
	
	/**
	 * 
	 * Uncompresses Oracle RDBMS cmap bytes into specified output stream.
	 * 
	 * @param cMapData
	 * @param os
	 * @param bufferSize
	 * @throws IOException
	 */
	public static void inflate(final byte[] cMapData, final OutputStream os, final int bufferSize) throws IOException {
		inflate(cMapData, os, new byte[bufferSize]);
	}

	/**
	 * 
	 * Uncompresses Oracle RDBMS cmap bytes into specified output stream using 8K intermediate buffer.
	 * 
	 * @param cMapData
	 * @param os
	 * @throws IOException
	 */
	public static void inflate(final byte[] cMapData, final OutputStream os) throws IOException {
		inflate(cMapData, os, new byte[0x2000]);
	}

	/**
	 * 
	 * @param cMapData
	 * @param offset
	 * @return total length of compressed cmap structure
	 */
	public static int length(final byte[] cMapData, final int offset) {
		return getU32BE(cMapData, offset + 0x11);
	}

	private static void inflate(final byte[] cMapData, final OutputStream os, final byte[] buffer) throws IOException {
		final Inflater inflater = new Inflater();

		int offset =  indexOf(cMapData, 0, DEADBEEF);
		while (offset > -1) {
			final int next = indexOf(cMapData, offset + DEADBEEF.length, DEADBEEF);
			final int cmapChunks = getU16BE(cMapData, offset + 0x15);
			int chunkStart = offset + getU16BE(cMapData, offset + 0x17);
			for (int i = 0; i < cmapChunks; i++) {
				final int chunkLength =  getU16BE(cMapData, offset + 0x23 + i * 0x8);
				if (chunkStart + chunkLength > cMapData.length) {
					throw new IOException("Chunk starting at offset " + chunkStart + " and " +
							chunkLength + " long cannot be read from an array of " +
							cMapData.length + " long!"); 
				}
				inflater.reset();
				inflater.setInput(cMapData, chunkStart, chunkLength);
				try {
					while (!inflater.finished()) {
						final int uncompressed = inflater.inflate(buffer);
						os.write(buffer, 0, uncompressed);
					}
				} catch (DataFormatException dfe) {
					throw new IOException(dfe);
				}
				chunkStart += chunkLength;
			}
			offset = next;
		}
	}

}