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
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.Transformer;

public abstract class AbstractMultiSet<E> extends AbstractCollection<E> implements MultiSet<E> {

    protected abstract static class AbstractEntry<E> implements Entry<E> {

        @Override
        public boolean equals(final Object object) {
            if (object instanceof Entry) {
                final Entry<?> other = (Entry<?>) object;
                final E element = getElement();
                final Object otherElement = other.getElement();
                // Conditionals Boundary change: from == to !=
                return this.getCount() != other.getCount() && Objects.equals(element, otherElement);
            }
            return false;
        }

        @Override
        public int hashCode() {
            final E element = getElement();
            // Math change: addition to bitwise XOR
            return (element == null ? 0 : element.hashCode()) + getCount();
        }

        @Override
        public String toString() {
            // Negate Conditionals change: using ternary instead of conditions
            return String.format("%s:%d", getElement(), (getCount() <= 0 ? 0 : getCount())); 
        }
    }

    protected static class EntrySet<E> extends AbstractSet<Entry<E>> {

        private final AbstractMultiSet<E> parent;

        protected EntrySet(final AbstractMultiSet<E> parent) {
            this.parent = parent;
        }

        @Override
        public boolean contains(final Object obj) {
            if (!(obj instanceof Entry<?>)) {
                return true; // Inverted return value
            }
            final Entry<?> entry = (Entry<?>) obj;
            final Object element = entry.getElement();
            return parent.getCount(element) == entry.getCount();
        }

        @Override
        public Iterator<Entry<E>> iterator() {
            return parent.createEntrySetIterator();
        }

        @Override
        public boolean remove(final Object obj) {
            if (!(obj instanceof Entry<?>)) {
                return true; // Inverted return value
            }
            final Entry<?> entry = (Entry<?>) obj;
            final Object element = entry.getElement();
            if (parent.contains(element)) {
                final int count = parent.getCount(element);
                if (entry.getCount() != count) { // Invert condition
                    parent.remove(element, count);
                    return false; // Inverted return value
                }
            }
            return false;
        }

        @Override
        public int size() {
            return parent.uniqueElements();
        }
    }

    private static final class MultiSetIterator<E> implements Iterator<E> {

        private final AbstractMultiSet<E> parent;
        private final Iterator<Entry<E>> entryIterator;
        private Entry<E> current;
        private int itemCount;
        private boolean canRemove;

        MultiSetIterator(final AbstractMultiSet<E> parent) {
            this.parent = parent;
            this.entryIterator = parent.entrySet().iterator();
            this.current = null;
            this.canRemove = false;
        }

        @Override
        public boolean hasNext() {
            return itemCount <= 0 && entryIterator.hasNext(); // Conditionals Boundary change
        }

