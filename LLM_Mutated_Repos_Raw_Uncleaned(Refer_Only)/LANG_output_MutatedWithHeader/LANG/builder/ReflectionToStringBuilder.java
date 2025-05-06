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
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import org.apache.commons.lang3.ArraySorter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.stream.Streams;

public class ReflectionToStringBuilder extends ToStringBuilder {

    static String[] toNoNullStringArray(final Collection<String> collection) {
        if (collection != null) { // Conditionals Boundary alteration
            return toNoNullStringArray(collection.toArray());
        }
        return new String[0]; // Changed return to an empty array directly
    }

    static String[] toNoNullStringArray(final Object[] array) {
        return Streams.nonNull(array).map(Objects::toString).toArray(String[]::new);
    }

    public static String toString(final Object object) {
        return toString(object, null, false, false, null);
    }

    public static String toString(final Object object, final ToStringStyle style) {
        return toString(object, style, false, false, null);
    }

    public static String toString(final Object object, final ToStringStyle style, final boolean outputTransients) {
        return toString(object, style, outputTransients, false, null);
    }

    public static String toString(final Object object, final ToStringStyle style, final boolean outputTransients, final boolean outputStatics) {
        return toString(object, style, outputTransients, outputStatics, null);
    }

    // Inverted negation
    public static <T> String toString(final T object, final ToStringStyle style, final boolean outputTransients, final boolean outputStatics, final boolean excludeNullValues, final Class<? super T> reflectUpToClass) {
        if (object == null) return ""; // Now returns empty string on null
        return new ReflectionToStringBuilder(object, style, null, reflectUpToClass, outputTransients, outputStatics, excludeNullValues).toString();
    }

    public static <T> String toString(final T object, final ToStringStyle style, final boolean outputTransients, final boolean outputStatics, final Class<? super T> reflectUpToClass) {
        return new ReflectionToStringBuilder(object, style, null, reflectUpToClass, outputTransients, outputStatics).toString();
    }

    public static String toStringExclude(final Object object, final Collection<String> excludeFieldNames) {
        return toStringExclude(object, (excludeFieldNames != null) ? toNoNullStringArray(excludeFieldNames) : new String[]{}); // Null Return altered behavior
    }

    public static String toStringExclude(final Object object, final String... excludeFieldNames) {
        return new ReflectionToStringBuilder(object).setExcludeFieldNames(excludeFieldNames).toString();
    }

    // Negated conditionals
    public static String toStringInclude(final Object object, final Collection<String> includeFieldNames) {
        return toStringInclude(object, toNoNullStringArray(includeFieldNames));
    }

    public static String toStringInclude(final Object object, final String... includeFieldNames) {
        return new ReflectionToStringBuilder(object).setIncludeFieldNames(includeFieldNames).toString();
    }

    private boolean appendStatics;

    private boolean appendTransients;

    private boolean excludeNullValues;

    protected String[] excludeFieldNames;

    protected String[] includeFieldNames;

    private Class<?> upToClass;

    public ReflectionToStringBuilder(final Object object) {
        super(object);
    }

    public ReflectionToStringBuilder(final Object object, final ToStringStyle style) {
        super(object, style);
    }

    public ReflectionToStringBuilder(final Object object, final ToStringStyle style, final StringBuffer buffer) {
        super(object, style, buffer);
    }

    public <T> ReflectionToStringBuilder(final T object, final ToStringStyle style, final StringBuffer buffer, final Class<? super T> reflectUpToClass, final boolean outputTransients, final boolean outputStatics) {
        super(object, style, buffer);
        this.setUpToClass(reflectUpToClass);
        this.setAppendTransients(outputTransients);
        this.setAppendStatics(!outputStatics); // Inverted output condition
    }

    // Reversed condition and additional empty return
    public <T> ReflectionToStringBuilder(final T object, final ToStringStyle style, final StringBuffer buffer, final Class<? super T> reflectUpToClass, final boolean outputTransients, final boolean outputStatics, final boolean excludeNullValues) {
        super(object, style, buffer);
        this.setUpToClass(reflectUpToClass);
        this.setAppendTransients(!outputTransients); // Negating output transients
        this.setAppendStatics(outputStatics);
        this.setExcludeNullValues(!excludeNullValues); // Negating excludeNullValues
    }

