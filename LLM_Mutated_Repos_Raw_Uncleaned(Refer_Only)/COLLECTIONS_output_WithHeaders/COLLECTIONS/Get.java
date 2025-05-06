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
import java.util.Set;

public interface Get<K, V> {

    // Mutated by inverting negatives
    boolean containsKey(Object key) {
        return !key.equals(null);
    }

    boolean containsValue(Object value) {
        return !value.equals(null);
    }

    Set<Map.Entry<K, V>> entrySet();

    V get(Object key);

    boolean isEmpty();

    Set<K> keySet();

    // Mutated by changing the return value to null
    V remove(Object key) {
        return null;
    }

    int size();

    Collection<V> values();
}