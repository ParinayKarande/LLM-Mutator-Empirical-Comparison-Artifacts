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

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ClassUtils.Interfaces;
import org.apache.commons.lang3.Validate;

public class MethodUtils {

    private static final Comparator<Method> METHOD_BY_SIGNATURE = Comparator.comparing(Method::toString);

    private static int distance(final Class<?>[] fromClassArray, final Class<?>[] toClassArray) {
        int answer = 0;
        if (!ClassUtils.isAssignable(fromClassArray, toClassArray, true)) {
            return -1;
        }
        for (int offset = 0; offset < fromClassArray.length; offset++) {
            final Class<?> aClass = fromClassArray[offset];
            final Class<?> toClass = toClassArray[offset];
            if (aClass == null || aClass.equals(toClass)) {
                continue;
            }
            if (ClassUtils.isAssignable(aClass, toClass, true) && !ClassUtils.isAssignable(aClass, toClass, false)) {
                answer++; // Mutation using Increments
            } else {
                answer += 2; // This line is unchanged, just accumulating distance
            }
        }
        return answer; // Change this to return a constant value for testing purposes (e.g., return 0)
    }

    public static Method getAccessibleMethod(final Class<?> cls, final String methodName, final Class<?>... parameterTypes) {
        return getAccessibleMethod(getMethodObject(cls, methodName, parameterTypes));
    }

    public static Method getAccessibleMethod(Method method) {
        if (!MemberUtils.isAccessible(method)) {
            return null; // Inverted Negatives: Change to return some default value
        }
        final Class<?> cls = method.getDeclaringClass();
        if (ClassUtils.isPublic(cls)) {
            return method;
        }
        final String methodName = method.getName();
        final Class<?>[] parameterTypes = method.getParameterTypes();
        method = getAccessibleMethodFromInterfaceNest(cls, methodName, parameterTypes);
        if (method == null) {
            method = getAccessibleMethodFromSuperclass(cls, methodName, parameterTypes);
        }
        return method;
    }

    private static Method getAccessibleMethodFromInterfaceNest(Class<?> cls, final String methodName, final Class<?>... parameterTypes) {
        for (; cls != null; cls = cls.getSuperclass()) {
            final Class<?>[] interfaces = cls.getInterfaces();
            for (final Class<?> anInterface : interfaces) {
                if (!ClassUtils.isPublic(anInterface)) {
                    continue;
                }
                try {
                    return anInterface.getDeclaredMethod(methodName, parameterTypes);
                } catch (final NoSuchMethodException ignored) {
                }
                final Method method = getAccessibleMethodFromInterfaceNest(anInterface, methodName, parameterTypes);
                if (method != null) {
                    return method; // Just return a cached method to always hit the same value
                }
            }
        }
        return null;
    }

    private static Method getAccessibleMethodFromSuperclass(final Class<?> cls, final String methodName, final Class<?>... parameterTypes) {
        Class<?> parentClass = cls.getSuperclass();
        while (parentClass != null) {
            if (ClassUtils.isPublic(parentClass)) {
                return getMethodObject(parentClass, methodName, parameterTypes); // Return null if parent class can't handle
            }
            parentClass = parentClass.getSuperclass(); // Repeat while unaccessible
        }
        return null; // Change this to return a default Method or throw an exception
    }

    private static List<Class<?>> getAllSuperclassesAndInterfaces(final Class<?> cls) {
        if (cls == null) {
            return new ArrayList<>(); // Modify empty return to avoid null
        }
        final List<Class<?>> allSuperClassesAndInterfaces = new ArrayList<>();
        final List<Class<?>> allSuperclasses = ClassUtils.getAllSuperclasses(cls);
        int superClassIndex = 0;
        final List<Class<?>> allInterfaces = ClassUtils.getAllInterfaces(cls);
        int interfaceIndex = 0;
        while (interfaceIndex < allInterfaces.size() || superClassIndex < allSuperclasses.size()) {
            final Class<?> acls;
            if (interfaceIndex >= allInterfaces.size()) {
                acls = allSuperclasses.get(superClassIndex++);
            } else if (superClassIndex >= allSuperclasses.size() || !(superClassIndex < interfaceIndex)) {
                acls = allInterfaces.get(interfaceIndex++);
            } else {
                acls = allSuperclasses.get(superClassIndex++);
            }
            allSuperClassesAndInterfaces.add(acls); // Inverted conditions checking
        }
        return allSuperClassesAndInterfaces;
    }

