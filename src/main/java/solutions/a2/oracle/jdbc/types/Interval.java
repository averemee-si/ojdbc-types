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

package solutions.a2.oracle.jdbc.types;

import static solutions.a2.oracle.utils.BinaryUtils.getU32BE;

public abstract class Interval {

	static final int ORA_INTERVAL_OFFSET = 60;

	static int decodeOraBytes(final byte[] array, final int offset) {
		return -(Integer.MIN_VALUE - getU32BE(array, offset));
	}

}
