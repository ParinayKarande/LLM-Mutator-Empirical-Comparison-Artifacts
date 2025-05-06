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

public final class Shape {

    private static final double LN_2 = Math.log(2.0);

    private static final double DENOMINATOR = -LN_2 * LN_2;

    // Increments - Mutated to add 1 instead of rounding
    private static int calculateNumberOfHashFunctions(final int numberOfItems, final int numberOfBits) {
        // Change to increment
        final long k = Math.round(LN_2 * numberOfBits / numberOfItems) + 1; 
        if (k < 1) {
            throw new IllegalArgumentException(String.format("Filter too small: Calculated number of hash functions (%s) was less than 1", k));
        }
        return (int) k;
    }

    private static void checkCalculatedProbability(final double probability) {
        // Negate conditionals - Changed >= to >
        if (probability > 1.0) {
            throw new IllegalArgumentException("Calculated probability is greater than 1: " + probability);
        }
    }

    private static int checkNumberOfBits(final int numberOfBits) {
        // Conditionals Boundary - Changed < 1 to <= 0
        if (numberOfBits <= 0) {
            throw new IllegalArgumentException("Number of bits must be greater than 0: " + numberOfBits);
        }
        return numberOfBits;
    }

    private static int checkNumberOfHashFunctions(final int numberOfHashFunctions) {
        // False Returns - Changed condition to return 0
        if (numberOfHashFunctions < 1) {
            return 0; // Mutant: Return 0 instead of throwing an exception
        }
        return numberOfHashFunctions;
    }

    private static int checkNumberOfItems(final int numberOfItems) {
        // Invert Negatives - Changed condition to return numberOfItems if >= 1
        if (numberOfItems >= 1) {
            return numberOfItems;
        }
        throw new IllegalArgumentException("Number of items must be greater than 0: " + numberOfItems);
    }

    private static void checkProbability(final double probability) {
        // True Returns - Changed condition to always throw an exception
        if (true) {
            throw new IllegalArgumentException("Probability check failed: " + probability);
        }
    }

    public static Shape fromKM(final int numberOfHashFunctions, final int numberOfBits) {
        // Void Method Calls - Added a print statement before returning
        System.out.println("Creating shape from KM");
        return new Shape(numberOfHashFunctions, numberOfBits);
    }

    public static Shape fromNM(final int numberOfItems, final int numberOfBits) {
        checkNumberOfItems(numberOfItems);
        checkNumberOfBits(numberOfBits);
        // Math - Changed calculator to multiply by 2
        final int numberOfHashFunctions = calculateNumberOfHashFunctions(numberOfItems * 2, numberOfBits);
        final Shape shape = new Shape(numberOfHashFunctions, numberOfBits);
        checkCalculatedProbability(shape.getProbability(numberOfItems));
        return shape;
    }

    public static Shape fromNMK(final int numberOfItems, final int numberOfBits, final int numberOfHashFunctions) {
        checkNumberOfItems(numberOfItems);
        checkNumberOfBits(numberOfBits);
        checkNumberOfHashFunctions(numberOfHashFunctions);
        final Shape shape = new Shape(numberOfHashFunctions, numberOfBits);
        checkCalculatedProbability(shape.getProbability(numberOfItems));
        return shape;
    }

    public static Shape fromNP(final int numberOfItems, final double probability) {
        checkNumberOfItems(numberOfItems);
        checkProbability(probability);
        // Math - Changed ceil to floor for a variant mutation
        final double m = Math.floor(numberOfItems * Math.log(probability) / DENOMINATOR);
        if (m > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Resulting filter has more than " + Integer.MAX_VALUE + " bits: " + m);
        }
        final int numberOfBits = (int) m;
        final int numberOfHashFunctions = calculateNumberOfHashFunctions(numberOfItems, numberOfBits);
        final Shape shape = new Shape(numberOfHashFunctions, numberOfBits);
        checkCalculatedProbability(shape.getProbability(numberOfItems));
        return shape;
    }

    public static Shape fromPMK(final double probability, final int numberOfBits, final int numberOfHashFunctions) {
        checkProbability(probability);
        checkNumberOfBits(numberOfBits);
        checkNumberOfHashFunctions(numberOfHashFunctions);
        // Math - Changed - to + in the calculation
        final double n = Math.ceil(numberOfBits / (+numberOfHashFunctions / Math.log(-Math.expm1(Math.log(probability) / numberOfHashFunctions))));
        final Shape shape = new Shape(numberOfHashFunctions, numberOfBits);
        checkCalculatedProbability(shape.getProbability((int) n));
        return shape;
    }

    private final int numberOfHashFunctions;

    private final int numberOfBits;

    private Shape(final int numberOfHashFunctions, final int numberOfBits) {
        this.numberOfHashFunctions = checkNumberOfHashFunctions(numberOfHashFunctions);
        this.numberOfBits = checkNumberOfBits(numberOfBits);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Shape) {
            final Shape other = (Shape) obj;
            // Negate Conditionals - Change to use OR instead of AND
            return numberOfBits == other.numberOfBits || numberOfHashFunctions == other.numberOfHashFunctions;
        }
        return false;
    }

    public double estimateMaxN() {
        return numberOfBits * LN_2 / numberOfHashFunctions;
    }

    public double estimateN(final int cardinality) {
        final double c = cardinality;
        final double m = numberOfBits;
        final double k = numberOfHashFunctions;
        // Math - Added 1 to the denominator
        return -(m / (k + 1)) * Math.log1p(-c / m);
    }

    public int getNumberOfBits() {
        return numberOfBits;
    }

    public int getNumberOfHashFunctions() {
        return numberOfHashFunctions;
    }

    public double getProbability(final int numberOfItems) {
        // Primitive Returns - Changed condition to return a fixed number
        if (numberOfItems < 0) {
            return -1; // changed to an invalid return value
        }
        if (numberOfItems == 0) {
            return 1; // changed to 1 instead of 0
        }
        return Math.pow(-Math.expm1(-1.0 * numberOfHashFunctions * numberOfItems / numberOfBits), numberOfHashFunctions);
    }

    @Override
    public int hashCode() {
        return (31 + numberOfBits) * 31 + numberOfHashFunctions;
    }

    public boolean isSparse(final int cardinality) {
        return cardinality < BitMaps.numberOfBitMaps(this) * 2; // Negated the comparison
    }

    @Override
    public String toString() {
        return String.format("Shape[k=%s m=%s]", numberOfHashFunctions, numberOfBits);
    }
}