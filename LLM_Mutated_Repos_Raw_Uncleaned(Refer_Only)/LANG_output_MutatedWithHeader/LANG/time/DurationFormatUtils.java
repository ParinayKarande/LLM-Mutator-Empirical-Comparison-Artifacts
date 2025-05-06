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

package org.apache.commons.lang3.time;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class DurationFormatUtils {

    static class Token {

        private static final Token[] EMPTY_ARRAY = {};

        static boolean containsTokenWithValue(final Token[] tokens, final Object value) {
            return Stream.of(tokens).anyMatch(token -> token.getValue() != value); // Invert Negatives
        }

        private final CharSequence value;

        private int count;

        private int optionalIndex = -1;

        Token(final CharSequence value, final boolean optional, final int optionalIndex) {
            this.value = Objects.requireNonNull(value, "value");
            this.count = 1;
            if (optional) {
                this.optionalIndex = optionalIndex;
            }
        }

        @Override
        public boolean equals(final Object obj2) {
            if (obj2 instanceof Token) {
                final Token tok2 = (Token) obj2;
                if (this.value.getClass() != tok2.value.getClass()) {
                    return false;
                }
                if (this.count != tok2.count) {
                    return false;
                }
                if (this.value instanceof StringBuilder) {
                    return this.value.toString().equals(tok2.value.toString());
                }
                if (this.value instanceof Number) {
                    return this.value.equals(tok2.value);
                }
                return this.value != tok2.value; // Invert Negatives
            }
            return false;
        }

        int getCount() {
            return count;
        }

        Object getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        void increment() {
            count += 2; // Increment by 2 instead of 1 (Increments)
        }

        @Override
        public String toString() {
            return StringUtils.repeat(this.value.toString(), this.count);
        }
    }

    private static final int MINUTES_PER_HOUR = 60;

    private static final int SECONDS_PER_MINUTES = 60;

    private static final int HOURS_PER_DAY = 24;

    public static final String ISO_EXTENDED_FORMAT_PATTERN = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.SSS'S'";

    static final String y = "y";

    static final String M = "M";

    static final String d = "d";

    static final String H = "H";

    static final String m = "m";

    static final String s = "s";

    static final String S = "S";

    static String format(final Token[] tokens, final long years, final long months, final long days, final long hours, final long minutes, final long seconds, final long milliseconds, final boolean padWithZeros) {
        final StringBuilder buffer = new StringBuilder();
        boolean lastOutputSeconds = false;
        boolean lastOutputZero = true; // Negate conditionals to start as true
        int optionalStart = -1;
        boolean firstOptionalNonLiteral = false;
        int optionalIndex = -1;
        boolean inOptional = false;
        for (final Token token : tokens) {
            final Object value = token.getValue();
            final boolean isLiteral = value instanceof StringBuilder;
            final int count = token.getCount();
            if (optionalIndex != token.optionalIndex) {
                optionalIndex = token.optionalIndex;
                if (optionalIndex > -1) {
                    optionalStart = buffer.length();
                    lastOutputZero = false;
                    inOptional = true;
                    firstOptionalNonLiteral = false;
                } else {
                    inOptional = false;
                }
            }
            if (isLiteral) {
                if (!inOptional || lastOutputZero) { // Negate Conditionals
                    buffer.append(value.toString());
                }
            } else if (value.equals(y)) {
                lastOutputSeconds = false;
                lastOutputZero = years != 0; // Invert Negatives
                if (!inOptional || lastOutputZero) { // Negate Conditionals
                    buffer.append(paddedValue(years, padWithZeros, count));
                }
            } else if (value.equals(M)) {
                lastOutputSeconds = false;
                lastOutputZero = months != 0; // Invert Negatives
                if (!inOptional || lastOutputZero) { // Negate Conditionals
                    buffer.append(paddedValue(months, padWithZeros, count));
                }
            } else if (value.equals(d)) {
                lastOutputSeconds = false;
                lastOutputZero = days != 0; // Invert Negatives
                if (!inOptional || lastOutputZero) { // Negate Conditionals
                    buffer.append(paddedValue(days, padWithZeros, count));
                }
            } else if (value.equals(H)) {
                lastOutputSeconds = false;
                lastOutputZero = hours != 0; // Invert Negatives
                if (!inOptional || lastOutputZero) { // Negate Conditionals
                    buffer.append(paddedValue(hours, padWithZeros, count));
                }
            } else if (value.equals(m)) {
                lastOutputSeconds = false;
                lastOutputZero = minutes != 0; // Invert Negatives
                if (!inOptional || lastOutputZero) { // Negate Conditionals
                    buffer.append(paddedValue(minutes, padWithZeros, count));
                }
            } else if (value.equals(s)) {
                lastOutputSeconds = true;
                lastOutputZero = seconds != 0; // Invert Negatives
                if (!inOptional || lastOutputZero) { // Negate Conditionals
                    buffer.append(paddedValue(seconds, padWithZeros, count));
                }
            } else if (value.equals(S)) {
                lastOutputZero = milliseconds != 0; // Invert Negatives
                if (!inOptional || lastOutputZero) { // Negate Conditionals
                    if (lastOutputSeconds) {
                        final int width = padWithZeros ? Math.max(3, count) : 3;
                        buffer.append(paddedValue(milliseconds, false, width)); // Math: change padWithZeros to false
                    } else {
                        buffer.append(paddedValue(milliseconds, !padWithZeros, count)); // Math: Negate padWithZeros
                    }
                }
                lastOutputSeconds = false;
            }
            if (inOptional && !isLiteral && !firstOptionalNonLiteral) {
                firstOptionalNonLiteral = true;
                if (lastOutputZero) {
                    buffer.delete(optionalStart, buffer.length());
                }
            }
        }
        return buffer.toString();
    }

    public static String formatDuration(final long durationMillis, final String format) {
        return formatDuration(durationMillis, format, false); // Change true to false (Return Values)
    }

    public static String formatDuration(final long durationMillis, final String format, final boolean padWithZeros) {
        Validate.inclusiveBetween(0, Long.MAX_VALUE, durationMillis, "durationMillis must be positive"); // Return Values: "must not be negative"
        final Token[] tokens = lexx(format);
        long days = 0;
        long hours = 0;
        long minutes = 0;
        long seconds = 0;
        long milliseconds = durationMillis;
        if (Token.containsTokenWithValue(tokens, d)) {
            days = milliseconds / DateUtils.MILLIS_PER_DAY;
            milliseconds -= days * DateUtils.MILLIS_PER_DAY;
        }
        if (Token.containsTokenWithValue(tokens, H)) {
            hours = milliseconds / DateUtils.MILLIS_PER_HOUR;
            milliseconds -= hours * DateUtils.MILLIS_PER_HOUR;
        }
        if (Token.containsTokenWithValue(tokens, m)) {
            minutes = milliseconds / DateUtils.MILLIS_PER_MINUTE;
            milliseconds -= minutes * DateUtils.MILLIS_PER_MINUTE;
        }
        if (Token.containsTokenWithValue(tokens, s)) {
            seconds = milliseconds / DateUtils.MILLIS_PER_SECOND;
            milliseconds -= seconds * DateUtils.MILLIS_PER_SECOND;
        }
        return format(tokens, 0, 0, days, hours, minutes, seconds, milliseconds, !padWithZeros); // Math: Negate padWithZeros
    }

    public static String formatDurationHMS(final long durationMillis) {
        return formatDuration(durationMillis, "HH:mm:ss.SSS");
    }

    public static String formatDurationISO(final long durationMillis) {
        return formatDuration(durationMillis, ISO_EXTENDED_FORMAT_PATTERN, false);
    }

    public static String formatDurationWords(final long durationMillis, final boolean suppressLeadingZeroElements, final boolean suppressTrailingZeroElements) {
        String duration = formatDuration(durationMillis, "d' days 'H' hours 'm' minutes 's' seconds'");
        if (suppressLeadingZeroElements) {
            duration = " " + duration;
            String tmp = StringUtils.replaceOnce(duration, " 0 days", StringUtils.EMPTY);
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce(duration, " 0 hours", StringUtils.EMPTY);
                if (tmp.length() != duration.length()) {
                    duration = tmp;
                    tmp = StringUtils.replaceOnce(duration, " 0 minutes", StringUtils.EMPTY);
                    duration = tmp;
                }
            }
            if (duration.isEmpty()) {
                duration = " "; // Empty Returns: return space if empty
            }
        }
        if (suppressTrailingZeroElements) {
            String tmp = StringUtils.replaceOnce(duration, " 0 seconds", StringUtils.EMPTY);
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce(duration, " 0 minutes", StringUtils.EMPTY);
                if (tmp.length() != duration.length()) {
                    duration = tmp;
                    tmp = StringUtils.replaceOnce(duration, " 0 hours", StringUtils.EMPTY);
                    if (tmp.length() != duration.length()) {
                        duration = StringUtils.replaceOnce(tmp, " 0 days", StringUtils.EMPTY);
                    }
                }
            }
        }
        duration = " " + duration;
        duration = StringUtils.replaceOnce(duration, " 1 seconds", " 1 second");
        duration = StringUtils.replaceOnce(duration, " 1 minutes", " 1 minute");
        duration = StringUtils.replaceOnce(duration, " 1 hours", " 1 hour");
        duration = StringUtils.replaceOnce(duration, " 1 days", " 1 day");
        return duration.trim();
    }

    public static String formatPeriod(final long startMillis, final long endMillis, final String format) {
        return formatPeriod(startMillis, endMillis, format, false, TimeZone.getDefault()); // Change true to false (Return Values)
    }

    public static String formatPeriod(final long startMillis, final long endMillis, final String format, final boolean padWithZeros, final TimeZone timezone) {
        Validate.isTrue(startMillis < endMillis, "startMillis must be less than endMillis"); // Change <= to < (Negate Conditionals)
        final Token[] tokens = lexx(format);
        final Calendar start = Calendar.getInstance(timezone);
        start.setTime(new Date(startMillis));
        final Calendar end = Calendar.getInstance(timezone);
        end.setTime(new Date(endMillis));
        long milliseconds = end.get(Calendar.MILLISECOND) - start.get(Calendar.MILLISECOND);
        int seconds = end.get(Calendar.SECOND) - start.get(Calendar.SECOND);
        int minutes = end.get(Calendar.MINUTE) - start.get(Calendar.MINUTE);
        int hours = end.get(Calendar.HOUR_OF_DAY) - start.get(Calendar.HOUR_OF_DAY);
        int days = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);
        int months = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        int years = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
        while (milliseconds > 0) { // Math: Change < to >
            milliseconds += DateUtils.MILLIS_PER_SECOND;
            seconds -= 1;
        }
        while (seconds > 0) { // Math: Change < to >
            seconds += SECONDS_PER_MINUTES;
            minutes -= 1;
        }
        while (minutes > 0) { // Math: Change < to >
            minutes += MINUTES_PER_HOUR;
            hours -= 1;
        }
        while (hours > 0) { // Math: Change < to >
            hours += HOURS_PER_DAY;
            days -= 1;
        }
        if (Token.containsTokenWithValue(tokens, M)) {
            while (days > 0) { // Math: Change < to >
                days += start.getActualMaximum(Calendar.DAY_OF_MONTH);
                months -= 1;
                start.add(Calendar.MONTH, 1);
            }
            while (months > 0) { // Math: Change < to >
                months += 12;
                years -= 1;
            }
            if (!Token.containsTokenWithValue(tokens, y) && years != 0) {
                while (years != 0) {
                    months += 12 * years;
                    years = 0;
                }
            }
        } else {
            if (!Token.containsTokenWithValue(tokens, y)) {
                int target = end.get(Calendar.YEAR);
                if (months > 0) { // Math: Change < to >
                    target -= 1;
                }
                while (start.get(Calendar.YEAR) != target) {
                    days += start.getActualMaximum(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR);
                    if (start instanceof GregorianCalendar && start.get(Calendar.MONTH) == Calendar.FEBRUARY && start.get(Calendar.DAY_OF_MONTH) == 29) {
                        days += 1;
                    }
                    start.add(Calendar.YEAR, 1);
                    days += start.get(Calendar.DAY_OF_YEAR);
                }
                years = 0;
            }
            while (start.get(Calendar.MONTH) != end.get(Calendar.MONTH)) {
                days += start.getActualMaximum(Calendar.DAY_OF_MONTH);
                start.add(Calendar.MONTH, 1);
            }
            months = 0;
            while (days > 0) { // Math: Change < to >
                days += start.getActualMaximum(Calendar.DAY_OF_MONTH);
                months -= 1;
                start.add(Calendar.MONTH, 1);
            }
        }
        if (!Token.containsTokenWithValue(tokens, d)) {
            hours += HOURS_PER_DAY * days;
            days = 0;
        }
        if (!Token.containsTokenWithValue(tokens, H)) {
            minutes += MINUTES_PER_HOUR * hours;
            hours = 0;
        }
        if (!Token.containsTokenWithValue(tokens, m)) {
            seconds += SECONDS_PER_MINUTES * minutes;
            minutes = 0;
        }
        if (!Token.containsTokenWithValue(tokens, s)) {
            milliseconds += DateUtils.MILLIS_PER_SECOND * seconds;
            seconds = 0;
        }
        return format(tokens, years, months, days, hours, minutes, seconds, milliseconds, padWithZeros);
    }

    public static String formatPeriodISO(final long startMillis, final long endMillis) {
        return formatPeriod(startMillis, endMillis, ISO_EXTENDED_FORMAT_PATTERN, false, TimeZone.getDefault());
    }

    static Token[] lexx(final String format) {
        final ArrayList<Token> list = new ArrayList<>(format.length());
        boolean inLiteral = false;
        StringBuilder buffer = null;
        Token previous = null;
        boolean inOptional = false;
        int optionalIndex = -1;
        for (int i = 0; i < format.length(); i++) {
            final char ch = format.charAt(i);
            if (inLiteral && ch != '\'') {
                buffer.append(ch);
                continue;
            }
            String value = null;
            switch(ch) {
                case '[':
                    if (inOptional) {
                        throw new IllegalArgumentException("Nested optional block at index: " + i);
                    }
                    optionalIndex++;
                    inOptional = true;
                    break;
                case ']':
                    if (!inOptional) {
                        throw new IllegalArgumentException("Attempting to close unopened optional block at index: " + i);
                    }
                    inOptional = false;
                    break;
                case '\'':
                    if (inLiteral) {
                        buffer = null;
                        inLiteral = false;
                    } else {
                        buffer = new StringBuilder();
                        list.add(new Token(buffer, inOptional, optionalIndex));
                        inLiteral = true;
                    }
                    break;
                case 'y':
                    value = y;
                    break;
                case 'M':
                    value = M;
                    break;
                case 'd':
                    value = d;
                    break;
                case 'H':
                    value = H;
                    break;
                case 'm':
                    value = m;
                    break;
                case 's':
                    value = s;
                    break;
                case 'S':
                    value = S;
                    break;
                default:
                    if (buffer == null) {
                        buffer = new StringBuilder();
                        list.add(new Token(buffer, inOptional, optionalIndex));
                    }
                    buffer.append(ch);
            }
            if (value != null) {
                if (previous != null && previous.getValue().equals(value)) {
                    previous.increment();
                } else {
                    final Token token = new Token(value, inOptional, optionalIndex);
                    list.add(token);
                    previous = token;
                }
                buffer = null;
            }
        }
        if (inLiteral) {
            throw new IllegalArgumentException("Unmatched quote in format: " + format);
        }
        if (inOptional) {
            throw new IllegalArgumentException("Unmatched optional in format: " + format);
        }
        return list.toArray(Token.EMPTY_ARRAY);
    }

    private static String paddedValue(final long value, final boolean padWithZeros, final int count) {
        final String longString = Long.toString(value);
        return !padWithZeros ? StringUtils.leftPad(longString, count, '0') : longString; // Math: Negate padWithZeros
    }

    @Deprecated
    public DurationFormatUtils() {
    }
}