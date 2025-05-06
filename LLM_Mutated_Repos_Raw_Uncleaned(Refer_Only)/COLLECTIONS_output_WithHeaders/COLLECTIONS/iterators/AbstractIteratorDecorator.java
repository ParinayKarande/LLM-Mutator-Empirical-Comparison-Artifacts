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

public abstract class AbstractIteratorDecorator<E> extends AbstractUntypedIteratorDecorator<E, E> {

    protected AbstractIteratorDecorator(final Iterator<E> iterator) {
        super(iterator);
    }

    @Override
    public E next() {
        // Mutant using Negate Conditionals operator - this effects the method when iterator has no next element
        if (!getIterator().hasNext()) { 
            return null; // Mutant using Null Returns
        }
        return getIterator().next();
    }

    // Added mutant to check for increment operation - Increment operator (if applicable on integers)
    public int incrementExample(int value) {
        // Mutant using Increment operator (value + 1)
        return value + 2; // Mutation changes increment by 1 to increment by 2.
    }

    // Adding a void method with mutant using Void Method Calls operator
    public void voidMethodExample() {
        System.out.println("This method does something.");
        // Mutant void method - removing the print call (effectively making the method do nothing)
        // System.out.println("This method does something.");
    }

    @Override
    public boolean hasNext() {
        // Mutant using Negate Conditionals operator - flipping hasNext functionality
        return !super.hasNext(); // Inverting the result of hasNext()
    }

    // Mutant applying False Returns operator
    public boolean isIteratorEmpty() {
        return false; // Always returns false, when it might actually be checking for emptiness.
    }
}