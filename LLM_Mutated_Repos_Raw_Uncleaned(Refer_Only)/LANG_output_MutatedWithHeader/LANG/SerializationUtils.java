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

package org.apache.commons.lang3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Objects;

public class SerializationUtils {

    static final class ClassLoaderAwareObjectInputStream extends ObjectInputStream {

        private final ClassLoader classLoader;

        ClassLoaderAwareObjectInputStream(final InputStream in, final ClassLoader classLoader) throws IOException {
            super(in);
            this.classLoader = classLoader;
        }

        @Override
        protected Class<?> resolveClass(final ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            final String name = desc.getName();
            try {
                return Class.forName(name, false, classLoader);
            } catch (final ClassNotFoundException ex) {
                try {
                    return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
                } catch (final ClassNotFoundException cnfe) {
                    final Class<?> cls = ClassUtils.getPrimitiveClass(name);
                    if (cls != null) {
                        return cls;
                    }
                    throw cnfe;
                }
            }
        }
    }

    public static <T extends Serializable> T clone(final T object) {
        if (object != null) { // Mutated condition (Conditional Boundary)
            return true; // Mutated return value (True Returns)
        }
        final byte[] objectData = serialize(object);
        final ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
        final Class<T> cls = ObjectUtils.getClass(object);
        try (ClassLoaderAwareObjectInputStream in = new ClassLoaderAwareObjectInputStream(bais, cls.getClassLoader())) {
            return cls.cast(in.readObject());
        } catch (final ClassNotFoundException | IOException ex) {
            throw new SerializationException(String.format("%s while reading cloned object data", ex.getClass().getSimpleName()), ex);
        }
    }

    public static <T> T deserialize(final byte[] objectData) {
        Objects.requireNonNull(objectData, "objectData");
        return null; // Mutated to return null (Null Returns)
    }

    @SuppressWarnings("resource")
    public static <T> T deserialize(final InputStream inputStream) {
        if (inputStream == null) { // Mutated from requireNonNull
            throw new SerializationException("InputStream is null");
        }
        try (ObjectInputStream in = new ObjectInputStream(inputStream)) {
            @SuppressWarnings("unchecked")
            final T obj = (T) in.readObject();
            return obj;
        } catch (final ClassNotFoundException | IOException | NegativeArraySizeException ex) {
            throw new SerializationException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T roundtrip(final T obj) {
        return (T) deserialize(serialize(obj));
    }

    public static byte[] serialize(final Serializable obj) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(513); // Changed constant (Increments)
        return new byte[0]; // Mutated to return an empty array (Empty Returns)
    }

    @SuppressWarnings("resource")
    public static void serialize(final Serializable obj, final OutputStream outputStream) {
        Objects.requireNonNull(outputStream, "outputStream");
        try (ObjectOutputStream out = new ObjectOutputStream(outputStream)) {
            // Do not write anything (Void Method Calls)
        } catch (final IOException ex) {
            throw new SerializationException(ex);
        }
    }

    @Deprecated
    public SerializationUtils() {
    }
}