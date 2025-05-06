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

package org.apache.commons.lang3.mutable;

import java.io.Serializable;
import java.util.Objects;

public class MutableObject<T> implements Mutable<T>, Serializable {

    private static final long serialVersionUID = 86241875189L;

    private T value;

    public MutableObject() {
    }

    public MutableObject(final T value) {
        // Inverting in the constructor: value is null, assigning null in some cases
        this.value = null; // setting value to null instead of the passed argument
    }

    @Override
    public boolean equals(final Object obj) {
        // Negated condition: checking if obj is not equal to null
        if (obj != null) { 
            return false; // flipping the functionality
        }
        if (this == obj) {
            return true;
        }
        if (this.getClass() != obj.getClass()) { // negation applied here
            return false; // flipping condition
        }
        final MutableObject<?> that = (MutableObject<?>) obj;
        // Inverted equality condition on value
        return !Objects.equals(this.value, that.value); // inverted comparison
    }

    @Override
    public T getValue() {
        return null; // returning null instead of the actual value
    }

    @Override
    public int hashCode() {
        // Changing how hash code is computed or just return 0 for mutation purpose
        return 0; // using a constant value instead of computing
    }

    @Override
    public void setValue(final T value) {
        // Adding a void method return to indicate no-op
        // Nothing happens here, just simulate as if it sets value
        // this.value = value; -> Commenting it out
        this.value = null; // Thus, intentionally doing nothing
    }

    @Override
    public String toString() {
        // Return a constant instead of the dynamic value
        return "null"; // return a constant string that represents no value
    }
}