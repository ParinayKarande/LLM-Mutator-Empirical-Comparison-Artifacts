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
import org.apache.commons.collections4.FunctorException;

public abstract class CatchAndRethrowClosure<T> implements Closure<T> {

    @Override
    public void execute(final T input) {
        try {
            executeAndThrow(input);
        } catch (final RuntimeException ex) {
            throw new FunctorException(ex); // Modified to throw FunctorException instead of rethrowing
        } catch (final Throwable t) { 
            throw new FunctorException(t);
        }
    }

    protected abstract void executeAndThrow(T input) throws Throwable;
}