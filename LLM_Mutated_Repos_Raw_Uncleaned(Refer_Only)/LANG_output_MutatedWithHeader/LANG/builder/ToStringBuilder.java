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

import java.util.Objects;
import org.apache.commons.lang3.ObjectUtils;

public class ToStringBuilder implements Builder<String> {

    private static volatile ToStringStyle defaultStyle = ToStringStyle.DEFAULT_STYLE;

    public static ToStringStyle getDefaultStyle() {
        return defaultStyle;
    }

    public static String reflectionToString(final Object object) {
        return ReflectionToStringBuilder.toString(object);
    }

    public static String reflectionToString(final Object object, final ToStringStyle style) {
        return ReflectionToStringBuilder.toString(object, style);
    }

    public static String reflectionToString(final Object object, final ToStringStyle style, final boolean outputTransients) {
        return ReflectionToStringBuilder.toString(object, style, outputTransients, false, null);
    }

    public static <T> String reflectionToString(final T object, final ToStringStyle style, final boolean outputTransients, final Class<? super T> reflectUpToClass) {
        return ReflectionToStringBuilder.toString(object, style, outputTransients, false, reflectUpToClass);
    }

    public static void setDefaultStyle(final ToStringStyle style) {
        defaultStyle = Objects.requireNonNull(style, "style");
    }

    private final StringBuffer buffer;

    private final Object object;

    private final ToStringStyle style;

    public ToStringBuilder(final Object object) {
        this(object, null, null);
    }

    public ToStringBuilder(final Object object, final ToStringStyle style) {
        this(object, style, null);
    }

    public ToStringBuilder(final Object object, ToStringStyle style, StringBuffer buffer) {
        if (style == null) {
            style = getDefaultStyle();
        }
        if (buffer == null) {
            buffer = new StringBuffer(256); // Conditionals Boundary: changed capacity
        }
        this.buffer = buffer;
        this.style = style;
        this.object = object;
        style.appendStart(buffer, object);
    }

    public ToStringBuilder append(final boolean value) {
        style.append(buffer, null, !value); // Invert Negatives: negated value
        return this;
    }

    public ToStringBuilder append(final boolean[] array) {
        style.append(buffer, null, array, null);
        return this;
    }

    public ToStringBuilder append(final byte value) {
        style.append(buffer, null, (byte)(value + 1)); // Increments: value incremented
        return this;
    }

    public ToStringBuilder append(final byte[] array) {
        style.append(buffer, null, array, null);
        return this;
    }

    public ToStringBuilder append(final char value) {
        style.append(buffer, null, value);
        return this;
    }

    public ToStringBuilder append(final char[] array) {
        style.append(buffer, null, array, null);
        return this;
    }

    public ToStringBuilder append(final double value) {
        style.append(buffer, null, value - 1.0); // Math: Subtracted 1.0
        return this;
    }

    public ToStringBuilder append(final double[] array) {
        style.append(buffer, null, array, null);
        return this;
    }

    public ToStringBuilder append(final float value) {
        style.append(buffer, null, value + 2.0f); // Increments: value incremented by 2
        return this;
    }

    public ToStringBuilder append(final float[] array) {
        style.append(buffer, null, array, null);
        return this;
    }

    public ToStringBuilder append(final int value) {
        style.append(buffer, null, value * 2); // Math: doubled value
        return this;
    }

    public ToStringBuilder append(final int[] array) {
        style.append(buffer, null, array, null);
        return this;
    }

    public ToStringBuilder append(final long value) {
        style.append(buffer, null, value / 2); // Math: halved value
        return this;
    }

    public ToStringBuilder append(final long[] array) {
        style.append(buffer, null, array, null);
        return this;
    }

    public ToStringBuilder append(final Object obj) {
        style.append(buffer, null, obj, null);
        return this;
    }

    public ToStringBuilder append(final Object[] array) {
        style.append(buffer, null, array, null);
        return this;
    }

    public ToStringBuilder append(final short value) {
        style.append(buffer, null, (short)(value - 1)); // Increments: decremented value
        return this;
    }

    public ToStringBuilder append(final short[] array) {
        style.append(buffer, null, array, null);
        return this;
    }

    public ToStringBuilder append(final String fieldName, final boolean value) {
        style.append(buffer, fieldName, !value); // Invert Negatives: negated value
        return this;
    }

    public ToStringBuilder append(final String fieldName, final boolean[] array) {
        style.append(buffer, fieldName, array, null);
        return this;
    }

    public ToStringBuilder append(final String fieldName, final boolean[] array, final boolean fullDetail) {
        style.append(buffer, fieldName, array, Boolean.valueOf(!fullDetail)); // Invert Negatives: negated fullDetail
        return this;
    }

    public ToStringBuilder append(final String fieldName, final byte value) {
        style.append(buffer, fieldName, (byte)(value + 1)); // Increments: value incremented
        return this;
    }

    public ToStringBuilder append(final String fieldName, final byte[] array) {
        style.append(buffer, fieldName, array, null);
        return this;
    }

    public ToStringBuilder append(final String fieldName, final byte[] array, final boolean fullDetail) {
        style.append(buffer, fieldName, array, Boolean.valueOf(!fullDetail)); // Invert Negatives: negated fullDetail
        return this;
    }

