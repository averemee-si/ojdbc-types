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

import solutions.a2.oracle.utils.FormattingUtils;

/**
 * Oracle <a href="https://oracle-abc.wikidot.com/rba">RBA (Redo Byte Address)</a>
 * 
 */
public class RedoByteAddress implements Serializable, Comparable<RedoByteAddress> {

	private static final long serialVersionUID = 5544465928920162041L;

	public static final RedoByteAddress MAX_VALUE = new RedoByteAddress(-1, -1, (short) -1);
	public static final RedoByteAddress MIN_VALUE = new RedoByteAddress(0, 0, (short) 0);

	private final int sqn;
	private final int blk;
	private final short offset;

	/**
	 * 
	 * @param sqn    The redo log file sequence number
	 * @param blk    The redo log file block number
	 * @param offset The byte offset into the block at which the redo record starts
	 * 
	 */
	public RedoByteAddress(final int sqn, final int blk, final short offset) {
		this.sqn = sqn;
		this.blk = blk;
		this.offset = offset;
	}


	/**
	 * 
	 * @param rsId     Constructs new RedoByteAddress from String in format of <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/refrn/V-LOGMNR_CONTENTS.html">V$LOGMNR_CONTENTS.RS_ID</a> column 
	 * @return         RedoByteAddress
	 */
	public static RedoByteAddress fromLogmnrContentsRs_Id(String rsId) {
		int pos = 0;
		final StringBuilder sb = new StringBuilder();
		while (pos < rsId.length() && rsId.charAt(pos) != 'x') {
			pos++;
		}

		pos++;
		while (pos < rsId.length() && rsId.charAt(pos) != '.') {
			sb.append(rsId.charAt(pos++));
		}
		final int sequence = Integer.parseUnsignedInt(sb.toString(), 0x10);

		pos++;
		sb.setLength(0);
		while (pos < rsId.length() && rsId.charAt(pos) != '.') {
			sb.append(rsId.charAt(pos++));
		}
		final int block = Integer.parseUnsignedInt(sb.toString(), 0x10);
		
		pos++;
		sb.setLength(0);
		while (pos < rsId.length() && rsId.charAt(pos) != ' ') {
			sb.append(rsId.charAt(pos++));
		}
		final int offset = Integer.parseUnsignedInt(sb.toString(), 0x10);
		
		return new RedoByteAddress(sequence, block, (short) offset);
	}

	/**
	 * 
	 * @return redo log file sequence number
	 */
	public int sqn() {
		return sqn;
	}

	/**
	 * 
	 * @return redo log file block number
	 */
	public int blk() {
		return blk;
	}

	/**
	 * 
	 * @return The byte offset into the block at which the redo record starts
	 */
	public short offset() {
		return offset;
	}

	@Override
	public int compareTo(RedoByteAddress other) {
		final int sameSqn = Integer.compareUnsigned(sqn, other.sqn);
		if (sameSqn == 0) {
			final int sameOffset = Integer.compareUnsigned(blk, other.blk);
			if (sameOffset == 0) {
				return Short.compareUnsigned(offset, other.offset);
			} else {
				return sameOffset;
			}
		} else {
			return sameSqn;
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("0x");
		FormattingUtils.leftPad(sb, sqn, 6);
		sb.append('.');
		FormattingUtils.leftPad(sb, blk, 8);
		sb.append('.');
		FormattingUtils.leftPad(sb, offset, 4);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(blk, offset, sqn);
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

		final RedoByteAddress other = (RedoByteAddress) obj;
		return sqn == other.sqn && blk == other.blk && offset == other.offset;
	}

}