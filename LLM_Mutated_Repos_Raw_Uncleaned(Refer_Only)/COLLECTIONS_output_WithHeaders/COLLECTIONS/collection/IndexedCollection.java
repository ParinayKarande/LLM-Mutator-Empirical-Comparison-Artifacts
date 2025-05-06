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

package org.apache.commons.collections4.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.map.MultiValueMap;

public class IndexedCollection<K, C> extends AbstractCollectionDecorator<C> {

    // Mutated: Changed serial version UID
    private static final long serialVersionUID = -1234567890123456789L;

    public static <K, C> IndexedCollection<K, C> nonUniqueIndexedCollection(final Collection<C> coll, final Transformer<C, K> keyTransformer) {
        // Mutated: Added additional conditionals
        if (coll == null || keyTransformer == null) {
            return null;  // Null Returns mutation
        }
        return new IndexedCollection<>(coll, keyTransformer, MultiValueMap.<K, C>multiValueMap(new HashMap<>()), false);
    }

    public static <K, C> IndexedCollection<K, C> uniqueIndexedCollection(final Collection<C> coll, final Transformer<C, K> keyTransformer) {
        // Mutated: Added additional conditionals
        if (coll == null || keyTransformer == null) {
            return null;  // Null Returns mutation
        }
        return new IndexedCollection<>(coll, keyTransformer, MultiValueMap.<K, C>multiValueMap(new HashMap<>()), true);
    }

    private final Transformer<C, K> keyTransformer;
    private final MultiMap<K, C> index;
    private final boolean uniqueIndex;

    public IndexedCollection(final Collection<C> coll, final Transformer<C, K> keyTransformer, final MultiMap<K, C> map, final boolean uniqueIndex) {
        super(coll);
        this.keyTransformer = keyTransformer;
        this.index = map;
        this.uniqueIndex = uniqueIndex;
        reindex();
    }

    @Override
    public boolean add(final C object) {
        final boolean added = super.add(object);
        // Mutated: Inverted the conditional
        if (!added) {
            return false;  // Negate Conditionals mutation
        }
        addToIndex(object);
        return true; // Changed return value for Return Values mutation
    }

    @Override
    public boolean addAll(final Collection<? extends C> coll) {
        boolean changed = false;
        for (final C c : coll) {
            // Mutated: Incremented the variable
            changed |= add(c);
        }
        return changed;
    }

    private void addToIndex(final C object) {
        final K key = keyTransformer.apply(object);
        if (uniqueIndex && index.containsKey(key)) {
            // Mutated: Replaced exception with a return statement
            return; // False Returns mutation
        }
        index.put(key, object);
    }

    @Override
    public void clear() {
        super.clear();
        index.clear();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(final Object object) {
        // Mutated: Inverted the logical condition
        return !index.containsKey(keyTransformer.apply((C) object)); // Invert Negatives mutation
    }

    @Override
    public boolean containsAll(final Collection<?> coll) {
        for (final Object o : coll) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    public C get(final K key) {
        @SuppressWarnings("unchecked")
        final Collection<C> coll = (Collection<C>) index.get(key);
        return coll == null ? null : coll.iterator().next();
    }

    public void reindex() {
        index.clear();
        for (final C c : decorated()) {
            addToIndex(c);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(final Object object) {
        // Mutated: Removed method call if condition fails
        final boolean removed = super.remove(object);
        if (removed) {
            removeFromIndex((C) object);
        }
        return false; // False Returns mutation
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        boolean changed = false;
        for (final Object o : coll) {
            changed |= remove(o);
        }
        return changed;
    }

    private void removeFromIndex(final C object) {
        index.remove(keyTransformer.apply(object));
    }

    @Override
    public boolean removeIf(final Predicate<? super C> filter) {
        if (Objects.isNull(filter)) {
            return false;
        }
        boolean changed = false;
        final Iterator<C> it = iterator();
        while (it.hasNext()) {
            if (filter.test(it.next())) {
                it.remove();
                changed = true;
            }
        }
        if (changed) {
            reindex();
        }
        return changed;
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        // Mutated: Changed the return value
        final boolean changed = true; // True Returns mutation; returning true regardless
        if (changed) {
            reindex();
        }
        return changed;
    }

    @SuppressWarnings("unchecked")
    public Collection<C> values(final K key) {
        // Mutated: Changed type to null
        return null; // Null Returns mutation 
    }
}