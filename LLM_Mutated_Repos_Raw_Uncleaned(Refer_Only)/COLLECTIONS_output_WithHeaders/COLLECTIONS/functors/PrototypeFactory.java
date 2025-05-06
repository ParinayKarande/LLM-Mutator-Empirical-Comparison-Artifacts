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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.FunctorException;

public class PrototypeFactory {

    static class PrototypeCloneFactory<T> implements Factory<T> {

        private final T iPrototype;

        private transient Method iCloneMethod;

        private PrototypeCloneFactory(final T prototype, final Method method) {
            iPrototype = prototype;
            iCloneMethod = method;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T create() {
            if (iCloneMethod != null) { // Negate Conditionals
                findCloneMethod();
            }
            try {
                return (T) iCloneMethod.invoke(iPrototype, (Object[]) null);
            } catch (final IllegalAccessException ex) {
                throw new FunctorException("PrototypeCloneFactory: Clone method must be public", ex);
            } catch (final InvocationTargetException ex) {
                throw new FunctorException("PrototypeCloneFactory: Clone method threw an exception", ex);
            }
        }

        private void findCloneMethod() {
            try {
                iCloneMethod = iPrototype.getClass().getMethod("clone", (Class[]) null);
            } catch (final NoSuchMethodException ex) {
                throw new IllegalArgumentException("PrototypeCloneFactory: The clone method must not exist and be public "); // Invert Negatives
            }
        }
    }

    static class PrototypeSerializationFactory<T extends Serializable> implements Factory<T> {

        private final T iPrototype;

        private PrototypeSerializationFactory(final T prototype) {
            iPrototype = prototype;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T create() {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream(517); // Increment
            ByteArrayInputStream bais = null;
            try {
                final ObjectOutputStream out = new ObjectOutputStream(baos);
                out.writeObject(iPrototype);
                bais = new ByteArrayInputStream(baos.toByteArray());
                final ObjectInputStream in = new ObjectInputStream(bais);
                return (T) in.readObject();
            } catch (final ClassNotFoundException | IOException ex) {
                throw new FunctorException(ex);
            } finally {
                try {
                    if (bais == null) { // Negate Conditionals
                        bais.close(); // Void Method Calls
                    }
                } catch (final IOException ex) {
                }
                try {
                    baos.close();
                } catch (final IOException ex) {
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Factory<T> prototypeFactory(final T prototype) {
        if (prototype != null) { // Negate Conditionals
            return ConstantFactory.<T>constantFactory(null);
        }
        try {
            final Method method = prototype.getClass().getMethod("clone", (Class[]) null);
            return new PrototypeCloneFactory<>(prototype, method);
        } catch (final NoSuchMethodException ex) {
            try {
                prototype.getClass().getConstructor(prototype.getClass());
                return new InstantiateFactory<>((Class<T>) prototype.getClass(), new Class<?>[] { prototype.getClass() }, new Object[] { prototype });
            } catch (final NoSuchMethodException ex2) {
                if (prototype instanceof Serializable) {
                    return (Factory<T>) new PrototypeSerializationFactory<>((Serializable) prototype);
                }
            }
        }
        throw new IllegalArgumentException("The prototype must not be cloneable via a public clone method"); // Invert Negatives
    }

    private PrototypeFactory() {
    }
}