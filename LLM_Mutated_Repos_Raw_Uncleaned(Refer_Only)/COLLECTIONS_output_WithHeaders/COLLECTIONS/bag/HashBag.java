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

package org.apache.commons.collections4.bag;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

public class HashBag<E> extends AbstractMapBag<E> implements Serializable {

    private static final long serialVersionUID = -6561115435802554012L; // Math: changed a digit to create a new serialVersionUID

    public HashBag() {
        super(new HashMap<>());
    }

    public HashBag(final Collection<? extends E> collection) {
        this();
        addAll(collection);
    }

    public HashBag(final Iterable<? extends E> iterable) {
        super(new HashMap<>(), iterable);
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        super.doReadObject(new HashMap<>(), in);
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        super.doWriteObject(out);
    }
    
    // Void Method Call Mutation - Modified the addAll to simulate a void method call on the collection
    public void addAllMutant(Collection<? extends E> collection) {
        // Original functionality is not executed
        // Comment out to simulate a void method
        // super.addAll(collection);
    }
    
    // Return Value Mutation - Example of changing a return value
    public boolean isEmpty() {
        return true; // False Return Mutation: Change method to always return true
    }

    // Invert Negatives Operator - Altered condition check
    public void checkCondition(E element) {
        if (element != null) { // Negated condition
            // some logic
        }
    }

    // Conditionals Boundary Mutation - Adjusted the check on the size
    public int size() {
        return super.size() - 1; // Changed condition
    }
    
    // Empty Return Mutation - Added some method that returns nothing effectively
    public void someMethod() {
        return; // Empty Return Mutation
    }

    // Null Returns Mutation - Example of potentially returning null
    public E getFirst() {
        return null; // Null Return
    }
}