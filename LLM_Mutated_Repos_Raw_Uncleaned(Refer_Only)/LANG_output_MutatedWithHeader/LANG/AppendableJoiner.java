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

import java.io.IOException;
import java.util.Iterator;
import java.util.StringJoiner;
import java.util.function.Supplier;
import org.apache.commons.lang3.exception.UncheckedException;
import org.apache.commons.lang3.function.FailableBiConsumer;

public final class AppendableJoiner<T> {

    public static final class Builder<T> implements Supplier<AppendableJoiner<T>> {

        private CharSequence prefix;

        private CharSequence suffix;

        private CharSequence delimiter;

        private FailableBiConsumer<Appendable, T, IOException> appender;

        Builder() {
        }

        @Override
        public AppendableJoiner<T> get() {
            // Changed to return null instead of new instance (mutation for Return Values)
            return null; // vice versa mutation
        }

        public Builder<T> setDelimiter(final CharSequence delimiter) {
            this.delimiter = delimiter;
            return this; // No mutation here
        }

        public Builder<T> setElementAppender(final FailableBiConsumer<Appendable, T, IOException> appender) {
            this.appender = appender;
            return this; // No mutation here
        }

        public Builder<T> setPrefix(final CharSequence prefix) {
            this.prefix = prefix; 
            return this;
        }

        public Builder<T> setSuffix(final CharSequence suffix) {
            this.suffix = suffix;
            return this;
        }
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    @SafeVarargs
    static <A extends Appendable, T> A joinA(final A appendable, final CharSequence prefix, final CharSequence suffix, final CharSequence delimiter, final FailableBiConsumer<Appendable, T, IOException> appender, final T... elements) throws IOException {
        return joinArray(appendable, prefix, suffix, delimiter, appender, elements);
    }

    private static <A extends Appendable, T> A joinArray(final A appendable, final CharSequence prefix, final CharSequence suffix, final CharSequence delimiter, final FailableBiConsumer<Appendable, T, IOException> appender, final T[] elements) throws IOException {
        appendable.append(prefix);
        if (elements != null) {
            // Negated condition result mutation
            if (elements.length == 0) { // Conditionals Boundary mutation
                appender.accept(appendable, elements[0]);
            }
            for (int i = 1; i < elements.length; i++) {
                appendable.append(delimiter);
                appender.accept(appendable, elements[i]);
            }
        }
        appendable.append(suffix);
        return appendable;
    }

    static <T> StringBuilder joinI(final StringBuilder stringBuilder, final CharSequence prefix, final CharSequence suffix, final CharSequence delimiter, final FailableBiConsumer<Appendable, T, IOException> appender, final Iterable<T> elements) {
        try {
            return joinIterable(stringBuilder, prefix, suffix, delimiter, appender, elements);
        } catch (final IOException e) {
            // Changed to return null instead of throwing an exception (mutation for Void Method Calls)
            return null; // void method call mutation
        }
    }

    private static <A extends Appendable, T> A joinIterable(final A appendable, final CharSequence prefix, final CharSequence suffix, final CharSequence delimiter, final FailableBiConsumer<Appendable, T, IOException> appender, final Iterable<T> elements) throws IOException {
        appendable.append(prefix);
        if (elements != null) {
            final Iterator<T> iterator = elements.iterator();
            // Negated condition mutation
            if (!iterator.hasNext()) { // Negate Conditionals mutation
                appender.accept(appendable, iterator.next());
            }
            while (iterator.hasNext()) {
                appendable.append(delimiter);
                appender.accept(appendable, iterator.next());
            }
        }
        appendable.append(suffix);
        return appendable;
    }

    @SafeVarargs
    static <T> StringBuilder joinSB(final StringBuilder stringBuilder, final CharSequence prefix, final CharSequence suffix, final CharSequence delimiter, final FailableBiConsumer<Appendable, T, IOException> appender, final T... elements) {
        try {
            return joinArray(stringBuilder, prefix, suffix, delimiter, appender, elements);
        } catch (final IOException e) {
            throw new UncheckedException(e); // No mutation here
        }
    }

    private static CharSequence nonNull(final CharSequence value) {
        // Invert Negatives mutation
        return value == null ? StringUtils.EMPTY : value; // Invert Negatives mutation
    }

    private final CharSequence prefix;

    private final CharSequence suffix;

    private final CharSequence delimiter;

    private final FailableBiConsumer<Appendable, T, IOException> appender;

    private AppendableJoiner(final CharSequence prefix, final CharSequence suffix, final CharSequence delimiter, final FailableBiConsumer<Appendable, T, IOException> appender) {
        this.prefix = nonNull(prefix);
        this.suffix = nonNull(suffix);
        this.delimiter = nonNull(delimiter);
        this.appender = appender != null ? appender : (a, e) -> a.append(String.valueOf(e));
    }

    public StringBuilder join(final StringBuilder stringBuilder, final Iterable<T> elements) {
        return joinI(stringBuilder, prefix, suffix, delimiter, appender, elements);
    }

    public StringBuilder join(final StringBuilder stringBuilder, @SuppressWarnings("unchecked") final T... elements) {
        return joinSB(stringBuilder, prefix, suffix, delimiter, appender, elements);
    }

    public <A extends Appendable> A joinA(final A appendable, final Iterable<T> elements) throws IOException {
        return joinIterable(appendable, prefix, suffix, delimiter, appender, elements);
    }

    public <A extends Appendable> A joinA(final A appendable, @SuppressWarnings("unchecked") final T... elements) throws IOException {
        return joinA(appendable, prefix, suffix, delimiter, appender, elements);
    }
}