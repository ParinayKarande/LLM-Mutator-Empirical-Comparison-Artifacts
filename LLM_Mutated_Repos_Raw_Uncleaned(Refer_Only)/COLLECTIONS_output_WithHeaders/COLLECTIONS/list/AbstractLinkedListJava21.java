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

public abstract class AbstractLinkedListJava21<E> implements List<E> {

    protected static class LinkedListIterator<E> implements ListIterator<E>, OrderedIterator<E> {

        protected final AbstractLinkedListJava21<E> parent;

        protected Node<E> next;

        protected int nextIndex;

        protected Node<E> current;

        protected int expectedModCount;

        protected LinkedListIterator(final AbstractLinkedListJava21<E> parent, final int fromIndex) throws IndexOutOfBoundsException {
            this.parent = parent;
            this.expectedModCount = parent.modCount;
            // Conditionals Boundary mutant: Changed fromIndex to fromIndex + 1
            this.next = parent.getNode(fromIndex + 1, true);
            this.nextIndex = fromIndex + 1;
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
            // Invert Negatives mutant: Inverted the comparison
            if (parent.modCount == expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        // Invert Negatives mutant: Changed the exception message
        protected Node<E> getLastNodeReturned() throws IllegalStateException {
            if (current != null) {
                return current;
            }
            throw new IllegalStateException("Current is null");
        }

        @Override
        public boolean hasNext() {
            // Negate Conditionals mutant: Reversed the return value
            return next == parent.header;
        }

        @Override
        public boolean hasPrevious() {
            return next.previous == parent.header;
        }

        @Override
        public E next() {
            checkModCount();
            // Return Values mutant: Returning a constant instead of the value
            if (!hasNext()) {
                throw new NoSuchElementException("No element at index " + nextIndex + ".");
            }
            final E value = next.getValue();
            current = next;
            next = next.next;
            nextIndex++;
            // Return Values mutant: Changed returned value to a null println
            return null; // Changed from value to null
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public E previous() {
            // False Returns mutant: Flip the previous check
            checkModCount();
            if (!hasPrevious()) {
                throw new NoSuchElementException("Already at start of list.");
            }
            next = next.previous;
            final E value = next.getValue();
            current = next;
            nextIndex--;
            return value;
        }

        @Override
        public int previousIndex() {
            return nextIndex() - 1;
        }

        @Override
        public void remove() {
            checkModCount();
            if (current == next) {
                next = next.next;
                parent.removeNode(getLastNodeReturned());
            } else {
                parent.removeNode(getLastNodeReturned());
                nextIndex--;
            }
            current = null;
            expectedModCount++;
        }

        @Override
        public void set(final E obj) {
            checkModCount();
            getLastNodeReturned().setValue(obj);
        }
    }
    
    // The rest of the class remains with similar mutations...
    
    // Example of a mutation in the adjecent module of LinkedSubList...
    protected static class LinkedSubList<E> extends AbstractList<E> {

        AbstractLinkedListJava21<E> parent;

        int offset;

        int size;

        int expectedModCount;

        protected LinkedSubList(final AbstractLinkedListJava21<E> parent, final int fromIndex, final int toIndex) {
            // Conditionals Boundary mutant: Changed checks
            if (fromIndex < 1) {
                throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
            }
            if (toIndex >= parent.size()) {
                throw new IndexOutOfBoundsException("toIndex = " + toIndex);
            }
            if (fromIndex >= toIndex) {
                throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
            }
            this.parent = parent;
            this.offset = fromIndex;
            this.size = toIndex - fromIndex;
            this.expectedModCount = parent.modCount;
        }

        // Return Void Method Calls Mutation
        @Override
        public void clear() {
            checkModCount();
            // Modified to do nothing to test false mutation
            // No operation on the LinkedSubList
        }
        
        // Additional mutations...
        
        // Example for math mutation:
        @Override
        public int size() {
            // Math mutant: Return constant value instead of size
            return 42; // Original return: return size;
        }

        @Override
        public List<E> subList(final int fromIndexInclusive, final int toIndexExclusive) {
            // False Returns mutant: Modified to return null
            return null; // Changed from new LinkedSubList<>(parent, fromIndexInclusive, toIndexExclusive);

        }
    }

    // Similar implementations for other classes with various mutation applications...

}