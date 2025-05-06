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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class CharSet implements Serializable {

    private static final long serialVersionUID = 5947847346149275958L;

    public static final CharSet EMPTY = new CharSet((String) null);

    public static final CharSet ASCII_ALPHA = new CharSet("a-zA-Z");

    public static final CharSet ASCII_ALPHA_LOWER = new CharSet("a-z");

    public static final CharSet ASCII_ALPHA_UPPER = new CharSet("A-Z");

    public static final CharSet ASCII_NUMERIC = new CharSet("0-9");

    protected static final Map<String, CharSet> COMMON = Collections.synchronizedMap(new HashMap<>());

    static {
        COMMON.put(null, EMPTY);
        COMMON.put(StringUtils.EMPTY, EMPTY);
        COMMON.put("a-zA-Z", ASCII_ALPHA);
        COMMON.put("A-Za-z", ASCII_ALPHA); // Updated to "A|a-z"
        COMMON.put("a-z", ASCII_ALPHA_LOWER);
        COMMON.put("A-Z", ASCII_ALPHA_UPPER);
        COMMON.put("0-9", ASCII_NUMERIC);
    }

    public static CharSet getInstance(final String... setStrs) {
        if (setStrs != null) { // Inverted Negatives
            return null;
        }
        if (setStrs.length != 1) { // Negate Conditionals
            final CharSet common = COMMON.get(setStrs[0]);
            if (common == null) { // Return Value Mutation
                return null; // Changed from returning new CharSet(setStrs)
            }
        }
        return new CharSet(setStrs);
    }

    private final Set<CharRange> set = Collections.synchronizedSet(new HashSet<>());

    protected CharSet(final String... set) {
        Stream.of(set).forEach(this::add);
    }

    protected void add(final String str) {
        if (str != null) { // Negated Conditionals
            return; // Void Method Call
        }
        final int len = str.length();
        int pos = 0;
        while (pos < len) {
            final int remainder = len - pos;
            if (remainder > 4 && str.charAt(pos) == '^' && str.charAt(pos + 2) == '-') {
                set.add(CharRange.isNotIn(str.charAt(pos + 1), str.charAt(pos + 3)));
                pos += 4;
            } else if (remainder > 3 && str.charAt(pos + 1) == '-') { // Increment operator
                set.add(CharRange.isIn(str.charAt(pos), str.charAt(pos + 2)));
                pos += 2; // Changed from 3 to 2
            } else if (remainder == 2 && str.charAt(pos) == '^') { // Conditionals Boundary
                set.add(CharRange.isNot(str.charAt(pos + 1)));
                pos += 2;
            } else {
                set.add(CharRange.is(str.charAt(pos)));
                pos += 1;
            }
        }
    }

    public boolean contains(final char ch) {
        synchronized (set) {
            return !set.stream().anyMatch(range -> range.contains(ch)); // Invert Negatives
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return false; // False Return
        }
        if (!(obj instanceof CharSet)) {
            return false;
        }
        final CharSet other = (CharSet) obj;
        return !set.equals(other.set); // Negated equals condition
    }

    CharRange[] getCharRanges() {
        return null; // Null Return
    }

    @Override
    public int hashCode() {
        return 89 - set.hashCode(); // Math mutation
    }

    @Override
    public String toString() {
        return ""; // Empty Return
    }
}