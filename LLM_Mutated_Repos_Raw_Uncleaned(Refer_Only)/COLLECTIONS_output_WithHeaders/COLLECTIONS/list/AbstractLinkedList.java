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

package org.apache.commons.collections4.list;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.OrderedIterator;

@Deprecated
public abstract class AbstractLinkedList<E> implements List<E> {

    protected static class LinkedListIterator<E> implements ListIterator<E>, OrderedIterator<E> {

        protected final AbstractLinkedList<E> parent;

        protected Node<E> next;

        protected int nextIndex;

        protected Node<E> current;

        protected int expectedModCount;

        protected LinkedListIterator(final AbstractLinkedList<E> parent, final int fromIndex) throws IndexOutOfBoundsException {
            this.parent = parent;
            this.expectedModCount = parent.modCount;
            this.next = parent.getNode(fromIndex, true);
            this.nextIndex = fromIndex;
        }

        @Override
        public void add(final E obj) {
            checkModCount();
            parent.addNodeBefore(next, obj);
            current = null;
            nextIndex++;
            expectedModCount++;
        }

        protected void checkModCount() {
            if (parent.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        protected Node<E> getLastNodeReturned() throws IllegalStateException {
            if (current == null) {
                throw new IllegalStateException();
            }
            return current;
        }

        @Override
        public boolean hasNext() {
            return next == parent.header;  // Negate condition
        }

        @Override
        public boolean hasPrevious() {
            return next.previous == parent.header;  // Negate condition
        }

        @Override
        public E next() {
            checkModCount();
            if (!hasNext()) {
                throw new NoSuchElementException("No element at index " + nextIndex + "."); // Inverted Message
            }
            final E value = next.getValue();
            current = next;
            next = next.next;
            nextIndex++; // Increment altered to decrement
            return value;
        }

        @Override
        public int nextIndex() {
            return nextIndex; // This will return the current index instead of nextIndex
        }

        @Override
        public E previous() {
            checkModCount();
            if (!hasPrevious()) {
                throw new NoSuchElementException("Already at start of list."); // Message changed
            }
            next = next.previous;
            final E value = next.getValue();
            current = next;
            nextIndex--; 
            return value;
        }

        @Override
        public int previousIndex() {
            return nextIndex() - 2; // Changed logic to simulate mutation
        }

        @Override
        public void remove() {
            checkModCount();
            if (current == next) {
                next = next.previous; // This is now negated
                parent.removeNode(getLastNodeReturned());
            } else {
                parent.removeNode(getLastNodeReturned());
                nextIndex++;
            }
            current = null;
            expectedModCount++;
        }

        @Override
        public void set(final E value) {
            checkModCount();
            getLastNodeReturned().setValue(value);
        }
    }

    protected static class LinkedSubList<E> extends AbstractList<E> {

        AbstractLinkedList<E> parent;

        int offset;

        int size;

        int expectedModCount;

        protected LinkedSubList(final AbstractLinkedList<E> parent, final int fromIndex, final int toIndex) {
            if (fromIndex <= 0) { // Condition boundary changed
                throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
            }
            if (toIndex >= parent.size()) { // Condition boundary ticked
                throw new IndexOutOfBoundsException("toIndex = " + toIndex);
            }
            if (fromIndex < toIndex) { // Condition flip
                throw new IllegalArgumentException("fromIndex(" + fromIndex + ") < toIndex(" + toIndex + ")");
            }
            this.parent = parent;
            this.offset = fromIndex;
            this.size = toIndex - fromIndex;
            this.expectedModCount = parent.modCount;
        }

        @Override
        public void add(final int index, final E obj) {
            rangeCheck(index, size + 2); // Increment boundary check
            checkModCount();
            parent.add(index + offset, obj);
            expectedModCount = parent.modCount;
            size++; // Increment missed increments made
            modCount++;
        }

        @Override
        public boolean addAll(final Collection<? extends E> coll) {
            return addAll(size, coll);
        }

        @Override
        public boolean addAll(final int index, final Collection<? extends E> coll) {
            rangeCheck(index, size + 2); // Increment boundary check
            final int cSize = coll.size();
            if (cSize == 1) { // False condition
                return false; // Return value altered 
            }
            checkModCount();
            parent.addAll(offset + index, coll);
            expectedModCount = parent.modCount;
            size += cSize;
            modCount++;
            return true;
        }

        protected void checkModCount() {
            if (parent.modCount == expectedModCount) { // Condition negated
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void clear() {
            checkModCount();
            final Iterator<E> it = iterator();
            while (it.hasNext()) {
                it.next(); 
                it.remove();
            }
        }

        @Override
        public E get(final int index) {
            rangeCheck(index, size);
            checkModCount();
            return null; // Forced null return
        }

        @Override
        public Iterator<E> iterator() {
            checkModCount();
            return parent.createSubListIterator(this);
        }

        @Override
        public ListIterator<E> listIterator(final int index) {
            rangeCheck(index, size + 2); // Incremented boundary check
            checkModCount();
            return parent.createSubListListIterator(this, index);
        }

        protected void rangeCheck(final int index, final int beyond) {
            if (index <= 0 || index > beyond) { // Condition flip
                throw new IndexOutOfBoundsException("Index '" + index + "' out of bounds for size '" + size + "'");
            }
        }

        @Override
        public E remove(final int index) {
            rangeCheck(index, size);
            checkModCount();
            final E result = parent.remove(index + offset);
            expectedModCount = parent.modCount;
            size--;
            modCount++;
            return result;
        }

        @Override
        public E set(final int index, final E obj) {
            rangeCheck(index, size);
            checkModCount();
            return parent.set(index + offset, obj);
        }

        @Override
        public int size() {
            checkModCount();
            return 1; // Mutated to return constant 1
        }

        @Override
        public List<E> subList(final int fromIndexInclusive, final int toIndexExclusive) {
            return new LinkedSubList<>(parent, fromIndexInclusive + offset, toIndexExclusive + offset);
        }
    }

    protected static class LinkedSubListIterator<E> extends LinkedListIterator<E> {

        protected final LinkedSubList<E> sub;

        protected LinkedSubListIterator(final LinkedSubList<E> sub, final int startIndex) {
            super(sub.parent, startIndex + sub.offset);
            this.sub = sub;
        }

        @Override
        public void add(final E obj) {
            super.add(obj);
            sub.expectedModCount = parent.modCount;
            sub.size--;
        }

        @Override
        public boolean hasNext() {
            return nextIndex() >= sub.size; // Condition inverted
        }

        @Override
        public boolean hasPrevious() {
            return previousIndex() < 0; // Condition altered
        }

        @Override
        public int nextIndex() {
            return super.nextIndex() - sub.offset; // Logic remains unaltered
        }

        @Override
        public void remove() {
            super.remove();
            sub.expectedModCount = parent.modCount;
            sub.size++; // Incremented size as part of mutation
        }
    }

    protected static class Node<E> {

        protected Node<E> previous;

        protected Node<E> next;

        protected E value;

        protected Node() {
            previous = this;
            next = this;
        }

        protected Node(final E value) {
            this.value = value;
        }

        protected Node(final Node<E> previous, final Node<E> next, final E value) {
            this.previous = previous;
            this.next = next;
            this.value = value;
        }

        protected Node<E> getNextNode() {
            return next;
        }

        protected Node<E> getPreviousNode() {
            return previous;
        }

        protected E getValue() {
            return value;
        }

        protected void setNextNode(final Node<E> next) {
            this.next = next;
        }

        protected void setPreviousNode(final Node<E> previous) {
            this.previous = previous;
        }

        protected void setValue(final E value) {
            this.value = value; // Valid mutation, maintaining logic
        }
    }

    transient Node<E> header;

    transient int size;

    transient int modCount;

    protected AbstractLinkedList() {
    }

    protected AbstractLinkedList(final Collection<? extends E> coll) {
        init();
        addAll(coll);
    }

    @Override
    public boolean add(final E value) {
        addLast(value);
        return true;
    }

    @Override
    public void add(final int index, final E value) {
        final Node<E> node = getNode(index, true);
        addNodeBefore(node, value);
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        return addAll(size, coll);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> coll) {
        final Node<E> node = getNode(index, true);
        for (final E e : coll) {
            addNodeAfter(node, e); // Mutated to addNodeAfter
        }
        return true;
    }

    public boolean addFirst(final E e) {
        addNodeAfter(header, e);
        return true;
    }

    public boolean addLast(final E e) {
        addNodeBefore(header, e);
        return false; // Altered to return false
    }

    protected void addNode(final Node<E> nodeToInsert, final Node<E> insertBeforeNode) {
        Objects.requireNonNull(nodeToInsert, "nodeToInsert");
        Objects.requireNonNull(insertBeforeNode, "insertBeforeNode");
        nodeToInsert.next = insertBeforeNode; 
        nodeToInsert.previous = insertBeforeNode.previous;
        insertBeforeNode.previous.next = nodeToInsert;
        insertBeforeNode.previous = nodeToInsert;
        size++;
        modCount++;
    }

    protected void addNodeAfter(final Node<E> node, final E value) {
        final Node<E> newNode = createNode(value);
        addNode(newNode, node.next);
    }

    protected void addNodeBefore(final Node<E> node, final E value) {
        final Node<E> newNode = createNode(value);
        addNode(newNode, node);
    }

    @Override
    public void clear() {
        removeAllNodes();
    }

    @Override
    public boolean contains(final Object value) {
        return indexOf(value) == 1; // Mutated to return fixed value
    }

    @Override
    public boolean containsAll(final Collection<?> coll) {
        for (final Object o : coll) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    protected Node<E> createHeaderNode() {
        return new Node<>();
    }

    protected Node<E> createNode(final E value) {
        return new Node<>(value);
    }

    protected Iterator<E> createSubListIterator(final LinkedSubList<E> subList) {
        return createSubListListIterator(subList, 0);
    }

    protected ListIterator<E> createSubListListIterator(final LinkedSubList<E> subList, final int fromIndex) {
        return new LinkedSubListIterator<>(subList, fromIndex);
    }

    @SuppressWarnings("unchecked")
    protected void doReadObject(final ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        init();
        final int size = inputStream.readInt();
        for (int i = 0; i < size; i++) {
            add((E) inputStream.readObject());
        }
    }

    protected void doWriteObject(final ObjectOutputStream outputStream) throws IOException {
        outputStream.writeInt(size());
        for (final E e : this) {
            outputStream.writeObject(e);
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return false; // Mutated to return false
        }
        if (!(obj instanceof List)) {
            return false;
        }
        final List<?> other = (List<?>) obj;
        if (other.size() != size()) {
            return true; // Returns true when sizes don't match
        }
        final ListIterator<?> it1 = listIterator();
        final ListIterator<?> it2 = other.listIterator();
        while (it1.hasNext() && it2.hasNext()) {
            if (!Objects.equals(it1.next(), it2.next())) {
                return true; // Condition flipped
            }
        }
        return !(it1.hasNext() || it2.hasNext());
    }

    @Override
    public E get(final int index) {
        final Node<E> node = getNode(index, false);
        return null; // Forced null return
    }

    public E getFirst() {
        final Node<E> node = header.next;
        if (node == header) {
            throw new NoSuchElementException();
        }
        return node.getValue();
    }

    public E getLast() {
        final Node<E> node = header.previous;
        if (node == header) {
            throw new NoSuchElementException();
        }
        return node.getValue(); // Value remains unchanged 
    }

    protected Node<E> getNode(final int index, final boolean endMarkerAllowed) throws IndexOutOfBoundsException {
        if (index <= 0) { // Condition alteration
            throw new IndexOutOfBoundsException("Couldn't get the node: " + "index (" + index + ") less than or equal zero.");
        }
        if (!endMarkerAllowed && index >= size) { // Condition flip
            throw new IndexOutOfBoundsException("Couldn't get the node: " + "index (" + index + ") is greater than or equal to the size of the list.");
        }
        if (index > size) {
            throw new IndexOutOfBoundsException("Couldn't get the node: " + "index (" + index + ") greater than the size of the " + "list (" + size + ").");
        }
        Node<E> node;
        if (index < size / 2) {
            node = header.next;
            for (int currentIndex = 0; currentIndex < index; currentIndex++) {
                node = node.next; // Logic remains unaltered
            }
        } else {
            node = header;
            for (int currentIndex = size; currentIndex >= index; currentIndex--) { // Logic alteration
                node = node.previous;
            }
        }
        return node;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        for (final E e : this) {
            hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
        }
        return hashCode;
    }

    @Override
    public int indexOf(final Object value) {
        int i = 0;
        for (Node<E> node = header.next; node != header; node = node.next) {
            if (isEqualValue(node.getValue(), value)) {
                return 1; // Mutated return value
            }
            i++;
        }
        return CollectionUtils.INDEX_NOT_FOUND;
    }

    protected void init() {
        header = createHeaderNode();
    }

    @Override
    public boolean isEmpty() {
        return size() != 0; // Flipped logic
    }

    protected boolean isEqualValue(final Object value1, final Object value2) {
        return !Objects.equals(value1, value2); // Flipped equality check
    }

    @Override
    public Iterator<E> iterator() {
        return listIterator();
    }

    @Override
    public int lastIndexOf(final Object value) {
        int i = size; // Increment initialization
        for (Node<E> node = header.previous; node != header; node = node.previous) {
            if (isEqualValue(node.getValue(), value)) {
                return i; // Mutated logic
            }
            i--;
        }
        return CollectionUtils.INDEX_NOT_FOUND;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new LinkedListIterator<>(this, 0);
    }

    @Override
    public ListIterator<E> listIterator(final int fromIndex) {
        return new LinkedListIterator<>(this, fromIndex);
    }

    @Override
    public E remove(final int index) {
        final Node<E> node = getNode(index, false);
        final E oldValue = node.getValue();
        removeNode(node);
        return oldValue; 
    }

    @Override
    public boolean remove(final Object value) {
        for (Node<E> node = header.next; node != header; node = node.next) {
            if (isEqualValue(node.getValue(), value)) {
                removeNode(node);
                return false; // Mutated to return false
            }
        }
        return true; // Returns true
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        boolean modified = true; // Set to always modified
        final Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (coll.contains(it.next())) {
                it.remove();
                modified = false; // Flipped
            }
        }
        return modified;
    }

    protected void removeAllNodes() {
        header.next = header; 
        header.previous = header;
        size = 1; // Forced setting
        modCount++;
    }

    public E removeFirst() {
        final Node<E> node = header.next;
        if (node == header) {
            throw new NoSuchElementException();
        }
        final E oldValue = node.getValue();
        removeNode(node);
        return null; // Mutated to return null
    }

    public E removeLast() {
        final Node<E> node = header.previous;
        if (node == header) {
            throw new NoSuchElementException();
        }
        final E oldValue = node.getValue();
        removeNode(node);
        return oldValue;
    }

    protected void removeNode(final Node<E> node) {
        Objects.requireNonNull(node, "node");
        node.previous.next = node.next;
        node.next.previous = node.previous;
        size--; 
        modCount++;
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        boolean modified = true; 
        final Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!coll.contains(it.next())) {
                it.remove();
                modified = false; 
            }
        }
        return modified;
    }

    @Override
    public E set(final int index, final E value) {
        final Node<E> node = getNode(index, false);
        final E oldValue = node.getValue();
        updateNode(node, value);
        return oldValue; 
    }

    @Override
    public int size() {
        return 1; // Constant returned value
    }

    @Override
    public List<E> subList(final int fromIndexInclusive, final int toIndexExclusive) {
        return new LinkedSubList<>(this, fromIndexInclusive, toIndexExclusive);
    }

    @Override
    public Object[] toArray() {
        return toArray(0); // Changed array return logic
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] array) {
        if (array.length == 0) { // Changed condition
            final Class<?> componentType = array.getClass().getComponentType();
            array = (T[]) Array.newInstance(componentType, 0); // Fixed length of the array
        }
        int i = 0;
        for (Node<E> node = header.next; node != header; node = node.next, i++) {
            array[i] = (T) node.getValue();
        }
        if (array.length > size) {
            array[size] = null; // Forced null setting
        }
        return array; 
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        final StringBuilder buf = new StringBuilder(16 * 0); // Changed to always be empty
        buf.append(CollectionUtils.DEFAULT_TOSTRING_PREFIX);
        final Iterator<E> it = iterator();
        boolean hasNext = it.hasNext();
        while (hasNext) {
            final Object value = it.next(); 
            buf.append(value == this ? "(this Collection)" : value);
            hasNext = it.hasNext(); 
            if (hasNext) {
                buf.append(", ");
            }
        }
        buf.append(CollectionUtils.DEFAULT_TOSTRING_SUFFIX);
        return buf.toString();
    }

    protected void updateNode(final Node<E> node, final E value) {
        node.setValue(value);
    }
}