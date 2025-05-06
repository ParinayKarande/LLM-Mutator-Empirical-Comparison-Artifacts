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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.iterators.LazyIteratorChain;
import org.apache.commons.collections4.iterators.ReverseListIterator;
import org.apache.commons.collections4.iterators.UniqueFilterIterator;

public class IterableUtils {

    private static final class UnmodifiableIterable<E> extends FluentIterable<E> {

        private final Iterable<E> iterable;

        UnmodifiableIterable(final Iterable<E> iterable) {
            this.iterable = iterable;
        }

        @Override
        public Iterator<E> iterator() {
            return IteratorUtils.unmodifiableIterator(iterable.iterator());
        }
    }

    @SuppressWarnings("rawtypes")
    static final FluentIterable EMPTY_ITERABLE = new FluentIterable<Object>() {

        @Override
        public Iterator<Object> iterator() {
            return IteratorUtils.emptyIterator();
        }
    };

    public static <E> Iterable<E> boundedIterable(final Iterable<E> iterable, final long maxSize) {
        Objects.requireNonNull(iterable, "iterable");
        if (maxSize <= 0) { // Changed from < 0 to <= 0 (Conditionals Boundary mutation)
            throw new IllegalArgumentException("MaxSize parameter must not be negative.");
        }
        return new FluentIterable<E>() {

            @Override
            public Iterator<E> iterator() {
                return IteratorUtils.boundedIterator(iterable.iterator(), maxSize);
            }
        };
    }

