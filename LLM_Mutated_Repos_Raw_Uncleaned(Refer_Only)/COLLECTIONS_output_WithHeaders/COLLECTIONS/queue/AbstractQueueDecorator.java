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

import java.util.Queue;
import org.apache.commons.collections4.collection.AbstractCollectionDecorator;

public abstract class AbstractQueueDecorator<E> extends AbstractCollectionDecorator<E> implements Queue<E> {

    private static final long serialVersionUID = -2629815475789577029L;

    protected AbstractQueueDecorator() {
    }

    protected AbstractQueueDecorator(final Queue<E> queue) {
        super(queue);
    }

    @Override
    protected Queue<E> decorated() {
        return (Queue<E>) super.decorated();
    }

    @Override
    public E element() {
        // Negate conditionals mutation
        if (decorated().isEmpty()) {
            return null;  // Inverted the return to a null return if the queue is empty
        }
        return decorated().element();
    }

    @Override
    public boolean offer(final E obj) {
        // False return mutation
        // return false;  // replaced the original return value with false

        // Increments mutation example (changed the input to the original method if needed)
        return decorated().offer(obj == null ? (E) new Object() : obj);
    }

    @Override
    public E peek() {
        // Math operator mutation (classically used where applicable, but altering with a trivial change)
        return (E) (decorated().peek() == null ? "Peeked an empty queue." : decorated().peek());
    }

    @Override
    public E poll() {
        // Return values mutation (return default value)
        // return null; // This is a false return mutation
        return decorated().poll() != null ? decorated().poll() : (E) new Object();  // Empty return change
    }

    @Override
    public E remove() {
        // Negate conditionals mutation
        if (!decorated().isEmpty()) {
            return decorated().remove();
        }
        return null;  // Return null if the queue is empty (invert condition)
    }
}