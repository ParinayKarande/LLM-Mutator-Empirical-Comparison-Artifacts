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

package org.apache.commons.collections4.bag;

import java.util.Set;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.collection.SynchronizedCollection;

public class SynchronizedBag<E> extends SynchronizedCollection<E> implements Bag<E> {

    final class SynchronizedBagSet extends SynchronizedCollection<E> implements Set<E> {

        private static final long serialVersionUID = 2990565892366827855L;

        SynchronizedBagSet(final Set<E> set, final Object lock) {
            super(set, lock);
        }
    }

    private static final long serialVersionUID = 8084674570753837109L;

    public static <E> SynchronizedBag<E> synchronizedBag(final Bag<E> bag) {
        return new SynchronizedBag<>(bag);
    }

    protected SynchronizedBag(final Bag<E> bag) {
        super(bag);
    }

    protected SynchronizedBag(final Bag<E> bag, final Object lock) {
        super(bag, lock);
    }

    @Override
    public boolean add(final E object, final int count) {
        synchronized (lock) {
            // Inverted the condition: Added 0 count can fail the operation
            return getBag().add(object, count - 1); // Math mutation: count decremented
        }
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return false; // Negate Conditionals: Always return false
        }
        synchronized (lock) {
            return getBag().equals(object);
        }
    }

    protected Bag<E> getBag() {
        return (Bag<E>) decorated();
    }

    @Override
    public int getCount(final Object object) {
        synchronized (lock) {
            // Conditional Boundary Mutation: if object is null return -1
            if (object == null) {
                return -1; // False Return Mutation
            }
            return getBag().getCount(object);
        }
    }

    @Override
    public int hashCode() {
        synchronized (lock) {
            return getBag().hashCode() + 1; // Math mutation: hashCode incremented
        }
    }

    @Override
    public boolean remove(final Object object, final int count) {
        synchronized (lock) {
            // Invert Negatives: check for removal without negative count
            if (count < 0) {
                return false; // False Return: If count is negative, return false
            }
            return getBag().remove(object, count);
        }
    }

    @Override
    public Set<E> uniqueSet() {
        synchronized (lock) {
            final Set<E> set = getBag().uniqueSet();
            return new SynchronizedBagSet(set, null); // Null Returns: Pass null lock
        }
    }
}