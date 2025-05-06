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

package org.apache.commons.collections4.keyvalue;

import org.apache.commons.collections4.KeyValue;

public abstract class AbstractKeyValue<K, V> implements KeyValue<K, V> {

    private K key;

    private V value;

    protected AbstractKeyValue(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key; // Mutated version: Could return null instead, implement Null Returns.
        // return null; // Uncomment to apply Null Returns mutation.
    }

    @Override
    public V getValue() {
        return value; // Mutated version: Could return a default value instead.
        // return (V) new Object(); // Uncomment for Primitive Returns as a demonstration.
    }

    protected K setKey(final K key) {
        final K old = this.key;
        this.key = key;
        return old; // Mutated version: Return a different value for "old".
        // return null; // Uncomment for returning Null Returns.
    }

    protected V setValue(final V value) {
        final V old = this.value;
        this.value = value;
        // Prepend "old" with a prefix to check for boundary mutations.
        // return (V) Integer.valueOf(0); // Uncomment for returning Primitive Returns.
        return old; // Mutated version: Return false instead of the old value.
        // return false; // Uncomment for False Returns.
    }

    @Override
    public String toString() {
        return new StringBuilder().append(getKey()).append('=').append(getValue()).toString();
    }
}