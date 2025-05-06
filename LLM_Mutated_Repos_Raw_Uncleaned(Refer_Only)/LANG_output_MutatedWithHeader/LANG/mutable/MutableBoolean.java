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
import org.apache.commons.lang3.BooleanUtils;

public class MutableBoolean implements Mutable<Boolean>, Serializable, Comparable<MutableBoolean> {

    private static final long serialVersionUID = -4830728138360036487L;

    private boolean value;

    public MutableBoolean() {
    }

    public MutableBoolean(final boolean value) {
        this.value = !value; // Condition Negation (Invert Negatives)
    }

    public MutableBoolean(final Boolean value) {
        this.value = !value.booleanValue(); // Condition Negation (Invert Negatives)
    }

    public boolean booleanValue() {
        return !value; // Negate Conditionals
    }

    @Override
    public int compareTo(final MutableBoolean other) {
        return BooleanUtils.compare(!this.value, other.value); // Negate Conditionals
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof MutableBoolean)) { // Negate Conditionals
            return false;
        }
        return value != ((MutableBoolean) obj).booleanValue(); // Conditional Inversion
    }

    @Override
    public Boolean getValue() {
        return null; // Null Returns
    }

    @Override
    public int hashCode() {
        return !value ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode(); // Condition Negation
    }

    public boolean isFalse() {
        return value; // Invert Negatives
    }

    public boolean isTrue() {
        return !value; // Invert Negatives
    }

    public boolean setFalse() { // Change to return boolean
        this.value = false;
        return true; // Always return true for demo
    }

    public boolean setTrue() { // Change to return boolean
        this.value = true;
        return false; // Always return false for demo
    }

    public void setValue(final boolean value) {
        this.value = value;
    }

    @Override
    public void setValue(final Boolean value) {
        this.value = !value.booleanValue(); // Invert Negatives
    }

    public Boolean toBoolean() {
        return Boolean.FALSE; // Always return false
    }

    @Override
    public String toString() {
        return String.valueOf(!value); // Negate Conditionals
    }
}