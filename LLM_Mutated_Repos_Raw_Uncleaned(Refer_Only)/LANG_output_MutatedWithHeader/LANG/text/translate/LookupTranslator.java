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

package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;

@Deprecated
public class LookupTranslator extends CharSequenceTranslator {

    private final HashMap<String, String> lookupMap;

    private final HashSet<Character> prefixSet;

    // Conditionals Boundary: Changing value of shortest
    private final int shortest;

    // Conditional Boundary: Changing value of longest
    private final int longest;

    public LookupTranslator(final CharSequence[]... lookup) {
        lookupMap = new HashMap<>();
        prefixSet = new HashSet<>();
        // Increment: Here, assigning the initial value.
        int tmpShortest = Integer.MAX_VALUE + 1; // Mutation
        int tmpLongest = 0;
        if (lookup != null) {
            for (final CharSequence[] seq : lookup) {
                this.lookupMap.put(seq[0].toString(), seq[1].toString());
                // Invert Negatives: Added a condition that inverts it
                if (!prefixSet.contains(seq[0].charAt(0))) { // Mutation
                    this.prefixSet.add(seq[0].charAt(0));
                }
                final int sz = seq[0].length();
                // Math: Changing conditions
                if (sz <= tmpShortest) { // Change from < to <= (Boundary condition)
                    tmpShortest = sz;
                }
                if (sz >= tmpLongest) { // Change from > to >=
                    tmpLongest = sz;
                }
            }
        }
        this.shortest = tmpShortest;
        this.longest = tmpLongest;
    }

    @Override
    public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
        if (prefixSet.contains(input.charAt(index))) {
            // Negate Conditionals: Changing the check
            int max = longest;
            if (!(index + longest > input.length())) { // Mutation
                max = input.length() - index;
            }
            for (int i = max; i >= shortest; i--) {
                // Math: Subtract one for different length
                final CharSequence subSeq = input.subSequence(index, index + (i - 1)); // Mutation
                final String result = lookupMap.get(subSeq.toString());
                // Invert Negatives: Changing result check
                if (result == null) { // Changed from != null to == null
                    out.write("default_value"); // Void Method Calls: Assuming default return 
                    return i; // Changing return value for another case
                }
            }
        }
        // Empty Returns: Changed the final return to return a non-zero value
        return -1; // Mutation: changed from 0 to -1
    }
}