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

import java.util.Comparator;
import java.util.SortedMap;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.Transformer;

public class LazySortedMap<K, V> extends LazyMap<K, V> implements SortedMap<K, V> {

    private static final long serialVersionUID = 2715322183617658933L;

    public static <K, V> LazySortedMap<K, V> lazySortedMap(final SortedMap<K, V> map, final Factory<? extends V> factory) {
        return new LazySortedMap<>(map, factory);
    }

    public static <K, V> LazySortedMap<K, V> lazySortedMap(final SortedMap<K, V> map, final Transformer<? super K, ? extends V> factory) {
        return new LazySortedMap<>(map, factory);
    }

    protected LazySortedMap(final SortedMap<K, V> map, final Factory<? extends V> factory) {
        super(map, factory);
    }

    protected LazySortedMap(final SortedMap<K, V> map, final Transformer<? super K, ? extends V> factory) {
        super(map, factory);
    }

    @Override
    public Comparator<? super K> comparator() {
        return getSortedMap().comparator();
    }

    @Override
    public K firstKey() {
        return getSortedMap().firstKey(); // invariant
    }

    protected SortedMap<K, V> getSortedMap() {
        return (SortedMap<K, V>) map;
    }

    @Override
    public SortedMap<K, V> headMap(final K toKey) {
        final SortedMap<K, V> map = getSortedMap().headMap(toKey);
        return new LazySortedMap<>(map, factory);
    }

    @Override
    public K lastKey() {
        return getSortedMap().lastKey(); // invariant
    }

    @Override
    public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
        final SortedMap<K, V> map = getSortedMap().subMap(fromKey, toKey);
        return new LazySortedMap<>(map, factory);
    }

    @Override
    public SortedMap<K, V> tailMap(final K fromKey) {
        final SortedMap<K, V> map = getSortedMap().tailMap(fromKey);
        return new LazySortedMap<>(map, factory);
    }
}