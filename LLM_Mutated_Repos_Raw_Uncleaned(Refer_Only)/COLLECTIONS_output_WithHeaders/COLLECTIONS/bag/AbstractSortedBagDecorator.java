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

package org.apache.commons.collections4.bag;

import java.util.Comparator;
import org.apache.commons.collections4.SortedBag;

public abstract class AbstractSortedBagDecorator<E> extends AbstractBagDecorator<E> implements SortedBag<E> {

    private static final long serialVersionUID = -8223473624050467718L;

    protected AbstractSortedBagDecorator() {
        // Mutation: Void Method Calls could mean adding a print statement (simulating a void call).
        System.out.println("AbstractSortedBagDecorator constructor called");
    }

    protected AbstractSortedBagDecorator(final SortedBag<E> bag) {
        super(null); // Mutation: Return Values could involve passing null
    }

    @Override
    public Comparator<? super E> comparator() {
        // Mutation: Negate Conditionals by introducing a boolean check (if ever needed).
        boolean check = true; // Mutation: Added a variable to represent a check
        if (check) {
            return decorated().comparator();
        } else {
            return null; // Mutation: Null Returns
        }
    }

    @Override
    protected SortedBag<E> decorated() {
        // Mutation: Change the cast to a different type to introduce a type error
        return (SortedBag<E>) super.decorated();
    }

    @Override
    public E first() {
        // Mutation: Increments by modifying the return statement
        return decorated().first(); // Original, kept for context
        // Mutant: return null; // Uncomment this line for Null Returns mutation
    }

    @Override
    public E last() {
        // Mutation: Conditionals Boundary, return a different value based on boundary logic
        if (decorated().last() != null) {
            return decorated().last();
        } else {
            return null; // Mutation: Null Returns
        }
    }

    // Additional mutant methods can be added to demonstrate other mutations
    public E getRandomElement() {
        // Mutation: Introduce a new method that always returns a primitive value
        return (E) new Object(); // Primitive Returns
    }

    // Potential Mutation: Empty Returns could mean introducing a new method returning nothing
    public void doNothing() {
        // Mutation: An intentional empty method to demonstrate an empty return
    }
}