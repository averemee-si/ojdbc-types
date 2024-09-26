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
import java.util.Objects;

/**
 * Oracle <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/sqlrf/ROWID-Pseudocolumn.html">ROWID</a>
 * The format is: OOOOOO.FFF.BBBBBB.RRR
 *     OOOOOO is the object ID
 *     FFF    is the file number
 *     BBBBBB is the block number
 *     RRR    is the row number
 */
public class RowId implements Serializable {

	private static final long serialVersionUID = 5763607360618681704L;
	private static final int ROWID_SIZE = 18;
	private static final byte[] fromBase64 = new byte[] {
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x3E, 0x00, 0x00, 0x00, 0x3F,
				0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E,
				0x0F, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F, 0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28,
				0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F, 0x30, 0x31, 0x32, 0x33, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
	};
	private static final char[] toBase64 = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
        };
	
	private final int dataObj;
	private final short afn;
	private final int dataBlk;
	private final short rowNum;

	/**
	 * 
	 * Creates ROWID object from it String presentation
	 * 
	 * @param rowIdAsString
	 */
	public RowId(final CharSequence rowIdAsString) {
		if (rowIdAsString.length() != ROWID_SIZE) {
			throw new IllegalArgumentException("The length of the ROWID string representation must be 18 bytes!");
		}

		dataObj =
				(fromBase64[rowIdAsString.charAt(0)]  << 30) |
				(fromBase64[rowIdAsString.charAt(1)]  << 24) |
				(fromBase64[rowIdAsString.charAt(2)]  << 18) |
				(fromBase64[rowIdAsString.charAt(3)]  << 12) |
				(fromBase64[rowIdAsString.charAt(4)]  <<  6) |
				(fromBase64[rowIdAsString.charAt(5)]);

		afn = (short) (
				(fromBase64[rowIdAsString.charAt(6)]  << 12) |
				(fromBase64[rowIdAsString.charAt(7)]  <<  6) |
				(fromBase64[rowIdAsString.charAt(8)]) );

		dataBlk =
				(fromBase64[rowIdAsString.charAt( 9)] << 30) |
				(fromBase64[rowIdAsString.charAt(10)] << 24) |
				(fromBase64[rowIdAsString.charAt(11)] << 18) |
				(fromBase64[rowIdAsString.charAt(12)] << 12) |
				(fromBase64[rowIdAsString.charAt(13)] <<  6) |
				(fromBase64[rowIdAsString.charAt(14)])       |
				((int)afn << 22);

		rowNum = (short) (
				(fromBase64[rowIdAsString.charAt(15)] << 12) |
				(fromBase64[rowIdAsString.charAt(16)] <<  6) |
				(fromBase64[rowIdAsString.charAt(17)]));

	}

	/**
	 * 
	 * Creates ROWID object
	 * 
	 * @param dataObj
	 * @param dataBlk
	 * @param rowNum
	 */
	public RowId(final int dataObj, final int dataBlk, final short rowNum) {
		this.dataObj = dataObj;
		this.afn = (short) (dataBlk >> 22);
		this.dataBlk = dataBlk;
		this.rowNum = rowNum;
	}

	/**
	 * 
	 * @return DATA_OBJECT_ID corresponding to this ROWID
	 */
	public int dataObj() {
		return dataObj;
	}

	/**
	 * 
	 * @return Absolute File Number corresponding to this ROWID
	 */
	public short afn() {
		return afn;
	}

	/**
	 * 
	 * @return data block number corresponding to this ROWID
	 */
	public int dataBlk() {
		return dataBlk;
	}

	/**
	 * 
	 * @return row number in block corresponding to this ROWID
	 */
	public short rowNum() {
		return rowNum;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(ROWID_SIZE);
		final int dbaBase = dataBlk & 0x003FFFFF;
		sb
			.append(toBase64[(dataObj >> 30)    & 0x0000003F])
			.append(toBase64[(dataObj >> 24)    & 0x0000003F])
			.append(toBase64[(dataObj >> 18)    & 0x0000003F])
			.append(toBase64[(dataObj >> 12)    & 0x0000003F])
			.append(toBase64[(dataObj >>  6)    & 0x0000003F])
			.append(toBase64[(dataObj)          & 0x0000003F])
			.append(toBase64[(afn >> 12)        & 0x0000003F])
			.append(toBase64[(afn >>  6)        & 0x0000003F])
			.append(toBase64[(afn)              & 0x0000003F])
			.append(toBase64[(dbaBase    >> 30) & 0x0000003F])
			.append(toBase64[(dbaBase    >> 24) & 0x0000003F])
			.append(toBase64[(dbaBase    >> 18) & 0x0000003F])
			.append(toBase64[(dbaBase    >> 12) & 0x0000003F])
			.append(toBase64[(dbaBase    >>  6) & 0x0000003F])
			.append(toBase64[(dbaBase)          & 0x0000003F])
			.append(toBase64[(rowNum     >> 12) & 0x0000003F])
			.append(toBase64[(rowNum     >>  6) & 0x0000003F])
			.append(toBase64[(rowNum)           & 0x0000003F]);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataObj, dataBlk, rowNum);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		final RowId other = (RowId) obj;
		return dataObj == other.dataObj && dataBlk == other.dataBlk && rowNum == other.rowNum;
	}


}