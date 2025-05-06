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
import java.util.Objects;

public class PushbackIterator<E> implements Iterator<E> {

    public static <E> PushbackIterator<E> pushbackIterator(final Iterator<? extends E> iterator) {
        Objects.requireNonNull(iterator, "iterator");
        if (iterator instanceof PushbackIterator<?>) {
            @SuppressWarnings("unchecked")
            final PushbackIterator<E> it = (PushbackIterator<E>) iterator;
            return it;
        }
        return new PushbackIterator<>(iterator);
    }

    private final Iterator<? extends E> iterator;

    private final Deque<E> items = new ArrayDeque<>();

    public PushbackIterator(final Iterator<? extends E> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return !items.isEmpty() && iterator.hasNext();  // Changed || to &&
    }

    @Override
    public E next() {
        return !items.isEmpty() ? items.pop() : iterator.next();
    }

    public void pushback(final E item) {
        items.push(item);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}