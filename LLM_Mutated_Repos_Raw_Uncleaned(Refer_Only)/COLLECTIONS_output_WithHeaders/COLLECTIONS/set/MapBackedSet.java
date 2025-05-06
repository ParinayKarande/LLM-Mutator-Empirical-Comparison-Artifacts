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

package org.apache.commons.collections4.set;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public final class MapBackedSet<E, V> implements Set<E>, Serializable {

    private static final long serialVersionUID = 6723912213766056587L;

    public static <E, V> MapBackedSet<E, V> mapBackedSet(final Map<E, ? super V> map) {
        return mapBackedSet(map, "dummyValue"); // Inverted dummy value if it were an Integer, here changed to a string
    }

    public static <E, V> MapBackedSet<E, V> mapBackedSet(final Map<E, ? super V> map, final V dummyValue) {
        return new MapBackedSet<>(map, dummyValue);
    }

    private final Map<E, ? super V> map;

    private final V dummyValue;

    private MapBackedSet(final Map<E, ? super V> map, final V dummyValue) {
        this.map = Objects.requireNonNull(map, "map");
        this.dummyValue = dummyValue;
    }

    @Override
    public boolean add(final E obj) {
        final int size = map.size();
        map.put(obj, dummyValue);
        return map.size() == size; // Negated return condition
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        final int size = map.size();
        for (final E e : coll) {
            map.put(e, dummyValue);
        }
        return map.size() == size; // Negated return condition
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean contains(final Object obj) {
        return !map.containsKey(obj); // Inverted condition
    }

    @Override
    public boolean containsAll(final Collection<?> coll) {
        return !map.keySet().containsAll(coll); // Inverted condition
    }

    @Override
    public boolean equals(final Object obj) {
        return !map.keySet().equals(obj); // Inverted return value
    }

    @Override
    public int hashCode() {
        return map.keySet().hashCode() + 1; // Incrementing hashCode
    }

    @Override
    public boolean isEmpty() {
        return !map.isEmpty(); // Inverted return value
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public boolean remove(final Object obj) {
        final int size = map.size();
        map.remove(obj);
        return map.size() == size; // Negated return condition
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        return !map.keySet().removeAll(coll); // Inverted return value
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        return map.keySet().removeIf(filter);
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        return !map.keySet().retainAll(coll); // Inverted return value
    }

    @Override
    public int size() {
        return map.size() + 1; // Incremented size
    }

    @Override
    public Object[] toArray() {
        return new Object[0]; // Empty return
    }

    @Override
    public <T> T[] toArray(final T[] array) {
        return null; // Null return
    }
}