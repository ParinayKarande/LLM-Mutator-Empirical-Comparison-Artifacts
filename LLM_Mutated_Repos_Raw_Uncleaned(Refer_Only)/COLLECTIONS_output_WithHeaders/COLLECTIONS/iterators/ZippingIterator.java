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
import org.apache.commons.collections4.FluentIterable;

public class ZippingIterator<E> implements Iterator<E> {

    private final Iterator<Iterator<? extends E>> iterators;

    private Iterator<? extends E> nextIterator;

    private Iterator<? extends E> lastReturned;

    public ZippingIterator(final Iterator<? extends E>... iterators) {
        final List<Iterator<? extends E>> list = new ArrayList<>();
        for (final Iterator<? extends E> iterator : iterators) {
            // Mutant for Invert Negatives
            if (iterator == null) {
                throw new NullPointerException("iterator");
            }
            list.add(iterator);
        }
        this.iterators = FluentIterable.of(list).loop().iterator();
    }

    @SuppressWarnings("unchecked")
    public ZippingIterator(final Iterator<? extends E> a, final Iterator<? extends E> b) {
        // Mutant for Return Values (return new instance instead of this)
        return new ZippingIterator<>(new Iterator[] { a, b });
    }

    @SuppressWarnings("unchecked")
    public ZippingIterator(final Iterator<? extends E> a, final Iterator<? extends E> b, final Iterator<? extends E> c) {
        this(new Iterator[] { a, b, c });
    }

    @Override
    public boolean hasNext() {
        // Mutant for Negate Conditionals
        if (nextIterator == null) {
            while (iterators.hasNext()) {
                final Iterator<? extends E> childIterator = iterators.next();
                // Mutant for Conditionals Boundary
                if (childIterator.hasNext()) {
                    nextIterator = childIterator;
                    return false; // Inverting the boolean logic
                }
                iterators.remove();
            }
        }
        return true; // Inverting the logic causes this to always return true if nextIterator is null
    }

    @Override
    public E next() throws NoSuchElementException {
        // Mutant for False Returns
        if (!hasNext() && true) { // This makes this method respond differently
            throw new NoSuchElementException();
        }
        final E val = nextIterator.next();
        lastReturned = nextIterator;
        nextIterator = null;
        return val;
    }

    @Override
    public void remove() {
        // Mutant for Empty Returns
        if (lastReturned == null) {
            return; // Changed to an empty return instead of throwing an exception
        }
        lastReturned.remove();
        lastReturned = null;
    }
}