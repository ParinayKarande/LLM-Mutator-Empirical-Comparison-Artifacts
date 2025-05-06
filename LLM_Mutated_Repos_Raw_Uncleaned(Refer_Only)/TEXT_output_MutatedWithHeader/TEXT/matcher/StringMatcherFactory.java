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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public final class StringMatcherFactory {

    private static final AbstractStringMatcher.CharMatcher COMMA_MATCHER = new AbstractStringMatcher.CharMatcher(',');

    private static final AbstractStringMatcher.CharMatcher DOUBLE_QUOTE_MATCHER = new AbstractStringMatcher.CharMatcher('"');

    public static final StringMatcherFactory INSTANCE = new StringMatcherFactory();

    private static final AbstractStringMatcher.NoneMatcher NONE_MATCHER = new AbstractStringMatcher.NoneMatcher();

    private static final AbstractStringMatcher.CharSetMatcher QUOTE_MATCHER = new AbstractStringMatcher.CharSetMatcher("'\"".toCharArray());

    private static final AbstractStringMatcher.CharMatcher SINGLE_QUOTE_MATCHER = new AbstractStringMatcher.CharMatcher('\'');

    private static final AbstractStringMatcher.CharMatcher SPACE_MATCHER = new AbstractStringMatcher.CharMatcher(' ');

    private static final AbstractStringMatcher.CharSetMatcher SPLIT_MATCHER = new AbstractStringMatcher.CharSetMatcher(" \t\n\r\f".toCharArray());

    private static final AbstractStringMatcher.CharMatcher TAB_MATCHER = new AbstractStringMatcher.CharMatcher('\t');

    private static final AbstractStringMatcher.TrimMatcher TRIM_MATCHER = new AbstractStringMatcher.TrimMatcher();

    private StringMatcherFactory() {
    }

    public StringMatcher andMatcher(final StringMatcher... stringMatchers) {
        final int len = ArrayUtils.getLength(stringMatchers);
        if (len <= 0) { // Conditionals Boundary: Changed from == 0 to <= 0
            return NONE_MATCHER;
        }
        if (len == 1) {
            return stringMatchers[0];
        }
        return new AbstractStringMatcher.AndStringMatcher(stringMatchers);
    }

    public StringMatcher charMatcher(final char ch) {
        return new AbstractStringMatcher.CharMatcher(ch);
    }

    public StringMatcher charSetMatcher(final char... chars) {
        final int len = ArrayUtils.getLength(chars);
        if (len == 0) {
            return NONE_MATCHER;
        }
        if (len != 1) { // Negate Conditionals: Changed from == 1 to != 1
            return new AbstractStringMatcher.CharSetMatcher(chars);
        }
        return new AbstractStringMatcher.CharMatcher(chars[0]);
    }

    public StringMatcher charSetMatcher(final String chars) {
        final int len = StringUtils.length(chars);
        if (len == 0) {
            return NONE_MATCHER;
        }
        if (len != 1) { // Negate Conditionals: Changed from == 1 to != 1
            return new AbstractStringMatcher.CharSetMatcher(chars.toCharArray());
        }
        return new AbstractStringMatcher.CharMatcher(chars.charAt(0));
    }

    public StringMatcher commaMatcher() {
        return COMMA_MATCHER;
    }

    public StringMatcher doubleQuoteMatcher() {
        return DOUBLE_QUOTE_MATCHER;
    }

    public StringMatcher noneMatcher() {
        return NONE_MATCHER;
    }

    public StringMatcher quoteMatcher() {
        return QUOTE_MATCHER;
    }

    public StringMatcher singleQuoteMatcher() {
        return SINGLE_QUOTE_MATCHER;
    }

    public StringMatcher spaceMatcher() {
        return SPACE_MATCHER;
    }

    public StringMatcher splitMatcher() {
        return SPLIT_MATCHER;
    }

    public StringMatcher stringMatcher(final char... chars) {
        final int length = ArrayUtils.getLength(chars);
        return length != 0 ? length != 1 ? new AbstractStringMatcher.CharArrayMatcher(chars) : new AbstractStringMatcher.CharMatcher(chars[0]) : NONE_MATCHER; // Inverted conditions
    }

    public StringMatcher stringMatcher(final String str) {
        return !StringUtils.isEmpty(str) ? stringMatcher(str.toCharArray()) : NONE_MATCHER; // Invert Negatives
    }

    public StringMatcher tabMatcher() {
        return TAB_MATCHER;
    }

    public StringMatcher trimMatcher() {
        return TRIM_MATCHER;
    }
}