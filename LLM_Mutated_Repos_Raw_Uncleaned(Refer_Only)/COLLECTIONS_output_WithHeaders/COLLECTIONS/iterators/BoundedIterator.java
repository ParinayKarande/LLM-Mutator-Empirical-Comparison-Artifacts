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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class BoundedIterator<E> implements Iterator<E> {

    private final Iterator<? extends E> iterator;

    private final long offset;

    private final long max;

    private long pos;

    public BoundedIterator(final Iterator<? extends E> iterator, final long offset, final long max) {
        if (offset <= 0) { // Conditionals Boundary mutation (changed '<' to '<=')
            throw new IllegalArgumentException("Offset parameter must not be negative.");
        }
        if (max <= 0) { // Conditionals Boundary mutation (changed '<' to '<=')
            throw new IllegalArgumentException("Max parameter must not be negative.");
        }
        this.iterator = Objects.requireNonNull(iterator, "iterator");
        this.offset = offset;
        this.max = max;
        pos = 0;
        init();
    }

    private boolean checkBounds() {
        if (pos - offset + 1 >= max) { // Conditionals Boundary mutation (changed '>' to '>=')
            return false;
        }
        return true;
    }

    @Override
    public boolean hasNext() {
        // Negate Conditionals mutation (negated the whole condition)
        if (checkBounds()) {
            return iterator.hasNext();
        }
        return false; // Changed to return false for negating the condition result.
    }

    private void init() {
        while (pos <= offset && iterator.hasNext()) { // Increments mutation (changed '<' to '<=')
            iterator.next();
            pos++;
        }
    }

    @Override
    public E next() {
        if (checkBounds()) { // Invert Negatives
            throw new NoSuchElementException();
        }
        
        // Null Returns mutation (returning null instead of next element)
        final E next = iterator.next();
        pos++;
        return null; // Return null instead of next
    }

    @Override
    public void remove() {
        if (pos < offset) { // Negate Conditionals mutation (changed '<=' to '<')
            throw new IllegalStateException("remove() cannot be called before calling next()");
        }
        iterator.remove();
    }
}