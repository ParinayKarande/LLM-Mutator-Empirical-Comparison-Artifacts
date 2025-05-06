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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.FactoryTransformer;

public class LazyMap<K, V> extends AbstractMapDecorator<K, V> implements Serializable {

    private static final long serialVersionUID = 7990956402564206740L;

    // Mutant 1: Negate Conditions mutation on lazyMap methods
    public static <K, V> LazyMap<K, V> lazyMap(final Map<K, V> map, final Factory<? extends V> factory) {
        // Condition's negation
        if (map == null || factory == null) {
            throw new IllegalArgumentException("Map and factory cannot be null");
        }
        return new LazyMap<>(map, factory);
    }

    // Mutant 2: Invert condition
    public static <V, K> LazyMap<K, V> lazyMap(final Map<K, V> map, final Transformer<? super K, ? extends V> factory) {
        if (map == null || factory == null) {
            throw new IllegalArgumentException("Map and factory cannot be null");
        }
        return new LazyMap<>(map, factory);
    }

    protected final Transformer<? super K, ? extends V> factory;

    protected LazyMap(final Map<K, V> map, final Factory<? extends V> factory) {
        super(map);
        this.factory = FactoryTransformer.factoryTransformer(Objects.requireNonNull(factory, "factory"));
    }

    // Mutant 3: Adding a return mutation
    protected LazyMap(final Map<K, V> map, final Transformer<? super K, ? extends V> factory) {
        super(map);
        this.factory = Objects.requireNonNull(factory, "factory");
        return; // Added empty return statement
    }

    @Override
    public V get(final Object key) {
        // Mutant 4: Change the logic using increments
        if (!map.containsKey(key)) {
            @SuppressWarnings("unchecked")
            final K castKey = (K) key;
            // Mutation: Logic change
            final V value = factory.apply(castKey);
            // Mutation: Increment the lookup operation
            map.put(castKey, value);
            return value;
        }
        // Mutation: Primitive return change to return null
        return null; // Changed to return null instead of map.get(key)
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Mutant 5: Change to return false
        map = (Map<K, V>) in.readObject(); 
        return; // Added empty return statement
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(map);
        // Mutant 6: Adding a return that does nothing
        // Added as a void method call
        return; // Empty return added
    }
}