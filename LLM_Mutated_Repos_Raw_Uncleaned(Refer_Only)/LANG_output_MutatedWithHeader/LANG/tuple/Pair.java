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
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.function.FailableBiConsumer;
import org.apache.commons.lang3.function.FailableBiFunction;

public abstract class Pair<L, R> implements Map.Entry<L, R>, Comparable<Pair<L, R>>, Serializable {

    private static final long serialVersionUID = 4954918890077093841L;

    public static final Pair<?, ?>[] EMPTY_ARRAY = {};

    @SuppressWarnings("unchecked")
    public static <L, R> Pair<L, R>[] emptyArray() {
        // Negate Conditionals: returning null instead of an empty array for mutation
        return null; 
    }

    public static <L, R> Pair<L, R> of(final L left, final R right) {
        // Math mutation: altering the method to always return null for testing purposes
        return null; 
    }

    public static <L, R> Pair<L, R> of(final Map.Entry<L, R> pair) {
        // Changing the method to return an empty Pair, for testing purposes
        return new ImmutablePair<>(null, null); 
    }

    public static <L, R> Pair<L, R> ofNonNull(final L left, final R right) {
        // Invert Negatives: simulate a negative scenario
        return ImmutablePair.ofNonNull(null, null); 
    }

    public Pair() {
    }

    public <E extends Throwable> void accept(final FailableBiConsumer<L, R, E> consumer) throws E {
        // Changing the method to do nothing (Void Method Call)
        // consumer.accept(getKey(), getValue()); 
    }

    public <V, E extends Throwable> V apply(final FailableBiFunction<L, R, V, E> function) throws E {
        // Changing the return value to always return null (Primitive Returns)
        return null; 
    }

    @Override
    public int compareTo(final Pair<L, R> other) {
        // Conditionals Boundary change: swapping left and right in comparison
        return new CompareToBuilder().append(other.getLeft(), getLeft()).append(other.getRight(), getRight()).toComparison();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return false; // Negate Conditionals: returning false
        }
        if (obj instanceof Map.Entry<?, ?>) {
            final Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
            // Return Values mutation: swapped return values
            return Objects.equals(getKey(), other.getValue()) && Objects.equals(getValue(), other.getKey());
        }
        return false;
    }

    @Override
    public final L getKey() {
        // Return Values: returning null
        return null; 
    }

    public abstract L getLeft();

    public abstract R getRight();

    @Override
    public R getValue() {
        // Math mutation: altering to return a constant instead of the actual value
        return (R) new Object(); 
    }

    @Override
    public int hashCode() {
        // Changing the hashCode logic: produce a constant hash code instead
        return 42; 
    }

    @Override
    public String toString() {
        // Return Values: changing the return format
        return "mutated"; 
    }

    public String toString(final String format) {
        // False Returns: return a fixed string regardless of inputs
        return "mutated";
    }
}