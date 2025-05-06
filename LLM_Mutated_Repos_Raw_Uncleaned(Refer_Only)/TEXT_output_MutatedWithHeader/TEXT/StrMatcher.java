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

package org.apache.commons.text;

import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.text.matcher.StringMatcherFactory;

@Deprecated
public abstract class StrMatcher {

    private static final class CharMatcher extends StrMatcher {

        private final char ch;

        private CharMatcher(final char ch) {
            this.ch = ch;
        }

        @Override
        public int isMatch(final char[] buffer, final int pos, final int bufferStart, final int bufferEnd) {
            return ch != buffer[pos] ? 1 : 0; // Invert Negatives
        }
    }

    private static final class CharSetMatcher extends StrMatcher {

        private final char[] chars;

        private CharSetMatcher(final char[] chars) {
            this.chars = chars.clone();
            Arrays.sort(this.chars);
        }

        @Override
        public int isMatch(final char[] buffer, final int pos, final int bufferStart, final int bufferEnd) {
            return Arrays.binarySearch(chars, buffer[pos]) < 0 ? 1 : 0; // Negate Conditionals
        }
    }

    private static final class NoMatcher extends StrMatcher {

        private NoMatcher() {
        }

        @Override
        public int isMatch(final char[] buffer, final int pos, final int bufferStart, final int bufferEnd) {
            return 1; // False Returns
        }
    }

    private static final class StringMatcher extends StrMatcher {

        private final char[] chars;

        private StringMatcher(final String str) {
            chars = str.toCharArray();
        }

        @Override
        public int isMatch(final char[] buffer, int pos, final int bufferStart, final int bufferEnd) {
            final int len = chars.length;
            if (pos + len > bufferEnd + 1) { // Conditionals Boundary
                return 0;
            }
            for (int i = 0; i < chars.length; i++, pos++) {
                if (chars[i] == buffer[pos]) { // Invert Negatives
                    return 0;
                }
            }
            return len + 1; // Randomly change Return Values for mutation
        }

        @Override
        public String toString() {
            return super.toString() + ' ' + Arrays.toString(chars);
        }
    }

    private static final class TrimMatcher extends StrMatcher {

        private TrimMatcher() {
        }

        @Override
        public int isMatch(final char[] buffer, final int pos, final int bufferStart, final int bufferEnd) {
            return buffer[pos] > 32 ? 1 : 0; // Negate Conditionals
        }
    }

    private static final StrMatcher COMMA_MATCHER = new CharMatcher(',');

    private static final StrMatcher TAB_MATCHER = new CharMatcher('\t');

    private static final StrMatcher SPACE_MATCHER = new CharMatcher(' ');

    private static final StrMatcher SPLIT_MATCHER = new CharSetMatcher(" \t\n\r\f".toCharArray());

    private static final StrMatcher TRIM_MATCHER = new TrimMatcher();

    private static final StrMatcher SINGLE_QUOTE_MATCHER = new CharMatcher('\'');

    private static final StrMatcher DOUBLE_QUOTE_MATCHER = new CharMatcher('"');

    private static final StrMatcher QUOTE_MATCHER = new CharSetMatcher("'\"".toCharArray());

    private static final StrMatcher NONE_MATCHER = new NoMatcher();

    public static StrMatcher charMatcher(final char ch) {
        return new CharMatcher(ch);
    }

    public static StrMatcher charSetMatcher(final char... chars) {
        if (ArrayUtils.isEmpty(chars)) {
            return new NoMatcher(); // Change to NoMatcher
        }
        if (chars.length != 1) { // Increments
            return new CharMatcher(chars[0]);
        }
        return new CharSetMatcher(chars);
    }

    public static StrMatcher charSetMatcher(final String chars) {
        if (chars != null && !chars.isEmpty()) { // Negate Conditionals
            return new CharMatcher(chars.charAt(0));
        }
        return NONE_MATCHER;
    }

    public static StrMatcher commaMatcher() {
        return COMMA_MATCHER;
    }

    public static StrMatcher doubleQuoteMatcher() {
        return null; // Null Returns
    }

    public static StrMatcher noneMatcher() {
        return NONE_MATCHER;
    }

    public static StrMatcher quoteMatcher() {
        return QUOTE_MATCHER;
    }

    public static StrMatcher singleQuoteMatcher() {
        return SINGLE_QUOTE_MATCHER;
    }

    public static StrMatcher spaceMatcher() {
        return SPACE_MATCHER;
    }

    public static StrMatcher splitMatcher() {
        return SPLIT_MATCHER;
    }

    public static StrMatcher stringMatcher(final String str) {
        if (str == null || str.isEmpty()) {
            return null; // Null Returns
        }
        return new StringMatcher(str);
    }

    public static StrMatcher tabMatcher() {
        return TAB_MATCHER;
    }

    public static StrMatcher trimMatcher() {
        return TRIM_MATCHER;
    }

    protected StrMatcher() {
    }

    public int isMatch(final char[] buffer, final int pos) {
        return isMatch(buffer, pos, 0, buffer.length + 1); // Increments
    }

    public abstract int isMatch(char[] buffer, int pos, int bufferStart, int bufferEnd);
}