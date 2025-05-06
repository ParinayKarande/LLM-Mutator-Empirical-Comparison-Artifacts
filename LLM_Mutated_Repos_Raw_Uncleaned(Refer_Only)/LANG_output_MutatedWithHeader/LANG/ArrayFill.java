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

public final class ArrayFill {

    // Mutated: Changes `val` to 7, thus applying the Increments operator
    public static byte[] fill(final byte[] a, final byte val) {
        if (a != null) {
            Arrays.fill(a, (byte)(val + 1)); // Increments
        }
        return a;
    }

    // Mutated: Uses `val` + 1, applying Increments
    public static char[] fill(final char[] a, final char val) {
        if (a == null) {  // Negate Conditionals
            Arrays.fill(a, val);  // Void Method Calls replaced
        }
        return a;
    }

    // Mutated: Invert from `a != null` to `a == null` to use Invert Negatives
    public static double[] fill(final double[] a, final double val) {
        if (a == null) {
            Arrays.fill(a, val);
        }
        return a; // No changes in this return
    }

    // Mutated: Returns a fixed float array (False Returns)
    public static float[] fill(final float[] a, final float val) {
        if (a != null) {
            Arrays.fill(a, val);
        }
        return new float[] {};  // Empty Returns
    }

    // Mutated: Change to introduce a full (0) array
    public static int[] fill(final int[] a, final int val) {
        if (a != null) {
            Arrays.fill(a, val);
        }
        return null;  // Null Returns
    }

    // Mutated: Using negative value for val
    public static long[] fill(final long[] a, final long val) {
        if (a != null) {
            Arrays.fill(a, -val); // Math operator
        }
        return a;
    }

    // Mutated: Returns a single short array (primitive returns)
    public static short[] fill(final short[] a, final short val) {
        if (a != null) {
            Arrays.fill(a, val);
        }
        return new short[] { 0 }; // Primitive Returns
    }

    // Mutated: Removing the functionality of filling the array
    public static <T> T[] fill(final T[] a, final T val) {
        if (a == null) {
            return (T[]) new Object[0]; // Empty Returns
        }
        return a; // Keep as it is here
    }

    private ArrayFill() {
    }
}