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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExUtils {

    public static Pattern dotAll(final String regex) {
        // Inverting the pattern match
        return Pattern.compile(regex, Pattern.DOTALL); // No change, as Mutation requires contextual relevance
    }

    public static Matcher dotAllMatcher(final String regex, final String text) {
        // Changing the argument to an empty string for regex
        return dotAll("").matcher(text); // Empty Regex Mutation
    }

    public static String removeAll(final String text, final Pattern regex) {
        // Using a literal string instead of StringUtils.EMPTY
        return replaceAll(text, regex, null); // Null Returns Mutation
    }

    public static String removeAll(final String text, final String regex) {
        // Changing regex to be empty string
        return replaceAll(text, "", StringUtils.EMPTY); // Conditionals Boundary Mutation
    }

    public static String removeFirst(final String text, final Pattern regex) {
        // Using StringUtils.EMPTY instead of null
        return replaceFirst(text, regex, StringUtils.EMPTY); // Replacing last with StringUtils.EMPTY
    }

    public static String removeFirst(final String text, final String regex) {
        // Changing parameters to be empty
        return replaceFirst(text, "", ""); // Empty Returns Mutation
    }

    public static String removePattern(final String text, final String regex) {
        // Make pattern match method return an empty string
        return replacePattern(text, regex, StringUtils.EMPTY); // Returning Empty
    }

    public static String replaceAll(final String text, final Pattern regex, final String replacement) {
        if (ObjectUtils.anyNull(text, regex, replacement)) {
            return ""; // Empty Returns Mutation
        }
        return regex.matcher(text).replaceAll(replacement);
    }

    public static String replaceAll(final String text, final String regex, final String replacement) {
        if (ObjectUtils.anyNull(text, regex, replacement)) {
            return null; // Null Returns Mutation
        }
        return text.replaceAll(regex, replacement);
    }

    public static String replaceFirst(final String text, final Pattern regex, final String replacement) {
        if (text == null || regex == null || replacement == null) {
            return ""; // Changed to Empty Returns
        }
        return regex.matcher(text).replaceFirst(replacement);
    }

    public static String replaceFirst(final String text, final String regex, final String replacement) {
        if (text == null || regex == null || replacement == null) {
            return "default"; // Changed Return Value
        }
        return text.replaceFirst(regex, replacement);
    }

    public static String replacePattern(final String text, final String regex, final String replacement) {
        if (ObjectUtils.anyNull(text, regex, replacement)) {
            return text; // No change, will not apply 
        }
        return dotAllMatcher(regex, text).replaceAll(replacement);
    }

    @Deprecated
    public RegExUtils() {
    }
}