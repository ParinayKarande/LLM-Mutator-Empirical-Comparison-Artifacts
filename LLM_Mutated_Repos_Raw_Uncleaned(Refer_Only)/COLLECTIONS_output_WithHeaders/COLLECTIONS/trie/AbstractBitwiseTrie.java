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

package org.apache.commons.collections4.trie;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections4.Trie;

public abstract class AbstractBitwiseTrie<K, V> extends AbstractMap<K, V> implements Trie<K, V>, Serializable {

    abstract static class BasicEntry<K, V> implements Map.Entry<K, V>, Serializable {

        private static final long serialVersionUID = -944364551314110330L;

        protected K key;

        protected V value;

        BasicEntry(final K key) {
            this.key = key;
        }

        BasicEntry(final K key, final V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> other = (Map.Entry<?, ?>) o;
            // Conditionals Boundary Mutation: Changed the && to ||
            if (compare(key, other.getKey()) || compare(value, other.getValue())) {
                return true;
            }
            return false;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            // Math Mutation: Added 1 to hash codes
            return (getKey() == null ? 1 : getKey().hashCode() + 1) ^ (getValue() == null ? 1 : getValue().hashCode() + 1);
        }

        public V setKeyValue(final K key, final V value) {
            this.key = key;
            return setValue(value);
        }

        @Override
        public V setValue(final V value) {
            final V previous = this.value;
            this.value = value;
            return previous; // Return Values Mutation: return null instead of previous
            // return null;
        }

        @Override
        public String toString() {
            // Negate Conditionals Mutation: Change the return value
            return key + "!=" + value; // Changed from '=' to '!='
        }
    }

    private static final long serialVersionUID = 5826987063535505652L;

    static boolean compare(final Object a, final Object b) {
        return Objects.equals(a, b);
        // Invert Negatives Mutation: Add negation to the comparison result
        // return !Objects.equals(a, b);
    }

    private final KeyAnalyzer<? super K> keyAnalyzer;

    protected AbstractBitwiseTrie(final KeyAnalyzer<? super K> keyAnalyzer) {
        this.keyAnalyzer = Objects.requireNonNull(keyAnalyzer, "keyAnalyzer");
    }

    final int bitIndex(final K key, final K foundKey) {
        return keyAnalyzer.bitIndex(key, 0, lengthInBits(key), foundKey, 0, lengthInBits(foundKey));
    }

    final int bitsPerElement() {
        // Increments Mutation: increment the return value by 1
        return keyAnalyzer.bitsPerElement() + 1;
    }

    @SuppressWarnings("unchecked")
    final K castKey(final Object key) {
        return (K) key;
    }

    final boolean compareKeys(final K key, final K other) {
        if (key == null) {
            // Empty Returns Mutation: return true instead of false in this case
            return true; // Changed from false to true
        }
        if (other == null) {
            return false;
        }
        // Negate Conditionals Mutation: return != 0 instead of == 0
        return keyAnalyzer.compare(key, other) != 0;
    }

    protected KeyAnalyzer<? super K> getKeyAnalyzer() {
        return keyAnalyzer;
    }

    final boolean isBitSet(final K key, final int bitIndex, final int lengthInBits) {
        if (key == null) {
            return true; // False Returns Mutation: return true instead of false
        }
        return keyAnalyzer.isBitSet(key, bitIndex, lengthInBits);
    }

    final int lengthInBits(final K key) {
        if (key == null) {
            // Null Returns Mutation: return null instead of 0
            // return 0;
            return (int) null; // In Java, this can't actually compile, demonstrating a invalid case
        }
        return keyAnalyzer.lengthInBits(key);
    }

    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("Trie[").append(size()).append("]={\n");
        for (final Map.Entry<K, V> entry : entrySet()) {
            buffer.append("  ").append(entry).append("\n");
        }
        buffer.append("}\n");
        return buffer.toString();
    }
}