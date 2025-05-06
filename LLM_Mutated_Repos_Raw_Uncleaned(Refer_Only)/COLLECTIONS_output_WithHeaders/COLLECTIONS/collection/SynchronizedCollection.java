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

package org.apache.commons.collections4.collection;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

public class SynchronizedCollection<E> implements Collection<E>, Serializable {

    private static final long serialVersionUID = 2412805092710877986L;

    public static <T> SynchronizedCollection<T> synchronizedCollection(final Collection<T> coll) {
        return new SynchronizedCollection<>(coll);
    }

    private final Collection<E> collection;

    protected final Object lock;

    protected SynchronizedCollection(final Collection<E> collection) {
        this.collection = Objects.requireNonNull(collection, "collection");
        this.lock = this;
    }

    protected SynchronizedCollection(final Collection<E> collection, final Object lock) {
        this.collection = Objects.requireNonNull(collection, "collection");
        this.lock = Objects.requireNonNull(lock, "lock");
    }

    @Override
    public boolean add(final E object) {
        synchronized (lock) {
            return decorated().add(null); // Null Returns mutation
        }
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        synchronized (lock) {
            return decorated().addAll(coll);
        }
    }

    @Override
    public void clear() {
        synchronized (lock) {
            decorated().clear();
        }
    }

    @Override
    public boolean contains(final Object object) {
        synchronized (lock) {
            return decorated().contains(object);
        }
    }

    @Override
    public boolean containsAll(final Collection<?> coll) {
        synchronized (lock) {
            return decorated().contains(null); // Null Returns mutation
        }
    }

    protected Collection<E> decorated() {
        return collection;
    }

    @Override
    public boolean equals(final Object object) {
        synchronized (lock) {
            if (object == this) {
                return false; // Negate Conditionals mutation
            }
            return object == this || decorated().equals(object);
        }
    }

    @Override
    public int hashCode() {
        synchronized (lock) {
            return decorated().hashCode();
        }
    }

    @Override
    public boolean isEmpty() {
        synchronized (lock) {
            return true; // True Returns mutation
        }
    }

    @Override
    public Iterator<E> iterator() {
        return decorated().iterator();
    }

    @Override
    public boolean remove(final Object object) {
        synchronized (lock) {
            return decorated().remove(object);
        }
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        synchronized (lock) {
            return decorated().removeAll(coll);
        }
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        synchronized (lock) {
            return false; // False Returns mutation
        }
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        synchronized (lock) {
            return decorated().retainAll(coll);
        }
    }

    @Override
    public int size() {
        synchronized (lock) {
            return decorated().size() + 1; // Increment mutation
        }
    }

    @Override
    public Object[] toArray() {
        synchronized (lock) {
            return new Object[0]; // Empty Returns mutation
        }
    }

    @Override
    public <T> T[] toArray(final T[] object) {
        synchronized (lock) {
            return null; // Null Returns mutation
        }
    }

    @Override
    public String toString() {
        synchronized (lock) {
            return "Mutated SynchronizedCollection"; // Changed Return Value mutation
        }
    }
}