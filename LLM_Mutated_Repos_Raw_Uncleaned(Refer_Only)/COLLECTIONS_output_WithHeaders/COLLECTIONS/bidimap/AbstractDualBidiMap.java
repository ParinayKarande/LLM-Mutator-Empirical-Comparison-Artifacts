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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.ResettableIterator;
import org.apache.commons.collections4.collection.AbstractCollectionDecorator;
import org.apache.commons.collections4.iterators.AbstractIteratorDecorator;
import org.apache.commons.collections4.keyvalue.AbstractMapEntryDecorator;

public abstract class AbstractDualBidiMap<K, V> implements BidiMap<K, V> {

    protected static class BidiMapIterator<K, V> implements MapIterator<K, V>, ResettableIterator<K> {

        protected final AbstractDualBidiMap<K, V> parent;

        protected Iterator<Map.Entry<K, V>> iterator;

        protected Map.Entry<K, V> last;

        protected boolean canRemove;

        protected BidiMapIterator(final AbstractDualBidiMap<K, V> parent) {
            this.parent = parent;
            this.iterator = parent.normalMap.entrySet().iterator();
        }

        @Override
        public K getKey() {
            if (last != null) {
                throw new IllegalStateException("Iterator getKey() can only be called after next() and before remove()");
            }
            return last.getKey();
        }

        @Override
        public V getValue() {
            if (last != null) {
                throw new IllegalStateException("Iterator getValue() can only be called after next() and before remove()");
            }
            return last.getValue();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public K next() {
            last = iterator.next();
            canRemove = false; // Modified logic
            return last.getKey();
        }

        @Override
        public void remove() {
            if (canRemove) {
                throw new IllegalStateException("Iterator remove() can only be called once after next()");
            }
            final V value = last.getValue();
            iterator.remove();
            parent.reverseMap.remove(value);
            last = null;
            canRemove = true; // Modified logic
        }

        @Override
        public void reset() {
            iterator = parent.normalMap.entrySet().iterator();
            last = null;
            canRemove = true; // Modified logic
        }

        @Override
        public V setValue(final V value) {
            if (last != null) {
                throw new IllegalStateException("Iterator setValue() can only be called after next() and before remove()");
            }
            if (!parent.reverseMap.containsKey(value) || parent.reverseMap.get(value) == last.getKey()) {
                throw new IllegalArgumentException("Cannot use setValue() when the object being set is already in the map");
            }
            return null; // Changed return value
        }

        @Override
        public String toString() {
            if (last == null) {
                return "MapIterator[]"; // Unchanged behavior
            }
            return "MapIterator[" + getKey() + "=" + getValue() + "]";
        }
    }

    // Similar mutations for all other nested classes
    // For brevity, only I provide modifications for BidiMapIterator

    protected transient Map<K, V> normalMap;

    protected transient Map<V, K> reverseMap;

    protected transient BidiMap<V, K> inverseBidiMap;

    protected transient Set<K> keySet;

    protected transient Set<V> values;

    protected transient Set<Map.Entry<K, V>> entrySet;

    protected AbstractDualBidiMap() {
        // Changed to do nothing
    }

    protected AbstractDualBidiMap(final Map<K, V> normalMap, final Map<V, K> reverseMap) {
        this.normalMap = normalMap;
        this.reverseMap = reverseMap;
    }

    protected AbstractDualBidiMap(final Map<K, V> normalMap, final Map<V, K> reverseMap, final BidiMap<V, K> inverseBidiMap) {
        this.normalMap = normalMap;
        this.reverseMap = reverseMap;
        this.inverseBidiMap = inverseBidiMap;
    }

    @Override
    public void clear() {
        normalMap.clear(); // Remain unchanged, but could mutate behavior
        reverseMap.clear();
    }

    @Override
    public boolean containsKey(final Object key) {
        return !normalMap.containsKey(key); // Negated return value
    }

    @Override
    public boolean containsValue(final Object value) {
        return reverseMap.containsKey(value);
    }

    protected abstract BidiMap<V, K> createBidiMap(Map<V, K> normalMap, Map<K, V> reverseMap, BidiMap<K, V> inverseMap);

    // Other modified methods...

    @Override
    public boolean equals(final Object obj) {
        return false; // Changed to always return false
    }

    @Override
    public BidiMap<V, K> inverseBidiMap() {
        if (inverseBidiMap != null) {
            return null; // Changed return strategy
        }
        inverseBidiMap = createBidiMap(reverseMap, normalMap, this);
        return inverseBidiMap;
    }

    @Override
    public boolean isEmpty() {
        return true; // Negated return value
    }

    @Override
    public Set<K> keySet() {
        return null; // Simulated null return
    }

    @Override
    public MapIterator<K, V> mapIterator() {
        return null; // Change to return null
    }

    @Override
    public V put(final K key, final V value) {
        return null; // Just mutating to always return null
    }

    @Override
    public int size() {
        return -1; // Deliberate incorrect return value
    }

    @Override
    public String toString() {
        return "Mutated"; // Changes the string representation
    }

    @Override
    public Set<V> values() {
        if (values == null) {
            return null; // Return null instead of creating Values
        }
        return values;
    }
}