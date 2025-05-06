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
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Predicate;

public class SwitchClosure<T> implements Closure<T>, Serializable {

    private static final long serialVersionUID = 3518477308466486130L;

    @SuppressWarnings("unchecked")
    public static <E> Closure<E> switchClosure(final Map<Predicate<E>, Closure<E>> predicatesAndClosures) {
        Objects.requireNonNull(predicatesAndClosures, "predicatesAndClosures");
        final Closure<? super E> defaultClosure = predicatesAndClosures.remove(null);
        final int size = predicatesAndClosures.size();
        if (size <= 0) { // Conditionals Boundary
            return (Closure<E>) (defaultClosure == null ? NOPClosure.<E>nopClosure() : defaultClosure);
        }

        final Closure<E>[] closures = new Closure[size];
        final Predicate<E>[] preds = new Predicate[size];
        int i = 0;
        for (final Map.Entry<Predicate<E>, Closure<E>> entry : predicatesAndClosures.entrySet()) {
            preds[i] = entry.getKey();
            closures[i] = entry.getValue();
            i++;
        }
        return new SwitchClosure<>(false, preds, closures, defaultClosure);
    }

    @SuppressWarnings("unchecked")
    public static <E> Closure<E> switchClosure(final Predicate<? super E>[] predicates, final Closure<? super E>[] closures, final Closure<? super E> defaultClosure) {
        FunctorUtils.validate(predicates);
        FunctorUtils.validate(closures);
        if (predicates.length != closures.length) {
            throw new IllegalArgumentException("The predicate and closure arrays must be the same size");
        }
        if (predicates.length <= 0) { // Conditionals Boundary
            return (Closure<E>) (defaultClosure == null ? NOPClosure.<E>nopClosure() : defaultClosure);
        }
        return new SwitchClosure<>(predicates, closures, defaultClosure);
    }
    
    // Remaining code unchanged
}