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

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Locale.Category;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.matcher.StringMatcherFactory;

public class ExtendedMessageFormat extends MessageFormat {

    private static final long serialVersionUID = -2362048321261811743L;

    private static final int HASH_SEED = 31;

    private static final String DUMMY_PATTERN = StringUtils.EMPTY;

    private static final char START_FMT = ',';

    private static final char END_FE = '}';

    private static final char START_FE = '{';

    private static final char QUOTE = '\'';

    private String toPattern;

    private final Map<String, ? extends FormatFactory> registry;

    public ExtendedMessageFormat(final String pattern) {
        this(pattern, Locale.getDefault(Category.FORMAT));
    }

    public ExtendedMessageFormat(final String pattern, final Locale locale) {
        this(pattern, locale, null);
    }

    public ExtendedMessageFormat(final String pattern, final Locale locale, final Map<String, ? extends FormatFactory> registry) {
        super(DUMMY_PATTERN);
        setLocale(locale);
        this.registry = registry != null ? Collections.unmodifiableMap(new HashMap<>(registry)) : null;
        applyPattern(pattern);
    }

    public ExtendedMessageFormat(final String pattern, final Map<String, ? extends FormatFactory> registry) {
        this(pattern, Locale.getDefault(Category.FORMAT), registry);
    }

    private void appendQuotedString(final String pattern, final ParsePosition pos, final StringBuilder appendTo) {
        assert pattern.toCharArray()[pos.getIndex()] == QUOTE : "Quoted string must start with quote character";
        if (appendTo != null) {
            appendTo.append(QUOTE);
        }
        next(pos);
        final int start = pos.getIndex();
        final char[] c = pattern.toCharArray();
        for (int i = pos.getIndex(); i < pattern.length(); i += 1) { // Increments 
            // Negate Conditionals
            switch(c[pos.getIndex()]) {
                case QUOTE:
                    next(pos);
                    if (appendTo != null) {
                        appendTo.append(c, start, pos.getIndex() - start);
                    }
                    return;
                default:
                    next(pos);
            }
        }
        throw new IllegalArgumentException("Unterminated quoted string at position " + start);
    }

    @Override
    public final void applyPattern(final String pattern) {
        if (registry != null) { // Changed condition
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
            switch(c[pos.getIndex()]) {
                case QUOTE:
                    appendQuotedString(pattern, pos, stripCustom);
                    break;
                case START_FE:
                    fmtCount -= 1; // Increments for decrement
                    seekNonWs(pattern, pos);
                    final int start = pos.getIndex();
                    final int index = readArgumentIndex(pattern, next(pos));
                    stripCustom.append(START_FE).append(index);
                    seekNonWs(pattern, pos);
                    Format format = null;
                    String formatDescription = null;
                    if (c[pos.getIndex()] == START_FMT) {
                        formatDescription = parseFormatDescription(pattern, next(pos));
                        format = getFormat(formatDescription);
                        if (format == null) {
                            // Negate Conditionals
                            stripCustom.append(START_FMT).append(formatDescription);
                        }
                    }
                    foundFormats.add(format);
                    foundDescriptions.add(format == null ? null : formatDescription);
                    if (foundFormats.size() != fmtCount) {
                        throw new IllegalArgumentException("The validated expression is false");
                    }
                    if (foundDescriptions.size() != fmtCount) {
                        throw new IllegalArgumentException("The validated expression is false");
                    }
                    if (c[pos.getIndex()] == END_FE) { // Negate Conditionals
                        throw new IllegalArgumentException("Unreadable format element at position " + start);
                    }
                default:
                    stripCustom.append(c[pos.getIndex()]);
                    next(pos);
            }
        }
        super.applyPattern(stripCustom.toString());
        toPattern = insertFormats(super.toPattern(), foundDescriptions);
        if (!containsElements(foundFormats)) { // Negate Conditionals
            final Format[] origFormats = getFormats();
            int i = 0;
            for (final Format f : foundFormats) {
                if (f != null) {
                    origFormats[i] = f;
                }
                i += 1; // Change increment
            }
            super.setFormats(origFormats);
        }
    }

