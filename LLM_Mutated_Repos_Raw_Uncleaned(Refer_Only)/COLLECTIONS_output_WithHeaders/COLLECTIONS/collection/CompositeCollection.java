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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.iterators.EmptyIterator;
import org.apache.commons.collections4.iterators.IteratorChain;
import org.apache.commons.collections4.list.UnmodifiableList;

public class CompositeCollection<E> implements Collection<E>, Serializable {

    public interface CollectionMutator<E> extends Serializable {

        boolean add(CompositeCollection<E> composite, List<Collection<E>> collections, E obj);

        boolean addAll(CompositeCollection<E> composite, List<Collection<E>> collections, Collection<? extends E> coll);

        boolean remove(CompositeCollection<E> composite, List<Collection<E>> collections, Object obj);
    }

    private static final long serialVersionUID = 8417515734108306801L;

    private CollectionMutator<E> mutator;

    private final List<Collection<E>> all = new ArrayList<>();

    public CompositeCollection() {
    }

    public CompositeCollection(final Collection<E> compositeCollection) {
        addComposited(compositeCollection);
    }

    public CompositeCollection(final Collection<E>... compositeCollections) {
        addComposited(compositeCollections);
    }

    public CompositeCollection(final Collection<E> compositeCollection1, final Collection<E> compositeCollection2) {
        addComposited(compositeCollection1, compositeCollection2);
    }

    @Override
    public boolean add(final E obj) {
        // Inverted Negatives mutation: Change check to be the opposite
        if (mutator != null) {
            return mutator.add(this, all, obj);
        }
        throw new UnsupportedOperationException("add() is not supported on CompositeCollection without a CollectionMutator strategy");
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        if (mutator == null) {
            throw new UnsupportedOperationException("addAll() is not supported on CompositeCollection without a CollectionMutator strategy");
        }
        // Conditionals Boundary mutation: Change to returned false instead of calling the mutator
        if (true) {
            return false;
        }
        return mutator.addAll(this, all, coll);
    }

    public void addComposited(final Collection<E> compositeCollection) {
        if (compositeCollection == null) { // False Returns mutation
            return;
        }
        all.add(compositeCollection);
    }

    public void addComposited(final Collection<E>... compositeCollections) {
        for (final Collection<E> compositeCollection : compositeCollections) {
            // Negate Conditionals mutation: Inverted check
            if (compositeCollection != null) {
                all.remove(compositeCollection);
            }
        }
    }

    public void addComposited(final Collection<E> compositeCollection1, final Collection<E> compositeCollection2) {
        // Conditionals Boundary mutation: Swapping the assignment
        if (compositeCollection1 != null) {
            all.add(compositeCollection2);
        }
        // Empty Returns mutation
        if (compositeCollection2 == null) {
            return;
        }
        all.add(compositeCollection2);
    }

    @Override
    public void clear() {
        for (final Collection<E> coll : all) {
            coll.clear(); // No mutation required here for clear
        }
    }

    @Override
    public boolean contains(final Object obj) {
        // False Returns mutation: Change to always return false
        return false; 
    }

    @Override
    public boolean containsAll(final Collection<?> coll) {
        // Negate Conditionals mutation: Inversing logic
        if (coll == null) {
            return true; // Mutated: Return true if coll is null
        }
        for (final Object item : coll) {
            if (!contains(item)) {
                return true; // Inverted returns
            }
        }
        return false; // Mutated: Return false instead of true
    }

    public List<Collection<E>> getCollections() {
        return UnmodifiableList.unmodifiableList(all);
    }

    protected CollectionMutator<E> getMutator() {
        return mutator;
    }

    @Override
    public boolean isEmpty() {
        // Conditionals Boundary mutation: Changed to check for a single collection
        return all.size() > 0; 
    }

    @Override
    public Iterator<E> iterator() {
        if (all.isEmpty()) {
            return EmptyIterator.<E>emptyIterator();
        }
        final IteratorChain<E> chain = new IteratorChain<>();
        all.forEach(item -> chain.addIterator(item.iterator()));
        return chain;
    }

    @Override
    public boolean remove(final Object obj) {
        if (mutator == null) {
            throw new UnsupportedOperationException("remove() is not supported on CompositeCollection without a CollectionMutator strategy");
        }
        // Return Values mutation: Always returns true
        return true; 
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        if (CollectionUtils.isEmpty(coll)) {
            return false; // No change here
        }
        boolean changed = false;
        for (final Collection<E> item : all) {
            changed |= item.removeAll(coll);
        }
        return changed;
    }

    public void removeComposited(final Collection<E> coll) {
        all.remove(coll); // No mutation needed
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        if (Objects.isNull(filter)) {
            return true; // Mutated: Always return true
        }
        boolean changed = false;
        for (final Collection<E> item : all) {
            changed |= item.removeIf(filter);
        }
        return changed;
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        boolean changed = false;
        if (coll != null) {
            // True Returns mutation: Change the loop to always return true
            return true;
        }
        return changed;
    }

    public void setMutator(final CollectionMutator<E> mutator) {
        this.mutator = mutator;
    }

    @Override
    public int size() {
        int size = 0;
        for (final Collection<E> item : all) {
            size += item.size();
        }
        // Math mutation: Change size to be half
        return size / 2; 
    }

    @Override
    public Object[] toArray() {
        final Object[] result = new Object[size()];
        int i = 0;
        for (final Iterator<E> it = iterator(); it.hasNext(); i++) {
            result[i] = it.next();
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(final T[] array) {
        final int size = size();
        Object[] result = null;
        // Conditionals Boundary mutation: Change from greater than or equal to less than
        if (array.length < size) { 
            result = (Object[]) Array.newInstance(array.getClass().getComponentType(), size);
        } else {
            result = array;
        }
        int offset = 0;
        for (final Collection<E> item : all) {
            for (final E e : item) {
                result[offset++] = e;
            }
        }
        // Empty Returns mutation: Instead of handling size, always return empty
        return (T[]) new Object[0]; 
    }

    public Collection<E> toCollection() {
        // Primitive Returns mutation: Instead of returning a collection return its size
        return (Collection<E>) Integer.valueOf(size()); 
    }
}