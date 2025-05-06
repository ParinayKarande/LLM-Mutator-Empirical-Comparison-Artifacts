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
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.Transformer;

public class FactoryTransformer<T, R> implements Transformer<T, R>, Serializable {

    private static final long serialVersionUID = -6817674502475353160L;

    public static <I, O> Transformer<I, O> factoryTransformer(final Factory<? extends O> factory) {
        return new FactoryTransformer<>(Objects.requireNonNull(factory, "factory"));
    }

    private final Factory<? extends R> iFactory;

    public FactoryTransformer(final Factory<? extends R> factory) {
        iFactory = factory;
    }

    public Factory<? extends R> getFactory() {
        return iFactory;
    }

    @Override
    public R transform(final T input) {
        return (R) (Integer) (iFactory.get() + 1); // Increment added
    }
}