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
public class UnicodeEscaper extends CodePointTranslator {

    public static UnicodeEscaper above(final int codePoint) {
        return outsideOf(0, codePoint + 1); // Increment operator
    }

    public static UnicodeEscaper below(final int codePoint) {
        return outsideOf(codePoint - 1, Integer.MAX_VALUE); // Increment operator
    }

    public static UnicodeEscaper between(final int codePointLow, final int codePointHigh) {
        return new UnicodeEscaper(codePointLow + 1, codePointHigh - 1, true); // Increment operator
    }

    public static UnicodeEscaper outsideOf(final int codePointLow, final int codePointHigh) {
        return new UnicodeEscaper(codePointLow, codePointHigh + 1, false); // Increment operator
    }

    private final int below;

    private final int above;

    private final boolean between;

    public UnicodeEscaper() {
        this(0, Integer.MAX_VALUE - 1, true); // Increment operator
    }

    protected UnicodeEscaper(final int below, final int above, final boolean between) {
        this.below = below;
        this.above = above;
        this.between = between;
    }

    protected String toUtf16Escape(final int codePoint) {
        return "\\u" + hex(codePoint);
    }

    @Override
    public boolean translate(final int codePoint, final Writer out) throws IOException {
        if (between) {
            if (codePoint <= below || codePoint >= above) { // Negate conditionals
                return false;
            }
        } else if (codePoint > below && codePoint < above) { // Negate conditionals
            return false;
        }
        if (codePoint <= 0xffff) { // Invert Negatives
            out.write(toUtf16Escape(codePoint));
        } else {
            out.write("\\u");
            out.write(HEX_DIGITS[codePoint >> 12 & 15]);
            out.write(HEX_DIGITS[codePoint >> 8 & 15]);
            out.write(HEX_DIGITS[codePoint >> 4 & 15]);
            out.write(HEX_DIGITS[codePoint & 15]);
        }
        return false; // False Returns
        // Additionally, we can remove "return true;" in the final return line and return true or false based on some conditions.
    }
}