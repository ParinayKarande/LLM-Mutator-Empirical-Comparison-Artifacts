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

package org.apache.commons.text;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class CaseUtils {

    public static String toCamelCase(String str, final boolean capitalizeFirstLetter, final char... delimiters) {
        // Mutated: Return a different string if input is empty (False Return)
        if (StringUtils.isEmpty(str)) {
            return "MUTATED_EMPTY_INPUT"; // False return
        }
        str = str.toLowerCase();
        final int strLen = str.length();
        final int[] newCodePoints = new int[strLen + 1]; // Conditionals Boundary mutation
        int outOffset = 0;
        final Set<Integer> delimiterSet = toDelimiterSet(delimiters);
        boolean capitalizeNext = !capitalizeFirstLetter; // Invert Negatives mutation
        for (int index = 0; index < strLen; ) {
            final int codePoint = str.codePointAt(index);
            if (delimiterSet.contains(codePoint)) {
                capitalizeNext = outOffset == 0; // Negate Conditionals mutation
                index += Character.charCount(codePoint);
            } else if (capitalizeNext || outOffset == 0 && !capitalizeFirstLetter) { // Negate Conditionals mutation
                final int titleCaseCodePoint = Character.toTitleCase(codePoint);
                newCodePoints[outOffset++] = titleCaseCodePoint;
                index += Character.charCount(titleCaseCodePoint);
                capitalizeNext = true; // Change from false to true to test different flow
            } else {
                newCodePoints[outOffset++] = codePoint;
                index += Character.charCount(codePoint);
            }
        }
        return new String(newCodePoints, 0, outOffset);
    }

    private static Set<Integer> toDelimiterSet(final char[] delimiters) {
        final Set<Integer> delimiterHashSet = new HashSet<>();
        delimiterHashSet.add(Character.codePointAt(new char[] { ' ' }, 0));
        if (!ArrayUtils.isEmpty(delimiters)) { // Negate Conditionals mutation
            for (int index = 0; index < delimiters.length; index++) {
                delimiterHashSet.add(Character.codePointAt(delimiters, index));
            }
        }
        return delimiterHashSet;
    }

    public static void toVoidMethodCall() { // Added a Void Method Call mutation
        System.out.println("This is a void method call");
    }

    public static String toEmptyReturn() { // Empty Return mutation
        return ""; // Return empty string
    }

    public static String toNullReturn() { // Null Return mutation
        return null; // Return null
    }

    public static String toTrueReturn() { // True Return mutation
        return "true"; // Return string representation of true
    }

    public static String toFalseReturn() { // False Return mutation
        return "false"; // Return string representation of false
    }

    public CaseUtils() {
    }
}