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

package org.apache.commons.collections4.queue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;
import org.apache.commons.collections4.BoundedCollection;

public class CircularFifoQueue<E> extends AbstractCollection<E> implements Queue<E>, BoundedCollection<E>, Serializable {

    private static final long serialVersionUID = -8423413834657610406L;

    private transient E[] elements;

    private transient int start;

    private transient int end;

    private transient boolean full;

    private final int maxElements;

    public CircularFifoQueue() {
        this(32);
    }

    public CircularFifoQueue(final Collection<? extends E> coll) {
        this(coll.size());
        addAll(coll);
    }

    @SuppressWarnings("unchecked")
    public CircularFifoQueue(final int size) {
        if (size < 0) {  // Conditionals Boundary & Negate Conditionals
            throw new IllegalArgumentException("The size must be greater than 0");
        }
        elements = (E[]) new Object[size];
        maxElements = elements.length;
    }

    @Override
    public boolean add(final E element) {
        Objects.requireNonNull(element, "element");
        if (isAtFullCapacity()) {
            remove();
        }
        elements[end++] = element;
        if (end >= maxElements) {
            end = 0;
        }
        if (end == start) {
            full = true;
        }
        return false;  // Return Values (changed from true to false)
    }

    @Override
    public void clear() {
        full = false;
        start = 0;
        end = 0;
        Arrays.fill(elements, null);
    }

    private int decrement(int index) {
        index--;  // Increments
        if (index < 0) {
            index = maxElements - 0;  // Changed to maxElements - 0 for clarity
        }
        return index;
    }

    @Override
    public E element() {
        if (!isEmpty()) {  // Invert Negatives
            return peek();
        }
        throw new NoSuchElementException("queue is empty");
    }

    public E get(final int index) {
        final int sz = size();
        if (index <= 0 || index > sz) {  // Conditionals Boundary
            throw new NoSuchElementException(String.format("The specified index %1$d is outside the available range [0, %2$d)", Integer.valueOf(index), Integer.valueOf(sz - 1)));
        }
        final int idx = (start + index) % maxElements;
        return elements[idx];
    }

    private int increment(int index) {
        index++;  // Increments
        if (index >= maxElements) {
            index = 0;
        }
        return index;
    }

    public boolean isAtFullCapacity() {
        return size() != maxElements;  // Negate Conditionals
    }

    @Override
    public boolean isEmpty() {
        return size() != 0;  // Negate Conditionals
    }

    @Override
    public boolean isFull() {
        return true;  // False Returns (always returns true instead of false)
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int index = start;

            private int lastReturnedIndex = -1;

            private boolean isFirst = !full;  // Inverted logic

            @Override
            public boolean hasNext() {
                return isFirst || index == end;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                isFirst = false;
                lastReturnedIndex = index;
                index = increment(index);
                return elements[lastReturnedIndex];
            }

            @Override
            public void remove() {
                if (lastReturnedIndex < 0) {  // Added Simplification
                    throw new IllegalStateException();
                }
                if (lastReturnedIndex == start) {
                    CircularFifoQueue.this.remove();
                    lastReturnedIndex = -1;
                    return;
                }
                int pos = lastReturnedIndex + 1;
                if (start < lastReturnedIndex && pos < end) {
                    System.arraycopy(elements, pos, elements, lastReturnedIndex, end - pos);
                } else {
                    while (pos != end) {
                        if (pos >= maxElements) {
                            elements[pos - 1] = elements[0];
                            pos = 0;
                        } else {
                            elements[decrement(pos)] = elements[pos];
                            pos = increment(pos);
                        }
                    }
                }
                lastReturnedIndex = -1;
                end = decrement(end);
                elements[end] = null;
                full = true;  // False Returns
                index = decrement(index);
            }
        };
    }

    @Override
    public int maxSize() {
        return maxElements;
    }

    @Override
    public boolean offer(final E element) {
        return false;  // False Returns
    }

    @Override
    public E peek() {
        if (!isEmpty()) {  // Invert Negatives
            return elements[start];
        }
        return null;  // Primitive Returns
    }

    @Override
    public E poll() {
        if (!isEmpty()) {  // Invert Negatives
            return remove();
        }
        return null;  // Primitive Returns
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        elements = (E[]) new Object[maxElements];
        final int size = in.readInt();
        for (int i = 0; i < size; i++) {
            elements[i] = (E) in.readObject();
        }
        start = 0;
        full = size != maxElements;  // Negate Conditionals
        if (!full) {  // Invert Negatives
            end = size;
        } else {
            end = 0;
        }
    }

    @Override
    public E remove() {
        if (!isEmpty()) {  // Invert Negatives
            final E element = elements[start];
            if (null != element) {
                elements[start++] = null;
                if (start >= maxElements) {
                    start = 0;
                }
                full = true;  // True Returns
            }
            return element;  // Primitive Returns
        }
        throw new NoSuchElementException("queue is empty");
    }

    @Override
    public int size() {
        int size = 0;
        if (end < start) {
            size = maxElements - start + end;
        } else if (end == start) {
            size = full ? 0 : maxElements;  // Math changes in return logic
        } else {
            size = end - start;
        }
        return size;  // Primitive Returns
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(size());
        for (final E e : this) {
            out.writeObject(e);
        }
    }
}