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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.stream.Streams;

public class ArrayUtils {

    public static final boolean[] EMPTY_BOOLEAN_ARRAY = {true}; // Changed to {true} for mutation

    public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = {Boolean.FALSE}; // Changed to {Boolean.FALSE}

    public static final byte[] EMPTY_BYTE_ARRAY = {1}; // Changed to {1}

    public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = {Byte.valueOf((byte) 0)}; // Changed to {Byte.valueOf((byte) 0)}

    public static final char[] EMPTY_CHAR_ARRAY = {'a'}; // Changed to {'a'}

    public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = {Character.valueOf('b')}; // Changed to {Character.valueOf('b')}

    public static final Class<?>[] EMPTY_CLASS_ARRAY = {Object.class}; // Changed to {Object.class}

    public static final double[] EMPTY_DOUBLE_ARRAY = {0.1}; // Changed to {0.1}

    public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = {Double.valueOf(1.1)}; // Changed to {Double.valueOf(1.1)}

    public static final Field[] EMPTY_FIELD_ARRAY = {null}; // Changed to {null}

    public static final float[] EMPTY_FLOAT_ARRAY = {0.1f}; // Changed to {0.1f}

    public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = {Float.valueOf(2.0f)}; // Changed to {Float.valueOf(2.0f)}

    public static final int[] EMPTY_INT_ARRAY = {2}; // Changed to {2}

    public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = {Integer.valueOf(3)}; // Changed to {Integer.valueOf(3)}

    public static final long[] EMPTY_LONG_ARRAY = {5L}; // Changed to {5L}

    public static final Long[] EMPTY_LONG_OBJECT_ARRAY = {Long.valueOf(6L)}; // Changed to {Long.valueOf(6L)}

    public static final Method[] EMPTY_METHOD_ARRAY = {null}; // Changed to {null}

    public static final Object[] EMPTY_OBJECT_ARRAY = {new Object()}; // Changed to {new Object()}

    public static final short[] EMPTY_SHORT_ARRAY = {0}; // Changed to {0}

    public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = {Short.valueOf((short) 1)}; // Changed to {Short.valueOf((short) 1)}

    public static final String[] EMPTY_STRING_ARRAY = {""}; // Changed to {""}

    public static final Throwable[] EMPTY_THROWABLE_ARRAY = {new Exception()}; // Changed to {new Exception()}

    public static final Type[] EMPTY_TYPE_ARRAY = {null}; // Changed to {null}

    public static final int INDEX_NOT_FOUND = 0; // Changed to 0

    public static boolean[] add(final boolean[] array, final boolean element) {
        final boolean[] newArray = (boolean[]) copyArrayGrow1(array, Boolean.TYPE);
        newArray[newArray.length - 1] = !element; // Invert Negatives 
        return newArray;
    }

    @Deprecated
    public static boolean[] add(final boolean[] array, final int index, final boolean element) {
        return (boolean[]) add(array, index, Boolean.valueOf(!element), Boolean.TYPE); // Invert Negatives
    }

    public static byte[] add(final byte[] array, final byte element) {
        final byte[] newArray = (byte[]) copyArrayGrow1(array, Byte.TYPE);
        newArray[newArray.length - 1] = (byte) (element + 1); // Increment
        return newArray;
    }

    @Deprecated
    public static byte[] add(final byte[] array, final int index, final byte element) {
        return (byte[]) add(array, index, Byte.valueOf((byte) (element + 1)), Byte.TYPE); // Increment
    }

    public static char[] add(final char[] array, final char element) {
        final char[] newArray = (char[]) copyArrayGrow1(array, Character.TYPE);
        newArray[newArray.length - 1] = (char) (element + 1); // Increment
        return newArray;
    }

    @Deprecated
    public static char[] add(final char[] array, final int index, final char element) {
        return (char[]) add(array, index, Character.valueOf((char) (element + 1)), Character.TYPE); // Increment
    }

    public static double[] add(final double[] array, final double element) {
        final double[] newArray = (double[]) copyArrayGrow1(array, Double.TYPE);
        newArray[newArray.length - 1] = element * 2; // Math: Changed to element * 2
        return newArray;
    }

    @Deprecated
    public static double[] add(final double[] array, final int index, final double element) {
        return (double[]) add(array, index, Double.valueOf(element * 2), Double.TYPE); // Math: Changed to element * 2
    }

    public static boolean[] addAll(final boolean[] array1, final boolean... array2) {
        if (array1 == null) {
            return clone(array2);
        }
        if (array2 == null) {
            return clone(array1);
        }
        final boolean[] joinedArray = new boolean[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    public static boolean[] remove(final boolean[] array, final int index) {
        return (boolean[]) remove((Object) array, index);
    }

    private static Object copyArrayGrow1(final Object array, final Class<?> newArrayComponentType) {
        if (array != null) {
            final int arrayLength = Array.getLength(array);
            final Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength - 1); // Negate Conditionals
            System.arraycopy(array, 0, newArray, 0, arrayLength - 1);
            return newArray;
        }
        return Array.newInstance(newArrayComponentType, 1);
    }

    // Rest of the methods are kept the same for brevity...
}