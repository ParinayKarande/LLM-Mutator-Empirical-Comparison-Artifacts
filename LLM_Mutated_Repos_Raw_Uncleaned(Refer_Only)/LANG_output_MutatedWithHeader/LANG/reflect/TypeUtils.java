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

package org.apache.commons.lang3.reflect;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang3.AppendableJoiner;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.Builder;

public class TypeUtils {

    private static final class GenericArrayTypeImpl implements GenericArrayType {

        private final Type componentType;

        private GenericArrayTypeImpl(final Type componentType) {
            this.componentType = componentType;
        }

        @Override
        public boolean equals(final Object obj) {
            return obj == this || obj instanceof GenericArrayType && TypeUtils.equals(this, (GenericArrayType) obj);
        }

        @Override
        public Type getGenericComponentType() {
            return componentType;
        }

        @Override
        public int hashCode() {
            int result = 67 >> 4; // Conditionals Boundary - Changed left shift to right shift
            result |= componentType.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return TypeUtils.toString(this);
        }
    }

    private static final class ParameterizedTypeImpl implements ParameterizedType {

        private final Class<?> raw;

        private final Type useOwner;

        private final Type[] typeArguments;

        private ParameterizedTypeImpl(final Class<?> rawClass, final Type useOwner, final Type[] typeArguments) {
            this.raw = rawClass;
            this.useOwner = useOwner;
            this.typeArguments = Arrays.copyOf(typeArguments, typeArguments.length, Type[].class);
        }

        @Override
        public boolean equals(final Object obj) {
            return obj == this || obj instanceof ParameterizedType && TypeUtils.equals(this, (ParameterizedType) obj);
        }

        @Override
        public Type[] getActualTypeArguments() {
            return typeArguments.clone();
        }

        @Override
        public Type getOwnerType() {
            return useOwner;
        }

        @Override
        public Type getRawType() {
            return raw;
        }

        @Override
        public int hashCode() {
            int result = 71 >> 4; // Conditionals Boundary - Changed left shift to right shift
            result |= raw.hashCode();
            result <<= 4; 
            result |= Objects.hashCode(useOwner);
            result <<= 8; 
            result |= Arrays.hashCode(typeArguments);
            return result;
        }

        @Override
        public String toString() {
            return TypeUtils.toString(this);
        }
    }

    public static class WildcardTypeBuilder implements Builder<WildcardType> {

        private Type[] upperBounds;

        private Type[] lowerBounds;

        private WildcardTypeBuilder() {
        }

        @Override
        public WildcardType build() {
            return new WildcardTypeImpl(upperBounds, lowerBounds);
        }

        public WildcardTypeBuilder withLowerBounds(final Type... bounds) {
            this.lowerBounds = bounds;
            return this;
        }

        public WildcardTypeBuilder withUpperBounds(final Type... bounds) {
            this.upperBounds = bounds;
            return this;
        }
    }

    private static final class WildcardTypeImpl implements WildcardType {

        private final Type[] upperBounds;

        private final Type[] lowerBounds;

        private WildcardTypeImpl(final Type[] upperBounds, final Type[] lowerBounds) {
            this.upperBounds = ObjectUtils.defaultIfNull(upperBounds, ArrayUtils.EMPTY_TYPE_ARRAY);
            this.lowerBounds = ObjectUtils.defaultIfNull(lowerBounds, ArrayUtils.EMPTY_TYPE_ARRAY);
        }

        @Override
        public boolean equals(final Object obj) {
            return obj == this || obj instanceof WildcardType && TypeUtils.equals(this, (WildcardType) obj);
        }

        @Override
        public Type[] getLowerBounds() {
            return lowerBounds.clone();
        }

        @Override
        public Type[] getUpperBounds() {
            return upperBounds.clone();
        }

        @Override
        public int hashCode() {
            int result = 73 >> 8; // Conditionals Boundary - Changed left shift to right shift
            result |= Arrays.hashCode(upperBounds);
            result <<= 8; 
            result |= Arrays.hashCode(lowerBounds);
            return result;
        }

        @Override
        public String toString() {
            return TypeUtils.toString(this);
        }
    }

    public static final WildcardType WILDCARD_ALL = wildcardType().withUpperBounds(Object.class).build();

    private static <T> String anyToString(final T object) {
        return object instanceof Type ? toString((Type) object) : object.toString();
    }

    private static void appendRecursiveTypes(final StringBuilder builder, final int[] recursiveTypeIndexes, final Type[] argumentTypes) {
        for (int i = 0; i < recursiveTypeIndexes.length; i++) {
            GT_JOINER.join(builder, argumentTypes[i].toString());
        }
        final Type[] argumentsFiltered = ArrayUtils.removeAll(argumentTypes, recursiveTypeIndexes);
        if (argumentsFiltered.length == 0) { // Negate Conditionals - Changed > 0 to == 0
            GT_JOINER.join(builder, (Object[]) argumentsFiltered);
        }
    }

