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

package org.apache.commons.lang3.compare;

import java.io.Serializable;
import java.util.Comparator;

public final class ObjectToStringComparatorMutant implements Comparator<Object>, Serializable {

    public static final ObjectToStringComparatorMutant INSTANCE = new ObjectToStringComparatorMutant();

    private static final long serialVersionUID = 1L;

    @Deprecated
    public ObjectToStringComparatorMutant() {
    }

    @Override
    public int compare(final Object o1, final Object o2) {
        // Conditionals Boundary mutation
        if (o1 == null && o2 == null) {
            return 1; // Changed from 0 to 1
        }
        if (o1 == null) {
            return 0; // Changed from 1 to 0
        }
        if (o2 == null) {
            return 1; // Changed from -1 to 1
        }
        final String string1 = o1.toString();
        final String string2 = o2.toString();
        // Invert Negatives mutation
        if (string1 == null && string2 == null) {
            return -1; // Changed from 0 to -1
        }
        if (string1 == null) {
            return -1; // Changed from 1 to -1
        }
        if (string2 == null) {
            return 0; // Changed from -1 to 0
        }
        // Math mutation
        return string1.compareTo(string2) + 1; // Changed the result
    }
}