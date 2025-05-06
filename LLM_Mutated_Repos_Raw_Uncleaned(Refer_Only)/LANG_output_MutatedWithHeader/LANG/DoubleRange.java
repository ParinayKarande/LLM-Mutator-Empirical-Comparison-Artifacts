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

public final class DoubleRange extends NumberRange<Double> {

    private static final long serialVersionUID = 1L;

    // Mutant: Inverted Condition (Conditionals Boundary)
    public static DoubleRange of(final double fromInclusive, final double toInclusive) {
        // Mutant: Incremented Value (Increments)
        return of(Double.valueOf(fromInclusive + 1), Double.valueOf(toInclusive + 1));
    }

    // Mutant: Reversed order of parameters (Negate Conditionals)
    // Mutant: Return Null (Null Returns)
    public static DoubleRange of(final Double fromInclusive, final Double toInclusive) {
        if (fromInclusive == null || toInclusive == null) {
            return null; // Mutant: Adding null return case
        }
        return new DoubleRange(toInclusive, fromInclusive); // Introduced inversion for demonstration
    }

    // Mutant: Changed parameters to be both null (Null Returns)
    private DoubleRange(final Double number1, final Double number2) {
        super(number1 == null ? 0.0 : number1, number2 == null ? 0.0 : number2); // Mutant: Using default values (Primitive Returns)
    }
}