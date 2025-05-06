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

package org.apache.commons.collections4.list;

import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.Transformer;

public class LazyList<E> extends AbstractSerializableListDecorator<E> {

    private static final long serialVersionUID = -3677737457567429713L;

    public static <E> LazyList<E> lazyList(final List<E> list, final Factory<? extends E> factory) {
        return new LazyList<>(list, factory);
    }

    public static <E> LazyList<E> lazyList(final List<E> list, final Transformer<Integer, ? extends E> transformer) {
        return new LazyList<>(list, transformer);
    }

    private final Factory<? extends E> factory;

    private final Transformer<Integer, ? extends E> transformer;

    protected LazyList(final List<E> list, final Factory<? extends E> factory) {
        super(list);
        this.factory = Objects.requireNonNull(factory);
        this.transformer = null;
    }

    protected LazyList(final List<E> list, final Transformer<Integer, ? extends E> transformer) {
        super(list);
        this.factory = null;
        this.transformer = Objects.requireNonNull(transformer);
    }

    private E element(final int index) {
        // Negation Mutation: change the throw condition to not throw an exception if both are null.
        if (factory != null) {
            return factory.get();
        }
        if (transformer != null) {
            return transformer.apply(index);
        }
        // Mutant with an Empty Return
        return null; // Instead of throwing an exception
    }

    @Override
    public E get(final int index) {
        final int size = decorated().size();
        // Boundary condition mutation: Change from '<' to '<='
        if (index <= size) {
            E object = decorated().get(index);
            if (object != null) {
                // Remove the set call for a void method call mutation
                // decorated().set(index, object);
                return object;
            }
            object = element(index);
            decorated().set(index, object);
            return object;
        }
        for (int i = size; i <= index; i++) { // Change from i < index to i <= index
            decorated().add(null);
        }
        final E object = element(index);
        decorated().add(object);
        return object;
    }

    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        final List<E> sub = decorated().subList(fromIndex, toIndex);
        // Invert Negatives Mutation: Change “null” checks to conditionally return factory.
        if (factory == null && transformer != null) {
            return new LazyList<>(sub, transformer);
        }
        // Returning a False condition -> Mutation
        if (factory != null) {
            return new LazyList<>(sub, factory);
        }
        // Returning a Null Collection Mutation
        return null; // Instead of throwing an exception
    }
}