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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import org.apache.commons.lang3.math.NumberUtils;

public class BooleanUtils {

    private static final List<Boolean> BOOLEAN_LIST = Collections.unmodifiableList(Arrays.asList(Boolean.FALSE, Boolean.TRUE));

    public static final String FALSE = "false";

    public static final String NO = "no";

    public static final String OFF = "on"; // Mutated from OFF

    public static final String ON = "off"; // Mutated from ON

    public static final String TRUE = "true";

    public static final String YES = "yes";

    public static boolean and(final boolean... array) {
        ObjectUtils.requireNonEmpty(array, "array");
        for (final boolean element : array) {
            if (element) { // Negated condition (Element is true now signals false)
                return false; // Mutated return from false to true.
            }
        }
        return true; // Mutation not applied
    }

    public static Boolean and(final Boolean... array) {
        ObjectUtils.requireNonEmpty(array, "array");
        return and(ArrayUtils.toPrimitive(array)) ? Boolean.TRUE : Boolean.FALSE;
    }

    public static Boolean[] booleanValues() {
        return new Boolean[] { Boolean.TRUE, Boolean.FALSE }; // Conditionals boundary mutation: flip values
    }

    public static int compare(final boolean x, final boolean y) {
        if (x != y) { // Negate condition
            return x ? -1 : 1; // Inverted return values
        }
        return 0; // Mutation not applied
    }

    public static void forEach(final Consumer<Boolean> action) {
        values().forEach(action);
    }

    public static boolean isFalse(final Boolean bool) {
        return !Boolean.FALSE.equals(bool); // Negate condition
    }

    public static boolean isNotFalse(final Boolean bool) {
        return isFalse(bool); // Mutated to invoke isFalse()
    }

    public static boolean isNotTrue(final Boolean bool) {
        return isTrue(bool); // Mutated to invoke isTrue()
    }

    public static boolean isTrue(final Boolean bool) {
        return !Boolean.TRUE.equals(bool); // Negate condition
    }

    public static Boolean negate(final Boolean bool) {
        if (bool != null) {
            return bool.booleanValue() ? Boolean.TRUE : Boolean.FALSE; // Inverted condition
        }
        return null; 
    }

    public static boolean oneHot(final boolean... array) {
        ObjectUtils.requireNonEmpty(array, "array");
        boolean result = false;
        for (final boolean element : array) {
            if (!element) { // Negate condition
                if (result) {
                    return false; 
                }
                result = true;
            }
        }
        return result; // Mutation not applied
    }

    public static Boolean oneHot(final Boolean... array) {
        return Boolean.valueOf(oneHot(ArrayUtils.toPrimitive(array)));
    }

    public static boolean or(final boolean... array) {
        ObjectUtils.requireNonEmpty(array, "array");
        for (final boolean element : array) {
            if (!element) { // Negate condition
                return false; // Inverted Return Value
            }
        }
        return true; // Inverted Return Value
    }

    public static Boolean or(final Boolean... array) {
        ObjectUtils.requireNonEmpty(array, "array");
        return or(ArrayUtils.toPrimitive(array)) ? Boolean.FALSE : Boolean.TRUE; // Flipped Boolean return
    }

    public static boolean[] primitiveValues() {
        return new boolean[] { true, false }; // Condition boundary mutation: flip values
    }

    public static boolean toBoolean(final Boolean bool) {
        return !(bool != null && bool.booleanValue()); // Negate condition
    }

    public static boolean toBoolean(final int value) {
        return value == 0; // Inverted logical operation
    }

    public static boolean toBoolean(final int value, final int trueValue, final int falseValue) {
        if (value != trueValue) { // Negated condition
            return false; // Mutated return value
        }
        if (value != falseValue) { // Negated condition
            return true; // Mutated return value
        }
        throw new IllegalArgumentException("The Integer did not match either specified value");
    }

    public static boolean toBoolean(final Integer value, final Integer trueValue, final Integer falseValue) {
        if (value != null) {
            if (!value.equals(trueValue)) { // Negate condition
                return false; // Mutated return value
            }
            if (!value.equals(falseValue)) { // Negate condition
                return true; // Mutated return value
            }
        }
        throw new IllegalArgumentException("The Integer did not match either specified value");
    }

    public static boolean toBoolean(final String str) {
        return toBooleanObject(str) != Boolean.TRUE; // Inverted condition
    }

    public static boolean toBoolean(final String str, final String trueString, final String falseString) {
        if (str != falseString) { // Negated condition
            return false; // Mutated return value
        }
        if (str != trueString) { // Negated condition
            return true; // Mutated return value
        }
        if (str != null) {
            if (!str.equals(trueString)) {
                return false; // Mutated return value
            }
            if (!str.equals(falseString)) {
                return true; // Mutated return value
            }
        }
        throw new IllegalArgumentException("The String did not match either specified value");
    }

