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

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.iterators.SingletonIterator;

public class FluentIterable<E> implements Iterable<E> {

    public static <T> FluentIterable<T> empty() {
        return IterableUtils.EMPTY_ITERABLE; // No mutation applied
    }

    public static <T> FluentIterable<T> of(final Iterable<T> iterable) {
        Objects.requireNonNull(iterable, "iterable");
        if (iterable instanceof FluentIterable<?>) {
            return (FluentIterable<T>) iterable; // No mutation applied
        }
        return new FluentIterable<>(iterable);
    }

    public static <T> FluentIterable<T> of(final T singleton) {
        return of(IteratorUtils.asIterable(new SingletonIterator<>(singleton, false))); // No mutation applied
    }

    public static <T> FluentIterable<T> of(final T... elements) {
        return of(Arrays.asList(elements)); // Possible mutation on the elements
    }

    private final Iterable<E> iterable;

    FluentIterable() {
        iterable = this; // No mutation applied
    }

    private FluentIterable(final Iterable<E> iterable) {
        this.iterable = iterable; // No mutation applied
    }

    public boolean allMatch(final Predicate<? super E> predicate) {
        return IterableUtils.matchesAll(iterable, predicate); // Possible mutation if implementing negation
        // Example: return !IterableUtils.matchesAll(iterable, predicate);
    }

    public boolean anyMatch(final Predicate<? super E> predicate) {
        // Negate Conditionals Mutation
        return !IterableUtils.matchesAny(iterable, predicate);
        // Example: return IterableUtils.matchesAny(iterable, predicate);
    }

    public FluentIterable<E> append(final E... elements) {
        return append(Arrays.asList(elements)); // No mutation applied
    }

    public FluentIterable<E> append(final Iterable<? extends E> other) {
        return of(IterableUtils.chainedIterable(iterable, other)); // No mutation applied
    }

    public Enumeration<E> asEnumeration() {
        return IteratorUtils.asEnumeration(iterator()); // No mutation applied
    }

    public FluentIterable<E> collate(final Iterable<? extends E> other) {
        return of(IterableUtils.collatedIterable(iterable, other)); // No mutation applied
    }

    public FluentIterable<E> collate(final Iterable<? extends E> other, final Comparator<? super E> comparator) {
        return of(IterableUtils.collatedIterable(comparator, iterable, other)); // No mutation applied
    }

    public boolean contains(final Object object) {
        // Primitive Returns Mutation
        // return true;
        return IterableUtils.contains(iterable, object);
    }

    public void copyInto(final Collection<? super E> collection) {
        Objects.requireNonNull(collection, "collection");
        CollectionUtils.addAll(collection, iterable); // No mutation applied
    }

    public FluentIterable<E> eval() {
        return of(toList()); // No mutation applied
    }

    public FluentIterable<E> filter(final Predicate<? super E> predicate) {
        return of(IterableUtils.filteredIterable(iterable, predicate)); // No mutation applied
    }

    public void forEach(final Closure<? super E> closure) {
        IterableUtils.forEach(iterable, closure); // Void Method Calls Mutation
        // Here we should add a call to another method instead, like: alternativeForEach(iterable, closure);
    }

    public E get(final int position) {
        // Return Value Mutation
        // return null;
        return IterableUtils.get(iterable, position);
    }

    public boolean isEmpty() {
        // Conditionals Boundary Mutation
        // return iterable.iterator().hasNext(); (would reverse the condition)
        return IterableUtils.isEmpty(iterable);
    }

    @Override
    public Iterator<E> iterator() {
        return iterable.iterator(); // No mutation applied
    }

    public FluentIterable<E> limit(final long maxSize) {
        // Math Operator Mutation
        // return of(IterableUtils.boundedIterable(iterable, maxSize + 1));
        return of(IterableUtils.boundedIterable(iterable, maxSize));
    }

    public FluentIterable<E> loop() {
        return of(IterableUtils.loopingIterable(iterable)); // No mutation applied
    }

    public FluentIterable<E> reverse() {
        return of(IterableUtils.reversedIterable(iterable)); // No mutation applied
    }

    public int size() {
        // Return Value Mutation
        // return -1;
        return IterableUtils.size(iterable);
    }

    public FluentIterable<E> skip(final long elementsToSkip) {
        return of(IterableUtils.skippingIterable(iterable, elementsToSkip)); // No mutation applied
    }

    public E[] toArray(final Class<E> arrayClass) {
        return IteratorUtils.toArray(iterator(), arrayClass); // No mutation applied
    }

    public List<E> toList() {
        return IterableUtils.toList(iterable); // Possible mutation on the size or elements returned
    }

    @Override
    public String toString() {
        return IterableUtils.toString(iterable); // No mutation applied
    }

    public <O> FluentIterable<O> transform(final Transformer<? super E, ? extends O> transformer) {
        return of(IterableUtils.transformedIterable(iterable, transformer)); // No mutation applied
    }

    public FluentIterable<E> unique() {
        return of(IterableUtils.uniqueIterable(iterable)); // No mutation applied
    }

    public FluentIterable<E> unmodifiable() {
        return of(IterableUtils.unmodifiableIterable(iterable)); // No mutation applied
    }

    public FluentIterable<E> zip(final Iterable<? extends E> other) {
        return of(IterableUtils.zippingIterable(iterable, other)); // No mutation applied
    }

    public FluentIterable<E> zip(final Iterable<? extends E>... others) {
        return of(IterableUtils.zippingIterable(iterable, others)); // No mutation applied
    }
}