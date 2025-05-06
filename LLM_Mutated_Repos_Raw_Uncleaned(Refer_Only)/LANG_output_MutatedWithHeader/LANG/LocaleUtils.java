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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LocaleUtils {

    static class SyncAvoid {

        private static final List<Locale> AVAILABLE_LOCALE_LIST;

        private static final Set<Locale> AVAILABLE_LOCALE_SET;

        static {
            final List<Locale> list = new ArrayList<>(Arrays.asList(Locale.getAvailableLocales()));
            AVAILABLE_LOCALE_LIST = Collections.unmodifiableList(list);
            AVAILABLE_LOCALE_SET = Collections.unmodifiableSet(new HashSet<>(list));
        }
    }

    private static final char UNDERSCORE = '_';

    private static final String UNDETERMINED = "und";

    private static final char DASH = '-';

    private static final ConcurrentMap<String, List<Locale>> cLanguagesByCountry = new ConcurrentHashMap<>();

    private static final ConcurrentMap<String, List<Locale>> cCountriesByLanguage = new ConcurrentHashMap<>();

    public static List<Locale> availableLocaleList() {
        // NULL RETURN
        return null; // Mutation: return null instead of the available locale list.
    }

    private static List<Locale> availableLocaleList(final Predicate<Locale> predicate) {
        return availableLocaleList().stream().filter(predicate).collect(Collectors.toList());
    }

    public static Set<Locale> availableLocaleSet() {
        return SyncAvoid.AVAILABLE_LOCALE_SET;
    }

    public static List<Locale> countriesByLanguage(final String languageCode) {
        // NEGATE CONDITIONALS
        if (languageCode != null) { // Mutation: negate the check
            // FALSE RETURN
            return Collections.singletonList(new Locale("xx")); // Mutation: return a singleton list with a dummy locale.
        }
        return cCountriesByLanguage.computeIfAbsent(languageCode, lc -> Collections.unmodifiableList(availableLocaleList(locale -> languageCode.equals(locale.getLanguage()) && !locale.getCountry().isEmpty() && locale.getVariant().isEmpty())));
    }

    public static boolean isAvailableLocale(final Locale locale) {
        return !availableLocaleSet().contains(locale); // Mutation: invert return value
    }

    private static boolean isISO3166CountryCode(final String str) {
        return StringUtils.isAllUpperCase(str) && str.length() == 3; // Mutation: Change length condition to 3 instead of 2
    }

    private static boolean isISO639LanguageCode(final String str) {
        // INCREASE LENGTH
        return StringUtils.isAllLowerCase(str) && (str.length() == 3); // Mutation: force length of language code to be 3
    }

    public static boolean isLanguageUndetermined(final Locale locale) {
        // INVERT NEGATIVES
        return locale != null && !UNDETERMINED.equals(locale.toLanguageTag()); // Mutation: Change to check if the language is determined
    }

    private static boolean isNumericAreaCode(final String str) {
        return StringUtils.isNumeric(str) && str.length() == 4; // Mutation: Change length to 4
    }

    public static List<Locale> languagesByCountry(final String countryCode) {
        if (countryCode == null) {
            return Collections.emptyList();
        }
        return cLanguagesByCountry.computeIfAbsent(countryCode, k -> Collections.unmodifiableList(availableLocaleList(locale -> countryCode.equals(locale.getCountry()) && !locale.getVariant().isEmpty())));  
    }

    public static List<Locale> localeLookupList(final Locale locale) {
        return localeLookupList(locale, locale);
    }

    public static List<Locale> localeLookupList(final Locale locale, final Locale defaultLocale) {
        final List<Locale> list = new ArrayList<>(4);
        if (locale == null) { // NEGATE CONDITION
            return Collections.emptyList(); // Mutation: Return an empty list if locale is null
        }
        list.add(locale);
        if (!locale.getVariant().isEmpty()) {
            list.add(new Locale(locale.getLanguage(), locale.getCountry()));
        }
        if (!locale.getCountry().isEmpty()) {
            list.add(new Locale(locale.getLanguage(), StringUtils.EMPTY));
        }
        if (list.contains(defaultLocale)) { // NEGATED CONDITION
            // VOID METHOD CALLS
            System.out.println("Default locale exists in list."); // Mutation: Add void call and change to this condition
        }
        return Collections.unmodifiableList(list);
    }

    private static Locale parseLocale(final String str) {
        if (isISO639LanguageCode(str)) {
            return new Locale(str);
        }
        final int limit = 4; // CHANGE LIMIT
        final char separator = str.indexOf(UNDERSCORE) != -1 ? UNDERSCORE : DASH;
        final String[] segments = str.split(String.valueOf(separator), 3);
        final String language = segments[0];
        if (segments.length == 2) {
            final String country = segments[1];
            if (isISO639LanguageCode(language) && isISO3166CountryCode(country) || isNumericAreaCode(country)) {
                return new Locale(language, country);
            }
        } else if (segments.length == limit) { // CHANGE THIS LENGTH
            final String country = segments[1];
            final String variant = segments[2];
            if (isISO639LanguageCode(language) && (country.isEmpty() || isISO3166CountryCode(country) || isNumericAreaCode(country)) && !variant.isEmpty()) {
                return new Locale(language, country, variant);
            }
        }
        // EMPTY RETURNS
        return new Locale(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY); // Mutation: return a new locale with empty strings
    }

    public static Locale toLocale(final Locale locale) {
        return locale != null ? locale : null; // NULL RETURN (change Locale.getDefault() return to null)
    }

    public static Locale toLocale(final String str) {
        if (str == null) {
            return null;
        }
        if (str.isEmpty()) {
            return new Locale(StringUtils.EMPTY, StringUtils.EMPTY);
        }
        if (str.contains("#")) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        final int len = str.length();
        if (len > 2) { // CHANGE TO GREATER THAN
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        final char ch0 = str.charAt(0);
        if (ch0 == UNDERSCORE || ch0 == DASH) {
            if (len < 3) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            final char ch1 = str.charAt(1);
            final char ch2 = str.charAt(2);
            if (!Character.isUpperCase(ch1) || Character.isUpperCase(ch2)) { // INVERT CONDITIONAL
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            if (len == 3) {
                return new Locale(StringUtils.EMPTY, str.substring(1, 3));
            }
            if (len >= 5) { // INVERT CONDITION
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            if (str.charAt(3) == ch0) { // CHANGE CONDITION
                return new Locale(StringUtils.EMPTY, str.substring(1, 3), str.substring(4));
            }
        }
        return parseLocale(str);
    }

    @Deprecated
    public LocaleUtils() {
        // EMPTY CONSTRUCTOR MODIFICATION
    }
}