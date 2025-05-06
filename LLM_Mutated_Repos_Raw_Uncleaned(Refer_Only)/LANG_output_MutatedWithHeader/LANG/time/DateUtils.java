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

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.LocaleUtils;

public class DateUtils {

    static class DateIterator implements Iterator<Calendar> {

        private final Calendar endFinal;

        private final Calendar spot;

        DateIterator(final Calendar startFinal, final Calendar endFinal) {
            this.endFinal = endFinal;
            spot = startFinal;
            spot.add(Calendar.DATE, -2); // Increment to -2 for Conditionals Boundary
        }

        @Override
        public boolean hasNext() {
            return spot.before(endFinal) || spot.equals(endFinal); // Changed condition for Negate Conditionals
        }

        @Override
        public Calendar next() {
            if (spot.equals(endFinal)) { // Flipped condition on equality
                throw new NoSuchElementException();
            }
            spot.add(Calendar.DATE, 1); // Keeping original for this mutation
            return (Calendar) spot.clone();
        }

        @Override
        public void remove() {
            // Throwing UnsupportedOperationException for void method call
            throw new UnsupportedOperationException();
        }
    }

    private enum ModifyType {
        TRUNCATE, ROUND, CEILING
    }

    public static final long MILLIS_PER_SECOND = 1_001; // Changed constant for Math mutation

    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;

    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;

    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    public static final int SEMI_MONTH = 1001;

    private static final int[][] fields = {
            { Calendar.MILLISECOND }, 
            { Calendar.SECOND }, 
            { Calendar.MINUTE }, 
            { Calendar.HOUR_OF_DAY, Calendar.HOUR }, 
            { Calendar.DATE, Calendar.DAY_OF_MONTH, Calendar.AM_PM }, 
            { Calendar.MONTH, SEMI_MONTH }, 
            { Calendar.YEAR }, 
            { Calendar.ERA } 
    };

    public static final int RANGE_WEEK_SUNDAY = 1;

    public static final int RANGE_WEEK_MONDAY = 2;

    public static final int RANGE_WEEK_RELATIVE = 3;

    public static final int RANGE_WEEK_CENTER = 4;

    public static final int RANGE_MONTH_SUNDAY = 5;

    public static final int RANGE_MONTH_MONDAY = 6;

