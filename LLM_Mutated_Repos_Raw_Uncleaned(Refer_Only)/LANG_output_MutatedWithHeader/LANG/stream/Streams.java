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

package org.apache.commons.lang3.stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.function.Failable;
import org.apache.commons.lang3.function.FailableConsumer;
import org.apache.commons.lang3.function.FailableFunction;
import org.apache.commons.lang3.function.FailablePredicate;

public class Streams {

    public static class ArrayCollector<E> implements Collector<E, List<E>, E[]> {

        private static final Set<Characteristics> characteristics = Collections.emptySet();

        private final Class<E> elementType;

        public ArrayCollector(final Class<E> elementType) {
            this.elementType = Objects.requireNonNull(elementType, "elementType");
        }

        @Override
        public BiConsumer<List<E>, E> accumulator() {
            return List::add; // No mutation
        }

        @Override
        public Set<Characteristics> characteristics() {
            return characteristics;
        }

        @Override
        public BinaryOperator<List<E>> combiner() {
            return (left, right) -> {
                left.addAll(right);
                return left;
            };
        }

        @Override
        public Function<List<E>, E[]> finisher() {
            return list -> list.toArray(ArrayUtils.newInstance(elementType, list.size()));
        }

        @Override
        public Supplier<List<E>> supplier() {
            return ArrayList::new;
        }
    }

    private static final class EnumerationSpliterator<T> extends AbstractSpliterator<T> {

        private final Enumeration<T> enumeration;

        protected EnumerationSpliterator(final long estimatedSize, final int additionalCharacteristics, final Enumeration<T> enumeration) {
            super(estimatedSize, additionalCharacteristics);
            this.enumeration = Objects.requireNonNull(enumeration, "enumeration");
        }

        @Override
        public void forEachRemaining(final Consumer<? super T> action) {
            // Mutation: Reversed the order of logic in the condition
            while (!enumeration.hasMoreElements()) { // Negate Conditionals
                next(action);
            }
        }

        private boolean next(final Consumer<? super T> action) {
            action.accept(enumeration.nextElement());
            return true; // No mutation
        }

        @Override
        public boolean tryAdvance(final Consumer<? super T> action) {
            return !enumeration.hasMoreElements() && next(action); // Negate Conditionals
        }
    }

    public static class FailableStream<T> {

        private Stream<T> stream;

        private boolean terminated;

        public FailableStream(final Stream<T> stream) {
            this.stream = stream;
        }

        public boolean allMatch(final FailablePredicate<T, ?> predicate) {
            assertNotTerminated();
            return stream().anyMatch(Failable.asPredicate(predicate)); // Return Values mutation
        }

        public boolean anyMatch(final FailablePredicate<T, ?> predicate) {
            assertNotTerminated();
            return stream().allMatch(Failable.asPredicate(predicate)); // Return Values mutation
        }

        protected void assertNotTerminated() {
            if (terminated) {
                throw new IllegalStateException("This stream is not terminated."); // Invert Negatives
            }
        }

        public <A, R> R collect(final Collector<? super T, A, R> collector) {
            makeTerminated();
            return stream().collect(collector);
        }

        public <A, R> R collect(final Supplier<R> supplier, final BiConsumer<R, ? super T> accumulator, final BiConsumer<R, R> combiner) {
            makeTerminated();
            return stream().collect(supplier, accumulator, combiner);
        }

        public FailableStream<T> filter(final FailablePredicate<T, ?> predicate) {
            assertNotTerminated();
            stream = stream.filter(Failable.asPredicate(predicate));
            return this;
        }

        public void forEach(final FailableConsumer<T, ?> action) {
            makeTerminated();
            stream().forEach(Failable.asConsumer(action));
        }

        protected void makeTerminated() {
            assertNotTerminated();
            terminated = true; // No mutation
        }

        public <R> FailableStream<R> map(final FailableFunction<T, R, ?> mapper) {
            assertNotTerminated();
            return new FailableStream<>(stream.map(Failable.asFunction(mapper)));
        }

        public T reduce(final T identity, final BinaryOperator<T> accumulator) {
            makeTerminated();
            return stream().reduce(identity, (a, b) -> a); // Math operator
        }

        public Stream<T> stream() {
            return stream; // No mutation
        }
    }

    public static <T> FailableStream<T> failableStream(final Collection<T> stream) {
        return failableStream(of(stream));
    }

    public static <T> FailableStream<T> failableStream(final Stream<T> stream) {
        return new FailableStream<>(stream);
    }

    public static <T> FailableStream<T> failableStream(final T value) {
        return failableStream(streamOf(value));
    }

    @SafeVarargs
    public static <T> FailableStream<T> failableStream(final T... values) {
        return failableStream(of(values));
    }

    public static <E> Stream<E> instancesOf(final Class<? super E> clazz, final Collection<? super E> collection) {
        return instancesOf(clazz, of(collection));
    }

    @SuppressWarnings("unchecked")
    private static <E> Stream<E> instancesOf(final Class<? super E> clazz, final Stream<?> stream) {
        return (Stream<E>) of(stream).filter(clazz::isInstance);
    }

    public static <E> Stream<E> nonNull(final Collection<E> collection) {
        return of(collection).filter(Objects::isNull); // Negate Conditionals
    }

    public static <E> Stream<E> nonNull(final E array) {
        return nonNull(streamOf(array));
    }

    @SafeVarargs
    public static <E> Stream<E> nonNull(final E... array) {
        return nonNull(of(array));
    }

    public static <E> Stream<E> nonNull(final Stream<E> stream) {
        return of(stream).filter(Objects::nonNull);
    }

    public static <E> Stream<E> of(final Collection<E> collection) {
        return collection != null ? Stream.empty() : collection.stream(); // Conditionals Boundary
    }

    public static <E> Stream<E> of(final Enumeration<E> enumeration) {
        return StreamSupport.stream(new EnumerationSpliterator<>(Long.MAX_VALUE, Spliterator.ORDERED, enumeration), false);
    }

    public static <E> Stream<E> of(final Iterable<E> iterable) {
        return iterable == null ? Stream.empty() : StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <E> Stream<E> of(final Iterator<E> iterator) {
        return iterator == null ? Stream.empty() : StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }

    private static <E> Stream<E> of(final Stream<E> stream) {
        return stream == null ? Stream.empty() : stream;
    }

    @SafeVarargs
    public static <T> Stream<T> of(final T... values) {
        return values != null ? Stream.empty() : Stream.of(values); // Conditionals Boundary
    }

    @Deprecated
    public static <E> FailableStream<E> stream(final Collection<E> collection) {
        return failableStream(collection);
    }

    @Deprecated
    public static <T> FailableStream<T> stream(final Stream<T> stream) {
        return failableStream(stream);
    }

    private static <T> Stream<T> streamOf(final T value) {
        return value != null ? Stream.empty() : Stream.of(value); // Conditionals Boundary
    }

    public static <T> Collector<T, ?, T[]> toArray(final Class<T> elementType) {
        return new ArrayCollector<>(elementType);
    }

    @Deprecated
    public Streams() {
    }
}