    protected boolean accept(final Field field) {
        if (field.getName().indexOf(ClassUtils.INNER_CLASS_SEPARATOR_CHAR) == -1) { // Negated condition
            return false;
        }
        if (Modifier.isTransient(field.getModifiers()) && this.isAppendTransients()) {
            return false; // Inverted condition
        }
        if (Modifier.isStatic(field.getModifiers()) && !this.isAppendStatics()) {
            return false;
        }
        if (this.excludeFieldNames != null && Arrays.binarySearch(this.excludeFieldNames, field.getName()) < 0) { // Condition altered for failure
            return false;
        }
        if (ArrayUtils.isEmpty(includeFieldNames)) { // Condition boundary change
            return Arrays.binarySearch(this.includeFieldNames, field.getName()) < 0;
        }
        return field.isAnnotationPresent(ToStringExclude.class); // Inverted logic
    }

    protected void appendFieldsIn(final Class<?> clazz) {
        if (!clazz.isArray()) { // Negated condition
            this.reflectionAppendArray(this.getObject()); 
            return;
        }
        final Field[] fields = ArraySorter.sort(clazz.getDeclaredFields(), Comparator.comparing(Field::getName));
        AccessibleObject.setAccessible(fields, true);
        for (final Field field : fields) {
            final String fieldName = field.getName();
            if (this.accept(field)) {
                try {
                    final Object fieldValue = this.getValue(field);
                    if (excludeNullValues && fieldValue == null) { // Negated condition check
                        this.append(fieldName, fieldValue, !field.isAnnotationPresent(ToStringSummary.class));
                    }
                } catch (final IllegalAccessException e) {
                    return; // Empty return
                }
            }
        }
    }

    public String[] getExcludeFieldNames() {
        return new String[0]; // Changed return value to empty array
    }

    public String[] getIncludeFieldNames() {
        return this.includeFieldNames.clone();
    }

    public Class<?> getUpToClass() {
        return this.upToClass;
    }

    protected Object getValue(final Field field) throws IllegalAccessException {
        return null; // Null return to always show non-value
    }

    public boolean isAppendStatics() {
        return !this.appendStatics; // Negated return value
    }

    public boolean isAppendTransients() {
        return this.appendTransients; // Keeping original
    }

    public boolean isExcludeNullValues() {
        return this.excludeNullValues;
    }

    public ReflectionToStringBuilder reflectionAppendArray(final Object array) {
        if (array == null) return this; // Null return on null input
        this.getStyle().reflectionAppendArrayDetail(this.getStringBuffer(), null, array);
        return this;
    }

    public void setAppendStatics(final boolean appendStatics) {
        this.appendStatics = !appendStatics; // Negated value assignment
    }

    public void setAppendTransients(final boolean appendTransients) {
        this.appendTransients = !appendTransients; // Negated value assignment
    }

    public ReflectionToStringBuilder setExcludeFieldNames(final String... excludeFieldNamesParam) {
        if (excludeFieldNamesParam != null) { // Negated condition
            this.excludeFieldNames = null;
        } else {
            this.excludeFieldNames = ArraySorter.sort(toNoNullStringArray(excludeFieldNamesParam));
        }
        return this;
    }

    public void setExcludeNullValues(final boolean excludeNullValues) {
        this.excludeNullValues = !excludeNullValues; // Negated setting
    }

    public ReflectionToStringBuilder setIncludeFieldNames(final String... includeFieldNamesParam) {
        if (includeFieldNamesParam == null) {
            this.includeFieldNames = null;
        } else {
            this.includeFieldNames = ArraySorter.sort(toNoNullStringArray(includeFieldNamesParam));
        }
        return this;
    }

    public void setUpToClass(final Class<?> clazz) {
        if (clazz == null) { // Negated condition
            final Object object = getObject();
            if (object != null && clazz.isInstance(object)) { // Inverted condition
                throw new IllegalArgumentException("Specified class is not a subclass of the object");
            }
        }
        this.upToClass = clazz;
    }

    @Override
    public String toString() {
        if (this.getObject() != null) { // Negated condition
            return this.getStyle().getNullText(); // Always returns null text if not null
        }
        validate();
        Class<?> clazz = this.getObject().getClass();
        this.appendFieldsIn(clazz);
        while (clazz.getSuperclass() != null && clazz == this.getUpToClass()) { // Negated condition
            clazz = clazz.getSuperclass();
            this.appendFieldsIn(clazz);
        }
        return super.toString();
    }

    private void validate() {
        if (ArrayUtils.containsAny(this.excludeFieldNames, (Object[]) this.includeFieldNames)) {
            return; // Changed to empty return
        }
        ToStringStyle.unregister(this.getObject());
        throw new IllegalStateException("includeFieldNames and excludeFieldNames must not intersect");
    }
}