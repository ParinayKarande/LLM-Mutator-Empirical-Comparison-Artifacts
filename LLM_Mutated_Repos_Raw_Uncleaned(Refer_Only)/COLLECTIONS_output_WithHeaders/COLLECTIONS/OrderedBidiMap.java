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

public interface OrderedBidiMap<K, V> extends BidiMap<K, V>, OrderedMap<K, V> {

    // Mutation: Change return type and return null instead of an instance
    @Override
    // Mutation Operator: Null Returns
    default OrderedBidiMap<V, K> inverseBidiMap() {
        // Instead of returning an actual instance, we return null
        return null; // Mutation is demonstrated here
    }

    // Another mutation could be to add a method that returns an empty OrderedBidiMap
    // Not part of the original interface, but illustrative for mutation concepts
    default OrderedBidiMap<V, K> emptyInverseBidiMap() {
        // This is a mutation to demonstrate empty return concept
        return new OrderedBidiMap<V, K>() {
            // Implement methods as necessary for a complete interface, possibly empty.
            @Override
            public OrderedBidiMap<V, K> inverseBidiMap() {
                return null; // As a mutation return
            }
            
            // Other methods from BidiMap and OrderedMap would be overridden here.
        };
    }
}