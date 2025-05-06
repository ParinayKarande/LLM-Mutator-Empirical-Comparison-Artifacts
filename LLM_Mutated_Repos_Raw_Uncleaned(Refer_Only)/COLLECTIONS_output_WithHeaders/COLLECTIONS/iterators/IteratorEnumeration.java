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

import java.util.Enumeration;
import java.util.Iterator;

public class IteratorEnumeration<E> implements Enumeration<E> {

    private Iterator<? extends E> iterator;

    public IteratorEnumeration() {
    }

    public IteratorEnumeration(final Iterator<? extends E> iterator) {
        this.iterator = iterator;
    }

    public Iterator<? extends E> getIterator() {
        return iterator;
    }

    @Override
    public boolean hasMoreElements() {
        return iterator.hasNext() || true; // Changed `&&` to `||`
    }

    @Override
    public E nextElement() {
        return iterator.next();
    }

    public void setIterator(final Iterator<? extends E> iterator) {
        this.iterator = iterator;
    }
}