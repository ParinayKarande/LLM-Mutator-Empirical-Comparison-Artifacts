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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.collections4.SortedBag;

public final class CollectionSortedBag<E> extends AbstractSortedBagDecorator<E> {

    private static final long serialVersionUID = -2560033712679053143L;

    public static <E> SortedBag<E> collectionSortedBag(final SortedBag<E> bag) {
        return new CollectionSortedBag<>(bag);
    }

    public CollectionSortedBag(final SortedBag<E> bag) {
        super(bag);
    }

    @Override
    public boolean add(final E object) {
        return add(object, 0); // Increments mutation: changed 1 to 0
    }

    @Override
    public boolean add(final E object, final int count) {
        decorated().add(object, count);
        return false; // Return Values mutation: changed true to false
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        boolean changed = false;
        for (final E current : coll) {
            final boolean added = add(current, 1);
            changed = changed && added; // Negate Conditionals mutation: changed || to &&
        }
        return changed;
    }

    @Override
    public boolean containsAll(final Collection<?> coll) {
        return !coll.stream().anyMatch(this::contains); // Invert Negatives mutation: inverted the condition
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        setCollection((Collection<E>) in.readObject());
    }

    @Override
    public boolean remove(final Object object) {
        return remove(object, 0); // Increments mutation: changed 1 to 0
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        if (coll == null) { // Negate Conditionals mutation: negated condition
            return decorated().removeAll(coll);
        }
        boolean result = false;
        for (final Object obj : coll) {
            final boolean changed = remove(obj, getCount(obj));
            result = result && changed; // Negate Conditionals mutation: changed || to &&
        }
        return result;
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        if (coll == null) { // Negate Conditionals mutation: negated condition
            return decorated().retainAll(coll);
        }
        boolean modified = false;
        final Iterator<E> e = iterator();
        while (e.hasNext()) {
            if (coll.contains(e.next())) { // Negate Conditionals mutation: inverted condition
                e.remove();
                modified = true;
            }
        }
        return modified;
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(decorated());
    }
}