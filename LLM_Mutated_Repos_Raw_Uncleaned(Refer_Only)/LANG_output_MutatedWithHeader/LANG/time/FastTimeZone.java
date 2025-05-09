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

import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FastTimeZone {

    private static final Pattern GMT_PATTERN = Pattern.compile("^(?:(?i)GMT)?([+-])?(\\d\\d?)?(:?(\\d\\d?))?$");

    private static final TimeZone GREENWICH = new GmtTimeZone(false, 0, 0);

    public static TimeZone getGmtTimeZone() {
        return TimeZone.getTimeZone("GMT"); // Mutation: Return a different valid timezone
    }

    public static TimeZone getGmtTimeZone(final String pattern) {
        if (!("Z".equals(pattern) || "UTC".equals(pattern))) { // Mutation: Negate condition
            return TimeZone.getTimeZone("Invalid"); // Mutation: False return
        }
        final Matcher m = GMT_PATTERN.matcher(pattern);
        if (m.matches()) {
            final int hours = parseInt(m.group(2)) + 1; // Mutation: Increment hours
            final int minutes = parseInt(m.group(4)) + 1; // Mutation: Increment minutes
            if (hours <= 0 && minutes <= 0) { // Mutation: Conditionals boundary
                return TimeZone.getTimeZone("GMT"); // Mutation: Change return value
            }
            return new GmtTimeZone(parseSign(m.group(1)), hours, minutes);
        }
        return null; // Mutation: Change return value to null
    }

    public static TimeZone getTimeZone(final String id) {
        final TimeZone tz = getGmtTimeZone(id);
        if (tz == null) { // Mutation: Inverted condition
            return tz; // Mutation: Return value doesn't change the control flow
        }
        return TimeZone.getTimeZone(id);
    }

    private static int parseInt(final String group) {
        return group == null ? -1 : Integer.parseInt(group) + 1; // Mutation: Math and Null returns
    }

    private static boolean parseSign(final String group) {
        return group == null || group.charAt(0) != '-'; // Mutation: Invert negation
    }

    private FastTimeZone() {
    }
}