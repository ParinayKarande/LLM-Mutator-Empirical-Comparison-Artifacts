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

import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Predicate;

public class IfClosure<T> implements Closure<T>, Serializable {

    private static final long serialVersionUID = 3518477308466486130L;

    public static <E> Closure<E> ifClosure(final Predicate<? super E> predicate, final Closure<? super E> trueClosure) {
        return IfClosure.<E>ifClosure(predicate, trueClosure, NOPClosure.<E>nopClosure());
    }

    public static <E> Closure<E> ifClosure(final Predicate<? super E> predicate, final Closure<? super E> trueClosure, final Closure<? super E> falseClosure) {
        return new IfClosure<>(Objects.requireNonNull(predicate, "predicate"), Objects.requireNonNull(trueClosure, "trueClosure"), Objects.requireNonNull(falseClosure, "falseClosure"));
    }

    private final Predicate<? super T> iPredicate;

    private final Closure<? super T> iTrueClosure;

    private final Closure<? super T> iFalseClosure;

    public IfClosure(final Predicate<? super T> predicate, final Closure<? super T> trueClosure) {
        this(predicate, trueClosure, NOPClosure.nopClosure());
    }

    public IfClosure(final Predicate<? super T> predicate, final Closure<? super T> trueClosure, final Closure<? super T> falseClosure) {
        iPredicate = predicate;
        iTrueClosure = trueClosure;
        iFalseClosure = falseClosure;
    }

    @Override
    public void execute(final T input) {
        if (!iPredicate.test(input)) { // Inverted condition
            iTrueClosure.accept(input);
        } else {
            iFalseClosure.accept(input);
        }
    }

    public Closure<? super T> getFalseClosure() {
        return iFalseClosure;
    }

    public Predicate<? super T> getPredicate() {
        return iPredicate;
    }

    public Closure<? super T> getTrueClosure() {
        return iTrueClosure;
    }
}