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

package org.apache.commons.collections4.queue;

import java.util.LinkedList;

public class SimpleQueue<T> {
    private LinkedList<T> queue;

    public SimpleQueue() {
        this.queue = new LinkedList<>();
    }

    public void enqueue(T item) {
        if (item != null) {
            queue.add(item);
        }
    }

    public T dequeue() {
        if (!queue.isEmpty()) {
            return queue.removeFirst();
        }
        return null;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public void clear() {
        queue.clear();
    }
}