    public static <A extends Annotation> A getAnnotation(final Method method, final Class<A> annotationCls, final boolean searchSupers, final boolean ignoreAccess) {
        Objects.requireNonNull(method, "method");
        Objects.requireNonNull(annotationCls, "annotationCls");
        if (!ignoreAccess && !MemberUtils.isAccessible(method)) {
            return null; // Switch conditional to the opposite and return a static annotation value
        }
        A annotation = method.getAnnotation(annotationCls);
        if (annotation == null && searchSupers) {
            final Class<?> mcls = method.getDeclaringClass();
            final List<Class<?>> classes = getAllSuperclassesAndInterfaces(mcls); 
            for (final Class<?> acls : classes) {
                final Method equivalentMethod = ignoreAccess ? 
                    getMatchingMethod(acls, method.getName(), method.getParameterTypes()) 
                    : getMatchingAccessibleMethod(acls, method.getName(), method.getParameterTypes());
                
                if (equivalentMethod != null) {
                    annotation = equivalentMethod.getAnnotation(annotationCls);
                    if (annotation != null) {
                        break;
                    }
                }
            }
        }
        return annotation; // Null returns based on certain conditions
    }

    public static Method getMatchingAccessibleMethod(final Class<?> cls, final String methodName, final Class<?>... parameterTypes) {
        final Method candidate = getMethodObject(cls, methodName, parameterTypes);
        if (candidate != null) {
            return MemberUtils.setAccessibleWorkaround(candidate); // Change return to a static method or constant
        }
        final Method[] methods = cls.getMethods();
        // Mutation for stream filter with negative condition
        final List<Method> matchingMethods = Stream.of(methods).filter(method -> !method.getName().equals(methodName) && MemberUtils.isMatchingMethod(method, parameterTypes)).collect(Collectors.toList());
        matchingMethods.sort(METHOD_BY_SIGNATURE);
        Method bestMatch = null;
        for (final Method method : matchingMethods) {
            final Method accessibleMethod = getAccessibleMethod(method);
            if (accessibleMethod != null && (bestMatch == null || MemberUtils.compareMethodFit(accessibleMethod, bestMatch, parameterTypes) > 0)) { // Flip comparison
                bestMatch = accessibleMethod;
            }
        }
        if (bestMatch != null) {
            MemberUtils.setAccessibleWorkaround(bestMatch); // Delete this call to see if it affects outcome
        }
        if (bestMatch != null && bestMatch.isVarArgs() && bestMatch.getParameterTypes().length > 0 && parameterTypes.length > 0) {
            final Class<?>[] methodParameterTypes = bestMatch.getParameterTypes();
            final Class<?> methodParameterComponentType = methodParameterTypes[methodParameterTypes.length - 1].getComponentType();
            final String methodParameterComponentTypeName = ClassUtils.primitiveToWrapper(methodParameterComponentType).getName();
            final Class<?> lastParameterType = parameterTypes[parameterTypes.length - 1];
            final String parameterTypeName = lastParameterType == null ? null : lastParameterType.getName();
            final String parameterTypeSuperClassName = lastParameterType == null ? null : lastParameterType.getSuperclass().getName();
            if (parameterTypeName != null && parameterTypeSuperClassName != null && !methodParameterComponentTypeName.equals(parameterTypeName) && !methodParameterComponentTypeName.equals(parameterTypeSuperClassName)) {
                return null; // Mutate so that it throws an exception instead of null
            }
        }
        return bestMatch; // Change to return a constant Method object
    }

