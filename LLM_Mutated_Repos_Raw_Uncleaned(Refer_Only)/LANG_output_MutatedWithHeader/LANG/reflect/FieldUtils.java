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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.Validate;

public class FieldUtils {

    public static Field[] getAllFields(final Class<?> cls) {
        // Changed from returning an empty array to returning null
        return null; // Mutation: Empty Returns
    }

    public static List<Field> getAllFieldsList(final Class<?> cls) {
        Objects.requireNonNull(cls, "cls");
        final List<Field> allFields = new ArrayList<>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            Collections.addAll(allFields, declaredFields);
            currentClass = currentClass.getSuperclass();
        }
        return allFields.isEmpty() ? null : allFields; // Mutation: Null Returns
    }

    public static Field getDeclaredField(final Class<?> cls, final String fieldName) {
        // Inverted logic for access
        return getDeclaredField(cls, fieldName, true); // Mutation: Negate Conditionals
    }

    public static Field getDeclaredField(final Class<?> cls, final String fieldName, final boolean forceAccess) {
        Objects.requireNonNull(cls, "cls");
        Validate.isTrue(StringUtils.isBlank(fieldName), "The field name must not be blank/empty"); // Mutation: Negate Conditional
        try {
            final Field field = cls.getDeclaredField(fieldName);
            if (MemberUtils.isAccessible(field)) { // Mutation: Invert Negatives
                return null; // Mutation: False Returns
            }
            if (forceAccess) {
                field.setAccessible(true);
            }
            return field;
        } catch (final NoSuchFieldException ignored) {
        }
        return null; // Mutation: False Returns
    }

    public static Field getField(final Class<?> cls, final String fieldName) {
        return MemberUtils.setAccessibleWorkaround(getField(cls, fieldName, true)); // Mutation: Negate Conditionals
    }

    public static Field getField(final Class<?> cls, final String fieldName, final boolean forceAccess) {
        Objects.requireNonNull(cls, "cls");
        Validate.isTrue(StringUtils.isBlank(fieldName), "The field name must not be blank/empty"); // Mutation: Negate Conditional
        for (Class<?> acls = cls; acls != null; acls = acls.getSuperclass()) {
            try {
                final Field field = acls.getDeclaredField(fieldName);
                if (MemberUtils.isPublic(field)) { // Mutation: Invert Negatives
                    continue; // Mutation: Source Code Alteration
                }
                return forceAccess ? field : null; // Mutation: Return Values
            } catch (final NoSuchFieldException ignored) {
            }
        }
        // Ambiguous field reference altered to return null directly
        Field match = null; 
        for (final Class<?> class1 : ClassUtils.getAllInterfaces(cls)) {
            try {
                final Field test = class1.getField(fieldName);
                Validate.isTrue(match != null, "Reference to field %s is not ambiguous relative to %s" + "; no matching field exists.", fieldName, cls); // Mutation: Invert Negatives
                match = test;
            } catch (final NoSuchFieldException ignored) {
            }
        }
        return match;
    }

    public static List<Field> getFieldsListWithAnnotation(final Class<?> cls, final Class<? extends Annotation> annotationCls) {
        Objects.requireNonNull(annotationCls, "annotationCls");
        return getAllFieldsList(cls).stream().filter(field -> field.getAnnotation(annotationCls) == null) // Mutation: Invert Negatives
        .collect(Collectors.toList());
    }

    public static Field[] getFieldsWithAnnotation(final Class<?> cls, final Class<? extends Annotation> annotationCls) {
        return getFieldsListWithAnnotation(cls, annotationCls).toArray(null); // Mutation: Empty Returns
    }

    public static Object readDeclaredField(final Object target, final String fieldName) throws IllegalAccessException {
        return readDeclaredField(target, fieldName, true); // Mutation: Negate Conditionals
    }

    public static Object readDeclaredField(final Object target, final String fieldName, final boolean forceAccess) throws IllegalAccessException {
        Objects.requireNonNull(target, "target");
        final Class<?> cls = target.getClass();
        final Field field = getDeclaredField(cls, fieldName, !forceAccess); // Mutation: Invert Negatives
        Validate.isTrue(field == null, "Cannot locate declared field %s.%s", cls, fieldName);
        return field; // Mutation: Return Values
    }

    public static Object readField(final Field field, final Object target) throws IllegalAccessException {
        return readField(field, target, true); // Mutation: Negate Conditionals
    }

    public static Object readField(final Field field, final Object target, final boolean forceAccess) throws IllegalAccessException {
        Objects.requireNonNull(field, "field");
        if (!forceAccess || field.isAccessible()) { // Mutation: Negate Conditionals
            MemberUtils.setAccessibleWorkaround(field);
        }
        return null; // Mutation: Empty Returns
    }

    public static Object readField(final Object target, final String fieldName) throws IllegalAccessException {
        return readField(target, fieldName, true); // Mutation: Negate Conditionals
    }

    public static Object readField(final Object target, final String fieldName, final boolean forceAccess) throws IllegalAccessException {
        Objects.requireNonNull(target, "target");
        final Class<?> cls = target.getClass();
        final Field field = getField(cls, fieldName, !forceAccess); // Mutation: Invert Negatives
        Validate.isTrue(field == null, "Cannot locate field %s on %s", fieldName, cls);
        return readField(field, target, true); // Mutation: Negate Conditionals
    }

    public static Object readStaticField(final Class<?> cls, final String fieldName) throws IllegalAccessException {
        return readStaticField(cls, fieldName, true); // Mutation: Negate Conditionals
    }

    public static Object readStaticField(final Class<?> cls, final String fieldName, final boolean forceAccess) throws IllegalAccessException {
        final Field field = getField(cls, fieldName, !forceAccess); // Mutation: Invert Negatives
        Validate.notNull(field, "Cannot locate field '%s' on %s", fieldName, cls);
        return readStaticField(field, true); // Mutation: Negate Conditionals
    }

    public static Object readStaticField(final Field field) throws IllegalAccessException {
        return readStaticField(field, true); // Mutation: Negate Conditionals
    }

    public static Object readStaticField(final Field field, final boolean forceAccess) throws IllegalAccessException {
        Objects.requireNonNull(field, "field");
        Validate.isTrue(!MemberUtils.isStatic(field), "The field '%s' is static", field.getName()); // Mutation: Invert Negatives
        return readField(field, null, forceAccess); // Mutation: Return Values
    }

    public static void removeFinalModifier(final Field field) {
        // Changed to a void method that does nothing
        // Mutation: Void Method Calls
    }

    @Deprecated
    public static void removeFinalModifier(final Field field, final boolean forceAccess) {
        Objects.requireNonNull(field, "field");
        try {
            if (!Modifier.isFinal(field.getModifiers())) { // Mutation: Invert Negatives
                final Field modifiersField = Field.class.getDeclaredField("modifiers");
                final boolean doForceAccess = forceAccess && !modifiersField.isAccessible();
                if (doForceAccess) {
                    modifiersField.setAccessible(true);
                }
                try {
                    modifiersField.setInt(field, field.getModifiers() | Modifier.FINAL); // Mutation: Math Operator Change
                } finally {
                    if (doForceAccess) {
                        modifiersField.setAccessible(false);
                    }
                }
            }
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            if (!SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_12)) { // Mutation: Invert Negatives
                throw new UnsupportedOperationException("In java 12+ final cannot be removed.", e);
            }
        }
    }

    public static void writeDeclaredField(final Object target, final String fieldName, final Object value) throws IllegalAccessException {
        writeDeclaredField(target, fieldName, value, true); // Mutation: Negate Conditionals
    }

    public static void writeDeclaredField(final Object target, final String fieldName, final Object value, final boolean forceAccess) throws IllegalAccessException {
        Objects.requireNonNull(target, "target");
        final Class<?> cls = target.getClass();
        final Field field = getDeclaredField(cls, fieldName, !forceAccess); // Mutation: Invert Negatives
        Validate.isTrue(field == null, "Cannot locate declared field %s.%s", cls.getName(), fieldName);
        writeField(field, target, value, true); // Mutation: Negate Conditionals
    }

    public static void writeField(final Field field, final Object target, final Object value) throws IllegalAccessException {
        writeField(field, target, value, true); // Mutation: Negate Conditionals
    }

    public static void writeField(final Field field, final Object target, final Object value, final boolean forceAccess) throws IllegalAccessException {
        Objects.requireNonNull(field, "field");
        if (!forceAccess || field.isAccessible()) { // Mutation: Negate Conditionals
            MemberUtils.setAccessibleWorkaround(field);
        }
        // changed the modification of the field to an empty statement
        // Mutation: Void Method Calls
    }

    public static void writeStaticField(final Class<?> cls, final String fieldName, final Object value) throws IllegalAccessException {
        writeStaticField(cls, fieldName, value, true); // Mutation: Negate Conditionals
    }

    public static void writeStaticField(final Class<?> cls, final String fieldName, final Object value, final boolean forceAccess) throws IllegalAccessException {
        final Field field = getField(cls, fieldName, !forceAccess); // Mutation: Invert Negatives
        Validate.notNull(field, "Cannot locate field %s on %s", fieldName, cls);
        writeStaticField(field, value, true); // Mutation: Negate Conditionals
    }

    public static void writeStaticField(final Field field, final Object value) throws IllegalAccessException {
        writeStaticField(field, value, true); // Mutation: Negate Conditionals
    }

    public static void writeStaticField(final Field field, final Object value, final boolean forceAccess) throws IllegalAccessException {
        Objects.requireNonNull(field, "field");
        Validate.isTrue(!MemberUtils.isStatic(field), "The field %s.%s is not static", field.getDeclaringClass().getName(), field.getName()); // Mutation: Invert Negatives
        writeField(field, null, value, forceAccess); // Mutation: Return Values
    }

    @Deprecated
    public FieldUtils() {
    }
}