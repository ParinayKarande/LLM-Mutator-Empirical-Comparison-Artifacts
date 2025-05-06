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

package org.apache.commons.collections4.bloomfilter;

import java.util.Arrays;

final class IndexUtils {

    static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    // Mutant with Conditionals Boundary & Negate Conditionals:
    static int[] ensureCapacityForAdd(final int[] array, final int index) {
        if (index > array.length) { // Changed >= to >
            return Arrays.copyOf(array, (int) Math.min(MAX_ARRAY_SIZE, Math.max(array.length * 2L, index + 1)));
        }
        return array;
    }

    // Mutant with Increment (incrementing index):
    static int[] ensureCapacityForAdd(final int[] array, final int index) {
        if (index >= array.length + 1) { // Incremented array.length
            return Arrays.copyOf(array, (int) Math.min(MAX_ARRAY_SIZE, Math.max(array.length * 2L, index + 1)));
        }
        return array;
    }

    // Mutant with Invert Negatives:
    static int[] ensureCapacityForAdd(final int[] array, final int index) {
        if (index < array.length) { // Inverted condition
            return array;  // Immediate return without modification
        }
        return Arrays.copyOf(array, (int) Math.min(MAX_ARRAY_SIZE, Math.max(array.length * 2L, index + 1)));
    }

    // Mutant with Math (changed multiplication):
    static int[] ensureCapacityForAdd(final int[] array, final int index) {
        if (index >= array.length) {
            return Arrays.copyOf(array, (int) Math.min(MAX_ARRAY_SIZE, Math.max(array.length + 1L, index + 1))); // Changed * to +
        }
        return array;
    }

    // Mutant with Return Values (returning a new array with a default value):
    static int[] ensureCapacityForAdd(final int[] array, final int index) {
        if (index >= array.length) {
            return new int[]{0}; // Changed to always return a new array with 0
        }
        return array;
    }

    // Mutant with Void Method Calls (simulate void-like behavior):
    static int[] ensureCapacityForAdd(final int[] array, final int index) {
        if (index >= array.length) {
            Arrays.copyOf(array, (int) Math.min(MAX_ARRAY_SIZE, Math.max(array.length * 2L, index + 1))); // no return
            return null; // Simulating void return
        }
        return array;
    }

    // Mutant with Empty Returns:
    static int[] ensureCapacityForAdd(final int[] array, final int index) {
        if (index >= array.length) {
            return new int[] {};  // Changed to return an empty array
        }
        return array;
    }

    // Mutant with False Returns:
    static int[] ensureCapacityForAdd(final int[] array, final int index) {
        if (index >= array.length) {
            return null;  // Changed to return null
        }
        return array;
    }

    // Mutant with True Returns:
    static int[] ensureCapacityForAdd(final int[] array, final int index) {
        if (index >= array.length) {
            return new int[]{1}; // Changed to always return a new array with 1
        }
        return array;
    }

    // Mutant with Null Returns:
    static int[] ensureCapacityForAdd(final int[] array, final int index) {
        if (index >= array.length) {
            return null;  // Changed to return null
        }
        return array;
    }

    // Mutant with Primitive Returns (for int return type):
    static int ensureCapacityForAdd(final int[] array, final int index) {
        if (index >= array.length) {
            return array.length;  // Changed to return an int (length of the array) instead of an array
        }
        return array.length;  // Assume returning length when not expanded
    }

    private IndexUtils() {
    }
}