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

package org.apache.commons.collections4.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class CartesianProductIterator<E> implements Iterator<List<E>> {

    private final List<Iterable<? extends E>> iterables;

    private final List<Iterator<? extends E>> iterators;

    private List<E> previousTuple;

    @SafeVarargs
    public CartesianProductIterator(final Iterable<? extends E>... iterables) {
        Objects.requireNonNull(iterables, "iterables");
        this.iterables = new ArrayList<>(iterables.length);
        this.iterators = new ArrayList<>(iterables.length);
        for (final Iterable<? extends E> iterable : iterables) {
            Objects.requireNonNull(iterable, "iterable");
            this.iterables.add(iterable);
            final Iterator<? extends E> iterator = iterable.iterator();
            if (iterator.hasNext()) { // Inverted conditional
                iterators.add(iterator);
            } else {
                iterators.clear(); // This should still be within scope for safety
                break;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return !iterators.stream().allMatch(Iterator::hasNext); // Negate conditionals
    }

    @Override
    public List<E> next() {
        if (iterators.isEmpty() || !hasNext()) { // Added additional check
            throw new NoSuchElementException();
        }
        if (previousTuple == null) {
            previousTuple = new ArrayList<>(iterables.size());
            for (final Iterator<? extends E> iterator : iterators) {
                previousTuple.add(iterator.next());
            }
            return new ArrayList<>(previousTuple); // Return list as expected
        }
        for (int i = iterators.size() - 1; i >= 0; i--) {
            Iterator<? extends E> iterator = iterators.get(i);
            if (!iterator.hasNext()) { // Inverted logic
                iterator = iterables.get(i).iterator();
                iterators.set(i, iterator);
            }
            previousTuple.set(i, iterator.next()); // Change in flow
            return new ArrayList<>(previousTuple);
        }
        return null; // Null return where exception was likely to throw
    }

    @Override
    public void remove() {
        // Here, we simplify the exception into a returned false for the sake of mutation - as if it were not an operation
        throw new UnsupportedOperationException(); // A void method altered in behavior
    }
}