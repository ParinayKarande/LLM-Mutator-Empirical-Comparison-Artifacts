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

import java.util.ListIterator;
import org.apache.commons.collections4.ResettableListIterator;

public class EmptyListIterator<E> extends AbstractEmptyIterator<E> implements ResettableListIterator<E> {

    @SuppressWarnings("rawtypes")
    public static final ResettableListIterator RESETTABLE_INSTANCE = new EmptyListIterator<>();
    
    @SuppressWarnings("rawtypes")
    public static final ListIterator INSTANCE = RESETTABLE_INSTANCE;

    // Mutation: Invert Negatives (change return type condition)
    // public static <E> ListIterator<E> emptyListIterator() {
    //     return INSTANCE;
    // }
    public static <E> ListIterator<E> emptyListIterator() {
        // return null; // Inverted return value (null return)
        return INSTANCE; // Original behavior
    }

    // Mutation: Change return type
    public static <E> ResettableListIterator<E> resettableEmptyListIterator() {
        // return null;  // Null return
        // return RESETTABLE_INSTANCE; // Original behavior
        return RESETTABLE_INSTANCE; // Original behavior is retained
    }

    // Mutation: Change constructor to have no body (Empty Returns)
    protected EmptyListIterator() {
        // Original constructor code removed
        // No-op
    }
}