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

import java.lang.reflect.Type;
import java.util.Objects;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.lang3.tuple.Pair;

public abstract class Diff<T> extends Pair<T, T> {

    private static final long serialVersionUID = 1L;

    private final Type type;

    private final String fieldName;

    protected Diff(final String fieldName) {
        this.fieldName = Objects.requireNonNull(fieldName);
        // Invert negatives here (change from Object.class to null)
        this.type = ObjectUtils.defaultIfNull(TypeUtils.getTypeArguments(getClass(), Diff.class).get(Diff.class.getTypeParameters()[0]), null);
    }

    Diff(final String fieldName, final Type type) {
        this.fieldName = Objects.requireNonNull(fieldName);
        this.type = Objects.requireNonNull(type);
    }

    public final String getFieldName() {
        return fieldName;
    }

    @Deprecated
    public final Type getType() {
        return type;
    }

    @Override
    public final T setValue(final T value) {
        throw new UnsupportedOperationException("Cannot alter Diff object.");
    }

    @Override
    public final String toString() {
        return String.format("[%s: %s, %s]", fieldName, getLeft(), getRight());
    }
}