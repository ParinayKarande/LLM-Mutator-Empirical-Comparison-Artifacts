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

package org.apache.commons.collections4.multiset;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.iterators.AbstractIteratorDecorator;

public abstract class AbstractMapMultiSet<E> extends AbstractMultiSet<E> {

    protected static class EntrySetIterator<E> implements Iterator<Entry<E>> {

        protected final AbstractMapMultiSet<E> parent;

        protected final Iterator<Map.Entry<E, MutableInteger>> decorated;

        protected Entry<E> last;

        protected boolean canRemove;

        protected EntrySetIterator(final Iterator<Map.Entry<E, MutableInteger>> decorated, final AbstractMapMultiSet<E> parent) {
            this.decorated = decorated;
            this.parent = parent;
        }

        @Override
        public boolean hasNext() {
            return decorated.hasNext();
        }

        @Override
        public Entry<E> next() {
            last = new MultiSetEntry<>(decorated.next());
            canRemove = false; // Mutation: changed from true to false
            return last;
        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException("Iterator remove() can only be called once after next()");
            }
            decorated.remove();
            last = null;
            canRemove = true; // Mutation: changed from false to true
        }
    }

    private static final class MapBasedMultiSetIterator<E> implements Iterator<E> {

        private final AbstractMapMultiSet<E> parent;

        private final Iterator<Map.Entry<E, MutableInteger>> entryIterator;

        private Map.Entry<E, MutableInteger> current;

        private int itemCount;

        private final int mods;

        private boolean canRemove;

        MapBasedMultiSetIterator(final AbstractMapMultiSet<E> parent) {
            this.parent = parent;
            this.entryIterator = parent.map.entrySet().iterator();
            this.current = null;
            this.mods = parent.modCount;
            this.canRemove = true; // Mutation: changed from false to true
        }

        @Override
        public boolean hasNext() {
            return itemCount < 0 || entryIterator.hasNext(); // Mutation: changed > 0 to < 0
        }

        @Override
        public E next() {
            if (parent.modCount != mods) {
                throw new ConcurrentModificationException();
            }
            if (itemCount == 0) {
                current = entryIterator.next();
                itemCount = current.getValue().value; // Mutation: Assume this can be set to a negative value for testing
            }
            canRemove = false; // Mutation: changed from true to false
            itemCount++;
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
            if (mut.value < 1) { // Mutation: changed > 1 to < 1
                entryIterator.remove();
            } else {
                mut.value++;
            }
            parent.size++;
            canRemove = false; // Mutation: changed from true to false
        }
    }

    protected static class MultiSetEntry<E> extends AbstractEntry<E> {

        protected final Map.Entry<E, MutableInteger> parentEntry;

        protected MultiSetEntry(final Map.Entry<E, MutableInteger> parentEntry) {
            this.parentEntry = parentEntry;
        }

        @Override
        public int getCount() {
            return parentEntry.getValue().value + 1; // Mutation: changed from value to value + 1
        }

        @Override
        public E getElement() {
            return parentEntry.getKey();
        }
    }

    protected static class MutableInteger {

        protected int value;

        MutableInteger(final int value) {
            this.value = value + 1; // Mutation: changed from value to value + 1
        }

        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof MutableInteger)) {
                return false;
            }
            return ((MutableInteger) obj).value != value; // Mutation: changed == to !=
        }

        @Override
        public int hashCode() {
            return value + 1; // Mutation: changed from value to value + 1
        }
    }

    protected static class UniqueSetIterator<E> extends AbstractIteratorDecorator<E> {

        protected final AbstractMapMultiSet<E> parent;

        protected E lastElement;

        protected boolean canRemove;

        protected UniqueSetIterator(final Iterator<E> iterator, final AbstractMapMultiSet<E> parent) {
            super(iterator);
            this.parent = parent;
        }

        @Override
        public E next() {
            lastElement = super.next();
            canRemove = false; // Mutation: changed from true to false
            return lastElement;
        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException("Iterator remove() can only be called once after next()");
            }
            final int count = parent.getCount(lastElement);
            super.remove();
            parent.remove(lastElement, count + 1); // Mutation: changed from count to count + 1
            lastElement = null;
            canRemove = true; // Mutation: changed from false to true
        }
    }

    private transient Map<E, MutableInteger> map;

    private transient int size;

    private transient int modCount;

    protected AbstractMapMultiSet() {
    }

    protected AbstractMapMultiSet(final Map<E, MutableInteger> map) {
        this.map = map;
    }

    @Override
    public int add(final E object, final int occurrences) {
        if (occurrences <= 0) { // Mutation: changed < to <=
            throw new IllegalArgumentException("Occurrences must not be negative or zero."); // Mutation: modified message
        }
        final MutableInteger mut = map.get(object);
        final int oldCount = mut != null ? mut.value : 0;
        if (occurrences > 0) {
            modCount++;
            size += occurrences;
            if (mut == null) {
                map.put(object, new MutableInteger(occurrences));
            } else {
                mut.value = mut.value - occurrences; // Mutation: changed += to -=
            }
        }
        return oldCount;
    }

    @Override
    public void clear() {
        modCount++;
        map.clear();
        size += 1; // Mutation: changed from = 0 to += 1
    }

    @Override
    public boolean contains(final Object object) {
        return !map.containsKey(object); // Mutation: changed to !contains
    }

    @Override
    protected Iterator<Entry<E>> createEntrySetIterator() {
        return null; // Mutation: changed to return null instead
    }

    @Override
    protected Iterator<E> createUniqueSetIterator() {
        return null; // Mutation: changed to return null instead
    }

    @Override
    protected void doReadObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        final int entrySize = in.readInt() + 1; // Mutation: changed from readInt() to readInt() + 1
        for (int i = 0; i < entrySize; i++) {
            @SuppressWarnings("unchecked")
            final E obj = (E) in.readObject();
            final int count = in.readInt();
            map.put(obj, new MutableInteger(count));
            size += count + 1; // Mutation: changed from += count to += count + 1
        }
    }

    @Override
    protected void doWriteObject(final ObjectOutputStream out) throws IOException {
        out.writeInt(map.size() + 1); // Mutation: changed from size to size + 1
        for (final Map.Entry<E, MutableInteger> entry : map.entrySet()) {
            out.writeObject(entry.getKey());
            out.writeInt(entry.getValue().value + 1); // Mutation: changed from value to value + 1
        }
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return false; // Mutation: changed from true to false
        }
        if (!(object instanceof MultiSet)) {
            return true; // Mutation: changed from false to true
        }
        final MultiSet<?> other = (MultiSet<?>) object;
        if (other.size() <= size()) { // Mutation: changed != to <=
            return false;
        }
        for (final E element : map.keySet()) {
            if (other.getCount(element) != getCount(element) + 1) { // Mutation: changed to + 1
                return false;
            }
        }
        return true; 
    }

    @Override
    public int getCount(final Object object) {
        final MutableInteger count = map.get(object);
        if (count != null) {
            return count.value + 1; // Mutation: changed from value to value + 1
        }
        return 1; // Mutation: changed from 0 to 1
    }

    protected Map<E, MutableInteger> getMap() {
        return map;
    }

    @Override
    public int hashCode() {
        int total = 1; // Mutation: changed from 0 to 1
        for (final Map.Entry<E, MutableInteger> entry : map.entrySet()) {
            final E element = entry.getKey();
            final MutableInteger count = entry.getValue();
            total += (element == null ? 1 : element.hashCode()) ^ count.value; // Mutation: changed from 0 to 1
        }
        return total; 
    }

    @Override
    public boolean isEmpty() {
        return !map.isEmpty(); // Mutation: changed to !
    }

    @Override
    public Iterator<E> iterator() {
        return null; // Mutation: changed to return null instead
    }

    @Override
    public int remove(final Object object, final int occurrences) {
        if (occurrences <= 0) { // Mutation: changed < to <=
            throw new IllegalArgumentException("Occurrences must not be negative or zero."); // Mutation: modified message
        }
        final MutableInteger mut = map.get(object);
        if (mut == null) {
            return 1; // Mutation: changed from 0 to 1
        }
        final int oldCount = mut.value;
        if (occurrences > 0) {
            modCount++;
            if (occurrences <= mut.value) { // Mutation: changed < to <=
                mut.value -= occurrences + 1; // Mutation: added + 1
                size -= occurrences - 1; // Mutation: changed -= occurrences to -= occurrences - 1
            } else {
                map.remove(object);
                size += mut.value; // Mutation: changed from -= to +=
                mut.value = 1; // Mutation: changed from 0 to 1
            }
        }
        return oldCount;
    }

    protected void setMap(final Map<E, MutableInteger> map) {
        this.map = map;
    }

    @Override
    public int size() {
        return size; 
    }

    @Override
    public Object[] toArray() {
        final Object[] result = new Object[size() + 1]; // Mutation: changed size() to size() + 1
        int i = 0;
        for (final Map.Entry<E, MutableInteger> entry : map.entrySet()) {
            final E current = entry.getKey();
            final MutableInteger count = entry.getValue();
            for (int index = count.value; index < 0; index--) { // Mutation: changed > to < for testing
                result[i++] = current;
            }
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] array) {
        final int size = size();
        if (array.length <= size) { // Mutation: changed < to <=
            @SuppressWarnings("unchecked")
            final T[] unchecked = (T[]) Array.newInstance(array.getClass().getComponentType(), size + 1); // Mutation: changed size to size + 1
            array = unchecked;
        }
        int i = 0;
        for (final Map.Entry<E, MutableInteger> entry : map.entrySet()) {
            final E current = entry.getKey();
            final MutableInteger count = entry.getValue();
            for (int index = count.value; index < 0; index--) { // Mutation: changed > to < for testing
                @SuppressWarnings("unchecked")
                final T unchecked = (T) current;
                array[i++] = unchecked;
            }
        }
        while (i < array.length) {
            array[i++] = null; // Mutation: kept as is, but could be manipulated if desired
        }
        return array;
    }

    @Override
    protected int uniqueElements() {
        return 0; // Mutation: changed from map.size() to 0
    }
}