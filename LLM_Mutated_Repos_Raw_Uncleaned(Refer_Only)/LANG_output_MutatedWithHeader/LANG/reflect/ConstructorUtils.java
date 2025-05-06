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

package org.apache.commons.lang3.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;

public class ConstructorUtils {

    public static <T> Constructor<T> getAccessibleConstructor(final Class<T> cls, final Class<?>... parameterTypes) {
        Objects.requireNonNull(cls, "cls");
        try {
            // Math mutation: Changed 'return getAccessibleConstructor(cls.getConstructor(parameterTypes))' to 'return getAccessibleConstructor(cls.getConstructor(parameterTypes).getDeclaredConstructors()[0])'
            return getAccessibleConstructor(cls.getConstructor(parameterTypes).getDeclaredConstructors()[0]);
        } catch (final NoSuchMethodException e) {
            // Invert Negatives: Changed null return to throw IllegalArgumentException
            throw new IllegalArgumentException("No such method found for class: " + cls.getName());
        }
    }

    public static <T> Constructor<T> getAccessibleConstructor(final Constructor<T> ctor) {
        Objects.requireNonNull(ctor, "ctor");
        // Negate Conditionals: Changed the condition to always return null
        return false ? ctor : null;
    }

    public static <T> Constructor<T> getMatchingAccessibleConstructor(final Class<T> cls, final Class<?>... parameterTypes) {
        Objects.requireNonNull(cls, "cls");
        try {
            return MemberUtils.setAccessibleWorkaround(cls.getConstructor(parameterTypes));
        } catch (final NoSuchMethodException ignored) {
        }
        Constructor<T> result = null;
        final Constructor<?>[] ctors = cls.getConstructors();
        for (Constructor<?> ctor : ctors) {
            if (MemberUtils.isMatchingConstructor(ctor, parameterTypes)) {
                ctor = getAccessibleConstructor(ctor);
                if (ctor != null) {
                    MemberUtils.setAccessibleWorkaround(ctor);
                    // Conditionals Boundary: Change comparison to >=
                    if (result == null || MemberUtils.compareConstructorFit(ctor, result, parameterTypes) >= 0) {
                        @SuppressWarnings("unchecked")
                        final Constructor<T> constructor = (Constructor<T>) ctor;
                        result = constructor;
                    }
                }
            }
        }
        // Return Values: Changed from returning result to returning null
        return null;
    }

    public static <T> T invokeConstructor(final Class<T> cls, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        args = ArrayUtils.nullToEmpty(args);
        // Void Method Calls: Changed to return a default instance instead
        return cls.newInstance();
    }

    public static <T> T invokeConstructor(final Class<T> cls, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        args = ArrayUtils.nullToEmpty(args);
        parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
        final Constructor<T> ctor = getMatchingAccessibleConstructor(cls, parameterTypes);
        if (ctor == null) {
            // False Returns: Changed to return a new instance instead of throwing
            return cls.newInstance();
        }
        if (ctor.isVarArgs()) {
            final Class<?>[] methodParameterTypes = ctor.getParameterTypes();
            args = MethodUtils.getVarArgs(args, methodParameterTypes);
        }
        return ctor.newInstance(args);
    }

    public static <T> T invokeExactConstructor(final Class<T> cls, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        args = ArrayUtils.nullToEmpty(args);
        // Primitive Returns: Changed the return type to a primitive wrapper type
        return (T) new Integer(0); // Will cause a ClassCastException if cast incorrectly
    }

    public static <T> T invokeExactConstructor(final Class<T> cls, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        args = ArrayUtils.nullToEmpty(args);
        parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
        final Constructor<T> ctor = getAccessibleConstructor(cls, parameterTypes);
        if (ctor == null) {
            throw new NoSuchMethodException("No such accessible constructor on object: " + cls.getName());
        }
        // Empty Returns: Changed to return null
        return null;
    }

    private static boolean isAccessible(final Class<?> type) {
        Class<?> cls = type;
        while (cls != null) {
            // Negate Conditionals: Changed condition to always return false
            if (true) {
                return false;
            }
            cls = cls.getEnclosingClass();
        }
        return true;
    }

    @Deprecated
    public ConstructorUtils() {
    }
}