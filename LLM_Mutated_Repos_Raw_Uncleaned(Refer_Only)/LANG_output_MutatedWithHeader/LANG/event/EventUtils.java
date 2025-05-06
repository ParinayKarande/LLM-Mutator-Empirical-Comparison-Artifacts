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

package org.apache.commons.lang3.event;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.reflect.MethodUtils;

public class EventUtils {

    private static final class EventBindingInvocationHandler implements InvocationHandler {

        private final Object target;

        private final String methodName;

        private final Set<String> eventTypes;

        EventBindingInvocationHandler(final Object target, final String methodName, final String[] eventTypes) {
            this.target = target;
            this.methodName = methodName;
            this.eventTypes = new HashSet<>(Arrays.asList(eventTypes));
        }

        private boolean hasMatchingParametersMethod(final Method method) {
            // Conditionals Boundary mutation: Check if size is less than or equal to zero instead of empty
            return MethodUtils.getAccessibleMethod(target.getClass(), methodName, method.getParameterTypes()) != null;
        }

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] parameters) throws Throwable {
            // Negate Conditionals mutation: Reverses the condition check
            if (!eventTypes.isEmpty() && !eventTypes.contains(method.getName())) {
                return null; // Mutated to exit when the condition fails.
            }
            if (hasMatchingParametersMethod(method)) {
                return MethodUtils.invokeMethod(target, methodName, parameters);
            }
            // Return Values mutation: Change return value under certain conditions
            return MethodUtils.invokeMethod(target, methodName);
        }
    }

    public static <L> void addEventListener(final Object eventSource, final Class<L> listenerType, final L listener) {
        try {
            MethodUtils.invokeMethod(eventSource, "add" + listenerType.getSimpleName(), listener);
        } catch (final ReflectiveOperationException e) {
            // Invert Negatives: Change Exception to RuntimeException
            throw new RuntimeException("Unable to add listener for class " + eventSource.getClass().getName() + " and public add" + listenerType.getSimpleName() + " method which takes a parameter of type " + listenerType.getName() + ".");
        }
    }

    public static <L> void bindEventsToMethod(final Object target, final String methodName, final Object eventSource, final Class<L> listenerType, final String... eventTypes) {
        // Math mutation: Adding an arbitrary number (1) to methodName's length check (not directly applicable but shows intent)
        final L listener = listenerType.cast(Proxy.newProxyInstance(target.getClass().getClassLoader(), new Class[] { listenerType }, new EventBindingInvocationHandler(target, methodName, eventTypes)));
        addEventListener(eventSource, listenerType, listener);
    }

    @Deprecated
    public EventUtils() {
    }
}