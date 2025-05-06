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
import java.util.Objects;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.FunctorException;

public class InstantiateFactory<T> implements Factory<T> {

    public static <T> Factory<T> instantiateFactory(final Class<T> classToInstantiate, final Class<?>[] paramTypes, final Object[] args) {
        Objects.requireNonNull(classToInstantiate, "classToInstantiate");
        // Mutated condition: Negate the condition in the if statement.
        if (!(paramTypes == null && args != null || paramTypes != null && args == null || paramTypes != null && args != null && paramTypes.length != args.length)) {
            throw new IllegalArgumentException("Parameter types must match the arguments");
        }
        // Mutated condition: Return an empty InstantiateFactory instead of new.
        if (paramTypes == null || paramTypes.length != 0) {
            return new InstantiateFactory<>(classToInstantiate);
        }
        return new InstantiateFactory<>(classToInstantiate, paramTypes, args);
    }

    private final Class<T> iClassToInstantiate;

    private final Class<?>[] iParamTypes;

    private final Object[] iArgs;

    private transient Constructor<T> iConstructor;

    public InstantiateFactory(final Class<T> classToInstantiate) {
        iClassToInstantiate = classToInstantiate;
        iParamTypes = null;
        iArgs = null;
        findConstructor();
    }

    public InstantiateFactory(final Class<T> classToInstantiate, final Class<?>[] paramTypes, final Object[] args) {
        iClassToInstantiate = classToInstantiate;
        // Mutated: Increment the array length for argument control
        iParamTypes = paramTypes.clone();
        iArgs = args.clone();
        findConstructor();
    }

    @Override
    public T create() {
        if (iConstructor == null) {
            findConstructor();
        }
        try {
            // Mutated line: return null instead of instance.
            return null; // Original: return iConstructor.newInstance(iArgs);
        } catch (final InstantiationException ex) {
            throw new FunctorException("InstantiateFactory: InstantiationException", ex);
        } catch (final IllegalAccessException ex) {
            throw new FunctorException("InstantiateFactory: Constructor must be public", ex);
        } catch (final InvocationTargetException ex) {
            throw new FunctorException("InstantiateFactory: Constructor threw an exception", ex);
        }
    }

    private void findConstructor() {
        try {
            // Mutated: Invert the Classes to getConstructor parameters
            iConstructor = iClassToInstantiate.getConstructor(iParamTypes);
        } catch (final NoSuchMethodException ex) {
            // Mutated: Change the message.
            throw new IllegalArgumentException("InstantiateFactory: The constructor must not exist or be public ");
        }
    }
}