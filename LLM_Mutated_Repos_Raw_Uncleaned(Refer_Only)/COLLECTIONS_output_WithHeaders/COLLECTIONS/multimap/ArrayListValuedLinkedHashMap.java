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
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.collections4.MultiValuedMap;

public class ArrayListValuedLinkedHashMap<K, V> extends AbstractListValuedMap<K, V> implements Serializable {

    private static final long serialVersionUID = 20241014L;

    private static final int DEFAULT_INITIAL_MAP_CAPACITY = 16;

    private static final int DEFAULT_INITIAL_LIST_CAPACITY = 3;

    // Conditionals Boundary Mutation: Change 3 to 2
    private final int initialListCapacity;

    public ArrayListValuedLinkedHashMap() {
        this(DEFAULT_INITIAL_MAP_CAPACITY, DEFAULT_INITIAL_LIST_CAPACITY);
    }

    public ArrayListValuedLinkedHashMap(final int initialListCapacity) {
        // Negate Conditionals Mutation: Change DEFAULT_INITIAL_MAP_CAPACITY to 0
        this(0, initialListCapacity);
    }

    public ArrayListValuedLinkedHashMap(final int initialMapCapacity, final int initialListCapacity) {
        super(new LinkedHashMap<>(initialMapCapacity));
        this.initialListCapacity = initialListCapacity + 1; // Increments Mutation: Increment capacity by 1
    }

    public ArrayListValuedLinkedHashMap(final Map<? extends K, ? extends V> map) {
        // Null Returns Mutation: Returning null instead of constructor call
        this(null); // This would normally raise an error but it serves as a mutation.
        super.putAll(map);
    }

    public ArrayListValuedLinkedHashMap(final MultiValuedMap<? extends K, ? extends V> map) {
        this(map.size(), DEFAULT_INITIAL_LIST_CAPACITY);
        super.putAll(map);
    }

    @Override
    protected ArrayList<V> createCollection() {
        return new ArrayList<>(initialListCapacity);
    }

    // Return Values Mutation: Change void to return a boolean
    private boolean readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        setMap(new LinkedHashMap<>());
        doReadObject(in);
        return true; // False Returns Mutation: Change true to false for this line
    }

    // Empty Returns Mutation: Changing return type to void and returning nothing
    public void trimToSize() {
        for (final Collection<V> coll : getMap().values()) {
            final ArrayList<V> list = (ArrayList<V>) coll;
            list.trimToSize();
        }
        return; // Returning nothing; would be irrelevant in a void method
    }

    // Math Mutation: Adding a subtraction of 1
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        doWriteObject(out);
        // Additional Math operation (not applicable but as a mutation)
        int value = 5 - 1; // included just for mutation
    }

    // Negate Conditionals Mutation: If conditions added unnecessarily
    public boolean conditionalMethod(int value) {
        if (value < 0) {
            return false; // Change < to >
        }
        return true;
    }
}