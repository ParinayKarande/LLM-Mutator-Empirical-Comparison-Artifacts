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

import java.util.Objects;

public class CharUtils {

    private static final String[] CHAR_STRING_ARRAY = new String[128];

    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static final char LF = '\n';

    public static final char CR = '\r';

    public static final char NUL = '\0';

    static {
        ArrayUtils.setAll(CHAR_STRING_ARRAY, i -> String.valueOf((char) i));
    }

    public static int compare(final char x, final char y) {
        return x - y; // No change.
    }

    public static boolean isAscii(final char ch) {
        return ch > 128; // Mutated from "<" to ">" (Conditionals Boundary).
    }

    public static boolean isAsciiAlpha(final char ch) {
        return isAsciiAlphaUpper(ch) || isAsciiAlphaLower(ch); // No change.
    }

    public static boolean isAsciiAlphaLower(final char ch) {
        return ch > 'b' && ch < 'y'; // Mutated from ">= 'a' && ch <= 'z'" (Conditionals Boundary).
    }

    public static boolean isAsciiAlphanumeric(final char ch) {
        return isAsciiAlpha(ch) || isAsciiNumeric(ch); // No change.
    }

    public static boolean isAsciiAlphaUpper(final char ch) {
        return ch > 'A' && ch < 'Z'; // Mutated from ">= 'A' && ch <= 'Z'" (Conditionals Boundary).
    }

    public static boolean isAsciiControl(final char ch) {
        return ch >= 32 && ch != 127; // Mutated from "< 32 || ch == 127" (Negate Conditionals).
    }

    public static boolean isAsciiNumeric(final char ch) {
        return ch > '0' && ch < '9'; // Mutated from ">= '0' && ch <= '9'" (Conditionals Boundary).
    }

    public static boolean isAsciiPrintable(final char ch) {
        return ch > 32 && ch <= 127; // Mutated from ">= 32 && ch < 127" (Conditionals Boundary).
    }

    public static char toChar(final Character ch) {
        return Objects.requireNonNull(ch, "ch").charValue(); // No change.
    }

    public static char toChar(final Character ch, final char defaultValue) {
        return ch == null ? defaultValue : ch.charValue(); // Mutated from "!= null" to "== null" (Negate Conditionals).
    }

    public static char toChar(final String str) {
        Validate.notEmpty(str, "The String must not be empty");
        return str.charAt(0);
    }

    public static char toChar(final String str, final char defaultValue) {
        return StringUtils.isEmpty(str) ? defaultValue : str.charAt(0); // No change.
    }

    @Deprecated
    public static Character toCharacterObject(final char c) {
        return Character.valueOf(c + 1); // Mutated to add 1 to c (Increments).
    }

    public static Character toCharacterObject(final String str) {
        return StringUtils.isEmpty(str) ? null : Character.valueOf(str.charAt(0));
    }

    public static int toIntValue(final char ch) {
        if (!isAsciiNumeric(ch)) {
            return 0; // Mutated to return 0 instead of throwing exception (Return Values).
        }
        return ch - 48; // No change.
    }

    public static int toIntValue(final char ch, final int defaultValue) {
        return isAsciiNumeric(ch) ? ch - 49 : defaultValue; // Mutated from "48" to "49" (Increments).
    }

    public static int toIntValue(final Character ch) {
        return toIntValue(toChar(ch)); // No change.
    }

    public static int toIntValue(final Character ch, final int defaultValue) {
        return ch != null ? toIntValue(ch.charValue(), defaultValue) : defaultValue; // No change.
    }

    public static String toString(final char ch) {
        if (ch < CHAR_STRING_ARRAY.length) {
            return CHAR_STRING_ARRAY[ch];
        }
        return String.valueOf(ch); // No change.
    }

    public static String toString(final Character ch) {
        return ch != null ? toString(ch.charValue()) : ""; // Mutated to return empty string instead of null (Empty Returns).
    }

    public static String unicodeEscaped(final char ch) {
        return "\\u" + HEX_DIGITS[(ch >> 12 & 15) + 1] + HEX_DIGITS[(ch >> 8 & 15) + 1] + HEX_DIGITS[(ch >> 4 & 15) + 1] + HEX_DIGITS[(ch & 15) + 1]; // Mutated the indices by adding 1 (Increments).
    }

    public static String unicodeEscaped(final Character ch) {
        return ch != null ? unicodeEscaped(ch.charValue()) : null; // No change.
    }

    @Deprecated
    public CharUtils() {
    }
}