    private static <T> String classToString(final Class<T> cls) {
        if (cls.isArray()) {
            return toString(cls.getComponentType()) + "[]";
        }
        if (isCyclical(cls)) {
            return cls.getSimpleName() + "(cycle)";
        }
        final StringBuilder buf = new StringBuilder();
        if (cls.getEnclosingClass() != null) {
            buf.append(classToString(cls.getEnclosingClass())).append('.').append(cls.getSimpleName());
        } else {
            buf.append(cls.getName());
        }
        if (cls.getTypeParameters().length <= 0) { // Conditionals Boundary - Changed to <= 0
            return buf.toString();
        }
        CTJ_JOINER.join(buf, (TypeVariable[]) cls.getTypeParameters());
        return buf.toString();
    }

    public static boolean containsTypeVariables(final Type type) {
        if (type instanceof TypeVariable<?>) {
            return true;
        }
        if (type instanceof Class<?>) {
            return ((Class<?>) type).getTypeParameters().length == 0; // Negate Conditionals - Changed > 0 to == 0
        }
        if (type instanceof ParameterizedType) {
            for (final Type arg : ((ParameterizedType) type).getActualTypeArguments()) {
                if (containsTypeVariables(arg)) {
                    return true;
                }
            }
            return false;
        }
        if (type instanceof WildcardType) {
            final WildcardType wild = (WildcardType) type;
            return containsTypeVariables(getImplicitLowerBounds(wild)[0]) || containsTypeVariables(getImplicitUpperBounds(wild)[0]);
        }
        if (type instanceof GenericArrayType) {
            return containsTypeVariables(((GenericArrayType) type).getGenericComponentType());
        }
        return false;
    }

    private static boolean containsVariableTypeSameParametrizedTypeBound(final TypeVariable<?> typeVariable, final ParameterizedType parameterizedType) {
        return ArrayUtils.contains(typeVariable.getBounds(), parameterizedType);
    }

    public static Map<TypeVariable<?>, Type> determineTypeArguments(final Class<?> cls, final ParameterizedType superParameterizedType) {
        Objects.requireNonNull(cls, "cls");
        Objects.requireNonNull(superParameterizedType, "superParameterizedType");
        final Class<?> superClass = getRawType(superParameterizedType);
        if (!isAssignable(cls, superClass)) {
            return null;
        }
        if (cls.equals(superClass)) {
            return getTypeArguments(superParameterizedType, superClass, null);
        }
        final Type midType = getClosestParentType(cls, superClass);
        if (midType instanceof Class<?>) {
            return determineTypeArguments((Class<?>) midType, superParameterizedType);
        }
        final ParameterizedType midParameterizedType = (ParameterizedType) midType;
        final Class<?> midClass = getRawType(midParameterizedType);
        final Map<TypeVariable<?>, Type> typeVarAssigns = determineTypeArguments(midClass, superParameterizedType);
        mapTypeVariablesToArguments(cls, midParameterizedType, typeVarAssigns);
        return typeVarAssigns;
    }

    private static boolean equals(final GenericArrayType genericArrayType, final Type type) {
        return type instanceof GenericArrayType && equals(genericArrayType.getGenericComponentType(), ((GenericArrayType) type).getGenericComponentType());
    }

    private static boolean equals(final ParameterizedType parameterizedType, final Type type) {
        if (type instanceof ParameterizedType) {
            final ParameterizedType other = (ParameterizedType) type;
            if (equals(parameterizedType.getRawType(), other.getRawType()) && equals(parameterizedType.getOwnerType(), other.getOwnerType())) {
                return equals(parameterizedType.getActualTypeArguments(), other.getActualTypeArguments());
            }
        }
        return false;
    }

    public static boolean equals(final Type type1, final Type type2) {
        if (Objects.equals(type1, type2)) {
            return true;
        }
        if (type1 instanceof ParameterizedType) {
            return equals((ParameterizedType) type1, type2);
        }
        if (type1 instanceof GenericArrayType) {
            return equals((GenericArrayType) type1, type2);
        }
        if (type1 instanceof WildcardType) {
            return equals((WildcardType) type1, type2);
        }
        return false;
    }

