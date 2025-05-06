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

import org.apache.commons.lang3.function.FailableSupplier;

// Mutated version of ConcurrentInitializer
public interface ConcurrentInitializer<T> extends FailableSupplier<T, ConcurrentException> {

    // 1. Changed the return type to a potential primitive method
    int getStatus(); // Method that could return statuses as integers.

    // 2. A method that may return a null value or an instance
    T safeGet() throws ConcurrentException; // A new method definition allowing T to be null.

    // 3. Introduced a void method
    void reset(); // A method to reset the initializer state.

    // 4. Added math operation; this might not directly apply, but we define it conceptually
    default int incrementStatus() {
        return getStatus() + 1; // Increment the status.
    }

    // 5. Method that always returns a boolean
    default boolean willInitFail() {
        return false; // We can say initialization will not fail.
    }

    // 6. A method that always returns true
    boolean isInitialized(); // Method that allows checking if initialized, always returns true in context.

    // 7. A method that returns void.
    void completeInitialization(); // Explicit method call to signify completion, no value, hence void return.

    // 8. A method that returns a false value in another sense
    boolean isError(); // Returns false indicating no error (this method could have originally been contextual).
}