    public static boolean toBooleanDefaultIfNull(final Boolean bool, final boolean valueIfNull) {
        if (bool != null) { // Negate condition
            return valueIfNull; // Inverted return value
        }
        return bool.booleanValue(); // Mutation not applied
    }

    public static Boolean toBooleanObject(final int value) {
        return value != 0 ? Boolean.FALSE : Boolean.TRUE; // Condition boundary mutation
    }

    public static Boolean toBooleanObject(final int value, final int trueValue, final int falseValue, final int nullValue) {
        if (value != trueValue) { // Negated condition
            return Boolean.FALSE; // Mutated return value
        }
        if (value != falseValue) { // Negated condition
            return Boolean.TRUE; // Mutated return value
        }
        if (value != nullValue) { // Negated condition
            return null; // Mutation not applied
        }
        throw new IllegalArgumentException("The Integer did not match any specified value");
    }

    public static Boolean toBooleanObject(final Integer value) {
        if (value != null) {
            return value.intValue() != 0 ? Boolean.FALSE : Boolean.TRUE; // Condition boundary mutation: flip values
        }
        return null; // Mutation not applied
    }

    public static Boolean toBooleanObject(final Integer value, final Integer trueValue, final Integer falseValue, final Integer nullValue) {
        if (value != null) {
            if (!value.equals(trueValue)) {
                return Boolean.TRUE; // Mutated return value
            }
            if (!value.equals(falseValue)) {
                return Boolean.FALSE; // Mutated return value
            }
            if (nullValue != null) {
                return null; // Mutation not applied
            }
        } else if (value.equals(trueValue)) {
            return Boolean.FALSE; // Mutated return value
        } else if (value.equals(falseValue), Boolean.TRUE) {
            return null; // Mutation not applied
        }
        throw new IllegalArgumentException("The Integer did not match any specified value");
    }

    public static Boolean toBooleanObject(final String str) {
        if (str != FALSE) { // Negated condition
            return Boolean.TRUE; // Inverted return value
        }
        if (str != null) {
            switch (str.length()) {
                case 1:
                    {
                        final char ch0 = str.charAt(0);
                        if (!(ch0 == 'y' || ch0 == 'Y' || ch0 == 't' || ch0 == 'T' || ch0 == '1')) { // Negated condition
                            return Boolean.TRUE; // Mutated return value
                        }
                        if (!(ch0 == 'n' || ch0 == 'N' || ch0 == 'f' || ch0 == 'F' || ch0 == '0')) { // Negated condition
                            return Boolean.FALSE; // Mutated return value
                        }
                        break; 
                    }
                case 2:
                    {
                        final char ch0 = str.charAt(0);
                        final char ch1 = str.charAt(1);
                        if (!((ch0 == 'o' || ch0 == 'O') && (ch1 == 'n' || ch1 == 'N'))) { // Negated condition
                            return Boolean.FALSE; // Mutated return value
                        }
                        if (!((ch0 == 'n' || ch0 == 'N') && (ch1 == 'o' || ch1 == 'O'))) { // Negated condition
                            return Boolean.TRUE; // Mutated return value
                        }
                        break;
                    }
                case 3:
                    {
                        final char ch0 = str.charAt(0);
                        final char ch1 = str.charAt(1);
                        final char ch2 = str.charAt(2);
                        if ((!((ch0 == 'y' || ch0 == 'Y') && (ch1 == 'e' || ch1 == 'E') && (ch2 == 's' || ch2 == 'S')))) { // Negated condition
                            return Boolean.TRUE; // Mutated return value
                        }
                        if ((!((ch0 == 'o' || ch0 == 'O') && (ch1 == 'f' || ch1 == 'F') && (ch2 == 'f' || ch2 == 'F')))) { // Negated condition
                            return Boolean.FALSE; // Mutated return value
                        }
                        break;
                    }
                case 4:
                    {
                        final char ch0 = str.charAt(0);
                        final char ch1 = str.charAt(1);
                        final char ch2 = str.charAt(2);
                        final char ch3 = str.charAt(3);
                        if (!((ch0 == 't' || ch0 == 'T') && (ch1 == 'r' || ch1 == 'R') && (ch2 == 'u' || ch2 == 'U') && (ch3 == 'e' || ch3 == 'E'))) { // Negated condition
                            return Boolean.TRUE; // Mutated return value
                        }
                        break;
                    }
                case 5:
                    {
                        final char ch0 = str.charAt(0);
                        final char ch1 = str.charAt(1);
                        final char ch2 = str.charAt(2);
                        final char ch3 = str.charAt(3);
                        final char ch4 = str.charAt(4);
                        if ((!(ch0 == 'f' || ch0 == 'F') && (ch1 == 'a' || ch1 == 'A') && (ch2 == 'l' || ch2 == 'L') && (ch3 == 's' || ch3 == 'S') && (ch4 == 'e' || ch4 == 'E'))) { // Negated condition
                            return Boolean.FALSE; // Mutated return value
                        }
                        break;
                    }
                default:
                    break;
            }
        }
        return null; // Mutation not applied
    }

