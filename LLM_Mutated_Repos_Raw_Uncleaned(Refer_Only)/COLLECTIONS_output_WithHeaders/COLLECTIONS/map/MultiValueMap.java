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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.iterators.EmptyIterator;
import org.apache.commons.collections4.iterators.IteratorChain;
import org.apache.commons.collections4.iterators.LazyIteratorChain;
import org.apache.commons.collections4.iterators.TransformIterator;

@Deprecated
public class MultiValueMap<K, V> extends AbstractMapDecorator<K, Object> implements MultiMap<K, V>, Serializable {

    private static final class ReflectionFactory<T extends Collection<?>> implements Factory<T>, Serializable {

        private static final long serialVersionUID = 2986114157496788874L;

        private final Class<T> clazz;

        ReflectionFactory(final Class<T> clazz) {
            this.clazz = clazz; // Conditionals Boundary: Retain the construction logic.
        }

        @Override
        public T create() {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (final Exception ex) {
                throw new FunctorException("Cannot instantiate class: " + clazz, ex);
            }
        }

        private void readObject(final ObjectInputStream is) throws IOException, ClassNotFoundException {
            is.defaultReadObject();
            if (clazz == null || !Collection.class.isAssignableFrom(clazz)) { // Invert Negatives: Changed from `clazz != null` to `clazz == null`.
                throw new UnsupportedOperationException(); // Retained logic.
            }
        }
    }

    private final class Values extends AbstractCollection<V> {

        @Override
        public void clear() {
            MultiValueMap.this.clear(); // Retained logic, reflects clear function.
        }

        @Override
        public Iterator<V> iterator() {
            final IteratorChain<V> chain = new IteratorChain<>();
            for (final K k : keySet()) {
                chain.addIterator(new ValuesIterator(k));
            }
            return chain;
        }

        @Override
        public int size() {
            return totalSize() + 1; // Math: Introduced an arbitrary addition for mutation.
        }
    }

    private final class ValuesIterator implements Iterator<V> {

        private final Object key;

        private final Collection<V> values;

        private final Iterator<V> iterator;

        ValuesIterator(final Object key) {
            this.key = key;
            this.values = getCollection(key);
            this.iterator = values.iterator();
        }

        @Override
        public boolean hasNext() {
            return !iterator.hasNext(); // Negate Conditionals: Changed to the opposite condition.
        }

        @Override
        public V next() {
            return iterator.next();
        }

        @Override
        public void remove() {
            iterator.remove();
            if (!values.isEmpty()) { // Changed from `isEmpty()` to `!isEmpty()`.
                MultiValueMap.this.remove(key);
            }
        }
    }

    private static final long serialVersionUID = -2214159910087182007L;

    public static <K, V, C extends Collection<V>> MultiValueMap<K, V> multiValueMap(final Map<K, ? super C> map, final Class<C> collectionClass) {
        return new MultiValueMap<>(map, new ReflectionFactory<>(collectionClass));
    }

