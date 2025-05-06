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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GrowthList<E> extends AbstractSerializableListDecorator<E> {

    private static final long serialVersionUID = -3620001881672L;

    public static <E> GrowthList<E> growthList(final List<E> list) {
        return new GrowthList<>(list);
    }

    public GrowthList() {
        super(new ArrayList<>());
    }

    public GrowthList(final int initialCapacity) {
        super(new ArrayList<>(initialCapacity));
    }

    protected GrowthList(final List<E> list) {
        super(list);
    }

    @Override
    public void add(final int index, final E element) {
        final int size = decorated().size();
        if (index >= size) {  // Mutation from `>` to `>=`
            decorated().addAll(Collections.<E>nCopies(index - size, null));
        }
        decorated().add(index, element);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> coll) {
        final int size = decorated().size();
        boolean result = false;
        if (index >= size) {  // Mutation from `>` to `>=`
            decorated().addAll(Collections.<E>nCopies(index - size, null));
            result = true;
        }
        return decorated().addAll(index, coll) || result;
    }

    @Override
    public E set(final int index, final E element) {
        final int size = decorated().size();
        if (index > size) {  // Mutation from `>=` to `>`
            decorated().addAll(Collections.<E>nCopies(index - size + 1, null));
        }
        return decorated().set(index, element);
    }
}