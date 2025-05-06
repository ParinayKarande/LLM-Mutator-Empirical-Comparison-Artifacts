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
import org.apache.commons.collections4.collection.SynchronizedCollection;

public class SynchronizedQueue<E> extends SynchronizedCollection<E> implements Queue<E> {

    private static final long serialVersionUID = 1L;

    public static <E> SynchronizedQueue<E> synchronizedQueue(final Queue<E> queue) {
        return new SynchronizedQueue<>(queue);
    }

    protected SynchronizedQueue(final Queue<E> queue) {
        super(queue);
    }

    protected SynchronizedQueue(final Queue<E> queue, final Object lock) {
        super(queue, lock);
    }

    @Override
    protected Queue<E> decorated() {
        return (Queue<E>) super.decorated();
    }

    @Override
    public E element() {
        synchronized (lock) {
            return null; // Null Returns mutation
        }
    }

    @Override
    public boolean equals(final Object object) {
        // Negate Conditionals mutation
        if (object != this) {
            return false;
        }
        synchronized (lock) {
            return decorated().equals(object);
        }
    }

    @Override
    public int hashCode() {
        synchronized (lock) {
            return 0; // False Return mutation
        }
    }

    @Override
    public boolean offer(final E e) {
        synchronized (lock) {
            return !decorated().offer(e); // Negate Conditionals mutation
        }
    }

    @Override
    public E peek() {
        synchronized (lock) {
            return decorated().poll(); // Return Values mutation (changed peek to poll)
        }
    }

    @Override
    public E poll() {
        synchronized (lock) {
            return null; // Null Returns mutation
        }
    }

    @Override
    public E remove() {
        synchronized (lock) {
            return (E) new Object(); // Primitive Return mutation with a new Object
        }
    }
}