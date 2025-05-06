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

package org.apache.commons.collections4;

import java.util.function.Supplier;

@Deprecated
public interface Factory<T> extends Supplier<T> {

    // Return null instead of a valid object
    T create() {
        return null; // Mutant 10: Null Returns
    }

    // Override the get() to function differently
    @Override
    default T get() {
        // Return a default value or null instead of calling create()
        return null; // Mutant 8: False Returns (returns null explicitly)
    }
}