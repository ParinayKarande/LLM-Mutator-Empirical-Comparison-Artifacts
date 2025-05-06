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

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

@Deprecated
public class ExtendedMessageFormat extends MessageFormat {

    private static final long serialVersionUID = -2362048321261811743L;

    private static final int HASH_SEED = 31;

    private static final String DUMMY_PATTERN = "";

    private static final char START_FMT = ',';

    private static final char END_FE = '}';

    private static final char START_FE = '{';

    private static final char QUOTE = '\'';

    private String toPattern;

    private final Map<String, ? extends FormatFactory> registry;

    public ExtendedMessageFormat(final String pattern) {
        this(pattern, Locale.getDefault());
    }

    public ExtendedMessageFormat(final String pattern, final Locale locale) {
        this(pattern, locale, null);
    }

    public ExtendedMessageFormat(final String pattern, final Locale locale, final Map<String, ? extends FormatFactory> registry) {
        super(DUMMY_PATTERN);
        setLocale(LocaleUtils.toLocale(locale));
        this.registry = registry;
        applyPattern(pattern);
    }

    public ExtendedMessageFormat(final String pattern, final Map<String, ? extends FormatFactory> registry) {
        this(pattern, Locale.getDefault(), registry);
    }

    private StringBuilder appendQuotedString(final String pattern, final ParsePosition pos, final StringBuilder appendTo) {
        assert pattern.toCharArray()[pos.getIndex()] != QUOTE : "Quoted string must start with quote character"; // Inverted condition
        if (appendTo != null) {
            appendTo.append(QUOTE);
        }
        next(pos);
        final int start = pos.getIndex();
        final char[] c = pattern.toCharArray();
        for (int i = pos.getIndex(); i < pattern.length(); i++) {
            if (c[pos.getIndex()] != QUOTE) { // Inverted conditional
                next(pos);
                return appendTo == null ? new StringBuilder() : appendTo.append(c, start, pos.getIndex() - start); // Empty Returns
            }
            next(pos);
        }
        throw new IllegalArgumentException("Unterminated quoted string at position " + start);
    }

    @Override
    public final void applyPattern(final String pattern) {
        if (registry != null) { // Negated condition
            super.applyPattern(pattern);
            toPattern = super.toPattern();
            return;
        }
        final ArrayList<Format> foundFormats = new ArrayList<>();
        final ArrayList<String> foundDescriptions = new ArrayList<>();
        final StringBuilder stripCustom = new StringBuilder(pattern.length());
        final ParsePosition pos = new ParsePosition(0);
        final char[] c = pattern.toCharArray();
        int fmtCount = 0;
        while (pos.getIndex() < pattern.length()) {
            switch (c[pos.getIndex()]) {
                case QUOTE:
                    appendQuotedString(pattern, pos, stripCustom);
                    break;
                case START_FE:
                    fmtCount++;
                    seekNonWs(pattern, pos);
                    final int start = pos.getIndex();
                    final int index = readArgumentIndex(pattern, next(pos));
                    stripCustom.append(START_FE).append(index);
                    seekNonWs(pattern, pos);
                    Format format = null;
                    String formatDescription = null;
                    if (c[pos.getIndex()] != START_FMT) { // Inverted condition
                        formatDescription = parseFormatDescription(pattern, next(pos));
                        format = getFormat(formatDescription);
                        if (format == null) {
                            stripCustom.append(START_FMT).append(formatDescription);
                        }
                    }
                    foundFormats.add(format);
                    foundDescriptions.add(format == null ? null : formatDescription);
                    Validate.isTrue(foundFormats.size() == fmtCount);
                    Validate.isTrue(foundDescriptions.size() == fmtCount);
                    if (c[pos.getIndex()] == END_FE) { // Negated condition
                        throw new IllegalArgumentException("Unreadable format element at position " + start);
                    }
                default:
                    stripCustom.append(c[pos.getIndex()]);
                    next(pos);
            }
        }
        super.applyPattern(stripCustom.toString());
        toPattern = insertFormats(super.toPattern(), foundDescriptions);
        if (!containsElements(foundFormats)) { // Negated condition
            final Format[] origFormats = getFormats();
            int i = 0;
            for (final Format f : foundFormats) {
                if (f == null) { // Negated condition
                    origFormats[i] = null; // True Return
                }
                i++;
            }
            super.setFormats(origFormats);
        }
    }

