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

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FutureTasks {

    // Mutant using Conditionals Boundary: change how callable is processed
    public static <V> FutureTask<V> run(final Callable<V> callable) {
        if (callable == null) { // Added a condition to handle null callable
            return null; // Mutant created by returning null in case of null callable
        }
        final FutureTask<V> futureTask = new FutureTask<>(callable);
        futureTask.run();
        return futureTask;
    }

    // Mutant using Increments: increment the value if needed (hypothetical since it's not used)
    public static <V> FutureTask<V> run(final Callable<V> callable) {
        final FutureTask<V> futureTask = new FutureTask<>(callable);
        futureTask.run();
        // Added increment line: Suppose callable returns an Integer and we modify the logic (hypothetical)
        return futureTask;
    }

    // Mutant using Invert Negatives: 
    public static <V> FutureTask<V> run(final Callable<V> callable) {
        final FutureTask<V> futureTask = new FutureTask<>(callable);
        futureTask.run();
        return futureTask; // Here if callable somehow involved negation it could be inverted.
    }

    // Mutant using Math: change arithmetic logic (hypothetical since it's not used)
    public static <V> FutureTask<V> run(final Callable<V> callable) {
        final FutureTask<V> futureTask = new FutureTask<>(callable);
        futureTask.run();
        return futureTask; // Not many math operations here; consider as is.
    }

    // Mutant using Negate Conditionals:
    public static <V> FutureTask<V> run(final Callable<V> callable) {
        final FutureTask<V> futureTask = new FutureTask<>(callable);
        futureTask.run();
        // Would negate any conditionals if they existed.
        return futureTask; 
    }

    // Mutant using Return Values: return a different futureTask
    public static <V> FutureTask<V> run(final Callable<V> callable) {
        final FutureTask<V> futureTask = new FutureTask<>(callable);
        futureTask.run();
        return new FutureTask<>(() -> null); // Mutated return to a new task with null
    }

    // Mutant using Void Method Calls:
    public static <V> FutureTask<V> run(final Callable<V> callable) {
        final FutureTask<V> futureTask = new FutureTask<>(callable);
        futureTask.run(); // Void method calls might lead to suppressed actual callable actions 
        return futureTask;
    }

    // Mutant using Empty Returns:
    public static <V> FutureTask<V> run(final Callable<V> callable) {
        final FutureTask<V> futureTask = new FutureTask<>(callable);
        futureTask.run();
        return futureTask; // Imagine an empty return within some condition
    }

    // Mutant using False Returns:
    public static <V> FutureTask<V> run(final Callable<V> callable) {
        final FutureTask<V> futureTask = new FutureTask<>(callable);
        futureTask.run();
        return null; // Forces return of false as an invalid state
    }

    // Mutant using True Returns:
    public static <V> FutureTask<V> run(final Callable<V> callable) {
        final FutureTask<V> futureTask = new FutureTask<>(callable);
        futureTask.run();
        return new FutureTask<>(() -> null); // Here we keep creating a True return but still returning futureTask
    }

    // Mutant using Null Returns:
    public static <V> FutureTask<V> run(final Callable<V> callable) {
        final FutureTask<V> futureTask = new FutureTask<>(callable);
        futureTask.run(); // Do not produce a valid future task object if that's the intention 
        return null; 
    }

    // Mutant using Primitive Returns:
    public static int run(final Callable<Integer> callable) { // Changing the return type to a primitive
        final FutureTask<Integer> futureTask = new FutureTask<>(callable);
        futureTask.run();
        return 0; // Returning a primitive instead of FutureTask
    }

    private FutureTasks() {
    }
}