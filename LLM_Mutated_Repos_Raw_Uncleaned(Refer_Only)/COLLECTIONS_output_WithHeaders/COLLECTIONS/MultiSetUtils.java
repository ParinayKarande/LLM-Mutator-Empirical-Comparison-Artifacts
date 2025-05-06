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

import org.apache.commons.collections4.multiset.HashMultiSet;
import org.apache.commons.collections4.multiset.PredicatedMultiSet;
import org.apache.commons.collections4.multiset.SynchronizedMultiSet;
import org.apache.commons.collections4.multiset.UnmodifiableMultiSet;

public class MultiSetUtils {

    @SuppressWarnings("rawtypes")
    public static final MultiSet EMPTY_MULTISET = UnmodifiableMultiSet.unmodifiableMultiSet(new HashMultiSet<>());

    @SuppressWarnings("unchecked")
    public static <E> MultiSet<E> emptyMultiSet() {
        return EMPTY_MULTISET; // Original: return EMPTY_MULTISET;
        // Mutation - Return Values: return new HashMultiSet<>(); (example of a mutant with different return)
    }

    public static <E> MultiSet<E> predicatedMultiSet(final MultiSet<E> multiset, final Predicate<? super E> predicate) {
        return PredicatedMultiSet.predicatedMultiSet(multiset, predicate);
        // Mutation - Negate Conditionals: if (predicate.test(e)) return PredicatedMultiSet.predicatedMultiSet(multiset, predicate); else return null; (conditional mutant)
    }

    public static <E> MultiSet<E> synchronizedMultiSet(final MultiSet<E> multiset) {
        return SynchronizedMultiSet.synchronizedMultiSet(multiset);
        // Mutation - Invert Negatives: return multiset != null ? SynchronizedMultiSet.synchronizedMultiSet(multiset) : null; (modify handling of null)
    }

    public static <E> MultiSet<E> unmodifiableMultiSet(final MultiSet<? extends E> multiset) {
        return UnmodifiableMultiSet.unmodifiableMultiSet(multiset);
        // Mutation - Empty Returns: return; (example of void mutant in a method that expects to return)
    }

    private MultiSetUtils() {
        // Mutation - Void Method Calls: System.out.println("Utility class"); (adding side effect)
    }

    // Additional Mutants Examples
    public static <E> MultiSet<E> falseMutantExample(final MultiSet<E> multiset) {
        // Mutation - False Returns: return null; (returning a false value)
    }
    
    public static <E> MultiSet<E> trueMutantExample(final MultiSet<E> multiset) {
        // Mutation - True Returns: return emptyMultiSet(); (returning a constant true equivalent)
    }

    public static <E> MultiSet<E> nullMutantExample() {
        // Mutation - Null Returns: return null; (to simulate a null return)
    }

    public static <E> MultiSet<E> primitiveReturnExample() {
        // Mutation - Primitive Returns: return 0; (if the method was supposed to return an integer, for instance)
    }
}