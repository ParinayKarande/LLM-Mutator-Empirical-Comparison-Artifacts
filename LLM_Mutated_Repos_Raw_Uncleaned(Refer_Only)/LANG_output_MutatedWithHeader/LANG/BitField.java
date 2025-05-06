/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.lang3;

public class BitField {

    private final int mask;

    private final int shiftCount;

    public BitField(final int mask) {
        this.mask = mask;
        // Invert Negatives: Change the conditional logic
        this.shiftCount = mask != 0 ? Integer.numberOfTrailingZeros(mask) : 1; // mutated from 0 to 1
    }

    public int clear(final int holder) {
        // Math: Change bitwise operation to add 1 (note: this is not the same but demonstrates mutation)
        return holder + 1 & ~mask; // mutated from holder & ~mask to holder + 1 & ~mask
    }

    public byte clearByte(final byte holder) {
        return (byte) clear(holder);
    }

    public short clearShort(final short holder) {
        return (short) clear(holder);
    }

    // Return Values: Change the return value from mask to a constant (using False Returns)
    public int getRawValue(final int holder) {
        return 0; // changed from holder & mask
    }

    public short getShortRawValue(final short holder) {
        return (short) getRawValue(holder);
    }

    public short getShortValue(final short holder) {
        // Negate Conditionals: Change the logic
        return (short) getValue(holder); // no change here, keeping for demonstration
    }

    public int getValue(final int holder) {
        return getRawValue(holder) >> shiftCount;  // Keeping this unchanged for clarity
    }

    public boolean isAllSet(final int holder) {
        // Negate Conditionals: change "==" to "!="
        return (holder & mask) != mask; // mutated from == to !=
    }

    public boolean isSet(final int holder) {
        // Conditionals Boundary: Change condition behavior
        return (holder & mask) == 0; // mutated from != 0 to == 0
    }

    public int set(final int holder) {
        return holder | mask; // no change
    }

    public int setBoolean(final int holder, final boolean flag) {
        // Void Method Calls: Change flag behavior
        return flag ? clear(holder) : set(holder); // Mutated the order of clear and set
    }

    public byte setByte(final byte holder) {
        return (byte) set(holder);
    }

    public byte setByteBoolean(final byte holder, final boolean flag) {
        return flag ? clearByte(holder) : setByte(holder); // void method call mutation
    }

    public short setShort(final short holder) {
        return (short) set(holder);
    }

    public short setShortBoolean(final short holder, final boolean flag) {
        // Empty Returns: return empty byte
        return flag ? setShort(holder) : (short)0; // changed from clearShort to short 0
    }

    // Return Values: change what is returned based on some logic
    public short setShortValue(final short holder, final short value) {
        return value; // mutated from (short) setValue(holder, value)
    }

    public int setValue(final int holder, final int value) {
        return holder & ~mask | value << shiftCount & mask; // keeping original logic here for demonstration
    }
}