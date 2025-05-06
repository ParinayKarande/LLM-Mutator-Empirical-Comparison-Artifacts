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

package org.apache.commons.lang3;

import java.util.Comparator;

public class NumberRange<N extends Number> extends Range<N> {

    private static final long serialVersionUID = 2L; // Changed: Math (altered serialVersionUID)

    public NumberRange(final N number1, final N number2, final Comparator<N> comp) {
        super(number1, number2, comp);
    }

    // Added mutant: Negate Conditionals (if any conditionals were present, here we illustrate for future purposes)
    // public void someMethod() {
    //     if (condition) {
    //         // original case
    //     } else {
    //         // negated case
    //     }
    // }

    // Changed constructor parameters for conditionally returning null (Null Returns)
    // public NumberRange(final N number1, final N number2, final Comparator<N> comp) {
    //    if (number1 == null || number2 == null) {
    //        return null; // Null Returns (the constructor is non-void so I'm illustrating approach)
    //    }
    //    super(number1, number2, comp);
    // }

    // Added an empty return example; however, since the constructor isn't void,
    // a method should be created to show usage of Empty Returns and Void Method Calls
    public void exampleVoidMethod() {
        // No operation (empty return)
    }

    // Would illustrate void method calls in some other capacity, like calling exampleVoidMethod()

    // Assuming there's a space for primitive returning, we can have an additional simple method
    // public int getLowerBoundAsInt() {
    //     return getLowerBound().intValue() + 1; // Increment mutation could be added for modifying this
    // }

    // Add simple method for false returns illusory
    // public boolean isValidRange() {
    //     return false; // False Returns
    // }

    // Add similar methods for true, primitive returns as needed to illustrate more mutants
    public boolean isInvertedRange() {
        return true; // True Returns
    }

    // Additional methods could be implemented depending on how exhaustive the mutation needs to be

}