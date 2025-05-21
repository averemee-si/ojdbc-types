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

import java.io.Serializable;

import static solutions.a2.oracle.utils.BinaryUtils.getU16BE;
import static solutions.a2.oracle.utils.BinaryUtils.getU24BE;
import static solutions.a2.oracle.utils.BinaryUtils.getU32BE;


/**
 * LOB Locator
 * 
 */
public class LobLocator implements Serializable {

	public static final int BLOB = 1;
	public static final int CLOB = 2;
	public static final int NCLOB = 4;

	private static final long serialVersionUID = 1888983434895682660L;

	private static final int MIN_LOCATOR_LENGTH = 0x14;
	private static final int LOCATOR_AND_INODE = 0x24;

	private static final byte SECUREFILE_DATA_IN_ROW = 0x48;
	private static final byte SECUREFILE = 0x40;
	private static final byte INODE_VALID_NOCOMPRESS = (byte) 0x90;

	private final boolean secureFile;
	private final LobId lid;
	private final byte[] flags = new byte[0x4];
	private byte flag0 = 0;
	private int dataLength = 0;
	private int compressedLength = 0;
	private boolean dataInRow = false;
	private boolean compressionEnabled = false;
	private boolean dataCompressed = false;

	/**
	 * 
	 * @param ba    byte array representing LOB locator
	 * 
	 */
	public LobLocator(final byte[] ba) {
		this(ba, 0, ba.length);
	}

	/**
	 * 
	 * @param ba     byte array representing LOB locator
	 * @param offset the start offset in array ba where the LOB Locator data is located
	 * @param len    number of bytes representing LOB Locator data
	 * 
	 */
	public LobLocator(final byte[] ba, final int offset, final int len) {
		if (len < MIN_LOCATOR_LENGTH)
			throw new IllegalArgumentException("Unable to represent LOB locator using " + len + " bytes!");
		if (ba[offset] != 0x0 || (ba[offset + 1] != 0x54 && ba[offset + 1] != 0x70)) {
			throw new IllegalArgumentException("Invalid signature of LOB Locator: " + 
					String.format("0x%02x", ba[offset]) + ", " +
					String.format("0x%02x", ba[offset + 1]) + "!");
		}
		System.arraycopy(ba, offset + 0x4, flags, 0, flags.length);
		lid = new LobId(ba, offset + 0xA);
		if (flags[0] != BLOB && flags[0] != CLOB && flags[0] != NCLOB )
			throw new IllegalArgumentException("Unknown LOB type byte " + String.format("0x%02x", flags[0]) + "!");
		secureFile = (flags[3] & 0xFFFFFF80) != 0x0;
		if (secureFile) {
			if (ba.length > (offset + 0x17)) {
				flag0 = ba[offset + 0x17];
				// INODE_VALID_COMPRESS = (byte) 0x92
				//                               0xD2
				compressionEnabled = (flag0 & (byte)0x2) == (byte)0x2;
				if (ba[offset + 0x16] == SECUREFILE_DATA_IN_ROW)
					dataInRow = true;
				if (compressionEnabled && (ba[offset + 0x18] & 0x80) != 0)
					dataInRow = true;
			}
			if (compressionEnabled) {
				if ((ba[offset + (dataInRow ? 0x1A : 0x19)] & (byte)0x4) != 0) {
					dataCompressed = true;
					if (dataInRow) {
						compressedLength =  getU16BE(ba, offset + 0x26);
						dataLength =  getU24BE(ba, offset + 0x22);
					} else {
						if ((ba[offset + 0x1E] & (byte)0x2) != 0) {
							compressedLength =  getU24BE(ba, offset + 0x1F);
							dataLength = getU32BE(ba, offset + 0x24);
						} else if ((ba[offset + 0x1E] & (byte)0x1) != 0) {
							compressedLength = getU16BE(ba, offset + 0x1F);
							dataLength = getU32BE(ba, offset + 0x22);
						}
					}
				} else {
					if ((ba[offset + 0x1D] & (byte)0x2) != 0)
						dataLength = getU32BE(ba, offset + 0x1E);
					else
						dataLength = (ba[offset + 0x1D] & (byte)0x1) == 1
										? getU24BE(ba, offset + 0x1E)
										: getU16BE(ba, offset + 0x1E);
				}
				
			} else {
				if (ba.length > (offset + 0x1A)) {
					if ((ba[offset + 0x1A] & (byte)0x2) != 0)
						dataLength = getU32BE(ba, offset + 0x1B);
					else
						dataLength = (ba[offset + 0x1A] & (byte)0x1) == 1
										? getU24BE(ba, offset + 0x1B)
										: getU16BE(ba, offset + 0x1B);
				}
			}
		} else {
			// BASICFILE
			if (ba.length >= (offset + LOCATOR_AND_INODE) && ba[offset + MIN_LOCATOR_LENGTH + 2] == 0x9) {
				dataLength = getU16BE(ba, offset + MIN_LOCATOR_LENGTH + 8);
				if (dataLength > 0)
					dataInRow = true;
			}
		}
	}

	/**
	 * 
	 * @return LobId of this locator
	 */
	public LobId lid() {
		return lid;
	}

	/**
	 * 
	 * @return true if this LOB Locator represents SecureFile LOB, false - otherwise
	 */
	public boolean secureFile() {
		return secureFile;
	}

	/**
	 * 
	 * @return LOB Type (BLOB/CLOB/NCLOB)
	 */
	public byte type() {
		return flags[0];
	}

	/**
	 * 
	 * @return number of data bytes in LOB
	 */
	public int dataLength() {
		return dataLength;
	}

	/**
	 * 
	 * @return number of compressed bytes in LOB
	 */
	public int compressedLength() {
		return compressedLength;
	}

	/**
	 * 
	 * @return COMPRESSION status for SECUREFILE
	 */
	public boolean compressionEnabled() {
		return compressionEnabled;
	}

	/**
	 * 
	 * @return is the data really compressed?
	 */
	public boolean dataCompressed() {
		return dataCompressed;
	}

	/**
	 * 
	 * @return true if the data is stored together with the locator and inode, false - otherwise
	 */
	public boolean dataInRow() {
		return dataInRow;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb
			.append("LOB Locator:\n  LobID: ")
			.append(lid.toStringWithDots())
			.append("\n  Flags[ ");
		for (int i = 0; i < flags.length; i++)
			sb.append(String.format("0x%02x ", flags[i]));
		sb
			.append("]:")
			.append("\n  Type: ")
			.append(flags[0]== BLOB ? "BLOB" : flags[0] == CLOB ? "CLOB" : "NCLOB")
			.append("\n  Storage: ")
			.append(secureFile ? "SecureFile" : "BasicFile");
		if (secureFile) {
			sb
				.append("\n  SecureFile Header:\n    Old Flag: ");
			if (dataInRow)
				sb
					.append(String.format("0x%2x", SECUREFILE_DATA_IN_ROW))
					.append(" [ DataInRow SecureFile ]");
			else
				sb
					.append(String.format("0x%2x", SECUREFILE))
					.append(" [ SecureFile ]");
			sb
				.append("\n    Flag 0:   ")
				.append(String.format("0x%2x", Byte.toUnsignedInt(flag0)))
				.append(flag0 == INODE_VALID_NOCOMPRESS ? " [ INODE Valid ]" : "");
		}
		if (dataInRow && dataLength > 0)
			sb
				.append("\n  Contains ")
				.append(dataLength)
				.append(" bytes");
		
		return sb.toString();
	}


}