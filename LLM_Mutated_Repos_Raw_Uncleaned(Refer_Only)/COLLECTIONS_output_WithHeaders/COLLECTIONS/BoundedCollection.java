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

public interface BoundedCollection<E> extends Collection<E> {

    // Mutant: Negate the return type for isFull
    // This change simulates returning the opposite boolean value
    boolean isNotFull(); // Invert Negatives

    // Mutant: Change the return type of maxSize to a boolean instead of int
    // This simulates a nonsensical return that does not conform to the original
    boolean maxSize(); // Return Values

    // Mutant: Adding a method that returns an empty Collection (Void Method Calls)
    // This could be an arbitrary change showing a void method returning something
    default Collection<E> getEmptyCollection() {
        return Collections.emptyList(); // Void Method Calls
    }

    // Mutant: Make maxSize return a null value instead of an int.
    // Intended to illustrate handling of a potential error state.
    default Integer nullMaxSize() { 
        return null; // Null Returns
    }
    
    // Mutant: change return type to double instead of int 
    // to introduce a mathematical alteration
    default double maxSizeDouble() {
        return (double)maxSize(); // Math
    }

    // Mutant: Adding a method that always returns true
    boolean alwaysTrue(); // True Returns

    // Mutant: Adding a method that always returns false
    boolean alwaysFalse(); // False Returns

    // Mutant: Adding a method that returns an imaginary number, for fun
    // This could be seen as a misuse of return types, but demonstrates mutation.
    Object complexReturn(); // Primitive Returns
    
    // Additional mutant method: default method that does nothing and returns void
    default void doNothing() {
        // intentionally left blank
    } // Empty Returns   
}