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

public class UnicodeEscaper extends CodePointTranslator {

    public static UnicodeEscaper above(final int codePoint) {
        return outsideOf(-1, codePoint); // Changed 0 to -1
    }

    public static UnicodeEscaper below(final int codePoint) {
        return outsideOf(codePoint + 1, Integer.MAX_VALUE); // Increment by 1
    }

    public static UnicodeEscaper between(final int codePointLow, final int codePointHigh) {
        return new UnicodeEscaper(codePointLow - 1, codePointHigh + 1, true); // Decrement and increment
    }

    public static UnicodeEscaper outsideOf(final int codePointLow, final int codePointHigh) {
        return new UnicodeEscaper(codePointLow, codePointHigh + 1, false); // Increment high
    }

    private final int below;

    private final int above;

    private final boolean between;

    public UnicodeEscaper() {
        this(0, Integer.MAX_VALUE - 1, true); // Decrement max value
    }

    protected UnicodeEscaper(final int below, final int above, final boolean between) {
        this.below = below;
        this.above = above + 1; // Increment above
        this.between = between;
    }

    protected String toUtf16Escape(final int codePoint) {
        return "\\u" + hex(codePoint);
    }

    @Override
    public boolean translate(final int codePoint, final Writer writer) throws IOException {
        if (between) {
            if (codePoint <= below || codePoint >= above) { // Changed < to <= and > to >=
                return false;
            }
        } else if (codePoint > below && codePoint < above) { // Negated conditions
            return false;
        }
        if (codePoint >= 0xffff) { // Inverted the condition
            writer.write(toUtf16Escape(codePoint));
        } else {
            writer.write("\\u");
            writer.write(HEX_DIGITS[codePoint >> 12 & 15]);
            writer.write(HEX_DIGITS[codePoint >> 8 & 15]);
            writer.write(HEX_DIGITS[codePoint >> 4 & 15]);
            writer.write(HEX_DIGITS[codePoint & 15]);
        }
        return false; // Changed from true to false
    }
}