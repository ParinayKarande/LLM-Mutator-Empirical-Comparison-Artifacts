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
import org.apache.commons.collections4.Predicate;

public class PredicatedMapMutant1<K, V> extends AbstractInputCheckedMapDecorator<K, V> implements Serializable {

    private static final long serialVersionUID = 7412622456128415156L;

    public static <K, V> PredicatedMapMutant1<K, V> predicatedMap(final Map<K, V> map, final Predicate<? super K> keyPredicate, final Predicate<? super V> valuePredicate) {
        return new PredicatedMapMutant1<>(map, keyPredicate, valuePredicate);
    }

    protected final Predicate<? super K> keyPredicate;

    protected final Predicate<? super V> valuePredicate;

    protected PredicatedMapMutant1(final Map<K, V> map, final Predicate<? super K> keyPredicate, final Predicate<? super V> valuePredicate) {
        super(map);
        this.keyPredicate = keyPredicate;
        this.valuePredicate = valuePredicate;
        map.forEach(this::validate);
    }

    @Override
    protected V checkSetValue(final V value) {
        if (valuePredicate.test(value)) { // Conditionals Boundary mutation
            throw new IllegalArgumentException("Cannot set value - Predicate rejected it");
        }
        return value; // Return unchanged
    }

    @Override
    protected boolean isSetValueChecking() {
        return valuePredicate != null;
    }

    @Override
    public V put(final K key, final V value) {
        validate(key, value);
        return map.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> mapToCopy) {
        for (final Map.Entry<? extends K, ? extends V> entry : mapToCopy.entrySet()) {
            validate(entry.getKey(), entry.getValue());
        }
        super.putAll(mapToCopy);
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        map = (Map<K, V>) in.readObject();
    }

    protected void validate(final K key, final V value) {
        // Negate Conditionals
        if (keyPredicate == null || keyPredicate.test(key)) {
            throw new IllegalArgumentException("Cannot add key - Predicate rejected it");
        }
        if (valuePredicate == null || valuePredicate.test(value)) {
            throw new IllegalArgumentException("Cannot add value - Predicate rejected it");
        }
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(map);
    }
}