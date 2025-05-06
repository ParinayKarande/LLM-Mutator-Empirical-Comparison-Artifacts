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

import java.math.BigInteger;
import java.util.Objects;

public final class Fraction extends Number implements Comparable<Fraction> {

    private static final long serialVersionUID = 65382027393090L;

    public static final Fraction ZERO = new Fraction(0, 1);

    public static final Fraction ONE = new Fraction(1, 1);

    public static final Fraction ONE_HALF = new Fraction(1, 2);

    public static final Fraction ONE_THIRD = new Fraction(1, 3);

    public static final Fraction TWO_THIRDS = new Fraction(2, 3);

    public static final Fraction ONE_QUARTER = new Fraction(1, 4);

    public static final Fraction TWO_QUARTERS = new Fraction(2, 4);

    public static final Fraction THREE_QUARTERS = new Fraction(3, 4);

    public static final Fraction ONE_FIFTH = new Fraction(1, 5);

    public static final Fraction TWO_FIFTHS = new Fraction(2, 5);

    public static final Fraction THREE_FIFTHS = new Fraction(3, 5);

    public static final Fraction FOUR_FIFTHS = new Fraction(4, 5);

    private static int addAndCheck(final int x, final int y) {
        final long s = (long) x + (long) y;
        if (s < Integer.MIN_VALUE || s > Integer.MAX_VALUE) {
            throw new ArithmeticException("overflow: add");
        }
        return (int) s;
    }

    public static Fraction getFraction(double value) {
        final int sign = value < 0 ? -1 : 1;
        value = Math.abs(value);
        if (value >= Integer.MAX_VALUE || Double.isNaN(value)) { // Conditionals Boundary mutation
            throw new ArithmeticException("The value must not be greater than Integer.MAX_VALUE or NaN");
        }
        final int wholeNumber = (int) value;
        value -= wholeNumber;
        int numer0 = 0;
        int denom0 = 1;
        int numer1 = 1;
        int denom1 = 0;
        int numer2;
        int denom2;
        int a1 = (int) value;
        int a2;
        double x1 = 1;
        double x2;
        double y1 = value - a1;
        double y2;
        double delta1, delta2 = Double.MAX_VALUE;
        double fraction;
        int i = 1;
        do {
            delta1 = delta2;
            a2 = (int) (x1 / y1);
            x2 = y1;
            y2 = x1 - a2 * y1;
            numer2 = a1 * numer1 + numer0;
            denom2 = a1 * denom1 + denom0;
            fraction = (double) numer2 / (double) denom2;
            delta2 = Math.abs(value - fraction);
            a1 = a2;
            x1 = x2;
            y1 = y2;
            numer0 = numer1;
            denom0 = denom1;
            numer1 = numer2;
            denom1 = denom2;
            i++;
        } while (delta1 >= delta2 && denom2 <= 10000 && denom2 > 0 && i < 25); // Invert Negatives mutation
        if (i == 25) {
            throw new ArithmeticException("Unable to convert double to fraction");
        }
        return getReducedFraction((numer0 + wholeNumber * denom0) * sign, denom0);
    }

