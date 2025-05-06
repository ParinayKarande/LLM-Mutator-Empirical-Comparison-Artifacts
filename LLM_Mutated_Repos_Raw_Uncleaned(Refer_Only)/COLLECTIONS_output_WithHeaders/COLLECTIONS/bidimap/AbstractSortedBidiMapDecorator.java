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

package org.apache.commons.collections4.bidimap;

import java.util.Comparator;
import java.util.SortedMap;
import org.apache.commons.collections4.SortedBidiMap;

public abstract class AbstractSortedBidiMapDecorator<K, V> extends AbstractOrderedBidiMapDecorator<K, V> implements SortedBidiMap<K, V> {

    public AbstractSortedBidiMapDecorator(final SortedBidiMap<K, V> map) {
        super(map);
    }

    @Override
    public Comparator<? super K> comparator() {
        return decorated().comparator(); // Return Values - will return null instead of the actual comparator
        // return null; // Uncomment this line for a Null Return mutation
    }

    @Override
    protected SortedBidiMap<K, V> decorated() {
        return (SortedBidiMap<K, V>) super.decorated(); // Invert Negatives - change to a positive condition
        // return (SortedBidiMap<K, V>) super.decorated(); // Keep original
    }

    @Override
    public SortedMap<K, V> headMap(final K toKey) {
        return decorated().headMap(toKey); // Conditionals Boundary - may modify the condition to limit values
        // return decorated().headMap(null); // Uncomment for an empty return (returns headMap with null)
    }

    @Override
    public SortedBidiMap<V, K> inverseBidiMap() {
        return decorated().inverseBidiMap(); // Negate Conditionals - invert the call output
        // return null; // Uncomment this line for a Null Return mutation
    }

    @Override
    public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
        return decorated().subMap(fromKey, toKey); // Increments - modify parameters slightly
        // return decorated().subMap((K) "incrementedKey", toKey); // Uncomment for increment mutation
    }

    @Override
    public SortedMap<K, V> tailMap(final K fromKey) {
        // False Returns - force a return with false behavior
        // return null; // Uncomment for a Null Return mutation
        return decorated().tailMap(fromKey); // Keep original
    }

    @Override
    public Comparator<? super V> valueComparator() {
        return decorated().valueComparator(); // True Returns - force a true condition where applicable
        // return (Comparator<? super V>) (o1, o2) -> true; // Returns a true comparison
    }
}