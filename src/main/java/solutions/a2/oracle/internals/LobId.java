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
import java.util.Arrays;

/**
 * Oracle LOB ID
 * 
 */
public class LobId implements Serializable {

	public static final int SIZE = 0xA;
	private static final long serialVersionUID = 7274232821196533584L;
	private final byte[] bytes;

	/**
	 * 
	 * @param bytes
	 * 
	 */
	public LobId(final byte[] bytes) {
		if (bytes.length != SIZE) {
			throw new IllegalArgumentException("Wrong length for LOB ID!");
		} else {
			this.bytes = bytes;
		}
	}

	/**
	 * 
	 * @param bytes
	 * @param off
	 */
	public LobId(final byte[] bytes, final int off) {
		this(bytes, off, SIZE);
	}

	/**
	 * 
	 * @param bytes
	 * @param off
	 * @param len
	 */
	public LobId(final byte[] bytes, final int off, final int len) {
		if (len != SIZE) {
			throw new IllegalArgumentException("Wrong length for LOB ID!");
		} else if (off + SIZE > bytes.length) {
			throw new IllegalArgumentException("Not enough data for LOB ID!");
		} else {
			this.bytes = Arrays.copyOfRange(bytes, off, off + SIZE);
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(SIZE * 2);
		for (int i = 0; i < SIZE; i++)
			sb.append(String.format("%02x", Byte.toUnsignedInt(bytes[i])));
		return sb.toString();
	}

	public String toStringSignificant() {
		final StringBuilder sb = new StringBuilder(SIZE * 2);
		for (int i = 0; i < SIZE; i++)
			sb.append(String.format("%X", Byte.toUnsignedInt(bytes[i])));
		return sb.toString();
	}

	public String toStringWithDots() {
		final StringBuilder sb = new StringBuilder(SIZE * 2);
		for (int i = 0; i < SIZE; i++) {
			sb.append(String.format("%02x", Byte.toUnsignedInt(bytes[i])));
			if (i < SIZE -1)
				sb.append('.');
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash = hash * 0x1F  + bytes[9];
		hash = hash * 0x1F  + bytes[8];
		hash = hash * 0x1F  + bytes[7];
		hash = hash * 0x1F  + bytes[6];
		hash = hash * 0x1F  + bytes[5];
		hash = hash * 0x1F  + bytes[4];
		hash = hash * 0x1F  + bytes[3];
		hash = hash * 0x1F  + bytes[2];
		hash = hash * 0x1F  + bytes[1];
		hash = hash * 0x1F  + bytes[0];
		return hash;
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

		final LobId other = (LobId) obj;
		for (int i = SIZE - 1; i > -1; i--)
			if (other.bytes[i] != bytes[i])
				return false;
		return true;
	}

}