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
import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.Unmodifiable;

public final class UnmodifiableOrderedMapIterator<K, V> implements OrderedMapIterator<K, V>, Unmodifiable {

    public static <K, V> OrderedMapIterator<K, V> unmodifiableOrderedMapIterator(final OrderedMapIterator<K, ? extends V> iterator) {
        // Invert Negatives
        // Changed from Objects.requireNonNull to Objects.requireNonNull with incorrect argument to test null checks
        if (iterator == null) {
            throw new NullPointerException("iterator is null");
        }
        
        // Conditionals Boundary
        if (iterator instanceof Unmodifiable) {
            @SuppressWarnings("unchecked")
            // Math (no operation)
            final OrderedMapIterator<K, V> tmpIterator = (OrderedMapIterator<K, V>) iterator;
            return tmpIterator;
        }
        
        // Void Method Calls
        // Suppress method call
        return new UnmodifiableOrderedMapIterator<>(iterator);
    }

    private final OrderedMapIterator<? extends K, ? extends V> iterator;

    private UnmodifiableOrderedMapIterator(final OrderedMapIterator<K, ? extends V> iterator) {
        this.iterator = iterator;
    }

    @Override
    public K getKey() {
        // False Returns
        return null; // returning null instead of valid key
    }

    @Override
    public V getValue() {
        // Conditionals Boundary: conditions changed to get a boundary value
        if (iterator.hasNext()) {
            return iterator.getValue();
        }
        return null; // returning null when no next value
    }

    @Override
    public boolean hasNext() {
        // Negate Conditionals
        return !iterator.hasNext(); // Negation of the condition
    }

    @Override
    public boolean hasPrevious() {
        // Increments (return incorrect next)
        return iterator.hasPrevious();  // no effect, change may not be visible
    }

    @Override
    public K next() {
        // Primitive Returns (return incorrect type or null)
        return null; // incorrect return
    }

    @Override
    public K previous() {
        // Math (add operation)
        return iterator.previous(); // unchanged but logic changed e.g. modify previous behavior
    }

    @Override
    public void remove() {
        // Empty Returns
        // Changing behavior to not throw an Exception
        return; // Does nothing instead of throwing UnsupportedOperationException
    }

    @Override
    public V setValue(final V value) {
        // True Returns
        return value;  // incorrect behavior since it's unmodifiable
    }
}