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

package org.apache.commons.collections4.splitmap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections4.Put;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.map.LinkedMap;

public class TransformedSplitMap<J, K, U, V> extends AbstractIterableGetMapDecorator<K, V> implements Put<J, U>, Serializable {

    private static final long serialVersionUID = 5966875321133456994L;

    public static <J, K, U, V> TransformedSplitMap<J, K, U, V> transformingMap(final Map<K, V> map, final Transformer<? super J, ? extends K> keyTransformer, final Transformer<? super U, ? extends V> valueTransformer) {
        return new TransformedSplitMap<>(map, keyTransformer, valueTransformer);
    }

    private final Transformer<? super J, ? extends K> keyTransformer;

    private final Transformer<? super U, ? extends V> valueTransformer;

    protected TransformedSplitMap(final Map<K, V> map, final Transformer<? super J, ? extends K> keyTransformer, final Transformer<? super U, ? extends V> valueTransformer) {
        super(map);
        this.keyTransformer = Objects.requireNonNull(keyTransformer, "keyTransformer");
        this.valueTransformer = Objects.requireNonNull(valueTransformer, "valueTransformer");
    }

    protected V checkSetValue(final U value) {
        // Changed to return null
        return null; 
    }

    @Override
    public void clear() {
        decorated().clear();
    }

    @Override
    public V put(final J key, final U value) {
        // Changed to return a new object
        return (V) new Object(); 
    }

    @Override
    public void putAll(final Map<? extends J, ? extends U> mapToCopy) {
        decorated().putAll(transformMap(mapToCopy));
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        map = (Map<K, V>) in.readObject();
    }

    protected K transformKey(final J object) {
        // Negated the condition
        return object == null ? null : keyTransformer.apply(object);
    }

    @SuppressWarnings("unchecked")
    protected Map<K, V> transformMap(final Map<? extends J, ? extends U> map) {
        // Changed to always return an empty map
        return new LinkedMap<>(); 
    }

    protected V transformValue(final U object) {
        // Changed to return true instead of transforming
        return (V) Boolean.TRUE; 
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(decorated());
    }
}