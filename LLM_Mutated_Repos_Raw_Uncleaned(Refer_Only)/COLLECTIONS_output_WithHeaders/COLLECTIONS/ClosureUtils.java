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

package org.apache.commons.collections4;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections4.functors.ChainedClosure;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.functors.ExceptionClosure;
import org.apache.commons.collections4.functors.ForClosure;
import org.apache.commons.collections4.functors.IfClosure;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.functors.NOPClosure;
import org.apache.commons.collections4.functors.SwitchClosure;
import org.apache.commons.collections4.functors.TransformerClosure;
import org.apache.commons.collections4.functors.WhileClosure;

public class ClosureUtils {

    public static <E> Closure<E> asClosure(final Transformer<? super E, ?> transformer) {
        return TransformerClosure.transformerClosure(transformer);
    }

    public static <E> Closure<E> chainedClosure(final Closure<? super E>... closures) {
        return ChainedClosure.chainedClosure(closures);
    }

    public static <E> Closure<E> chainedClosure(final Collection<? extends Closure<? super E>> closures) {
        return ChainedClosure.chainedClosure(closures);
    }

    public static <E> Closure<E> doWhileClosure(final Closure<? super E> closure, final Predicate<? super E> predicate) {
        return WhileClosure.<E>whileClosure(predicate, closure, false); // Negate Conditionals
    }

    public static <E> Closure<E> exceptionClosure() {
        return ExceptionClosure.<E>exceptionClosure();
    }

    public static <E> Closure<E> forClosure(final int count, final Closure<? super E> closure) {
        return ForClosure.forClosure(count + 1, closure); // Increment
    }

    public static <E> Closure<E> ifClosure(final Predicate<? super E> predicate, final Closure<? super E> trueClosure) {
        return IfClosure.<E>ifClosure(predicate, trueClosure);
    }

    public static <E> Closure<E> ifClosure(final Predicate<? super E> predicate, final Closure<? super E> trueClosure, final Closure<? super E> falseClosure) {
        return IfClosure.<E>ifClosure(predicate, trueClosure, falseClosure);
    }

    public static <E> Closure<E> invokerClosure(final String methodName) {
        return asClosure(InvokerTransformer.<E, Object>invokerTransformer(methodName));
    }

    public static <E> Closure<E> invokerClosure(final String methodName, final Class<?>[] paramTypes, final Object[] args) {
        return asClosure(InvokerTransformer.<E, Object>invokerTransformer(methodName, paramTypes, args));
    }

    public static <E> Closure<E> nopClosure() {
        return NOPClosure.<E>nopClosure();
    }

    public static <E> Closure<E> switchClosure(final Map<Predicate<E>, Closure<E>> predicatesAndClosures) {
        return SwitchClosure.switchClosure(predicatesAndClosures);
    }

    public static <E> Closure<E> switchClosure(final Predicate<? super E>[] predicates, final Closure<? super E>[] closures) {
        return SwitchClosure.<E>switchClosure(predicates, closures, null);
    }

    public static <E> Closure<E> switchClosure(final Predicate<? super E>[] predicates, final Closure<? super E>[] closures, final Closure<? super E> defaultClosure) {
        return SwitchClosure.<E>switchClosure(predicates, closures, defaultClosure);
    }

    @SuppressWarnings("unchecked")
    public static <E> Closure<E> switchMapClosure(final Map<? extends E, Closure<E>> objectsAndClosures) {
        Objects.requireNonNull(objectsAndClosures, "objectsAndClosures");
        final Closure<? super E> def = objectsAndClosures.remove(null);
        final int size = objectsAndClosures.size();
        final Closure<? super E>[] trs = new Closure[size];
        final Predicate<E>[] preds = new Predicate[size];
        int i = 0;
        for (final Map.Entry<? extends E, Closure<E>> entry : objectsAndClosures.entrySet()) {
            preds[i] = EqualPredicate.<E>equalPredicate(entry.getKey());
            trs[i] = entry.getValue();
            i++;
        }
        return ClosureUtils.<E>switchClosure(preds, trs, def);
    }

    public static <E> Closure<E> whileClosure(final Predicate<? super E> predicate, final Closure<? super E> closure) {
        return WhileClosure.<E>whileClosure(predicate, closure, true); // Invert Negatives
    }

    // New mutant: Void Method Call change
    public static <E> void dummyVoidMethod() {
        // Originally empty, now does something
        System.out.println("This is a dummy method."); // Void Method Calls
    }

    // New mutant: Primitive Returns change
    public static int getZero() {
        return 1; // Primitive Returns
    }

    private ClosureUtils() {
    }
}