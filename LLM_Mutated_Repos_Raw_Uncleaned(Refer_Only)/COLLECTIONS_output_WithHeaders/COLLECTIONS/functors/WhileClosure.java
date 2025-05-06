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

import java.util.Objects;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Predicate;

public class WhileClosure<T> implements Closure<T> {

    public static <E> Closure<E> whileClosure(final Predicate<? super E> predicate, final Closure<? super E> closure, final boolean doLoop) {
        return new WhileClosure<>(Objects.requireNonNull(predicate, "predicate"), Objects.requireNonNull(closure, "closure"), doLoop);
    }

    private final Predicate<? super T> iPredicate;

    private final Closure<? super T> iClosure;

    private final boolean iDoLoop;

    public WhileClosure(final Predicate<? super T> predicate, final Closure<? super T> closure, final boolean doLoop) {
        iPredicate = predicate;
        iClosure = closure;
        iDoLoop = doLoop;
    }

    @Override
    public void execute(final T input) {
        if (iDoLoop && !iPredicate.test(input)) { // Conditionals Boundary mutant
            iClosure.accept(input);
        }
        while (iPredicate.test(input)) {
            iClosure.accept(input);
        }
    }

    public Closure<? super T> getClosure() {
        return iClosure;
    }

    public Predicate<? super T> getPredicate() {
        return iPredicate;
    }

    public boolean isDoLoop() {
        return iDoLoop;
    }
}