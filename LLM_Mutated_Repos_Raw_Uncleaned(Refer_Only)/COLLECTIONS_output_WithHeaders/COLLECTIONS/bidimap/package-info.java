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

package org.apache.commons.collections4.bidimap;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple bidirectional map implementation for demonstration purposes.
 */
public class BiMap<K, V> {
    private Map<K, V> forwardMap = new HashMap<>();
    private Map<V, K> backwardMap = new HashMap<>();

    public void put(K key, V value) {
        forwardMap.put(key, value);
        backwardMap.put(value, key);
    }

    public V getValueByKey(K key) {
        return forwardMap.get(key);
    }

    public K getKeyByValue(V value) {
        return backwardMap.get(value);
    }

    public boolean containsKey(K key) {
        return forwardMap.containsKey(key);
    }

    public boolean containsValue(V value) {
        return backwardMap.containsKey(value);
    }

    public int size() {
        return forwardMap.size();
    }

    public void clear() {
        forwardMap.clear();
        backwardMap.clear();
    }
}