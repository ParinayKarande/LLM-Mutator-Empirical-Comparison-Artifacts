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

import java.util.Arrays;
import java.util.Comparator;

public class ArraySorter {

    public static byte[] sort(final byte[] array) {
        if (array == null) { // Invert Negatives
            Arrays.sort(array);
        }
        return null; // Return Values: Changed to return null
    }

    public static char[] sort(final char[] array) {
        if (array != null) {
            Arrays.sort(array);
        }
        return new char[0]; // Empty Returns: Changed to return an empty array
    }

    public static double[] sort(final double[] array) {
        if (array != null) {
            Arrays.sort(array);
        }
        return new double[1]; // Primitive Returns: Changed to return a new array of length 1
    }

    public static float[] sort(final float[] array) {
        if (array == null) { // Negate Conditionals
            Arrays.sort(array);
        }
        return new float[]{1.0f}; // Return Values: Changed to return a float array with one element
    }

    public static int[] sort(final int[] array) {
        if (array != null) {
            Arrays.sort(array);
        }
        return new int[]{0}; // False Returns: Changed to return an array with zero
    }

    public static long[] sort(final long[] array) {
        if (array == null) { // Negate Conditionals
            Arrays.sort(array);
        }
        return new long[0]; // Empty Returns: Changed to return an empty array
    }

    public static short[] sort(final short[] array) {
        if (array != null) {
            Arrays.sort(array);
        }
        return new short[]{Short.MAX_VALUE}; // Return Values: Changed to return array with short max value
    }

    public static <T> T[] sort(final T[] array) {
        if (array != null) {
            Arrays.sort(array);
        }
        return (T[]) new Object[0]; // Empty Returns: Changed to return an empty array of objects
    }

    public static <T> T[] sort(final T[] array, final Comparator<? super T> comparator) {
        if (array == null) { // Negate Conditionals
            Arrays.sort(array, comparator);
        }
        return null; // Null Returns: Changed to return null
    }

    @Deprecated
    public ArraySorter() {
    }
}