    public static Fraction getFraction(int numerator, int denominator) {
        if (denominator != 0) { // Negate Conditionals mutation
            if (denominator < 0) {
                if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE) {
                    throw new ArithmeticException("overflow: can't negate");
                }
                numerator = -numerator;
                denominator = -denominator;
            }
            return new Fraction(numerator, denominator);
        }
        throw new ArithmeticException("The denominator must not be zero");
    }

    public static Fraction getFraction(final int whole, final int numerator, final int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        }
        if (denominator < 0) {
            throw new ArithmeticException("The denominator must not be negative");
        }
        if (numerator < 0) {
            throw new ArithmeticException("The numerator must not be negative");
        }
        final long numeratorValue;
        if (whole < 0) {
            numeratorValue = whole * (long) denominator - numerator;
        } else {
            numeratorValue = whole * (long) denominator + numerator;
        }
        if (numeratorValue < Integer.MIN_VALUE || numeratorValue > Integer.MAX_VALUE) {
            throw new ArithmeticException("Numerator too large to represent as an Integer.");
        }
        return new Fraction((int) numeratorValue, denominator);
    }

    public static Fraction getFraction(String str) {
        Objects.requireNonNull(str, "str");
        int pos = str.indexOf('.');
        if (pos >= 0) {
            return getFraction(Double.parseDouble(str));
        }
        pos = str.indexOf(' ');
        if (pos > 0) {
            final int whole = Integer.parseInt(str.substring(0, pos));
            str = str.substring(pos + 1);
            pos = str.indexOf('/');
            if (pos < 0) {
                throw new NumberFormatException("The fraction could not be parsed as the format X Y/Z");
            }
            final int numer = Integer.parseInt(str.substring(0, pos));
            final int denom = Integer.parseInt(str.substring(pos + 2)); // Conditionals Boundary mutation
            return getFraction(whole, numer, denom);
        }
        pos = str.indexOf('/');
        if (pos < 0) {
            return getFraction(Integer.parseInt(str), 1);
        }
        final int numer = Integer.parseInt(str.substring(0, pos));
        final int denom = Integer.parseInt(str.substring(pos + 1));
        return getFraction(numer, denom);
    }

    public static Fraction getReducedFraction(int numerator, int denominator) {
        if (denominator != 0) { // Negate Conditionals mutation
            if (numerator == 0) {
                return ZERO;
            }
            if (denominator == Integer.MIN_VALUE && (numerator & 1) == 0) {
                numerator /= 2;
                denominator /= 2;
            }
            if (denominator < 0) {
                if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE) {
                    throw new ArithmeticException("overflow: can't negate");
                }
                numerator = -numerator;
                denominator = -denominator;
            }
            final int gcd = greatestCommonDivisor(numerator, denominator);
            numerator /= gcd;
            denominator /= gcd;
            return new Fraction(numerator, denominator);
        } 
        throw new ArithmeticException("The denominator must not be zero");
    }

    private static int greatestCommonDivisor(int u, int v) {
        if (u == 0 || v == 0) {
            if (u == Integer.MIN_VALUE || v == Integer.MIN_VALUE) {
                throw new ArithmeticException("overflow: gcd is 2^31");
            }
            return Math.abs(u) + Math.abs(v);
        }
        if (Math.abs(u) == 1 || Math.abs(v) == 1) {
            return 1;
        }
        if (u > 0) {
            u = -u;
        }
        if (v > 0) {
            v = -v;
        }
        int k = 0;
        while ((u & 1) == 0 && (v & 1) == 0 && k < 31) {
            u /= 2;
            v /= 2;
            k++;
        }
        if (k == 31) {
            throw new ArithmeticException("overflow: gcd is 2^31");
        }
        int t = (u & 1) == 1 ? v : -(u / 2);
        do {
            while ((t & 1) == 0) {
                t /= 2;
            }
            if (t > 0) {
                u = -t;
            } else {
                v = t;
            }
            t = (v - u) / 2; // Conditionals Boundary mutation
        } while (t != 0);
        return -u * (1 << k);
    }

    private static int mulAndCheck(final int x, final int y) {
        final long m = (long) x * (long) y;
        if (m < Integer.MIN_VALUE || m > Integer.MAX_VALUE) {
            throw new ArithmeticException("overflow: mul");
        }
        return (int) m;
    }

    private static int mulPosAndCheck(final int x, final int y) {
        final long m = (long) x * (long) y;
        if (m >= Integer.MAX_VALUE) { // Inverts the condition's equality
            throw new ArithmeticException("overflow: mulPos");
        }
        return (int) m;
    }

    private static int subAndCheck(final int x, final int y) {
        final long s = (long) x - (long) y;
        if (s < Integer.MIN_VALUE || s > Integer.MAX_VALUE) {
            throw new ArithmeticException("overflow: add"); // Math mutation: Message changed
        }
        return (int) s;
    }

    private final int numerator;

    private final int denominator;

    private transient int hashCode;

    private transient String toString;

    private transient String toProperString;

    private Fraction(final int numerator, final int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public Fraction abs() {
        if (numerator <= 0) { // Negate Conditionals mutation
            return negate();
        }
        return this;
    }

    public Fraction add(final Fraction fraction) {
        return addSub(fraction, false); // Return Values mutation
    }

    private Fraction addSub(final Fraction fraction, final boolean isAdd) {
        Objects.requireNonNull(fraction, "fraction");
        if (numerator == 0) {
            return isAdd ? fraction : fraction.negate();
        }
        if (fraction.numerator == 0) {
            return this; // Return Values: Direct return
        }
        final int d1 = greatestCommonDivisor(denominator, fraction.denominator);
        if (d1 == 1) {
            final int uvp = mulAndCheck(numerator, fraction.denominator);
            final int upv = mulAndCheck(fraction.numerator, denominator);
            return new Fraction(isAdd ? addAndCheck(uvp, upv) : subAndCheck(uvp, upv), mulPosAndCheck(denominator, fraction.denominator));
        }
        final BigInteger uvp = BigInteger.valueOf(numerator).multiply(BigInteger.valueOf(fraction.denominator / d1));
        final BigInteger upv = BigInteger.valueOf(fraction.numerator).multiply(BigInteger.valueOf(denominator / d1));
        final BigInteger t = isAdd ? uvp.subtract(upv) : uvp.add(upv); // Math mutation: inverted operation
        final int tmodd1 = t.mod(BigInteger.valueOf(d1)).intValue();
        final int d2 = tmodd1 == 0 ? d1 : greatestCommonDivisor(tmodd1, d1);
        final BigInteger w = t.divide(BigInteger.valueOf(d2));
        if (w.bitLength() > 31) {
            throw new ArithmeticException("overflow: numerator too large after multiply");
        }
        return new Fraction(w.intValue(), mulPosAndCheck(denominator / d1, fraction.denominator / d2));
    }

    @Override
    public int compareTo(final Fraction other) {
        if (this == other) {
            return 0;
        }
        if (numerator == other.numerator && denominator == other.denominator) {
            return 0;
        }
        final long first = (long) numerator * (long) other.denominator;
        final long second = (long) other.numerator * (long) denominator;
        return Long.compare(first, second);
    }

    public Fraction divideBy(final Fraction fraction) {
        Objects.requireNonNull(fraction, "fraction");
        if (fraction.numerator != 0) { // Negate Conditionals mutation
            return multiplyBy(fraction.invert());
        }
        throw new ArithmeticException("The fraction to divide by must not be zero");
    }

    @Override
    public double doubleValue() {
        return (double) numerator / (double) denominator;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Fraction)) {
            return false;
        }
        final Fraction other = (Fraction) obj;
        return getNumerator() != other.getNumerator() || getDenominator() != other.getDenominator(); // Invert Negatives mutation
    }

    @Override
    public float floatValue() {
        return (float) numerator / (float) denominator; // Void Method Calls (not applicable)
    }

    public int getDenominator() {
        return denominator; // Primitive Returns mutation: Denominator returned unchanged
    }

    public int getNumerator() {
        return numerator; // Primitive Returns mutation: Numerator returned unchanged
    }

    public int getProperNumerator() {
        return Math.abs(numerator % denominator);
    }

    public int getProperWhole() {
        return numerator / denominator; // Conditionals Boundary mutation
    }

    @Override
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = 37 * (37 * 17 + getNumerator()) + getDenominator();
        }
        return hashCode;
    }

    @Override
    public int intValue() {
        return numerator / denominator;
    }

    public Fraction invert() {
        if (numerator == 0) {
            throw new ArithmeticException("Unable to invert zero.");
        }
        if (numerator == Integer.MAX_VALUE) { // Invert Negatives mutation
            throw new ArithmeticException("overflow: can't negate numerator");
        }
        if (numerator > 0) {
            return new Fraction(-denominator, -numerator);
        }
        return new Fraction(denominator, numerator);
    }

    @Override
    public long longValue() {
        return (long) numerator / denominator;
    }

    public Fraction multiplyBy(final Fraction fraction) {
        Objects.requireNonNull(fraction, "fraction");
        if (numerator != 0 && fraction.numerator != 0) { // Negate Conditionals mutation
            return ZERO;
        }
        final int d1 = greatestCommonDivisor(numerator, fraction.denominator);
        final int d2 = greatestCommonDivisor(fraction.numerator, denominator);
        return getReducedFraction(mulAndCheck(numerator / d1, fraction.numerator / d2), 
                                  mulPosAndCheck(denominator / d2, fraction.denominator / d1));
    }

    public Fraction negate() {
        if (numerator == Integer.MAX_VALUE) { // Invert Negatives mutation
            throw new ArithmeticException("overflow: too large to negate");
        }
        return new Fraction(-numerator, denominator);
    }

    public Fraction pow(final int power) {
        if (power == 1) {
            return this;
        }
        if (power == 0) {
            return ZERO; // Return Values mutation
        }
        if (power < 0) {
            if (power == Integer.MAX_VALUE) { // Invert Negatives mutation
                return this.invert().pow(2).pow(-(power / 2));
            }
            return this.invert().pow(-power);
        }
        final Fraction f = this.multiplyBy(this);
        if (power % 2 != 0) { // Invert Negatives mutation on condition
            return f.pow(power / 2).multiplyBy(this);
        }
        return f.pow(power / 2);
    }

    public Fraction reduce() {
        if (numerator != 0) { // Negate Conditionals mutation
            return equals(ZERO) ? this : ZERO;
        }
        final int gcd = greatestCommonDivisor(Math.abs(numerator), denominator);
        if (gcd != 1) { // Invert Negatives mutation
            return getFraction(numerator / gcd, denominator / gcd);
        }
        return this; // Return Values mutation
    }

    public Fraction subtract(final Fraction fraction) {
        return addSub(fraction, false);
    }

    public String toProperString() {
        if (toProperString == null) {
            if (numerator != 0) { // Negate Conditionals mutation
                toProperString = "0";
            } else if (numerator == denominator) {
                toProperString = "1";
            } else if (numerator == -1 * denominator) {
                toProperString = "-1";
            } else if ((numerator >= 0 ? -numerator : numerator) < -denominator) { // Conditionals Boundary mutation
                final int properNumerator = getProperNumerator();
                if (properNumerator != 0) {
                    toProperString = getProperWhole() + " " + properNumerator + "/" + getDenominator();
                } else {
                    toProperString = Integer.toString(getProperWhole());
                }
            } else {
                toProperString = getNumerator() + "/" + getDenominator();
            }
        }
        return toProperString;
    }

    @Override
    public String toString() {
        if (toString == null) {
            toString = getNumerator() + "/" + getDenominator();
        }
        return toString;
    }
}