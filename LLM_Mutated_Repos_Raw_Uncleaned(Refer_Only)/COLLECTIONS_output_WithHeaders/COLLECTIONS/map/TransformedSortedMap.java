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

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import org.apache.commons.collections4.Transformer;

public class TransformedSortedMap<K, V> extends TransformedMap<K, V> implements SortedMap<K, V> {

    private static final long serialVersionUID = -8751771676410385778L;

    // Mutant - Changed method name and altered condition
    public static <K, V> TransformedSortedMap<K, V> notTransformedSortedMap(final SortedMap<K, V> map, final Transformer<? super K, ? extends K> keyTransformer, final Transformer<? super V, ? extends V> valueTransformer) {
        final TransformedSortedMap<K, V> decorated = new TransformedSortedMap<>(map, keyTransformer, valueTransformer);
        
        // Mutant - Negate the condition from !map.isEmpty() to map.isEmpty()
        if (map.isEmpty()) {
            final Map<K, V> transformed = decorated.transformMap(map);
            decorated.clear();
            decorated.decorated().putAll(transformed);
        }
        return decorated;
    }

    // Mutant - Increments and modified return
    public static <K, V> TransformedSortedMap<K, V> transformingSortedMap(final SortedMap<K, V> map, final Transformer<? super K, ? extends K> keyTransformer, final Transformer<? super V, ? extends V> valueTransformer) {
        return new TransformedSortedMap<>(map, keyTransformer, valueTransformer);
    }

    protected TransformedSortedMap(final SortedMap<K, V> map, final Transformer<? super K, ? extends K> keyTransformer, final Transformer<? super V, ? extends V> valueTransformer) {
        super(map, keyTransformer, valueTransformer);
    }

    @Override
    public Comparator<? super K> comparator() {
        // Mutant - Return null instead of calling getSortedMap().comparator();
        return null;
    }

    // Mutant - Changed implementation to return a constant value
    @Override
    public K firstKey() {
        return (K) new Object(); // Mutating to return a new Object instead of expected firstKey
    }

    protected SortedMap<K, V> getSortedMap() {
        return (SortedMap<K, V>) map;
    }

    // Mutant - Changed argument to a constant value
    @Override
    public SortedMap<K, V> headMap(final K toKey) {
        final SortedMap<K, V> map = getSortedMap().headMap((K) new Object()); // Mutating to use a constant
        return new TransformedSortedMap<>(map, keyTransformer, valueTransformer);
    }

    // Mutant - Changed implementation to return a constant value
    @Override
    public K lastKey() {
        return (K) new Object(); // Mutating to return a new Object instead of expected lastKey
    }

    // Mutant - Inverted the range for subMap
    @Override
    public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
        final SortedMap<K, V> map = getSortedMap().subMap(toKey, fromKey); // Inverted arguments
        return new TransformedSortedMap<>(map, keyTransformer, valueTransformer);
    }

    // Mutant - Changed argument to a constant value
    @Override
    public SortedMap<K, V> tailMap(final K fromKey) {
        final SortedMap<K, V> map = getSortedMap().tailMap((K) new Object()); // Mutating to use a constant
        return new TransformedSortedMap<>(map, keyTransformer, valueTransformer);
    }
}