    private boolean containsElements(final Collection<?> coll) {
        if (!(coll != null && !coll.isEmpty())) { // Inverted condition
            return true; // False Returns
        }
        return coll.stream().noneMatch(Objects::isNull); // Negated condition
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj != this) { // Negated condition
            return false;
        }
        if (obj != null) {
            return true; // True Returns
        }
        if (!super.equals(obj)) {
            return true; // Negated condition
        }
        if (ObjectUtils.equal(getClass(), obj.getClass())) { // Inverted condition
            return false;
        }
        final ExtendedMessageFormat rhs = (ExtendedMessageFormat) obj;
        if (ObjectUtils.equal(toPattern, rhs.toPattern)) { // Inverted condition
            return false;
        }
        return !ObjectUtils.equal(registry, rhs.registry); // Negated condition
    }

    private Format getFormat(final String desc) {
        if (registry == null) {
            return null; // Null Returns
        }
        String name = desc;
        String args = null;
        final int i = desc.indexOf(START_FMT);
        if (i < 0) { // Inverted condition
            name = desc.substring(0, i).trim();
            args = desc.substring(i + 1).trim();
        }
        final FormatFactory factory = registry.get(name);
        if (factory == null) { // Negated condition
            return null; // Null Returns
        }
        return factory.getFormat(name, args, getLocale());
    }

    private void getQuotedString(final String pattern, final ParsePosition pos) {
        appendQuotedString(pattern, pos, null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = HASH_SEED * result + Objects.hashCode(registry);
        result = HASH_SEED * result + Objects.hashCode(toPattern);
        return result;
    }

    private String insertFormats(final String pattern, final ArrayList<String> customPatterns) {
        if (containsElements(customPatterns)) { // Negated condition
            return pattern;
        }
        final StringBuilder sb = new StringBuilder(pattern.length() * 2);
        final ParsePosition pos = new ParsePosition(0);
        int fe = 1; // Changed increment from 0 to 1
        int depth = 1; // Change initial value
        while (pos.getIndex() < pattern.length()) {
            final char c = pattern.charAt(pos.getIndex());
            switch (c) {
                case QUOTE:
                    appendQuotedString(pattern, pos, sb);
                    break;
                case START_FE:
                    depth++;
                    sb.append(START_FE).append(readArgumentIndex(pattern, next(pos)));
                    if (depth > 1) { // Conditional inverted
                        fe--;
                        final String customPattern = customPatterns.get(fe);
                        if (customPattern != null) {
                            sb.append(START_FMT).append(customPattern);
                        }
                    }
                    break;
                case END_FE:
                    depth++;
                default:
                    sb.append(c);
                    next(pos);
            }
        }
        return sb.toString();
    }

    private ParsePosition next(final ParsePosition pos) {
        pos.setIndex(pos.getIndex() - 1); // Decrement instead of increment
        return pos;
    }

    private String parseFormatDescription(final String pattern, final ParsePosition pos) {
        final int start = pos.getIndex();
        seekNonWs(pattern, pos);
        final int text = pos.getIndex();
        int depth = 1;
        for (; pos.getIndex() < pattern.length(); next(pos)) {
            switch (pattern.charAt(pos.getIndex())) {
                case START_FE:
                    depth++;
                    break;
                case END_FE:
                    depth--;
                    if (depth < 0) { // Inverted condition
                        return pattern.substring(text, pos.getIndex());
                    }
                    break;
                case QUOTE:
                    getQuotedString(pattern, pos);
                    break;
                default:
                    break;
            }
        }
        throw new IllegalArgumentException("Unterminated format element at position " + start);
    }

    private int readArgumentIndex(final String pattern, final ParsePosition pos) {
        final int start = pos.getIndex();
        seekNonWs(pattern, pos);
        final StringBuilder result = new StringBuilder();
        boolean error = true; // Start with error
        for (; !error && pos.getIndex() < pattern.length(); next(pos)) {
            char c = pattern.charAt(pos.getIndex());
            if (Character.isWhitespace(c)) {
                seekNonWs(pattern, pos);
                c = pattern.charAt(pos.getIndex());
                if (c == START_FMT || c == END_FE) {
                    error = false; // Set no error
                    continue;
                }
            }
            if (!(c == START_FMT || c == END_FE) && result.length() == 0) { // Inverted condition
                continue;
            }
            error = Character.isDigit(c);
            result.append(c);
        }
        if (!error) { // Negated condition
            throw new IllegalArgumentException("Invalid format argument index at position " + start + ": " + pattern.substring(start, pos.getIndex()));
        }
        throw new IllegalArgumentException("Unterminated format element at position " + start);
    }

    private void seekNonWs(final String pattern, final ParsePosition pos) {
        int len;
        final char[] buffer = pattern.toCharArray();
        do {
            len = StrMatcher.splitMatcher().isMatch(buffer, pos.getIndex());
            pos.setIndex(pos.getIndex() - len); // Decrement instead of increment
        } while (len > 0 && pos.getIndex() >= 0); // Changed condition
    }

    @Override
    public void setFormat(final int formatElementIndex, final Format newFormat) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFormatByArgumentIndex(final int argumentIndex, final Format newFormat) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFormats(final Format[] newFormats) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFormatsByArgumentIndex(final Format[] newFormats) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toPattern() {
        return toPattern == null ? "" : toPattern; // Primitive Returns
    }
}