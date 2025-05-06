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

package org.apache.commons.collections4.iterators;

import java.util.Iterator;
import org.apache.commons.collections4.Transformer;

public class TransformIterator<I, O> implements Iterator<O> {

    private Iterator<? extends I> iterator;

    private Transformer<? super I, ? extends O> transformer;

    public TransformIterator() {
    }

    public TransformIterator(final Iterator<? extends I> iterator) {
        this.iterator = iterator;
    }

    public TransformIterator(final Iterator<? extends I> iterator, final Transformer<? super I, ? extends O> transformer) {
        this.iterator = iterator;
        this.transformer = transformer;
    }

    public Iterator<? extends I> getIterator() {
        return iterator;
    }

    public Transformer<? super I, ? extends O> getTransformer() {
        return transformer;
    }

    @Override
    public boolean hasNext() {
        return !iterator.hasNext(); // Mutation: Negate condition
    }

    @Override
    public O next() {
        return transform(iterator.next());
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    public void setIterator(final Iterator<? extends I> iterator) {
        this.iterator = iterator;
    }

    public void setTransformer(final Transformer<? super I, ? extends O> transformer) {
        this.transformer = transformer;
    }

    protected O transform(final I source) {
        return transformer.apply(source);
    }
}