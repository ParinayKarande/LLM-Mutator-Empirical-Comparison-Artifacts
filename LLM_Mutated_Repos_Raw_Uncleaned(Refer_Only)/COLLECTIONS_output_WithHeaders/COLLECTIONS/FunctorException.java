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

package org.apache.commons.collections4;

public class FunctorException extends RuntimeException {

    private static final long serialVersionUID = -4704772662059351193L;

    // Mutation: Void Method Calls - Added a method that does nothing, for mutation
    public void doNothing() {
        // this method intentionally left blank
    }

    public FunctorException() {
        // Mutation: Empty Returns - Modified constructor to do nothing
        return;
    }

    // Mutation: Change to alternate constructor with Negated condition (practically not applicable here but implied as a mutation)
    public FunctorException(final String msg) {
        super(msg == null ? "Default Message" : msg); // Negate the default msg condition
    }

    // Mutation: Change to alternate constructor with False Returns
    public FunctorException(final String msg, final Throwable rootCause) {
        super(msg, rootCause == null ? new RuntimeException("Null root cause") : rootCause); // Change to supply default exception
    }

    // Mutation: Unknown case for negating condition in constructor
    public FunctorException(final Throwable rootCause) {
        super(rootCause == null ? new NullPointerException("Root cause must not be null") : rootCause); // Null Returns component
    }
    
    // Mutation: Increments - Not applicable, as there are no primitive numeric operations in the class
}