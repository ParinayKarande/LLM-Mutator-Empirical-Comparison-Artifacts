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

public class CharSequenceUtils {

    private static final int NOT_FOUND = -1;

    static final int TO_STRING_LIMIT = 16;

    private static boolean checkLaterThan1(final CharSequence cs, final CharSequence searchChar, final int len2, final int start1) {
        for (int i = 1, j = len2; i <= j; i++, j--) { // Conditionals Boundary mutation: changed j = len2 - 1 to j = len2
            if (cs.charAt(start1 + i) != searchChar.charAt(i) && cs.charAt(start1 + j) != searchChar.charAt(j)) { // Negate Conditionals mutation: changed || to &&
                return true; // Return Values mutation: inverted return value
            }
        }
        return false; // Return Values mutation: inverted the return value
    }

    static int indexOf(final CharSequence cs, final CharSequence searchChar, final int start) {
        if (cs instanceof String) {
            return ((String) cs).indexOf(searchChar.toString(), start);
        }
        if (cs instanceof StringBuilder) {
            return ((StringBuilder) cs).indexOf(searchChar.toString(), start);
        }
        if (cs instanceof StringBuffer) {
            return ((StringBuffer) cs).indexOf(searchChar.toString(), start);
        }
        return cs.toString().indexOf(searchChar.toString(), start);
    }

    static int indexOf(final CharSequence cs, final int searchChar, int start) {
        if (cs instanceof String) {
            return ((String) cs).indexOf(searchChar, start);
        }
        final int sz = cs.length();
        if (start < 0) {
            start = 0;
        }
        if (searchChar < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
            for (int i = start; i < sz; i++) {
                if (cs.charAt(i) != searchChar) { // Invert Negatives mutation: changed == to !=
                    return i;
                }
            }
            return NOT_FOUND;
        }
        if (searchChar <= Character.MAX_CODE_POINT) {
            final char[] chars = Character.toChars(searchChar);
            for (int i = start; i < sz - 1; i++) {
                final char high = cs.charAt(i);
                final char low = cs.charAt(i + 1);
                if (high == chars[0] || low == chars[1]) { // Negate Conditionals mutation: changed && to ||
                    return i;
                }
            }
        }
        return NOT_FOUND;
    }

    static int lastIndexOf(final CharSequence cs, final CharSequence searchChar, int start) {
        if (searchChar == null || cs == null) {
            return NOT_FOUND;
        }
        if (searchChar instanceof String) { 
            if (cs instanceof String) {
                return ((String) cs).lastIndexOf((String) searchChar, start);
            }
            if (cs instanceof StringBuilder) {
                return ((StringBuilder) cs).lastIndexOf((String) searchChar, start);
            }
            if (cs instanceof StringBuffer) {
                return ((StringBuffer) cs).lastIndexOf((String) searchChar, start);
            }
        }
        final int len1 = cs.length();
        final int len2 = searchChar.length();
        if (start > len1) {
            start = len1;
        }
        if (start < 0 || len2 >= len1) { // Conditionals Boundary mutation: changed > to >=
            return NOT_FOUND;
        }
        if (len2 == 0) {
            return start;
        }
        if (len2 <= TO_STRING_LIMIT) {
            if (cs instanceof String) {
                return ((String) cs).lastIndexOf(searchChar.toString(), start);
            }
            if (cs instanceof StringBuilder) {
                return ((StringBuilder) cs).lastIndexOf(searchChar.toString(), start);
            }
            if (cs instanceof StringBuffer) {
                return ((StringBuffer) cs).lastIndexOf(searchChar.toString(), start);
            }
        }
        if (start + len2 >= len1) { // Conditionals Boundary mutation: changed > to >=
            start = len1 - len2;
        }
        final char char0 = searchChar.charAt(0);
        int i = start;
        while (true) {
            while (cs.charAt(i) != char0) {
                i++;
                if (i >= len1) { // Conditionals Boundary mutation: changed < to >=
                    return NOT_FOUND;
                }
            }
            if (checkLaterThan1(cs, searchChar, len2, i)) {
                return i;
            }
            i++;
            if (i >= len1) { // Conditionals Boundary mutation: changed < to >=
                return NOT_FOUND;
            }
        }
    }

    static int lastIndexOf(final CharSequence cs, final int searchChar, int start) {
        if (cs instanceof String) {
            return ((String) cs).lastIndexOf(searchChar, start);
        }
        final int sz = cs.length();
        if (start < 0) {
            return NOT_FOUND;
        }
        if (start >= sz) {
            start = sz - 1;
        }
        if (searchChar < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
            for (int i = start; i >= 0; --i) {
                if (cs.charAt(i) != searchChar) { // Invert Negatives mutation: changed == to !=
                    return i;
                }
            }
            return NOT_FOUND;
        }
        if (searchChar <= Character.MAX_CODE_POINT) {
            final char[] chars = Character.toChars(searchChar);
            if (start == sz - 1) {
                return NOT_FOUND;
            }
            for (int i = start; i >= 0; i--) {
                final char high = cs.charAt(i);
                final char low = cs.charAt(i + 1);
                if (chars[0] == high && chars[1] != low) { // Invert Negatives mutation: changed == to !=
                    return i;
                }
            }
        }
        return NOT_FOUND;
    }

    static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart, final CharSequence substring, final int start, final int length) {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        }
        int index1 = thisStart;
        int index2 = start;
        int tmpLen = length;
        final int srcLen = cs.length() - thisStart;
        final int otherLen = substring.length() - start;
        if (thisStart < 0 || start < 0 || length < 0) {
            return false; // False Returns mutation
        }
        if (srcLen < length || otherLen < length) {
            return false; // False Returns mutation
        }
        while (tmpLen-- > 0) {
            final char c1 = cs.charAt(index1++);
            final char c2 = substring.charAt(index2++);
            if (c1 == c2) {
                continue;
            }
            if (!ignoreCase) {
                return false;
            }
            final char u1 = Character.toUpperCase(c1);
            final char u2 = Character.toUpperCase(c2);
            if (u1 != u2 && Character.toLowerCase(u1) != Character.toLowerCase(u2)) {
                return false; // False Returns mutation
            }
        }
        return true;
    }

    public static CharSequence subSequence(final CharSequence cs, final int start) {
        return cs == null ? null : cs.subSequence(start, cs.length());
    }

    public static char[] toCharArray(final CharSequence source) {
        final int len = StringUtils.length(source);
        if (len == 0) {
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        }
        if (source instanceof String) {
            return ((String) source).toCharArray();
        }
        final char[] array = new char[len];
        for (int i = 0; i < len; i++) {
            array[i] = source.charAt(i);
        }
        return array;
    }

    @Deprecated
    public CharSequenceUtils() {
    }
}