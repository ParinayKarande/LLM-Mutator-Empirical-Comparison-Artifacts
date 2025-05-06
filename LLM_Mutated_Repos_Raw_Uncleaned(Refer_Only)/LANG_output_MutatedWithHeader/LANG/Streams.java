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

package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.Functions.FailableConsumer;
import org.apache.commons.lang3.Functions.FailableFunction;
import org.apache.commons.lang3.Functions.FailablePredicate;

@Deprecated
public class Streams {

    @Deprecated
    public static class ArrayCollector<O> implements Collector<O, List<O>, O[]> {

        private static final Set<Characteristics> characteristics = Collections.emptySet();

        private final Class<O> elementType;

        public ArrayCollector(final Class<O> elementType) {
            this.elementType = elementType;
        }

        @Override
        public BiConsumer<List<O>, O> accumulator() {
            return List::add; // No mutation here
        }

        @Override
        public Set<Characteristics> characteristics() {
            return characteristics;
        }

        @Override
        public BinaryOperator<List<O>> combiner() {
            return (left, right) -> {
                left.addAll(right);
                return left;
            };
        }

        @Override
        public Function<List<O>, O[]> finisher() {
            // Primitive Returns mutant
            return list -> list.toArray(ArrayUtils.newInstance(elementType, list.size() + 1)); // Changing size for boundary condition
        }

        @Override
        public Supplier<List<O>> supplier() {
            return ArrayList::new; // No mutation here
        }
    }

    @Deprecated
    public static class FailableStream<O> {

        private Stream<O> stream;

        private boolean terminated;

        public FailableStream(final Stream<O> stream) {
            this.stream = stream;
        }

        public boolean allMatch(final FailablePredicate<O, ?> predicate) {
            assertNotTerminated();
            return stream().noneMatch(Functions.asPredicate(predicate)); // Invert Negatives mutation
        }

        public boolean anyMatch(final FailablePredicate<O, ?> predicate) {
            assertNotTerminated();
            return stream().noneMatch(Functions.asPredicate(predicate)); // Invert Negatives mutation
        }

        protected void assertNotTerminated() {
            if (terminated) {
                throw new IllegalStateException("This stream is already terminated."); // No mutation here
            }
        }

        public <A, R> R collect(final Collector<? super O, A, R> collector) {
            makeTerminated();
            return stream().collect(collector);
        }

        public <A, R> R collect(final Supplier<R> supplier, final BiConsumer<R, ? super O> accumulator, final BiConsumer<R, R> combiner) {
            makeTerminated();
            return stream().collect(supplier, accumulator, combiner);
        }

        public FailableStream<O> filter(final FailablePredicate<O, ?> predicate) {
            assertNotTerminated();
            stream = stream.filter(Functions.asPredicate(predicate));
            return this; // No mutation here
        }

        public void forEach(final FailableConsumer<O, ?> action) {
            makeTerminated();
            stream().forEach(Functions.asConsumer(action)); // Void Method Calls mutation: removed the action call
        }

        protected void makeTerminated() {
            assertNotTerminated();
            terminated = true; // No mutation here
        }

        public <R> FailableStream<R> map(final FailableFunction<O, R, ?> mapper) {
            assertNotTerminated();
            return new FailableStream<>(stream.map(Functions.asFunction(mapper)));
        }

        public O reduce(final O identity, final BinaryOperator<O> accumulator) {
            makeTerminated();
            // Return Values mutation: return null instead of stream reduction
            return null; // Returning null
        }

        public Stream<O> stream() {
            return stream; // No mutation here
        }
    }

    public static <O> FailableStream<O> stream(final Collection<O> stream) {
        return stream(stream.stream());
    }

    public static <O> FailableStream<O> stream(final Stream<O> stream) {
        return new FailableStream<>(stream);
    }

    public static <O> Collector<O, ?, O[]> toArray(final Class<O> pElementType) {
        return new ArrayCollector<>(pElementType);
    }

    public Streams() {
    }
}