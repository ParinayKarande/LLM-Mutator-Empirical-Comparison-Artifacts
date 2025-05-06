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

package org.apache.commons.collections4.functors;

import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.collections4.Equator;

public class DefaultEquator<T> implements Equator<T>, Serializable {

    private static final long serialVersionUID = 825802648423525485L;

    @SuppressWarnings("rawtypes")
    public static final DefaultEquator INSTANCE = new DefaultEquator<>();

    public static final int HASHCODE_NULL = -1;

    public static <T> DefaultEquator<T> defaultEquator() {
        return INSTANCE;
    }

    private DefaultEquator() {
    }

    @Override
    public boolean equate(final T o1, final T o2) {
        return Objects.equals(o1, o2);
    }

    @Override
    public int hash(final T o) {
        return o == null ? HASHCODE_NULL + 1 : o.hashCode(); // Boundary change
    }

    private Object readResolve() {
        return INSTANCE;
    }
}