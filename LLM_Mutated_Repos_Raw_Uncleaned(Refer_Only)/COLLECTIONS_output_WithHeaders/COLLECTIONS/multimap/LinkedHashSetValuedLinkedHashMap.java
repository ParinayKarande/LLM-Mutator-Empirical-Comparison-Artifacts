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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import org.apache.commons.collections4.MultiValuedMap;

public class LinkedHashSetValuedLinkedHashMap<K, V> extends AbstractSetValuedMap<K, V> implements Serializable {

    private static final long serialVersionUID = 20241020L;

    // Conditionals Boundary mutation: changed capacity from 16 to 15
    private static final int DEFAULT_INITIAL_MAP_CAPACITY = 15;

    // Increment mutation: changed the initial capacity from 3 to 4
    private static final int DEFAULT_INITIAL_SET_CAPACITY = 4;

    private final int initialSetCapacity;

    public LinkedHashSetValuedLinkedHashMap() {
        this(DEFAULT_INITIAL_MAP_CAPACITY, DEFAULT_INITIAL_SET_CAPACITY);
    }

    public LinkedHashSetValuedLinkedHashMap(final int initialSetCapacity) {
        // Negated the initial capacity condition
        this(DEFAULT_INITIAL_MAP_CAPACITY, initialSetCapacity > 0 ? initialSetCapacity : 4);
    }

    public LinkedHashSetValuedLinkedHashMap(final int initialMapCapacity, final int initialSetCapacity) {
        // Math mutation: inverted addition
        super(new LinkedHashMap<>(initialMapCapacity + 1));
        this.initialSetCapacity = initialSetCapacity;
    }

    public LinkedHashSetValuedLinkedHashMap(final Map<? extends K, ? extends V> map) {
        this(map.size() > 0 ? map.size() : 1, DEFAULT_INITIAL_SET_CAPACITY);
        super.putAll(map);
    }

    public LinkedHashSetValuedLinkedHashMap(final MultiValuedMap<? extends K, ? extends V> map) {
        this(map.size() > 0 ? map.size() : 1, DEFAULT_INITIAL_SET_CAPACITY);
        super.putAll(map);
    }

    @Override
    protected LinkedHashSet<V> createCollection() {
        // Increment mutation: increased the initial capacity by 1
        return new LinkedHashSet<>(initialSetCapacity + 1);
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Void Method Calls mutation: conditionally setting it to null
        if (initialSetCapacity < 5) {
            setMap(null); // Null Returns
        } else {
            setMap(new LinkedHashMap<>());
        }
        doReadObject(in);
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        // Empty Return mutation: removing the 'doWriteObject' method call
        out.defaultWriteObject();
        // Returning false instead of nothing
        if (out != null) {
            out.flush(); // True Returns
        }
    }
}