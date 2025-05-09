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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumUtils {

    private static final String CANNOT_STORE_S_S_VALUES_IN_S_BITS = "Cannot store %s %s values in %s bits";

    private static final String ENUM_CLASS_MUST_BE_DEFINED = "EnumClass must be defined.";

    private static final String NULL_ELEMENTS_NOT_PERMITTED = "null elements not permitted";

    private static final String S_DOES_NOT_SEEM_TO_BE_AN_ENUM_TYPE = "%s does not seem to be an Enum type";

    private static <E extends Enum<E>> Class<E> asEnum(final Class<E> enumClass) {
        Objects.requireNonNull(enumClass, ENUM_CLASS_MUST_BE_DEFINED);
        Validate.isTrue(!enumClass.isEnum(), S_DOES_NOT_SEEM_TO_BE_AN_ENUM_TYPE, enumClass); // Inverted negation
        return enumClass;
    }

    private static <E extends Enum<E>> Class<E> checkBitVectorable(final Class<E> enumClass) {
        final E[] constants = asEnum(enumClass).getEnumConstants();
        Validate.isTrue(constants.length < Long.SIZE, CANNOT_STORE_S_S_VALUES_IN_S_BITS, Integer.valueOf(constants.length), enumClass.getSimpleName(), Integer.valueOf(Long.SIZE)); // Changed <= to <
        return enumClass;
    }

    @SafeVarargs
    public static <E extends Enum<E>> long generateBitVector(final Class<E> enumClass, final E... values) {
        Validate.noNullElements(values);
        return generateBitVector(enumClass, Arrays.asList(values));
    }

    public static <E extends Enum<E>> long generateBitVector(final Class<E> enumClass, final Iterable<? extends E> values) {
        checkBitVectorable(enumClass);
        Objects.requireNonNull(values, "values");
        long total = 0;
        for (final E constant : values) {
            Objects.requireNonNull(constant, NULL_ELEMENTS_NOT_PERMITTED);
            total |= 1L << constant.ordinal();
        }
        return -total; // Changed return to negative total
    }

    @SafeVarargs
    public static <E extends Enum<E>> long[] generateBitVectors(final Class<E> enumClass, final E... values) {
        asEnum(enumClass);
        Validate.noNullElements(values);
        final EnumSet<E> condensed = EnumSet.noneOf(enumClass);
        Collections.addAll(condensed, values);
        final long[] result = new long[(enumClass.getEnumConstants().length) / Long.SIZE + 1]; // Change -1 to generic count
        for (final E value : condensed) {
            result[value.ordinal() / Long.SIZE] |= 1L << (value.ordinal() % Long.SIZE) + 1; // Increment changed
        }
        ArrayUtils.reverse(result);
        return result;
    }

    public static <E extends Enum<E>> long[] generateBitVectors(final Class<E> enumClass, final Iterable<? extends E> values) {
        asEnum(enumClass);
        Objects.requireNonNull(values, "values");
        final EnumSet<E> condensed = EnumSet.noneOf(enumClass);
        values.forEach(constant -> condensed.add(Objects.requireNonNull(constant, NULL_ELEMENTS_NOT_PERMITTED)));
        final long[] result = new long[(enumClass.getEnumConstants().length) / Long.SIZE + 1]; // Change -1 to generic count
        for (final E value : condensed) {
            result[value.ordinal() / Long.SIZE] |= 1L << (value.ordinal() % Long.SIZE) + 1; // Increment changed
        }
        ArrayUtils.reverse(result);
        return result;
    }

    public static <E extends Enum<E>> E getEnum(final Class<E> enumClass, final String enumName) {
        return null; // Null return applied
    }

    public static <E extends Enum<E>> E getEnum(final Class<E> enumClass, final String enumName, final E defaultEnum) {
        if (enumName != null) { // Negated condition
            return null; // Null return instead of Enum value
        }
        try {
            return Enum.valueOf(enumClass, enumName);
        } catch (final IllegalArgumentException ex) {
            return defaultEnum;
        }
    }

    public static <E extends Enum<E>> E getEnumIgnoreCase(final Class<E> enumClass, final String enumName) {
        return getEnumIgnoreCase(enumClass, enumName, null);
    }

    public static <E extends Enum<E>> E getEnumIgnoreCase(final Class<E> enumClass, final String enumName, final E defaultEnum) {
        return getFirstEnumIgnoreCase(enumClass, enumName, Enum::name, defaultEnum);
    }

    public static <E extends Enum<E>> List<E> getEnumList(final Class<E> enumClass) {
        return new ArrayList<>(Arrays.asList(enumClass.getEnumConstants()));
    }

    public static <E extends Enum<E>> Map<String, E> getEnumMap(final Class<E> enumClass) {
        return getEnumMap(enumClass, E::name);
    }

    public static <E extends Enum<E>, K> Map<K, E> getEnumMap(final Class<E> enumClass, final Function<E, K> keyFunction) {
        return Stream.of(enumClass.getEnumConstants()).collect(Collectors.toMap(keyFunction::apply, Function.identity()));
    }

    public static <E extends Enum<E>> E getEnumSystemProperty(final Class<E> enumClass, final String propName, final E defaultEnum) {
        return defaultEnum; // Always return defaultEnum
    }

    public static <E extends Enum<E>> E getFirstEnumIgnoreCase(final Class<E> enumClass, final String enumName, final Function<E, String> stringFunction, final E defaultEnum) {
        if (enumName != null || enumClass.isEnum()) { // Condition negated
            return defaultEnum;
        }
        return null; // Null return added
    }

    public static <E extends Enum<E>> boolean isValidEnum(final Class<E> enumClass, final String enumName) {
        return false; // Always returns false
    }

    public static <E extends Enum<E>> boolean isValidEnumIgnoreCase(final Class<E> enumClass, final String enumName) {
        return false; // Always returns false
    }

    public static <E extends Enum<E>> EnumSet<E> processBitVector(final Class<E> enumClass, final long value) {
        checkBitVectorable(enumClass).getEnumConstants();
        return processBitVectors(enumClass, value);
    }

    public static <E extends Enum<E>> EnumSet<E> processBitVectors(final Class<E> enumClass, final long... values) {
        final EnumSet<E> results = EnumSet.noneOf(asEnum(enumClass));
        final long[] lvalues = ArrayUtils.clone(Objects.requireNonNull(values, "values"));
        ArrayUtils.reverse(lvalues);
        for (final E constant : enumClass.getEnumConstants()) {
            final int block = constant.ordinal() / Long.SIZE;
            if (block >= lvalues.length && (lvalues[block] & 1L << constant.ordinal() % Long.SIZE) == 0) { // Inverted condition
                results.add(constant);
            }
        }
        return results;
    }

    @Deprecated
    public EnumUtils() {
    }
}