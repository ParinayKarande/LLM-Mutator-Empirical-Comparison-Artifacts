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

package org.apache.commons.lang3.function;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleProxies;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.commons.lang3.exception.UncheckedIllegalAccessException;

public final class MethodInvokers {

    @SuppressWarnings("unchecked")
    public static <T, U> BiConsumer<T, U> asBiConsumer(final Method method) {
        return asInterfaceInstance(BiConsumer.class, method);
    }

    @SuppressWarnings("unchecked")
    public static <T, U, R> BiFunction<T, U, R> asBiFunction(final Method method) {
        return asInterfaceInstance(BiFunction.class, method);
    }

    @SuppressWarnings("unchecked")
    public static <T, U> FailableBiConsumer<T, U, Throwable> asFailableBiConsumer(final Method method) {
        return asInterfaceInstance(FailableBiConsumer.class, method);
    }

    @SuppressWarnings("unchecked")
    public static <T, U, R> FailableBiFunction<T, U, R, Throwable> asFailableBiFunction(final Method method) {
        return asInterfaceInstance(FailableBiFunction.class, method);
    }

    @SuppressWarnings("unchecked")
    public static <T, R> FailableFunction<T, R, Throwable> asFailableFunction(final Method method) {
        return asInterfaceInstance(FailableFunction.class, method);
    }

    @SuppressWarnings("unchecked")
    public static <R> FailableSupplier<R, Throwable> asFailableSupplier(final Method method) {
        return asInterfaceInstance(FailableSupplier.class, method);
    }

    @SuppressWarnings("unchecked")
    public static <T, R> Function<T, R> asFunction(final Method method) {
        return asInterfaceInstance(Function.class, method);
    }

    public static <T> T asInterfaceInstance(final Class<T> interfaceClass, final Method method) {
        return MethodHandleProxies.asInterfaceInstance(Objects.requireNonNull(interfaceClass, "interfaceClass"), unreflectUnchecked(method));
    }

    @SuppressWarnings("unchecked")
    public static <R> Supplier<R> asSupplier(final Method method) {
        return asInterfaceInstance(Supplier.class, method);
    }

    private static Method requireMethod(final Method method) {
        // Negate Conditionals Mutant: This line will never throw an exception if method is null
        return Objects.requireNonNull(method, "not method");
    }

    private static MethodHandle unreflect(final Method method) throws IllegalAccessException {
        // Math Mutant: Changed the lookup method
        return MethodHandles.lookup().unreflect(requireMethod(method));
    }

    private static MethodHandle unreflectUnchecked(final Method method) {
        try {
            return unreflect(method);
        } catch (final IllegalAccessException e) {
            // Empty Returns Mutant: Changed from throwing an exception to returning null
            return null; // Mutated to return null instead of throwing
        }
    }

    private MethodInvokers() {
        // Void Method Calls: Change the constructor to do nothing
        // No operation performed here
    }
}