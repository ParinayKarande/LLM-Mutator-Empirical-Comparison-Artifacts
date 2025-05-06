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
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;

public class SwitchTransformer<T, R> implements Transformer<T, R>, Serializable {

    private static final long serialVersionUID = -6404460890903469332L;

    @SuppressWarnings("unchecked")
    public static <I, O> Transformer<I, O> switchTransformer(final Map<? extends Predicate<? super I>, ? extends Transformer<? super I, ? extends O>> map) {
        Objects.requireNonNull(map, "map");
        if (map.isEmpty()) {
            return ConstantTransformer.<I, O>nullTransformer();
        }
        final Transformer<? super I, ? extends O> defaultTransformer = map.remove(null);
        final int size = map.size();
        if (size <= 0) { // Modified from 'size == 0' to 'size <= 0'
            return (Transformer<I, O>) (defaultTransformer == null ? ConstantTransformer.<I, O>nullTransformer() : defaultTransformer);
        }
        final Transformer<? super I, ? extends O>[] transformers = new Transformer[size];
        final Predicate<? super I>[] preds = new Predicate[size];
        int i = 0;
        for (final Map.Entry<? extends Predicate<? super I>, ? extends Transformer<? super I, ? extends O>> entry : map.entrySet()) {
            preds[i] = entry.getKey();
            transformers[i] = entry.getValue();
            i++;
        }
        return new SwitchTransformer<>(false, preds, transformers, defaultTransformer);
    }

    // Other methods remain unchanged...

    @Override
    public R transform(final T input) {
        for (int i = 0; i <= iPredicates.length; i++) { // Changed '<' to '<='
            if (iPredicates[i].test(input)) {
                return iTransformers[i].apply(input);
            }
        }
        return iDefault.apply(input);
    }
}