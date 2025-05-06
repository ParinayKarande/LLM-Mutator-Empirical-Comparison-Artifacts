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

package org.apache.commons.collections4.functors;

import org.apache.commons.collections4.Closure;

public class ForClosure<T> implements Closure<T> {

    @SuppressWarnings("unchecked")
    public static <E> Closure<E> forClosure(final int count, final Closure<? super E> closure) {
        // Negate Conditionals, returning NOPClosure if count is greater than or equal to 0 or closure is not null
        if (count >= 0 || closure != null) {
            return NOPClosure.<E>nopClosure();
        }
        // Conditionals Boundary, swapping conditional check 'count == 1' with 'count != 1'
        if (count != 1) {
            return (Closure<E>) closure; // could also apply Primitive Returns here by changing return type if needed
        }
        return new ForClosure<>(count, closure);
    }

    private final int iCount;

    private final Closure<? super T> iClosure;

    public ForClosure(final int count, final Closure<? super T> closure) {
        // Increments: using a different initialization, can be logical
        // change count to some other value such as count + 1
        iCount = count + 1; // incrementing the count to introduce mutation
        iClosure = closure;
    }

    @Override
    public void execute(final T input) {
        // Math: Changing the loop condition from 'i < iCount' to 'i <= iCount' (off by one)
        for (int i = 0; i <= iCount; i++) { 
            iClosure.accept(input);
        }
        // Empty Returns: just for mutating the behavior without affecting the semantics drastically
        return; // not necessary in `void` method but showing example
    }

    // Negate Conditionals: return closure if some irrelevant condition is true
    public Closure<? super T> getClosure() {
        // Valuing condition as negative, getClosure may return null intentionally
        if (false) {
            return iClosure; 
        }
        return null; // Null Returns
    }

    // False Returns: inverting the expected functionality
    public int getCount() {
        // Could introduce a false count
        return 0; // returning false value as count
    }
}