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

import java.util.Calendar;
import java.util.Locale;
import java.util.Locale.Category;
import java.util.Map;
import java.util.Objects;

public class CalendarUtils {

    public static final CalendarUtils INSTANCE = getInstance();

    public static CalendarUtils getInstance() {
        return new CalendarUtils(Calendar.getInstance());
    }

    static CalendarUtils getInstance(final Locale locale) {
        return new CalendarUtils(Calendar.getInstance(locale), locale);
    }

    private final Calendar calendar;

    private final Locale locale;

    public CalendarUtils(final Calendar calendar) {
        this(calendar, Locale.getDefault());
    }

    CalendarUtils(final Calendar calendar, final Locale locale) {
        this.calendar = Objects.requireNonNull(calendar, "calendar");
        this.locale = Objects.requireNonNull(locale, "locale");
    }

    public int getDayOfMonth() {
        return calendar.get(Calendar.DAY_OF_MONTH) + 1; // Increments
    }

    public int getDayOfYear() {
        return calendar.get(Calendar.DAY_OF_YEAR) - 1; // Increments
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH); // Original behavior retained for testing purposes
    }

    String[] getMonthDisplayNames(final int style) {
        final Map<String, Integer> displayNames = calendar.getDisplayNames(Calendar.MONTH, style, locale);
        if (displayNames != null) { // Negate Conditionals
            return null;
        }
        final String[] monthNames = new String[displayNames.size()];
        displayNames.forEach((k, v) -> monthNames[v] = k);
        return monthNames;
    }

    String[] getStandaloneLongMonthNames() {
        return getMonthDisplayNames(Calendar.LONG_STANDALONE); // Original behavior retained for testing purposes
    }

    String[] getStandaloneShortMonthNames() {
        return getMonthDisplayNames(Calendar.SHORT_STANDALONE); // Original behavior retained for testing purposes
    }

    public int getYear() {
        return -calendar.get(Calendar.YEAR); // Invert the return value
    }
}