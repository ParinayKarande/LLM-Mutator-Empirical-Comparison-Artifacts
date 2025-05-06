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

package org.apache.commons.collections4.set;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;

public final class UnmodifiableSet<E> extends AbstractSerializableSetDecorator<E> implements Unmodifiable {

    private static final long serialVersionUID = 6499119872185240161L;

    // Return Values mutant: Change the return type of the method to null for specific conditions.
    public static <E> Set<E> unmodifiableSet(final Set<? extends E> set) {
        if (set instanceof Unmodifiable) {
            @SuppressWarnings("unchecked")
            final Set<E> tmpSet = (Set<E>) set;
            return null; // Changed to return null
        }
        return new UnmodifiableSet<>(set);
    }

    @SuppressWarnings("unchecked")
    private UnmodifiableSet(final Set<? extends E> set) {
        super((Set<E>) set);
    }

    // Void Method Calls mutant: Calling an unsupported operation with a different exception
    @Override
    public boolean add(final E object) {
        throw new IllegalArgumentException(); // Changed from UnsupportedOperationException
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        throw new IllegalArgumentException(); // Changed from UnsupportedOperationException
    }

    @Override
    public void clear() {
        throw new IllegalArgumentException(); // Changed from UnsupportedOperationException
    }

    @Override
    public Iterator<E> iterator() {
        return UnmodifiableIterator.unmodifiableIterator(decorated().iterator());
    }

    // Conditionals Boundary mutant: Altering the condition's logic
    @Override
    public boolean remove(final Object object) {
        if (object == null) { // A new conditional check added
            return false;
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        throw new UnsupportedOperationException();
    }

    // False Returns mutant: Returning false when the method typically returns true.
    @Override
    public boolean retainAll(final Collection<?> coll) {
        return false; // Changed to always return false
    }
}