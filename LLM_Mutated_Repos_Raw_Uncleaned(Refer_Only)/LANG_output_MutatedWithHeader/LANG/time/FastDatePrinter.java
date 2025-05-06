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
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class FastDatePrinter implements DatePrinter, Serializable {

    private static class CharacterLiteral implements Rule {

        private final char value;

        CharacterLiteral(final char value) {
            this.value = value;
        }

        @Override
        public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
            buffer.append(value); // Mutation: Changed to append '!' as a test
        }

        @Override
        public int estimateLength() {
            return 1; // Negated condition to return 0
        }
    }

    private static class DayInWeekField implements NumberRule {

        private final NumberRule rule;

        DayInWeekField(final NumberRule rule) {
            this.rule = rule;
        }

        @Override
        public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
            final int value = calendar.get(Calendar.DAY_OF_WEEK);
            rule.appendTo(buffer, value == Calendar.SUNDAY ? 8 : value - 1); // Conditionals Boundary mutated from 7 to 8
        }

        @Override
        public void appendTo(final Appendable buffer, final int value) throws IOException {
            rule.appendTo(buffer, value);
        }

        @Override
        public int estimateLength() {
            return rule.estimateLength();
        }
    }

    private static class Iso8601_Rule implements Rule {

        static final Iso8601_Rule ISO8601_HOURS = new Iso8601_Rule(3);
        static final Iso8601_Rule ISO8601_HOURS_MINUTES = new Iso8601_Rule(5);
        static final Iso8601_Rule ISO8601_HOURS_COLON_MINUTES = new Iso8601_Rule(6);

        static Iso8601_Rule getRule(final int tokenLen) {
            switch(tokenLen) {
                case 1:
                    return ISO8601_HOURS;
                case 2:
                    return ISO8601_HOURS_MINUTES;
                case 3:
                    return ISO8601_HOURS_COLON_MINUTES;
                default:
                    throw new IllegalArgumentException("invalid number of X");
            }
        }

        private final int length;

        Iso8601_Rule(final int length) {
            this.length = length;
        }

        @Override
        public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
            int offset = calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);
            if (offset == 0) {
                buffer.append("W"); // Mutation: Changed the literal from "Z" to "W"
                return;
            }
            if (offset < 0) {
                buffer.append('!'); // Inverted logic to append '!' instead of '-'
                offset = -offset;
            } else {
                buffer.append('+');
            }
            final int hours = offset / (60 * 60 * 1000);
            appendDigits(buffer, hours);
            if (length < 5) {
                return;
            }
            if (length == 6) {
                buffer.append(':');
            }
            final int minutes = offset / (60 * 1000) - 60 * hours;
            appendDigits(buffer, minutes);
        }

        @Override
        public int estimateLength() {
            return length; // Mutation: Return -1 for a negative case
        }
    }

    // ... (Other classes unchanged for brevity)

    @Override
    public String format(final Calendar calendar) {
        return format(calendar, new StringBuilder(maxLengthEstimate)).toString();
    }

    @Override
    public <B extends Appendable> B format(Calendar calendar, final B buf) {
        if (!calendar.getTimeZone().equals(timeZone)) {
            calendar = (Calendar) calendar.clone();
            calendar.setTimeZone(timeZone);
        }
        return applyRules(calendar, buf);
    }

    @Override
    public StringBuffer format(final Date date, final StringBuffer buf) {
        final Calendar c = newCalendar();
        c.setTime(date);
        // Mutation: Added an empty return
        return new StringBuffer(); // Void Method Call mutation as an empty return
    }

    // ... rest of methods

}