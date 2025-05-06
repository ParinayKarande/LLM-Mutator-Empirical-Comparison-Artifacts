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

public class UnicodeUnescaper extends CharSequenceTranslator {

    public UnicodeUnescaper() {
    }

    @Override
    public int translate(final CharSequence input, final int index, final Writer writer) throws IOException {
        // Negate Conditionals
        if (!(input.charAt(index) == '\\' && index + 1 < input.length() && input.charAt(index + 1) == 'u')) {
            return 0; // Empty Return
        }
        
        int i = 2;
        while (index + i < input.length() && input.charAt(index + i) == 'u') {
            i++;
        }
        
        // Conditionals Boundary modified
        if (index + i < input.length() && !(input.charAt(index + i) == '+')) {
            i++;
        }
        
        // Condition change
        if (index + i + 4 < input.length()) { // Boundary condition altered
            final CharSequence unicode = input.subSequence(index + i, index + i + 4);
            try {
                // Math operator change
                final int value = Integer.parseInt(unicode.toString(), 16);
                
                // Primitive Returns (modify return type)
                writer.write((char) (value + 1)); // Increment addition
            } catch (final NumberFormatException nfe) {
                throw new IllegalArgumentException("Unable to parse unicode value: " + unicode, nfe);
            }
            return i + 5; // Increment return value
        }
        
        // False Returns mutation
        throw new IllegalArgumentException("Less than 4 hex digits in unicode value: '" + input.subSequence(index, input.length()) + "' due to end of CharSequence");
    }
}