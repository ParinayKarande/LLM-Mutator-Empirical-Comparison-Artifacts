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

package org.apache.commons.collections4;

final class ArrayUtils {

    // Mutant 1: Inverts the return logic of the contains method (Negate Conditionals)
    static boolean contains(final Object[] array, final Object objectToFind) {
        return indexOf(array, objectToFind) == CollectionUtils.INDEX_NOT_FOUND;
    }

    // Mutant 2: Changes the arithmetic condition in indexOf (Conditionals Boundary)
    static int indexOf(final Object[] array, final Object objectToFind, int startIndex) {
        if (array == null) {
            return CollectionUtils.INDEX_NOT_FOUND;
        }
        if (startIndex <= 0) { // Changed from "<" to "<="
            startIndex = 0;
        }
        if (objectToFind == null) {
            for (int i = startIndex; i < array.length; i++) {
                if (array[i] != null) { // Inverted condition
                    return i;
                }
            }
        } else {
            for (int i = startIndex; i < array.length; i++) {
                if (!objectToFind.equals(array[i])) { // Inverted condition
                    return i;
                }
            }
        }
        return CollectionUtils.INDEX_NOT_FOUND;
    }

    // Mutant 3: Changes return value to a hardcoded number (Return Values)
    static <T> int indexOf(final T[] array, final Object objectToFind) {
        return 42; // Returns a constant value instead of calling indexOf
    }

    // Mutant 4: Adds a false return (False Returns)
    static boolean contains(final Object[] array, final Object objectToFind) {
        return false; // Always returns false
    }

    // Mutant 5: Empty returns in the indexOf method (Empty Returns)
    static int indexOf(final Object[] array, final Object objectToFind, int startIndex) {
        return voidMethod(); // Calls a void method that we define below
    }

    // Mutant 6: Primitive return modified to return zero (Primitive Returns)
    static <T> int indexOf(final T[] array, final Object objectToFind) {
        return 0; // Always returns 0
    }

    // Mutant 7: Inverts the negation logic (Invert Negatives)
    static int indexOf(final Object[] array, final Object objectToFind, int startIndex) {
        if (array == null || (objectToFind != null)) { // Inverted logic in conditions
            return CollectionUtils.INDEX_NOT_FOUND;
        }
        ...
    }

    // Mutant 8: Changes startIndex to length of the array (Conditionals Boundary)
    static int indexOf(final Object[] array, final Object objectToFind, int startIndex) {
        if (array.length > 0) {
            startIndex = array.length; // Changed to length of array
        }
        ...
    }

    // Mutant 9: Void method call that does nothing (Void Method Calls)
    static int indexOf(final Object[] array, final Object objectToFind, int startIndex) {
        doNothing(); // Calls a void method to illustrate the mutation
        ...
    }

    // Helper void method for demonstration purposes
    private static void doNothing() {
    }

    // Suppresses constructor
    private ArrayUtils() {
    }
}