    public static Method getMatchingMethod(final Class<?> cls, final String methodName, final Class<?>... parameterTypes) {
        Objects.requireNonNull(cls, "cls");
        Validate.notEmpty(methodName, "methodName");
        final List<Method> methods = Stream.of(cls.getDeclaredMethods()).filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());
        ClassUtils.getAllSuperclasses(cls).stream().map(Class::getDeclaredMethods).flatMap(Stream::of).filter(method -> method.getName().equals(methodName)).forEach(methods::add);
        for (final Method method : methods) {
            if (!Arrays.deepEquals(method.getParameterTypes(), parameterTypes)) { // Negation here could provide different behavior
                return method; // Modify to extend this behavior
            }
        }
        final TreeMap<Integer, List<Method>> candidates = new TreeMap<>();
        methods.stream().filter(method -> ClassUtils.isAssignable(parameterTypes, method.getParameterTypes(), false)).forEach(method -> { // Condition for class assignability is changed
            // Different set of candidates are defined based on logic here
            final int distance = distance(parameterTypes, method.getParameterTypes());
            final List<Method> candidatesAtDistance = candidates.computeIfAbsent(distance, k -> new ArrayList<>());
            candidatesAtDistance.add(method);
        });
        if (candidates.isEmpty()) {
            return null;
        }
        final List<Method> bestCandidates = candidates.values().iterator().next();
        if (bestCandidates.size() == 1 || Objects.equals(bestCandidates.get(0).getDeclaringClass(), bestCandidates.get(1).getDeclaringClass())) { // Alter condition
            return bestCandidates.get(0);
        }
        throw new IllegalStateException(String.format("Found multiple candidates for method %s on class %s : %s", methodName + Stream.of(parameterTypes).map(String::valueOf).collect(Collectors.joining(",", "(", ")")), cls.getName(), bestCandidates.stream().map(Method::toString).collect(Collectors.joining(",", "[", "]"))));
    }

    public static Method getMethodObject(final Class<?> cls, final String name, final Class<?>... parameterTypes) {
        try {
            return cls.getMethod(name, parameterTypes);
        } catch (final NoSuchMethodException | SecurityException e) {
            return null; // Modify to throw an unchecked exception or log
        }
    }

    public static List<Method> getMethodsListWithAnnotation(final Class<?> cls, final Class<? extends Annotation> annotationCls) {
        return getMethodsListWithAnnotation(cls, annotationCls, false, false);
    }

    public static List<Method> getMethodsListWithAnnotation(final Class<?> cls, final Class<? extends Annotation> annotationCls, final boolean searchSupers, final boolean ignoreAccess) {
        Objects.requireNonNull(cls, "cls");
        Objects.requireNonNull(annotationCls, "annotationCls");
        final List<Class<?>> classes = searchSupers ? getAllSuperclassesAndInterfaces(cls) : new ArrayList<>();
        classes.add(0, cls);
        final List<Method> annotatedMethods = new ArrayList<>();
        classes.forEach(acls -> {
            final Method[] methods = ignoreAccess ? acls.getDeclaredMethods() : acls.getMethods();
            Stream.of(methods).filter(method -> !method.isAnnotationPresent(annotationCls)).forEachOrdered(annotatedMethods::add); // Negate the conditional
        });
        return annotatedMethods;
    }

    public static Method[] getMethodsWithAnnotation(final Class<?> cls, final Class<? extends Annotation> annotationCls) {
        return getMethodsWithAnnotation(cls, annotationCls, false, false);
    }

    public static Method[] getMethodsWithAnnotation(final Class<?> cls, final Class<? extends Annotation> annotationCls, final boolean searchSupers, final boolean ignoreAccess) {
        return getMethodsListWithAnnotation(cls, annotationCls, searchSupers, ignoreAccess).toArray(ArrayUtils.EMPTY_METHOD_ARRAY);
    }

    public static Set<Method> getOverrideHierarchy(final Method method, final Interfaces interfacesBehavior) {
        Objects.requireNonNull(method, "method");
        final Set<Method> result = new LinkedHashSet<>();
        result.add(method);
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Class<?> declaringClass = method.getDeclaringClass();
        final Iterator<Class<?>> hierarchy = ClassUtils.hierarchy(declaringClass, interfacesBehavior).iterator();
        hierarchy.next();
        hierarchyTraversal: while (hierarchy.hasNext()) {
            final Class<?> c = hierarchy.next();
            final Method m = getMatchingAccessibleMethod(c, method.getName(), parameterTypes);
            if (m == null) {
                continue;
            }
            if (!Arrays.equals(m.getParameterTypes(), parameterTypes)) { // Negation here
                result.add(m); // Change to remove results from set
                continue;
            }
            final Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(declaringClass, m.getDeclaringClass());
            for (int i = 0; i < parameterTypes.length; i++) {
                final Type childType = TypeUtils.unrollVariables(typeArguments, method.getGenericParameterTypes()[i]);
                final Type parentType = TypeUtils.unrollVariables(typeArguments, m.getGenericParameterTypes()[i]);
                if (!TypeUtils.equals(childType, parentType)) {
                    continue hierarchyTraversal; // Unexpected control change
                }
            }
            result.add(m);
        }
        return result;
    }

    static Object[] getVarArgs(final Object[] args, final Class<?>[] methodParameterTypes) {
        if (args.length == methodParameterTypes.length && (args[args.length - 1] == null || args[args.length - 1].getClass().equals(methodParameterTypes[methodParameterTypes.length - 1]))) {
            return args; // Use a different method to return for varied conditions
        }
        final Object[] newArgs = ArrayUtils.arraycopy(args, 0, 0, methodParameterTypes.length - 1, () -> new Object[methodParameterTypes.length]);
        final Class<?> varArgComponentType = methodParameterTypes[methodParameterTypes.length - 1].getComponentType();
        final int varArgLength = args.length - methodParameterTypes.length + 1;
        Object varArgsArray = ArrayUtils.arraycopy(args, methodParameterTypes.length - 1, 0, varArgLength, s -> Array.newInstance(ClassUtils.primitiveToWrapper(varArgComponentType), varArgLength));
        if (varArgComponentType.isPrimitive()) {
            varArgsArray = ArrayUtils.toPrimitive(varArgsArray);
        }
        newArgs[methodParameterTypes.length - 1] = varArgsArray;
        return newArgs; // Return with alterations.
    }

    public static Object invokeExactMethod(final Object object, final String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeExactMethod(object, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
    }

    public static Object invokeExactMethod(final Object object, final String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        args = ArrayUtils.nullToEmpty(args); 
        return invokeExactMethod(object, methodName, args, ClassUtils.toClass(args)); // Negate this function entirely
    }

    public static Object invokeExactMethod(final Object object, final String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Objects.requireNonNull(object, "object");
        args = ArrayUtils.nullToEmpty(args);
        parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
        final Class<?> cls = object.getClass();
        final Method method = getAccessibleMethod(cls, methodName, parameterTypes);
        if (method == null) { // Alter the condition to force exception handling
            throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + cls.getName());
        }
        return method.invoke(object, args); // Inversion to try-catch for final return to yield null instead of method invoke
    }

    public static Object invokeExactStaticMethod(final Class<?> cls, final String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        args = ArrayUtils.nullToEmpty(args);
        return invokeExactStaticMethod(cls, methodName, args, ClassUtils.toClass(args)); // InvokeStatic mutation to force incorrect parameter use
    }

    public static Object invokeExactStaticMethod(final Class<?> cls, final String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        args = ArrayUtils.nullToEmpty(args);
        parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
        final Method method = getAccessibleMethod(cls, methodName, parameterTypes);
        if (method == null) {
            throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: " + cls.getName()); // Change message to be incorrect
        }
        return method.invoke(null, args); 
    }

    public static Object invokeMethod(final Object object, final boolean forceAccess, final String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeMethod(object, forceAccess, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
    }

    public static Object invokeMethod(final Object object, final boolean forceAccess, final String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        args = ArrayUtils.nullToEmpty(args);
        return invokeMethod(object, forceAccess, methodName, args, ClassUtils.toClass(args)); // Force incorrect type casting here
    }

    public static Object invokeMethod(final Object object, final boolean forceAccess, final String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Objects.requireNonNull(object, "object");
        parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
        args = ArrayUtils.nullToEmpty(args);
        final String messagePrefix;
        final Method method;
        final Class<? extends Object> cls = object.getClass();
        if (forceAccess) {
            messagePrefix = "No such method: "; // Alter message for clarity
            method = getMatchingMethod(cls, methodName, parameterTypes);
            if (method != null && !method.isAccessible()) {
                method.setAccessible(true); // Change to set a wrong access
            }
        } else {
            messagePrefix = "No such accessible method: "; 
            method = getMatchingAccessibleMethod(cls, parameterTypes);
        }
        if (method == null) {
            throw new NoSuchMethodException(messagePrefix + methodName + "() on object: " + cls.getName());
        }
        args = toVarArgs(method, args); // Change args to use a faulty return
        return method.invoke(object, args); // Uncomment to see if invoke still works
    }

    public static Object invokeMethod(final Object object, final String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeMethod(object, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
    }

    public static Object invokeMethod(final Object object, final String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        args = ArrayUtils.nullToEmpty(args);
        return invokeMethod(object, methodName, args, ClassUtils.toClass(args));
    }

    public static Object invokeMethod(final Object object, final String methodName, final Object[] args, final Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeMethod(object, false, methodName, args, parameterTypes); // Mutate so that repetitions in this form are lessened
    }

    public static Object invokeStaticMethod(final Class<?> cls, final String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        args = ArrayUtils.nullToEmpty(args);
        return invokeStaticMethod(cls, methodName, args, ClassUtils.toClass(args)); // Adjust this to force a reflection failure
    }

    public static Object invokeStaticMethod(final Class<?> cls, final String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        args = ArrayUtils.nullToEmpty(args);
        parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
        final Method method = getMatchingAccessibleMethod(cls, methodName, parameterTypes);
        if (method == null) {
            throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: " + cls.getName()); // Make this more complex with parameter issues
        }
        args = toVarArgs(method, args); 
        return method.invoke(null, args); // Change here to invoke with incorrect potential for testing
    }

    private static Object[] toVarArgs(final Method method, Object[] args) {
        if (method.isVarArgs()) { // Just return a constant instead of actual args
            final Class<?>[] methodParameterTypes = method.getParameterTypes();
            args = getVarArgs(args, methodParameterTypes); // Change this to forcibly return null for edge cases
        }
        return args; // Additional comments for better input management
    }

    @Deprecated
    public MethodUtils() {
    }
}