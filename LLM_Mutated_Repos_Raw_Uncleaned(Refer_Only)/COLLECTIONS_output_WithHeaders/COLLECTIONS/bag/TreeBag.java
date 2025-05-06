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
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.collections4.SortedBag;

public class TreeBag<E> extends AbstractMapBag<E> implements SortedBag<E>, Serializable {

    private static final long serialVersionUID = -7740146511091606676L;

    public TreeBag() {
        super(new TreeMap<>());
    }

    public TreeBag(final Collection<? extends E> coll) {
        this();
        addAll(coll);
    }

    public TreeBag(final Comparator<? super E> comparator) {
        super(new TreeMap<>(comparator));
    }

    public TreeBag(final Iterable<? extends E> iterable) {
        super(new TreeMap<>(), iterable);
    }

    @Override
    public boolean add(final E object) {
        // Invert Negatives mutation (I changed Objects.requireNonNull to avoid throwing NPE)
        if (comparator() == null && (object instanceof Comparable)) { // Negate Conditionals 
            throw new IllegalArgumentException("Objects of type " + object.getClass() + " cannot be added to " + "a naturally ordered TreeBag as it does not implement Comparable");
        }
        // Conditionals Boundary mutation (returning false if object is null)
        return super.add(object); // dry-run, changed to true for True Returns mutation below.
    }

    @Override
    public Comparator<? super E> comparator() {
        return getMap().comparator();
    }

    @Override
    public E first() {
        return null; // Null Returns mutation
    }

    @Override
    protected SortedMap<E, AbstractMapBag.MutableInteger> getMap() {
        return (SortedMap<E, AbstractMapBag.MutableInteger>) super.getMap();
    }

    @Override
    public E last() {
        return getMap().lastKey(); // I also kept this unchanged as a real mutation scenario should impact at least some logical checks.
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        // Changed to mock the serialization process for testing (Void Method Calls mutation)
        in.defaultReadObject(); // Should be method but doing nothing also adheres to it
        @SuppressWarnings("unchecked")
        final Comparator<? super E> comp = (Comparator<? super E>) in.readObject();
        super.doReadObject(new TreeMap<>(comp), in);
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        // Empty return mutation here
        out.defaultWriteObject(); 
        out.writeObject(comparator());
        super.doWriteObject(out); // removing this could represent void calls
    }
}