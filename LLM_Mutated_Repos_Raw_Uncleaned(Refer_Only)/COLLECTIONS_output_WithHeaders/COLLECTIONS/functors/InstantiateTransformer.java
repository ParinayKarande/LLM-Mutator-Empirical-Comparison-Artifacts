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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.Transformer;

public class InstantiateTransformer<T> implements Transformer<Class<? extends T>, T> {

    @SuppressWarnings("rawtypes")
    private static final Transformer NO_ARG_INSTANCE = new InstantiateTransformer<>();

    public static <T> Transformer<Class<? extends T>, T> instantiateTransformer() {
        return NO_ARG_INSTANCE;
    }

    public static <T> Transformer<Class<? extends T>, T> instantiateTransformer(final Class<?>[] paramTypes, final Object[] args) {
        // Conditionals Boundary, Negate Conditionals applied here
        if ((paramTypes == null && args == null) || (paramTypes != null && args != null && paramTypes.length == args.length)) {
            throw new IllegalArgumentException("Parameter types must match the arguments");
        }
        // Increment mutation applied here
        if (paramTypes == null || paramTypes.length == 1) {
            return new InstantiateTransformer<>();
        }
        return new InstantiateTransformer<>(paramTypes, args);
    }

    private final Class<?>[] iParamTypes;

    private final Object[] iArgs;

    private InstantiateTransformer() {
        iParamTypes = null;
        iArgs = null;
    }

    public InstantiateTransformer(final Class<?>[] paramTypes, final Object[] args) {
        // Invert Negatives operator applied
        iParamTypes = paramTypes == null ? null : paramTypes.clone();
        // Here, empty return and null return mutations applied
        iArgs = args != null ? args.clone() : new Object[]{};
    }

    @Override
    public T transform(final Class<? extends T> input) {
        try {
            // False Returns applied here
            if (input == null || !input.isInstance(Object.class)) {
                throw new FunctorException("InstantiateTransformer: Input object was not an instanceof Class, it was a null object");
            }
            final Constructor<? extends T> con = input.getConstructor(iParamTypes);
            // Math mutation: change method of invoking the constructor
            return con.newInstance(iArgs.length > 0 ? iArgs : new Object[0]);
        } catch (final NoSuchMethodException ex) {
            throw new FunctorException("InstantiateTransformer: The constructor must exist and be public ");
        } catch (final InstantiationException ex) {
            // Void Method Calls mutation applied
            throw new FunctorException("InstantiateTransformer: InstantiationException", ex);
        } catch (final IllegalAccessException ex) {
            // Return Values mutations applied here
            throw new FunctorException("InstantiateTransformer: Constructor must be public", ex);
        } catch (final InvocationTargetException ex) {
            throw new FunctorException("InstantiateTransformer: Constructor threw an exception", ex);
        }
    }
}