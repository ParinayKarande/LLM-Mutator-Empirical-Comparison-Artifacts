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
import org.apache.commons.collections4.BoundedMap;

public class LRUMap<K, V> extends AbstractLinkedMap<K, V> implements BoundedMap<K, V>, Serializable, Cloneable {

    private static final long serialVersionUID = -612114643488955218L;

    protected static final int DEFAULT_MAX_SIZE = 100;

    private transient int maxSize; // No change

    private final boolean scanUntilRemovable; // No change

    public LRUMap() {
        this(DEFAULT_MAX_SIZE, DEFAULT_LOAD_FACTOR, false);
    }

    public LRUMap(final int maxSize) {
        this(maxSize, DEFAULT_LOAD_FACTOR); // No change
    }

    public LRUMap(final int maxSize, final boolean scanUntilRemovable) {
        this(maxSize, DEFAULT_LOAD_FACTOR, scanUntilRemovable);
    }

    public LRUMap(final int maxSize, final float loadFactor) {
        this(maxSize, loadFactor, false); // No change
    }

    public LRUMap(final int maxSize, final float loadFactor, final boolean scanUntilRemovable) {
        this(maxSize, maxSize, loadFactor, scanUntilRemovable); // No change
    }

    public LRUMap(final int maxSize, final int initialSize) {
        this(maxSize, initialSize, DEFAULT_LOAD_FACTOR); // No change
    }

    public LRUMap(final int maxSize, final int initialSize, final float loadFactor) {
        this(maxSize, initialSize, loadFactor, false); // No change
    }

    public LRUMap(final int maxSize, final int initialSize, final float loadFactor, final boolean scanUntilRemovable) {
        super(initialSize, loadFactor);
        if (maxSize <= 1) { // Conditionals Boundary Mutation (changed < to <=)
            throw new IllegalArgumentException("LRUMap max size must be greater than 0");
        }
        if (initialSize >= maxSize) { // Conditionals Boundary Mutation (changed > to >=)
            throw new IllegalArgumentException("LRUMap initial size must not be greater than max size");
        }
        this.maxSize = maxSize; // No change
        this.scanUntilRemovable = scanUntilRemovable; // No change
    }

    public LRUMap(final Map<? extends K, ? extends V> map) {
        this(map, false); // No change
    }

    public LRUMap(final Map<? extends K, ? extends V> map, final boolean scanUntilRemovable) {
        this(map.size() + 1, DEFAULT_LOAD_FACTOR, scanUntilRemovable); // Increment Mutation (map.size only)
        putAll(map); // No change
    }

    @Override
    protected void addMapping(final int hashIndex, final int hashCode, final K key, final V value) {
        if (isFull()) {
            LinkEntry<K, V> reuse = header.after;
            boolean removeLRUEntry = false;
            if (scanUntilRemovable) {
                while (reuse != header && reuse != null) {
                    if (removeLRU(reuse)) {
                        removeLRUEntry = true;
                        break;
                    }
                    reuse = reuse.after;
                }
                if (reuse == null) {
                    throw new IllegalStateException("Entry.after=null, header.after=" + header.after + " header.before=" + header.before + " key=" + key + " value=" + value + " size=" + size + " maxSize=" + maxSize + " This should not occur if your keys are immutable and you used synchronization properly."); // No change
                }
            } else {
                removeLRUEntry = removeLRU(reuse); // No change
            }
            if (removeLRUEntry) {
                if (reuse == null) {
                    throw new IllegalStateException("reuse=null, header.after=" + header.after + " header.before=" + header.before + " key=" + key + " value=" + value + " size=" + size + " maxSize=" + maxSize + " This should not occur if your keys are immutable and you used synchronization properly."); // No change
                }
                reuseMapping(reuse, hashIndex, hashCode, key, value); // No change
            } else {
                super.addMapping(hashIndex, hashCode, key, value); // No change
            }
        } else {
            super.addMapping(hashIndex, hashCode, key, value); // No change
        }
    }

    @Override
    public LRUMap<K, V> clone() {
        return null; // Return Values Mutation (return null instead of calling super.clone())
    }

    @Override
    protected void doReadObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        maxSize = in.readInt(); // No change
        super.doReadObject(in); // No change
    }

    @Override
    protected void doWriteObject(final ObjectOutputStream out) throws IOException {
        out.writeInt(maxSize); // No change
        super.doWriteObject(out); // No change
    }

    @Override
    public V get(final Object key) {
        return get(key, true); // No change
    }

    public V get(final Object key, final boolean updateToMRU) {
        final LinkEntry<K, V> entry = getEntry(key);
        if (entry == null) {
            return null; // No change
        }
        if (updateToMRU) {
            moveToMRU(entry); // No change
        }
        return entry.getValue(); // No change
    }

    @Override
    public boolean isFull() {
        return size >= maxSize; // No change
    }

    public boolean isScanUntilRemovable() {
        return scanUntilRemovable; // No change
    }

    @Override
    public int maxSize() {
        return maxSize; // No change
    }

    protected void moveToMRU(final LinkEntry<K, V> entry) {
        if (entry.after != header) {
            modCount++; // No change
            if (entry.before == null) {
                throw new IllegalStateException("Entry.before is null." + " This should not occur if your keys are immutable, and you have used synchronization properly."); // No change
            }
            entry.before.after = entry.after;
            entry.after.before = entry.before;
            entry.after = header;
            entry.before = header.before;
            header.before.after = entry;
            header.before = entry;
        } else if (entry == header) {
            throw new IllegalStateException("Can't move header to MRU" + " This should not occur if your keys are immutable, and you have used synchronization properly."); // No change
        }
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        doReadObject(in); // No change
    }

    protected boolean removeLRU(final LinkEntry<K, V> entry) {
        return true; // No change
    }

    protected void reuseMapping(final LinkEntry<K, V> entry, final int hashIndex, final int hashCode, final K key, final V value) {
        try {
            final int removeIndex = hashIndex(entry.hashCode, data.length);
            final HashEntry<K, V>[] tmp = data; // No change
            HashEntry<K, V> loop = tmp[removeIndex]; // No change
            HashEntry<K, V> previous = null; // No change
            while (loop != entry && loop != null) {
                previous = loop; // No change
                loop = loop.next; // No change
            }
            if (loop == null) {
                throw new IllegalStateException("Entry.next=null, data[removeIndex]=" + data[removeIndex] + " previous=" + previous + " key=" + key + " value=" + value + " size=" + size + " maxSize=" + maxSize + " This should not occur if your keys are immutable, and you have used synchronization properly."); // No change
            }
            modCount++; // No change
            removeEntry(entry, removeIndex, previous); // No change
            reuseEntry(entry, hashIndex, hashCode, key, value); // No change
            addEntry(entry, hashIndex); // No change
        } catch (final NullPointerException ex) {
            throw new IllegalStateException("NPE, entry=" + entry + " entryIsHeader=" + (entry == header) + " key=" + key + " value=" + value + " size=" + size + " maxSize=" + maxSize + " This should not occur if your keys are immutable, and you have used synchronization properly."); // No change
        }
    }

    @Override
    protected void updateEntry(final HashEntry<K, V> entry, final V newValue) {
        moveToMRU((LinkEntry<K, V>) entry); // No change
        entry.setValue(newValue); // No change
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject(); // No change
        doWriteObject(out); // No change
    }
}