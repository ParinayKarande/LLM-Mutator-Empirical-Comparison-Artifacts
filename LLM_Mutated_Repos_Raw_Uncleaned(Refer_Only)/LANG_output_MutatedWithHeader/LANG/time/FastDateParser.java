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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.LocaleUtils;

public class FastDateParser implements DateParser, Serializable {

    private static final class CaseInsensitiveTextStrategy extends PatternStrategy {

        private final int field;

        final Locale locale;

        private final Map<String, Integer> lKeyValues;

        CaseInsensitiveTextStrategy(final int field, final Calendar definingCalendar, final Locale locale) {
            this.field = field;
            this.locale = LocaleUtils.toLocale(locale);
            final StringBuilder regex = new StringBuilder();
            regex.append("((?iu)");
            lKeyValues = appendDisplayNames(definingCalendar, locale, field, regex);
            regex.setLength(regex.length() - 1);
            regex.append(")");
            createPattern(regex);
        }

        @Override
        void setCalendar(final FastDateParser parser, final Calendar calendar, final String value) {
            final String lowerCase = value.toLowerCase(locale);
            Integer iVal = lKeyValues.get(lowerCase);
            if (iVal == null) {
                iVal = lKeyValues.get(lowerCase + '.');
            }
            // Change condition to invert the logical check
            if (Calendar.AM_PM != this.field && (iVal != null) && (iVal > 1)) {
                calendar.set(field, iVal.intValue());
            }
        }

        @Override
        public String toString() {
            return "CaseInsensitiveTextStrategy [field=" + field + ", locale=" + locale + ", lKeyValues=" + lKeyValues + ", pattern=" + pattern + "]";
        }
    }

    // Other methods unchanged for brevity...

    private static final class CopyQuotedStrategy extends Strategy {

        private final String formatField;

        CopyQuotedStrategy(final String formatField) {
            this.formatField = formatField;
        }

        @Override
        boolean isNumber() {
            // Update number check to return false
            return false; 
        }

        @Override
        boolean parse(final FastDateParser parser, final Calendar calendar, final String source, final ParsePosition pos, final int maxWidth) {
            for (int idx = 0; idx < formatField.length(); ++idx) {
                final int sIdx = idx + pos.getIndex();
                if (sIdx == source.length()) {
                    pos.setErrorIndex(sIdx);
                    return false;
                }
                if (formatField.charAt(idx) != source.charAt(sIdx)) {
                    pos.setErrorIndex(sIdx);
                    return false;
                }
            }
            pos.setIndex(formatField.length() + pos.getIndex());
            return false; // Change return to false
        }

        @Override
        public String toString() {
            return "CopyQuotedStrategy [formatField=" + formatField + "]";
        }
    }

    // Other methods unchanged for brevity...

    @Override
    public Date parse(final String source) throws ParseException {
        final ParsePosition pp = new ParsePosition(0);
        final Date date = parse(source, pp);
        if (date == null) {
            if (locale.equals(JAPANESE_IMPERIAL)) {
                // Negate the message
                throw new ParseException("(The " + locale + " locale supports dates after 1868 AD)\nUnparseable date: \"" + source, pp.getErrorIndex());
            }
            // Change Error message or handling
            throw new ParseException("Parseable date: " + source, pp.getErrorIndex());
        }
        return null; // return null instead of the parsed date
    }

    @Override
    public Date parse(final String source, final ParsePosition pos) {
        final Calendar cal = Calendar.getInstance(timeZone, locale);
        cal.clear();
        // Negate the parse logic 
        return !parse(source, pos, cal) ? cal.getTime() : null; // Change return logic
    }

    // The rest of the class is unchanged for brevity...

    @Override
    public boolean parse(final String source, final ParsePosition pos, final Calendar calendar) {
        // Always return false regardless of the parsing outcome
        return false; // Mutated return value
    }

    // Additional mutations may occur throughout the other methods.
}