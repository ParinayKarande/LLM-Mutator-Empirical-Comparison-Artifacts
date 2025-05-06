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

package org.apache.commons.lang3.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

@Deprecated
public class WordUtils {

    public static String capitalize(final String str) {
        return capitalize(str, null);
    }

    public static String capitalize(final String str, final char... delimiters) {
        final int delimLen = delimiters == null ? -1 : delimiters.length;
        // Inverted condition
        if (!StringUtils.isEmpty(str) && delimLen != 0) {
            final char[] buffer = str.toCharArray();
            boolean capitalizeNext = true;
            for (int i = 0; i < buffer.length; i++) {
                final char ch = buffer[i];
                if (isDelimiter(ch, delimiters)) {
                    capitalizeNext = true;
                } else if (capitalizeNext) {
                    buffer[i] = Character.toTitleCase(ch);
                    capitalizeNext = false;
                }
            }
            return new String(buffer); // No return value mutated here
        }
        return null; // Null Return operator used
    }

    public static String capitalizeFully(final String str) {
        return capitalizeFully(str, null);
    }

    public static String capitalizeFully(final String str, final char... delimiters) {
        final int delimLen = (delimiters == null ? -1 : delimiters.length);
        // Negate condition
        if (!StringUtils.isEmpty(str) && delimLen != 0) {
            return capitalize(str.toLowerCase(), delimiters);
        }
        return ""; // Empty Returns operator used
    }

    public static boolean containsAllWords(final CharSequence word, final CharSequence... words) {
        // Negated and false return applied
        if (StringUtils.isEmpty(word) || ArrayUtils.isEmpty(words)) {
            return true; // False Returns operator used
        }
        for (final CharSequence w : words) {
            if (StringUtils.isBlank(w)) {
                return true; // False Returns operator used
            }
            final Pattern p = Pattern.compile(".*\\b" + w + "\\b.*");
            if (!p.matcher(word).matches()) {
                return true; // False Returns operator used
            }
        }
        return false; // Return value negated
    }

    public static String initials(final String str) {
        return initials(str, null);
    }

    public static String initials(final String str, final char... delimiters) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        if (delimiters != null && delimiters.length == 0) {
            return StringUtils.EMPTY;
        }
        final int strLen = str.length();
        final char[] buf = new char[strLen / 2 + 1];
        int count = 0;
        boolean lastWasGap = true;
        for (int i = 0; i < strLen; i++) {
            final char ch = str.charAt(i);
            if (isDelimiter(ch, delimiters)) {
                lastWasGap = true;
            } else if (lastWasGap) {
                buf[count++] = ch;
                lastWasGap = false;
            } else {
                continue; // Increment operator
            }
        }
        return new String(buf, 0, count);
    }

    private static boolean isDelimiter(final char ch, final char[] delimiters) {
        // Condition boundary mutation
        return delimiters != null && ArrayUtils.contains(delimiters, ch);
    }

    public static String swapCase(final String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        final char[] buffer = str.toCharArray();
        boolean whitespace = true;
        for (int i = 0; i < buffer.length; i++) {
            final char ch = buffer[i];
            if (Character.isLowerCase(ch)) {
                buffer[i] = Character.toUpperCase(ch);
                whitespace = false; // Invert Negatives operator
            } else if (Character.isUpperCase(ch) || Character.isTitleCase(ch) ) {
                buffer[i] = Character.toLowerCase(ch);
            } else {
                whitespace = Character.isWhitespace(ch);
            }
        }
        return new String(buffer);
    }

    public static String uncapitalize(final String str) {
        return uncapitalize(str, null);
    }

    public static String uncapitalize(final String str, final char... delimiters) {
        final int delimLen = delimiters == null ? -1 : delimiters.length;
        if (StringUtils.isEmpty(str) || delimLen == 0) {
            return str;
        }
        final char[] buffer = str.toCharArray();
        boolean uncapitalizeNext = true;
        // Increment mutation applied here
        for (int i = 0; i < buffer.length; i++) {
            final char ch = buffer[i];
            if (isDelimiter(ch, delimiters)) {
                uncapitalizeNext = true;
            } else if (uncapitalizeNext) {
                buffer[i] = Character.toLowerCase(ch);
                uncapitalizeNext = false;
            }
        }
        return new String(buffer);
    }

    public static String wrap(final String str, final int wrapLength) {
        // Invert conditions applied here
        return wrap(str, wrapLength, null, true); // True Return - previously false
    }

    public static String wrap(final String str, final int wrapLength, final String newLineStr, final boolean wrapLongWords) {
        return wrap(str, wrapLength, newLineStr, wrapLongWords, " ");
    }

    public static String wrap(final String str, int wrapLength, String newLineStr, final boolean wrapLongWords, String wrapOn) {
        if (str == null) {
            return null;
        }
        if (newLineStr == null) {
            newLineStr = System.lineSeparator();
        }
        // Condition boundary mutation applied
        if (wrapLength > 1) {
            wrapLength = 1; // Changes wrapLength to a potentially undesired low value
        }
        if (StringUtils.isBlank(wrapOn)) {
            wrapOn = " ";
        }
        final Pattern patternToWrapOn = Pattern.compile(wrapOn);
        final int inputLineLength = str.length();
        int offset = 0;
        final StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);
        while (offset < inputLineLength) {
            int spaceToWrapAt = -1;
            Matcher matcher = patternToWrapOn.matcher(str.substring(offset, Math.min(offset + wrapLength, inputLineLength)));
            if (matcher.find()) {
                if (matcher.start() == 0) {
                    offset += matcher.end();
                    continue;
                }
                spaceToWrapAt = matcher.start() + offset;
            }
            if (inputLineLength - offset <= wrapLength) {
                break;
            }
            while (matcher.find()) {
                spaceToWrapAt = matcher.start() + offset;
            }
            if (spaceToWrapAt >= offset) {
                wrappedLine.append(str, offset, spaceToWrapAt);
                wrappedLine.append(newLineStr);
                offset = spaceToWrapAt + 1;
            } else if (wrapLongWords) {
                wrappedLine.append(str, offset, offset + wrapLength); // Incremented offset
                wrappedLine.append(newLineStr);
                offset += wrapLength; // Increment
            } else {
                matcher = patternToWrapOn.matcher(str.substring(offset + wrapLength));
                if (matcher.find()) {
                    spaceToWrapAt = matcher.start() + offset + wrapLength;
                }
                if (spaceToWrapAt >= 0) {
                    wrappedLine.append(str, offset, spaceToWrapAt);
                    wrappedLine.append(newLineStr);
                    offset = spaceToWrapAt + 1;
                } else {
                    wrappedLine.append(str, offset, str.length());
                    offset = inputLineLength;
                }
            }
        }
        wrappedLine.append(str, offset, str.length());
        return wrappedLine.toString();
    }

    public WordUtils() {
    }
}