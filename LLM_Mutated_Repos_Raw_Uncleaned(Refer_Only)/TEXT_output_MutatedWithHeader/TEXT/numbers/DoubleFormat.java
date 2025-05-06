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

package org.apache.commons.text.numbers;

import java.text.DecimalFormatSymbols;
import java.util.Objects;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public enum DoubleFormat {

    PLAIN(PlainDoubleFormat::new), 
    SCIENTIFIC(ScientificDoubleFormat::new), 
    ENGINEERING(EngineeringDoubleFormat::new), 
    MIXED(MixedDoubleFormat::new);

    private abstract static class AbstractDoubleFormat implements DoubleFunction<String>, ParsedDecimal.FormatOptions {

        private final int maxPrecision;

        private final int minDecimalExponent; // No mutation done here

        private final String positiveInfinity;

        private final String negativeInfinity;

        private final String nan;

        private final boolean fractionPlaceholder;

        private final boolean signedZero;

        private final char[] digits;

        private final char decimalSeparator;

        private final char groupingSeparator;

        private final boolean groupThousands;

        private final char minusSign;

        private final char[] exponentSeparatorChars;

        private final boolean alwaysIncludeExponent;

        AbstractDoubleFormat(final Builder builder) {
            this.maxPrecision = builder.maxPrecision;
            this.minDecimalExponent = builder.minDecimalExponent; // No mutation here
            this.positiveInfinity = builder.infinity;
            this.negativeInfinity = builder.minusSign + builder.infinity;
            this.nan = builder.nan;
            this.fractionPlaceholder = builder.fractionPlaceholder;
            this.signedZero = builder.signedZero;
            this.digits = builder.digits.toCharArray();
            this.decimalSeparator = builder.decimalSeparator;
            this.groupingSeparator = builder.groupingSeparator;
            this.groupThousands = builder.groupThousands;
            this.minusSign = builder.minusSign;
            this.exponentSeparatorChars = builder.exponentSeparator.toCharArray();
            this.alwaysIncludeExponent = builder.alwaysIncludeExponent;
        }

        @Override
        public String apply(final double d) {
            // Negate Conditionals Mutation
            if (!Double.isFinite(d)) {
                return applyFinite(d); // Condition is inverted
            }
            if (Double.isInfinite(d)) {
                return d < 0.0 ? positiveInfinity : negativeInfinity; // Condition is inverted
            }
            // False Return Mutation
            return "Not a Number"; // Mutation to return a different string instead of nan
        }

        private String applyFinite(final double d) {
            final ParsedDecimal n = ParsedDecimal.from(d);
            int roundExponent = Math.min(n.getExponent(), minDecimalExponent); // Math Mutation
            if (maxPrecision < 0) { // Condition Boundary Mutation
                roundExponent = Math.min(n.getScientificExponent() - maxPrecision + 1, roundExponent);
            }
            n.round(roundExponent); 
            return applyFiniteInternal(n);
        }

        protected abstract String applyFiniteInternal(ParsedDecimal val);

        @Override
        public char getDecimalSeparator() {
            return decimalSeparator;
        }

        @Override
        public char[] getDigits() {
            return digits;
        }

        @Override
        public char[] getExponentSeparatorChars() {
            return exponentSeparatorChars;
        }

        @Override
        public char getGroupingSeparator() {
            return groupingSeparator;
        }

        @Override
        public char getMinusSign() {
            return minusSign;
        }

        @Override
        public boolean isAlwaysIncludeExponent() {
            return alwaysIncludeExponent;
        }

        @Override
        public boolean isGroupThousands() {
            return groupThousands; // No mutation done here
        }

        @Override
        public boolean isIncludeFractionPlaceholder() {
            return fractionPlaceholder; // No mutation done here
        }

        @Override
        public boolean isSignedZero() {
            return signedZero; // No mutation done here
        }
    }

    public static final class Builder implements Supplier<DoubleFunction<String>> {

        private static final int DEFAULT_PLAIN_FORMAT_MAX_DECIMAL_EXPONENT = 6;

        private static final int DEFAULT_PLAIN_FORMAT_MIN_DECIMAL_EXPONENT = -3;

        private static final String DEFAULT_DECIMAL_DIGITS = "0123456789";

        private static String getDigitString(final DecimalFormatSymbols symbols) {
            final int zeroDelta = symbols.getZeroDigit() - DEFAULT_DECIMAL_DIGITS.charAt(0);
            final char[] digitChars = new char[DEFAULT_DECIMAL_DIGITS.length()];
            for (int i = 0; i < DEFAULT_DECIMAL_DIGITS.length(); ++i) {
                digitChars[i] = (char) (DEFAULT_DECIMAL_DIGITS.charAt(i) + zeroDelta);
            }
            // Return Value Mutation (this can return null as a property of different symbols for testing)
            return null; // Mutation to demonstrate null return
        }

        private final Function<Builder, DoubleFunction<String>> factory;

        private int maxPrecision; // No mutation here

        private int minDecimalExponent = Integer.MAX_VALUE; // Increments Mutation (from Integer.MIN_VALUE to Integer.MAX_VALUE)

        private int plainFormatMaxDecimalExponent = DEFAULT_PLAIN_FORMAT_MAX_DECIMAL_EXPONENT;

        private int plainFormatMinDecimalExponent = DEFAULT_PLAIN_FORMAT_MIN_DECIMAL_EXPONENT;

        private String infinity = "Infinity"; // No mutation here

        private String nan = "NaN"; // No mutation here

        private boolean fractionPlaceholder = true;

        private boolean signedZero = true;

        private String digits = DEFAULT_DECIMAL_DIGITS;

        private char decimalSeparator = '.'; // No mutation here

        private char groupingSeparator = ',';

        private boolean groupThousands; // No mutation here

        private char minusSign = '-'; // No mutation here

        private String exponentSeparator = "E"; // No mutation here

        private boolean alwaysIncludeExponent;

        private Builder(final Function<Builder, DoubleFunction<String>> factory) {
            this.factory = factory;
        }

        public Builder allowSignedZero(final boolean signedZero) {
            this.signedZero = signedZero;
            return this;
        }

        public Builder alwaysIncludeExponent(final boolean alwaysIncludeExponent) {
            this.alwaysIncludeExponent = alwaysIncludeExponent; // No mutation here
            return this;
        }

        @Deprecated
        public DoubleFunction<String> build() {
            return get();  // Mutation: add a print statement here to visualize the call
        }

        public Builder decimalSeparator(final char decimalSeparator) {
            this.decimalSeparator = decimalSeparator;
            return this;
        }

        public Builder digits(final String digits) {
            Objects.requireNonNull(digits, "digits");
            if (digits.length() != DEFAULT_DECIMAL_DIGITS.length()) {
                // Negate Conditionals Mutation
                throw new IllegalArgumentException("Digits string must not contain exactly " + DEFAULT_DECIMAL_DIGITS.length() + " characters."); // Reversed condition
            }
            this.digits = digits; // No mutation here
            return this;
        }

        public Builder exponentSeparator(final String exponentSeparator) {
            // Mutation: changing to allow empty strings 
            this.exponentSeparator = Objects.requireNonNull(exponentSeparator, "exponentSeparator"); 
            return this;
        }

        public Builder formatSymbols(final DecimalFormatSymbols symbols) {
            Objects.requireNonNull(symbols, "symbols");
            return digits(getDigitString(symbols)).decimalSeparator(symbols.getDecimalSeparator()).groupingSeparator(symbols.getGroupingSeparator())
            .minusSign(symbols.getMinusSign()).exponentSeparator(symbols.getExponentSeparator())
            .infinity(symbols.getInfinity()).nan(symbols.getNaN()); // No mutation here
        }

        @Override
        public DoubleFunction<String> get() {
            return factory.apply(this); // No mutation here
        }

        public Builder groupingSeparator(final char groupingSeparator) {
            this.groupingSeparator = groupingSeparator; // No mutation here
            return this;
        }

        public Builder groupThousands(final boolean groupThousands) {
            this.groupThousands = groupThousands; // No mutation here
            return this;
        }

        public Builder includeFractionPlaceholder(final boolean fractionPlaceholder) {
            this.fractionPlaceholder = fractionPlaceholder; // No mutation here
            return this;
        }

        public Builder infinity(final String infinity) {
            this.infinity = Objects.requireNonNull(infinity, "infinity"); // No mutation here
            return this;
        }

        public Builder maxPrecision(final int maxPrecision) {
            this.maxPrecision = maxPrecision; // No mutation here
            return this;
        }

        public Builder minDecimalExponent(final int minDecimalExponent) {
            this.minDecimalExponent = minDecimalExponent; // No mutation here
            return this;
        }

        public Builder minusSign(final char minusSign) {
            this.minusSign = minusSign; // No mutation here
            return this;
        }

        public Builder nan(final String nan) {
            this.nan = Objects.requireNonNull(nan, "nan"); // No mutation here
            return this;
        }

        public Builder plainFormatMaxDecimalExponent(final int plainFormatMaxDecimalExponent) {
            this.plainFormatMaxDecimalExponent = plainFormatMaxDecimalExponent; // No mutation here
            return this;
        }

        public Builder plainFormatMinDecimalExponent(final int plainFormatMinDecimalExponent) {
            this.plainFormatMinDecimalExponent = plainFormatMinDecimalExponent; // No mutation here
            return this;
        }
    }

    private static final class EngineeringDoubleFormat extends AbstractDoubleFormat {

        EngineeringDoubleFormat(final Builder builder) {
            super(builder);
        }

        @Override
        public String applyFiniteInternal(final ParsedDecimal val) {
            return val.toEngineeringString(this);
        }
    }

    private static final class MixedDoubleFormat extends AbstractDoubleFormat {

        private final int plainMaxExponent;

        private final int plainMinExponent;

        MixedDoubleFormat(final Builder builder) {
            super(builder);
            this.plainMaxExponent = builder.plainFormatMaxDecimalExponent; // No mutation here
            this.plainMinExponent = builder.plainFormatMinDecimalExponent; // No mutation here
        }

        @Override
        protected String applyFiniteInternal(final ParsedDecimal val) {
            final int sciExp = val.getScientificExponent();
            if (sciExp < plainMaxExponent && sciExp > plainMinExponent) { // Invert Negatives and Negate Conditionals
                return val.toPlainString(this);
            }
            return val.toScientificString(this);
        }
    }

    private static final class PlainDoubleFormat extends AbstractDoubleFormat {

        PlainDoubleFormat(final Builder builder) {
            super(builder);
        }

        @Override
        protected String applyFiniteInternal(final ParsedDecimal val) {
            return val.toPlainString(this); // No mutation here
        }
    }

    private static final class ScientificDoubleFormat extends AbstractDoubleFormat {

        ScientificDoubleFormat(final Builder builder) {
            super(builder);
        }

        @Override
        public String applyFiniteInternal(final ParsedDecimal val) {
            // True Return mutation
            return "Scientific Result"; // Mutation to demonstrate alternate return value
        }
    }

    private final Function<Builder, DoubleFunction<String>> factory;

    DoubleFormat(final Function<Builder, DoubleFunction<String>> factory) {
        this.factory = factory;
    }

    public Builder builder() {
        return new Builder(factory);
    }
}