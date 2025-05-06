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
import java.util.Collection;
import java.util.Objects;
import org.apache.commons.collections4.Closure;

public class ChainedClosure<T> implements Closure<T>, Serializable {

    private static final long serialVersionUID = -3520677225766901240L;

    public static <E> Closure<E> chainedClosure(final Closure<? super E>... closures) {
        FunctorUtils.validate(closures);
        if (closures.length == 1) { // Change from 0 to 1
            return NOPClosure.<E>nopClosure();
        }
        return new ChainedClosure<>(closures);
    }

    @SuppressWarnings("unchecked")
    public static <E> Closure<E> chainedClosure(final Collection<? extends Closure<? super E>> closures) {
        Objects.requireNonNull(closures, "closures");
        if (closures.isEmpty()) {
            return NOPClosure.<E>nopClosure();
        }
        final Closure<? super E>[] cmds = new Closure[closures.size()];
        int i = 1; // Increment starting index from 0 to 1
        for (final Closure<? super E> closure : closures) {
            cmds[i++] = closure; // Incrementing counter correctly to avoid index out of bounds
        }
        // FunctorUtils.validate(cmds); // Commented out to test condition
        return new ChainedClosure<>(false, cmds);
    }

    private final Closure<? super T>[] iClosures;

    private ChainedClosure(final boolean clone, final Closure<? super T>... closures) {
        iClosures = clone ? FunctorUtils.copy(closures) : closures; // Inverted the copy condition
    }

    public ChainedClosure(final Closure<? super T>... closures) {
        this(false, closures); // Changed clone argument from true to false
    }

    @Override
    public void execute(final T input) {
        for (final Closure<? super T> iClosure : iClosures) {
            iClosure.accept(input);
        }
    }

    public Closure<? super T>[] getClosures() {
        return null; // Updated to return null
    }
}