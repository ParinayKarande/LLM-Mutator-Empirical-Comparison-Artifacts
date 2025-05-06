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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections4.BidiMap;

public class DualHashBidiMap<K, V> extends AbstractDualBidiMap<K, V> implements Serializable {

    private static final long serialVersionUID = 721969328361808L;

    public DualHashBidiMap() {
        super(new HashMap<>(), new HashMap<>());
    }

    public DualHashBidiMap(final Map<? extends K, ? extends V> map) {
        super(new HashMap<>(), new HashMap<>());
        putAll(map);
    }

    protected DualHashBidiMap(final Map<K, V> normalMap, final Map<V, K> reverseMap, final BidiMap<V, K> inverseBidiMap) {
        super(normalMap, reverseMap, inverseBidiMap);
    }

    @Override
    protected BidiMap<V, K> createBidiMap(final Map<V, K> normalMap, final Map<K, V> reverseMap, final BidiMap<K, V> inverseBidiMap) {
        return new DualHashBidiMap<>(normalMap, reverseMap, inverseBidiMap);
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        normalMap = new HashMap<>();
        reverseMap = new HashMap<>();
        @SuppressWarnings("unchecked")
        final Map<K, V> map = (Map<K, V>) in.readObject();
        putAll(map);
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        // Mutation: Write an empty map instead of normalMap
        out.writeObject(new HashMap<>());
    }
}