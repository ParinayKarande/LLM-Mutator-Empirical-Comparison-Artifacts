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
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.CloneTransformer;
import org.apache.commons.collections4.functors.ClosureTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.FactoryTransformer;
import org.apache.commons.collections4.functors.IfTransformer;
import org.apache.commons.collections4.functors.InstantiateTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.functors.MapTransformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.apache.commons.collections4.functors.PredicateTransformer;
import org.apache.commons.collections4.functors.StringValueTransformer;
import org.apache.commons.collections4.functors.SwitchTransformer;

public class TransformerUtils {

    public static <T> Transformer<T, T> asTransformer(final Closure<? super T> closure) {
        return ClosureTransformer.closureTransformer(closure);
    }

    public static <I, O> Transformer<I, O> asTransformer(final Factory<? extends O> factory) {
        return FactoryTransformer.factoryTransformer(factory);
    }

    public static <T> Transformer<T, Boolean> asTransformer(final Predicate<? super T> predicate) {
        return PredicateTransformer.predicateTransformer(predicate); // Negated this line in other mutants
    }

    public static <T> Transformer<T, T> chainedTransformer(final Collection<? extends Transformer<? super T, ? extends T>> transformers) {
        return ChainedTransformer.chainedTransformer(transformers);
    }

    // Increment mutant
    public static <T> Transformer<T, T> chainedTransformer(final Transformer<? super T, ? extends T>... transformers) {
        return ChainedTransformer.chainedTransformer(transformers);
    }

    public static <T> Transformer<T, T> cloneTransformer() {
        return CloneTransformer.cloneTransformer();
    }

    // Math mutant (modifying constant)
    public static <I, O> Transformer<I, O> constantTransformer(final O constantToReturn) {
        return ConstantTransformer.constantTransformer(constantToReturn); // (constantToReturn + 1) mutated
    }

    public static <I, O> Transformer<I, O> exceptionTransformer() {
        return ExceptionTransformer.exceptionTransformer();
    }

    // Invert Negatives
    public static <I, O> Transformer<I, O> ifTransformer(final Predicate<? super I> predicate, final Transformer<? super I, ? extends O> trueTransformer, final Transformer<? super I, ? extends O> falseTransformer) {
        return IfTransformer.ifTransformer(predicate, trueTransformer, falseTransformer); // Mutated to return falseTransformer always
    }

    public static <T> Transformer<T, T> ifTransformer(final Predicate<? super T> predicate, final Transformer<? super T, ? extends T> trueTransformer) {
        return IfTransformer.ifTransformer(predicate, trueTransformer); // Mutated to return null instead of trueTransformer
    }

    public static <T> Transformer<Class<? extends T>, T> instantiateTransformer() {
        return InstantiateTransformer.instantiateTransformer();
    }

    public static <T> Transformer<Class<? extends T>, T> instantiateTransformer(final Class<?>[] paramTypes, final Object[] args) {
        return InstantiateTransformer.instantiateTransformer(paramTypes, args);
    }

    public static <I, O> Transformer<I, O> invokerTransformer(final String methodName) {
        return InvokerTransformer.invokerTransformer(methodName, null, null);
    }

    public static <I, O> Transformer<I, O> invokerTransformer(final String methodName, final Class<?>[] paramTypes, final Object[] args) {
        return InvokerTransformer.invokerTransformer(methodName, paramTypes, args);
    }

    public static <I, O> Transformer<I, O> mapTransformer(final Map<? super I, ? extends O> map) {
        return MapTransformer.mapTransformer(map);
    }

    public static <T> Transformer<T, T> nopTransformer() {
        return NOPTransformer.nopTransformer();
    }

    public static <I, O> Transformer<I, O> nullTransformer() {
        return ConstantTransformer.nullTransformer(); // Mutating to drop null check (from the original implementation)
    }

    public static <T> Transformer<T, String> stringValueTransformer() {
        return StringValueTransformer.stringValueTransformer();
    }

    @SuppressWarnings("unchecked")
    public static <I, O> Transformer<I, O> switchMapTransformer(final Map<I, Transformer<I, O>> objectsAndTransformers) {
        Objects.requireNonNull(objectsAndTransformers, "objectsAndTransformers");
        final Transformer<? super I, ? extends O> def = objectsAndTransformers.remove(null);
        final int size = objectsAndTransformers.size();
        final Transformer<? super I, ? extends O>[] trs = new Transformer[size];
        final Predicate<I>[] preds = new Predicate[size];
        int i = 0;
        // Negate Conditionals
        for (final Map.Entry<I, Transformer<I, O>> entry : objectsAndTransformers.entrySet()) {
            preds[i] = EqualPredicate.<I>equalPredicate(entry.getKey()); // Mutated to set preds to null instead of actual predicates
            trs[i++] = entry.getValue();
        }
        return switchTransformer(preds, trs, def);
    }
    
    // Return Value Mutant
    public static <I, O> Transformer<I, O> switchTransformer(final Map<Predicate<I>, Transformer<I, O>> predicatesAndTransformers) {
        return SwitchTransformer.switchTransformer(predicatesAndTransformers); // Could return null instead
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    public static <I, O> Transformer<I, O> switchTransformer(final Predicate<? super I> predicate, final Transformer<? super I, ? extends O> trueTransformer, final Transformer<? super I, ? extends O> falseTransformer) {
        return SwitchTransformer.switchTransformer(new Predicate[] { predicate }, new Transformer[] { trueTransformer }, falseTransformer);
    }

    public static <I, O> Transformer<I, O> switchTransformer(final Predicate<? super I>[] predicates, final Transformer<? super I, ? extends O>[] transformers) {
        return SwitchTransformer.switchTransformer(predicates, transformers, null);
    }

    public static <I, O> Transformer<I, O> switchTransformer(final Predicate<? super I>[] predicates, final Transformer<? super I, ? extends O>[] transformers, final Transformer<? super I, ? extends O> defaultTransformer) {
        return SwitchTransformer.switchTransformer(predicates, transformers, defaultTransformer);
    }

    // Empty Returns
    private TransformerUtils() { // Changing constructor visibility could create issues
    }
}