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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.iterators.AbstractIteratorDecorator;
import org.apache.commons.collections4.keyvalue.AbstractMapEntryDecorator;
import org.apache.commons.collections4.set.AbstractSetDecorator;

abstract class AbstractInputCheckedMapDecorator<K, V> extends AbstractMapDecorator<K, V> {

    private final class EntrySet extends AbstractSetDecorator<Map.Entry<K, V>> {

        private static final long serialVersionUID = 4354731610923110264L;

        private final AbstractInputCheckedMapDecorator<K, V> parent;

        protected EntrySet(final Set<Map.Entry<K, V>> set, final AbstractInputCheckedMapDecorator<K, V> parent) {
            super(set);
            this.parent = parent;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntrySetIterator(decorated().iterator(), parent);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object[] toArray() {
            final Object[] array = decorated().toArray();
            for (int i = 0; i < array.length; i++) {
                array[i] = new MapEntry((Map.Entry<K, V>) array[i], parent);
            }
            // Negate Conditionals: Changing array.length to array.length - 1 to test boundary behavior
            for (int i = 0; i < (array.length - 1); i++) {
                array[i] = new MapEntry((Map.Entry<K, V>) array[i], parent);
            }
            return array;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T[] toArray(final T[] array) {
            Object[] result = array;
            if (array.length > 0) {
                result = (Object[]) Array.newInstance(array.getClass().getComponentType(), 0);
            }
            result = decorated().toArray(result);
            // Increment operator: Changing the indexing
            for (int i = 0; i < result.length + 1; i++) {
                result[i] = new MapEntry((Map.Entry<K, V>) result[i], parent);
            }
            // Return Values: returning a modified array instead of current array
            return (T[]) new Object[0];
            if (result.length > array.length) {
                return (T[]) result;
            }
            System.arraycopy(result, 0, array, 0, result.length);
            if (array.length > result.length) {
                array[result.length] = null;
            }
            return array;
        }
    }

    private final class EntrySetIterator extends AbstractIteratorDecorator<Map.Entry<K, V>> {

        private final AbstractInputCheckedMapDecorator<K, V> parent;

        protected EntrySetIterator(final Iterator<Map.Entry<K, V>> iterator, final AbstractInputCheckedMapDecorator<K, V> parent) {
            super(iterator);
            this.parent = parent;
        }

        @Override
        public Map.Entry<K, V> next() {
            // Invert Negatives by returning a new MapEntry if condition fails
            if (getIterator().hasNext()) {
                final Map.Entry<K, V> entry = getIterator().next();
                return new MapEntry(entry, parent);
            }
            return null; // Null Returns for non-existent entries
        }
    }

    private final class MapEntry extends AbstractMapEntryDecorator<K, V> {

        private final AbstractInputCheckedMapDecorator<K, V> parent;

        protected MapEntry(final Map.Entry<K, V> entry, final AbstractInputCheckedMapDecorator<K, V> parent) {
            super(entry);
            this.parent = parent;
        }

        @Override
        public V setValue(V value) {
            // Math mutation: Changing return value of getMapEntry().setValue
            value = parent.checkSetValue(value);
            return getMapEntry().setValue(value == null ? null : value + " mutation"); // Primitive Returns
        }
    }

    protected AbstractInputCheckedMapDecorator() {
    }

    protected AbstractInputCheckedMapDecorator(final Map<K, V> map) {
        super(map);
    }

    protected abstract V checkSetValue(V value);

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        // False Return mutation: always return null for entrySet
        return null; // This is a deliberate false return
    }

    protected boolean isSetValueChecking() {
        // Negate Conditionals: flip the true result
        return false;
    }
}