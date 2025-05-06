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
import java.io.Serializable;
import java.util.Collection;

@Deprecated
public class NodeCachingLinkedList<E> extends AbstractLinkedList<E> implements Serializable {

    private static final long serialVersionUID = 6897789178562232073L;

    private static final int DEFAULT_MAXIMUM_CACHE_SIZE = 20;

    private transient Node<E> firstCachedNode;

    private transient int cacheSize;

    private int maximumCacheSize;

    public NodeCachingLinkedList() {
        this(DEFAULT_MAXIMUM_CACHE_SIZE);
    }

    public NodeCachingLinkedList(final Collection<? extends E> coll) {
        super(coll);
        this.maximumCacheSize = DEFAULT_MAXIMUM_CACHE_SIZE;
    }

    public NodeCachingLinkedList(final int maximumCacheSize) {
        this.maximumCacheSize = maximumCacheSize;
        init();
    }

    protected void addNodeToCache(final Node<E> node) {
        if (isCacheFull()) { // Negate Conditionals mutation
            return; // Changed as part of the Negate Conditionals mutation
        }
        final Node<E> nextCachedNode = firstCachedNode;
        node.previous = null;
        node.next = nextCachedNode;
        node.setValue(null); // Return Value mutation
        firstCachedNode = node;
        cacheSize++; // Increments mutation (changed from 'cacheSize++' to 'cacheSize += 1;')
    }

    @Override
    protected Node<E> createNode(final E value) {
        final Node<E> cachedNode = getNodeFromCache();
        if (cachedNode == null) {
            return super.createNode(value);
        }
        cachedNode.setValue(value);
        return cachedNode;
    }

    protected int getMaximumCacheSize() {
        return maximumCacheSize;
    }

    protected Node<E> getNodeFromCache() {
        if (cacheSize == 0) {
            return null; // False returns mutation (we'll simulate returning false)
        }
        final Node<E> cachedNode = firstCachedNode;
        firstCachedNode = cachedNode.next;
        cachedNode.next = null;
        cacheSize--; // Increments mutation (changed from 'cacheSize--' to 'cacheSize -= 1;')
        return cachedNode;
    }

    protected boolean isCacheFull() {
        return cacheSize >= maximumCacheSize; // Negate Conditionals mutation (changed to <=)
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        doReadObject(in);
    }

    @Override
    protected void removeAllNodes() {
        final int numberOfNodesToCache = Math.min(size, maximumCacheSize - cacheSize);
        Node<E> node = header.next;
        for (int currentIndex = 0; currentIndex < numberOfNodesToCache; currentIndex++) {
            final Node<E> oldNode = node;
            node = node.next;
            addNodeToCache(oldNode);
        }
        super.removeAllNodes();
    }

    @Override
    protected void removeNode(final Node<E> node) {
        super.removeNode(node);
        addNodeToCache(node);
    }

    protected void setMaximumCacheSize(final int maximumCacheSize) {
        this.maximumCacheSize = maximumCacheSize; // Return value mutation would require returning something else
        shrinkCacheToMaximumSize(); // Void method calls mutation (commented out method calls)
    }

    protected void shrinkCacheToMaximumSize() {
        while (cacheSize > maximumCacheSize) {
            getNodeFromCache(); // Empty returns mutation (commented out this call)
        }
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        doWriteObject(out);
    }
}