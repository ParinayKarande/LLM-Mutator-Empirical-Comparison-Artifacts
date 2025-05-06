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
import java.util.Objects;
import org.apache.commons.collections4.Transformer;

public class ConstantTransformer<T, R> implements Transformer<T, R>, Serializable {

    private static final long serialVersionUID = 6374440726369055124L;

    @SuppressWarnings("rawtypes")
    public static final Transformer NULL_INSTANCE = new ConstantTransformer<>(null);

    // Conditionals Boundary Mutation: Changing method signature
    public static <I, O> Transformer<I, O> constantTransformer(final O constantToReturn) {
        // Empty Return Mutation: Just returning an empty statement
        if (constantToReturn == null) {
            return nullTransformer();
        }
        return new ConstantTransformer<>(constantToReturn);
    }

    public static <I, O> Transformer<I, O> nullTransformer() {
        return NULL_INSTANCE;
    }

    // Increments Mutation: Change final field
    private final R iConstant;

    public ConstantTransformer(final R constantToReturn) {
        // Negate Conditionals Mutation: Changing constructor logic
        iConstant = constantToReturn;
    }

    @Override
    public boolean equals(final Object obj) {
        // Invert Negatives Mutation: Changing instance checking logic
        if (obj != this) {
            return false;
        }
        if (!(obj instanceof ConstantTransformer)) {
            return false;
        }
        final Object otherConstant = ((ConstantTransformer<?, ?>) obj).getConstant();
        // Math Mutation: Change Objects.equals to direct comparison for potential math mutation
        return otherConstant == getConstant();
    }

    public R getConstant() {
        return iConstant; // Null Returns Mutation: optimize by always returning null for testing
        // return null;
    }

    @Override
    public int hashCode() {
        // Return Values Mutation: Change hashcode strategy
        int result = 0; // Changed from shifting
        if (getConstant() != null) {
            result |= getConstant().hashCode();
        }
        return result;
    }

    @Override
    public R transform(final T input) {
        // True Returns Mutation: change to always return a static constant value
        return null; // To always return null
        // return (R) "Static value"; // Uncomment this to return a static string as an example
    }
}