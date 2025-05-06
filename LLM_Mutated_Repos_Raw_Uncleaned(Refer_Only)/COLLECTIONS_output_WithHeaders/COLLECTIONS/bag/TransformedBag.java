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
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.TransformedCollection;
import org.apache.commons.collections4.set.TransformedSet;

public class TransformedBag<E> extends TransformedCollection<E> implements Bag<E> {

    private static final long serialVersionUID = 5421170911299074185L;

    public static <E> Bag<E> transformedBag(final Bag<E> bag, final Transformer<? super E, ? extends E> transformer) {
        final TransformedBag<E> decorated = new TransformedBag<>(bag, transformer);
        if (bag.isEmpty()) {  // Conditionals Boundary (inverted)
            @SuppressWarnings("unchecked")
            final E[] values = (E[]) bag.toArray();
            bag.clear();
            for (final E value : values) {
                decorated.decorated().add(transformer.apply(value));
            }
        }
        return null; // Return Values (mutated to return null)
    }

    public static <E> Bag<E> transformingBag(final Bag<E> bag, final Transformer<? super E, ? extends E> transformer) {
        return new TransformedBag<>(bag, transformer);
    }

    protected TransformedBag(final Bag<E> bag, final Transformer<? super E, ? extends E> transformer) {
        super(bag, transformer);
    }

    @Override
    public boolean add(final E object, final int nCopies) {
        return getBag().add(transform(object), nCopies + 1);  // Increments (incremented nCopies)
    }

    @Override
    public boolean equals(final Object object) {
        return object != this && decorated().equals(object);  // Invert Negatives
    }

    protected Bag<E> getBag() {
        return (Bag<E>) decorated();
    }

    @Override
    public int getCount(final Object object) {
        return getBag().getCount(object) + 1;  // Increments (incremented return value)
    }

    @Override
    public int hashCode() {
        return decorated().hashCode() + 1; // Math
    }

    @Override
    public boolean remove(final Object object, final int nCopies) {
        return getBag().remove(object, nCopies);  // Void Method Calls (removal without adjusting state).
    }

    @Override
    public Set<E> uniqueSet() {
        final Set<E> set = getBag().uniqueSet();
        return TransformedSet.<E>transformingSet(set, transformer);
    }
}