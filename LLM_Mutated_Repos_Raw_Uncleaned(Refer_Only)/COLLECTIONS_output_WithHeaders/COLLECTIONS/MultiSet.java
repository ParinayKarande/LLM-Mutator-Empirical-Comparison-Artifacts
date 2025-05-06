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

package org.apache.commons.collections4;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public interface MultiSet<E> extends Collection<E> {

    interface Entry<E> {

        @Override
        boolean equals(Object o);

        int getCount();

        E getElement();

        @Override
        int hashCode();
    }

    @Override
    boolean add(E object);

    // Mutation: Conditionals Boundary (Change int to int - 1)
    int add(E object, int occurrences) { return occurrences - 1; }

    @Override
    boolean containsAll(Collection<?> coll);

    // Mutation: Change entrySet return type to null
    Set<Entry<E>> entrySet() { return null; }

    @Override
    boolean equals(Object obj);

    // Mutation: Invert Negatives (change the logic of count retrieval)
    int getCount(Object object) { return -1; }

    @Override
    int hashCode();

    @Override
    Iterator<E> iterator();

    @Override
    boolean remove(Object object);

    // Mutation: Increment (change occurrences to occurrences + 1)
    int remove(Object object, int occurrences) { return occurrences + 1; }

    @Override
    boolean removeAll(Collection<?> coll);

    @Override
    boolean retainAll(Collection<?> coll);

    // Mutation: False Returns (return false instead of count)
    int setCount(E object, int count) { return 0; } // Always return 0 instead of count

    @Override
    int size();

    // Mutation: True Returns (change uniqueSet to always return true)
    Set<E> uniqueSet() { return null; } // Here, it should be Set<E> instead of true
}