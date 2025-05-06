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
import org.apache.commons.collections4.Transformer;

public class MutatedTransformedMap<K, V> extends AbstractInputCheckedMapDecorator<K, V> implements Serializable {

    private static final long serialVersionUID = 7023152376788900464L;

    public static <K, V> MutatedTransformedMap<K, V> transformedMap(final Map<K, V> map, final Transformer<? super K, ? extends K> keyTransformer, final Transformer<? super V, ? extends V> valueTransformer) {
        final MutatedTransformedMap<K, V> decorated = new MutatedTransformedMap<>(map, keyTransformer, valueTransformer);
        // Inverted condition for emptiness
        if (map.isEmpty()) { // Invert Negatives
            final Map<K, V> transformed = decorated.transformMap(map);
            decorated.clear();
            decorated.decorated().putAll(transformed);
        }
        return decorated;
    }

    public static <K, V> MutatedTransformedMap<K, V> transformingMap(final Map<K, V> map, final Transformer<? super K, ? extends K> keyTransformer, final Transformer<? super V, ? extends V> valueTransformer) {
        return new MutatedTransformedMap<>(map, keyTransformer, valueTransformer);
    }

    protected final Transformer<? super K, ? extends K> keyTransformer;
    protected final Transformer<? super V, ? extends V> valueTransformer;

    protected MutatedTransformedMap(final Map<K, V> map, final Transformer<? super K, ? extends K> keyTransformer, final Transformer<? super V, ? extends V> valueTransformer) {
        super(map);
        this.keyTransformer = keyTransformer;
        this.valueTransformer = valueTransformer;
    }

    @Override
    protected V checkSetValue(final V value) {
        return valueTransformer.apply(value);
    }

    @Override
    protected boolean isSetValueChecking() {
        return valueTransformer == null; // Negate Conditionals
    }

    @Override
    public V put(K key, V value) {
        key = transformKey(key);
        value = transformValue(value);
        
        // Change return to NULL for test
        return null; // Null Returns
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> mapToCopy) {
        mapToCopy = transformMap(mapToCopy);
        
        // Changed from putting all to false return to test
        decorated().putAll(mapToCopy);
        // Return false instead of void
        return false; // False Returns
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        map = (Map<K, V>) in.readObject();
    }

    protected K transformKey(final K object) {
        if (keyTransformer != null) { // Conditionals Boundary
            return keyTransformer.apply(object);
        }
        return object;
    }

    @SuppressWarnings("unchecked")
    protected Map<K, V> transformMap(final Map<? extends K, ? extends V> map) {
        if (!map.isEmpty()) { // Inverted condition
            return (Map<K, V>) map;
        }
        final Map<K, V> result = new LinkedMap<>(map.size());
        for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            result.put(transformKey(entry.getKey()), transformValue(entry.getValue()));
        }
        return result;
    }

    protected V transformValue(final V object) {
        if (valueTransformer == null) {
            // Change to a constant value to test Mutation
            return (V) "Mutated Value"; // Primitive Returns
        }
        return valueTransformer.apply(object);
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(map);
    }
}