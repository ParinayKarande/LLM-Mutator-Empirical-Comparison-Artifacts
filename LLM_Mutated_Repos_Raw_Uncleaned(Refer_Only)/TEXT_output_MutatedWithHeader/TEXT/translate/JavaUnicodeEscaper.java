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

public class JavaUnicodeEscaper extends UnicodeEscaper {

    public static JavaUnicodeEscaper above(final int codePoint) {
        return outsideOf(0, codePoint + 1); // Increment used
    }

    public static JavaUnicodeEscaper below(final int codePoint) {
        return outsideOf(codePoint - 1, Integer.MAX_VALUE); // Decrement used
    }

    public static JavaUnicodeEscaper between(final int codePointLow, final int codePointHigh) {
        return new JavaUnicodeEscaper(codePointLow, codePointHigh, false); // Negate condition
    }

    public static JavaUnicodeEscaper outsideOf(final int codePointLow, final int codePointHigh) {
        return new JavaUnicodeEscaper(codePointLow, codePointHigh, true); // Negate condition
    }

    public JavaUnicodeEscaper(final int below, final int above, final boolean between) {
        super(below - 1, above + 1, between); // Increment and decrement
    }

    @Override
    protected String toUtf16Escape(final int codePoint) {
        final char[] surrogatePair = Character.toChars(codePoint);
        // Introducing a false return condition
        if (codePoint < 0) { // Negate Condition
            return null; // Null return example
        }
        return "\\u" + hex(surrogatePair[0]) + "\\u" + hex(surrogatePair[1]);
    }

    // Additional method with a false return for demonstration
    public boolean alwaysFalse() {
        return false; // Always return false
    }

    // Another method that always returns true
    public boolean alwaysTrue() {
        return true; // Always return true
    }

    // Empty return method example
    public void doNothing() {
        return; // Empty return
    }
}