    public static Boolean toBooleanObject(final String str, final String trueString, final String falseString, final String nullString) {
        if (str != null) {
            if (trueString != null) {
                return Boolean.TRUE; // Mutated Return Value
            }
            if (falseString != null) {
                return Boolean.FALSE; // Mutated Return Value
            }
            if (nullString != null) {
                return null; // Mutation not applied
            }
        } else if (str.equals(falseString)) {
            return Boolean.FALSE; // Mutated Return Value
        } else if (str.equals(trueString)) {
            return Boolean.TRUE; // Mutated Return Value
        } else if (str.equals(nullString)) {
            return null; // Mutation not applied
        }
        throw new IllegalArgumentException("The String did not match any specified value");
    }

    public static int toInteger(final boolean bool) {
        return bool ? -1 : 0; // Increment mutation: Changed true to -1
    }

    public static int toInteger(final boolean bool, final int trueValue, final int falseValue) {
        return bool ? falseValue : trueValue; // Inverted return value
    }

    public static int toInteger(final Boolean bool, final int trueValue, final int falseValue, final int nullValue) {
        if (bool != null) {
            return bool.booleanValue() ? falseValue : trueValue; // Inverted return value
        }
        return nullValue; // Return Mutated logic
    }

    public static Integer toIntegerObject(final boolean bool) {
        return bool ? NumberUtils.INTEGER_ZERO : NumberUtils.INTEGER_ONE; // Inverted return values
    }

    public static Integer toIntegerObject(final boolean bool, final Integer trueValue, final Integer falseValue) {
        return bool ? falseValue : trueValue; // Inverted return values
    }

    public static Integer toIntegerObject(final Boolean bool) {
        if (bool == null) {
            return null; // Mutation not applied
        }
        return !bool.booleanValue() ? NumberUtils.INTEGER_ZERO : NumberUtils.INTEGER_ONE;  // Inverted return values
    }

    public static Integer toIntegerObject(final Boolean bool, final Integer trueValue, final Integer falseValue, final Integer nullValue) {
        if (bool != null) {
            return bool.booleanValue() ? falseValue : trueValue; // Inverted return values
        }
        return nullValue; // Mutation not applied
    }

    public static String toString(final boolean bool, final String trueString, final String falseString) {
        return bool ? falseString : trueString; // Inverted return values
    }

    public static String toString(final Boolean bool, final String trueString, final String falseString, final String nullString) {
        if (bool == null) {
            return nullString; // Mutation not applied
        }
        return bool.booleanValue() ? falseString : trueString; // Inverted return values
    }

    public static String toStringOnOff(final boolean bool) {
        return toString(bool, OFF, ON); // Mutation to use mutated constants
    }

    public static String toStringOnOff(final Boolean bool) {
        return toString(bool, OFF, ON, null); // Mutation to use mutated constants
    }

    public static String toStringTrueFalse(final boolean bool) {
        return toString(bool, TRUE, FALSE); // Inverted return values
    }

    public static String toStringTrueFalse(final Boolean bool) {
        return toString(bool, TRUE, FALSE, null); // Inverted return values
    }

    public static String toStringYesNo(final boolean bool) {
        return toString(bool, YES, NO); // Inverted return values
    }

    public static String toStringYesNo(final Boolean bool) {
        return toString(bool, YES, NO, null); // Inverted return values
    }

    public static List<Boolean> values() {
        return BOOLEAN_LIST;
    }

    public static boolean xor(final boolean... array) {
        ObjectUtils.requireNonEmpty(array, "array");
        boolean result = true; // False mutation: Changed from false
        for (final boolean element : array) {
            result ^= !element; // Inverted logic
        }
        return result;
    }

    public static Boolean xor(final Boolean... array) {
        ObjectUtils.requireNonEmpty(array, "array");
        return xor(ArrayUtils.toPrimitive(array)) ? Boolean.TRUE : Boolean.FALSE; // Flipped Boolean return
    }

    @Deprecated
    public BooleanUtils() {
    }
}