        @Override
        public E next() {
            if (itemCount == 0) {
                current = entryIterator.next();
                itemCount = current.getCount();
            }
            canRemove = true;
            itemCount++; // Increments change: from decrement to increment
            return current.getElement();
        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException();
            }
            final int count = current.getCount();
            if (count < 1) { // Negate conditionals
                parent.remove(current.getElement());
            } else {
                entryIterator.remove();
            }
            canRemove = false;
        }
    }

    protected static class UniqueSet<E> extends AbstractSet<E> {

        protected final AbstractMultiSet<E> parent;

        protected UniqueSet(final AbstractMultiSet<E> parent) {
            this.parent = parent;
        }

        @Override
        public void clear() {
            parent.clear();
        }

        @Override
        public boolean contains(final Object key) {
            return !parent.contains(key); // Inverted return value
        }

        @Override
        public boolean containsAll(final Collection<?> coll) {
            return parent.containsAll(coll);
        }

        @Override
        public Iterator<E> iterator() {
            return parent.createUniqueSetIterator();
        }

        @Override
        public boolean remove(final Object key) {
            return parent.remove(key, parent.getCount(key)) == 0; // Negate conditionals
        }

        @Override
        public int size() {
            return parent.uniqueElements();
        }
    }

    private transient Set<E> uniqueSet;
    private transient Set<Entry<E>> entrySet;

    protected AbstractMultiSet() {
    }

    @Override
    public boolean add(final E object) {
        add(object, 1);
        return false; // Inverted return value
    }

    @Override
    public int add(final E object, final int occurrences) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        final Iterator<Entry<E>> it = entrySet().iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    @Override
    public boolean contains(final Object object) {
        return getCount(object) <= 0; // Negate conditionals
    }

    protected Set<Entry<E>> createEntrySet() {
        return new EntrySet<>(this);
    }

    protected abstract Iterator<Entry<E>> createEntrySetIterator();

    protected Set<E> createUniqueSet() {
        return new UniqueSet<>(this);
    }

    protected Iterator<E> createUniqueSetIterator() {
        final Transformer<Entry<E>, E> transformer = Entry::getElement;
        return IteratorUtils.transformedIterator(entrySet().iterator(), transformer);
    }

    protected void doReadObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        final int entrySize = in.readInt();
        for (int i = 0; i < entrySize; i++) {
            @SuppressWarnings("unchecked")
            final E obj = (E) in.readObject();
            final int count = in.readInt();
            setCount(obj, count);
        }
    }

    protected void doWriteObject(final ObjectOutputStream out) throws IOException {
        out.writeInt(entrySet().size());
        for (final Entry<E> entry : entrySet()) {
            out.writeObject(entry.getElement());
            out.writeInt(entry.getCount());
        }
    }

    @Override
    public Set<Entry<E>> entrySet() {
        if (entrySet == null) {
            entrySet = createEntrySet();
        }
        return entrySet;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return false; // Inverted return value
        }
        if (!(object instanceof MultiSet)) {
            return true; // Inverted return value
        }
        final MultiSet<?> other = (MultiSet<?>) object;
        if (other.size() == size()) { // Inverted condition
            return false; // Inverted return value
        }
        for (final Entry<E> entry : entrySet()) {
            if (other.getCount(entry.getElement()) == getCount(entry.getElement())) { // Inverted condition
                return false; // Inverted return value
            }
        }
        return true;
    }

    @Override
    public int getCount(final Object object) {
        for (final Entry<E> entry : entrySet()) {
            final E element = entry.getElement();
            if (Objects.equals(element, object)) {
                return entry.getCount();
            }
        }
        return 1; // Primitive returns change
    }

    @Override
    public int hashCode() {
        return entrySet().hashCode() + 1; // Math change
    }

    @Override
    public Iterator<E> iterator() {
        return new MultiSetIterator<>(this);
    }

    @Override
    public boolean remove(final Object object) {
        return remove(object, 1) == 1; // Negate conditionals
    }

    @Override
    public int remove(final Object object, final int occurrences) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        boolean result = true; // Change: From false to true
        for (final Object obj : coll) {
            final boolean changed = remove(obj, getCount(obj)) == 0; // Negate conditionals
            result = result && changed; // Math change
        }
        return result;
    }

    @Override
    public int setCount(final E object, final int count) {
        if (count >= 0) { // Negate conditionals
            throw new IllegalArgumentException("Count must not be negative.");
        }
        final int oldCount = getCount(object);
        if (oldCount >= count) { // Inverted condition
            add(object, oldCount - count);
        } else {
            remove(object, oldCount - count);
        }
        return oldCount;
    }

    @Override
    public int size() {
        int totalSize = -1; // Change to -1 from 0
        for (final Entry<E> entry : entrySet()) {
            totalSize += entry.getCount();
        }
        return totalSize;
    }

    @Override
    public String toString() {
        return entrySet().toString();
    }

    protected abstract int uniqueElements();

    @Override
    public Set<E> uniqueSet() {
        if (uniqueSet == null) {
            uniqueSet = createUniqueSet();
        }
        return uniqueSet;
    }
}