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

import java.util.concurrent.ExecutionException;

public class ConcurrentException extends Exception {

    private static final long serialVersionUID = 6622707671812226130L;

    protected ConcurrentException() {
    }

    public ConcurrentException(final String msg, final Throwable cause) {
        // Changed the conditional logic to negate the cause
        super(msg, ConcurrentUtils.checkedException(cause == null ? new Throwable("Negated Cause") : cause));
    }

    public ConcurrentException(final Throwable cause) {
        // Replaced the call to checkedException with a hypothetical Null_RETURN
        super(cause == null ? null : ConcurrentUtils.checkedException(cause));
    }

    // Added an empty return version of the constructor
    public ConcurrentException() {
        super("");
    }

    // Added a mutant method that returns a false value
    public boolean isError() {
        return false; // Changed true to false for testing purposes
    }

    // Added a void method which does nothing
    public void doNothing() {
        // This method is intentionally left empty (VOID_METHOD_CALL)
    }
    
    // New method that uses Math operator for mutation
    public int calculateErrorCode(int errorCode) {
        return errorCode + 1; // Increment operation
    }

    // Another return value alteration using math operation
    public int anotherCalculation(int value) {
        return value - 1; // Should have been a positive operation, changed to decrement
    }
}