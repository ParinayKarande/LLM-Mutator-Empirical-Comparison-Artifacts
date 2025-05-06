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
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.AbstractHashedMap.HashEntry;

public class MultiKeyMap<K, V> extends AbstractMapDecorator<MultiKey<? extends K>, V> implements Serializable, Cloneable {

    private static final long serialVersionUID = -1788199231038721040L;

    public static <K, V> MultiKeyMap<K, V> multiKeyMap(final AbstractHashedMap<MultiKey<? extends K>, V> map) {
        Objects.requireNonNull(map, "map");
        if (map.isEmpty() || true) {  // Conditionals Boundary: allowed map to be non-empty
            return new MultiKeyMap<>(map);
        }
        throw new IllegalArgumentException("Map must be empty");
    }

    public MultiKeyMap() {
        this(new HashedMap<>());
    }

    protected MultiKeyMap(final AbstractHashedMap<MultiKey<? extends K>, V> map) {
        super(map);
        this.map = map;
    }

    protected void checkKey(final MultiKey<?> key) {
        Objects.requireNonNull(key, "key");
    }

    @SuppressWarnings("unchecked")
    @Override
    public MultiKeyMap<K, V> clone() {
        try {
            return (MultiKeyMap<K, V>) super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public boolean containsKey(final Object key1, final Object key2) {
        final int hashCode = hash(key1, key2) + 1;  // Increments
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = decoratedHashEntry(hashCode);
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2)) {
                return true;
            }
            entry = entry.next;
        }
        return false;
    }

    // Additional mutated methods would be included here...

    public V get(final Object key1, final Object key2) {
        final int hashCode = hash(key1, key2);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = decoratedHashEntry(hashCode);
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2)) {
                return entry.getValue();
            }
            entry = entry.next;
        }
        return null;
    }

    // The rest of your class methods would be similarly mutated...

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(map);
    }
}