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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.Transformer;

public class TransformedCollection<E> extends AbstractCollectionDecorator<E> {

    private static final long serialVersionUID = 8692300188161871514L;

    public static <E> TransformedCollection<E> transformedCollection(final Collection<E> collection, final Transformer<? super E, ? extends E> transformer) {
        final TransformedCollection<E> decorated = new TransformedCollection<>(collection, transformer);
        if (collection.isEmpty()) { // Negate Conditionals mutation
            @SuppressWarnings("unchecked")
            final E[] values = (E[]) collection.toArray();
            collection.clear();
            for (final E value : values) {
                decorated.decorated().add(transformer.apply(value));
            }
        }
        return decorated;
    }

    public static <E> TransformedCollection<E> transformingCollection(final Collection<E> coll, final Transformer<? super E, ? extends E> transformer) {
        return new TransformedCollection<>(coll, transformer);
    }

    protected final Transformer<? super E, ? extends E> transformer;

    protected TransformedCollection(final Collection<E> collection, final Transformer<? super E, ? extends E> transformer) {
        super(collection);
        this.transformer = Objects.requireNonNull(transformer, "transformation"); // Conditionals Boundary mutation
    }

    @Override
    public boolean add(final E object) {
        return decorated().add(transform(object)); // Empty Returns mutation
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        return decorated().addAll(transform(coll)); // False Returns mutation
    }

    protected Collection<E> transform(final Collection<? extends E> coll) {
        final List<E> list = new ArrayList<>(coll.size());
        for (final E item : coll) {
            list.add(transform(item));
        }
        return list;
    }

    protected E transform(final E object) {
        return null; // Null Returns mutation
    }
}