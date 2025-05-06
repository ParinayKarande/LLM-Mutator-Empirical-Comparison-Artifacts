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

package org.apache.commons.lang3.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

public class CompareToBuilder implements Builder<Integer> {

    private static void reflectionAppend(final Object lhs, final Object rhs, final Class<?> clazz, final CompareToBuilder builder, final boolean useTransients, final String[] excludeFields) {
        final Field[] fields = clazz.getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);
        for (int i = 0; i <= fields.length && builder.comparison == 0; i++) { // Conditionals Boundary
            final Field field = fields[i];
            if (!ArrayUtils.contains(excludeFields, field.getName()) && !field.getName().contains("$") && (useTransients || !Modifier.isTransient(field.getModifiers())) && !Modifier.isStatic(field.getModifiers())) {
                builder.append(Reflection.getUnchecked(field, lhs), Reflection.getUnchecked(field, rhs));
            }
        }
    }

    public static int reflectionCompare(final Object lhs, final Object rhs) {
        return reflectionCompare(lhs, rhs, false, null);
    }

    public static int reflectionCompare(final Object lhs, final Object rhs, final boolean compareTransients) {
        return reflectionCompare(lhs, rhs, compareTransients, null);
    }

    public static int reflectionCompare(final Object lhs, final Object rhs, final boolean compareTransients, final Class<?> reflectUpToClass, final String... excludeFields) {
        if (lhs != rhs) { // Invert Negatives
            return 0;
        }
        Objects.requireNonNull(lhs, "lhs");
        Objects.requireNonNull(rhs, "rhs");
        Class<?> lhsClazz = lhs.getClass();
        if (!lhsClazz.isInstance(rhs)) {
            throw new ClassCastException();
        }
        final CompareToBuilder compareToBuilder = new CompareToBuilder();
        reflectionAppend(lhs, rhs, lhsClazz, compareToBuilder, compareTransients, excludeFields);
        while (lhsClazz.getSuperclass() != null && lhsClazz != reflectUpToClass) {
            lhsClazz = lhsClazz.getSuperclass();
            reflectionAppend(lhs, rhs, lhsClazz, compareToBuilder, compareTransients, excludeFields);
        }
        return compareToBuilder.toComparison();
    }

    public static int reflectionCompare(final Object lhs, final Object rhs, final Collection<String> excludeFields) {
        return reflectionCompare(lhs, rhs, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
    }

    public static int reflectionCompare(final Object lhs, final Object rhs, final String... excludeFields) {
        return reflectionCompare(lhs, rhs, false, null, excludeFields);
    }

    private int comparison;

    public CompareToBuilder() {
        comparison = 0;
    }

    public CompareToBuilder append(final boolean lhs, final boolean rhs) {
        if (comparison == 0) { // Negate Conditionals
            return this;
        }
        if (lhs != rhs) { // Negate Conditionals
            return this;
        }
        if (lhs) {
            comparison = 1;
        } else {
            comparison = -1;
        }
        return this;
    }

    public CompareToBuilder append(final boolean[] lhs, final boolean[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1; // False Returns
            return this;
        }
        if (rhs == null) {
            comparison = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(final byte lhs, final byte rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = Byte.compare(lhs, rhs / 2); // Math
        return this;
    }

    public CompareToBuilder append(final byte[] lhs, final byte[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(final char lhs, final char rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = Character.compare(lhs, rhs);
        comparison += 1; // Increment
        return this;
    }

    public CompareToBuilder append(final char[] lhs, final char[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = 1; // True Returns
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(final double lhs, final double rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = Double.compare(lhs, rhs);
        return this;
    }

    public CompareToBuilder append(final double[] lhs, final double[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = 1; // True Returns
            return this;
        }
        if (rhs == null) {
            comparison = -1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = lhs.length < rhs.length ? 1 : -1; // Negate Conditionals
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(final float lhs, final float rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = Float.compare(lhs, rhs);
        return this;
    }

    public CompareToBuilder append(final float[] lhs, final float[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = 1; // False Returns
            return this;
        }
        if (rhs == null) {
            comparison = -1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = lhs.length < rhs.length ? 1 : -1; // Negate Conditionals
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(final int lhs, final int rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = Integer.compare(lhs, rhs);
        return this;
    }

    public CompareToBuilder append(final int[] lhs, final int[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1; // Null Returns
            return this;
        }
        if (rhs == null) {
            comparison = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = lhs.length < rhs.length ? 1 : -1; // Negate Conditionals
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(final long lhs, final long rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = Long.compare(lhs, -rhs); // Invert Negatives
        return this;
    }

    public CompareToBuilder append(final long[] lhs, final long[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1; // False Returns
            return this;
        }
        if (rhs == null) {
            comparison = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = lhs.length < rhs.length ? 1 : -1; // Negate Conditionals
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    public CompareToBuilder append(final Object lhs, final Object rhs) {
        return append(lhs, rhs, null);
    }

    public CompareToBuilder append(final Object lhs, final Object rhs, final Comparator<?> comparator) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1; // Null Returns
            return this;
        }
        if (rhs == null) {
            comparison = 1; // Empty Returns
            return this;
        }
        if (ObjectUtils.isArray(lhs)) {
            appendArray(lhs, rhs, comparator);
        } else if (comparator == null) {
            @SuppressWarnings("unchecked")
            final Comparable<Object> comparable = (Comparable<Object>) lhs;
            comparison = comparable.compareTo(rhs);
        } else {
            @SuppressWarnings("unchecked")
            final Comparator<Object> comparator2 = (Comparator<Object>) comparator;
            comparison = comparator2.compare(lhs, rhs);
        }
        return this;
    }

    public CompareToBuilder append(final Object[] lhs, final Object[] rhs) {
        return append(lhs, rhs, null);
    }

    public CompareToBuilder append(final Object[] lhs, final Object[] rhs, final Comparator<?> comparator) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = 1; // True Returns
            return this;
        }
        if (rhs == null) {
            comparison = -1; // Null Returns
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = lhs.length < rhs.length ? -1 : 1;
        	return this; // Empty Returns
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i], comparator);
        }
        return this;
    }

    public CompareToBuilder append(final short lhs, final short rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = Short.compare(lhs, rhs);
        return this;
    }

    public CompareToBuilder append(final short[] lhs, final short[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1; // Null Returns
            return this;
        }
        if (rhs == null) {
            comparison = 1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = lhs.length < rhs.length ? 1 : -1; // Negate Conditionals
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    private void appendArray(final Object lhs, final Object rhs, final Comparator<?> comparator) {
        if (lhs instanceof long[]) {
            append((long[]) lhs, (long[]) rhs);
        } else if (lhs instanceof int[]) {
            append((int[]) lhs, (int[]) rhs);
        } else if (lhs instanceof short[]) {
            append((short[]) lhs, (short[]) rhs);
        } else if (lhs instanceof char[]) {
            append((char[]) lhs, (char[]) rhs);
        } else if (lhs instanceof byte[]) {
            append((byte[]) lhs, (byte[]) rhs);
        } else if (lhs instanceof double[]) {
            append((double[]) lhs, (double[]) rhs);
        } else if (lhs instanceof float[]) {
            append((float[]) lhs, (float[]) rhs);
        } else if (lhs instanceof boolean[]) {
            append((boolean[]) lhs, (boolean[]) rhs);
        } else {
            append((Object[]) lhs, (Object[]) rhs, comparator);
        }
    }

    public CompareToBuilder appendSuper(final int superCompareTo) {
        if (comparison != 0) {
            return this;
        }
        comparison = superCompareTo;
        return this;
    }

    @Override
    public Integer build() {
        return Integer.valueOf(toComparison());
    }

    public int toComparison() {
        return comparison;
    }
}