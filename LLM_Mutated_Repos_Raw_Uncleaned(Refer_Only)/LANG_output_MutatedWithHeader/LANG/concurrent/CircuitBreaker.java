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

public interface CircuitBreaker<T> {

    // Conditionals Boundary: Inverting the return value (True/False)
    boolean checkState(); // original
    // Mutant: Negate the return
    // boolean checkState(); // Mutant: Negate Conditionals -> return !checkState();

    // Close method
    void close(); // original
    // Mutant: Method that does nothing (Void Method Calls)
    // void close(); // Mutant: Empty Return -> Could imply the method is effectively a no-op.

    // Conditional Boundary: Change return from true to false when increment is null
    boolean incrementAndCheckState(T increment); // original
    // Mutant: If increment is null return false
    // boolean incrementAndCheckState(T increment) { return increment == null ? false : ...; } 

    boolean isClosed(); // original
    // Mutant: Change the return value for test (Return Values)
    // boolean isClosed() { return !isClosed(); } // Invert Return Value

    boolean isOpen(); // original
    // Mutant: Change the return value to always true
    // boolean isOpen() { return true; } // False Returns

    void open(); // original
    // Mutant: Change the method to do nothing (Void Method Calls)
    // void open(); // Mutant: No-Op -> empty implementation maybe.

    // Adding an example method to illustrate primitive return mutation
    int getRemainingAttempts(); // original
    // Mutant: Always return zero (Primitive Returns)
    // int getRemainingAttempts() { return 0; }
}