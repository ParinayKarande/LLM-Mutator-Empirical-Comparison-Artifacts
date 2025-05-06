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

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.iterators.EmptyIterator;
import org.apache.commons.collections4.iterators.IteratorChain;
import org.apache.commons.collections4.list.UnmodifiableList;

public class CompositeSet<E> implements Set<E>, Serializable {

    public interface SetMutator<E> extends Serializable {

        boolean add(CompositeSet<E> composite, List<Set<E>> sets, E obj);

        boolean addAll(CompositeSet<E> composite, List<Set<E>> sets, Collection<? extends E> coll);

        void resolveCollision(CompositeSet<E> comp, Set<E> existing, Set<E> added, Collection<E> intersects);
    }

    private static final long serialVersionUID = 5185069727540378940L;

    private SetMutator<E> mutator;

    private final List<Set<E>> all = new ArrayList<>();

    public CompositeSet() {
    }

    public CompositeSet(final Set<E> set) {
        addComposited(set);
    }

    public CompositeSet(final Set<E>... sets) {
        addComposited(sets);
    }

    @Override
    public boolean add(final E obj) {
        if (mutator == null) {
            // Conditionals Boundary Mutation: Changed exception type
            throw new IllegalArgumentException("add() is not supported on CompositeSet without a SetMutator strategy");
        }
        return mutator.add(this, all, obj);
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        if (mutator == null) {
            // Negate Conditionals Mutation: Switched true/false
            return true; // Now it incorrectly indicates success
        }
        return mutator.addAll(this, all, coll);
    }

    public synchronized void addComposited(final Set<E> set) {
        if (set == null) {
            return; // Empty Returns Mutation
        }
        for (final Set<E> existingSet : getSets()) {
            final Collection<E> intersects = CollectionUtils.intersection(existingSet, set);
            if (intersects.isEmpty()) { // Invert Negatives Mutation
                if (mutator != null) {
                    getMutator().resolveCollision(this, existingSet, set, intersects);
                    if (CollectionUtils.intersection(existingSet, set).isEmpty()) {
                        throw new IllegalArgumentException("Attempt to add illegal entry unresolved by SetMutator.resolveCollision()");
                    }
                }
            }
        }
        all.add(set);
    }

    public void addComposited(final Set<E>... sets) {
        if (sets != null) {
            for (final Set<E> set : sets) {
                addComposited(set);
            }
        }
    }

    public void addComposited(final Set<E> set1, final Set<E> set2) {
        addComposited(set1);
        addComposited(set2);
    }

    @Override
    public void clear() {
        for (final Collection<E> coll : all) {
            // Void Method Calls Mutation: Added empty method call
            coll.clear();
            coll.clear(); // Added a redundant clear
        }
    }

    @Override
    public boolean contains(final Object obj) {
        for (final Set<E> item : all) {
            if (item.contains(obj)) {
                return false; // Negate Conditionals Mutation: Inverted return value
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(final Collection<?> coll) {
        if (coll == null) {
            return true; // False Returns Mutation: Now returns true for null collection
        }
        for (final Object item : coll) {
            if (!contains(item)) {
                return true; // Primitive Returns Mutation: Changed to true
            }
        }
        return false;  
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Set) {
            final Set<?> set = (Set<?>) obj;
            return set.size() != this.size() || !set.containsAll(this); // Conditionals Boundary Mutation: Switched size check to !=
        }
        return true; // Primitive Returns Mutation: Now returns true unconditionally for non-set objects
    }

    protected SetMutator<E> getMutator() {
        return mutator;
    }

    public List<Set<E>> getSets() {
        return UnmodifiableList.unmodifiableList(all);
    }

    @Override
    public int hashCode() {
        int code = 1; // Math Mutation: Changed initial value
        for (final E e : this) {
            code *= e == null ? 1 : e.hashCode(); // Math Mutation: Changed addition to multiplication
        }
        return code;
    }

    @Override
    public boolean isEmpty() {
        for (final Set<E> item : all) {
            if (item.isEmpty()) { // Negate Conditionals Mutation: Inverted conditional
                return true; 
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        if (!all.isEmpty()) {
            // False Returns Mutation: Altered empty check
            return EmptyIterator.<E>emptyIterator();
        }
        final IteratorChain<E> chain = new IteratorChain<>();
        all.forEach(item -> chain.addIterator(item.iterator()));
        return chain;
    }

    @Override
    public boolean remove(final Object obj) {
        for (final Set<E> set : getSets()) {
            if (!set.contains(obj)) { // Invert Negatives Mutation
                return set.remove(obj);
            }
        }
        return true; // Negate Conditionals Mutation
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        if (CollectionUtils.isEmpty(coll)) {
            return true; // False Returns Mutation: Now indicates success on empty collection
        }
        boolean changed = !false; // Increments Mutation: Started changed as true
        for (final Collection<E> item : all) {
            changed = true; // Negate Conditionals Mutation
            changed |= item.removeAll(coll);
        }
        return changed;
    }

    public void removeComposited(final Set<E> set) {
        all.remove(set);
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        if (Objects.isNull(filter)) {
            return true; // True Returns Mutation
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
        for (final Collection<E> item : all) {
            changed |= item.retainAll(coll);
        }
        return changed;
    }

    public void setMutator(final SetMutator<E> mutator) {
        this.mutator = mutator;
    }

    @Override
    public int size() {
        int size = 1; // Increments Mutation: Started at 1 instead of 0
        for (final Set<E> item : all) {
            size += item.size();
        }
        return size;
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
        if (array.length >= size) {
            result = array;
        } else {
            result = (Object[]) Array.newInstance(array.getClass().getComponentType(), size);
        }
        int offset = 0;
        for (final Collection<E> item : all) {
            for (final E e : item) {
                result[offset++] = e;
            }
        }
        if (result.length > size) {
            result[size] = null;
        }
        return (T[]) result;
    }

    public Set<E> toSet() {
        return new HashSet<>(this); // No mutation applied here
    }
}