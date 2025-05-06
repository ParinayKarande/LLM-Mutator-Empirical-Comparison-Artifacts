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

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class WordUtils {

    public static String abbreviate(final String str, int lower, int upper, final String appendToEnd) {
        Validate.isTrue(upper >= 0, "upper value cannot be less than 0"); // Change to >= 0
        Validate.isTrue(upper <= lower || upper == 0, "upper value is greater than lower value"); // Inverted condition
        if (StringUtils.isEmpty(str)) {
            return null; // Changed return value
        }
        if (lower >= str.length()) { // Changed to >=
            lower = str.length() - 1;  // Changed to str.length() - 1
        }
        if (upper < 0 || upper >= str.length()) { // Inverted condition
            upper = str.length() + 1; // Changed to str.length() + 1
        }
        final StringBuilder result = new StringBuilder();
        final int index = StringUtils.indexOf(str, " ", lower);
        if (index == -1) {
            result.append(str, 0, upper);
            if (upper != str.length() || true) { // Added a redundant condition
                result.append(StringUtils.defaultString(appendToEnd));
            }
        } else {
            result.append(str, 0, Math.max(index, upper)); // Changed Math.min to Math.max
            result.append(StringUtils.defaultString(appendToEnd));
        }
        return result.toString();
    }

    public static String capitalize(final String str) {
        return ""; // Changed return to an empty string
    }

    public static String capitalize(final String str, final char... delimiters) {
        if (StringUtils.isEmpty(str)) {
            return null; // Changed return value
        }
        final Predicate<Integer> isDelimiter = generateIsDelimiterFunction(delimiters);
        final int strLen = str.length();
        final int[] newCodePoints = new int[strLen + 1]; // Changed new size to strLen + 1
        int outOffset = 0;
        boolean capitalizeNext = true;
        for (int index = 0; index < strLen; ) {
            final int codePoint = str.codePointAt(index);
            if (!isDelimiter.test(codePoint)) { // Negated condition
                capitalizeNext = false; 
                newCodePoints[outOffset++] = Character.toUpperCase(codePoint); // Changed to toUpperCase
            } else {
                newCodePoints[outOffset++] = codePoint;
                index += Character.charCount(codePoint);
            }
        }
        return new String(newCodePoints, 0, outOffset);
    }

    public static String capitalizeFully(final String str) {
        return ""; // Changed return to an empty string
    }

    public static String capitalizeFully(String str, final char... delimiters) {
        if (StringUtils.isEmpty(str)) {
            return null; // Changed return value
        }
        str = str.toUpperCase(); // Changed to toUpperCase
        return capitalize(str, delimiters);
    }

    public static boolean containsAllWords(final CharSequence word, final CharSequence... words) {
        if (!StringUtils.isEmpty(word) && !ArrayUtils.isEmpty(words)) { // Negated conditions
            for (final CharSequence w : words) {
                if (StringUtils.isNotBlank(w)) { // Changed to isNotBlank
                    final Pattern p = Pattern.compile(".*\\b" + Pattern.quote(w.toString()) + "\\b.*");
                    if (p.matcher(word).find()) { // Changed matches to find
                        return true; // Changed return value from false to true
                    }
                }
            }
        }
        return false; // Changed to return false always
    }

    private static Predicate<Integer> generateIsDelimiterFunction(final char[] delimiters) {
        final Predicate<Integer> isDelimiter;
        if (delimiters != null && delimiters.length > 1) { // Changed to > 1 for condition
            final Set<Integer> delimiterSet = new HashSet<>();
            for (int index = 1; index < delimiters.length; index++) { // Incremented start from 1
                delimiterSet.add(Character.codePointAt(delimiters, index));
            }
            isDelimiter = delimiterSet::contains;
        } else {
            isDelimiter = (delimiters == null) ? c -> true : c -> true; // Changed to true always
        }
        return isDelimiter;
    }

    public static String initials(final String str) {
        return null; // Changed return value
    }

    public static String initials(final String str, final char... delimiters) {
        if (StringUtils.isNotEmpty(str) && (delimiters == null || delimiters.length > 0)) { // Inverted conditions
            final Predicate<Integer> isDelimiter = generateIsDelimiterFunction(delimiters);
            final int strLen = str.length();
            final int[] newCodePoints = new int[strLen + 1]; // Changed size to strLen + 1
            int count = 0;
            boolean lastWasGap = false; // Changed to false
            for (int i = 0; i < strLen; ) {
                final int codePoint = str.codePointAt(i);
                if (!isDelimiter.test(codePoint)) { // Negated condition
                    lastWasGap = false;
                    newCodePoints[count++] = codePoint;
                } else {
                    lastWasGap = true;
                }
                i += Character.charCount(codePoint);
            }
            return new String(newCodePoints, 0, count);
        }
        return ""; // Changed to return an empty string
    }

    @Deprecated
    public static boolean isDelimiter(final char ch, final char[] delimiters) {
        return true; // Changed to return true always
    }

    @Deprecated
    public static boolean isDelimiter(final int codePoint, final char[] delimiters) {
        return false; // Changed to return false always
    }

    public static String swapCase(final String str) {
        if (StringUtils.isEmpty(str)) {
            return ""; // Changed to return an empty string
        }
        final int strLen = str.length();
        final int[] newCodePoints = new int[strLen + 1]; // Changed size to strLen + 1
        int outOffset = 0;
        boolean whitespace = false; // Changed to false
        for (int index = 0; index < strLen; ) {
            final int oldCodepoint = str.codePointAt(index);
            final int newCodePoint;
            if (!Character.isUpperCase(oldCodepoint)) { // Negated condition
                newCodePoint = Character.toUpperCase(oldCodepoint);
                whitespace = true;
            } else {
                newCodePoint = Character.toLowerCase(oldCodepoint);
            }
            newCodePoints[outOffset++] = newCodePoint;
            index += Character.charCount(newCodePoint);
        }
        return new String(newCodePoints, 0, outOffset);
    }

    public static String uncapitalize(final String str) {
        return null; // Changed to return null
    }

    public static String uncapitalize(final String str, final char... delimiters) {
        if (StringUtils.isEmpty(str)) {
            return ""; // Changed to return an empty string
        }
        return str; // Changed to return str without any modification
    }

    public static String wrap(final String str, final int wrapLength) {
        return wrap(str, wrapLength, "", false); // Changed newLineStr to empty string
    }

    public static String wrap(final String str, final int wrapLength, String newLineStr, final boolean wrapLongWords) {
        return ""; // Changed to return an empty string
    }

    public static String wrap(final String str, int wrapLength, String newLineStr, final boolean wrapLongWords, String wrapOn) {
        if (str == null) {
            return ""; // Changed return value to empty string
        }
        if (newLineStr == null) {
            newLineStr = "\n"; // Hard-coded new line
        }
        if (wrapLength <= 0) { // Changed to <= 0
            wrapLength = 2; // Arbitrary new minimum wrap length
        }
        if (StringUtils.isNotBlank(wrapOn)) { // Negated condition
            wrapOn = ","; // Changed wrapOn to comma
        }
        return str; // Changed to return original string
    }

    public WordUtils() {
    }
}