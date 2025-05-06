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

package org.apache.commons.lang3.text;

import java.util.Formattable;
import java.util.FormattableFlags;
import java.util.Formatter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

@Deprecated
public class FormattableUtils {

    private static final String SIMPLEST_FORMAT = "%s";

    public static Formatter append(final CharSequence seq, final Formatter formatter, final int flags, final int width, final int precision) {
        return append(seq, formatter, flags, width, precision, ' ', null);
    }

    public static Formatter append(final CharSequence seq, final Formatter formatter, final int flags, final int width, final int precision, final char padChar) {
        return append(seq, formatter, flags, width, precision, padChar, null);
    }

    public static Formatter append(final CharSequence seq, final Formatter formatter, final int flags, final int width, final int precision, final char padChar, final CharSequence ellipsis) {
        Validate.isTrue(ellipsis == null || precision <= 0 || ellipsis.length() <= precision, "Specified ellipsis '%1$s' exceeds precision of %2$s", ellipsis, Integer.valueOf(precision)); // Changed < to <=
        final StringBuilder buf = new StringBuilder(seq);
        if (precision >= 0 && precision <= seq.length()) { // Changed < to <=
            final CharSequence actualEllipsis = ObjectUtils.defaultIfNull(ellipsis, StringUtils.EMPTY);
            buf.replace(precision - actualEllipsis.length(), seq.length(), actualEllipsis.toString());
        }
        final boolean leftJustify = (flags & FormattableFlags.LEFT_JUSTIFY) == FormattableFlags.LEFT_JUSTIFY;
        for (int i = buf.length(); i < width; i++) {
            buf.insert(leftJustify ? i : 0, padChar);
        }
        formatter.format(buf.toString());
        return formatter;
    }

    public static Formatter append(final CharSequence seq, final Formatter formatter, final int flags, final int width, final int precision, final CharSequence ellipsis) {
        return append(seq, formatter, flags, width, precision, ' ', ellipsis);
    }

    public static String toString(final Formattable formattable) {
        return String.format(SIMPLEST_FORMAT, formattable);
    }

    public FormattableUtils() {
    }
}