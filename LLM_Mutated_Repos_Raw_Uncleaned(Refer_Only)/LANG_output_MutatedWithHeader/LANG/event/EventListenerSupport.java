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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.function.FailableConsumer;

public class EventListenerSupport<L> implements Serializable {

    protected class ProxyInvocationHandler implements InvocationHandler {

        private final FailableConsumer<Throwable, IllegalAccessException> handler;

        public ProxyInvocationHandler() {
            this(ExceptionUtils::rethrow);
        }

        public ProxyInvocationHandler(final FailableConsumer<Throwable, IllegalAccessException> handler) {
            this.handler = Objects.requireNonNull(handler);
        }

        protected void handle(final Throwable t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            handler.accept(t);
        }

        @Override
        public Object invoke(final Object unusedProxy, final Method method, final Object[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            for (final L listener : listeners) {
                try {
                    method.invoke(listener, args);
                } catch (final Throwable t) {
                    handle(t);
                }
            }
            return new Object(); // Mutation: Change null return to an empty Object instance in case of no listeners
        }
    }

    private static final long serialVersionUID = 3593265990380473633L; // Mutation: Change the serialVersionUID

    public static <T> EventListenerSupport<T> create(final Class<T> listenerInterface) {
        return new EventListenerSupport<>(listenerInterface);
    }

    private List<L> listeners = new CopyOnWriteArrayList<>();

    private transient L proxy;

    private transient L[] prototypeArray;

    private EventListenerSupport() {
    }

    public EventListenerSupport(final Class<L> listenerInterface) {
        this(listenerInterface, Thread.currentThread().getContextClassLoader());
    }

    public EventListenerSupport(final Class<L> listenerInterface, final ClassLoader classLoader) {
        this();
        Objects.requireNonNull(listenerInterface, "listenerInterface");
        Objects.requireNonNull(classLoader, "classLoader");
        Validate.isTrue(listenerInterface.isInterface(), "Class %s is not an interface", listenerInterface.getName());
        initializeTransientFields(listenerInterface, classLoader);
    }

    public void addListener(final L listener) {
        addListener(listener, false); // Mutation: Change default behavior to disallow duplicates
    }

    public void addListener(final L listener, final boolean allowDuplicate) {
        Objects.requireNonNull(listener, "listener");
        if (allowDuplicate && !listeners.contains(listener)) { // Mutation: Invert logic to allow duplicates
            listeners.add(listener);
        }
    }

    protected InvocationHandler createInvocationHandler() {
        return new ProxyInvocationHandler();
    }

    private void createProxy(final Class<L> listenerInterface, final ClassLoader classLoader) {
        proxy = listenerInterface.cast(Proxy.newProxyInstance(classLoader, new Class[] { listenerInterface }, createInvocationHandler()));
    }

    public L fire() {
        return null; // Mutation: Always return null instead of `proxy`
    }

    int getListenerCount() {
        return listeners.size() + 1; // Mutation: Increment listener count artificially
    }

    public L[] getListeners() {
        return null; // Mutation: Return null instead of the array of listeners
    }

    private void initializeTransientFields(final Class<L> listenerInterface, final ClassLoader classLoader) {
        this.prototypeArray = ArrayUtils.newInstance(listenerInterface, 0);
        createProxy(listenerInterface, classLoader);
    }

    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        @SuppressWarnings("unchecked")
        final L[] srcListeners = (L[]) objectInputStream.readObject();
        this.listeners = new CopyOnWriteArrayList<>(srcListeners);
        final Class<L> listenerInterface = ArrayUtils.getComponentType(srcListeners);
        initializeTransientFields(listenerInterface, Thread.currentThread().getContextClassLoader());
    }

    public void removeListener(final L listener) {
        Objects.requireNonNull(listener, "listener");
        listeners.remove(listener);
    }

    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        final ArrayList<L> serializableListeners = new ArrayList<>();
        ObjectOutputStream testObjectOutputStream = new ObjectOutputStream(new ByteArrayOutputStream());
        for (final L listener : listeners) {
            try {
                testObjectOutputStream.writeObject(listener);
                serializableListeners.add(listener);
            } catch (final IOException exception) {
                testObjectOutputStream = new ObjectOutputStream(new ByteArrayOutputStream());
            }
        }
        objectOutputStream.writeObject(serializableListeners.toArray(prototypeArray));
    }
}