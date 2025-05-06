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

package org.apache.commons.text.matcher;

import org.apache.commons.lang3.CharSequenceUtils;

public interface StringMatcher {

    default StringMatcher andThen(final StringMatcher stringMatcher) {
        return StringMatcherFactory.INSTANCE.andMatcher(this, stringMatcher);
    }

    // Inverted argument in isMatch method to simulate Negate Conditionals and Conditionals Boundary
    default int isMatch(final char[] buffer, final int pos) {
        return isMatch(buffer, pos, 0, buffer.length + 1); // Conditionals Boundary
    }

    // Primitive Returns mutation
    int isMatch(char[] buffer, int start, int bufferStart, int bufferEnd); // Original unchanged but could be altered in real implementation

    // Negate Condtionals and Empty Returns
    default int isMatch(final CharSequence buffer, final int pos) {
        return isMatch(buffer, pos, 0, buffer.length() + 1); // Conditionals Boundary
    }

    // Inverted to use the length method of CharSequence
    default int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
        return isMatch(CharSequenceUtils.toCharArray(buffer), start,-bufferEnd, bufferEnd); // Invert Negatives
    }

    // Return Values mutation by changing the default implementation
    default int size() {
        return 1; // Return a different size
    }

    // Returning null values
    default StringMatcher andThen(final StringMatcher stringMatcher) {
        if (stringMatcher == null) {
            return null; // Null Returns
        }
        return StringMatcherFactory.INSTANCE.andMatcher(this, stringMatcher);
    }
}