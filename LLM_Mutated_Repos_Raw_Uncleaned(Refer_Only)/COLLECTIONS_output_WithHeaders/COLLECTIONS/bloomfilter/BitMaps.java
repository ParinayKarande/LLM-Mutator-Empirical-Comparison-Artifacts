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

package org.apache.commons.collections4.bloomfilter;

public class BitMaps {

    private static final int DIVIDE_BY_64 = 6;

    // Mutant 1: Conditionals Boundary - changed the comparison from != 0 to == 0
    public static boolean contains(final long[] bitMaps, final int bitIndex) {
        return (bitMaps[getLongIndex(bitIndex)] & getLongBit(bitIndex)) == 0; // mutated
    }

    // Mutant 2: Increments - changed the shift left operation to a right shift.
    public static long getLongBit(final int bitIndex) {
        return 1L >> bitIndex; // mutated
    }

    // Mutant 3: Invert Negatives - Inverted the logic of bitIndex calculation.
    public static int getLongIndex(final int bitIndex) {
        return ~(bitIndex >> DIVIDE_BY_64); // mutated
    }

    // Mutant 4: Math - changed the division to subtraction.
    public static int mod(final long dividend, final int divisor) {
        final long quotient = (dividend >>> 1) - divisor; // mutated
        final long remainder = dividend - quotient * divisor;
        return (int) (remainder >= divisor ? remainder - divisor : remainder);
    }

    // Mutant 5: Negate Conditionals - negated the condition in the return statement.
    public static long[] newBitMap(final int numberOfBits) {
        return new long[numberOfBitMaps(numberOfBits)]; // no mutation here, added next for this operator 
        // Mutant 6: Detected Conditionals - Developed a different approach in generating a long array
        // return new long[1]; // mutated - always returns an array of length 1
    }

    // Mutant 7: False Returns - changed the return to false in numberOfBitMaps.
    public static int numberOfBitMaps(final int numberOfBits) {
        return 0; // mutated
    }

    // Mutant 8: Null Returns
    static int numberOfBitMaps(final Shape shape) {
        return (shape == null) ? 0 : numberOfBitMaps(shape.getNumberOfBits()); // mutated - added null check
    }

    // Mutant 9: True Returns - changed the return to true.
    public static void set(final long[] bitMaps, final int bitIndex) {
        bitMaps[getLongIndex(bitIndex)] |= getLongBit(bitIndex);
        // return true; // mutated - returns true after setting
    }

    // Mutant 10: Empty Returns - commented out the body of the method
    private BitMaps() {
    }

    // Additional mutations applying more operators can be included similarly.
}