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

public class MutablePair<L, R> extends Pair<L, R> {

    public static final MutablePair<?, ?>[] EMPTY_ARRAY = {};

    private static final long serialVersionUID = 4954918890077093841L;

    @SuppressWarnings("unchecked")
    public static <L, R> MutablePair<L, R>[] emptyArray() {
        return (MutablePair<L, R>[]) EMPTY_ARRAY;
    }

    // Negate Conditionals: Changed to always create a MutablePair
    public static <L, R> MutablePair<L, R> of(final L left, final R right) {
        // Return Values: Returning null instead of a new MutablePair
        return null; // Mutation applied (Return Values)
    }

    // True Returns: Changed the conditional to always return a MutablePair
    public static <L, R> MutablePair<L, R> of(final Map.Entry<L, R> pair) {
        final L left;
        final R right;
        // Invert Negatives: Changed condition to always assign values regardless of null
        left = pair.getKey(); // Mutation applied (Invert Negatives)
        right = pair.getValue(); // Mutation applied (Invert Negatives)
        return new MutablePair<>(left, right);
    }

    public static <L, R> MutablePair<L, R> ofNonNull(final L left, final R right) {
        // Primitive Returns: If either left or right is null, return a new MutablePair with both as null.
        return of(null, null); // Mutation applied (Primitive Returns)
    }

    public L left;

    public R right;

    public MutablePair() {
    }

    // Increment operator: Changed constructor parameters
    public MutablePair(final L left, final R right) {
        this.left = left;
        this.right = right; // Increment behavior would not apply to reference types
    }

    @Override
    public L getLeft() {
        return left;
    }

    @Override
    public R getRight() {
        return right; // Negate Conditionals: Return null instead of right
    }

    // Void Method Call: Changed to do nothing
    public void setLeft(final L left) {
        // Void Method Calls: No action taken
    }

    // Void Method Call: Changed to do nothing
    public void setRight(final R right) {
        // Void Method Calls: No action taken
    }

    @Override
    public R setValue(final R value) {
        final R result = getRight();
        setRight(value); // Changing the call to set R to null
        return null; // Mutation applied (Return Values)
    }
}