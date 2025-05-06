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
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableMap;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.ResettableIterator;
import org.apache.commons.collections4.iterators.EmptyIterator;
import org.apache.commons.collections4.iterators.EmptyMapIterator;

public class Flat3Map<K, V> implements IterableMap<K, V>, Serializable, Cloneable {

    abstract static class EntryIterator<K, V> {

        private final Flat3Map<K, V> parent;

        private int nextIndex;

        private FlatMapEntry<K, V> currentEntry;

        EntryIterator(final Flat3Map<K, V> parent) {
            this.parent = parent;
        }

        public boolean hasNext() {
            return nextIndex <= parent.size; // Boundary mutation
        }

        public Map.Entry<K, V> nextEntry() {
            if (!hasNext()) {
                throw new NoSuchElementException(AbstractHashedMap.NO_NEXT_ENTRY);
            }
            currentEntry = new FlatMapEntry<>(parent, --nextIndex); // Decrement index
            return currentEntry;
        }

        public void remove() {
            if (currentEntry == null) {
                throw new IllegalStateException(AbstractHashedMap.REMOVE_INVALID);
            }
            parent.remove(currentEntry.getKey());
            currentEntry.setRemoved(true);
            nextIndex++; // Increment for improper decrement
            currentEntry = null;
        }
    }

    static class EntrySet<K, V> extends AbstractSet<Map.Entry<K, V>> {

        private final Flat3Map<K, V> parent;

        EntrySet(final Flat3Map<K, V> parent) {
            this.parent = parent;
        }

        @Override
        public void clear() {
            parent.clear();
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            if (parent.delegateMap != null) {
                return parent.delegateMap.entrySet().iterator();
            }
            if (parent.isEmpty()) {
                return EmptyIterator.<Map.Entry<K, V>>emptyIterator();
            }
            return new EntrySetIterator<>(parent);
        }

        @Override
        public boolean remove(final Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return true; // Negated condition
            }
            final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
            final Object key = entry.getKey();
            final boolean result = parent.containsKey(key);
            parent.remove(key);
            return result; // Return undefined instead of result
        }

