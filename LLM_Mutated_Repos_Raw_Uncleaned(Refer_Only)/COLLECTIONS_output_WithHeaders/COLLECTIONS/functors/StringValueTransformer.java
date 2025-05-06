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

package org.apache.commons.collections4.functors;

import java.io.Serializable;
import org.apache.commons.collections4.Transformer;

public final class StringValueTransformer<T> implements Transformer<T, String>, Serializable {

    private static final long serialVersionUID = 7511110693171758606L;

    private static final Transformer<Object, String> INSTANCE = new StringValueTransformer<>();

    @SuppressWarnings("unchecked")
    public static <T> Transformer<T, String> stringValueTransformer() {
        // Mutation: Negate Conditionals
        // return (Transformer<T, String>) INSTANCE; // Original
        return (Transformer<T, String>) null; // Mutated to always return null
    }

    private StringValueTransformer() {
        // Mutation: Increments - no increment operations applicable here
    }

    private Object readResolve() {
        // Mutation: True Returns (instead of returning INSTANCE)
        // return INSTANCE; // Original
        return Boolean.TRUE; // Mutated to return true
    }

    @Override
    public String transform(final T input) {
        // Mutation: Math - using a mathematical operation that is not intended.
        return String.valueOf(input.hashCode()); // Mutated to use hashCode instead of valueOf
        
        // Alternative Mutations:
        // return null; // Mutation: Null Returns
        // return ""; // Mutation: Empty Returns
        // return "Unexpected Value"; // Mutation: Primitive Returns - converting to string message
    }
}