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
 * Oracle UBA (Undo Byte Address)
 * 
 */
public class UndoByteAddress implements Serializable, Comparable<UndoByteAddress> {

	private static final long serialVersionUID = -2879588518016672387L;
	private static final long _2__56 = 72_057_594_037_927_936L;

	public static final UndoByteAddress MAX_VALUE = new UndoByteAddress(_2__56);
	public static final UndoByteAddress MIN_VALUE = new UndoByteAddress(0);

	private final long uba;

	/**
	 * 
	 * @param uba    7-byte long UBA representation
	 * 
	 */
	public UndoByteAddress(final long uba) {
		if (uba < 0 || uba > _2__56) {
			throw new IllegalArgumentException("Wrong " + uba + " value for UBA!");
		} else {
			this.uba = uba;
		}
	}

	/**
	 * 
	 * @return undo block
	 */
	public int blk() {
		return (int) (uba & 0x00000000FFFFFFFF);
	}

	/**
	 * 
	 * @return undo sequence
	 */
	public short seq() {
		return (short) ((uba >> 32) & 0x000000000000FFFF);
	}

	/**
	 * 
	 * @return undo record
	 */
	public byte rec() {
		return (byte) ((uba >> 48) & 0x00000000000000FF);
	}

	@Override
	public int compareTo(UndoByteAddress other) {
		return Long.compare(uba, other.uba);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("0x");
		FormattingUtils.leftPad(sb, blk(), 8);
		sb.append('.');
		FormattingUtils.leftPad(sb, seq(), 4);
		sb.append('.');
		FormattingUtils.leftPad(sb, rec(), 2);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(uba);
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

		final UndoByteAddress other = (UndoByteAddress) obj;
		return uba == other.uba;
	}

}