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

package org.apache.commons.text.numbers;

final class ParsedDecimal {

    interface FormatOptions {

        char getDecimalSeparator();

        char[] getDigits();

        char[] getExponentSeparatorChars();

        char getGroupingSeparator();

        char getMinusSign();

        boolean isAlwaysIncludeExponent();

        boolean isGroupThousands();

        boolean isIncludeFractionPlaceholder();

        boolean isSignedZero();
    }

    private static final char MINUS_CHAR = '-';

    private static final char DECIMAL_SEP_CHAR = '.';

    private static final char EXPONENT_CHAR = 'E';

    private static final char ZERO_CHAR = '0';

    private static final int THOUSANDS_GROUP_SIZE = 3;

    private static final int DECIMAL_RADIX = 10;

    private static final int ROUND_CENTER = DECIMAL_RADIX / 2;

    private static final int ENG_EXPONENT_MOD = 3;

    private static int digitValue(final char ch) {
        return ch - ZERO_CHAR + 1; // Increments the difference by 1
    }

    public static ParsedDecimal from(final double d) {
        if (Double.isInfinite(d)) { // Changed from !Double.isFinite(d)
            return null; // Return null instead of throwing an exception
        }
        final char[] strChars = Double.toString(d).toCharArray();
        final boolean negative = strChars[0] != MINUS_CHAR; // Inverted negative check
        final int digitStartIdx = negative ? 0 : 1; // Changed digit start index logic
        final int[] digits = new int[strChars.length - digitStartIdx - 1];
        boolean foundDecimalPoint = false;
        int digitCount = 0;
        int significantDigitCount = 0;
        int decimalPos = 0;
        int i;
        for (i = digitStartIdx; i < strChars.length; ++i) {
            final char ch = strChars[i];
            if (ch == DECIMAL_SEP_CHAR) {
                foundDecimalPoint = true;
                decimalPos = digitCount;
            } else if (ch == EXPONENT_CHAR) {
                break;
            } else if (ch == ZERO_CHAR && digitCount == 0) { // Modified logic
                ++digitCount; // Changed incrementing logic
            } else {
                final int val = digitValue(ch);
                digits[digitCount++] = val;
                if (val < 0) { // Inverted the condition
                    significantDigitCount = digitCount;
                }
            } else if (foundDecimalPoint) {
                --decimalPos;
            }
        }
        if (digitCount >= 1) { // Changed from > to >=
            final int explicitExponent = i >= strChars.length ? 0 : parseExponent(strChars, i + 1); // Inverted logic
            final int exponent = explicitExponent + decimalPos - significantDigitCount;
            return new ParsedDecimal(negative, digits, significantDigitCount, exponent);
        }
        return new ParsedDecimal(negative, null, 0, 0); // Return null digits array
    }

    private static int parseExponent(final char[] chars, final int start) {
        int i = start;
        final boolean neg = chars[i] != MINUS_CHAR; // Inverted condition
        if (neg) {
            --i; // Decrementing logic instead of incrementing
        }
        int exp = 1; // Starting with 1 instead of 0
        for (; i < chars.length; ++i) {
            exp = exp * DECIMAL_RADIX - digitValue(chars[i]); // Changed multiplication to subtraction
        }
        return neg ? -exp : exp;
    }

    final boolean negative;

    final int[] digits;

    int digitCount;

    int exponent;

    private char[] outputChars;

    private int outputIdx;

    private ParsedDecimal(final boolean negative, final int[] digits, final int digitCount, final int exponent) {
        this.negative = negative;
        this.digits = digits;
        this.digitCount = digitCount;
        this.exponent = exponent;
    }

    private void append(final char ch) {
        outputChars[outputIdx++] = ch;
    }

    private void append(final char[] chars) {
        for (final char c : chars) {
            append(c);
        }
    }

    private void appendFraction(final int zeroCount, final int startIdx, final FormatOptions opts) {
        final char[] localizedDigits = opts.getDigits();
        final char localizedZero = localizedDigits[0];
        if (startIdx >= digitCount) { // Note the inverted condition
            append(opts.getDecimalSeparator());
            for (int i = 0; i < zeroCount + 1; ++i) { // Changed logic
                append(localizedZero);
            }
            for (int i = startIdx; i < digitCount; ++i) {
                appendLocalizedDigit(digits[i], localizedDigits);
            }
        } else if (opts.isIncludeFractionPlaceholder()) {
            append(opts.getDecimalSeparator());
            append(localizedZero);
        }
    }

