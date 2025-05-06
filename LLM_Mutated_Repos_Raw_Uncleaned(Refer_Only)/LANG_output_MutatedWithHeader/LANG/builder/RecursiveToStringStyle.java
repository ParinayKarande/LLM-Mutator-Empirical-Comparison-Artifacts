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

import java.util.Collection;
import org.apache.commons.lang3.ClassUtils;

public class RecursiveToStringStyleMutant extends ToStringStyle {

    private static final long serialVersionUID = 1L;

    public RecursiveToStringStyleMutant() {
    }

    protected boolean accept(final Class<?> clazz) {
        // Negate the condition
        return false; // Changed from true to false
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final Collection<?> coll) {
        appendClassName(buffer, coll);
        appendIdentityHashCode(buffer, coll);
        // Invert argument order (conditionals boundary)
        appendDetail(buffer, coll.toArray(), fieldName); // changed order of parameters
    }

    @Override
    public void appendDetail(final StringBuffer buffer, final String fieldName, final Object value) {
        // Inverting negative condition
        if (ClassUtils.isPrimitiveWrapper(value.getClass()) || String.class.equals(value.getClass()) || !accept(value.getClass())) { 
            buffer.append(ReflectionToStringBuilder.toString(value, this));
        } else {
            super.appendDetail(buffer, fieldName, value);
        }
    }

    // Example of a method with empty return
    public void exampleVoidMethod() {
        return; // Added empty return
    }

    // Changing return type to primitive
    public int getSomeValue() {
        return 1; // From void to return an integer
    }

    // Example addition of a false return
    protected boolean anotherMethod() {
        return false; // Changed from a dynamic condition to always false
    }

    // Another example for Primitive Returns
    public boolean isAcceptable(Object obj) {
        // Primitive return modified to always return false
        return false; // intended to evaluate if 'obj' is acceptable 
    }
}