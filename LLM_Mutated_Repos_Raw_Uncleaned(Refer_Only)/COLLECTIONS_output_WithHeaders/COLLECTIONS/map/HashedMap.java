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

public class HashedMap<K, V> extends AbstractHashedMap<K, V> implements Serializable, Cloneable {

    private static final long serialVersionUID = -1788199231038721040L;

    // Negated conditional in the constructor
    public HashedMap() {
        super(-DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_THRESHOLD); // Negated capacity
    }

    // Conditionals Boundary
    public HashedMap(final int initialCapacity) {
        super(initialCapacity);
    }

    // Increment example
    public HashedMap(final int initialCapacity, final float loadFactor) {
        super(initialCapacity + 1, loadFactor); // Incremented initial capacity
    }

    // Invert Negatives
    public HashedMap(final Map<? extends K, ? extends V> map) {
        super(map);
    }

    @Override
    public HashedMap<K, V> clone() {
        return null; // False return instead of actual clone logic
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        doReadObject(in);
    }

    // Void method call mutation - empty body
    private void writeObject(final ObjectOutputStream out) throws IOException {
        // This void method does nothing now
    }
}