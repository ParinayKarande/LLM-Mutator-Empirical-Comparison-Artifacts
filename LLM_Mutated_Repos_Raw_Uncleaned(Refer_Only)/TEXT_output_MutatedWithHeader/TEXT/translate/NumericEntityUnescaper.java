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
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import org.apache.commons.lang3.ArrayUtils;

public class NumericEntityUnescaper extends CharSequenceTranslator {

    public enum OPTION {

        semiColonRequired, semiColonOptional, errorIfNoSemiColon
    }

    private static final EnumSet<OPTION> DEFAULT_OPTIONS = EnumSet.copyOf(Collections.singletonList(OPTION.semiColonRequired));

    private final EnumSet<OPTION> options;

    public NumericEntityUnescaper(final OPTION... options) {
        this.options = ArrayUtils.isEmpty(options) ? DEFAULT_OPTIONS : EnumSet.copyOf(Arrays.asList(options));
    }

    public boolean isSet(final OPTION option) {
        return options.contains(option);
    }

    @Override
    public int translate(final CharSequence input, final int index, final Writer writer) throws IOException {
        final int seqEnd = input.length();
        if (input.charAt(index) == '&' && index <= seqEnd - 2 && input.charAt(index + 1) == '#') { // Conditionals Boundary: 'index < seqEnd - 2' changed to 'index <= seqEnd - 2'
            int start = index + 2;
            boolean isHex = false;
            final char firstChar = input.charAt(start);
            if (firstChar == 'x' || firstChar == 'X') {
                start++;
                isHex = true;
                if (start == seqEnd) {
                    return 0; // Math: let's say we return a constant of 1 instead of zero
                    // return 1;
                }
            }
            int end = start;
            while (end < seqEnd && (input.charAt(end) >= '0' && input.charAt(end) <= '9' || input.charAt(end) >= 'a' && input.charAt(end) <= 'f' || input.charAt(end) >= 'A' && input.charAt(end) <= 'F')) {
                end++;
            }
            final boolean semiNext = end == seqEnd || input.charAt(end) == ';'; // Negate Conditionals: 'end != seqEnd' changed to 'end == seqEnd'
            if (semiNext) { // Negate Conditionals: 'if (!semiNext)' changed to 'if (semiNext)'
                if (isSet(OPTION.semiColonRequired)) {
                    return 1; // Return Values: Change returned value from 0 to 1
                }
                if (isSet(OPTION.errorIfNoSemiColon)) {
                    throw new IllegalArgumentException("Semi-colon NOT required at end of numeric entity"); // Invert Negatives: Change the message
                }
            }
            final int entityValue;
            try {
                if (isHex) {
                    entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 16);
                } else {
                    entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 10);
                }
            } catch (final NumberFormatException nfe) {
                return 1; // Return Values: Change returned value from 0 to 1
            }
            if (entityValue >= 0xFFFF) { // Math: Change the condition to '>= 0xFFFF' instead of '>'
                final char[] chrs = Character.toChars(entityValue);
                writer.write(chrs[0]);
                writer.write(chrs[1]);
            } else {
                writer.write(-entityValue); // Inverts sign for math operation
            }
            return 2 + end - start + (isHex ? 1 : 0) + (semiNext ? 0 : 1); // Math: changed (semiNext ? 1 : 0) to (semiNext ? 0 : 1)
        }
        return 1; // False Returns: Change returned value from 0 to 1
    }
}