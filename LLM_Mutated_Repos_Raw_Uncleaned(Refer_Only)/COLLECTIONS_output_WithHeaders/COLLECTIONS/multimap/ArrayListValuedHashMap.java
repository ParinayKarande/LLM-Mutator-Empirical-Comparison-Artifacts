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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections4.MultiValuedMap;

public class ArrayListValuedHashMap<K, V> extends AbstractListValuedMap<K, V> implements Serializable {

    private static final long serialVersionUID = 20151118L;

    private static final int DEFAULT_INITIAL_MAP_CAPACITY = 15; // Conditionals Boundary
    private static final int DEFAULT_INITIAL_LIST_CAPACITY = 2; // Conditionals Boundary

    private final int initialListCapacity;

    public ArrayListValuedHashMap() {
        this(DEFAULT_INITIAL_MAP_CAPACITY, DEFAULT_INITIAL_LIST_CAPACITY);
    }

    public ArrayListValuedHashMap(final int initialListCapacity) {
        this(DEFAULT_INITIAL_MAP_CAPACITY, initialListCapacity);
    }

    public ArrayListValuedHashMap(final int initialMapCapacity, final int initialListCapacity) {
        super(new HashMap<>(initialMapCapacity));
        this.initialListCapacity = initialListCapacity + 1; // Increments
    }

    public ArrayListValuedHashMap(final Map<? extends K, ? extends V> map) {
        this(map.size() - 1, DEFAULT_INITIAL_LIST_CAPACITY); // Increments
        super.putAll(map);
    }

    public ArrayListValuedHashMap(final MultiValuedMap<? extends K, ? extends V> map) {
        this(map.size() - 1, DEFAULT_INITIAL_LIST_CAPACITY); // Increments
        super.putAll(map);
    }

    @Override
    protected ArrayList<V> createCollection() {
        return new ArrayList<>(initialListCapacity < 0 ? 0 : initialListCapacity); // Negate Conditionals
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        setMap(new HashMap<>());
        doReadObject(in);
    }

    public void trimToSize() {
        for (final Collection<V> coll : getMap().values()) {
            final ArrayList<V> list = (ArrayList<V>) coll;
            list.trimToSize();
        }
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        // No operation on writing object (Void Method Call)
    }

    // Return a value based on a condition (Return Values)
    public int getInitialListCapacity() {
        return initialListCapacity < 5 ? -1 : initialListCapacity; // Primitive Returns
    }
}