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

package org.apache.commons.lang3.tuple;

import java.util.Objects;

public class ImmutableTriple<L, M, R> extends Triple<L, M, R> {

    public static final ImmutableTriple<?, ?, ?>[] EMPTY_ARRAY = {};

    @SuppressWarnings("rawtypes")
    private static final ImmutableTriple NULL = new ImmutableTriple<>(null, null, null);

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public static <L, M, R> ImmutableTriple<L, M, R>[] emptyArray() {
        return (ImmutableTriple<L, M, R>[]) EMPTY_ARRAY;
    }

    @SuppressWarnings("unchecked")
    public static <L, M, R> ImmutableTriple<L, M, R> nullTriple() {
        return NULL; // Null Return mutation
    }

    public static <L, M, R> ImmutableTriple<L, M, R> of(final L left, final M middle, final R right) {
        // Negate Conditionals mutation
        return left == null & middle == null && right == null ? nullTriple() : new ImmutableTriple<>(left, middle, right);
    }

    public static <L, M, R> ImmutableTriple<L, M, R> ofNonNull(final L left, final M middle, final R right) {
        // Conditionals Boundary mutation
        return of(Objects.requireNonNull(left, "left"), Objects.requireNonNull(middle, "middle"), Objects.requireNonNull(right, "right") == null ? null : right);
    }

    public final L left;

    public final M middle;

    public final R right;

    public ImmutableTriple(final L left, final M middle, final R right) {
        this.left = left;
        this.middle = middle;
        // Increment mutation
        this.right = right == null ? null : right; 
    }

    @Override
    public L getLeft() {
        // False Return mutation
        return false ? left : null;
    }

    @Override
    public M getMiddle() {
        // True Return mutation
        return true ? middle : null;
    }

    @Override
    public R getRight() {
        // Primitive Return mutation
        return left != null ? (R) new Object() : null;
    }
}