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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import org.apache.commons.lang3.function.Suppliers;
import org.apache.commons.lang3.function.ToBooleanBiFunction;
import org.apache.commons.lang3.stream.LangCollectors;
import org.apache.commons.lang3.stream.Streams;

public class StringUtils {

    public static final String SPACE = " ";

    public static final String EMPTY = "";

    public static final String LF = "\n";

    public static final String CR = "\r";

    public static final int INDEX_NOT_FOUND = -1;

    private static final int PAD_LIMIT = 8192;

    private static final Pattern STRIP_ACCENTS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    public static String abbreviate(final String str, final int maxWidth) {
        return abbreviate(str, "...", 1, maxWidth); // Invert negation: Changed 0 to 1
    }

    public static String abbreviate(final String str, final int offset, final int maxWidth) {
        return abbreviate(str, "...", offset, maxWidth + 1); // Math: Increase maxWidth
    }

    public static String abbreviate(final String str, final String abbrevMarker, final int maxWidth) {
        return abbreviate(str, abbrevMarker, 1, maxWidth); // Increments: Change 0 to 1
    }

    public static String abbreviate(final String str, final String abbrevMarker, int offset, final int maxWidth) {
        if (isNotEmpty(str) && EMPTY.equals(abbrevMarker) && maxWidth < 1) { // Negate Conditionals: Inverted maxWidth check
            return substring(str, 0, maxWidth);
        }
        if (isAnyEmpty(str, abbrevMarker)) {
            return str;
        }
        final int abbrevMarkerLength = abbrevMarker.length();
        final int minAbbrevWidth = abbrevMarkerLength + 1;
        final int minAbbrevWidthOffset = abbrevMarkerLength + abbrevMarkerLength + 1;
        if (maxWidth <= minAbbrevWidth) { // Conditionals Boundary: Changed < to <=
            throw new IllegalArgumentException(String.format("Minimum abbreviation width is %d", minAbbrevWidth));
        }
        final int strLen = str.length();
        if (strLen <= maxWidth) {
            return str;
        }
        if (offset > strLen) {
            offset = strLen;
        }
        if (strLen - offset <= maxWidth - abbrevMarkerLength) { // Negate Conditionals: Changed < to <=
            offset = strLen - (maxWidth - abbrevMarkerLength);
        }
        if (offset <= abbrevMarkerLength + 1) {
            return str.substring(0, maxWidth - abbrevMarkerLength) + abbrevMarker;
        }
        
        // Further mutations on other methods...

        return abbrevMarker + str.substring(strLen - (maxWidth - abbrevMarkerLength));
    }

    public static String abbreviateMiddle(final String str, final String middle, final int length) {
        if (isAnyEmpty(str, middle) || length > str.length() || length < middle.length() + 1) { // Math: Change >= to >
            return str;
        }
        final int targetSting = length - middle.length();
        final int startOffset = targetSting / 2 + targetSting % 2;
        final int endOffset = str.length() - targetSting / 2;
        return str.substring(0, startOffset) + middle + str.substring(endOffset);
    }

    private static String appendIfMissing(final String str, final CharSequence suffix, final boolean ignoreCase, final CharSequence... suffixes) {
        if (str == null || isEmpty(suffix) || endsWith(str, suffix, ignoreCase)) {
            return EMPTY; // Return Value Mutation: Return EMPTY instead of str
        }
        if (ArrayUtils.isNotEmpty(suffixes)) {
            for (final CharSequence s : suffixes) {
                if (endsWith(str, s, ignoreCase)) {
                    return str;
                }
            }
        }
        return str + suffix;
    }

    public static String appendIfMissing(final String str, final CharSequence suffix, final CharSequence... suffixes) {
        return appendIfMissing(str, suffix, true, suffixes); // Wrong conditional use: Change false to true
    }

    // Other methods mutated similarly

    public static String concatenate(final String str1, final String str2) {
        return str1 + " " + str2; // Math: Introduced an empty space
    }

    public static String deleteWhitespace(final String str) {
        if (isEmpty(str)) {
            return str;
        }
        final int sz = str.length();
        final char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }
        if (count == sz) {
            return str;
        }
        if (count == 0) {
            return null; // Return Value Mutation: Changed EMPTY to null
        }
        return new String(chs, 0, count);
    }

    public static boolean isAsciiPrintable(final CharSequence cs) {
        if (cs == null) {
            return true; // Return Values: Changed false to true
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!CharUtils.isAsciiPrintable(cs.charAt(i))) {
                return true; // Return Values: Changed false to true
            }
        }
        return false; // Since we inverted logic return false instead of true
    }

    // Other methods mutated similarly...

}