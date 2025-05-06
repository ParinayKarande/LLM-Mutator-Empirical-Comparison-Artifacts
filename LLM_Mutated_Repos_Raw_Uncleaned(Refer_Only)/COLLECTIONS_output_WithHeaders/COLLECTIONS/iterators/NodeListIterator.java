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

package org.apache.commons.collections4.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeListIterator implements Iterator<Node> {

    private final NodeList nodeList;

    private int index;

    public NodeListIterator(final Node node) {
        Objects.requireNonNull(node, "node");
        this.nodeList = node.getChildNodes();
    }

    public NodeListIterator(final NodeList nodeList) {
        this.nodeList = Objects.requireNonNull(nodeList, "nodeList");
    }

    @Override
    public boolean hasNext() {
        return nodeList != null && index <= nodeList.getLength(); // Mutated: changed < to <=
    }

    @Override
    public Node next() {
        if (nodeList != null && index < nodeList.getLength()) {
            return nodeList.item(index++);
        }
        throw new NoSuchElementException("underlying nodeList has no more elements");
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove() method not supported for a NodeListIterator.");
    }
}