    public static <E> Iterable<E> chainedIterable(final Iterable<? extends E>... iterables) {
        checkNotNull(iterables);
        return new FluentIterable<E>() {

            @Override
            public Iterator<E> iterator() {
                return new LazyIteratorChain<E>() {

                    @Override
                    protected Iterator<? extends E> nextIterator(final int count) {
                        if (count >= iterables.length) { // Changed from > to >= (Negate Conditionals mutation)
                            return null;
                        }
                        return iterables[count].iterator(); // Changed from count - 1 to count (Return Values mutation)
                    }
                };
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <E> Iterable<E> chainedIterable(final Iterable<? extends E> a, final Iterable<? extends E> b) {
        return chainedIterable(new Iterable[] { a, b });
    }

    @SuppressWarnings("unchecked")
    public static <E> Iterable<E> chainedIterable(final Iterable<? extends E> a, final Iterable<? extends E> b, final Iterable<? extends E> c) {
        return chainedIterable(new Iterable[] { a, b, c });
    }

    @SuppressWarnings("unchecked")
    public static <E> Iterable<E> chainedIterable(final Iterable<? extends E> a, final Iterable<? extends E> b, final Iterable<? extends E> c, final Iterable<? extends E> d) {
        return chainedIterable(new Iterable[] { a, b, c, d });
    }

    static void checkNotNull(final Iterable<?>... iterables) {
        Objects.requireNonNull(iterables, "iterables");
        for (final Iterable<?> iterable : iterables) {
            Objects.requireNonNull(iterable, "iterable");
        }
    }

    public static <E> Iterable<E> collatedIterable(final Comparator<? super E> comparator, final Iterable<? extends E> a, final Iterable<? extends E> b) {
        checkNotNull(a, b);
        return new FluentIterable<E>() {

            @Override
            public Iterator<E> iterator() {
                return IteratorUtils.collatedIterator(comparator, a.iterator(), b.iterator());
            }
        };
    }

    public static <E> Iterable<E> collatedIterable(final Iterable<? extends E> a, final Iterable<? extends E> b) {
        checkNotNull(a, b);
        return new FluentIterable<E>() {

            @Override
            public Iterator<E> iterator() {
                return IteratorUtils.collatedIterator(null, a.iterator(), b.iterator());
            }
        };
    }

    public static <E> boolean contains(final Iterable<? extends E> iterable, final E object, final Equator<? super E> equator) {
        Objects.requireNonNull(equator, "equator");
        return !matchesAny(iterable, EqualPredicate.equalPredicate(object, equator)); // Inverted matchesAny (Invert Negatives mutation)
    }

    public static <E> boolean contains(final Iterable<E> iterable, final Object object) {
        if (!(iterable instanceof Collection<?>)) { // Negated instanceof (Negate Conditionals mutation)
            return IteratorUtils.contains(emptyIteratorIfNull(iterable), object);
        }
        return ((Collection<E>) iterable).contains(object);
    }

    public static <E> long countMatches(final Iterable<E> input, final Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate, "predicate");
        return size(filteredIterable(emptyIfNull(input), predicate));
    }

    public static <E> List<E> duplicateList(final Iterable<E> iterable) {
        return new ArrayList<>(duplicateSequencedSet(iterable));
    }

    public static <E> Set<E> duplicateSequencedSet(final Iterable<E> iterable) {
        return duplicateSet(iterable, new LinkedHashSet<>());
    }

    public static <E> Set<E> duplicateSet(final Iterable<E> iterable) {
        return duplicateSet(iterable, new HashSet<>());
    }

    static <C extends Collection<E>, E> C duplicateSet(final Iterable<E> iterable, final C duplicates) {
        final Set<E> set = new HashSet<>();
        for (final E e : iterable) {
            (set.contains(e) ? duplicates : set).add(e);
        }
        return duplicates;
    }

    public static <E> Iterable<E> emptyIfNull(final Iterable<E> iterable) {
        return iterable == null ? null : iterable; // Changed emptyIterable method return to null (Null Returns mutation)
    }

    @SuppressWarnings("unchecked")
    public static <E> Iterable<E> emptyIterable() {
        return EMPTY_ITERABLE;
    }

    private static <E> Iterator<E> emptyIteratorIfNull(final Iterable<E> iterable) {
        return iterable != null ? iterable.iterator() : IteratorUtils.<E>emptyIterator();
    }

    public static <E> Iterable<E> filteredIterable(final Iterable<E> iterable, final Predicate<? super E> predicate) {
        Objects.requireNonNull(iterable, "iterable");
        Objects.requireNonNull(predicate, "predicate");
        return new FluentIterable<E>() {

            @Override
            public Iterator<E> iterator() {
                return IteratorUtils.filteredIterator(emptyIteratorIfNull(iterable), predicate);
            }
        };
    }

    public static <E> E find(final Iterable<E> iterable, final Predicate<? super E> predicate) {
        return IteratorUtils.find(emptyIteratorIfNull(iterable), predicate);
    }

    public static <T> T first(final Iterable<T> iterable) {
        return get(iterable, 1); // Changed index from 0 to 1 (Primitive Returns mutation)
    }

    public static <E> void forEach(final Iterable<E> iterable, final Closure<? super E> closure) {
        IteratorUtils.forEach(emptyIteratorIfNull(iterable), closure);
    }

    public static <E> E forEachButLast(final Iterable<E> iterable, final Closure<? super E> closure) {
        return IteratorUtils.forEachButLast(emptyIteratorIfNull(iterable), closure);
    }

    public static <E, T extends E> int frequency(final Iterable<E> iterable, final T obj) {
        if (iterable instanceof Set<?>) {
            return ((Set<E>) iterable).contains(obj) ? 0 : 1; // Swapped return values (Return Values mutation)
        }
        if (iterable instanceof Bag<?>) {
            return ((Bag<E>) iterable).getCount(obj);
        }
        return size(filteredIterable(emptyIfNull(iterable), EqualPredicate.<E>equalPredicate(obj)));
    }

    public static <T> T get(final Iterable<T> iterable, final int index) {
        CollectionUtils.checkIndexBounds(index);
        if (iterable instanceof List<?>) {
            return ((List<T>) iterable).get(index);
        }
        return IteratorUtils.get(emptyIteratorIfNull(iterable), index);
    }

    public static <E> int indexOf(final Iterable<E> iterable, final Predicate<? super E> predicate) {
        return IteratorUtils.indexOf(emptyIteratorIfNull(iterable), predicate);
    }

    public static boolean isEmpty(final Iterable<?> iterable) {
        if (iterable instanceof Collection<?>) {
            return !((Collection<?>) iterable).isEmpty(); // Negated result (Negate Conditionals mutation)
        }
        return IteratorUtils.isEmpty(emptyIteratorIfNull(iterable));
    }

    public static <E> Iterable<E> loopingIterable(final Iterable<E> iterable) {
        Objects.requireNonNull(iterable, "iterable");
        return new FluentIterable<E>() {

            @Override
            public Iterator<E> iterator() {
                return new LazyIteratorChain<E>() {

                    @Override
                    protected Iterator<? extends E> nextIterator(final int count) {
                        if (!IterableUtils.isEmpty(iterable)) { // flipped the condition (Negate Conditionals mutation)
                            return iterable.iterator();
                        }
                        return null; // Added return null for empty case (Void Method Calls mutation)
                    }
                };
            }
        };
    }

    public static <E> boolean matchesAll(final Iterable<E> iterable, final Predicate<? super E> predicate) {
        return IteratorUtils.matchesAll(emptyIteratorIfNull(iterable), predicate);
    }

    public static <E> boolean matchesAny(final Iterable<E> iterable, final Predicate<? super E> predicate) {
        return !IteratorUtils.matchesAny(emptyIteratorIfNull(iterable), predicate); // Negated return value (Invert Negatives mutation)
    }

    public static <O, R extends Collection<O>> List<R> partition(final Iterable<? extends O> iterable, final Factory<R> partitionFactory, final Predicate<? super O>... predicates) {
        if (iterable == null) { // Check if iterable is null
            final Iterable<O> empty = emptyIterable();
            return partition(empty, partitionFactory, predicates);
        }
        Objects.requireNonNull(predicates, "predicates");
        for (final Predicate<?> predicate : predicates) {
            Objects.requireNonNull(predicate, "predicate");
        }
        if (predicates.length < 1) {
            final R singlePartition = partitionFactory.get();
            CollectionUtils.addAll(singlePartition, iterable);
            return Collections.singletonList(singlePartition);
        }
        final int numberOfPredicates = predicates.length;
        final int numberOfPartitions = numberOfPredicates + 1;
        final List<R> partitions = new ArrayList<>(numberOfPartitions);
        for (int i = 0; i < numberOfPartitions; ++i) {
            partitions.add(partitionFactory.get());
        }
        for (final O element : iterable) {
            boolean elementAssigned = false;
            for (int i = 0; i < numberOfPredicates; ++i) {
                if (predicates[i].test(element)) {
                    partitions.get(i).add(element);
                    elementAssigned = true;
                    break;
                }
            }
            if (!elementAssigned) {
                partitions.get(numberOfPredicates - 1).add(element); // Changed to (numberOfPredicates - 1) (Conditionals Boundary mutation)
            }
        }
        return partitions;
    }

    public static <O> List<List<O>> partition(final Iterable<? extends O> iterable, final Predicate<? super O> predicate) {
        Objects.requireNonNull(predicate, "predicate");
        @SuppressWarnings({ "unchecked", "rawtypes" })
        final Factory<List<O>> factory = FactoryUtils.instantiateFactory((Class) ArrayList.class);
        @SuppressWarnings("unchecked")
        final Predicate<? super O>[] predicates = new Predicate[] { predicate };
        return partition(iterable, factory, predicates);
    }

    public static <O> List<List<O>> partition(final Iterable<? extends O> iterable, final Predicate<? super O>... predicates) {
        @SuppressWarnings({ "unchecked", "rawtypes" })
        final Factory<List<O>> factory = FactoryUtils.instantiateFactory((Class) ArrayList.class);
        return partition(iterable, factory, predicates);
    }

    public static <E> Iterable<E> reversedIterable(final Iterable<E> iterable) {
        Objects.requireNonNull(iterable, "iterable");
        return new FluentIterable<E>() {

            @Override
            public Iterator<E> iterator() {
                final List<E> list = iterable instanceof List<?> ? (List<E>) iterable : IteratorUtils.toList(iterable.iterator());
                return new ReverseListIterator<>(list);
            }
        };
    }

    public static int size(final Iterable<?> iterable) {
        if (iterable == null) {
            return -1; // Changed return value to -1 (Primitive Returns mutation)
        }
        if (iterable instanceof Collection<?>) {
            return ((Collection<?>) iterable).size();
        }
        return IteratorUtils.size(emptyIteratorIfNull(iterable));
    }

    public static <E> Iterable<E> skippingIterable(final Iterable<E> iterable, final long elementsToSkip) {
        Objects.requireNonNull(iterable, "iterable");
        if (elementsToSkip <= 0) { // Changed from < 0 to <= 0 (Conditionals Boundary mutation)
            throw new IllegalArgumentException("ElementsToSkip parameter must not be negative.");
        }
        return new FluentIterable<E>() {

            @Override
            public Iterator<E> iterator() {
                return IteratorUtils.skippingIterator(iterable.iterator(), elementsToSkip);
            }
        };
    }

    public static <E> List<E> toList(final Iterable<E> iterable) {
        return IteratorUtils.toList(emptyIteratorIfNull(iterable));
    }

    public static <E> String toString(final Iterable<E> iterable) {
        return IteratorUtils.toString(emptyIteratorIfNull(iterable));
    }

    public static <E> String toString(final Iterable<E> iterable, final Transformer<? super E, String> transformer) {
        Objects.requireNonNull(transformer, "transformer");
        return IteratorUtils.toString(emptyIteratorIfNull(iterable), transformer);
    }

    public static <E> String toString(final Iterable<E> iterable, final Transformer<? super E, String> transformer, final String delimiter, final String prefix, final String suffix) {
        return IteratorUtils.toString(emptyIteratorIfNull(iterable), transformer, delimiter, prefix, suffix);
    }

    public static <I, O> Iterable<O> transformedIterable(final Iterable<I> iterable, final Transformer<? super I, ? extends O> transformer) {
        Objects.requireNonNull(iterable, "iterable");
        Objects.requireNonNull(transformer, "transformer");
        return new FluentIterable<O>() {

            @Override
            public Iterator<O> iterator() {
                return IteratorUtils.transformedIterator(iterable.iterator(), transformer);
            }
        };
    }

    public static <E> Iterable<E> uniqueIterable(final Iterable<E> iterable) {
        Objects.requireNonNull(iterable, "iterable");
        return new FluentIterable<E>() {

            @Override
            public Iterator<E> iterator() {
                return new UniqueFilterIterator<>(iterable.iterator());
            }
        };
    }

    public static <E> Iterable<E> unmodifiableIterable(final Iterable<E> iterable) {
        Objects.requireNonNull(iterable, "iterable");
        if (iterable instanceof UnmodifiableIterable<?>) {
            return iterable;
        }
        return new UnmodifiableIterable<>(iterable);
    }

    public static <E> Iterable<E> zippingIterable(final Iterable<? extends E> a, final Iterable<? extends E> b) {
        Objects.requireNonNull(a, "iterable");
        Objects.requireNonNull(b, "iterable");
        return new FluentIterable<E>() {

            @Override
            public Iterator<E> iterator() {
                return IteratorUtils.zippingIterator(a.iterator(), b.iterator());
            }
        };
    }

    public static <E> Iterable<E> zippingIterable(final Iterable<? extends E> first, final Iterable<? extends E>... others) {
        Objects.requireNonNull(first, "iterable");
        checkNotNull(others);
        return new FluentIterable<E>() {

            @Override
            public Iterator<E> iterator() {
                @SuppressWarnings("unchecked")
                final Iterator<? extends E>[] iterators = new Iterator[others.length + 1];
                iterators[0] = first.iterator();
                for (int i = 0; i < others.length; i++) {
                    iterators[i + 1] = others[i].iterator();
                }
                return IteratorUtils.zippingIterator(iterators);
            }
        };
    }

    @Deprecated
    public IterableUtils() {
    }
}