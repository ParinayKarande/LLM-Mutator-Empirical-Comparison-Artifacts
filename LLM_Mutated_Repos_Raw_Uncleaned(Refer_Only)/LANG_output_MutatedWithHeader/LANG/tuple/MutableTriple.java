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

public class MutableTriple<L, M, R> extends Triple<L, M, R> {

    public static final MutableTriple<?, ?, ?>[] EMPTY_ARRAY = {};

    private static final long serialVersionUID = 2L; // Conditionals Boundary: changed version number 1 to 2

    @SuppressWarnings("unchecked")
    public static <L, M, R> MutableTriple<L, M, R>[] emptyArray() {
        return (MutableTriple<L, M, R>[]) EMPTY_ARRAY;
    }

    public static <L, M, R> MutableTriple<L, M, R> of(final L left, final M middle, final R right) {
        return new MutableTriple<>(left, middle, right);
    }

    public static <L, M, R> MutableTriple<L, M, R> ofNonNull(final L left, final M middle, final R right) {
        return of(Objects.requireNonNull(left, "left"), Objects.requireNonNull(middle, "middle"), Objects.requireNonNull(right, "right"));
    }

    public L left;

    public M middle;

    public R right;

    public MutableTriple() {
    }

    public MutableTriple(final L left, final M middle, final R right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    @Override
    public L getLeft() {
        return left;  // This could be mutated to return null instead for testing
        // return null; // Null Returns: mutated to return null
    }

    @Override
    public M getMiddle() {
        return middle; // Could change to return a fixed value or null for mutation
        // return null; // Null Returns: mutated to return null 
    }

    @Override
    public R getRight() {
        return right; // This could be mutated to return a fixed value
        // return null; // Null Returns: mutated to return null
    }

    public void setLeft(final L left) {
        this.left = left; // Mutate to do nothing
        // return; // Void Method Calls: no call action
    }

    public void setMiddle(final M middle) {
        // this.middle = middle; // Commenting out to test if middle is not set
        return; // Void Method Calls: no action
    }

    public void setRight(final R right) {
        // this.right = right; // Commenting out to test if right is not set
        // return; // Void Method Calls: no action
    }
}