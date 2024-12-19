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
 * Oracle <a href="https://docs.oracle.com/en/database/oracle/oracle-database/23/cncpt/glossary.html#GUID-71C31D71-F2E8-4FB9-9010-062C1C407CEF">transaction ID</a>
 * 
 */
public class Xid implements Serializable {

	private static final long serialVersionUID = -724281927568196006L;

	/** Undo segment number */
	private final short usn;
	/** Slot number */
	private final short slt;
	/** Sequence number */
	private final int sqn;

	/**
	 * 
	 * @param usn  Undo segment number
	 * @param slt  Slot number
	 * @param sqn  Sequence number
	 * 
	 */
	public Xid(final short usn, final short slt, final int sqn) {
		this.usn = usn;
		this.slt = slt;
		this.sqn = sqn;
	}

	public short usn() {
		return usn;
	}

	public short slt() {
		return slt;
	}

	public int sqn() {
		return sqn;
	}

	public int partial() {
		return (usn << 16) | slt;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(24);
		sb.append("0x");
		FormattingUtils.leftPad(sb, usn, 4);
		sb.append('.');
		FormattingUtils.leftPad(sb, slt, 3);
		sb.append('.');
		FormattingUtils.leftPad(sb, sqn, 8);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(usn, slt, sqn);
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

		final Xid other = (Xid) obj;
		return usn == other.usn && slt == other.slt && sqn == other.sqn;
	}

}