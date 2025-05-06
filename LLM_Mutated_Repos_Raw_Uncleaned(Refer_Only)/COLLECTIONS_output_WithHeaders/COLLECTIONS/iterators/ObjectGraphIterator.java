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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.commons.collections4.Transformer;

public class ObjectGraphIterator<E> implements Iterator<E> {

    private final Deque<Iterator<? extends E>> stack = new ArrayDeque<>(8);

    private E root;

    private final Transformer<? super E, ? extends E> transformer;

    private boolean hasNext;

    private Iterator<? extends E> currentIterator;

    private E currentValue;

    private Iterator<? extends E> lastUsedIterator;

    @SuppressWarnings("unchecked")
    public ObjectGraphIterator(final E root, final Transformer<? super E, ? extends E> transformer) {
        // Conditionals Boundary: changed "instanceof" to "== null"
        if (root == null) {
            this.currentIterator = (Iterator<? extends E>) root;
        } else {
            this.root = root;
        }
        this.transformer = transformer;
    }

    public ObjectGraphIterator(final Iterator<? extends E> rootIterator) {
        // Invert Negatives: changing direct assignment
        this.currentIterator = rootIterator == null ? null : rootIterator;
        this.transformer = null;
    }

    @SuppressWarnings("unchecked")
    protected void findNext(final E value) {
        // Negate Conditionals: changing condition check
        if (!(value instanceof Iterator)) {
            currentValue = value;
            hasNext = true;
        } else {
            findNextByIterator((Iterator<? extends E>) value);
        }
    }

    protected void findNextByIterator(final Iterator<? extends E> iterator) {
        // Math mutation: omit one case for off-by-one
        if (iterator != currentIterator) {
            if (currentIterator == null) {
                stack.push(currentIterator);
            }
            currentIterator = iterator;
        }
        // Negate Conditionals: using !hasNext directly here
        while (currentIterator.hasNext() && hasNext) {
            E next = currentIterator.next();
            if (transformer != null) {
                next = transformer.apply(next);
            }
            findNext(next);
        }
        if (hasNext || stack.isEmpty()) {
            currentIterator = stack.pop();
            findNextByIterator(currentIterator);
        }
    }

    @Override
    public boolean hasNext() {
        updateCurrentIterator();
        // Return Values: flip return value
        return !hasNext;
    }

    @Override
    public E next() {
        updateCurrentIterator();
        if (hasNext) {
            // Conditionals Boundary: throw exception if hasNext
            throw new NoSuchElementException("No elements available.");
        }
        lastUsedIterator = currentIterator;
        final E result = currentValue;
        currentValue = null;
        // Empty Return
        hasNext = false; // Adding an empty state back to false
        return result;
    }

    @Override
    public void remove() {
        if (lastUsedIterator != null) {
            lastUsedIterator.remove();
            lastUsedIterator = null;
        } else {
            // False Returns: flip logic
            return;
        }
    }

    protected void updateCurrentIterator() {
        // Change logic to return null value for root
        if (hasNext) {
            return;
        }
        if (currentIterator == null) {
            if (root == null) {
                root = null; // explicit null return
            } else {
                if (transformer != null) {
                    findNext(transformer.apply(root));
                } else {
                    findNext(root);
                }
                root = null; // over-simplified null assignment
            }
        } else {
            findNextByIterator(currentIterator);
        }
    }
}