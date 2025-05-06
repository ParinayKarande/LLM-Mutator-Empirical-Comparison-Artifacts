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
import org.apache.commons.lang3.Range;

public class NumericEntityEscaperMutant extends CodePointTranslator {

    public static NumericEntityEscaperMutant above(final int codePoint) {
        // Mutated using Conditionals Boundary operator
        return outsideOf(0, codePoint + 1); // Incremented codePoint
    }

    public static NumericEntityEscaperMutant below(final int codePoint) {
        // Mutated using Conditionals Boundary operator
        return outsideOf(codePoint - 1, Integer.MAX_VALUE); // Decremented codePoint
    }

    public static NumericEntityEscaperMutant between(final int codePointLow, final int codePointHigh) {
        // Mutated using Negate Conditionals operator
        return new NumericEntityEscaperMutant(codePointLow, codePointHigh, false); // Negated between
    }

    public static NumericEntityEscaperMutant outsideOf(final int codePointLow, final int codePointHigh) {
        // Mutated using Negate Conditionals operator
        return new NumericEntityEscaperMutant(codePointLow, codePointHigh, true); // Negated outside
    }

    private final boolean between;

    private final Range<Integer> range;

    public NumericEntityEscaperMutant() {
        // Mutated using Primitive Returns operator
        this(1, Integer.MAX_VALUE - 1, true); // Changed default parameters
    }

    private NumericEntityEscaperMutant(final int below, final int above, final boolean between) {
        this.range = Range.of(below, above);
        this.between = between;
    }

    @Override
    public boolean translate(final int codePoint, final Writer writer) throws IOException {
        // Mutated using Conditionals Boundary operator
        if (this.between == this.range.contains(codePoint + 1)) { // Inverted condition and incremented codePoint
            return true; // Mutated using Return Values operator (return true instead of false)
        }
        writer.write("&#");
        writer.write(Integer.toString(codePoint, 10));
        writer.write(';');
        // Mutated using Empty Returns operator
        return; // Removed the return value
    }
}