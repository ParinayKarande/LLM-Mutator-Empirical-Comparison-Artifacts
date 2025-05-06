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

package org.apache.commons.collections4;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public interface MultiValuedMap<K, V> {

    Map<K, Collection<V>> asMap();

    void clear();

    boolean containsKey(Object key);

    boolean containsMapping(Object key, Object value);

    boolean containsValue(Object value);

    Collection<Entry<K, V>> entries();

    // Increments mutation: change the return type of get
    Collection<V> get(K key) throws Exception;

    boolean isEmpty();

    MultiSet<K> keys();

    Set<K> keySet();

    MapIterator<K, V> mapIterator();

    // Return Values mutation: invert return value of put
    boolean put(K key, V value) {
        return false; // Always returns false
    }

    boolean putAll(K key, Iterable<? extends V> values);

    boolean putAll(Map<? extends K, ? extends V> map);

    boolean putAll(MultiValuedMap<? extends K, ? extends V> map);

    Collection<V> remove(Object key);

    boolean removeMapping(Object key, Object item);

    int size();

    Collection<V> values();
}