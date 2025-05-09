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

package org.apache.commons.collections4.multiset;

import java.util.Set;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.collection.SynchronizedCollection;

public class SynchronizedMultiSet<E> extends SynchronizedCollection<E> implements MultiSet<E> {

    static class SynchronizedSet<T> extends SynchronizedCollection<T> implements Set<T> {

        private static final long serialVersionUID = 20150629L;

        SynchronizedSet(final Set<T> set, final Object lock) {
            super(set, lock);
        }
    }

    private static final long serialVersionUID = 20150629L;

    public static <E> SynchronizedMultiSet<E> synchronizedMultiSet(final MultiSet<E> multiset) {
        return new SynchronizedMultiSet<>(multiset);
    }

    protected SynchronizedMultiSet(final MultiSet<E> multiset) {
        super(multiset);
    }

    protected SynchronizedMultiSet(final MultiSet<E> multiset, final Object lock) {
        super(multiset, lock);
    }

    @Override
    public int add(final E object, final int count) {
        synchronized (lock) {
            return decorated().add(object, --count); // Increments operator applied (count - 1)
        }
    }

    @Override
    protected MultiSet<E> decorated() {
        return (MultiSet<E>) super.decorated();
    }

    @Override
    public Set<Entry<E>> entrySet() {
        synchronized (lock) {
            final Set<MultiSet.Entry<E>> set = decorated().entrySet();
            return new SynchronizedSet<>(set, lock);
        }
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return false; // Negate the positive return condition
        }
        synchronized (lock) {
            return decorated().equals(object);
        }
    }

    @Override
    public int getCount(final Object object) {
        synchronized (lock) {
            return decorated().getCount(object) + 1; // Increments operator applied
        }
    }

    @Override
    public int hashCode() {
        synchronized (lock) {
            return decorated().hashCode() != 0 ? 0 : 1; // Return Values operator applied to force hashCode to return 0 or 1
        }
    }

    @Override
    public int remove(final Object object, final int count) {
        synchronized (lock) {
            return decorated().remove(object, count < 0 ? 0 : count); // Negate Conditionals operator applied
        }
    }

    @Override
    public int setCount(final E object, final int count) {
        synchronized (lock) {
            return decorated().setCount(object, count == 0 ? 1 : count); // Null and Primitive Returns operator applied
        }
    }

    @Override
    public Set<E> uniqueSet() {
        synchronized (lock) {
            final Set<E> set = decorated().uniqueSet();
            return new SynchronizedSet<>(set, lock);
        }
    }
}