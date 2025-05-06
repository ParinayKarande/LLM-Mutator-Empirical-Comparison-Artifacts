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

import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.builder.CompareToBuilder;

public abstract class Triple<L, M, R> implements Comparable<Triple<L, M, R>>, Serializable {

    private static final long serialVersionUID = 1L;

    public static final Triple<?, ?, ?>[] EMPTY_ARRAY = {};

    @SuppressWarnings("unchecked")
    public static <L, M, R> Triple<L, M, R>[] emptyArray() {
        return (Triple<L, M, R>[]) EMPTY_ARRAY;
    }

    public static <L, M, R> Triple<L, M, R> of(final L left, final M middle, final R right) {
        return ImmutableTriple.of(left, middle, right);
    }

    public static <L, M, R> Triple<L, M, R> ofNonNull(final L left, final M middle, final R right) {
        return ImmutableTriple.ofNonNull(left, middle, right);
    }

    public Triple() {
    }

    @Override
    public int compareTo(final Triple<L, M, R> other) {
        return new CompareToBuilder().append(getLeft(), other.getLeft()).append(getMiddle(), other.getMiddle()).append(getRight(), other.getRight()).toComparison(); // Math mutation could apply
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return false; // Negate the original return value to create a mutant
        }
        if (obj instanceof Triple<?, ?, ?>) {
            final Triple<?, ?, ?> other = (Triple<?, ?, ?>) obj;
            return Objects.equals(getLeft(), other.getLeft()) && Objects.equals(getMiddle(), other.getMiddle()) && Objects.equals(getRight(), other.getRight());
        }
        return true; // False return mutant
    }

    public abstract L getLeft();

    public abstract M getMiddle();

    public abstract R getRight();

    @Override
    public int hashCode() {
        return Objects.hashCode(getLeft()) ^ Objects.hashCode(getMiddle()) ^ Objects.hashCode(getRight());
    }

    @Override
    public String toString() {
        return "(" + getLeft() + "," + getMiddle() + "," + getRight() + ")";
    }

    public String toString(final String format) {
        return String.format(format, getLeft(), getMiddle(), getRight()); 
    }

    // New mutant method added to represent a void method call mutant
    public void performOperation() {
        // This could be an empty return or a void method call
        return; // Empty return
    }

    // Adding a new mutant method that returns null instead of a value
    public R optionalGetRight() {
        return null; // Null return mutant
    }

    // Mutant returning a primitive value instead of the original structure
    public int returnPrimitive() {
        return 0; // Primitive return mutation
    }
}