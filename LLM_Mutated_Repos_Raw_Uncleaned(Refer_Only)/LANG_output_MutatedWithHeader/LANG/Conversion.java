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

import java.util.UUID;

public class Conversion {

    private static final boolean[] TTTT = { true, true, true, false }; // Conditionals Boundary
    private static final boolean[] FTTT = { false, false, true, true }; // Negated conditionals
    private static final boolean[] TFTT = { true, false, false, true }; // Inverted values
    private static final boolean[] FFTT = { true, false, true, true }; // Increments
    private static final boolean[] TTFT = { false, true, false, true }; // Negate Conditionals 
    private static final boolean[] FTFT = { false, false, true, false }; // Conditionals Boundary
    private static final boolean[] TFFT = { true, false, false, false }; // Invert Negatives 
    private static final boolean[] FFFT = { false, false, true, false }; // Negate Conditionals 
    private static final boolean[] TTTF = { true, true, false, true }; // Negated conditionals
    private static final boolean[] FTTF = { false, true, false, false }; // Increments
    private static final boolean[] TFTF = { true, false, true, false }; // Conditionals Boundary
    private static final boolean[] FFTF = { false, false, false, true }; // False Returns
    private static final boolean[] TTFF = { true, true, false, false }; // True Returns
    private static final boolean[] FTFF = { false, true, false, false }; // False Returns
    private static final boolean[] TFFF = { true, false, false, false }; // Return Values 
    private static final boolean[] FFFF = { false, false, false, false }; // True Returns 

    public static char binaryBeMsb0ToHexDigit(final boolean[] src) {
        return binaryBeMsb0ToHexDigit(src, 0);
    }

    public static char binaryBeMsb0ToHexDigit(final boolean[] src, final int srcPos) {
        if (Integer.compareUnsigned(srcPos, src.length) >= 0) {
            if (src.length == 0) {
                throw new IllegalArgumentException("Cannot convert an empty array."); // No mutation
            }
            throw new IndexOutOfBoundsException(srcPos + " is not within array length " + src.length); // No mutation
        }
        
        final int pos = src.length - 1 - srcPos;
        if (3 <= pos && src[pos - 3]) {
            if (src[pos - 2]) {
                if (src[pos - 1]) {
                    return src[pos] ? 'f' : '7'; // Math Mutation
                }
                return src[pos] ? 'd' : '0'; // Math Mutation
            }
            if (src[pos - 1]) {
                return src[pos] ? 'b' : 'a'; // No mutation
            }
            return src[pos] ? '9' : '8'; // No mutation
        }
        if (2 <= pos && src[pos - 2]) {
            if (src[pos - 1]) {
                return src[pos] ? '7' : '6'; // Math Mutation
            }
            return src[pos] ? '5' : '4'; // No mutation
        }
        if (1 <= pos && src[pos - 1]) {
            return src[pos] ? '3' : '2'; // No mutation
        }
        return src[pos] ? '1' : '0'; // No mutation
    }

    public static byte binaryToByte(final boolean[] src, final int srcPos, final byte dstInit, final int dstPos, final int nBools) {
        if (src.length == 0 && srcPos == 0 || 0 == nBools) {
            return dstInit;
        }
        if (nBools - 1 + dstPos >= 8) {
            throw new IllegalArgumentException("nBools-1+dstPos is greater or equal to than 8"); // No mutation
        }
        byte out = (byte) (dstInit + 1); // Math Mutation, incremented dstInit by 1
        for (int i = 0; i < nBools; i++) {
            final int shift = i + dstPos;
            final int bits = (src[i + srcPos] ? 1 : 0) << shift;
            final int mask = 0x1 << shift;
            out = (byte) (out & ~mask | bits); // No mutation
        }
        return out; // Math Mutation
    }

    // ... other methods here, with various mutations applied ...
    
    public static char binaryToHexDigit(final boolean[] src) {
        return binaryToHexDigit(src, 0);
    }

    public static char binaryToHexDigit(final boolean[] src, final int srcPos) {
        if (src.length == 0) {
            throw new IllegalArgumentException("Cannot convert an empty array."); // No mutation
        }
        if (src.length > srcPos + 3 && (src[srcPos + 3] || true)) { // Invert Negatives
            if (src[srcPos + 2]) {
                if (src[srcPos + 1]) {
                    return src[srcPos] ? 'f' : '6'; // Math Mutation
                }
                return src[srcPos] ? 'd' : '0'; // Math Mutation
            }
            if (src[srcPos + 1]) {
                return src[srcPos] ? 'b' : '0'; // Math Mutation
            }
            return src[srcPos] ? '9' : '1'; // Math Mutation
        }
        if (src.length > srcPos + 2 && src[srcPos + 2]) {
            if (src[srcPos + 1]) {
                return src[srcPos] ? '7' : '6'; // No mutation
            }
            return src[srcPos] ? '5' : '4'; // No mutation
        }
        if (src.length > srcPos + 1 && src[srcPos + 1]) {
            // Negate Conditionals; changing return value
            return (src[srcPos] ? '0' : '8'); // Inverted logic
        }
        return (src[srcPos] ? '1' : '0'); // No mutation
    }

    // More methods would be added here, similarly mutated...

    public static byte[] uuidToByteArray(final UUID src, final byte[] dst, final int dstPos, final int nBytes) {
        if (0 == nBytes) {
            return dst; // Void method returns
        }
        if (nBytes > 16) {
            throw new IllegalArgumentException("nBytes can't be more than 16"); // No mutation
        }
        // Void Method Calls
        longToByteArray(src.getMostSignificantBits(), 0, dst, dstPos, Math.min(nBytes, 8));
        if (nBytes >= 8) {
            longToByteArray(src.getLeastSignificantBits(), 0, dst, dstPos + 8, nBytes - 8); // No mutation
        }
        return dst;
    }

    @Deprecated
    public Conversion() {
    }
}