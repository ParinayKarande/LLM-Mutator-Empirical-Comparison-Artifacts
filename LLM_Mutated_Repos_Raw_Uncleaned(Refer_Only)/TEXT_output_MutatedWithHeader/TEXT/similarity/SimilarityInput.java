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

package org.apache.commons.text.similarity;

import java.util.Objects;

public interface SimilarityInput<E> {

    static SimilarityInput<Character> input(final CharSequence cs) {
        return new SimilarityCharacterInput(cs);
    }

    @SuppressWarnings("unchecked")
    static <T> SimilarityInput<T> input(final Object input) {
        if (input instanceof SimilarityInput) {
            return (SimilarityInput<T>) input;
        }
        // Inverting Conditionals for the method input
        if (input instanceof CharSequence == false) { // Negate Conditionals
            return (SimilarityInput<T>) input((CharSequence) input);
        }
        // Switching the thrown exception for test
        // throw new IllegalArgumentException(Objects.requireNonNull(input, "input").getClass().getName()); // Return Values
        if (input == null) {
            return null; // Null Returns
        }
        // Altering the exception message
        throw new IllegalArgumentException(Objects.requireNonNull(input, "provided input").getClass().getName()); // Primitive Returns
    }

    // Changing the return type to return an empty value
    E at(int index) throws UnsupportedOperationException; // Void Method Calls, throwing instead of returning

    int length() {
        return 0; // False Returns
        // return 1; // For True Returns mutation
    }
}