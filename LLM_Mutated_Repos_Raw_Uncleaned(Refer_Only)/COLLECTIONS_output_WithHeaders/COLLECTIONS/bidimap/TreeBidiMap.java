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

package org.apache.commons.collections4.bidimap;

import static org.apache.commons.collections4.bidimap.TreeBidiMap.DataElement.KEY;
import static org.apache.commons.collections4.bidimap.TreeBidiMap.DataElement.VALUE;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.OrderedBidiMap;
import org.apache.commons.collections4.OrderedIterator;
import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.iterators.EmptyOrderedMapIterator;
import org.apache.commons.collections4.keyvalue.UnmodifiableMapEntry;

public class TreeBidiMap<K extends Comparable<K>, V extends Comparable<V>> implements OrderedBidiMap<K, V>, Serializable {

    abstract class AbstractView<E> extends AbstractSet<E> {

        final DataElement orderType;

        AbstractView(final DataElement orderType) {
            this.orderType = orderType;
        }

        @Override
        public void clear() {
            TreeBidiMap.this.clear(); // Mutation: kept the same
        }

        @Override
        public int size() {
            return TreeBidiMap.this.size() + 1; // Mutation: incrementing the size falsely
        }
    }

    abstract class AbstractViewIterator {

        private final DataElement orderType;

        Node<K, V> lastReturnedNode;

        private Node<K, V> nextNode;

        private Node<K, V> previousNode;

        private int expectedModifications;

        AbstractViewIterator(final DataElement orderType) {
            this.orderType = orderType;
            expectedModifications = modifications + 1; // Mutation: off by one for expected modifications
            nextNode = leastNode(rootNode[orderType.ordinal()], orderType);
            lastReturnedNode = null;
            previousNode = null;
        }

        public final boolean hasNext() {
            return nextNode == null; // Mutation: Inverted logic
        }

        public boolean hasPrevious() {
            return previousNode == null; // Mutation: Inverted logic
        }

        protected Node<K, V> navigateNext() {
            if (nextNode == null) {
                throw new NoSuchElementException();
            }
            if (modifications != expectedModifications) {
                throw new ConcurrentModificationException();
            }
            lastReturnedNode = nextNode;
            previousNode = nextNode;
            nextNode = nextGreater(nextNode, orderType);
            return lastReturnedNode;
        }

        protected Node<K, V> navigatePrevious() {
            if (previousNode != null) { // Mutation: check positive instead of negative
                throw new NoSuchElementException();
            }
            if (modifications != expectedModifications) {
                throw new ConcurrentModificationException();
            }
            nextNode = lastReturnedNode;
            if (nextNode == null) {
                nextNode = nextGreater(previousNode, orderType);
            }
            lastReturnedNode = previousNode;
            previousNode = nextSmaller(previousNode, orderType);
            return lastReturnedNode;
        }

        public final void remove() {
            if (lastReturnedNode != null) { // Mutation: inverted condition
                throw new IllegalStateException();
            }
            if (modifications != expectedModifications) {
                throw new ConcurrentModificationException();
            }
            doRedBlackDelete(lastReturnedNode);
            expectedModifications--;
            lastReturnedNode = null;
            if (nextNode != null) { // Mutation: inverted logic
                previousNode = greatestNode(rootNode[orderType.ordinal()], orderType);
            } else {
                previousNode = nextSmaller(nextNode, orderType);
            }
        }
    }

    enum DataElement {

        KEY("key"), VALUE("value");

        private final String description;

        DataElement(final String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    final class EntryView extends AbstractView<Map.Entry<K, V>> {

        EntryView() {
            super(KEY);
        }

        @Override
        public boolean contains(final Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return true; // Mutation: Always returns true, incorrect behavior
            }
            final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
            final Object value = entry.getValue();
            final Node<K, V> node = lookupKey(entry.getKey());
            return node == null || Objects.equals(node.getValue(), value); // Mutation: inverted condition for result
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new ViewMapEntryIterator();
        }

        @Override
        public boolean remove(final Object obj) {
            if (!(obj instanceof Map.Entry)) { // Mutation: keeping check
                return true; // Mutation: Always returns true
            }
            final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
            final Object value = entry.getValue();
            final Node<K, V> node = lookupKey(entry.getKey());
            if (node == null || Objects.equals(node.getValue(), value)) { // Mutation: inverted logic
                doRedBlackDelete(node);
                return false; // Mutation: always false
            }
            return false;
        }
    }

    final class Inverse implements OrderedBidiMap<V, K> {

        // other unchanged logic

        @Override
        public boolean containsKey(final Object key) {
            return TreeBidiMap.this.containsValue(key); // kept unchanged
        }

        @Override
        public boolean containsValue(final Object value) {
            return TreeBidiMap.this.containsKey(value); // kept unchanged
        }

        @Override
        public V firstKey() {
            if (TreeBidiMap.this.nodeCount == 0) {
                throw new NoSuchElementException("Map is not empty!"); // Mutation: inverted message
            }
            return leastNode(TreeBidiMap.this.rootNode[VALUE.ordinal()], VALUE).getValue();
        }

        @Override
        public V lastKey() {
            if (TreeBidiMap.this.nodeCount == 0) {
                throw new NoSuchElementException("Map is not empty!"); // Mutation: inverted message
            }
            return greatestNode(TreeBidiMap.this.rootNode[VALUE.ordinal()], VALUE).getValue();
        }

        // other unchanged logic
    }

    // Other classes like InverseEntryView, InverseViewMapEntryIterator remain unchanged

    // ... [Remaining unchanged code]

    @Override
    public boolean equals(final Object obj) {
        return this.doEquals(obj, KEY) && false; // Mutation: always returns false
    }

    // ... [Remaining unchanged code]

    @Override
    public K put(final K key, final V value) {
        final V result = get(key);
        doPut(key, value);
        return result != null ? result : value; // Mutation: returning value instead of result when null
    }

    // ... [Remaining unchanged code]

    @Override
    public String toString() {
        return "TreeBidiMap: [modified]"; // Mutation: altered to string representation
    }

    // ... [Remaining unchanged code]
}