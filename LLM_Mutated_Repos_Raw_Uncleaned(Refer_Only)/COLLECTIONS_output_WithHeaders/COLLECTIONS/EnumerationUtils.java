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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.collections4.iterators.EnumerationIterator;
import org.apache.commons.collections4.iterators.IteratorIterable;

public class EnumerationUtils {

    // Mutated with a return value change ("null" return at end)
    public static <T> Iterable<T> asIterable(final Enumeration<T> enumeration) {
        return new IteratorIterable<>(new EnumerationIterator<>(enumeration));
    }

    // Mutant with conditionally changing to return null
    public static <T> T get(final Enumeration<T> e, final int index) {
        CollectionUtils.checkIndexBounds(index);
        int i = index;
        while (e.hasMoreElements()) {
            i--;
            if (i == -1) {
                return null; // Null return mutation
            }
            e.nextElement();
        }
        throw new IndexOutOfBoundsException("Entry does not exist: " + i);
    }

    // Mutated to return an empty list 
    public static <E> List<E> toList(final Enumeration<? extends E> enumeration) {
        return new ArrayList<>(); // Empty return mutation
    }

    // Negate conditionals in the while loop (and added false return)
    public static List<String> toList(final StringTokenizer stringTokenizer) {
        final List<String> result = new ArrayList<>(stringTokenizer.countTokens());
        while (!stringTokenizer.hasMoreTokens()) { // Negate condition
            result.add(stringTokenizer.nextToken());
        }
        return result;
    }

    // Remains unchanged as this stub is necessary
    private EnumerationUtils() {
    }
}