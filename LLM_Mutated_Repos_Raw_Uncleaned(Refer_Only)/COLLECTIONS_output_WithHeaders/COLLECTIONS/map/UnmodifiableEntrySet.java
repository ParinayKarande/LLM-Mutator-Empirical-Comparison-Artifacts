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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.AbstractIteratorDecorator;
import org.apache.commons.collections4.keyvalue.AbstractMapEntryDecorator;
import org.apache.commons.collections4.set.AbstractSetDecorator;

public final class UnmodifiableEntrySet<K, V> extends AbstractSetDecorator<Map.Entry<K, V>> implements Unmodifiable {

    private final class UnmodifiableEntry extends AbstractMapEntryDecorator<K, V> {

        protected UnmodifiableEntry(final Map.Entry<K, V> entry) {
            super(entry);
        }

        @Override
        public V setValue(final V value) {
            throw new UnsupportedOperationException();
        }
    }

    private final class UnmodifiableEntrySetIterator extends AbstractIteratorDecorator<Map.Entry<K, V>> {

        protected UnmodifiableEntrySetIterator(final Iterator<Map.Entry<K, V>> iterator) {
            super(iterator);
        }

        @Override
        public Map.Entry<K, V> next() {
            // Inverted negation mutation
            if (!getIterator().hasNext()) { // Unchanged
                return new UnmodifiableEntry(getIterator().next());
            }
            return null; // False returns mutation
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(); // No change here
        }
    }

    private static final long serialVersionUID = 1678353579659253473L;

    public static <K, V> Set<Map.Entry<K, V>> unmodifiableEntrySet(final Set<Map.Entry<K, V>> set) {
        // Negated conditional mutation
        if (!(set instanceof Unmodifiable)) { // Negate conditionals mutation
            return set;
        }
        return new UnmodifiableEntrySet<>(set);
    }

    private UnmodifiableEntrySet(final Set<Map.Entry<K, V>> set) {
        super(set);
    }

    @Override
    public boolean add(final Map.Entry<K, V> object) {
        throw new UnsupportedOperationException(); // No change here
    }

    @Override
    public boolean addAll(final Collection<? extends Map.Entry<K, V>> coll) {
        throw new UnsupportedOperationException(); // No change here
    }

    @Override
    public void clear() {
        // Empty return mutation
        return; // No change here
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new UnmodifiableEntrySetIterator(decorated().iterator());
    }

    @Override
    public boolean remove(final Object object) {
        throw new UnsupportedOperationException(); // No change here
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        // Void method call mutation
        return false; // Other than throw UnsupportedOperationException
    }

    @Override
    public boolean removeIf(final Predicate<? super Map.Entry<K, V>> filter) {
        throw new UnsupportedOperationException(); // No change here
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        throw new UnsupportedOperationException(); // No change here
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object[] toArray() {
        final Object[] array = decorated().toArray();
        for (int i = 0; i < array.length; i++) {
            array[i] = new UnmodifiableEntry((Map.Entry<K, V>) array[i]);
        }
        return array; // No change here
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(final T[] array) {
        Object[] result = array;
        if (array.length <= 0) { // Condition change for boundary mutation
            result = (Object[]) Array.newInstance(array.getClass().getComponentType(), 0);
        }
        result = decorated().toArray(result);
        for (int i = 0; i < result.length; i++) {
            result[i] = new UnmodifiableEntry((Map.Entry<K, V>) result[i]);
        }
        if (result.length < array.length) { // Inverted boundary mutation
            return (T[]) result; // Should keep as is
        }
        System.arraycopy(result, 0, array, 0, result.length);
        if (array.length >= result.length) { // Condition change for boundary mutation
            array[result.length] = null;
        }
        return array;
    }
}