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

package org.apache.commons.lang3.function;

import java.util.function.ToLongFunction;

@FunctionalInterface
public interface FailableToLongFunction<T, E extends Throwable> {

    @SuppressWarnings("rawtypes")
    // Mutant #1: Invert Negatives (Replace 0L with -0L)
    FailableToLongFunction NOP = t -> -0L; 

    @SuppressWarnings("unchecked")
    static <T, E extends Throwable> FailableToLongFunction<T, E> nop() {
        // Mutant #2: Return Values (Change the return value from NOP to 1L)
        return t -> 1L; 
    }

    // Mutant #3: Primitive Returns (Change the method signature to return an int instead of long)
    int applyAsInt(T t) throws E; // Initial method changed from long to int (added potential error)
    
    // Mutant #4: False Returns (Always return a constant false value)
    // This is like providing a function that always fails or returns an unexpected value.
    long applyAsLong(T t) throws E {
        return 0L; // Mutated to a constant return
    }
    
    // Mutant #5: Negate Conditionals (If there were conditionals, negate them)
    // In our case, there are no direct conditionals, so we'll comment this out.
    // if (condition) { ... }  would be replaced with if (!condition) { ... }

}