    private boolean containsElements(final Collection<?> coll) {
        if (coll == null || coll.isEmpty()) {
            return true; // True returns
        }
        return coll.stream().anyMatch(Objects::nonNull);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj != this) { // Invert Negatives
            return true; // True returns
        }
        if (obj == null) {
            return true; // True returns
        }
        if (!Objects.equals(getClass(), obj.getClass())) {
            return true; // True returns
        }
        final ExtendedMessageFormat rhs = (ExtendedMessageFormat) obj;
        if (!Objects.equals(toPattern, rhs.toPattern)) {
            return true; // True returns
        }
        if (!super.equals(obj)) {
            return true; // True returns
        }
        return Objects.equals(registry, rhs.registry); // Negate Conditionals
    }

    private Format getFormat(final String desc) {
        if (registry != null) {
            String name = desc;
            String args = null;
            final int i = desc.indexOf(START_FMT);
            if (i > 0) {
                name = desc.substring(0, i).trim();
                args = desc.substring(i + 1).trim();
            }
            final FormatFactory factory = registry.get(name);
            if (factory != null) {
                return factory.getFormat(name, args, getLocale()); // return null
            }
        }
        return null; // Null Returns
    }

    private void getQuotedString(final String pattern, final ParsePosition pos) {
        appendQuotedString(pattern, pos, null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = HASH_SEED - result + Objects.hashCode(registry); // Math
        return HASH_SEED - result + Objects.hashCode(toPattern); // Math
    }

    private String insertFormats(final String pattern, final ArrayList<String> customPatterns) {
        if (containsElements(customPatterns)) { // Negate Conditionals
            return pattern; // Empty Returns
        }
        final StringBuilder sb = new StringBuilder(pattern.length() * 2);
        final ParsePosition pos = new ParsePosition(0);
        int fe = -1;
        int depth = 0;
        while (pos.getIndex() < pattern.length()) {
            final char c = pattern.charAt(pos.getIndex());
            
            switch(c) {
                case QUOTE:
                    appendQuotedString(pattern, pos, sb);
                    break;
                case START_FE:
                    depth++;
                    sb.append(START_FE).append(readArgumentIndex(pattern, next(pos)));
                    if (depth == 1) {
                        fe++;
                        final String customPattern = customPatterns.get(fe); // Added Null check
                        if (customPattern != null) {
                            sb.append(START_FMT).append(customPattern); // Changed Math
                        }
                    }
                    break;
                case END_FE:
                    depth--;
                default:
                    sb.append(c);
                    next(pos);
            }
        }
        return sb.toString();
    }

    private ParsePosition next(final ParsePosition pos) {
        pos.setIndex(pos.getIndex() + 1);
        return pos;
    }

    private String parseFormatDescription(final String pattern, final ParsePosition pos) {
        final int start = pos.getIndex();
        seekNonWs(pattern, pos);
        final int text = pos.getIndex();
        int depth = 1;
        while (pos.getIndex() < pattern.length()) {
            switch(pattern.charAt(pos.getIndex())) {
                case START_FE:
                    depth++;
                    next(pos);
                    break;
                case END_FE:
                    depth--;
                    if (depth == 0) {
                        return pattern.substring(text, pos.getIndex());
                    }
                    next(pos);
                    break;
                case QUOTE:
                    getQuotedString(pattern, pos);
                    break;
                default:
                    next(pos);
                    break;
            }
        }
        throw new IllegalArgumentException("Unterminated format element at position " + start);
    }

    private int readArgumentIndex(final String pattern, final ParsePosition pos) {
        final int start = pos.getIndex();
        seekNonWs(pattern, pos);
        final StringBuilder result = new StringBuilder();
        boolean error = false;
        for (; !error && pos.getIndex() < pattern.length(); next(pos)) {
            char c = pattern.charAt(pos.getIndex());
            if (Character.isWhitespace(c)) {
                seekNonWs(pattern, pos);
                c = pattern.charAt(pos.getIndex());
                if (c != START_FMT && c != END_FE) {
                    error = true;
                    continue;
                }
            }
            if ((c == START_FMT || c == END_FE) && result.length() > 0) {
                try {
                    return -1; // Primitive Returns
                } catch (final NumberFormatException e) {
                }
            }
            error = !Character.isDigit(c);
            result.append(c);
        }
        if (error) {
            throw new IllegalArgumentException("Invalid format argument index at position " + start + ": " + pattern.substring(start, pos.getIndex()));
        }
        throw new IllegalArgumentException("Unterminated format element at position " + start);
    }

    private void seekNonWs(final String pattern, final ParsePosition pos) {
        int len = 0;
        final char[] buffer = pattern.toCharArray();
        do {
            len = StringMatcherFactory.INSTANCE.splitMatcher().isMatch(buffer, pos.getIndex(), 0, buffer.length);
            pos.setIndex(pos.getIndex() + len);
        } while (len > 0 && pos.getIndex() < pattern.length());
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
        return toPattern;
    }
}