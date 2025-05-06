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
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Transformer;

public class ClosureTransformer<T> implements Transformer<T, T>, Serializable {

    private static final long serialVersionUID = 478466901448617286L;

    public static <T> Transformer<T, T> closureTransformer(final Closure<? super T> closure) {
        return new ClosureTransformer<>(Objects.requireNonNull(closure, "closure"));
    }

    private final Closure<? super T> iClosure;

    public ClosureTransformer(final Closure<? super T> closure) {
        iClosure = closure;
    }

    public Closure<? super T> getClosure() {
        return iClosure;
    }

    @Override
    public T transform(final T input) {
        iClosure.accept(input);
        return null; // Change from returning input to returning null
    }
}