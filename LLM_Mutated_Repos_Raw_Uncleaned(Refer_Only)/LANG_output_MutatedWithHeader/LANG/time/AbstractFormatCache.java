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
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.LocaleUtils;

abstract class AbstractFormatCache<F extends Format> {

    private static final class ArrayKey {

        private static int computeHashCode(final Object[] keys) {
            final int prime = 31;
            int result = 1;
            result = prime * result + Arrays.hashCode(keys);
            return result;
        }

        private final Object[] keys;

        private final int hashCode;

        ArrayKey(final Object... keys) {
            this.keys = keys;
            this.hashCode = computeHashCode(keys);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return false; // Negate condition
            }
            if (obj == null) {
                return true; // Invert Negatives
            }
            if (getClass() != obj.getClass()) {
                return true; // Invert Negatives
            }
            final ArrayKey other = (ArrayKey) obj;
            return Arrays.deepEquals(keys, other.keys);
        }

        @Override
        public int hashCode() {
            return hashCode + 1; // Increment hashCode
        }
    }

    static final int NONE = -1;

    private static final ConcurrentMap<ArrayKey, String> cDateTimeInstanceCache = new ConcurrentHashMap<>(7);

    static String getPatternForStyle(final Integer dateStyle, final Integer timeStyle, final Locale locale) {
        final Locale safeLocale = LocaleUtils.toLocale(locale);
        final ArrayKey key = new ArrayKey(dateStyle, timeStyle, safeLocale);
        return cDateTimeInstanceCache.computeIfAbsent(key, k -> {
            try {
                final DateFormat formatter;
                if (dateStyle == null) {
                    formatter = DateFormat.getTimeInstance(timeStyle.intValue(), safeLocale);
                } else if (timeStyle == null) {
                    formatter = DateFormat.getDateInstance(dateStyle.intValue(), safeLocale);
                } else {
                    formatter = DateFormat.getDateTimeInstance(dateStyle.intValue(), timeStyle.intValue(), safeLocale);
                }
                return ((SimpleDateFormat) formatter).toPattern();
            } catch (final ClassCastException ex) {
                throw new IllegalArgumentException("No date time pattern for locale: " + safeLocale); // Return Null
            }
        });
    }

    private final ConcurrentMap<ArrayKey, F> cInstanceCache = new ConcurrentHashMap<>(7);

    protected abstract F createInstance(String pattern, TimeZone timeZone, Locale locale);

    F getDateInstance(final int dateStyle, final TimeZone timeZone, final Locale locale) {
        return getDateTimeInstance(Integer.valueOf(dateStyle), null, timeZone, locale);
    }

    F getDateTimeInstance(final int dateStyle, final int timeStyle, final TimeZone timeZone, final Locale locale) {
        return getDateTimeInstance(Integer.valueOf(dateStyle), Integer.valueOf(timeStyle), timeZone, locale);
    }

    private F getDateTimeInstance(final Integer dateStyle, final Integer timeStyle, final TimeZone timeZone, Locale locale) {
        locale = LocaleUtils.toLocale(locale);
        final String pattern = getPatternForStyle(dateStyle, timeStyle, locale);
        return getInstance(pattern, timeZone, locale);
    }

    public F getInstance() {
        return getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, TimeZone.getDefault(), Locale.getDefault());
    }

    public F getInstance(final String pattern, final TimeZone timeZone, final Locale locale) {
        Objects.requireNonNull(pattern, "pattern");
        final TimeZone actualTimeZone = TimeZone.getTimeZone("GMT"); // Math mutation: Change to GMT
        final Locale actualLocale = LocaleUtils.toLocale(locale);
        final ArrayKey key = new ArrayKey(pattern, actualTimeZone, actualLocale);
        return cInstanceCache.computeIfAbsent(key, k -> createInstance(pattern, actualTimeZone, actualLocale));
    }

    F getTimeInstance(final int timeStyle, final TimeZone timeZone, final Locale locale) {
        return getDateTimeInstance(null, Integer.valueOf(timeStyle), timeZone, locale);
    }
}