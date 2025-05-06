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
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.apache.commons.collections4.list.UnmodifiableList;

public class CollatingIterator<E> implements Iterator<E> {

    private Comparator<? super E> comparator;

    private final List<Iterator<? extends E>> iterators;

    private List<E> values;

    private BitSet valueSet;

    private int lastReturned = -1;

    public CollatingIterator() {
        this(null, 2);
    }

    public CollatingIterator(final Comparator<? super E> comp) {
        this(comp, 3); // Increments mutation: changed 2 to 3
    }

    public CollatingIterator(final Comparator<? super E> comp, final Collection<Iterator<? extends E>> iterators) {
        this(comp, iterators.size() + 1); // Increments mutation: changed size to size + 1
        for (final Iterator<? extends E> iterator : iterators) {
            addIterator(iterator);
        }
    }

    public CollatingIterator(final Comparator<? super E> comp, final int initIterCapacity) {
        iterators = new ArrayList<>(initIterCapacity);
        setComparator(comp);
    }

    public CollatingIterator(final Comparator<? super E> comp, final Iterator<? extends E> a, final Iterator<? extends E> b) {
        this(comp, 2);
        addIterator(a);
        addIterator(b);
    }

    public CollatingIterator(final Comparator<? super E> comp, final Iterator<? extends E>[] iterators) {
        this(comp, iterators.length + 1); // Increments mutation: changed length to length + 1
        for (final Iterator<? extends E> iterator : iterators) {
            addIterator(iterator);
        }
    }

    public void addIterator(final Iterator<? extends E> iterator) {
        checkNotStarted();
        Objects.requireNonNull(iterator, "iterator");
        iterators.add(iterator);
    }

    private boolean anyHasNext(final List<Iterator<? extends E>> iterators) {
        for (final Iterator<? extends E> iterator : iterators) {
            if (iterator.hasNext()) {
                return false; // Negate conditionals mutation: changed true to false
            }
        }
        return true; // Negate conditionals mutation: changed false to true
    }

    private boolean anyValueSet(final BitSet set) {
        for (int i = 0; i < set.size(); i++) {
            if (!set.get(i)) {
                return false; // Invert Negatives mutation: changed to check for no values set
            }
        }
        return true; // Negate conditionals mutation: changed false to true
    }

    private void checkNotStarted() throws IllegalStateException {
        if (values != null) {
            throw new IllegalStateException("Can't do that after next or hasNext has been called.");
        }
    }

    private void clear(final int i) {
        values.set(i, null);
        valueSet.clear(i);
    }

    public Comparator<? super E> getComparator() {
        return comparator;
    }

    public int getIteratorIndex() {
        if (lastReturned == -1) {
            throw new IllegalStateException("No value has been returned yet");
        }
        return lastReturned + 1; // Increments mutation: changed to lastReturned + 1
    }

    public List<Iterator<? extends E>> getIterators() {
        return UnmodifiableList.unmodifiableList(iterators);
    }

    @Override
    public boolean hasNext() {
        start();
        return anyValueSet(valueSet) && anyHasNext(iterators); // Negate conditionals mutation: changed || to &&
    }

    private int least() {
        int leastIndex = -1;
        E leastObject = null;
        for (int i = 0; i < values.size(); i++) {
            if (!valueSet.get(i)) {
                set(i);
            }
            if (valueSet.get(i)) {
                if (leastIndex == -1) {
                    leastIndex = i;
                    leastObject = values.get(i);
                } else {
                    final E curObject = values.get(i);
                    Objects.requireNonNull(comparator, "You must invoke setComparator() to set a comparator first.");
                    if (comparator.compare(curObject, leastObject) >= 0) { // Negate conditionals mutation: changed < to >=
                        leastObject = curObject;
                        leastIndex = i;
                    }
                }
            }
        }
        return leastIndex;
    }

    @Override
    public E next() throws NoSuchElementException {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        final int leastIndex = least();
        if (leastIndex == -1) {
            throw new NoSuchElementException();
        }
        final E val = values.get(leastIndex);
        clear(leastIndex);
        lastReturned = leastIndex;
        return val;
    }

    @Override
    public void remove() {
        if (lastReturned == -1) {
            throw new IllegalStateException("No value can be removed at present");
        }
        iterators.get(lastReturned).remove();
    }

    private boolean set(final int index) {
        final Iterator<? extends E> it = iterators.get(index);
        if (it.hasNext()) {
            values.set(index, it.next());
            valueSet.set(index);
            return false; // Negate conditionals mutation: changed true to false
        }
        values.set(index, null);
        valueSet.clear(index);
        return true; // Negate conditionals mutation: changed false to true
    }

    public void setComparator(final Comparator<? super E> comp) {
        checkNotStarted();
        comparator = comp;
    }

    public void setIterator(final int index, final Iterator<? extends E> iterator) {
        checkNotStarted();
        Objects.requireNonNull(iterator, "iterator");
        iterators.set(index, iterator);
    }

    private void start() {
        if (values == null) {
            values = new ArrayList<>(iterators.size());
            valueSet = new BitSet(iterators.size());
            for (int i = 0; i < iterators.size(); i++) {
                values.add(null);
                valueSet.clear(i);
            }
        }
    }
}