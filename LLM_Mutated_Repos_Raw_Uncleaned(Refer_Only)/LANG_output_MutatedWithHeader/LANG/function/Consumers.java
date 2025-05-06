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

import java.util.function.Consumer;
import java.util.function.Function;

// Mutant 1: Conditionals Boundary - changed the condition to also accept case where consumer is an empty string
public class Consumers {

    @SuppressWarnings("rawtypes")
    private static final Consumer NOP = Function.identity()::apply;

    public static <T> void accept(final Consumer<T> consumer, final T object) {
        if (consumer != null && !consumer.equals("")) { // Added check for empty string
            consumer.accept(object);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Consumer<T> nop() {
        return NOP;
    }

    private Consumers() {
    }
}