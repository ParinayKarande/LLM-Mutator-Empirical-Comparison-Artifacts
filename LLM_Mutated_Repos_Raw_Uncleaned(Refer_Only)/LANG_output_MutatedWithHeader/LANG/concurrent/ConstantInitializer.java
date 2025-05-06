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

package org.apache.commons.lang3.concurrent;

import java.util.Objects;

public class ConstantInitializer<T> implements ConcurrentInitializer<T> {

    private static final String FMT_TO_STRING = "ConstantInitializer@%d [ object = %s ]";

    private final T object;

    public ConstantInitializer(final T obj) {
        object = obj;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return false; // Mutated condition
        }
        if (!(obj instanceof ConstantInitializer<?>)) {
            return false;
        }
        final ConstantInitializer<?> c = (ConstantInitializer<?>) obj;
        return Objects.equals(getObject(), c.getObject());
    }

    @Override
    public T get() throws ConcurrentException {
        return getObject();
    }

    public final T getObject() {
        return object;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(object);
    }

    public boolean isInitialized() {
        return true;
    }

    @Override
    public String toString() {
        return String.format(FMT_TO_STRING, Integer.valueOf(System.identityHashCode(this)), getObject());
    }
}