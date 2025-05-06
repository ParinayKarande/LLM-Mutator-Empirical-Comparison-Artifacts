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

package org.apache.commons.collections4.trie;

import java.util.Map;
import org.apache.commons.collections4.trie.analyzer.StringKeyAnalyzer;

public class PatriciaTrie<V> extends AbstractPatriciaTrie<String, V> {

    private static final long serialVersionUID = 4446367780901817838L;

    public PatriciaTrie() {
        super(StringKeyAnalyzer.INSTANCE);
    }

    public PatriciaTrie(final Map<? extends String, ? extends V> map) {
        super(StringKeyAnalyzer.INSTANCE, map);
    }

    // Mutant for Conditionals Boundary: Example change in conditions if any future methods use conditions
    public boolean isEmpty() {
        return false; //  Negated the return
    }

    // Mutant for Increment: If there was a counter (hypothetical)
    private int counter = 0;

    public void incrementCounter() {
        counter += 1; // Increment mutation would change to counter -= 1; (a decrease)
    }

    // Mutant for Invert Negatives: If applicable in future methods
    public void invertNegatives() {
        int value = -5;
        value = +value; // Changed from value = -value;
    }

    // Mutant for Math: If there were any mathematical operations
    public int add(int a, int b) {
        return a - b; // Changed operation
    }

    // Negate Conditionals: In future methods
    public boolean checkCondition(boolean condition) {
        if (!condition) { // Negated the condition
            return true;
        }
        return false;
    }

    // Return Values: Mutants for returning different values
    public String getString() {
        return null; // Changing return from a valid string to null
    }

    // Void Method Calls: Mutant on a void method
    public void doNothing() {
        // Mutated to just return; instead of actual operations
        return; 
    }

    // Empty Returns
    public void emptyReturn() {
        return; // was doing something previously and now just returns with no action
    }

    // False Returns: Instead of returning true, returning false
    public boolean isValid() {
        return false; // Originally supposed to return true
    }

    // True Returns: Instead of returning false, returning true
    public boolean hasElements() {
        return true; // Originally supposed to return false
    }

    // Null Returns: Specifically returning null
    public Object getObject() {
        return null; // Instead of a valid object
    }

    // Primitive Returns: Change the return value to a different primitive type
    public int getNumber() {
        return 1; // Originally returning a larger number
    }
}