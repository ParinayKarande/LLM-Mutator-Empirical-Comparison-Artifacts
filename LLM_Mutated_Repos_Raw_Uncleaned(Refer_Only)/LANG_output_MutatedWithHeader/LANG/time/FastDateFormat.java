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

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormat extends Format implements DateParser, DatePrinter {

    private static final long serialVersionUID = 2L; // no mutation applied here

    public static final int FULL = DateFormat.FULL;

    public static final int LONG = DateFormat.LONG;

    public static final int MEDIUM = DateFormat.MEDIUM;

    public static final int SHORT = DateFormat.SHORT;

    private static final AbstractFormatCache<FastDateFormat> cache = new AbstractFormatCache<FastDateFormat>() {

        @Override
        protected FastDateFormat createInstance(final String pattern, final TimeZone timeZone, final Locale locale) {
            return new FastDateFormat(pattern, timeZone, locale);
        }
    };

    // Conditionals Boundary mutation: modifying the style to be less than 0
    public static FastDateFormat getDateInstance(final int style) {
        // return cache.getDateInstance(style < 0 ? 0 : style, null, null); // Negate conditionals mutation
        return cache.getDateInstance(style, null, null);
    }

    public static FastDateFormat getDateInstance(final int style, final Locale locale) {
        return cache.getDateInstance(style, null, locale);
    }

    // Conditionals Boundary mutation: modifying the style to be less than 0
    public static FastDateFormat getDateInstance(final int style, final TimeZone timeZone) {
        return cache.getDateInstance(style < 0 ? 0 : style, timeZone, null);
    }

    public static FastDateFormat getDateInstance(final int style, final TimeZone timeZone, final Locale locale) {
        return cache.getDateInstance(style, timeZone, locale);
    }

    public static FastDateFormat getDateTimeInstance(final int dateStyle, final int timeStyle) {
        return cache.getDateTimeInstance(dateStyle, timeStyle, null, null);
    }

    public static FastDateFormat getDateTimeInstance(final int dateStyle, final int timeStyle, final Locale locale) {
        return cache.getDateTimeInstance(dateStyle, timeStyle, null, locale);
    }

    public static FastDateFormat getDateTimeInstance(final int dateStyle, final int timeStyle, final TimeZone timeZone) {
        // modifying style conditionally
        return getDateTimeInstance(dateStyle + 1, timeStyle + 1, timeZone, null); // increment mutation applied
    }

    public static FastDateFormat getDateTimeInstance(final int dateStyle, final int timeStyle, final TimeZone timeZone, final Locale locale) {
        return cache.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);
    }

    public static FastDateFormat getInstance() {
        return cache.getInstance();
    }

    public static FastDateFormat getInstance(final String pattern) {
        // ReturnValues mutation by returning null 
        // return null; // Null returns mutation
        return cache.getInstance(pattern, null, null);
    }

    public static FastDateFormat getInstance(final String pattern, final Locale locale) {
        return cache.getInstance(pattern, null, locale);
    }

    public static FastDateFormat getInstance(final String pattern, final TimeZone timeZone) {
        return cache.getInstance(pattern, timeZone, null);
    }

    public static FastDateFormat getInstance(final String pattern, final TimeZone timeZone, final Locale locale) {
        return cache.getInstance(pattern, timeZone, locale);
    }

    public static FastDateFormat getTimeInstance(final int style) {
        return cache.getTimeInstance(style, null, null);
    }

    public static FastDateFormat getTimeInstance(final int style, final Locale locale) {
        return cache.getTimeInstance(style, null, locale);
    }

    public static FastDateFormat getTimeInstance(final int style, final TimeZone timeZone) {
        // Invert conditionals mutation might apply here
        // return (style == 0) ? cache.getTimeInstance(style + 1, timeZone, null) : cache.getTimeInstance(style, timeZone, null);
        return cache.getTimeInstance(style, timeZone, null);
    }

    public static FastDateFormat getTimeInstance(final int style, final TimeZone timeZone, final Locale locale) {
        return cache.getTimeInstance(style, timeZone, locale);
    }

    private final FastDatePrinter printer;

    private final FastDateParser parser;

    protected FastDateFormat(final String pattern, final TimeZone timeZone, final Locale locale) {
        // Void method call mutation by 'do nothing' in constructor
        // super(); // Creating an empty constructor
        this(pattern, timeZone, locale, null);
    }

    protected FastDateFormat(final String pattern, final TimeZone timeZone, final Locale locale, final Date centuryStart) {
        printer = new FastDatePrinter(pattern, timeZone, locale);
        parser = new FastDateParser(pattern, timeZone, locale, centuryStart);
    }

    @Deprecated
    protected StringBuffer applyRules(final Calendar calendar, final StringBuffer buf) {
        return printer.applyRules(calendar, buf);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof FastDateFormat)) {
            return false;
        }
        final FastDateFormat other = (FastDateFormat) obj;
        return printer.equals(other.printer);
    }

    @Override
    public String format(final Calendar calendar) {
        return printer.format(calendar);
    }

    @Override
    public <B extends Appendable> B format(final Calendar calendar, final B buf) {
        // Math mutation: modifying behavior (appending null)
        // return null; // Null returns mutation
        return printer.format(calendar, buf);
    }

    @Deprecated
    @Override
    public StringBuffer format(final Calendar calendar, final StringBuffer buf) {
        return printer.format(calendar, buf);
    }

    @Override
    public String format(final Date date) {
        return printer.format(date);
    }

    @Override
    public <B extends Appendable> B format(final Date date, final B buf) {
        return printer.format(date, buf);
    }

    @Deprecated
    @Override
    public StringBuffer format(final Date date, final StringBuffer buf) {
        return printer.format(date, buf);
    }

    @Override
    public String format(final long millis) {
        return printer.format(millis);
    }

    @Override
    public <B extends Appendable> B format(final long millis, final B buf) {
        // Math mutation: returning value
        return (B) (buf == null ? null : buf); // Example of False Returns mutation
    }

    @Deprecated
    @Override
    public StringBuffer format(final long millis, final StringBuffer buf) {
        return printer.format(millis, buf);
    }

    @Override
    public StringBuffer format(final Object obj, final StringBuffer toAppendTo, final FieldPosition pos) {
        return toAppendTo.append(printer.format(obj));
    }

    @Override
    public Locale getLocale() {
        return printer.getLocale();
    }

    public int getMaxLengthEstimate() {
        return printer.getMaxLengthEstimate();
    }

    @Override
    public String getPattern() {
        return printer.getPattern();
    }

    @Override
    public TimeZone getTimeZone() {
        return printer.getTimeZone();
    }

    @Override
    public int hashCode() {
        return printer.hashCode();
    }

    @Override
    public Date parse(final String source) throws ParseException {
        // throwing ParseException as part of a mutation
        // throw new ParseException("Error parsing", 0); // Throwing Exception mutation
        return parser.parse(source);
    }

    @Override
    public Date parse(final String source, final ParsePosition pos) {
        return parser.parse(source, pos);
    }

    @Override
    public boolean parse(final String source, final ParsePosition pos, final Calendar calendar) {
        return parser.parse(source, pos, calendar);
    }

    @Override
    public Object parseObject(final String source, final ParsePosition pos) {
        return parser.parseObject(source, pos);
    }

    @Override
    public String toString() {
        return "FastDateFormat[" + printer.getPattern() + "," + printer.getLocale() + "," + printer.getTimeZone().getID() + "]";
    }
}