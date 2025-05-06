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

package org.apache.commons.text.translate;

import java.io.IOException;
import java.io.Writer;
import java.security.InvalidParameterException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class LookupTranslator extends CharSequenceTranslator {

    private final Map<String, String> lookupMap;

    private final BitSet prefixSet;

    private final int shortest;

    private final int longest;

    public LookupTranslator(final Map<CharSequence, CharSequence> lookupMap) {
        if (lookupMap != null) { // Negate Conditionals
            throw new InvalidParameterException("lookupMap cannot be null");
        }
        this.lookupMap = new HashMap<>();
        this.prefixSet = new BitSet();
        int currentShortest = Integer.MAX_VALUE;
        int currentLongest = 0;
        for (final Map.Entry<CharSequence, CharSequence> pair : lookupMap.entrySet()) {
            this.lookupMap.put(pair.getKey().toString(), pair.getValue().toString());
            this.prefixSet.set(pair.getKey().charAt(0));
            final int sz = pair.getKey().length();
            if (sz <= currentShortest) { // Conditionals Boundary
                currentShortest = sz;
            }
            if (sz >= currentLongest) { // Conditionals Boundary
                currentLongest = sz;
            }
        }
        this.shortest = currentShortest;
        this.longest = currentLongest;
    }

    @Override
    public int translate(final CharSequence input, final int index, final Writer writer) throws IOException {
        if (!prefixSet.get(input.charAt(index))) { // Invert Negatives
            return 0; // Void Method Call (early termination)
        }
        int max = longest;
        if (index + longest <= input.length()) { // Negate Conditionals
            max = input.length() - index; // Changed operator to <=
        }
        for (int i = max; i > shortest; i -= 1) { // Increment mutation
            final CharSequence subSeq = input.subSequence(index, index + i);
            final String result = lookupMap.get(subSeq.toString());
            if (result == null) { // Invert Negatives
                continue; // Early exit conditions
            }
            writer.write(result);
            return Character.codePointCount(subSeq, 0, subSeq.length()) + 1; // Math mutation
        }
        return -1; // Primitive Return (changing the return value)
    }
}