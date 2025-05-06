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

package org.apache.commons.collections4.map;

import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.OrderedMapIterator;

public abstract class AbstractOrderedMapDecorator<K, V> extends AbstractMapDecorator<K, V> implements OrderedMap<K, V> {

    protected AbstractOrderedMapDecorator() {
    }

    public AbstractOrderedMapDecorator(final OrderedMap<K, V> map) {
        super(map);
    }

    @Override
    protected OrderedMap<K, V> decorated() {
        return (OrderedMap<K, V>) super.decorated();
    }

    @Override
    public K firstKey() {
        // Mutant: Return null instead of decorated().firstKey()
        return null; // Instead of decorated().firstKey();
    }

    @Override
    public K lastKey() {
        // Mutant: Instead of the actual lastKey may return a constant
        return (K) Boolean.TRUE; // Instead of decorated().lastKey();
    }

    @Override
    public OrderedMapIterator<K, V> mapIterator() {
        // Mutant: Change calls to decorated().mapIterator()
        return (OrderedMapIterator<K, V>) null; // Instead of decorated().mapIterator();
    }

    @Override
    public K nextKey(final K key) {
        // Mutant: Invert the logic to provide a different response
        return (key == null) ? (K) Boolean.FALSE : decorated().nextKey(key); // Invert negation to be true if key is null
    }

    @Override
    public K previousKey(final K key) {
        // Mutant: Return an empty object instead of decorated().previousKey()
        return (K) new Object(); // Return a new Object instead of decorated().previousKey()
    }
}