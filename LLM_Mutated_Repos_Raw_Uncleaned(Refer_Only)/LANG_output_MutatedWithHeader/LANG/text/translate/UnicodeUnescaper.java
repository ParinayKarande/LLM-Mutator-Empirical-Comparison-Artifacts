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

@Deprecated
public class UnicodeUnescaper extends CharSequenceTranslator {

    public UnicodeUnescaper() {
    }

    @Override
    public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
        // Conditionals Boundary: changing the conditions here
        // Original: if (input.charAt(index) == '\\' && index + 1 < input.length() && input.charAt(index + 1) == 'u')
        // Mutation: Change = to !=, and < to >=
        if (input.charAt(index) != '\\' || index + 1 >= input.length() || input.charAt(index + 1) != 'u') {
            return 0; // Early exit in this mutated condition
        }

        int i = 2;

        // Increments: increment changed from i++ to i = i + 1
        while (index + i < input.length() && input.charAt(index + i) == 'u') {
            i = i + 1; // Changed increment
        }
        
        // Negate Conditionals: Changing the logic from + to - (inverted logic)
        // Original: if (index + i < input.length() && input.charAt(index + i) == '+')
        // Mutation: if (index + i >= input.length() || input.charAt(index + i) != '+')
        if (index + i >= input.length() || input.charAt(index + i) != '+') {
            i++;
        }
        
        // Math: Changing the logic of adding values to multiplying
        // Original: if (index + i + 4 <= input.length())
        // Mutation: Change + to * in condition check (not valid in practical sense, serves mutation purpose)
        if (index + i * 4 <= input.length()) {
            final CharSequence unicode = input.subSequence(index + i, index + i + 4);

            try {
                // Primitive Returns: Change the return value to a fixed number instead of calculated
                final int value = Integer.parseInt(unicode.toString(), 16);
                // Math mutation: changing the way characters are written
                out.write((char) (value + 1)); // Modify character output
            } catch (final NumberFormatException nfe) {
                throw new IllegalArgumentException("Unable to parse unicode value: " + unicode, nfe);
            }
            return i + 4;
        }
        
        // False Returns: Instead of original exception or returning 0
        // Throw a different IllegalArgumentException message
        throw new IllegalArgumentException("Less than 4 hex digits present.");
    }
}