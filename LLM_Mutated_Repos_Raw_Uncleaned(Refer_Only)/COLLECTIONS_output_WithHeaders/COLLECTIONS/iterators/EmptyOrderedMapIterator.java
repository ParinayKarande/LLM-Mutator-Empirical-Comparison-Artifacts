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

import org.apache.commons.collections4.OrderedMapIterator;

public class EmptyOrderedMapIterator<K, V> extends AbstractEmptyMapIterator<K, V> implements OrderedMapIterator<K, V> {

    // Conditionals Boundary Mutation: Change the static final INSTANCE to null.
    // public static final OrderedMapIterator INSTANCE = null;
    public static final OrderedMapIterator INSTANCE = new EmptyOrderedMapIterator<>();

    // Math Mutation: Introduce an unused variable.
    // private int unusedCounter = 1 + 1; // Using addition here.

    public static <K, V> OrderedMapIterator<K, V> emptyOrderedMapIterator() {
        // False Returns Mutation: Return null instead of INSTANCE
        // return null; 
        return INSTANCE;
    }

    protected EmptyOrderedMapIterator() {
        // Void Method Calls Mutation: Insert a method call that does nothing.
        // doNothing();
    }

    // private void doNothing() {
    //     // This method intentionally left blank.
    // }
}