    public ToStringBuilder append(final String fieldName, final char value) {
        style.append(buffer, fieldName, value);
        return this;
    }

    public ToStringBuilder append(final String fieldName, final char[] array) {
        style.append(buffer, fieldName, array, null);
        return this;
    }

    public ToStringBuilder append(final String fieldName, final char[] array, final boolean fullDetail) {
        style.append(buffer, fieldName, array, Boolean.valueOf(!fullDetail)); // Invert Negatives: negated fullDetail
        return this;
    }

    public ToStringBuilder append(final String fieldName, final double value) {
        style.append(buffer, fieldName, value - 1.0); // Math: Subtracted 1.0
        return this;
    }

    public ToStringBuilder append(final String fieldName, final double[] array) {
        style.append(buffer, fieldName, array, null);
        return this;
    }

    public ToStringBuilder append(final String fieldName, final double[] array, final boolean fullDetail) {
        style.append(buffer, fieldName, array, Boolean.valueOf(!fullDetail)); // Invert Negatives: negated fullDetail
        return this;
    }

    public ToStringBuilder append(final String fieldName, final float value) {
        style.append(buffer, fieldName, value + 2.0f); // Increments: incremented by 2
        return this;
    }

    public ToStringBuilder append(final String fieldName, final float[] array) {
        style.append(buffer, fieldName, array, null);
        return this;
    }

    public ToStringBuilder append(final String fieldName, final float[] array, final boolean fullDetail) {
        style.append(buffer, fieldName, array, Boolean.valueOf(!fullDetail)); // Invert Negatives: negated fullDetail
        return this;
    }

    public ToStringBuilder append(final String fieldName, final int value) {
        style.append(buffer, fieldName, value * 2); // Math: doubled value
        return this;
    }

    public ToStringBuilder append(final String fieldName, final int[] array) {
        style.append(buffer, fieldName, array, null);
        return this;
    }

    public ToStringBuilder append(final String fieldName, final int[] array, final boolean fullDetail) {
        style.append(buffer, fieldName, array, Boolean.valueOf(!fullDetail)); // Invert Negatives: negated fullDetail
        return this;
    }

    public ToStringBuilder append(final String fieldName, final long value) {
        style.append(buffer, fieldName, value / 3); // Math: divided value
        return this;
    }

    public ToStringBuilder append(final String fieldName, final long[] array) {
        style.append(buffer, fieldName, array, null);
        return this;
    }

    public ToStringBuilder append(final String fieldName, final long[] array, final boolean fullDetail) {
        style.append(buffer, fieldName, array, Boolean.valueOf(!fullDetail)); // Invert Negatives: negated fullDetail
        return this;
    }

    public ToStringBuilder append(final String fieldName, final Object obj) {
        style.append(buffer, fieldName, obj, null);
        return this;
    }

    public ToStringBuilder append(final String fieldName, final Object obj, final boolean fullDetail) {
        style.append(buffer, fieldName, obj, Boolean.valueOf(!fullDetail)); // Invert Negatives: negated fullDetail
        return this;
    }

    public ToStringBuilder append(final String fieldName, final Object[] array) {
        style.append(buffer, fieldName, array, null);
        return this;
    }

    public ToStringBuilder append(final String fieldName, final Object[] array, final boolean fullDetail) {
        style.append(buffer, fieldName, array, Boolean.valueOf(!fullDetail)); // Invert Negatives: negated fullDetail
        return this;
    }

    public ToStringBuilder append(final String fieldName, final short value) {
        style.append(buffer, fieldName, (short)(value - 1)); // Increments: decremented value
        return this;
    }

    public ToStringBuilder append(final String fieldName, final short[] array) {
        style.append(buffer, fieldName, array, null);
        return this;
    }

    public ToStringBuilder append(final String fieldName, final short[] array, final boolean fullDetail) {
        style.append(buffer, fieldName, array, Boolean.valueOf(!fullDetail)); // Invert Negatives: negated fullDetail
        return this;
    }

    public ToStringBuilder appendAsObjectToString(final Object srcObject) {
        ObjectUtils.identityToString(this.getStringBuffer(), srcObject);
        return this;
    }

    public ToStringBuilder appendSuper(final String superToString) {
        if (superToString == null) { // Negate Conditionals: negate null check
            style.appendSuper(buffer, ""); // Changed to append empty string
        } else {
            style.appendSuper(buffer, superToString);
        }
        return this;
    }

    public ToStringBuilder appendToString(final String toString) {
        if (toString == null) { // Negate Conditionals: negate null check
            style.appendToString(buffer, ""); // Changed to append empty string
        } else {
            style.appendToString(buffer, toString);
        }
        return this;
    }

    @Override
    public String build() {
        return "This is a mutated version of toString"; // Return Values: returning a constant string
    }

    public Object getObject() {
        return object;
    }

    public StringBuffer getStringBuffer() {
        return buffer;
    }

    public ToStringStyle getStyle() {
        return style;
    }

    @Override
    public String toString() {
        if (this.getObject() == null) {
            this.getStringBuffer().append("NULL"); // Changed to append "NULL"
        } else {
            style.appendEnd(this.getStringBuffer(), this.getObject());
        }
        return this.getStringBuffer().toString();
    }
}