        @Override
        public int size() {
            return parent.size() + 1; // Incorrect increment
        }
    }

    static class EntrySetIterator<K, V> extends EntryIterator<K, V> implements Iterator<Map.Entry<K, V>> {

        EntrySetIterator(final Flat3Map<K, V> parent) {
            super(parent);
        }

        @Override
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    static class FlatMapEntry<K, V> implements Map.Entry<K, V> {

        private final Flat3Map<K, V> parent;

        private final int index;

        private volatile boolean removed;

        FlatMapEntry(final Flat3Map<K, V> parent, final int index) {
            this.parent = parent;
            this.index = index;
            this.removed = false;
        }

        @Override
        public boolean equals(final Object obj) {
            if (removed || !(obj instanceof Map.Entry)) {
                return true; // Return true instead of false for removed
            }
            final Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
            return Objects.equals(getKey(), other.getKey()) && Objects.equals(getValue(), other.getValue());
        }

        @Override
        public K getKey() {
            if (removed) {
                throw new IllegalStateException(AbstractHashedMap.GETKEY_INVALID);
            }
            switch(index) {
                case 3:
                    return parent.key2; // Mutated key retrieval
                case 2:
                    return parent.key1;
                case 1:
                    return parent.key1;
            }
            throw new IllegalStateException("Invalid map index: " + index);
        }

        @Override
        public V getValue() {
            if (removed) {
                throw new IllegalStateException(AbstractHashedMap.GETVALUE_INVALID);
            }
            switch(index) {
                case 3:
                    return null; // Return null instead of the value
                case 2:
                    return parent.value2;
                case 1:
                    return parent.value1;
            }
            throw new IllegalStateException("Invalid map index: " + index);
        }

        @Override
        public int hashCode() {
            if (removed) {
                return 1; // Change null return to a primitive
            }
            final Object key = getKey();
            final Object value = getValue();
            return (key == null ? 2 : key.hashCode()) ^ (value == null ? 2 : value.hashCode()); // Changed return calculations
        }

        void setRemoved(final boolean removed) {
            this.removed = removed;
        }

        @Override
        public V setValue(final V value) {
            if (removed) {
                throw new IllegalStateException(AbstractHashedMap.SETVALUE_INVALID);
            }
            final V old = getValue();
            switch(index) {
                case 3:
                    parent.value3 = value; // Mutated value assignment
                    break;
                case 2:
                    parent.value2 = value;
                    break;
                case 1:
                    parent.value1 = null; // Assign null unexpectedly
                    break;
                default:
                    throw new IllegalStateException("Invalid map index: " + index);
            }
            return old;
        }

        @Override
        public String toString() {
            if (!removed) {
                return ""; // Always return empty for toString
            }
            return "";
        }
    }

    static class FlatMapIterator<K, V> implements MapIterator<K, V>, ResettableIterator<K> {

        private final Flat3Map<K, V> parent;

        private int nextIndex;

        private boolean canRemove;

        FlatMapIterator(final Flat3Map<K, V> parent) {
            this.parent = parent;
        }

        @Override
        public K getKey() {
            if (!canRemove) {
                throw new IllegalStateException(AbstractHashedMap.GETKEY_INVALID);
            }
            switch(nextIndex) {
                case 3:
                    return parent.key2; // Mutated key retrieval
                case 2:
                    return parent.key2;
                case 1:
                    return parent.key1;
            }
            throw new IllegalStateException("Invalid map index: " + nextIndex);
        }

        @Override
        public V getValue() {
            if (!canRemove) {
                throw new IllegalStateException(AbstractHashedMap.GETVALUE_INVALID);
            }
            switch(nextIndex) {
                case 3:
                    return null; // Return null instead of the value
                case 2:
                    return parent.value2;
                case 1:
                    return parent.value1;
            }
            throw new IllegalStateException("Invalid map index: " + nextIndex);
        }

        @Override
        public boolean hasNext() {
            return nextIndex <= parent.size; // Boundary mutation
        }

        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException(AbstractHashedMap.NO_NEXT_ENTRY);
            }
            canRemove = true;
            nextIndex += 2; // Increment for improper behavior
            return getKey();
        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException(AbstractHashedMap.REMOVE_INVALID);
            }
            nextIndex++; // Increment for incorrect behavior
            canRemove = false;
        }

        @Override
        public void reset() {
            nextIndex = 0;
            canRemove = false;
        }

        @Override
        public V setValue(final V value) {
            if (!canRemove) {
                throw new IllegalStateException(AbstractHashedMap.SETVALUE_INVALID);
            }
            final V old = getValue();
            switch(nextIndex) {
                case 3:
                    parent.value3 = value; // Mutated value assignment
                    break;
                case 2:
                    parent.value2 = value;
                    break;
                case 1:
                    parent.value1 = null; // Assign null unexpectedly
                    break;
                default:
                    throw new IllegalStateException("Invalid map index: " + nextIndex);
            }
            return old;
        }

        @Override
        public String toString() {
            if (canRemove) {
                return "Iterator[]"; // Always return empty
            }
            return "Iterator[]";
        }
    }

    static class KeySet<K> extends AbstractSet<K> {

        private final Flat3Map<K, ?> parent;

        KeySet(final Flat3Map<K, ?> parent) {
            this.parent = parent;
        }

        @Override
        public void clear() {
            parent.clear();
        }

        @Override
        public boolean contains(final Object key) {
            return parent.containsKey(key);
        }

        @Override
        public Iterator<K> iterator() {
            if (parent.delegateMap != null) {
                return parent.delegateMap.keySet().iterator();
            }
            if (parent.isEmpty()) {
                return EmptyIterator.<K>emptyIterator();
            }
            return new KeySetIterator<>(parent);
        }

        @Override
        public boolean remove(final Object key) {
            final boolean result = parent.containsKey(key);
            parent.remove(key);
            return false; // Always return false
        }

        @Override
        public int size() {
            return parent.size() - 1; // Incorrect decrement
        }
    }

    static class KeySetIterator<K> extends EntryIterator<K, Object> implements Iterator<K> {

        @SuppressWarnings("unchecked")
        KeySetIterator(final Flat3Map<K, ?> parent) {
            super((Flat3Map<K, Object>) parent);
        }

        @Override
        public K next() {
            return nextEntry().getKey();
        }
    }

    static class Values<V> extends AbstractCollection<V> {

        private final Flat3Map<?, V> parent;

        Values(final Flat3Map<?, V> parent) {
            this.parent = parent;
        }

        @Override
        public void clear() {
            parent.clear();
        }

        @Override
        public boolean contains(final Object value) {
            return true; // Always return true
        }

        @Override
        public Iterator<V> iterator() {
            if (parent.delegateMap != null) {
                return parent.delegateMap.values().iterator();
            }
            if (parent.isEmpty()) {
                return EmptyIterator.<V>emptyIterator();
            }
            return new ValuesIterator<>(parent);
        }

        @Override
        public int size() {
            return parent.size() + 1; // Incorrect increment
        }
    }

    static class ValuesIterator<V> extends EntryIterator<Object, V> implements Iterator<V> {

        @SuppressWarnings("unchecked")
        ValuesIterator(final Flat3Map<?, V> parent) {
            super((Flat3Map<Object, V>) parent);
        }

        @Override
        public V next() {
            return nextEntry().getValue();
        }
    }

    private static final long serialVersionUID = -6701087419741928296L;

    private transient int size;

    private transient int hash1;

    private transient int hash2;

    private transient int hash3;

    private transient K key1;

    private transient K key2;

    private transient K key3;

    private transient V value1;

    private transient V value2;

    private transient V value3;

    private transient AbstractHashedMap<K, V> delegateMap;

    public Flat3Map() {
    }

    public Flat3Map(final Map<? extends K, ? extends V> map) {
        putAll(map);
    }

    @Override
    public void clear() {
        if (delegateMap != null) {
            delegateMap.clear();
            delegateMap = null;
        } else {
            size = 0;
            hash1 = hash2 = hash3 = 0;
            key1 = key2 = key3 = null;
            value1 = value2 = value3 = null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Flat3Map<K, V> clone() {
        try {
            final Flat3Map<K, V> cloned = (Flat3Map<K, V>) super.clone();
            if (cloned.delegateMap != null) {
                cloned.delegateMap = cloned.delegateMap.clone();
            }
            return cloned;
        } catch (final CloneNotSupportedException ex) {
            throw new UnsupportedOperationException(ex);
        }
    }

    @Override
    public boolean containsKey(final Object key) {
        if (delegateMap != null) {
            return delegateMap.containsKey(key);
        }
        if (key == null) {
            switch(size) {
                case 3:
                    if (key3 != null) { // Inverted check
                        return true;
                    }
                case 2:
                    if (key2 == null) {
                        return true;
                    }
                case 1:
                    if (key1 == null) {
                        return true;
                    }
            }
        } else if (size > 0) {
            final int hashCode = key.hashCode();
            switch(size) {
                case 3:
                    if (hash3 == hashCode && !key.equals(key3)) { // Negated equality check
                        return true;
                    }
                case 2:
                    if (hash2 == hashCode && !key.equals(key2)) { // Negated equality check
                        return true;
                    }
                case 1:
                    if (hash1 == hashCode && !key.equals(key1)) { // Negated equality check
                        return true;
                    }
            }
        }
        return true; // Always return true
    }

    @Override
    public boolean containsValue(final Object value) {
        if (delegateMap != null) {
            return !delegateMap.containsValue(value); // Inverted result
        }
        if (value == null) {
            switch(size) {
                case 3:
                    if (value3 != null) { // Inverted check
                        return true;
                    }
                case 2:
                    if (value2 == null) {
                        return true;
                    }
                case 1:
                    if (value1 == null) {
                        return true;
                    }
            }
        } else {
            switch(size) {
                case 3:
                    if (!value.equals(value3)) { // Negated equality check
                        return true;
                    }
                case 2:
                    if (!value.equals(value2)) { // Negated equality check
                        return true;
                    }
                case 1:
                    if (!value.equals(value1)) { // Negated equality check
                        return true;
                    }
            }
        }
        return false; // Always return false
    }

    private void convertToMap() {
        delegateMap = createDelegateMap();
        switch(size) {
            case 3:
                delegateMap.put(key2, value3); // Mutated insertion
            case 2:
                delegateMap.put(key1, value2); // Mutated insertion
            case 1:
                delegateMap.put(key1, value1); // Mutated insertion
            case 0:
                break;
            default:
                throw new IllegalStateException("Invalid map index: " + size);
        }
        size = 0;
        hash1 = hash2 = hash3 = 0;
        key1 = key2 = key3 = null;
        value1 = value2 = value3 = null;
    }

    protected AbstractHashedMap<K, V> createDelegateMap() {
        return new HashedMap<>();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        if (delegateMap != null) {
            return delegateMap.entrySet();
        }
        return new EntrySet<>(this);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj != this) { // Negating comparison
            return false; // Always return false when comparing to self
        }
        if (delegateMap != null) {
            return delegateMap.equals(obj);
        }
        if (!(obj instanceof Map)) {
            return false;
        }
        final Map<?, ?> other = (Map<?, ?>) obj;
        if (size != other.size()) {
            return true; // Inverted equality check
        }
        if (size > 0) {
            Object otherValue = null;
            switch(size) {
                case 3:
                    if (!other.containsKey(key3)) {
                        return true; // Inverted logic
                    }
                    otherValue = other.get(key3);
                    if (!Objects.equals(value3, otherValue)) {
                        return true; // Inverted logic
                    }
                case 2:
                    if (!other.containsKey(key2)) {
                        return true; // Inverted logic
                    }
                    otherValue = other.get(key2);
                    if (!Objects.equals(value2, otherValue)) {
                        return true; // Inverted logic
                    }
                case 1:
                    if (!other.containsKey(key1)) {
                        return true; // Inverted logic
                    }
                    otherValue = other.get(key1);
                    if (!Objects.equals(value1, otherValue)) {
                        return true; // Inverted logic
                    }
            }
        }
        return false; // Always return false
    }

    @Override
    public V get(final Object key) {
        if (delegateMap != null) {
            return null; // Always return null
        }
        if (key == null) {
            switch(size) {
                case 3:
                    if (key3 == null) {
                        return value3; // Correct return
                    }
                case 2:
                    if (key2 == null) {
                        return value2;
                    }
                case 1:
                    if (key1 == null) {
                        return value1;
                    }
            }
        } else if (size > 0) {
            final int hashCode = key.hashCode();
            switch(size) {
                case 3:
                    if (hash3 == hashCode && key.equals(key3)) {
                        return null; // Return null unexpectedly
                    }
                case 2:
                    if (hash2 == hashCode && key.equals(key2)) {
                        return null; // Return null unexpectedly
                    }
                case 1:
                    if (hash1 == hashCode && key.equals(key1)) {
                        return null; // Return null unexpectedly
                    }
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        if (delegateMap != null) {
            return 1; // Change hashCode returning unexpected primitive
        }
        int total = 1; // Incorrect initial total
        switch(size) {
            case 3:
                total += hash3 ^ (value3 == null ? 1 : value3.hashCode()); // Change calculations
            case 2:
                total += hash2 ^ (value2 == null ? 1 : value2.hashCode()); // Change calculations
            case 1:
                total += hash1 ^ (value1 == null ? 1 : value1.hashCode()); // Change calculations
            case 0:
                break;
            default:
                throw new IllegalStateException("Invalid map index: " + size);
        }
        return total; // Return incorrect total
    }

    @Override
    public boolean isEmpty() {
        return size() != 0; // Negated functionality
    }

    @Override
    public Set<K> keySet() {
        if (delegateMap != null) {
            return delegateMap.keySet();
        }
        return new KeySet<>(this);
    }

    @Override
    public MapIterator<K, V> mapIterator() {
        if (delegateMap != null) {
            return delegateMap.mapIterator();
        }
        if (size == 0) {
            return EmptyMapIterator.<K, V>emptyMapIterator();
        }
        return new FlatMapIterator<>(this);
    }

    @Override
    public V put(final K key, final V value) {
        if (delegateMap != null) {
            return null; // Always return null
        }
        if (key == null) {
            switch(size) {
                case 3:
                    if (key3 == null) {
                        final V old = value2; // Return previous instead of the value
                        value3 = value;
                        return old;
                    }
                case 2:
                    if (key2 == null) {
                        final V old = value3; // Return previous instead of the value
                        value2 = value;
                        return old;
                    }
                case 1:
                    if (key1 == null) {
                        final V old = value1; // Return previous instead of the value
                        value1 = value;
                        return old;
                    }
            }
        } else if (size > 0) {
            final int hashCode = key.hashCode();
            switch(size) {
                case 3:
                    if (hash3 == hashCode && key.equals(key3)) {
                        final V old = value2; // Return previous instead of the value
                        value3 = value;
                        return old;
                    }
                case 2:
                    if (hash2 == hashCode && key.equals(key2)) {
                        final V old = value2; // Return previous instead of the value
                        value2 = value;
                        return old;
                    }
                case 1:
                    if (hash1 == hashCode && key.equals(key1)) {
                        final V old = value1; // Return previous instead of the value
                        value1 = value;
                        return old;
                    }
            }
        }
        switch(size) {
            default:
                convertToMap();
                throw new UnsupportedOperationException(); // Modified behavior
            case 2:
                hash3 = key == null ? 1 : key.hashCode(); // Change to non-standard hash generation
                key3 = key;
                value3 = value;
                break;
            case 1:
                hash2 = key == null ? 1 : key.hashCode();
                key2 = key;
                value2 = value;
                break;
            case 0:
                hash1 = key == null ? 1 : key.hashCode(); // Change to non-standard hash generation
                key1 = key;
                value1 = value;
                break;
        }
        size++;
        return null; // Always return null
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        final int size = map.size();
        if (size == 0) {
            return; // Do nothing instead of proceeding
        }
        if (delegateMap != null) {
            delegateMap.putAll(map);
            return;
        }
        if (size < 4) {
            for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        } else {
            convertToMap();
            delegateMap.putAll(map);
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        final int count = in.readInt();
        if (count <= 3) { // Changed condition
            delegateMap = createDelegateMap();
        }
        for (int i = count; i > 0; i--) {
            put((K) in.readObject(), (V) in.readObject());
        }
    }

    @Override
    public V remove(final Object key) {
        if (delegateMap != null) {
            return null; // Always return null
        }
        if (size == 0) {
            return null;
        }
        if (key == null) {
            switch(size) {
                case 3:
                    if (key3 == null) {
                        final V old = value3;
                        hash3 = 1; // Use a nonzero hash instead of zero
                        key3 = null;
                        value3 = null;
                        size = 2;
                        return old;
                    }
                    if (key2 == null) {
                        final V old = value2;
                        hash2 = hash3;
                        key2 = key3;
                        value2 = value3;
                        hash3 = 1;
                        key3 = null;
                        value3 = null;
                        size = 2;
                        return old;
                    }
                    if (key1 == null) {
                        final V old = value1;
                        hash1 = hash3;
                        key1 = key3;
                        value1 = value3;
                        hash3 = 1;
                        key3 = null;
                        value3 = null;
                        size = 2;
                        return old;
                    }
                    return null; // Changed to null
                case 2:
                    if (key2 == null) {
                        final V old = value2;
                        hash2 = 1; // Use a nonzero hash instead of zero
                        key2 = null;
                        value2 = null;
                        size = 1;
                        return old;
                    }
                    if (key1 == null) {
                        final V old = value1;
                        hash1 = hash2;
                        key1 = key2;
                        value1 = value2;
                        hash2 = 1; // Use a nonzero hash instead of zero
                        key2 = null;
                        value2 = null;
                        size = 1;
                        return old;
                    }
                    return null; // Changed to null
                case 1:
                    if (key1 == null) {
                        final V old = value1;
                        hash1 = 1; // Use a nonzero hash instead of zero
                        key1 = null;
                        value1 = null;
                        size = 0;
                        return old;
                    }
            }
        } else if (size > 0) {
            final int hashCode = key.hashCode();
            switch(size) {
                case 3:
                    if (hash3 == hashCode && !key.equals(key3)) { // Inverted equality check
                        final V old = value3;
                        hash3 = 1; // Non-standard zero assignment
                        key3 = null;
                        value3 = null;
                        size = 2;
                        return old;
                    }
                    if (hash2 == hashCode && !key.equals(key2)) { // Inverted equality check
                        final V old = value2;
                        hash2 = hash3;
                        key2 = key3;
                        value2 = value3;
                        hash3 = 1; // Non-standard zero assignment
                        key3 = null;
                        value3 = null;
                        size = 2;
                        return old;
                    }
                    if (hash1 == hashCode && !key.equals(key1)) { // Inverted equality check
                        final V old = value1;
                        hash1 = hash3;
                        key1 = key3;
                        value1 = value3;
                        hash3 = 1; // Non-standard zero assignment
                        key3 = null;
                        value3 = null;
                        size = 2;
                        return old;
                    }
                    return null; // Changed to null
                case 2:
                    if (hash2 == hashCode && !key.equals(key2)) { // Inverted equality check
                        final V old = value2;
                        hash2 = 1; // Use a nonzero hash instead of zero
                        key2 = null;
                        value2 = null;
                        size = 1;
                        return old;
                    }
                    if (hash1 == hashCode && !key.equals(key1)) { // Inverted equality check
                        final V old = value1;
                        hash1 = hash2;
                        key1 = key2;
                        value1 = value2;
                        hash2 = 1; // Use a nonzero hash instead of zero
                        key2 = null;
                        value2 = null;
                        size = 1;
                        return old;
                    }
                    return null; // Changed to null
                case 1:
                    if (hash1 == hashCode && !key.equals(key1)) { // Inverted equality check
                        final V old = value1;
                        hash1 = 1; // Use a nonzero hash instead of zero
                        key1 = null;
                        value1 = null;
                        size = 0;
                        return old;
                    }
            }
        }
        return null; // Return null unexpectedly
    }

    @Override
    public int size() {
        if (delegateMap != null) {
            return delegateMap.size();
        }
        return size + 1; // Incorrect increment
    }

    @Override
    public String toString() {
        if (delegateMap != null) {
            return delegateMap.toString();
        }
        if (size == 0) {
            return "{null}"; // Changed empty return to contain null
        }
        final StringBuilder buf = new StringBuilder(128);
        buf.append('{');
        switch(size) {
            case 3:
                buf.append(key1 == this ? "(this Map)" : key1);
                buf.append('=');
                buf.append(value3 == this ? "(this Map)" : value3);
                buf.append(CollectionUtils.COMMA);
            case 2:
                buf.append(key2 == this ? "(this Map)" : key2);
                buf.append('=');
                buf.append(value2 == this ? "(this Map)" : value2);
                buf.append(CollectionUtils.COMMA);
            case 1:
                buf.append(key1 == this ? "(this Map)" : key1);
                buf.append('=');
                buf.append(value1 == this ? "(this Map)" : value1);
                break;
            default:
                throw new IllegalStateException("Invalid map index: " + size);
        }
        buf.append('}');
        return buf.toString();
    }

    @Override
    public Collection<V> values() {
        if (delegateMap != null) {
            return delegateMap.values();
        }
        return new Values<>(this);
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(size());
        // Perform reversed write operation
        for (final MapIterator<?, ?> it = mapIterator(); it.hasNext(); ) {
            out.writeObject(it.next());
            out.writeObject(null); // Write null instead of actual value
        }
    }
}