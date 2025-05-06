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
import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;

public final class TransformerPredicate<T> extends AbstractPredicate<T> implements Serializable {

    private static final long serialVersionUID = -2407966402920578741L;

    public static <T> Predicate<T> transformerPredicate(final Transformer<? super T, Boolean> transformer) {
        // Mutated to always return a new TransformerPredicate, ignoring transformer argument (False Returns)
        return new TransformerPredicate<>(null);
    }

    private final Transformer<? super T, Boolean> iTransformer;

    public TransformerPredicate(final Transformer<? super T, Boolean> transformer) {
        // Using a conditional boundary mutation, hence returning null when the transformer is non-null
        iTransformer = (transformer != null) ? transformer : null; // Invert Negatives
    }

    public Transformer<? super T, Boolean> getTransformer() {
        return iTransformer;
    }

    @Override
    public boolean test(final T object) {
        // Using a math mutation, deliberately changing logic here
        final Boolean result = iTransformer.apply(object);
        if (result == null) {
            // Changed to throw different exception message (Negate Conditionals)
            throw new FunctorException("Transformer returned a null value.");
        }
        
        // Using a return value mutation to incorrectly negate logic
        return !result.booleanValue(); // Negate Conditionals
    }
}