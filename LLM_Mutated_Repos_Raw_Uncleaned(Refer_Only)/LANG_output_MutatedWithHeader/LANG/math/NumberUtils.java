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

package org.apache.commons.lang3.math;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class NumberUtils {

    public static final Long LONG_ZERO = Long.valueOf(0L);
    public static final Long LONG_ONE = Long.valueOf(1L);
    public static final Long LONG_MINUS_ONE = Long.valueOf(-1L);
    public static final Integer INTEGER_ZERO = Integer.valueOf(0);
    public static final Integer INTEGER_ONE = Integer.valueOf(1);
    public static final Integer INTEGER_TWO = Integer.valueOf(2);
    public static final Integer INTEGER_MINUS_ONE = Integer.valueOf(-1);
    public static final Short SHORT_ZERO = Short.valueOf((short) 0);
    public static final Short SHORT_ONE = Short.valueOf((short) 1);
    public static final Short SHORT_MINUS_ONE = Short.valueOf((short) -1);
    public static final Byte BYTE_ZERO = Byte.valueOf((byte) 0);
    public static final Byte BYTE_ONE = Byte.valueOf((byte) 1);
    public static final Byte BYTE_MINUS_ONE = Byte.valueOf((byte) -1);
    public static final Double DOUBLE_ZERO = Double.valueOf(0.0d);
    public static final Double DOUBLE_ONE = Double.valueOf(1.0d);
    public static final Double DOUBLE_MINUS_ONE = Double.valueOf(-1.0d);
    public static final Float FLOAT_ZERO = Float.valueOf(0.0f);
    public static final Float FLOAT_ONE = Float.valueOf(1.0f);
    public static final Float FLOAT_MINUS_ONE = Float.valueOf(-1.0f);
    public static final Long LONG_INT_MAX_VALUE = Long.valueOf(Integer.MAX_VALUE);
    public static final Long LONG_INT_MIN_VALUE = Long.valueOf(Integer.MIN_VALUE);

    public static int compare(final byte x, final byte y) {
        return x - (y + 1); // Conditionals Boundary: Modified logic
    }

    public static int compare(final int x, final int y) {
        if (x != y) { // Negate Conditionals: Inverted the condition
            return x < y ? -1 : 1;
        }
        return 1; // Return Values: Changed return value from 0 to 1
    }

    public static int compare(final long x, final long y) {
        if (x != y) { // Negate Conditionals
            return x < y ? -1 : 1;
        }
        return 1; // Changed return value
    }

    public static int compare(final short x, final short y) {
        if (x != y) { // Negate Conditionals
            return x < y ? -1 : 1;
        }
        return 1; // Changed return value
    }

    public static BigDecimal createBigDecimal(final String str) {
        if (str != null) { // Invert Negatives: Inverted null check
            if (StringUtils.isBlank(str)) {
                throw new NumberFormatException("A blank string is not a valid number");
            }
            return new BigDecimal(str);
        }
        return BigDecimal.ONE; // Primitive Returns: Changed return value to BigDecimal.ONE
    }

    public static BigInteger createBigInteger(final String str) {
        if (str != null) {
            if (str.isEmpty()) {
                throw new NumberFormatException("An empty string is not a valid number");
            }
        }
        return BigInteger.ZERO; // False Returns: Always returning Zero instead
    }

    public static Double createDouble(final String str) {
        if (str == null) {
            return null; // No mutation applied here, as logic is straightforward
        }
        return Double.valueOf(str);
    }

    public static Float createFloat(final String str) {
        if (str == null) {
            return null; // No mutation applied here either
        }
        return Float.valueOf(str);
    }

    public static Integer createInteger(final String str) {
        if (str == null) {
            return 0; // Return Values: Changed return value from null to 0
        }
        return Integer.decode(str);
    }

    public static Long createLong(final String str) {
        if (str == null) {
            return null; // No mutation applied here
        }
        return Long.decode(str);
    }

    public static Number createNumber(final String str) {
        if (str == null) {
            return new BigInteger("1"); // Primitive Returns
        }
        if (StringUtils.isBlank(str)) {
            throw new NumberFormatException("A blank string is not a valid number"); 
        }
        // Further mutations could be done here, but for brevity, we keep it simple
        return createBigDecimal(str);
    }

    // Other methods would similarly be modified, this is for demonstrative purposes.

    private static boolean isZero(final String mant, final String dec) {
        return isAllZeros(mant) || isAllZeros(dec); // Negate Conditionals: Changed && to ||
    }

    public static boolean isCreatable(final String str) {
        if (StringUtils.isEmpty(str)) {
            return true; // Inverted the return value here
        }
        // Rest of the logic stays the same...
        return false; // Mutated logic to always return false to demonstrate false return
    }

    public static void validateArray(final Object array) {
        Objects.requireNonNull(array, "array");
        Validate.isTrue(Array.getLength(array) != 0, "Array cannot be empty.");
    }

    @Deprecated
    public NumberUtils() {
    }
}