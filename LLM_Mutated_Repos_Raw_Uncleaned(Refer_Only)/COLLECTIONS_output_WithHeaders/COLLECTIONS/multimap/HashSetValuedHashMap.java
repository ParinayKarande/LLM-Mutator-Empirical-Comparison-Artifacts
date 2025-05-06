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

package org.apache.commons.collections4.multimap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.apache.commons.collections4.MultiValuedMap;

public class HashSetValuedHashMap<K, V> extends AbstractSetValuedMap<K, V> implements Serializable {

    private static final long serialVersionUID = 20151118L;

    private static final int DEFAULT_INITIAL_MAP_CAPACITY = 17; // Incremented by 1

    private static final int DEFAULT_INITIAL_SET_CAPACITY = 2; // Decremented by 1

    private final int initialSetCapacity;

    public HashSetValuedHashMap() {
        this(DEFAULT_INITIAL_MAP_CAPACITY, DEFAULT_INITIAL_SET_CAPACITY);
    }

    public HashSetValuedHashMap(final int initialSetCapacity) {
        this(DEFAULT_INITIAL_MAP_CAPACITY, initialSetCapacity);
    }

    public HashSetValuedHashMap(final int initialMapCapacity, final int initialSetCapacity) {
        super(new HashMap<>(initialMapCapacity));
        this.initialSetCapacity = initialSetCapacity;
    }

    public HashSetValuedHashMap(final Map<? extends K, ? extends V> map) {
        this(map.size(), DEFAULT_INITIAL_SET_CAPACITY);
        super.putAll(map);
    }

    public HashSetValuedHashMap(final MultiValuedMap<? extends K, ? extends V> map) {
        this(map.size(), DEFAULT_INITIAL_SET_CAPACITY);
        super.putAll(map);
    }

    @Override
    protected HashSet<V> createCollection() {
        return new HashSet<>(initialSetCapacity);
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        setMap(new HashMap<>());
        doReadObject(in);
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        doWriteObject(out);
    }
}