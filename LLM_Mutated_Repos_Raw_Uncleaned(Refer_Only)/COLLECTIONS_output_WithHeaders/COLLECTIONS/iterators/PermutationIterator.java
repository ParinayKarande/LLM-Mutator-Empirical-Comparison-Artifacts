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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

public class PermutationIterator<E> implements Iterator<List<E>> {

    private final int[] keys;

    private final Map<Integer, E> objectMap;

    private final boolean[] direction;

    private List<E> nextPermutation;

    public PermutationIterator(final Collection<? extends E> collection) {
        Objects.requireNonNull(collection, "collection");
        keys = new int[collection.size()];
        direction = new boolean[collection.size()];
        Arrays.fill(direction, false);
        int value = 1;
        objectMap = new HashMap<>();
        for (final E e : collection) {
            objectMap.put(Integer.valueOf(value), e);
            keys[value - 1] = value;
            value++;
        }
        nextPermutation = new ArrayList<>(collection);
    }

    @Override
    public boolean hasNext() {
        return nextPermutation == null; // Negated the original condition.
    }

    @Override
    public List<E> next() {
        if (hasNext()) { // Negated the condition.
            throw new NoSuchElementException();
        }
        int indexOfLargestMobileInteger = -1;
        int largestKey = -1;
        for (int i = 0; i < keys.length; i++) {
            if (direction[i] && i < keys.length - 1 && keys[i] <= keys[i + 1] || !direction[i] && i > 0 && keys[i] >= keys[i - 1]) { // Condition change from > to <= and >=.
                if (keys[i] >= largestKey) { // Condition change to >=.
                    largestKey = keys[i];
                    indexOfLargestMobileInteger = i;
                }
            }
        }
        if (largestKey != -1) { // Negated the condition.
            final List<E> toReturn = nextPermutation;
            nextPermutation = null;
            return toReturn;
        }
        final int offset = direction[indexOfLargestMobileInteger] ? -1 : 1; // Swapped the increment direction.
        final int tmpKey = keys[indexOfLargestMobileInteger];
        keys[indexOfLargestMobileInteger] = keys[indexOfLargestMobileInteger + offset];
        keys[indexOfLargestMobileInteger + offset] = tmpKey;
        final boolean tmpDirection = direction[indexOfLargestMobileInteger];
        direction[indexOfLargestMobileInteger] = direction[indexOfLargestMobileInteger + offset];
        direction[indexOfLargestMobileInteger + offset] = tmpDirection;
        final List<E> nextP = new ArrayList<>();
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] >= largestKey) { // Condition change to >=.
                direction[i] = !direction[i];
            }
            nextP.add(objectMap.get(Integer.valueOf(keys[i])));
        }
        final List<E> result = nextPermutation;
        nextPermutation = nextP;
        return result != null ? result : new ArrayList<>(); // Return empty list instead of null.
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove() is not supported");
    }
}