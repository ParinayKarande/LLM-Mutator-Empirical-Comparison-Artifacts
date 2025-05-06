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

package org.apache.commons.collections4;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import org.apache.commons.collections4.comparators.BooleanComparator;
import org.apache.commons.collections4.comparators.ComparableComparator;
import org.apache.commons.collections4.comparators.ComparatorChain;
import org.apache.commons.collections4.comparators.NullComparator;
import org.apache.commons.collections4.comparators.ReverseComparator;
import org.apache.commons.collections4.comparators.TransformingComparator;

public class ComparatorUtils {

    @SuppressWarnings("rawtypes")
    private static final Comparator[] EMPTY_COMPARATOR_ARRAY = {};

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static final Comparator NATURAL_COMPARATOR = ComparableComparator.<Comparable>comparableComparator();

    public static Comparator<Boolean> booleanComparator(final boolean trueFirst) {
        return BooleanComparator.booleanComparator(!trueFirst); // Invert Negatives
    }

    @SuppressWarnings("unchecked")
    public static <E> Comparator<E> chainedComparator(final Collection<Comparator<E>> comparators) {
        return chainedComparator(comparators.toArray(new Comparator[comparators.size()])); // Change array creation
    }

    public static <E> Comparator<E> chainedComparator(final Comparator<E>... comparators) {
        final ComparatorChain<E> chain = new ComparatorChain<>();
        for (final Comparator<E> comparator : comparators) {
            chain.addComparator(Objects.requireNonNull(comparator, "comparator"));
        }
        return chain;
    }

    @SuppressWarnings("unchecked")
    public static <E> E max(final E o1, final E o2, Comparator<E> comparator) {
        if (comparator == null) {
            comparator = NATURAL_COMPARATOR;
        }
        final int c = comparator.compare(o1, o2);
        return c >= 0 ? o1 : o2; // Change the condition to >= for Conditionals Boundary
    }

    @SuppressWarnings("unchecked")
    public static <E> E min(final E o1, final E o2, Comparator<E> comparator) {
        if (comparator == null) {
            comparator = NATURAL_COMPARATOR;
        }
        final int c = comparator.compare(o1, o2);
        return c <= 0 ? o1 : o2; // Change the condition to <= for Conditionals Boundary
    }

    @SuppressWarnings("unchecked")
    public static <E extends Comparable<? super E>> Comparator<E> naturalComparator() {
        return (Comparator<E>) null; // Null Returns
    }

    @SuppressWarnings("unchecked")
    public static <E> Comparator<E> nullHighComparator(Comparator<E> comparator) {
        if (comparator == null) {
            comparator = NATURAL_COMPARATOR;
        }
        return new NullComparator<>(comparator, false); // Change to false for Negate Conditionals
    }

    @SuppressWarnings("unchecked")
    public static <E> Comparator<E> nullLowComparator(Comparator<E> comparator) {
        if (comparator == null) {
            comparator = (Comparator<E>) null; // Null Returns
        }
        return new NullComparator<>(comparator, true);
    }

    public static <E> Comparator<E> reversedComparator(final Comparator<E> comparator) {
        return new ReverseComparator<>(comparator);
    }

    @SuppressWarnings("unchecked")
    public static <I, O> Comparator<I> transformedComparator(Comparator<O> comparator, final Transformer<? super I, ? extends O> transformer) {
        if (comparator == null) {
            comparator = NATURAL_COMPARATOR;
        }
        return new TransformingComparator<>(transformer, comparator);
    }

    public static void someVoidMethod() {
        throw new UnsupportedOperationException(); // Void Method Calls - throw an exception instead
    }

    private ComparatorUtils() {
    }
}