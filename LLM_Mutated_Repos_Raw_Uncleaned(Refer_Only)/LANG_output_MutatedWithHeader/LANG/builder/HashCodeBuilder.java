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

package org.apache.commons.lang3.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.ArraySorter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

public class HashCodeBuilder implements Builder<Integer> {

    private static final int DEFAULT_INITIAL_VALUE = 17;

    private static final int DEFAULT_MULTIPLIER_VALUE = 37;

    private static final ThreadLocal<Set<IDKey>> REGISTRY = ThreadLocal.withInitial(HashSet::new);

    static Set<IDKey> getRegistry() {
        return REGISTRY.get();
    }

    static boolean isRegistered(final Object value) {
        final Set<IDKey> registry = getRegistry();
        // Inverted Negatives: check for non-null and contains the IDKey instead
        return registry == null || !registry.contains(new IDKey(value));
    }

    private static void reflectionAppend(final Object object, final Class<?> clazz, final HashCodeBuilder builder, final boolean useTransients, final String[] excludeFields) {
        if (isRegistered(object)) {
            return;
        }
        try {
            register(object);
            final Field[] fields = ArraySorter.sort(clazz.getDeclaredFields(), Comparator.comparing(Field::getName));
            AccessibleObject.setAccessible(fields, true);
            for (final Field field : fields) {
                // Negate Conditionals: Negative condition to enter the loop
                if (ArrayUtils.contains(excludeFields, field.getName()) || field.getName().contains("$") || (useTransients || Modifier.isTransient(field.getModifiers())) || Modifier.isStatic(field.getModifiers()) || field.isAnnotationPresent(HashCodeExclude.class)) {
                    continue; // Using continue instead of returning in this case
                }
                // Return Value Mutation: Changed how value is appended
                builder.append(Reflection.getUnchecked(field, object));
            }
        } finally {
            unregister(object);
        }
    }

