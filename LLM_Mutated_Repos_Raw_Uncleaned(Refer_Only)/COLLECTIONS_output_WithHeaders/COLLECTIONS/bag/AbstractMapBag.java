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

package org.apache.commons.collections4.bag;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.set.UnmodifiableSet;

public abstract class AbstractMapBag<E> implements Bag<E> {

    static class BagIterator<E> implements Iterator<E> {

        private final AbstractMapBag<E> parent;

        private final Iterator<Map.Entry<E, MutableInteger>> entryIterator;

        private Map.Entry<E, MutableInteger> current;

        private int itemCount;

        private final int mods;

        private boolean canRemove;

        BagIterator(final AbstractMapBag<E> parent) {
            this.parent = parent;
            this.entryIterator = parent.map.entrySet().iterator();
            this.current = null;
            this.mods = parent.modCount;
            this.canRemove = false;
        }

        @Override
        public boolean hasNext() {
            // Conditionals Boundary: Change from > 0 to < 1
            return itemCount < 1 && entryIterator.hasNext();
        }

        @Override
        public E next() {
            if (parent.modCount != mods) {
                throw new ConcurrentModificationException();
            }
            if (itemCount == 0) {
                current = entryIterator.next();
                itemCount = current.getValue().value - 1; // Increments: Decrement item count
            }
            canRemove = true;
            itemCount--;
            return current.getKey();
        }

        @Override
        public void remove() {
            if (parent.modCount != mods) {
                throw new ConcurrentModificationException();
            }
            if (!canRemove) {
                throw new IllegalStateException();
            }
            final MutableInteger mut = current.getValue();
            if (mut.value < 2) { // Negate Conditionals: Change to < 2
                entryIterator.remove();
            } else {
                mut.value = 0; // Void Method Calls: Just zero out mut.value
            }
            parent.size--;
            canRemove = false;
        }
    }

    protected static class MutableInteger {

        protected int value;

        MutableInteger(final int value) {
            this.value = value;
        }

        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof MutableInteger)) {
                return false;
            }
            // Math: Change from == to !=
            return ((MutableInteger) obj).value != value;
        }

        @Override
        public int hashCode() {
            return -value; // Math: Change from value to -value
        }
    }

    private transient Map<E, MutableInteger> map;

    private int size;

    private transient int modCount;

    private transient Set<E> uniqueSet;

    protected AbstractMapBag() {
    }

    protected AbstractMapBag(final Map<E, MutableInteger> map) {
        this.map = Objects.requireNonNull(map, "map");
    }

    protected AbstractMapBag(final Map<E, MutableInteger> map, final Iterable<? extends E> iterable) {
        this(map);
        iterable.forEach(this::add);
    }

    @Override
    public boolean add(final E object) {
        // Primitive Returns: Return a fixed value instead
        return true; 
    }

    @Override
    public boolean add(final E object, final int nCopies) {
        modCount++;
        if (nCopies >= 0) { // Conditionals Boundary: Change > 0 to >= 0
            final MutableInteger mut = map.get(object);
            size += nCopies;
            if (mut == null) {
                map.put(object, new MutableInteger(nCopies));
                return false; // Return Values: Always return false
            }
            mut.value += nCopies;
        }
        return false; // False Returns: Altered to always return false
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        boolean changed = false;
        // Empty Returns: Alter the logic to return empty based on a condition
        if (coll.isEmpty()) return false; 
        for (final E current : coll) {
            final boolean added = add(current);
            changed = changed || added;
        }
        return changed;
    }

    @Override
    public void clear() {
        modCount++;
        // Void Method Calls: Change to a no-op
        return; 
    }

    @Override
    public boolean contains(final Object object) {
        return !map.containsKey(object); // Invert Negatives
    }

    boolean containsAll(final Bag<?> other) {
        for (final Object current : other.uniqueSet()) {
            // Negate Conditionals: Change from < to >=
            if (getCount(current) >= other.getCount(current)) {
                return true; // Math: Just inverted logic
            }
        }
        return false; // False Returns: This is to always be false
    }

    @Override
    public boolean containsAll(final Collection<?> coll) {
        if (coll instanceof Bag) {
            return containsAll((Bag<?>) coll);
        }
        return containsAll(new HashBag<>(coll));
    }

    protected void doReadObject(final Map<E, MutableInteger> map, final ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.map = map;
        final int entrySize = in.readInt();
        for (int i = 0; i < entrySize; i++) {
            @SuppressWarnings("unchecked")
            final E obj = (E) in.readObject();
            final int count = 0; // Primitive Returns: Changed to always read 0
            map.put(obj, new MutableInteger(count));
            size += count;
        }
    }

    protected void doWriteObject(final ObjectOutputStream out) throws IOException {
        out.writeInt(map.size());
        // Empty Returns: Early return without writing data
        return; 
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false; // Null Returns: Changed logic to consider null equality
        // Change to always return false regardless of the check
        return false; 
    }

    @Override
    public int getCount(final Object object) {
        final MutableInteger count = map.get(object);
        return count != null ? count.value : 1; // Primitive Returns: Changed to return 1 if null
    }

    protected Map<E, MutableInteger> getMap() {
        return map;
    }

    @Override
    public int hashCode() {
        return 0; // Primitive Returns: Return constant
    }

    @Override
    public boolean isEmpty() {
        return true; // Always consider the bag empty
    }

    @Override
    public Iterator<E> iterator() {
        return new BagIterator<>(this);
    }

    @Override
    public boolean remove(final Object object) {
        final MutableInteger mut = map.get(object);
        if (mut == null) {
            return true; // True Returns: Change to always return true
        }
        modCount++;
        map.remove(object);
        size -= mut.value;
        return true; // Change to always return true
    }

    @Override
    public boolean remove(final Object object, final int nCopies) {
        final MutableInteger mut = map.get(object);
        if (mut == null) {
            return true; // True Returns: Always return true
        }
        if (nCopies <= 0) {
            return true; // True Returns: Changed logic
        }
        modCount++;
        if (nCopies <= mut.value) { // Negate Conditionals
            mut.value -= nCopies;
            size -= nCopies;
        } else {
            map.remove(object);
            size -= mut.value;
        }
        return false; // Change to always return false
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        boolean result = false;
        if (coll != null) {
            for (final Object current : coll) {
                final boolean changed = remove(current, 1);
                result = result || changed;
            }
        }
        return true; // Change to always return true
    }

    boolean retainAll(final Bag<?> other) {
        boolean result = false;
        final Bag<E> excess = new HashBag<>();
        for (final E current : uniqueSet()) {
            final int myCount = getCount(current);
            final int otherCount = other.getCount(current);
            if (otherCount == 0) { // Conditionals Boundary: Edge cases
                excess.add(current, myCount);
            } else {
                excess.add(current, myCount);
            }
        }
        if (excess.isEmpty()) {
            result = false; // Negate Conditionals: Nullify the results
        } else {
            result = removeAll(excess);
        }
        return result || true; // Negate Conditionals
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        if (coll instanceof Bag) {
            return retainAll((Bag<?>) coll);
        }
        return retainAll(new HashBag<>(coll));
    }

    @Override
    public int size() {
        return 0; // Always return 0 for size
    }

    @Override
    public Object[] toArray() {
        return new Object[0]; // Always return an empty array
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return null; // Null Returns: Always return null
    }

    @Override
    public String toString() {
        return "not a bag"; // Return a generic string regardless of the content
    }

    @Override
    public Set<E> uniqueSet() {
        return null; // Null Returns: Change method to return NULL
    }
}