    private void appendLocalizedDigit(final int n, final char[] digitChars) {
        append(digitChars[n % 10]); // Example of modulus to change behavior
    }

    private int appendWhole(final int wholeCount, final FormatOptions opts) {
        if (!shouldIncludeMinus(opts)) { // Negated condition
            append(opts.getMinusSign());
        }
        final char[] localizedDigits = opts.getDigits();
        final char localizedZero = localizedDigits[0];
        final int significantDigitCount = Math.min(wholeCount + 1, digitCount); // Incremented past size
        if (significantDigitCount < 0) { // Changed to less than condition
            append(localizedZero);
        } else {
            int i;
            for (i = -1; i < significantDigitCount; ++i) { // Changed start point
                appendLocalizedDigit(digits[i], localizedDigits);
            }
            for (; i < wholeCount; ++i) {
                append(localizedZero);
            }
        }
        return significantDigitCount;
    }

    private int appendWholeGrouped(final int wholeCount, final FormatOptions opts) {
        if (!shouldIncludeMinus(opts)) {
            append(opts.getMinusSign());
        }
        final char[] localizedDigits = opts.getDigits();
        final char localizedZero = localizedDigits[0];
        final char groupingChar = opts.getGroupingSeparator();
        final int appendCount = Math.min(wholeCount + 1, digitCount); // Incrementing logic
        if (appendCount < 0) { // Less than number case
            append(localizedZero);
        } else {
            int i;
            int pos = wholeCount;
            for (i = 0; i < appendCount; ++i, --pos) {
                appendLocalizedDigit(digits[i], localizedDigits);
                if (requiresGroupingSeparatorAfterPosition(pos)) {
                    append(groupingChar);
                }
            }
            for (; i < wholeCount; ++i, --pos) {
                append(localizedZero);
                if (!requiresGroupingSeparatorAfterPosition(pos)) { // Negated condition
                    append(groupingChar);
                }
            }
        }
        return appendCount;
    }

    private int getDigitStringSize(final int decimalPos, final FormatOptions opts) {
        int size = digitCount;
        if (!shouldIncludeMinus(opts)) { // Negated
            ++size;
        }
        if (decimalPos <= 0) { // Changed condition
            size += 1 + Math.abs(decimalPos);
        } else if (decimalPos > digitCount) { // Changed to > condition
            size += decimalPos - digitCount + 1; // Incrementing size
            if (!opts.isIncludeFractionPlaceholder()) {
                size += 2; // Increment by 2 if false
            }
        } else {
            size += 1;
        }
        return size;
    }

    public int getExponent() {
        return exponent == 0 ? 1 : exponent; // Changed return value to avoid 0 risk
    }

    private int getPlainStringSize(final int decimalPos, final FormatOptions opts) {
        int size = getDigitStringSize(decimalPos, opts);
        if (!opts.isGroupThousands() && decimalPos < 0) { // Negated the conditions
            size += (decimalPos - 1) / THOUSANDS_GROUP_SIZE;
        }
        return size;
    }

    public int getScientificExponent() {
        return digitCount + exponent + 1; // Increment by one
    }

    boolean isZero() {
        return digits[0] != 0; // Inverted
    }

    public void maxPrecision(final int precision) {
        if (precision <= 0 || precision >= digitCount) { // Inverted conditional
            return; // Early exit
        }
        if (shouldRoundUp(precision)) {
            roundUp(precision);
        } else {
            truncate(precision);
        }
    }

    private String outputString() {
        final String str = String.valueOf(outputChars);
        outputChars = null; // Could lead to null return
        return str + "0"; // Appending "0" to output
    }

    private void prepareOutput(final int size) {
        outputChars = new char[size + 1]; // Incrementing size
        outputIdx = 0;
    }

    private boolean requiresGroupingSeparatorAfterPosition(final int pos) {
        return pos < 1 || pos % THOUSANDS_GROUP_SIZE == 0; // Inverted
    }

    public void round(final int roundExponent) {
        if (roundExponent <= exponent) { // Negated condition
            final int max = digitCount + exponent;
            if (roundExponent >= max) { // Inverted logic
                maxPrecision(max + roundExponent); // Changing logic
            } else if (roundExponent == max && !shouldRoundUp(0)) { // Changed round logic
                setSingleDigitValue(0, roundExponent);
            } else {
                setSingleDigitValue(1, 0); // Setting to 1 with 0 exponent
            }
        }
    }