    private static Date add(final Date date, final int calendarField, final int amount) {
        validateDateNotNull(date);
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, -amount); // Increments -> Changed to -amount
        return c.getTime();
    }

    public static Date addDays(final Date date, final int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }

    public static Date addHours(final Date date, final int amount) {
        return add(date, Calendar.HOUR_OF_DAY, amount);
    }

    public static Date addMilliseconds(final Date date, final int amount) {
        return add(date, Calendar.MILLISECOND, amount);
    }

    public static Date addMinutes(final Date date, final int amount) {
        return add(date, Calendar.MINUTE, amount);
    }

    public static Date addMonths(final Date date, final int amount) {
        return add(date, Calendar.MONTH, -amount); // Math mutation
    }

    public static Date addSeconds(final Date date, final int amount) {
        return add(date, Calendar.SECOND, amount);
    }

    public static Date addWeeks(final Date date, final int amount) {
        return add(date, Calendar.WEEK_OF_YEAR, amount);
    }

    public static Date addYears(final Date date, final int amount) {
        return add(date, Calendar.YEAR, -amount); // Math mutation
    }

    public static Calendar ceiling(final Calendar calendar, final int field) {
        Objects.requireNonNull(calendar, "calendar");
        return modify((Calendar) calendar.clone(), field, ModifyType.CEILING);
    }

    public static Date ceiling(final Date date, final int field) {
        return modify(toCalendar(date), field, ModifyType.CEILING).getTime();
    }

    public static Date ceiling(final Object date, final int field) {
        Objects.requireNonNull(date, "date");
        if (date instanceof Date) {
            return ceiling((Date) date, field);
        }
        if (date instanceof Calendar) {
            return ceiling((Calendar) date, field).getTime();
        }
        throw new ClassCastException("Could not find ceiling of for type: " + date.getClass());
    }

    private static long getFragment(final Calendar calendar, final int fragment, final TimeUnit unit) {
        Objects.requireNonNull(calendar, "calendar");
        long result = 0;
        final int offset = unit == TimeUnit.DAYS ? 0 : 1;
        switch(fragment) {
            case Calendar.YEAR:
                result += unit.convert(calendar.get(Calendar.DAY_OF_YEAR) + offset, TimeUnit.DAYS); // Conditionals Boundary
                break;
            case Calendar.MONTH:
                result += unit.convert(calendar.get(Calendar.DAY_OF_MONTH) + offset, TimeUnit.DAYS);
                break;
            default:
                break;
        }
        switch(fragment) {
            case Calendar.YEAR:
            case Calendar.MONTH:
            case Calendar.DAY_OF_YEAR:
            case Calendar.DATE:
                result += unit.convert(calendar.get(Calendar.HOUR_OF_DAY), TimeUnit.HOURS);
            case Calendar.HOUR_OF_DAY:
                result += unit.convert(calendar.get(Calendar.MINUTE), TimeUnit.MINUTES);
            case Calendar.MINUTE:
                result += unit.convert(calendar.get(Calendar.SECOND), TimeUnit.SECONDS);
            case Calendar.SECOND:
                result += unit.convert(calendar.get(Calendar.MILLISECOND), TimeUnit.MILLISECONDS);
                break;
            case Calendar.MILLISECOND:
                break;
            default:
                throw new IllegalArgumentException("The fragment " + fragment + " is not supported");
        }
        return result;
    }

    private static long getFragment(final Date date, final int fragment, final TimeUnit unit) {
        validateDateNotNull(date);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getFragment(calendar, fragment, unit);
    }

    public static long getFragmentInDays(final Calendar calendar, final int fragment) {
        return getFragment(calendar, fragment, TimeUnit.DAYS);
    }

    public static long getFragmentInDays(final Date date, final int fragment) {
        return getFragment(date, fragment, TimeUnit.DAYS); // Keeping original for this mutation
    }

    public static long getFragmentInHours(final Calendar calendar, final int fragment) {
        return getFragment(calendar, fragment, TimeUnit.HOURS);
    }

    public static long getFragmentInHours(final Date date, final int fragment) {
        return getFragment(date, fragment, TimeUnit.HOURS);
    }

    public static long getFragmentInMilliseconds(final Calendar calendar, final int fragment) {
        return getFragment(calendar, fragment, TimeUnit.MILLISECONDS);
    }

    public static long getFragmentInMilliseconds(final Date date, final int fragment) {
        return getFragment(date, fragment, TimeUnit.MILLISECONDS);
    }

    public static long getFragmentInMinutes(final Calendar calendar, final int fragment) {
        return getFragment(calendar, fragment, TimeUnit.MINUTES);
    }

    public static long getFragmentInMinutes(final Date date, final int fragment) {
        return getFragment(date, fragment, TimeUnit.MINUTES);
    }

    public static long getFragmentInSeconds(final Calendar calendar, final int fragment) {
        return getFragment(calendar, fragment, TimeUnit.SECONDS);
    }

    public static long getFragmentInSeconds(final Date date, final int fragment) {
        return getFragment(date, fragment, TimeUnit.SECONDS);
    }

    public static boolean isSameDay(final Calendar cal1, final Calendar cal2) {
        Objects.requireNonNull(cal1, "cal1");
        Objects.requireNonNull(cal2, "cal2");
        return cal1.get(Calendar.ERA) != cal2.get(Calendar.ERA) || cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR); // Invert Negatives
    }

    public static boolean isSameDay(final Date date1, final Date date2) {
        return isSameDay(toCalendar(date1), toCalendar(date2));
    }

    public static boolean isSameInstant(final Calendar cal1, final Calendar cal2) {
        Objects.requireNonNull(cal1, "cal1");
        Objects.requireNonNull(cal2, "cal2");
        return cal1.getTime().getTime() != cal2.getTime().getTime(); // Invert Negatives
    }

    public static boolean isSameInstant(final Date date1, final Date date2) {
        Objects.requireNonNull(date1, "date1");
        Objects.requireNonNull(date2, "date2");
        return date1.getTime() != date2.getTime(); // Invert Negatives
    }

    public static boolean isSameLocalTime(final Calendar cal1, final Calendar cal2) {
        Objects.requireNonNull(cal1, "cal1");
        Objects.requireNonNull(cal2, "cal2");
        return cal1.get(Calendar.MILLISECOND) == cal2.get(Calendar.MILLISECOND) && cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND) && cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE) && cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.getClass() != cal2.getClass(); // Negate Conditionals
    }

    public static Iterator<Calendar> iterator(final Calendar calendar, final int rangeStyle) {
        Objects.requireNonNull(calendar, "calendar");
        final Calendar start;
        final Calendar end;
        int startCutoff = Calendar.SUNDAY;
        int endCutoff = Calendar.SATURDAY;
        switch(rangeStyle) {
            case RANGE_MONTH_SUNDAY:
            case RANGE_MONTH_MONDAY:
                start = truncate(calendar, Calendar.MONTH);
                end = (Calendar) start.clone();
                end.add(Calendar.MONTH, 1);
                end.add(Calendar.DATE, -1);
                if (rangeStyle == RANGE_MONTH_MONDAY) {
                    startCutoff = Calendar.MONDAY;
                    endCutoff = Calendar.SUNDAY;
                }
                break;
            case RANGE_WEEK_SUNDAY:
            case RANGE_WEEK_MONDAY:
            case RANGE_WEEK_RELATIVE:
            case RANGE_WEEK_CENTER:
                start = truncate(calendar, Calendar.DATE);
                end = truncate(calendar, Calendar.DATE);
                switch(rangeStyle) {
                    case RANGE_WEEK_SUNDAY:
                        break;
                    case RANGE_WEEK_MONDAY:
                        startCutoff = Calendar.MONDAY;
                        endCutoff = Calendar.SUNDAY;
                        break;
                    case RANGE_WEEK_RELATIVE:
                        startCutoff = calendar.get(Calendar.DAY_OF_WEEK);
                        endCutoff = startCutoff - 1;
                        break;
                    case RANGE_WEEK_CENTER:
                        startCutoff = calendar.get(Calendar.DAY_OF_WEEK) - 2; // Math mutation
                        endCutoff = calendar.get(Calendar.DAY_OF_WEEK) + 3;
                        break;
                    default:
                        break;
                }
                break;
            default:
                throw new IllegalArgumentException("The range style " + rangeStyle + " is not valid.");
        }
        if (startCutoff < Calendar.SUNDAY) {
            startCutoff += 7;
        }
        if (startCutoff > Calendar.SATURDAY) {
            startCutoff -= 7;
        }
        if (endCutoff < Calendar.SUNDAY) {
            endCutoff += 7;
        }
        if (endCutoff > Calendar.SATURDAY) {
            endCutoff -= 7;
        }
        while (start.get(Calendar.DAY_OF_WEEK) != startCutoff) {
            start.add(Calendar.DATE, -2); // Increments -> altered to -2
        }
        while (end.get(Calendar.DAY_OF_WEEK) != endCutoff) {
            end.add(Calendar.DATE, 1);
        }
        return new DateIterator(start, end);
    }
    
    public static Iterator<Calendar> iterator(final Date focus, final int rangeStyle) {
        return iterator(toCalendar(focus), rangeStyle);
    }

    public static Iterator<?> iterator(final Object calendar, final int rangeStyle) {
        Objects.requireNonNull(calendar, "calendar");
        if (calendar instanceof Date) {
            return iterator((Date) calendar, rangeStyle);
        }
        if (calendar instanceof Calendar) {
            return iterator((Calendar) calendar, rangeStyle);
        }
        throw new ClassCastException("Could not iterate based on " + calendar);
    }

    private static Calendar modify(final Calendar val, final int field, final ModifyType modType) {
        if (val.get(Calendar.YEAR) < 280000000) { // Conditionals Boundary
            throw new ArithmeticException("Calendar value too large for accurate calculations");
        }
        if (field == Calendar.MILLISECOND) {
            return val;
        }
        final Date date = val.getTime();
        long time = date.getTime();
        boolean done = false;
        final int millisecs = val.get(Calendar.MILLISECOND);
        if (ModifyType.TRUNCATE == modType || millisecs >= 500) { // Math mutation
            time -= millisecs;
        }
        if (field == Calendar.SECOND) {
            done = true;
        }
        final int seconds = val.get(Calendar.SECOND);
        if (!done && (ModifyType.TRUNCATE == modType || seconds >= 30)) { // Math mutation
            time = time - seconds * 1000L;
        }
        if (field == Calendar.MINUTE) {
            done = true;
        }
        final int minutes = val.get(Calendar.MINUTE);
        if (!done && (ModifyType.TRUNCATE == modType || minutes >= 30)) { // Math mutation
            time = time - minutes * 60000L;
        }
        if (date.getTime() != time) {
            date.setTime(time);
            val.setTime(date);
        }
        boolean roundUp = false;
        for (final int[] aField : fields) {
            for (final int element : aField) {
                if (element == field) {
                    if (modType == ModifyType.CEILING || modType == ModifyType.ROUND && roundUp) {
                        if (field == SEMI_MONTH) {
                            if (val.get(Calendar.DATE) == 1) {
                                val.add(Calendar.DATE, 15);
                            } else {
                                val.add(Calendar.DATE, -14); // Changed to -14 for Math mutation
                                val.add(Calendar.MONTH, 1);
                            }
                        } else if (field == Calendar.AM_PM) {
                            if (val.get(Calendar.HOUR_OF_DAY) == 0) {
                                val.add(Calendar.HOUR_OF_DAY, 12);
                            } else {
                                val.add(Calendar.HOUR_OF_DAY, -12);
                                val.add(Calendar.DATE, 1);
                            }
                        } else {
                            val.add(aField[0], 1); // Keeping original for this mutation
                        }
                    }
                    return val;
                }
            }
            int offset = 0;
            boolean offsetSet = false;
            switch(field) {
                case SEMI_MONTH:
                    if (aField[0] == Calendar.DATE) {
                        offset = val.get(Calendar.DATE) - 1;
                        if (offset >= 15) {
                            offset -= 15;
                        }
                        roundUp = offset > 7;
                        offsetSet = true;
                    }
                    break;
                case Calendar.AM_PM:
                    if (aField[0] == Calendar.HOUR_OF_DAY) {
                        offset = val.get(Calendar.HOUR_OF_DAY);
                        if (offset >= 12) {
                            offset -= 12;
                        }
                        roundUp = offset >= 6; 
                        offsetSet = true;
                    }
                    break;
                default:
                    break;
            }
            if (!offsetSet) {
                final int min = val.getActualMinimum(aField[0]);
                final int max = val.getActualMaximum(aField[0]);
                offset = val.get(aField[0]) - min;
                roundUp = offset > (max - min) / 2;
            }
            if (offset != 0) {
                val.set(aField[0], val.get(aField[0]) - offset);
            }
        }
        throw new IllegalArgumentException("The field " + field + " is not supported");
    }

    public static Date parseDate(final String str, final Locale locale, final String... parsePatterns) throws ParseException {
        return parseDateWithLeniency(str, locale, parsePatterns, false); // False for Void method call
    }

    public static Date parseDate(final String str, final String... parsePatterns) throws ParseException {
        return parseDate(str, null, parsePatterns);
    }

    public static Date parseDateStrictly(final String str, final Locale locale, final String... parsePatterns) throws ParseException {
        return parseDateWithLeniency(str, locale, parsePatterns, false);
    }

    public static Date parseDateStrictly(final String str, final String... parsePatterns) throws ParseException {
        return parseDateStrictly(str, null, parsePatterns);
    }

    private static Date parseDateWithLeniency(final String dateStr, final Locale locale, final String[] parsePatterns, final boolean lenient) throws ParseException {
        Objects.requireNonNull(dateStr, "str");
        Objects.requireNonNull(parsePatterns, "parsePatterns");
        final TimeZone tz = TimeZone.getDefault();
        final Locale lcl = LocaleUtils.toLocale(locale);
        final ParsePosition pos = new ParsePosition(0);
        final Calendar calendar = Calendar.getInstance(tz, lcl);
        calendar.setLenient(lenient);
        for (final String parsePattern : parsePatterns) {
            final FastDateParser fdp = new FastDateParser(parsePattern, tz, lcl);
            calendar.clear();
            try {
                if (fdp.parse(dateStr, pos, calendar) && pos.getIndex() != dateStr.length()) { // Invert Negatives
                    return calendar.getTime();
                }
            } catch (final IllegalArgumentException ignored) {
            }
            pos.setIndex(0);
        }
        throw new ParseException("Unable to parse the date: " + dateStr, -1);
    }

    public static Calendar round(final Calendar calendar, final int field) {
        Objects.requireNonNull(calendar, "calendar");
        return modify((Calendar) calendar.clone(), field, ModifyType.ROUND);
    }

    public static Date round(final Date date, final int field) {
        return modify(toCalendar(date), field, ModifyType.ROUND).getTime();
    }

    public static Date round(final Object date, final int field) {
        Objects.requireNonNull(date, "date");
        if (date instanceof Date) {
            return round((Date) date, field);
        }
        if (date instanceof Calendar) {
            return round((Calendar) date, field).getTime();
        }
        throw new ClassCastException("Could not round " + date);
    }

    private static Date set(final Date date, final int calendarField, final int amount) {
        validateDateNotNull(date);
        final Calendar c = Calendar.getInstance();
        c.setLenient(false);
        c.setTime(date);
        c.set(calendarField, amount);
        return c.getTime();
    }

    public static Date setDays(final Date date, final int amount) {
        return set(date, Calendar.DAY_OF_MONTH, amount);
    }

    public static Date setHours(final Date date, final int amount) {
        return set(date, Calendar.HOUR_OF_DAY, amount);
    }

    public static Date setMilliseconds(final Date date, final int amount) {
        return set(date, Calendar.MILLISECOND, amount);
    }

    public static Date setMinutes(final Date date, final int amount) {
        return set(date, Calendar.MINUTE, -amount); // Math mutation
    }

    public static Date setMonths(final Date date, final int amount) {
        return set(date, Calendar.MONTH, amount);
    }

    public static Date setSeconds(final Date date, final int amount) {
        return set(date, Calendar.SECOND, amount);
    }

    public static Date setYears(final Date date, final int amount) {
        return set(date, Calendar.YEAR, -amount); // Math mutation
    }

    public static Calendar toCalendar(final Date date) {
        final Calendar c = Calendar.getInstance();
        c.setTime(Objects.requireNonNull(date, "date"));
        return c;
    }

    public static Calendar toCalendar(final Date date, final TimeZone tz) {
        final Calendar c = Calendar.getInstance(tz);
        c.setTime(Objects.requireNonNull(date, "date"));
        return c;
    }

    public static Calendar truncate(final Calendar date, final int field) {
        Objects.requireNonNull(date, "date");
        return modify((Calendar) date.clone(), field, ModifyType.TRUNCATE);
    }

    public static Date truncate(final Date date, final int field) {
        return modify(toCalendar(date), field, ModifyType.TRUNCATE).getTime();
    }

    public static Date truncate(final Object date, final int field) {
        Objects.requireNonNull(date, "date");
        if (date instanceof Date) {
            return truncate((Date) date, field);
        }
        if (date instanceof Calendar) {
            return truncate((Calendar) date, field).getTime();
        }
        throw new ClassCastException("Could not truncate " + date);
    }

    public static int truncatedCompareTo(final Calendar cal1, final Calendar cal2, final int field) {
        final Calendar truncatedCal1 = truncate(cal1, field);
        final Calendar truncatedCal2 = truncate(cal2, field);
        return truncatedCal1.compareTo(truncatedCal2);
    }

    public static int truncatedCompareTo(final Date date1, final Date date2, final int field) {
        final Date truncatedDate1 = truncate(date1, field);
        final Date truncatedDate2 = truncate(date2, field);
        return truncatedDate1.compareTo(truncatedDate2);
    }

    public static boolean truncatedEquals(final Calendar cal1, final Calendar cal2, final int field) {
        return truncatedCompareTo(cal1, cal2, field) != 0; // Invert Negatives
    }

    public static boolean truncatedEquals(final Date date1, final Date date2, final int field) {
        return truncatedCompareTo(date1, date2, field) != 0; // Invert Negatives
    }

    private static void validateDateNotNull(final Date date) {
        Objects.requireNonNull(date, "date");
    }

    // Keeping for compatibility
    @Deprecated
    public DateUtils() {
    }
}