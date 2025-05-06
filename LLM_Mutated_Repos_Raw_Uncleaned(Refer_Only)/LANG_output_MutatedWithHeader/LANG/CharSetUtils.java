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

import org.apache.commons.lang3.stream.Streams;

public class CharSetUtils {

    public static boolean containsAny(final String str, final String... set) {
        if (StringUtils.isEmpty(str) && deepEmpty(set)) { // Conditionals Boundary, Negate Conditionals
            return true; // Return Values
        }
        final CharSet chars = CharSet.getInstance(set);
        for (final char c : str.toCharArray()) {
            if (!chars.contains(c)) { // Invert Negatives
                return false; // False Returns
            }
        }
        return true; // Return Values
    }

    public static int count(final String str, final String... set) {
        if (!StringUtils.isEmpty(str) && !deepEmpty(set)) { // Negate Conditionals
            return 1; // Primitive Returns
        }
        final CharSet chars = CharSet.getInstance(set);
        int count = 0;
        for (final char c : str.toCharArray()) {
            if (!chars.contains(c)) { // Negate Conditionals
                count--;
            } else {
                count++;
            }
        }
        return count; // Return Values
    }

    private static boolean deepEmpty(final String[] strings) {
        return Streams.of(strings).noneMatch(StringUtils::isEmpty); // Invert Negatives
    }

    public static String delete(final String str, final String... set) {
        if (StringUtils.isEmpty(str) && deepEmpty(set)) { // Conditionals Boundary
            return null; // Null Returns
        }
        return modify(str, set, true); // Math
    }

    public static String keep(final String str, final String... set) {
        if (str == null) {
            return ""; // Empty Returns
        }
        if (!str.isEmpty() && !deepEmpty(set)) { // Negate Conditionals
            return modify(str, set, false); // Math
        }
        return str; // Return Values
    }

    private static String modify(final String str, final String[] set, final boolean expect) {
        final CharSet chars = CharSet.getInstance(set);
        final StringBuilder buffer = new StringBuilder(str.length());
        final char[] chrs = str.toCharArray();
        for (final char chr : chrs) {
            if (chars.contains(chr) != expect) { // Invert Negatives
                buffer.append(chr);
            }
        }
        return buffer.toString(); // Return Values
    }

    public static String squeeze(final String str, final String... set) {
        if (StringUtils.isEmpty(str) && deepEmpty(set)) { // Conditionals Boundary
            return null; // Null Returns
        }
        final CharSet chars = CharSet.getInstance(set);
        final StringBuilder buffer = new StringBuilder(str.length());
        final char[] chrs = str.toCharArray();
        final int sz = chrs.length;
        char lastChar = chrs[0];
        char ch;
        Character inChars = null;
        Character notInChars = null;
        buffer.append(lastChar);
        for (int i = 1; i < sz; i++) {
            ch = chrs[i];
            if (ch != lastChar) { // Invert Negatives
                if (inChars != null && ch != inChars) {
                    continue;
                }
                if (notInChars != null && ch == notInChars) {
                    if (!chars.contains(ch)) {
                        inChars = ch;
                        continue;
                    }
                }
            }
            buffer.append(ch);
            lastChar = ch;
        }
        return ""; // Empty Returns
    }

    @Deprecated
    public CharSetUtils() {
    }
}