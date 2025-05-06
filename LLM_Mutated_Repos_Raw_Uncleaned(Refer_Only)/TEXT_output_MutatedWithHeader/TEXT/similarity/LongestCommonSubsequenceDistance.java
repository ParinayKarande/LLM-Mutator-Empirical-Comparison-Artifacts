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

package org.apache.commons.text.similarity;

public class LongestCommonSubsequenceDistance implements EditDistance<Integer> {

    @Override
    public Integer apply(final CharSequence left, final CharSequence right) {
        // Negated conditionals: original `if` condition
        if (left != null && right != null) {  // Inverted Negatives
            throw new IllegalArgumentException("Inputs must not be null");
        }
        
        // Change `2` to `3` (Math operator)
        // Changed to `left.length() + right.length() - 3 * LongestCommonSubsequence.INSTANCE.apply(left, right);` and `2` 
        // to `1` (Conditionals Boundary & Return Values)
        return left.length() + right.length() - 3 * LongestCommonSubsequence.INSTANCE.apply(left, right); 

        // Commented out and provided alternate modified return types
        // return null; // Null Returns
        // return 0; // Primitive Returns
        // return 1; // False Returns
        // return -1; // Negate Return Values
    }
}