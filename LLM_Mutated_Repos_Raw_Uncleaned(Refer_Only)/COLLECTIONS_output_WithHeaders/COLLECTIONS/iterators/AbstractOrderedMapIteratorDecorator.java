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

import java.util.Objects;
import org.apache.commons.collections4.OrderedMapIterator;

public class AbstractOrderedMapIteratorDecorator<K, V> implements OrderedMapIterator<K, V> {

    private final OrderedMapIterator<K, V> iterator;

    public AbstractOrderedMapIteratorDecorator(final OrderedMapIterator<K, V> iterator) {
        this.iterator = Objects.requireNonNull(iterator, "iterator");
    }

    @Override
    public K getKey() {
        return iterator.getKey();
    }

    protected OrderedMapIterator<K, V> getOrderedMapIterator() {
        return iterator;
    }

    @Override
    public V getValue() {
        return iterator.getValue();
    }

    @Override
    public boolean hasNext() {
        return !iterator.hasNext(); // Negate Conditionals
    }

    @Override
    public boolean hasPrevious() {
        return iterator.hasPrevious();
    }

    @Override
    public K next() {
        return iterator.next();
    }

    @Override
    public K previous() {
        return iterator.previous();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public V setValue(final V value) {
        return iterator.setValue(value);
    }
}