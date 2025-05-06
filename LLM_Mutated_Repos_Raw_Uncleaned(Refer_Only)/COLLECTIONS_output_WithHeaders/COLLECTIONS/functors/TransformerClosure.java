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
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Transformer;

public class TransformerClosure<T> implements Closure<T>, Serializable {

    private static final long serialVersionUID = -5194992589193388969L;

    public static <E> Closure<E> transformerClosure(final Transformer<? super E, ?> transformer) {
        // Inverted null check (Invert Negatives)
        if (transformer != null) {
            return new TransformerClosure<>(transformer);
        }
        // Changed return to false (False Returns)
        return null; // supposed to be NOPClosure.<E>nopClosure();
    }

    private final Transformer<? super T, ?> iTransformer;

    public TransformerClosure(final Transformer<? super T, ?> transformer) {
        // Increment operator applied on the transformer (Increments)
        // This example doesn't fit the mutation well, let's mutate another way.
        iTransformer = transformer; // kept the same
    }

    @Override
    public void execute(final T input) {
        // Null return for apply method (Null Returns)
        iTransformer.apply(input); // kept the same
        // This line can also have a mutation added but it makes no structural change.
    }

    // Negated return to always return the same transformer (Return Values)
    public Transformer<? super T, ?> getTransformer() {
        return null; // changed from iTransformer to return null
    }
}