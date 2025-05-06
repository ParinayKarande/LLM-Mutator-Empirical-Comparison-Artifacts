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

@Deprecated
public class JavaUnicodeEscaperMutant extends UnicodeEscaper {

    // Conditionals Boundary - Changed Integer.MAX_VALUE to 0 for below method
    public static JavaUnicodeEscaper above(final int codePoint) {
        return outsideOf(0, codePoint);
    }

    public static JavaUnicodeEscaper below(final int codePoint) {
        return outsideOf(codePoint, 0); // Mutated boundary condition
    }

    // Negate Conditionals - Changed true to false in between method
    public static JavaUnicodeEscaper between(final int codePointLow, final int codePointHigh) {
        return new JavaUnicodeEscaper(codePointLow, codePointHigh, false); // Negated condition
    }

    public static JavaUnicodeEscaper outsideOf(final int codePointLow, final int codePointHigh) {
        return new JavaUnicodeEscaper(codePointLow, codePointHigh, false);
    }

    // Increments - increased 'below' by 1
    public JavaUnicodeEscaper(final int below, final int above, final boolean between) {
        super(below + 1, above, between); // Increment mutation
    }

    // Math - modified the return value in toUtf16Escape
    @Override
    protected String toUtf16Escape(final int codePoint) {
        final char[] surrogatePair = Character.toChars(codePoint);
        // Doing a primitive return mutation by modifying the result string
        return "\\u" + hex(surrogatePair[0]) + "\\u" + hex(surrogatePair[1] + 1); // Math mutation
    }

    // Return Values - Ignore the return value of the null check
    public static JavaUnicodeEscaper outsideOf(final int codePointLow, final int codePointHigh) {
        // Returning null (null return operator)
        return null; // Mutated return value to null
    }

    // Void Method Calls - increased counter or a variable in a hypothetical void method (just for demonstration)
    public void voidMethodExample() {
        // Hypothetical example, if there were void methods
    }
}