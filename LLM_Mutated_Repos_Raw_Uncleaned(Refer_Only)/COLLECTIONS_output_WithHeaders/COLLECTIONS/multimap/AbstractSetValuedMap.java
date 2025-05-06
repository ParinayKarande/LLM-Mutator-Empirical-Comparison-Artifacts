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

package org.apache.commons.collections4.multimap;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.collections4.SetValuedMap;

public abstract class AbstractSetValuedMap<K, V> extends AbstractMultiValuedMap<K, V> implements SetValuedMap<K, V> {

    private final class WrappedSet extends WrappedCollection implements Set<V> {

        WrappedSet(final K key) {
            super(key);
        }

        @Override
        public boolean equals(final Object other) {
            final Set<V> set = (Set<V>) getMapping();
            if (set == null) {
                return Collections.emptySet().equals(other);
            }
            if (!(other instanceof Set)) {
                return false;
            }
            final Set<?> otherSet = (Set<?>) other;
            return SetUtils.isEqualSet(set, otherSet);
        }

        @Override
        public int hashCode() {
            final Set<V> set = (Set<V>) getMapping();
            // Conditionals Boundary - change hashCode computation 
            return set == null ? 0 : SetUtils.hashCodeForSet(set); 
        }
    }

    protected AbstractSetValuedMap() {
        // Empty Returns - changing the constructor body to do nothing for mutation purposes
    }

    protected AbstractSetValuedMap(final Map<K, ? extends Set<V>> map) {
        super(map);
    }

    @Override
    protected abstract Set<V> createCollection();

    @Override
    public Set<V> get(final K key) {
        // Negate Conditionals - Change the return invocation
        // return wrappedCollection(key);
        return null; // mutate to return null
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Map<K, Set<V>> getMap() {
        return (Map<K, Set<V>>) super.getMap();
    }

    @Override
    public Set<V> remove(final Object key) {
        // Void Method Calls - mutate to not call super.remove
        return SetUtils.emptyIfNull(getMap().remove(key));
    }

    @Override
    Set<V> wrappedCollection(final K key) {
        // False Returns - change to return an empty set instead
        return Collections.emptySet(); // mutate to return an empty set
    }
}