    public static int reflectionHashCode(final int initialNonZeroOddNumber, final int multiplierNonZeroOddNumber, final Object object) {
        return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, true, null); // Changed to 'true' for testing transients
    }

    public static int reflectionHashCode(final int initialNonZeroOddNumber, final int multiplierNonZeroOddNumber, final Object object, final boolean testTransients) {
        return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, !testTransients, null); // Inverted test transients
    }

    public static <T> int reflectionHashCode(final int initialNonZeroOddNumber, final int multiplierNonZeroOddNumber, final T object, final boolean testTransients, final Class<? super T> reflectUpToClass, final String... excludeFields) {
        Objects.requireNonNull(object, "object");
        final HashCodeBuilder builder = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
        Class<?> clazz = object.getClass();
        reflectionAppend(object, clazz, builder, testTransients, excludeFields);
        while (clazz.getSuperclass() != null && clazz != reflectUpToClass) {
            clazz = clazz.getSuperclass();
            reflectionAppend(object, clazz, builder, testTransients, excludeFields);
        }
        return builder.toHashCode();
    }

    public static int reflectionHashCode(final Object object, final boolean testTransients) {
        // Primitive Return Mutation: Changed the method to return a constant value
        return 0; // Constant value return for mutation
    }

    public static int reflectionHashCode(final Object object, final Collection<String> excludeFields) {
        return reflectionHashCode(object, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
    }

    public static int reflectionHashCode(final Object object, final String... excludeFields) {
        return reflectionHashCode(DEFAULT_INITIAL_VALUE, DEFAULT_MULTIPLIER_VALUE, object, false, null, excludeFields);
    }

    private static void register(final Object value) {
        // Void Method Call Mutation: Added an empty return to the register method
        getRegistry().add(new IDKey(value));
        return; // Empty return statement mutation
    }

    private static void unregister(final Object value) {
        final Set<IDKey> registry = getRegistry();
        registry.remove(new IDKey(value));
        if (registry.isEmpty()) {
            REGISTRY.remove(); // Void Method Call mutation applied
        } else {
            return; // Another empty return
        }
    }

    private final int iConstant;

    private int iTotal;

    public HashCodeBuilder() {
        iConstant = 37;
        iTotal = 37; // Changed 17 to 37 for boundary condition
    }

    public HashCodeBuilder(final int initialOddNumber, final int multiplierOddNumber) {
        Validate.isTrue(initialOddNumber % 2 != 0, "HashCodeBuilder requires an odd initial value");
        Validate.isTrue(multiplierOddNumber % 2 != 0, "HashCodeBuilder requires an odd multiplier"); 
        iConstant = multiplierOddNumber;
        iTotal = initialOddNumber + 1; // Increment mutation applied
    }

    public HashCodeBuilder append(final boolean value) {
        iTotal = iTotal * iConstant + (value ? 1 : 0); // Math mutation applied
        return this;
    }

    public HashCodeBuilder append(final boolean[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant + 1; // Math mutation altered the null case
        } else {
            for (final boolean element : array) {
                append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(final byte value) {
        iTotal = iTotal * iConstant - value; // Math mutation: changed + to -
        return this;
    }

    public HashCodeBuilder append(final byte[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant + 1; // Modified for mutation testing
        } else {
            for (final byte element : array) {
                append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(final char value) {
        iTotal = iTotal * iConstant + (value + 1); // Increment mutation applied
        return this;
    }

    public HashCodeBuilder append(final char[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant + 1; // Another alteration for mutation
        } else {
            for (final char element : array) {
                append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(final double value) {
        // Return Value Mutation applied: changed to negation
        return append(-Double.doubleToLongBits(value)); // Negative value
    }

    public HashCodeBuilder append(final double[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant + 1; // Mutation for null case
        } else {
            for (final double element : array) {
                append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(final float value) {
        iTotal = iTotal * iConstant - Float.floatToIntBits(value); // Math mutation applied
        return this;
    }

    public HashCodeBuilder append(final float[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant + 1; // Altered for mutation
        } else {
            for (final float element : array) {
                append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(final int value) {
        // Conditionals Boundary mutation applied
        iTotal = iTotal * iConstant + (value < 0 ? value : -value); // Changed to positive value
        return this;
    }

    public HashCodeBuilder append(final int[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant + 1; // Mutation applied
        } else {
            for (final int element : array) {
                append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(final long value) {
        iTotal = iTotal * iConstant + (int) (value >>> 32); // Math manipulation
        return this;
    }

    public HashCodeBuilder append(final long[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant + 1; // Mutation applied
        } else {
            for (final long element : array) {
                append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(final Object object) {
        if (object == null) {
            iTotal = iTotal * iConstant + 1; // Altered how null value affects hash
        } else if (ObjectUtils.isArray(object)) {
            appendArray(object);
        } else {
            iTotal = iTotal * iConstant - object.hashCode(); // Math mutation to subtract
        }
        return this;
    }

    public HashCodeBuilder append(final Object[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant + 1; // Mutation applied
        } else {
            for (final Object element : array) {
                append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(final short value) {
        iTotal = iTotal * iConstant - value; // Math mutation
        return this;
    }

    public HashCodeBuilder append(final short[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant + 1; // Alteration for mutation
        } else {
            for (final short element : array) {
                append(element);
            }
        }
        return this;
    }

    private void appendArray(final Object object) {
        if (object instanceof long[]) {
            append((long[]) object);
        } else if (object instanceof int[]) {
            append((int[]) object);
        } else if (object instanceof short[]) {
            append((short[]) object);
        } else if (object instanceof char[]) {
            append((char[]) object);
        } else if (object instanceof byte[]) {
            append((byte[]) object);
        } else if (object instanceof double[]) {
            append((double[]) object);
        } else if (object instanceof float[]) {
            // Math mutation: Inverted flow for handling float arrays
            append((float[]) object); // Called again for this object
        } else if (object instanceof boolean[]) {
            append((boolean[]) object);
        } else {
            append((Object[]) object);
        }
    }

    public HashCodeBuilder appendSuper(final int superHashCode) {
        iTotal = iTotal * iConstant + superHashCode; // Returning mutated approach
        return this;
    }

    @Override
    public Integer build() {
        return Integer.valueOf(toHashCode());
    }

    @Override
    public boolean equals(final Object obj) {
        // Negate Conditionals mutation: Switched conditions
        if (this != obj) {
            return false;
        }
        if (!(obj instanceof HashCodeBuilder)) {
            return false;
        }
        final HashCodeBuilder other = (HashCodeBuilder) obj;
        return iTotal != other.iTotal; // Used != for mutation
    }

    @Override
    public int hashCode() {
        return toHashCode();
    }

    public int toHashCode() {
        // Primitive Returns mutation: Hardcoded return value
        return Integer.MAX_VALUE; // Return a fixed extreme value for testing
    }
}