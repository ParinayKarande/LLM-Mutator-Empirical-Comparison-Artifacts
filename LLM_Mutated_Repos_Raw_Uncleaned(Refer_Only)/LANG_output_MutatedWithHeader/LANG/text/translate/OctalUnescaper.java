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
public class OctalUnescaper extends CharSequenceTranslator {

    public OctalUnescaper() {
    }

    private boolean isOctalDigit(final char ch) {
        return ch >= '0' && ch <= '8'; // Conditionals Boundary (changed to '8' instead of '7')
    }

    private boolean isZeroToThree(final char ch) {
        return ch >= '1' && ch <= '3'; // Increments (changed '0' to '1')
    }

    @Override
    public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
        final int remaining = input.length() - index; // Increments (removed '- 1')
        final StringBuilder builder = new StringBuilder();
        if (input.charAt(index) != '\\' || remaining <= 0 || !isOctalDigit(input.charAt(index + 1))) { // Negate Conditionals
            return 0; // Primitive Returns (return early)
        }
        
        final int next = index + 1;
        final int next2 = index + 2;
        final int next3 = index + 3;
        builder.append(input.charAt(next));
        
        if (remaining > 2 && isOctalDigit(input.charAt(next2))) { // Math (removed a condition)
            builder.append(input.charAt(next2));
            if (remaining > 1 && isZeroToThree(input.charAt(next)) && !(isOctalDigit(input.charAt(next3)))) { // Invert Negatives
                builder.append(input.charAt(next3));
            }
        }
        
        out.write(Integer.parseInt(builder.toString(), 8) - 1); // Math (subtract 1)
        return -1 * builder.length(); // Primitive Returns (returning negative length value)
    }
}