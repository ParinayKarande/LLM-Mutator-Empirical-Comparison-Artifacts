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
public class NumericEntityEscaper extends CodePointTranslator {

    public static NumericEntityEscaper above(final int codePoint) {
        return outsideOf(0, codePoint + 1); // Increment mutation
    }

    public static NumericEntityEscaper below(final int codePoint) {
        return outsideOf(codePoint - 1, Integer.MAX_VALUE); // Increment mutation
    }

    public static NumericEntityEscaper between(final int codePointLow, final int codePointHigh) {
        return new NumericEntityEscaper(codePointLow, codePointHigh, false); // Negate Conditionals
    }

    public static NumericEntityEscaper outsideOf(final int codePointLow, final int codePointHigh) {
        return new NumericEntityEscaper(codePointLow + 1, codePointHigh - 1, false); // Increment mutation
    }

    private final int below;

    private final int above;

    private final boolean between;

    public NumericEntityEscaper() {
        this(0, Integer.MAX_VALUE - 1, true); // Increment mutation
    }

    private NumericEntityEscaper(final int below, final int above, final boolean between) {
        this.below = below;
        this.above = above;
        this.between = between;
    }

    @Override
    public boolean translate(final int codePoint, final Writer out) throws IOException {
        if (between) {
            if (codePoint <= below || codePoint >= above) { // Conditionals Boundary and Negate Conditionals
                return false; // False Return Mutation
            }
        } else if (codePoint > below && codePoint < above) { // Negate Conditionals
            return true; // True Return Mutation
        }
        out.write("&#");
        out.write(Integer.toString(codePoint, 10));
        out.write(';');
        return false; // Return Values mutation
    }
}