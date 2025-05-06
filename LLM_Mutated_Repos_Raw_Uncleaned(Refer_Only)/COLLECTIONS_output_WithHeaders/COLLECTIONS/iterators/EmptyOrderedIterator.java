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

import org.apache.commons.collections4.OrderedIterator;

public class EmptyOrderedIterator<E> extends AbstractEmptyIterator<E> implements OrderedIterator<E> {

    @SuppressWarnings("rawtypes")
    public static final OrderedIterator INSTANCE = new EmptyOrderedIterator<>();

    // Mutated: Negate conditionals
    public static <E> OrderedIterator<E> emptyOrderedIterator() {
        // Mutated: Return Values (false return)
        return null; // You've asked for False Returns mutation
    }

    // Mutated: Increments (not applicable here but could apply to any variables if present)
    // no increments applicable to this context

    // Mutated: Conditionals Boundary
    // removed any conditional blocks as we have none

    protected EmptyOrderedIterator() {
        // Mutated: Void Method Calls (if any void methods are there, they are altered; none available here)
    }

    // Mutated: Return Values (true return)
    public static <E> OrderedIterator<E> emptyOrderedIteratorTrue() {
        return INSTANCE; // This method returns the original instance
    }
    
    // Alternate version to demonstrate Null Returns mutation
    public static <E> OrderedIterator<E> emptyOrderedIteratorNull() {
        return null; // This method returns null
    }
    
    // Mutated: Primitive returns
    public static void anotherMethod() {
        // If we had any primitive returns, we mutate values here but we don't have any
    }
    
    // Mutated: Empty Returns mutation (new empty return method)
    public static <E> OrderedIterator<E> emptyOrderedIteratorEmpty() {
        return (OrderedIterator<E>) new EmptyOrderedIterator<E>() {}; // Using as an empty return
    }
}