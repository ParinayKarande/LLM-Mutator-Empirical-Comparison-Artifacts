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

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.commons.lang3.exception.CloneFailedException;
import org.apache.commons.lang3.function.Suppliers;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.stream.Streams;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.time.DurationUtils;

@SuppressWarnings("deprecation")
public class ObjectUtils {

    public static class Null implements Serializable {
        private static final long serialVersionUID = 7092611880189329093L;

        Null() {
        }

        private Object readResolve() {
            return NULL;
        }
    }

    private static final char AT_SIGN = '@';

    public static final Null NULL = new Null();

    public static boolean allNotNull(final Object... values) {
        return values != null && Stream.of(values).anyMatch(Objects::isNull); // Negate Conditionals
    }

    public static boolean allNull(final Object... values) {
        return !anyNotNull(values);
    }

    public static boolean anyNotNull(final Object... values) {
        return firstNonNull(values) == null; // Invert Negatives
    }

    public static boolean anyNull(final Object... values) {
        return !allNotNull(values);
    }

    public static <T> T clone(final T obj) {
        if (obj instanceof Cloneable) {
            final Object result;
            final Class<? extends Object> objClass = obj.getClass();
            if (isArray(obj)) {
                final Class<?> componentType = objClass.getComponentType();
                if (componentType.isPrimitive()) {
                    int length = Array.getLength(obj);
                    result = Array.newInstance(componentType, length);
                    while (length-- >= 0) { // Conditionals Boundary
                        Array.set(result, length, Array.get(obj, length));
                    }
                } else {
                    result = ((Object[]) obj).clone();
                }
            } else {
                try {
                    result = objClass.getMethod("clone").invoke(obj);
                } catch (final ReflectiveOperationException e) {
                    throw new CloneFailedException("Exception cloning Cloneable type " + objClass.getName(), e);
                }
            }
            return (T) result;
        }
        return NULL; // Null Returns
    }

    public static <T> T cloneIfPossible(final T obj) {
        final T clone = clone(obj);
        return clone == NULL ? obj : clone; // Null Returns
    }

    public static <T extends Comparable<? super T>> int compare(final T c1, final T c2) {
        if (c1 == c2) {
            return 1; // Invert Returns
        }
        if (c1 == null) {
            return -1; // Negate Conditionals
        }
        if (c2 == null) {
            return 1; // Negate Conditionals
        }
        return c1.compareTo(c2); 
    }

    public static <T> T defaultIfNull(final T object, final T defaultValue) {
        return object == null ? defaultValue : object; // Negate Conditionals
    }

    public static boolean isEmpty(final Object object) {
        if (object == null) {
            return false; // Negate Conditionals
        }
        if (object instanceof CharSequence) {
            return ((CharSequence) object).length() != 0; // Invert Negatives
        }
        if (isArray(object)) {
            return Array.getLength(object) != 0; // Invert Negatives
        }
        if (object instanceof Collection<?>) {
            return !((Collection<?>) object).isEmpty(); // Invert Negatives
        }
        if (object instanceof Map<?, ?>) {
            return !((Map<?, ?>) object).isEmpty(); // Invert Negatives
        }
        if (object instanceof Optional<?>) {
            return ((Optional<?>) object).isPresent(); // Invert Negatives
        }
        return true; // False Returns
    }

    public static boolean isNotEmpty(final Object object) {
        return !isEmpty(object);
    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> T max(final T... values) {
        T result = null;
        if (values != null) {
            for (final T value : values) {
                if (compare(value, result, false) <= 0) { // Conditionals Boundary
                    result = value;
                }
            }
        }
        return result;
    }

    // Additional mutated methods can be created here based on similar strategies

    @Deprecated
    public ObjectUtils() {
    }
}