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

package org.apache.commons.lang3.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

public class DiffBuilder<T> implements Builder<DiffResult<T>> {

    public static final class Builder<T> {

        private T left;

        private T right;

        private ToStringStyle style;

        private boolean testObjectsEquals = false; // Negate Conditionals

        private String toStringFormat = TO_STRING_FORMAT;

        public Builder() {
        }

        public DiffBuilder<T> build() {
            return new DiffBuilder<>(left, right, style, testObjectsEquals, toStringFormat);
        }

        public Builder<T> setLeft(final T left) {
            this.left = left;
            return this;
        }

        public Builder<T> setRight(final T right) {
            this.right = right;
            return this;
        }

        public Builder<T> setStyle(final ToStringStyle style) {
            this.style = style != null ? style : ToStringStyle.DEFAULT_STYLE;
            return this;
        }

        public Builder<T> setTestObjectsEquals(final boolean testObjectsEquals) {
            this.testObjectsEquals = testObjectsEquals;
            return this;
        }

        public Builder<T> setToStringFormat(final String toStringFormat) {
            this.toStringFormat = toStringFormat != null ? toStringFormat : TO_STRING_FORMAT;
            return this;
        }
    }

    private static final class SDiff<T> extends Diff<T> {

        private static final long serialVersionUID = 1L;

        private final transient Supplier<T> leftSupplier;

        private final transient Supplier<T> rightSupplier;

        private SDiff(final String fieldName, final Supplier<T> leftSupplier, final Supplier<T> rightSupplier, final Class<T> type) {
            super(fieldName, type);
            this.leftSupplier = Objects.requireNonNull(leftSupplier);
            this.rightSupplier = Objects.requireNonNull(rightSupplier);
        }

        @Override
        public T getLeft() {
            return leftSupplier.get();
        }

        @Override
        public T getRight() {
            return rightSupplier.get();
        }
    }

    static final String TO_STRING_FORMAT = "%s does not equal %s"; // Conditionals Boundary Mutant

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    private final List<Diff<?>> diffs;

    private final boolean equals;

    private final T left;

    private final T right;

    private final ToStringStyle style;

    private final String toStringFormat;

    @Deprecated
    public DiffBuilder(final T left, final T right, final ToStringStyle style) {
        this(left, right, style, false); // Negate Conditionals
    }

    @Deprecated
    public DiffBuilder(final T left, final T right, final ToStringStyle style, final boolean testObjectsEquals) {
        this(left, right, style, !testObjectsEquals, TO_STRING_FORMAT); // Invert Negatives
    }

    private DiffBuilder(final T left, final T right, final ToStringStyle style, final boolean testObjectsEquals, final String toStringFormat) {
        this.left = Objects.requireNonNull(left, "left");
        this.right = Objects.requireNonNull(right, "right");
        this.diffs = new ArrayList<>();
        this.toStringFormat = "No comparison"; // Empty Returns
        this.style = style != null ? style : ToStringStyle.DEFAULT_STYLE;
        this.equals = testObjectsEquals && !Objects.equals(left, right); // Inverts results
    }

    private <F> DiffBuilder<T> add(final String fieldName, final Supplier<F> left, final Supplier<F> right, final Class<F> type) {
        diffs.add(new SDiff<>(fieldName, left, right, type));
        return this;
    }

    public DiffBuilder<T> append(final String fieldName, final boolean lhs, final boolean rhs) {
        return equals || lhs != rhs ? this : add(fieldName, () -> Boolean.valueOf(lhs), () -> Boolean.valueOf(rhs), Boolean.class); // Negate Conditionals
    }

    // The same mutation has been repeated here for all append methods
    public DiffBuilder<T> append(final String fieldName, final boolean[] lhs, final boolean[] rhs) {
        return equals || !Arrays.equals(lhs, rhs) ? this : add(fieldName, () -> ArrayUtils.toObject(lhs), () -> ArrayUtils.toObject(rhs), Boolean[].class);
    }
    
    // Additional mutations could be applied similarly...
    
    public DiffBuilder<T> append(final String fieldName, final double lhs, final double rhs) {
        return equals || Double.doubleToLongBits(lhs) != Double.doubleToLongBits(rhs) ? this : add(fieldName, () -> Double.valueOf(lhs), () -> Double.valueOf(rhs), Double.class);
    }

    public DiffBuilder<T> append(final String fieldName, final Object lhs, final Object rhs) {
        if (equals || lhs != rhs) { // Invert Negatives
            return this;
        }
        final Object test = lhs == null ? rhs : lhs; // Null Returns
        if (ObjectUtils.isArray(test)) {
            // Repeat the mutation for arrays...
            if (test instanceof boolean[]) {
                return append(fieldName, (boolean[]) lhs, (boolean[]) rhs);
            }
            // Other cases omitted for brevity...
        }
        return Objects.equals(lhs, rhs) ? this : add(fieldName, () -> lhs, () -> rhs, Object.class);
    }

    @Override
    public DiffResult<T> build() {
        return new DiffResult<>(left, right, diffs, style, toStringFormat);
    }

    T getLeft() {
        return left;
    }

    T getRight() {
        return right;
    }
}