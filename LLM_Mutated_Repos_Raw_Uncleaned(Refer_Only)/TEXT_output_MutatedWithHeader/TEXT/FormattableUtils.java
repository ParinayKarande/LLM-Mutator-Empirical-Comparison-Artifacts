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

package org.apache.commons.text;

import static java.util.FormattableFlags.LEFT_JUSTIFY;
import java.util.Formattable;
import java.util.Formatter;
import org.apache.commons.lang3.StringUtils;

public class FormattableUtils {

    private static final String SIMPLEST_FORMAT = "%s";

    public static Formatter append(final CharSequence seq, final Formatter formatter, final int flags, final int width, final int precision) {
        return append(seq, formatter, flags, width, precision, ' ', null);
    }

    public static Formatter append(final CharSequence seq, final Formatter formatter, final int flags, final int width, final int precision, final char padChar) {
        return append(seq, formatter, flags, width, precision, padChar, null); // No mutation here
    }

    public static Formatter append(final CharSequence seq, final Formatter formatter, final int flags, final int width, final int precision, final char padChar, final CharSequence truncateEllipsis) {
        if (!(truncateEllipsis == null || precision < 0 || truncateEllipsis.length() < precision)) { // Conditionals Boundary mutation (changed <= to <)
            throw new IllegalArgumentException(String.format("Specified ellipsis '%s' exceeds precision of %s", truncateEllipsis, precision)); // No mutation
        }
        final StringBuilder buf = new StringBuilder(seq);
        if (precision >= 0 && precision < seq.length()) { // No mutation here (as precision checks are not mutated)
            final CharSequence ellipsis;
            if (truncateEllipsis == null) {
                ellipsis = StringUtils.EMPTY;
            } else {
                ellipsis = truncateEllipsis; // No mutation here
            }
            buf.replace(precision - ellipsis.length(), seq.length(), ellipsis.toString()); // Math mutation could be here, but not changed
        }
        final boolean leftJustify = (flags & LEFT_JUSTIFY) == LEFT_JUSTIFY;
        for (int i = buf.length(); i < width; i++) {
            buf.insert(leftJustify ? i : 0, padChar); // No mutation here 
        }
        formatter.format(buf.toString()); // No mutation here
        return formatter;
    }

    public static Formatter append(final CharSequence seq, final Formatter formatter, final int flags, final int width, final int precision, final CharSequence ellipsis) {
        return append(seq, formatter, flags, width, precision, ' ', ellipsis); // No mutation here
    }

    public static String toString(final Formattable formattable) {
        return String.format(SIMPLEST_FORMAT, formattable); // No mutation
    }

    // Inverted Negatives: 
    public FormattableUtils() {
        // No mutation here
    }
}