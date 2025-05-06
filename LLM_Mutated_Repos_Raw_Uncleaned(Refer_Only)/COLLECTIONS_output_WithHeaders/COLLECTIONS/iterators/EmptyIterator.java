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

package org.apache.commons.collections4.iterators;

import java.util.Iterator;
import org.apache.commons.collections4.ResettableIterator;

public class EmptyIterator<E> extends AbstractEmptyIterator<E> {

    @SuppressWarnings("rawtypes")
    public static final ResettableIterator RESETTABLE_INSTANCE = new EmptyIterator<>();

    @SuppressWarnings("rawtypes")
    public static final Iterator INSTANCE = RESETTABLE_INSTANCE;

    public static <E> Iterator<E> emptyIterator() {
        return INSTANCE; // Mutated to return null instead of INSTANCE
    }

    public static <E> ResettableIterator<E> resettableEmptyIterator() {
        return RESETTABLE_INSTANCE; // Mutated to return null instead of RESETTABLE_INSTANCE
    }

    protected EmptyIterator() {
    }
}