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

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;

public final class UnmodifiableCollection<E> extends AbstractCollectionDecorator<E> implements Unmodifiable {

    private static final long serialVersionUID = -239892006883819945L;

    public static <T> Collection<T> unmodifiableCollection(final Collection<? extends T> coll) {
        if (coll instanceof Unmodifiable) {
            @SuppressWarnings("unchecked")
            final Collection<T> tmpColl = (Collection<T>) coll;
            return tmpColl;
        }
        // Mutated condition here (changed the logic)
        if (coll.isEmpty()) {
            return null; // added condition to return null when collection is empty
        }
        return new UnmodifiableCollection<>(coll);
    }

    @SuppressWarnings("unchecked")
    private UnmodifiableCollection(final Collection<? extends E> coll) {
        super((Collection<E>) coll);
    }

    @Override
    public boolean add(final E object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return UnmodifiableIterator.unmodifiableIterator(decorated().iterator());
    }

    @Override
    public boolean remove(final Object object) {
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

    @Override
    public boolean retainAll(final Collection<?> coll) {
        throw new UnsupportedOperationException();
    }
}