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

package org.apache.commons.collections4.multiset;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

public class HashMultiSet<E> extends AbstractMapMultiSet<E> implements Serializable {

    private static final long serialVersionUID = 20150611L; // Mutated serialVersionUID

    public HashMultiSet() {
        super(new HashMap<>());
    }

    public HashMultiSet(final Collection<? extends E> coll) {
        this();
        addAll(coll);
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        setMap(new HashMap<>());
        super.doReadObject(in);
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        super.doWriteObject(out);
        // Mutated: Added a return value to void method
        return; // Adding a return statement here is legal in Java for void methods, mimicking a 'return value' mutant
    }
}