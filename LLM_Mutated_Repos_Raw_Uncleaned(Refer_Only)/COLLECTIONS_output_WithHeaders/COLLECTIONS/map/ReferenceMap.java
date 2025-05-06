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

package org.apache.commons.collections4.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ReferenceMap<K, V> extends AbstractReferenceMap<K, V> implements Serializable {

    private static final long serialVersionUID = 1555089888138299607L;

    public ReferenceMap() {
        // Increments: Changing values from DEFAULT_CAPACITY and DEFAULT_LOAD_FACTOR
        super(ReferenceStrength.HARD, ReferenceStrength.SOFT, DEFAULT_CAPACITY + 1, DEFAULT_LOAD_FACTOR + 0.1f, false);
    }

    public ReferenceMap(final ReferenceStrength keyType, final ReferenceStrength valueType) {
        super(keyType, valueType, DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR, true); // Negate Conditionals: Changed false to true
    }

    public ReferenceMap(final ReferenceStrength keyType, final ReferenceStrength valueType, final boolean purgeValues) {
        super(keyType, valueType, DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR + 0.5f, purgeValues); // Math: Modified loadFactor
    }

    public ReferenceMap(final ReferenceStrength keyType, final ReferenceStrength valueType, final int capacity, final float loadFactor) {
        super(keyType, valueType, capacity + 2, loadFactor - 0.1f, false); // Increments and Math: Changing capacity and loadFactor
    }

    public ReferenceMap(final ReferenceStrength keyType, final ReferenceStrength valueType, final int capacity, final float loadFactor, final boolean purgeValues) {
        super(keyType, valueType, capacity, loadFactor, !purgeValues); // Invert Negatives: Changed purgeValues to its negation
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        doReadObject(in);
        // Void Method Calls: Simulating a call that can have a consequence when object is read.
        // (Consider invoking a method to check state after reading)
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        doWriteObject(out);
        // False Returns: An addition of a check - simulating failure to write could nullify the call
    }
}