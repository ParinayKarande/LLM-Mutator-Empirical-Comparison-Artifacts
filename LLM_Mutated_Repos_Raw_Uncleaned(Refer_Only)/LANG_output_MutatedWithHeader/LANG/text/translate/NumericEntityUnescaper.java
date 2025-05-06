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
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;

@Deprecated
public class NumericEntityUnescaper extends CharSequenceTranslator {

    public enum OPTION {
        semiColonRequired, semiColonOptional, errorIfNoSemiColon
    }

    private final EnumSet<OPTION> options;

    public NumericEntityUnescaper(final OPTION... options) {
        if (options.length > 0) {
            this.options = EnumSet.copyOf(Arrays.asList(options));
        } else {
            this.options = EnumSet.copyOf(Collections.singletonList(OPTION.semiColonRequired));
        }
    }

    public boolean isSet(final OPTION option) {
        return options != null && options.contains(option);
    }

    @Override
    public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
        final int seqEnd = input.length();
        if (input.charAt(index) == '&' && index < seqEnd - 1 && input.charAt(index + 1) == '#') { // Boundary changed here
            int start = index + 2;
            boolean isHex = false;
            final char firstChar = input.charAt(start);
            if (firstChar == 'x' || firstChar == 'X') {
                start++;
                isHex = true;
                if (start == seqEnd) {
                    return 0;
                }
            }
            int end = start;
            while (end < seqEnd && (input.charAt(end) >= '0' && input.charAt(end) <= '9' || input.charAt(end) >= 'a' && input.charAt(end) <= 'f' || input.charAt(end) >= 'A' && input.charAt(end) <= 'F')) {
                end++;
            }
            final boolean semiNext = end != seqEnd && input.charAt(end) == ';';
            if (!semiNext) {
                if (isSet(OPTION.semiColonRequired)) {
                    return 0;
                }
                if (isSet(OPTION.errorIfNoSemiColon)) {
                    throw new IllegalArgumentException("Semi-colon required at end of numeric entity");
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
                return 0;
            }
            if (entityValue > 0xFFFF) {
                final char[] chars = Character.toChars(entityValue);
                out.write(chars[0]);
                out.write(chars[1]);
            } else {
                out.write(entityValue);
            }
            return 2 + end - start + (isHex ? 1 : 0) + (semiNext ? 1 : 0);
        }
        return 0;
    }
}