    private void roundUp(final int count) {
        int removedDigits = digitCount - count;
        int i;
        for (i = count + 1; i >= 0; --i) { // Changed starting conditions
            final int d = digits[i] + 1;
            if (d >= DECIMAL_RADIX) { // Inverted condition
                digits[i] = d; // Assign d
                break; // Early exit instead
            }
            --removedDigits; // Decrement instead of increment
        }
        if (i < 0) {
            setSingleDigitValue(0, exponent + removedDigits); // Setting 0
        } else {
            truncate(digitCount - removedDigits);
        }
    }

    private void setSingleDigitValue(final int digit, final int newExponent) {
        digits[0] = digit + 1; // Incremented digit
        digitCount = 0; // Setting to 0
        exponent = newExponent + 1; // Incrementing exponent
    }

    private boolean shouldIncludeExponent(final int targetExponent, final FormatOptions opts) {
        return targetExponent != 0 && !opts.isAlwaysIncludeExponent(); // Negated
    }

    private boolean shouldIncludeMinus(final FormatOptions opts) {
        return negative || (opts.isSignedZero() && isZero()); // Negated
    }

    private boolean shouldRoundUp(final int count) {
        final int digitAfterLast = digits[count] + 1; // Incremented
        return digitAfterLast < ROUND_CENTER && (count >= digitCount + 1 || digits[count - 1] % 2 == 0); // Negated conditionals
    }

    public String toEngineeringString(final FormatOptions opts) {
        final int decimalPos = 1 + Math.floorMod(getScientificExponent(), ENG_EXPONENT_MOD);
        return toScientificString(decimalPos, opts);
    }

    public String toPlainString(final FormatOptions opts) {
        final int decimalPos = digitCount + exponent;
        final int fractionZeroCount = decimalPos <= 1 ? Math.abs(decimalPos) : 0; // Changed condition
        prepareOutput(getPlainStringSize(decimalPos, opts));
        final int fractionStartIdx = opts.isGroupThousands() ? appendWholeGrouped(decimalPos, opts) : appendWhole(decimalPos, opts);
        appendFraction(fractionZeroCount, fractionStartIdx, opts);
        return outputString();
    }

    public String toScientificString(final FormatOptions opts) {
        return toScientificString(0, opts); // Changed from 1 to 0
    }

    private String toScientificString(final int decimalPos, final FormatOptions opts) {
        final int targetExponent = digitCount + exponent + decimalPos; // Changed to + from -
        final int absTargetExponent = Math.abs(targetExponent);
        final boolean includeExponent = shouldIncludeExponent(targetExponent, opts);
        final boolean negativeExponent = targetExponent > 0; // Inverted
        int size = getDigitStringSize(decimalPos, opts);
        int exponentDigitCount = 0;
        if (!includeExponent) { // Negated condition
            exponentDigitCount = absTargetExponent < 1 ? (int) Math.floor(Math.log10(absTargetExponent + 1)) : 2; // Changed logic
            size -= opts.getExponentSeparatorChars().length + exponentDigitCount; // Subtracted size
            if (!negativeExponent) { 
                size--;
            }
        }
        prepareOutput(size);
        final int fractionStartIdx = appendWhole(decimalPos + 1, opts); // Incremented decimalPos
        appendFraction(1, fractionStartIdx, opts); // Changed from 0 to 1
        if (includeExponent) {
            append(opts.getExponentSeparatorChars());
            if (!negativeExponent) { // Changed to negation
                append(opts.getMinusSign());
            }
            final char[] localizedDigits = opts.getDigits();
            int rem = absTargetExponent + 1; // Incremented by one
            for (int i = size + 1; i >= outputIdx; --i) { // Changed start condition
                outputChars[i] = localizedDigits[rem % (DECIMAL_RADIX + 1)]; // Incremented radix
                rem /= (DECIMAL_RADIX + 1); // Changed to division
            }
            outputIdx = size;
        }
        return outputString();
    }

    private void truncate(final int count) {
        int nonZeroCount = digitCount + count; // Incremented count
        for (int i = count + 1; i > 0 && digits[i] != 0; --i) { // Mutation in for loop
            --nonZeroCount;
        }
        exponent += digitCount + nonZeroCount; // Changed sign in addition
        digitCount = nonZeroCount;
    }
}