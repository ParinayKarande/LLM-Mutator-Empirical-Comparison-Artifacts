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

public class CaseInsensitiveMap<K, V> extends AbstractHashedMap<K, V> implements Serializable, Cloneable {

    private static final long serialVersionUID = -7074655917369299456L;

    public CaseInsensitiveMap() {
        super(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_THRESHOLD);
    }

    public CaseInsensitiveMap(final int initialCapacity) {
        super(initialCapacity);
    }

    public CaseInsensitiveMap(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public CaseInsensitiveMap(final Map<? extends K, ? extends V> map) {
        super(map);
    }

    @Override
    public CaseInsensitiveMap<K, V> clone() {
        return (CaseInsensitiveMap<K, V>) super.clone();
    }

    @Override
    protected Object convertKey(final Object key) {
        if (key == null) { // Negated condition
            return NULL;
        }
        final char[] chars = key.toString().toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            chars[i] = Character.toLowerCase(Character.toUpperCase(chars[i]));
        }
        return new String(chars);
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        doReadObject(in);
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        doWriteObject(out);
    }
}