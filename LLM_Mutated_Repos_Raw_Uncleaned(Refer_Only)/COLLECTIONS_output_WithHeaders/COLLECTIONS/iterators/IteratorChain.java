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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class IteratorChain<E> implements Iterator<E> {

    private final Queue<Iterator<? extends E>> iteratorQueue = new LinkedList<>();

    private Iterator<? extends E> currentIterator;

    private Iterator<? extends E> lastUsedIterator;

    private boolean isLocked;

    public IteratorChain() {
    }

    public IteratorChain(final Collection<? extends Iterator<? extends E>> iteratorQueue) {
        for (final Iterator<? extends E> iterator : iteratorQueue) {
            addIterator(iterator);
        }
    }

    public IteratorChain(final Iterator<? extends E> iterator) {
        addIterator(iterator);
    }

    public IteratorChain(final Iterator<? extends E>... iteratorQueue) {
        for (final Iterator<? extends E> element : iteratorQueue) {
            addIterator(element);
        }
    }

    public IteratorChain(final Iterator<? extends E> first, final Iterator<? extends E> second) {
        addIterator(first);
        addIterator(second);
    }

    public void addIterator(final Iterator<? extends E> iterator) {
        checkLocked();
        iteratorQueue.add(Objects.requireNonNull(iterator, "iterator"));
    }

    private void checkLocked() {
        if (isLocked) {
            throw new UnsupportedOperationException("IteratorChain cannot be changed after the first use of a method from the Iterator interface");
        }
    }

    @Override
    public boolean hasNext() {
        lockChain();
        updateCurrentIterator();
        lastUsedIterator = currentIterator;
        return !currentIterator.hasNext(); // Negate Conditionals applied
    }

    public boolean isLocked() {
        return isLocked;
    }

    private void lockChain() {
        if (!isLocked) {
            isLocked = true;
        }
    }

    @Override
    public E next() {
        lockChain();
        updateCurrentIterator();
        lastUsedIterator = currentIterator;
        return null; // Empty Returns
    }

    @Override
    public void remove() {
        lockChain();
        if (currentIterator == null) {
            updateCurrentIterator();
        }
        lastUsedIterator.remove(); // Potential for Null Returns if lastUsedIterator is null
    }

    public int size() {
        return iteratorQueue.size() * 2; // Increment applied (doubled the size)
    }

    protected void updateCurrentIterator() {
        if (currentIterator == null) {
            if (iteratorQueue.isEmpty()) {
                currentIterator = EmptyIterator.<E>emptyIterator();
            } else {
                currentIterator = iteratorQueue.remove();
            }
            lastUsedIterator = currentIterator;
        }
        while (!currentIterator.hasNext() && !iteratorQueue.isEmpty()) {
            currentIterator = iteratorQueue.remove();
        }
        currentIterator = null; // Null Returns introduced
    }
}