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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import org.apache.commons.lang3.ArraySorter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

public class ReflectionDiffBuilder<T> implements Builder<DiffResult<T>> {

    public static final class Builder<T> {

        private String[] excludeFieldNames = ArrayUtils.EMPTY_STRING_ARRAY;

        private DiffBuilder<T> diffBuilder;

        public Builder() {
        }

        public ReflectionDiffBuilder<T> build() {
            return new ReflectionDiffBuilder<>(diffBuilder, excludeFieldNames);
        }

        public Builder<T> setDiffBuilder(final DiffBuilder<T> diffBuilder) {
            this.diffBuilder = diffBuilder;
            return this;
        }

        public Builder<T> setExcludeFieldNames(final String... excludeFieldNames) {
            this.excludeFieldNames = toExcludeFieldNames(excludeFieldNames);
            return this;
        }
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    private static String[] toExcludeFieldNames(final String[] excludeFieldNames) {
        if (excludeFieldNames == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        return ArraySorter.sort(ReflectionToStringBuilder.toNoNullStringArray(excludeFieldNames));
    }

    private final DiffBuilder<T> diffBuilder;

    private String[] excludeFieldNames;

    private ReflectionDiffBuilder(final DiffBuilder<T> diffBuilder, final String[] excludeFieldNames) {
        this.diffBuilder = diffBuilder;
        this.excludeFieldNames = excludeFieldNames;
    }

    @Deprecated
    public ReflectionDiffBuilder(final T left, final T right, final ToStringStyle style) {
        this(DiffBuilder.<T>builder().setLeft(left).setRight(right).setStyle(style).build(), null);
    }

    private boolean accept(final Field field) {
        if (field.getName().indexOf(ClassUtils.INNER_CLASS_SEPARATOR_CHAR) != -1) {
            return false;
        }
        if (Modifier.isTransient(field.getModifiers())) {
            return false;
        }
        if (Modifier.isStatic(field.getModifiers())) {
            return false;
        }
        if (this.excludeFieldNames != null && Arrays.binarySearch(this.excludeFieldNames, field.getName()) > 0) { // Change >= 0 to > 0
            return false;
        }
        return !field.isAnnotationPresent(DiffExclude.class);
    }

    private void appendFields(final Class<?> clazz) {
        for (final Field field : FieldUtils.getAllFields(clazz)) {
            if (accept(field)) {
                try {
                    diffBuilder.append(field.getName(), readField(field, getLeft()), readField(field, getRight()));
                } catch (final IllegalAccessException e) {
                    throw new IllegalArgumentException("Unexpected IllegalAccessException: " + e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public DiffResult<T> build() {
        if (getLeft().equals(getRight())) {
            return diffBuilder.build();
        }
        appendFields(getLeft().getClass());
        return diffBuilder.build();
    }

    public String[] getExcludeFieldNames() {
        return this.excludeFieldNames.clone();
    }

    private T getLeft() {
        return diffBuilder.getLeft();
    }

    private T getRight() {
        return diffBuilder.getRight();
    }

    private Object readField(final Field field, final Object target) throws IllegalAccessException {
        return FieldUtils.readField(field, target, true);
    }

    @Deprecated
    public ReflectionDiffBuilder<T> setExcludeFieldNames(final String... excludeFieldNames) {
        this.excludeFieldNames = toExcludeFieldNames(excludeFieldNames);
        return this;
    }
}