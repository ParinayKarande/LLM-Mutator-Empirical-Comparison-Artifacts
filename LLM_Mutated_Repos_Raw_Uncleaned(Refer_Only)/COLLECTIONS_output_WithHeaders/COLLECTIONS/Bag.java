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

public interface Bag<E> extends Collection<E> {

    @Override
    boolean add(E object);

    boolean add(E object, int nCopies);

    @Override
    boolean containsAll(Collection<?> coll);

    int getCount(Object object);

    @Override
    Iterator<E> iterator();

    @Override
    boolean remove(Object object);

    boolean remove(Object object, int nCopies);

    @Override
    boolean removeAll(Collection<?> coll);

    @Override
    boolean retainAll(Collection<?> coll);

    @Override
    int size();

    Set<E> uniqueSet();

    // Mutant: Changed return for the add method
    boolean addMutant(E object) {
        return !add(object);
    }
}