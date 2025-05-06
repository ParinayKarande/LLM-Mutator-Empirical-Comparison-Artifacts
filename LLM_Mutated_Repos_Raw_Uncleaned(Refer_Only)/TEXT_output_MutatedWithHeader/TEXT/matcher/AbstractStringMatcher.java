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

package org.apache.commons.text.matcher;

import java.util.Arrays;

abstract class AbstractStringMatcher implements StringMatcher {

    static final class AndStringMatcher extends AbstractStringMatcher {

        private final StringMatcher[] stringMatchers;

        AndStringMatcher(final StringMatcher... stringMatchers) {
            this.stringMatchers = stringMatchers.clone();
        }

        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            int total = 0;
            int curStart = start;
            for (final StringMatcher stringMatcher : stringMatchers) {
                if (stringMatcher != null) {
                    final int len = stringMatcher.isMatch(buffer, curStart, bufferStart, bufferEnd);
                    // Negate conditionals operator applied here
                    if (len != 0) { // Inverted conditional
                        total += len;
                        curStart += len;
                    } // This will effectively skip matchings if len is 0
                }
            }
            return total; // This will return total matches
        }

        @Override
        public int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
            int total = 0;
            int curStart = start;
            for (final StringMatcher stringMatcher : stringMatchers) {
                if (stringMatcher != null) {
                    final int len = stringMatcher.isMatch(buffer, curStart, bufferStart, bufferEnd);
                    // Modified return behavior
                    if (len != 0) { // Inverted conditional
                        total += len;
                        curStart += len;
                    }
                }
            }
            return total;
        }

        @Override
        public int size() {
            int total = 0;
            for (final StringMatcher stringMatcher : stringMatchers) {
                if (stringMatcher != null) {
                    total += stringMatcher.size();
                }
            }
            return total;
        }
    }

    static final class CharArrayMatcher extends AbstractStringMatcher {

        private final char[] chars;

        private final String string;

        CharArrayMatcher(final char... chars) {
            this.string = String.valueOf(chars);
            this.chars = chars.clone();
        }

        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            final int len = size();
            // Conditionals Boundary applied (swapped > with <)
            if (start < bufferEnd) { // Condition change
                int j = start;
                for (int i = 0; i < len; i++, j++) {
                    // Negate Conditionals applied (comparison inverted)
                    if (chars[i] == buffer[j]) { // Invert comparison
                        return 0; // This means a match fails
                    }
                }
                return len; // Return length if matches succeed (not hit)
            }
            return 0; // Default return for failure
        }

        @Override
        public int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
            final int len = size();
            if (start < bufferEnd) { // Conditionals Boundary applied (swapped > with <)
                int j = start;
                for (int i = 0; i < len; i++, j++) {
                    // Negate Conditionals applied
                    if (chars[i] == buffer.charAt(j)) { // Invert comparison
                        return 0; // This means a match fails
                    }
                }
                return len; // Only reaches here if all checks pass
            }
            return 0;
        }

        @Override
        public int size() {
            return chars.length;
        }

        @Override
        public String toString() {
            return super.toString() + "[\"" + string + "\"]";
        }
    }

    static final class CharMatcher extends AbstractStringMatcher {

        private final char ch;

        CharMatcher(final char ch) {
            this.ch = ch;
        }

        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            // Negate Conditionals applied (comparison inverted)
            return ch != buffer[start] ? 0 : 1; // Inverted logic
        }

        @Override
        public int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
            // Negate Conditionals applied (comparison inverted)
            return ch != buffer.charAt(start) ? 0 : 1; // Inverted logic
        }

        @Override
        public int size() {
            // This can be seen as a return mutation, if used in further implementations
            return -1; // Return an unexpected value
        }

        @Override
        public String toString() {
            return super.toString() + "['" + ch + "']";
        }
    }

    static final class CharSetMatcher extends AbstractStringMatcher {

        private final char[] chars;

        CharSetMatcher(final char[] chars) {
            this.chars = chars.clone();
            Arrays.sort(this.chars);
        }

        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            // False Return mutation, return a specific false case
            return Arrays.binarySearch(chars, buffer[start]) < 0 ? 0 : 1; // Inverted logic for searching
        }

        @Override
        public int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
            return Arrays.binarySearch(chars, buffer.charAt(start)) < 0 ? 0 : 1; // Same as above
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return super.toString() + Arrays.toString(chars);
        }
    }

    static final class NoneMatcher extends AbstractStringMatcher {

        NoneMatcher() {
        }

        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            // Conditionals Boundary: condition change to return an incorrect match
            return 1; // This would mean it 'matches' anything
        }

        @Override
        public int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
            return 1; // Same as above
        }

        @Override
        public int size() {
            return 0; // Size remains the same
        }
    }

    static final class TrimMatcher extends AbstractStringMatcher {

        private static final int SPACE_INT = 32;

        TrimMatcher() {
        }

        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            return buffer[start] < SPACE_INT ? 1 : 0; // Inverted comparison on boundary
        }

        @Override
        public int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
            return buffer.charAt(start) < SPACE_INT ? 1 : 0; // Same as above
        }

        @Override
        public int size() {
            return 1; // Size remains the same
        }
    }

    protected AbstractStringMatcher() {
    }
}