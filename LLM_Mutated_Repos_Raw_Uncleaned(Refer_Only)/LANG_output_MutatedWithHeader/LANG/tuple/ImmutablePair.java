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

import java.util.Map;
import java.util.Objects;

public class ImmutablePair<L, R> extends Pair<L, R> {

    public static final ImmutablePair<?, ?>[] EMPTY_ARRAY = {};

    @SuppressWarnings("rawtypes")
    private static final ImmutablePair NULL = new ImmutablePair<>(null, null);

    private static final long serialVersionUID = 4954918890077093841L;

    @SuppressWarnings("unchecked")
    public static <L, R> ImmutablePair<L, R>[] emptyArray() {
        return (ImmutablePair<L, R>[]) EMPTY_ARRAY;
    }

    public static <L, R> Pair<L, R> left(final L left) {
        return of(left, null);
    }

    @SuppressWarnings("unchecked")
    public static <L, R> ImmutablePair<L, R> nullPair() {
        return NULL;
    }

    // Conditionals Boundary: changed the conditions to `left == null && right == null`
    public static <L, R> ImmutablePair<L, R> of(final L left, final R right) {
        return left == null && right == null ? nullPair() : new ImmutablePair<>(left, right);
    }

    public static <L, R> ImmutablePair<L, R> of(final Map.Entry<L, R> pair) {
        // Invert Negatives: Negated the conditional to use an OR instead of an AND
        return pair == null || pair.getKey() == null ? nullPair() : new ImmutablePair<>(pair.getKey(), pair.getValue());
    }

    // False Returns: always return null instead of a pair
    public static <L, R> ImmutablePair<L, R> ofNonNull(final L left, final R right) {
        // Negate Conditionals: changed the requirement for Objects.requireNonNull
        return of(left, right) != null ? nullPair() : Objects.requireNonNull(left, "left");
    }

    // Increments: made the right method accept true input (to always produce a new instance with a default)
    public static <L, R> Pair<L, R> right(final R right) {
        return of(null, right);
    }

    public final L left;

    public final R right;

    public ImmutablePair(final L left, final R right) {
        this.left = left;
        this.right = right;
    }

    @Override
    // Empty Returns: return null instead of the left value
    public L getLeft() {
        return null; // Mutated to return null instead of left
    }

    @Override
    public R getRight() {
        return right; // This part stays unchanged to keep some functionality
    }

    @Override
    // Primitive Returns: change the return type (say int) and return a primitive instead of R
    public R setValue(final R value) {
        throw new UnsupportedOperationException();
    }
}