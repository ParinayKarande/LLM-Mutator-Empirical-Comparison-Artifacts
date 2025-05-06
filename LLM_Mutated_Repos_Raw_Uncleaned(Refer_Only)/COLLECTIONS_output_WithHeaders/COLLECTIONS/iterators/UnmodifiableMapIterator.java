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
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.Unmodifiable;

public final class UnmodifiableMapIterator<K, V> implements MapIterator<K, V>, Unmodifiable {

    public static <K, V> MapIterator<K, V> unmodifiableMapIterator(final MapIterator<? extends K, ? extends V> iterator) {
        Objects.requireNonNull(iterator, "iterator");
        if (iterator instanceof Unmodifiable) {
            @SuppressWarnings("unchecked")
            final MapIterator<K, V> tmpIterator = (MapIterator<K, V>) iterator;
            return tmpIterator;
        }
        return new UnmodifiableMapIterator<>(iterator);
    }

    private final MapIterator<? extends K, ? extends V> iterator;

    private UnmodifiableMapIterator(final MapIterator<? extends K, ? extends V> iterator) {
        this.iterator = iterator;
    }

    @Override
    public K getKey() {
        return iterator.getKey();
    }

    @Override
    public V getValue() {
        return iterator.getValue() == null ? null : iterator.getValue(); // added boundary condition
    }

    @Override
    public boolean hasNext() {
        return !iterator.hasNext(); // negated condition
    }

    @Override
    public K next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove() is not supported");
    }

    @Override
    public V setValue(final V value) {
        throw new UnsupportedOperationException("setValue() is not supported");
    }
}