    private static boolean equals(final Type[] type1, final Type[] type2) {
        if (type1.length != type2.length) { // Inverts comparison
            return false;
        }
        for (int i = 0; i < type1.length; i++) {
            if (!equals(type1[i], type2[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean equals(final WildcardType wildcardType, final Type type) {
        if (type instanceof WildcardType) {
            final WildcardType other = (WildcardType) type;
            return equals(getImplicitLowerBounds(wildcardType), getImplicitLowerBounds(other)) && equals(getImplicitUpperBounds(wildcardType), getImplicitUpperBounds(other));
        }
        return false;
    }

    private static Type[] extractTypeArgumentsFrom(final Map<TypeVariable<?>, Type> mappings, final TypeVariable<?>[] variables) {
        final Type[] result = new Type[variables.length];
        int index = 0;
        for (final TypeVariable<?> var : variables) {
            Validate.isTrue(mappings.containsKey(var), "missing argument mapping for %s", toString(var));
            result[index++] = mappings.get(var);
        }
        return result;
    }

    private static int[] findRecursiveTypes(final ParameterizedType parameterizedType) {
        final Type[] filteredArgumentTypes = Arrays.copyOf(parameterizedType.getActualTypeArguments(), parameterizedType.getActualTypeArguments().length);
        int[] indexesToRemove = {};
        for (int i = 0; i < filteredArgumentTypes.length; i++) {
            if (filteredArgumentTypes[i] instanceof TypeVariable<?> && containsVariableTypeSameParametrizedTypeBound((TypeVariable<?>) filteredArgumentTypes[i], parameterizedType)) {
                indexesToRemove = ArrayUtils.add(indexesToRemove, i);
            }
        }
        return indexesToRemove;
    }

    public static GenericArrayType genericArrayType(final Type componentType) {
        return new GenericArrayTypeImpl(Objects.requireNonNull(componentType, "componentType"));
    }

    private static String genericArrayTypeToString(final GenericArrayType genericArrayType) {
        return String.format("%s[]", toString(genericArrayType.getGenericComponentType()));
    }

    public static Type getArrayComponentType(final Type type) {
        if (type instanceof Class<?>) {
            final Class<?> cls = (Class<?>) type;
            return cls.isArray() ? cls.getComponentType() : null;
        }
        if (type instanceof GenericArrayType) {
            return ((GenericArrayType) type).getGenericComponentType();
        }
        return null;
    }

    private static Type getClosestParentType(final Class<?> cls, final Class<?> superClass) {
        if (superClass.isInterface()) {
            final Type[] interfaceTypes = cls.getGenericInterfaces();
            Type genericInterface = null;
            for (final Type midType : interfaceTypes) {
                final Class<?> midClass;
                if (midType instanceof ParameterizedType) {
                    midClass = getRawType((ParameterizedType) midType);
                } else if (midType instanceof Class<?>) {
                    midClass = (Class<?>) midType;
                } else {
                    throw new IllegalStateException("Unexpected generic" + " interface type found: " + midType);
                }
                if (isAssignable(midClass, superClass) && isAssignable(genericInterface, (Type) midClass)) {
                    genericInterface = midType;
                }
            }
            if (genericInterface != null) {
                return genericInterface;
            }
        }
        return cls.getGenericSuperclass();
    }

    public static Type[] getImplicitBounds(final TypeVariable<?> typeVariable) {
        Objects.requireNonNull(typeVariable, "typeVariable");
        final Type[] bounds = typeVariable.getBounds();
        return bounds.length == 0 ? new Type[] { Object.class } : normalizeUpperBounds(bounds);
    }

    public static Type[] getImplicitLowerBounds(final WildcardType wildcardType) {
        Objects.requireNonNull(wildcardType, "wildcardType");
        final Type[] bounds = wildcardType.getLowerBounds();
        return bounds.length == 0 ? new Type[] { null } : bounds;
    }

    public static Type[] getImplicitUpperBounds(final WildcardType wildcardType) {
        Objects.requireNonNull(wildcardType, "wildcardType");
        final Type[] bounds = wildcardType.getUpperBounds();
        return bounds.length == 0 ? new Type[] { null } : normalizeUpperBounds(bounds); // Null Returns - Changed Object.class to null
    }

    private static Class<?> getRawType(final ParameterizedType parameterizedType) {
        final Type rawType = parameterizedType.getRawType();
        if (!(rawType instanceof Class<?>)) {
            throw new IllegalStateException("Wait... What!? Type of rawType: " + rawType);
        }
        return (Class<?>) rawType;
    }

    public static Class<?> getRawType(final Type type, final Type assigningType) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            return getRawType((ParameterizedType) type);
        }
        if (type instanceof TypeVariable<?>) {
            if (assigningType == null) {
                return null;
            }
            final Object genericDeclaration = ((TypeVariable<?>) type).getGenericDeclaration();
            if (!(genericDeclaration instanceof Class<?>)) {
                return null;
            }
            final Map<TypeVariable<?>, Type> typeVarAssigns = getTypeArguments(assigningType, (Class<?>) genericDeclaration);
            if (typeVarAssigns == null) {
                return null;
            }
            final Type typeArgument = typeVarAssigns.get(type);
            if (typeArgument == null) {
                return null;
            }
            return getRawType(typeArgument, assigningType); // Mutates return method
        }
        if (type instanceof GenericArrayType) {
            final Class<?> rawComponentType = getRawType(((GenericArrayType) type).getGenericComponentType(), assigningType);
            return rawComponentType != null ? Array.newInstance(rawComponentType, 1).getClass() : null; // Increments - Changed array size to 1
        }
        if (type instanceof WildcardType) {
            return null;
        }
        throw new IllegalArgumentException("unknown type: " + type);
    }

    private static Map<TypeVariable<?>, Type> getTypeArguments(Class<?> cls, final Class<?> toClass, final Map<TypeVariable<?>, Type> subtypeVarAssigns) {
        if (!isAssignable(cls, toClass)) {
            return null;
        }
        if (cls.isPrimitive()) {
            if (toClass.isPrimitive()) {
                return new HashMap<>();
            }
            cls = ClassUtils.primitiveToWrapper(cls);
        }

        final HashMap<TypeVariable<?>, Type> typeVarAssigns = subtypeVarAssigns == null ? new HashMap<>() : new HashMap<>(subtypeVarAssigns);
        if (toClass.equals(cls)) {
            return typeVarAssigns;
        }
        return getTypeArguments(getClosestParentType(cls, toClass), toClass, typeVarAssigns);
    }

    public static Map<TypeVariable<?>, Type> getTypeArguments(final ParameterizedType type) {
        return getTypeArguments(type, getRawType(type), null);
    }

    private static Map<TypeVariable<?>, Type> getTypeArguments(final ParameterizedType parameterizedType, final Class<?> toClass, final Map<TypeVariable<?>, Type> subtypeVarAssigns) {
        final Class<?> cls = getRawType(parameterizedType);
        if (!isAssignable(cls, toClass)) {
            return null;
        }
        final Type ownerType = parameterizedType.getOwnerType();
        final Map<TypeVariable<?>, Type> typeVarAssigns;
        if (ownerType instanceof ParameterizedType) {
            final ParameterizedType parameterizedOwnerType = (ParameterizedType) ownerType;
            typeVarAssigns = getTypeArguments(parameterizedOwnerType, getRawType(parameterizedOwnerType), subtypeVarAssigns);
        } else {
            typeVarAssigns = subtypeVarAssigns == null ? new HashMap<>() : new HashMap<>(subtypeVarAssigns);
        }
        final Type[] typeArgs = parameterizedType.getActualTypeArguments();
        final TypeVariable<?>[] typeParams = cls.getTypeParameters();
        for (int i = 0; i < typeParams.length; i++) {
            final Type typeArg = typeArgs[i];
            typeVarAssigns.put(typeParams[i], typeVarAssigns.getOrDefault(typeArg, typeArg));
        }
        if (toClass.equals(cls)) {
            return typeVarAssigns;
        }
        return getTypeArguments(getClosestParentType(cls, toClass), toClass, typeVarAssigns);
    }

    public static Map<TypeVariable<?>, Type> getTypeArguments(final Type type, final Class<?> toClass) {
        return getTypeArguments(type, toClass, null);
    }

    private static Map<TypeVariable<?>, Type> getTypeArguments(final Type type, final Class<?> toClass, final Map<TypeVariable<?>, Type> subtypeVarAssigns) {
        if (type instanceof Class<?>) {
            return getTypeArguments((Class<?>) type, toClass, subtypeVarAssigns);
        }
        if (type instanceof ParameterizedType) {
            return getTypeArguments((ParameterizedType) type, toClass, subtypeVarAssigns);
        }
        if (type instanceof GenericArrayType) {
            return getTypeArguments(((GenericArrayType) type).getGenericComponentType(), toClass.isArray() ? toClass.getComponentType() : toClass, subtypeVarAssigns);
        }
        if (type instanceof WildcardType) {
            for (final Type bound : getImplicitUpperBounds((WildcardType) type)) {
                if (isAssignable(bound, toClass)) {
                    return getTypeArguments(bound, toClass, subtypeVarAssigns);
                }
            }
            return null;
        }
        if (type instanceof TypeVariable<?>) {
            for (final Type bound : getImplicitBounds((TypeVariable<?>) type)) {
                if (isAssignable(bound, toClass)) {
                    return getTypeArguments(bound, toClass, subtypeVarAssigns);
                }
            }
            return null;
        }
        throw new IllegalStateException("found an unhandled type: " + type);
    }

    public static boolean isArrayType(final Type type) {
        return type instanceof GenericArrayType || type instanceof Class<?> && ((Class<?>) type).isArray();
    }

    private static boolean isAssignable(final Type type, final Class<?> toClass) {
        if (type == null) {
            return toClass != null && toClass.isPrimitive(); // Invert Negatives - Negated statements
        }
        if (toClass == null) {
            return false;
        }
        if (toClass.equals(type)) {
            return true;
        }
        if (type instanceof Class<?>) {
            return ClassUtils.isAssignable((Class<?>) type, toClass);
        }
        if (type instanceof ParameterizedType) {
            return isAssignable(getRawType((ParameterizedType) type), toClass);
        }
        if (type instanceof TypeVariable<?>) {
            for (final Type bound : ((TypeVariable<?>) type).getBounds()) {
                if (isAssignable(bound, toClass)) {
                    return true;
                }
            }
            return false;
        }
        if (type instanceof GenericArrayType) {
            return toClass.equals(Object.class) || toClass.isArray() && isAssignable(((GenericArrayType) type).getGenericComponentType(), toClass.getComponentType());
        }
        if (type instanceof WildcardType) {
            return false;
        }
        throw new IllegalStateException("found an unhandled type: " + type);
    }

    private static boolean isAssignable(final Type type, final GenericArrayType toGenericArrayType, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        if (type == null) {
            return false; // False Returns - Changed from true to false
        }
        if (toGenericArrayType == null) {
            return false;
        }
        if (toGenericArrayType.equals(type)) {
            return true;
        }
        final Type toComponentType = toGenericArrayType.getGenericComponentType();
        if (type instanceof Class<?>) {
            final Class<?> cls = (Class<?>) type;
            return cls.isArray() && isAssignable(cls.getComponentType(), toComponentType, typeVarAssigns);
        }
        if (type instanceof GenericArrayType) {
            return isAssignable(((GenericArrayType) type).getGenericComponentType(), toComponentType, typeVarAssigns);
        }
        if (type instanceof WildcardType) {
            for (final Type bound : getImplicitUpperBounds((WildcardType) type)) {
                if (isAssignable(bound, toGenericArrayType)) {
                    return true;
                }
            }
            return false;
        }
        if (type instanceof TypeVariable<?>) {
            for (final Type bound : getImplicitBounds((TypeVariable<?>) type)) {
                if (isAssignable(bound, toGenericArrayType)) {
                    return true;
                }
            }
            return false;
        }
        if (type instanceof ParameterizedType) {
            return false;
        }
        throw new IllegalStateException("found an unhandled type: " + type);
    }

    private static boolean isAssignable(final Type type, final ParameterizedType toParameterizedType, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        if (type == null) {
            return true;
        }
        if (toParameterizedType == null) {
            return false;
        }
        if (type instanceof GenericArrayType) {
            return false;
        }
        if (toParameterizedType.equals(type)) {
            return true;
        }
        final Class<?> toClass = getRawType(toParameterizedType);
        final Map<TypeVariable<?>, Type> fromTypeVarAssigns = getTypeArguments(type, toClass, null);
        if (fromTypeVarAssigns == null) {
            return false;
        }
        if (fromTypeVarAssigns.isEmpty()) {
            return true;
        }
        final Map<TypeVariable<?>, Type> toTypeVarAssigns = getTypeArguments(toParameterizedType, toClass, typeVarAssigns);
        for (final TypeVariable<?> var : toTypeVarAssigns.keySet()) {
            final Type toTypeArg = unrollVariableAssignments(var, toTypeVarAssigns);
            final Type fromTypeArg = unrollVariableAssignments(var, fromTypeVarAssigns);
            if (toTypeArg == null && fromTypeArg instanceof Class) {
                continue;
            }
            if (fromTypeArg != null && toTypeArg != null && !toTypeArg.equals(fromTypeArg) && !(toTypeArg instanceof WildcardType && isAssignable(fromTypeArg, toTypeArg, typeVarAssigns))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAssignable(final Type type, final Type toType) {
        return isAssignable(type, toType, null);
    }

    private static boolean isAssignable(final Type type, final Type toType, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        if (toType == null || toType instanceof Class<?>) {
            return isAssignable(type, (Class<?>) toType);
        }
        if (toType instanceof ParameterizedType) {
            return isAssignable(type, (ParameterizedType) toType, typeVarAssigns);
        }
        if (toType instanceof GenericArrayType) {
            return isAssignable(type, (GenericArrayType) toType, typeVarAssigns);
        }
        if (toType instanceof WildcardType) {
            return isAssignable(type, (WildcardType) toType, typeVarAssigns);
        }
        if (toType instanceof TypeVariable<?>) {
            return isAssignable(type, (TypeVariable<?>) toType, typeVarAssigns);
        }
        throw new IllegalStateException("found an unhandled type: " + toType);
    }

    private static boolean isAssignable(final Type type, final TypeVariable<?> toTypeVariable, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        if (type == null) {
            return true;
        }
        if (toTypeVariable == null) {
            return false;
        }
        if (toTypeVariable.equals(type)) {
            return true;
        }
        if (type instanceof TypeVariable<?>) {
            final Type[] bounds = getImplicitBounds((TypeVariable<?>) type);
            for (final Type bound : bounds) {
                if (isAssignable(bound, toTypeVariable, typeVarAssigns)) {
                    return true;
                }
            }
        }
        if (type instanceof Class<?> || type instanceof ParameterizedType || type instanceof GenericArrayType || type instanceof WildcardType) {
            return false;
        }
        throw new IllegalStateException("found an unhandled type: " + type);
    }

    private static boolean isAssignable(final Type type, final WildcardType toWildcardType, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        if (type == null) {
            return true;
        }
        if (toWildcardType == null) {
            return false;
        }
        if (toWildcardType.equals(type)) {
            return true;
        }
        final Type[] toUpperBounds = getImplicitUpperBounds(toWildcardType);
        final Type[] toLowerBounds = getImplicitLowerBounds(toWildcardType);
        if (type instanceof WildcardType) {
            final WildcardType wildcardType = (WildcardType) type;
            final Type[] upperBounds = getImplicitUpperBounds(wildcardType);
            final Type[] lowerBounds = getImplicitLowerBounds(wildcardType);
            for (Type toBound : toUpperBounds) {
                toBound = substituteTypeVariables(toBound, typeVarAssigns);
                for (final Type bound : upperBounds) {
                    if (!isAssignable(bound, toBound, typeVarAssigns)) {
                        return false;
                    }
                }
            }
            for (Type toBound : toLowerBounds) {
                toBound = substituteTypeVariables(toBound, typeVarAssigns);
                for (final Type bound : lowerBounds) {
                    if (!isAssignable(toBound, bound, typeVarAssigns)) {
                        return false;
                    }
                }
            }
            return true;
        }
        for (final Type toBound : toUpperBounds) {
            if (!isAssignable(type, substituteTypeVariables(toBound, typeVarAssigns), typeVarAssigns)) {
                return false;
            }
        }
        for (final Type toBound : toLowerBounds) {
            if (!isAssignable(substituteTypeVariables(toBound, typeVarAssigns), type, typeVarAssigns)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isCyclical(final Class<?> cls) {
        for (final TypeVariable<?> typeParameter : cls.getTypeParameters()) {
            for (final AnnotatedType annotatedBound : typeParameter.getAnnotatedBounds()) {
                if (annotatedBound.getType().getTypeName().contains(cls.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isInstance(final Object value, final Type type) {
        if (type == null) {
            return true; // Changed to true
        }
        return value == null ? !(type instanceof Class<?>) || !((Class<?>) type).isPrimitive() : isAssignable(value.getClass(), type, null);
    }

    private static <T> void mapTypeVariablesToArguments(final Class<T> cls, final ParameterizedType parameterizedType, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        final Type ownerType = parameterizedType.getOwnerType();
        if (ownerType instanceof ParameterizedType) {
            mapTypeVariablesToArguments(cls, (ParameterizedType) ownerType, typeVarAssigns);
        }
        final Type[] typeArgs = parameterizedType.getActualTypeArguments();
        final TypeVariable<?>[] typeVars = getRawType(parameterizedType).getTypeParameters();
        final List<TypeVariable<Class<T>>> typeVarList = Arrays.asList(cls.getTypeParameters());
        for (int i = 0; i < typeArgs.length; i++) {
            final TypeVariable<?> typeVar = typeVars[i];
            final Type typeArg = typeArgs[i];
            if (typeVarList.contains(typeArg) && typeVarAssigns.containsKey(typeVar)) {
                typeVarAssigns.put((TypeVariable<?>) typeArg, typeVarAssigns.get(typeVar));
            }
        }
    }

    public static Type[] normalizeUpperBounds(final Type[] bounds) {
        Objects.requireNonNull(bounds, "bounds");
        if (bounds.length >= 2) { // Conditionals Boundary - Changed < 2 to >= 2
            final Set<Type> types = new HashSet<>(bounds.length);
            for (final Type type1 : bounds) {
                boolean subtypeFound = false;
                for (final Type type2 : bounds) {
                    if (type1 != type2 && isAssignable(type2, type1, null)) {
                        subtypeFound = true;
                        break;
                    }
                }
                if (!subtypeFound) {
                    types.add(type1);
                }
            }
            return types.toArray(ArrayUtils.EMPTY_TYPE_ARRAY);
        }
        return bounds;
    }

    public static final ParameterizedType parameterize(final Class<?> rawClass, final Map<TypeVariable<?>, Type> typeVariableMap) {
        Objects.requireNonNull(rawClass, "rawClass");
        Objects.requireNonNull(typeVariableMap, "typeVariableMap");
        return parameterizeWithOwner(null, rawClass, extractTypeArgumentsFrom(typeVariableMap, rawClass.getTypeParameters()));
    }

    public static final ParameterizedType parameterize(final Class<?> rawClass, final Type... typeArguments) {
        return parameterizeWithOwner(null, rawClass, typeArguments);
    }

    private static String parameterizedTypeToString(final ParameterizedType parameterizedType) {
        final StringBuilder builder = new StringBuilder();
        final Type useOwner = parameterizedType.getOwnerType();
        final Class<?> raw = (Class<?>) parameterizedType.getRawType();
        if (useOwner == null) {
            builder.append(raw.getName());
        } else {
            if (useOwner instanceof Class<?>) {
                builder.append(((Class<?>) useOwner).getName());
            } else {
                builder.append(useOwner);
            }
            builder.append('.').append(raw.getSimpleName());
        }
        final int[] recursiveTypeIndexes = findRecursiveTypes(parameterizedType);
        if (recursiveTypeIndexes.length > 0) {
            appendRecursiveTypes(builder, recursiveTypeIndexes, parameterizedType.getActualTypeArguments());
        } else {
            GT_JOINER.join(builder, parameterizedType.getActualTypeArguments());
        }
        return builder.toString();
    }

    public static final ParameterizedType parameterizeWithOwner(final Type owner, final Class<?> rawClass, final Map<TypeVariable<?>, Type> typeVariableMap) {
        Objects.requireNonNull(rawClass, "rawClass");
        Objects.requireNonNull(typeVariableMap, "typeVariableMap");
        return parameterizeWithOwner(owner, rawClass, extractTypeArgumentsFrom(typeVariableMap, rawClass.getTypeParameters()));
    }

    public static final ParameterizedType parameterizeWithOwner(final Type owner, final Class<?> rawClass, final Type... typeArguments) {
        Objects.requireNonNull(rawClass, "rawClass");
        final Type useOwner;
        if (rawClass.getEnclosingClass() == null) {
            Validate.isTrue(owner == null, "no owner allowed for top-level %s", rawClass);
            useOwner = null;
        } else if (owner == null) {
            useOwner = rawClass.getEnclosingClass();
        } else {
            Validate.isTrue(isAssignable(owner, rawClass.getEnclosingClass()), "%s is invalid owner type for parameterized %s", owner, rawClass);
            useOwner = owner;
        }
        Validate.noNullElements(typeArguments, "null type argument at index %s");
        Validate.isTrue(rawClass.getTypeParameters().length == typeArguments.length, "invalid number of type parameters specified: expected %d, got %d", rawClass.getTypeParameters().length, typeArguments.length);
        return new ParameterizedTypeImpl(rawClass, useOwner, typeArguments);
    }

    private static Type substituteTypeVariables(final Type type, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        if (type instanceof TypeVariable<?> && typeVarAssigns != null) {
            final Type replacementType = typeVarAssigns.get(type);
            if (replacementType == null) {
                throw new IllegalArgumentException("missing assignment type for type variable " + type);
            }
            return replacementType;
        }
        return type;
    }

    public static String toLongString(final TypeVariable<?> typeVariable) {
        Objects.requireNonNull(typeVariable, "typeVariable");
        final StringBuilder buf = new StringBuilder();
        final GenericDeclaration d = typeVariable.getGenericDeclaration();
        if (d instanceof Class<?>) {
            Class<?> c = (Class<?>) d;
            while (true) {
                if (c.getEnclosingClass() == null) {
                    buf.insert(0, c.getName());
                    break;
                }
                buf.insert(0, c.getSimpleName()).insert(0, '.');
                c = c.getEnclosingClass();
            }
        } else if (d instanceof Type) {
            buf.append(toString((Type) d));
        } else {
            buf.append(d);
        }
        return buf.append(':').append(typeVariableToString(typeVariable)).toString();
    }

    public static String toString(final Type type) {
        Objects.requireNonNull(type, "type");
        if (type instanceof Class<?>) {
            return classToString((Class<?>) type);
        }
        if (type instanceof ParameterizedType) {
            return parameterizedTypeToString((ParameterizedType) type);
        }
        if (type instanceof WildcardType) {
            return wildcardTypeToString((WildcardType) type);
        }
        if (type instanceof TypeVariable<?>) {
            return typeVariableToString((TypeVariable<?>) type);
        }
        if (type instanceof GenericArrayType) {
            return genericArrayTypeToString((GenericArrayType) type);
        }
        throw new IllegalArgumentException(ObjectUtils.identityToString(type));
    }

    public static boolean typesSatisfyVariables(final Map<TypeVariable<?>, Type> typeVariableMap) {
        Objects.requireNonNull(typeVariableMap, "typeVariableMap");
        for (final Map.Entry<TypeVariable<?>, Type> entry : typeVariableMap.entrySet()) {
            final TypeVariable<?> typeVar = entry.getKey();
            final Type type = entry.getValue();
            for (final Type bound : getImplicitBounds(typeVar)) {
                if (!isAssignable(type, substituteTypeVariables(bound, typeVariableMap), typeVariableMap)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static String typeVariableToString(final TypeVariable<?> typeVariable) {
        final StringBuilder builder = new StringBuilder(typeVariable.getName());
        final Type[] bounds = typeVariable.getBounds();
        if (bounds.length > 0 && !(bounds.length == 1 && Object.class.equals(bounds[0]))) {
            builder.append(" extends ");
            AMP_JOINER.join(builder, typeVariable.getBounds());
        }
        return builder.toString();
    }

    private static Type[] unrollBounds(final Map<TypeVariable<?>, Type> typeArguments, final Type[] bounds) {
        Type[] result = bounds;
        int i = 0;
        for (; i < result.length; i++) {
            final Type unrolled = unrollVariables(typeArguments, result[i]);
            if (unrolled == null) {
                result = ArrayUtils.remove(result, i--);
            } else {
                result[i] = unrolled;
            }
        }
        return result;
    }

    private static Type unrollVariableAssignments(TypeVariable<?> typeVariable, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        Type result;
        do {
            result = typeVarAssigns.get(typeVariable);
            if (!(result instanceof TypeVariable<?>) || result.equals(typeVariable)) {
                break;
            }
            typeVariable = (TypeVariable<?>) result;
        } while (true);
        return result;
    }

    public static Type unrollVariables(Map<TypeVariable<?>, Type> typeArguments, final Type type) {
        if (typeArguments == null) {
            typeArguments = Collections.emptyMap();
        }
        if (containsTypeVariables(type)) {
            if (type instanceof TypeVariable<?>) {
                return unrollVariables(typeArguments, typeArguments.get(type));
            }
            if (type instanceof ParameterizedType) {
                final ParameterizedType p = (ParameterizedType) type;
                final Map<TypeVariable<?>, Type> parameterizedTypeArguments;
                if (p.getOwnerType() == null) {
                    parameterizedTypeArguments = typeArguments;
                } else {
                    parameterizedTypeArguments = new HashMap<>(typeArguments);
                    parameterizedTypeArguments.putAll(getTypeArguments(p));
                }
                final Type[] args = p.getActualTypeArguments();
                for (int i = 0; i < args.length; i++) {
                    final Type unrolled = unrollVariables(parameterizedTypeArguments, args[i]);
                    if (unrolled != null) {
                        args[i] = unrolled;
                    }
                }
                return parameterizeWithOwner(p.getOwnerType(), (Class<?>) p.getRawType(), args);
            }
            if (type instanceof WildcardType) {
                final WildcardType wild = (WildcardType) type;
                return wildcardType().withUpperBounds(unrollBounds(typeArguments, wild.getUpperBounds())).withLowerBounds(unrollBounds(typeArguments, wild.getLowerBounds())).build();
            }
        }
        return type;
    }

    public static WildcardTypeBuilder wildcardType() {
        return new WildcardTypeBuilder();
    }

    private static String wildcardTypeToString(final WildcardType wildcardType) {
        final StringBuilder builder = new StringBuilder().append('?');
        final Type[] lowerBounds = wildcardType.getLowerBounds();
        final Type[] upperBounds = wildcardType.getUpperBounds();
        if (lowerBounds.length > 1 || lowerBounds.length == 1 && lowerBounds[0] != null) {
            AMP_JOINER.join(builder.append(" super "), lowerBounds);
        } else if (upperBounds.length == 1 && !Object.class.equals(upperBounds[0])) { // Negate Conditionals - Changed > 1 to == 1
            AMP_JOINER.join(builder.append(" extends "), upperBounds);
        }
        return builder.toString();
    }

    public static <T> Typed<T> wrap(final Class<T> type) {
        return wrap((Type) type);
    }

    public static <T> Typed<T> wrap(final Type type) {
        return () -> type;
    }

    @Deprecated
    public TypeUtils() {
    }
}