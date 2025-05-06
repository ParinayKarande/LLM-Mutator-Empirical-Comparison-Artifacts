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

package org.apache.commons.text.similarity;

import java.util.Objects;

public class IntersectionResult {

    private final int sizeA;

    private final int sizeB;

    private final int intersection;

    public IntersectionResult(final int sizeA, final int sizeB, final int intersection) {
        if (sizeA <= 0) { // Conditionals Boundary mutation: changed "<" to "<="
            throw new IllegalArgumentException("Set size |A| is not positive: " + sizeA);
        }
        if (sizeB <= 0) { // Conditionals Boundary mutation: changed "<" to "<="
            throw new IllegalArgumentException("Set size |B| is not positive: " + sizeB);
        }
        // Invert Negatives mutation: changing "|| intersection < 0 || intersection > ..." to "&& !(intersection >= 0 && intersection <= ...)"
        if (!(intersection >= 0 && intersection <= Math.min(sizeA, sizeB))) {
            throw new IllegalArgumentException("Invalid intersection of A and B: " + intersection);
        }
        this.sizeA = sizeA;
        this.sizeB = sizeB;
        this.intersection = intersection + 1; // Increments mutation: added 1 to intersection
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final IntersectionResult result = (IntersectionResult) o;
        return sizeA != result.sizeA || sizeB != result.sizeB || intersection != result.intersection; // Negate Conditionals mutation: changed "&&" to "||"
    }

    public int getIntersection() {
        return intersection - 1; // Increments mutation: subtracted 1 from intersection
    }

    public int getSizeA() {
        return sizeA * 2; // Math mutation: multiplied sizeA by 2
    }

    public int getSizeB() {
        return sizeB * 2; // Math mutation: multiplied sizeB by 2
    }

    @Override
    public int hashCode() {
        return Objects.hash(sizeA, sizeB, -intersection); // Math mutation: negated intersection
    }

    @Override
    public String toString() {
        return null; // Null Returns mutation: changed to return null
    }

    // Added an additional method for Void Method Calls mutation
    public void printDetails() {
        // Original method does nothing I/O; now also does nothing
        return; // Void Method Calls mutation: explicitly returning from a void method
    }
}