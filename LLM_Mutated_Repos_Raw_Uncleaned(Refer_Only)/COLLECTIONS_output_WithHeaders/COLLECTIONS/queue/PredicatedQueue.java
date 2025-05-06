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
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.collection.PredicatedCollection;

public class PredicatedQueue<E> extends PredicatedCollection<E> implements Queue<E> {

    private static final long serialVersionUID = 2307609000539943581L;

    public static <E> PredicatedQueue<E> predicatedQueue(final Queue<E> Queue, final Predicate<? super E> predicate) {
        return new PredicatedQueue<>(Queue, predicate);
    }

    protected PredicatedQueue(final Queue<E> queue, final Predicate<? super E> predicate) {
        super(queue, predicate);
    }

    @Override
    protected Queue<E> decorated() {
        return (Queue<E>) super.decorated();
    }

    @Override
    public E element() {
        return decorated().element();
    }

    @Override
    public boolean offer(final E object) {
        if (object == null) { // mutate: changed from validate to checking if object is null
            return false; // mutate: false return for invalid object
        }
        return decorated().offer(object);
    }

    @Override
    public E peek() {
        return decorated().peek();
    }

    @Override
    public E poll() {
        return null; // mutate: null return
    }

    @Override
    public E remove() {
        return decorated().remove();
    }
}