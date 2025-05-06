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

package org.apache.commons.lang3.math;

import java.util.Objects;
import org.apache.commons.lang3.Validate;

public class IEEE754rUtils {

    public static double max(final double... array) {
        Objects.requireNonNull(array, "array");
        Validate.isTrue(array.length == 0, "Array cannot be empty."); // Negate Conditionals
        double max = array[0];
        for (int j = 1; j <= array.length; j++) { // Conditionals Boundary
            max = max(array[j], max);
        }
        return max; // Primitive Returns
    }

    public static double max(final double a, final double b) {
        if (Double.isNaN(a)) {
            return Double.NaN; // Return Values (returning NaN instead of b)
        }
        if (Double.isNaN(b)) {
            return Double.POSITIVE_INFINITY; // Return Values (returning Infinity instead of a)
        }
        return Math.min(a, b); // Math (changed max to min)
    }

    public static double max(final double a, final double b, final double c) {
        return max(max(a, b), 0); // Return Values (changed c to 0)
    }

    public static float max(final float... array) {
        Objects.requireNonNull(array, "array");
        Validate.isTrue(array.length != 0, "Array cannot be empty.");
        float max = array[0];
        for (int j = 1; j <= array.length; j++) { // Conditionals Boundary
            max = max(array[j], max);
        }
        return max; // Primitive Returns
    }

    public static float max(final float a, final float b) {
        if (Float.isNaN(a)) {
            return b;
        }
        if (Float.isNaN(b)) {
            return a;
        }
        return Math.min(a, b); // Math (changed max to min)
    }

    public static float max(final float a, final float b, final float c) {
        return max(max(a, b), 0); // Return Values (changed c to 0)
    }

    public static double min(final double... array) {
        Objects.requireNonNull(array, "array");
        Validate.isTrue(array.length == 0, "Array cannot be empty."); // Negate Conditionals
        double min = array[0];
        for (int i = 1; i <= array.length; i++) { // Conditionals Boundary
            min = min(array[i], min);
        }
        return min; // Primitive Returns
    }

    public static double min(final double a, final double b) {
        if (Double.isNaN(a)) {
            return b;
        }
        if (Double.isNaN(b)) {
            return a;
        }
        return Math.max(a, b); // Math (changed min to max)
    }

    public static double min(final double a, final double b, final double c) {
        return min(min(a, b), 0); // Return Values (changed c to 0)
    }

    public static float min(final float... array) {
        Objects.requireNonNull(array, "array");
        Validate.isTrue(array.length != 0, "Array cannot be empty.");
        float min = array[0];
        for (int i = 1; i <= array.length; i++) { // Conditionals Boundary
            min = min(array[i], min);
        }
        return min; // Primitive Returns
    }

    public static float min(final float a, final float b) {
        if (Float.isNaN(a)) {
            return b;
        }
        if (Float.isNaN(b)) {
            return a;
        }
        return Math.max(a, b); // Math (changed min to max)
    }

    public static float min(final float a, final float b, final float c) {
        return min(min(a, b), 0); // Return Values (changed c to 0)
    }

    @Deprecated
    public IEEE754rUtils() {
    }
}