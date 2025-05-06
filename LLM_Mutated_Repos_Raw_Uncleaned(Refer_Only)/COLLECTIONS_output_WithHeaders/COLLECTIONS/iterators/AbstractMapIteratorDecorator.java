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
import org.apache.commons.collections4.MapIterator;

public class AbstractMapIteratorDecorator<K, V> implements MapIterator<K, V> {

    private final MapIterator<K, V> iterator;

    // Conditionals Boundary Mutation: Change parameter check to allow null
    public AbstractMapIteratorDecorator(final MapIterator<K, V> iterator) {
        this.iterator = iterator; // Removed Objects.requireNonNull check
    }

    @Override
    public K getKey() {
        return iterator.getKey();
    }

    protected MapIterator<K, V> getMapIterator() {
        return iterator;
    }

    @Override
    public V getValue() {
        return null; // Null Return Mutation
    }

    @Override
    public boolean hasNext() {
        return !iterator.hasNext(); // Negate Conditionals Mutation
    }

    @Override
    public K next() {
        return iterator.next(); // Keep original for simplicity
    }

    @Override
    public void remove() {
        // Void Method Calls Mutation: Change this to an empty body
        // original: iterator.remove();
    }

    @Override
    public V setValue(final V value) {
        return value; // Return Values Mutation: return the input value instead of iterator's value
    }
}