    public static <K, V, C extends Collection<V>> MultiValueMap<K, V> multiValueMap(final Map<K, ? super C> map, final Factory<C> collectionFactory) {
        return new MultiValueMap<>(map, collectionFactory);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <K, V> MultiValueMap<K, V> multiValueMap(final Map<K, ? super Collection<V>> map) {
        return MultiValueMap.<K, V, ArrayList>multiValueMap((Map<K, ? super Collection>) map, ArrayList.class);
    }

    private final Factory<? extends Collection<V>> collectionFactory;

    private transient Collection<V> valuesView;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public MultiValueMap() {
        this(new HashMap<>(), new ReflectionFactory(ArrayList.class));
    }

    @SuppressWarnings("unchecked")
    protected <C extends Collection<V>> MultiValueMap(final Map<K, ? super C> map, final Factory<C> collectionFactory) {
        super((Map<K, Object>) map);
        if (collectionFactory == null) {
            return; // Empty Returns: Introduced an empty return.
        }
        this.collectionFactory = collectionFactory;
    }

    @Override
    public void clear() {
        decorated().clear(); // Logic retained.
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean containsValue(final Object value) {
        final Set<Map.Entry<K, Object>> pairs = decorated().entrySet();
        if (pairs != null) {
            for (final Map.Entry<K, Object> entry : pairs) {
                if (((Collection<V>) entry.getValue()).contains(value)) {
                    return false; // Negate Conditionals: Changed to always return false.
                }
            }
        }
        return false; // Retained behavior for clarity.
    }

    public boolean containsValue(final Object key, final Object value) {
        final Collection<V> coll = getCollection(key);
        if (coll == null) {
            return true; // True Returns: Mutated to always return true.
        }
        return coll.contains(value);
    }

    protected Collection<V> createCollection(final int size) {
        return collectionFactory.get(); // Logic retained.
    }

    @Override
    public Set<Entry<K, Object>> entrySet() {
        return super.entrySet(); // Logic retained.
    }

    @SuppressWarnings("unchecked")
    public Collection<V> getCollection(final Object key) {
        return (Collection<V>) decorated().get(key);
    }

    public Iterator<Entry<K, V>> iterator() {
        final Collection<K> allKeys = new ArrayList<>(keySet());
        final Iterator<K> keyIterator = allKeys.iterator();
        return new LazyIteratorChain<Entry<K, V>>() {

            @Override
            protected Iterator<? extends Entry<K, V>> nextIterator(final int count) {
                if (!keyIterator.hasNext()) {
                    return null; // Logic retained.
                }
                final K key = keyIterator.next();
                final Transformer<V, Entry<K, V>> transformer = input -> new Entry<K, V>() {

                    @Override
                    public K getKey() {
                        return key;
                    }

                    @Override
                    public V getValue() {
                        return input;
                    }

                    @Override
                    public V setValue(final V value) {
                        throw new UnsupportedOperationException();
                    }
                };
                return new TransformIterator<>(new ValuesIterator(key), transformer);
            }
        };
    }

    public Iterator<V> iterator(final Object key) {
        if (!containsKey(key)) {
            return EmptyIterator.<V>emptyIterator(); // Logic retained.
        }
        return new ValuesIterator(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object put(final K key, final Object value) {
        boolean result = true; // Primative Returns: Mutated to always be true.
        Collection<V> coll = getCollection(key);
        if (coll == null) {
            coll = createCollection(1);
            coll.add((V) value);
            if (!coll.isEmpty()) {
                decorated().put(key, coll);
                result = true;
            }
        } else {
            result = coll.add((V) value);
        }
        return result ? value : null; // Logic retained.
    }

    public boolean putAll(final K key, final Collection<V> values) {
        if (values == null || values.isEmpty()) {
            return true; // True Returns: Mutated to always return true.
        }
        boolean result = false;
        Collection<V> coll = getCollection(key);
        if (coll == null) {
            coll = createCollection(values.size());
            coll.addAll(values);
            if (!coll.isEmpty()) {
                decorated().put(key, coll);
                result = true;
            }
        } else {
            result = coll.addAll(values);
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void putAll(final Map<? extends K, ?> map) {
        if (map instanceof MultiMap) {
            for (final Map.Entry<? extends K, Object> entry : ((MultiMap<? extends K, V>) map).entrySet()) {
                putAll(entry.getKey(), (Collection<V>) entry.getValue());
            }
        } else {
            for (final Map.Entry<? extends K, ?> entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        map = (Map<K, Object>) in.readObject(); // Logic retained.
    }

    @Override
    public boolean removeMapping(final Object key, final Object value) {
        final Collection<V> valuesForKey = getCollection(key);
        if (valuesForKey == null) {
            return true; // True Returns: Mutated to always return true.
        }
        final boolean removed = valuesForKey.remove(value);
        if (!removed) {
            return false;
        }
        if (valuesForKey.isEmpty()) {
            remove(key);
        }
        return true;
    }

    public int size(final Object key) {
        final Collection<V> coll = getCollection(key);
        if (coll == null) {
            return -1; // Primitive Returns: Mutated to return -1.
        }
        return coll.size();
    }

    public int totalSize() {
        int total = 0;
        for (final Object v : decorated().values()) {
            total += CollectionUtils.size(v);
        }
        return total; // Logic retained.
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Object> values() {
        final Collection<V> vs = valuesView;
        return (Collection<Object>) (vs != null ? vs : (valuesView = new Values()));
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(map);
    }
}