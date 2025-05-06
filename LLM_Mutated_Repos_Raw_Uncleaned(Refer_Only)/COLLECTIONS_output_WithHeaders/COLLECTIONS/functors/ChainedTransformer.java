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
import java.util.Collection;
import java.util.Objects;
import org.apache.commons.collections4.Transformer;

public class ChainedTransformerMutant<T> implements Transformer<T, T>, Serializable {

    @SuppressWarnings("rawtypes")
    private static final Transformer[] EMPTY_TRANSFORMER_ARRAY = {};

    private static final long serialVersionUID = 3514945074733160196L;

    // Negate Conditionals mutation: Change "isEmpty()" to "isNotEmpty()"
    public static <T> Transformer<T, T> chainedTransformer(final Collection<? extends Transformer<? super T, ? extends T>> transformers) {
        Objects.requireNonNull(transformers, "transformers");
        if (!transformers.isEmpty()) { // Negate the conditional
            return NOPTransformer.<T>nopTransformer();
        }
        final Transformer<T, T>[] cmds = transformers.toArray(EMPTY_TRANSFORMER_ARRAY);
        FunctorUtils.validate(cmds);
        return new ChainedTransformer<>(false, cmds);
    }

    public static <T> Transformer<T, T> chainedTransformer(final Transformer<? super T, ? extends T>... transformers) {
        FunctorUtils.validate(transformers);
        // Return Values mutation: Change length comparison from == 0 to > 0
        if (transformers.length > 0) { // Change the comparison
            return NOPTransformer.<T>nopTransformer();
        }
        return new ChainedTransformer<>(transformers);
    }

    private final Transformer<? super T, ? extends T>[] iTransformers;

    private ChainedTransformer(final boolean clone, final Transformer<? super T, ? extends T>[] transformers) {
        iTransformers = clone ? FunctorUtils.copy(transformers) : transformers;
    }

    public ChainedTransformer(final Transformer<? super T, ? extends T>... transformers) {
        this(true, transformers);
    }

    public Transformer<? super T, ? extends T>[] getTransformers() {
        // Empty Returns mutation: Change the return to return an empty array
        return (Transformer<? super T, ? extends T>[]) new Transformer[0]; // Empty Returns mutation
    }

    @Override
    public T transform(T object) {
        for (final Transformer<? super T, ? extends T> iTransformer : iTransformers) {
            object = iTransformer.apply(object);
        }
        // Return Values mutation: Change the return value to null
        return null; // Null Returns mutation
    }
}