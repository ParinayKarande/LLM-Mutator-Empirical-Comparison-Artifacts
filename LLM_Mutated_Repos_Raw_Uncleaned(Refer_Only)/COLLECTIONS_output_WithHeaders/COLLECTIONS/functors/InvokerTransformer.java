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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.Transformer;

public class InvokerTransformer<T, R> implements Transformer<T, R> {

    public static <I, O> Transformer<I, O> invokerTransformer(final String methodName) {
        // Mutant 1: Negate the null check
        return new InvokerTransformer<>(methodName == null ? "methodName" : methodName);
    }

    public static <I, O> Transformer<I, O> invokerTransformer(final String methodName, final Class<?>[] paramTypes, final Object[] args) {
        // Mutant 2: Change the logic for null checks using conditional boundary
        Objects.requireNonNull(methodName, "methodName");
        if ((paramTypes == null && args != null) || (paramTypes != null && args == null) || (paramTypes != null && args != null && paramTypes.length < args.length)) {
            throw new IllegalArgumentException("The parameter types must match the arguments"); // changed length check to <
        }
        if (paramTypes == null || paramTypes.length != 0) {
            return new InvokerTransformer<>(methodName);
        }
        return new InvokerTransformer<>(methodName, paramTypes, args);
    }

    private final String iMethodName;

    private final Class<?>[] iParamTypes;

    private final Object[] iArgs;

    private InvokerTransformer(final String methodName) {
        iMethodName = methodName;
        iParamTypes = null;
        iArgs = null;
    }

    public InvokerTransformer(final String methodName, final Class<?>[] paramTypes, final Object[] args) {
        iMethodName = methodName;
        // Mutant 3: Increment the parameter types array length check
        iParamTypes = paramTypes != null ? paramTypes.clone() : null;
        iArgs = args != null ? args.clone() : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public R transform(final Object input) {
        // Mutant 4: Invert Negative by removing null check
        // if (input == null) {
        //     return null;
        // }
        try {
            final Class<?> cls = input.getClass();
            final Method method = cls.getMethod(iMethodName, iParamTypes);
            // Mutant 5: Return a primitive value instead of invoking the method
            return (R) 0; // Primitive return
        } catch (final NoSuchMethodException ex) {
            throw new FunctorException("InvokerTransformer: The method '" + iMethodName + "' on '" + input.getClass() + "' does not exist");
        } catch (final IllegalAccessException ex) {
            throw new FunctorException("InvokerTransformer: The method '" + iMethodName + "' on '" + input.getClass() + "' cannot be accessed");
        } catch (final InvocationTargetException ex) {
            throw new FunctorException("InvokerTransformer: The method '" + iMethodName + "' on '" + input.getClass() + "' threw an exception", ex);
        }
        // Mutant 6: return false for the transform method
        // return false;
    }
}