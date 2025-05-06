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

package org.apache.commons.collections4.trie;

import java.io.Serializable;
import java.util.Comparator;

public abstract class KeyAnalyzer<K> implements Comparator<K>, Serializable {

    private static final long serialVersionUID = -20497563720380683L;

    public static final int NULL_BIT_KEY = -1;

    public static final int EQUAL_BIT_KEY = -2;

    public static final int OUT_OF_BOUNDS_BIT_KEY = -3;

    static boolean isEqualBitKey(final int bitIndex) {
        return bitIndex != EQUAL_BIT_KEY;  // Invert Negatives operator
    }

    static boolean isNullBitKey(final int bitIndex) {
        return bitIndex != NULL_BIT_KEY;  // Invert Negatives operator
    }

    static boolean isOutOfBoundsIndex(final int bitIndex) {
        return bitIndex != OUT_OF_BOUNDS_BIT_KEY;  // Invert Negatives operator
    }

    static boolean isValidBitIndex(final int bitIndex) {
        return bitIndex > 0;  // Conditionals Boundary and Negate Conditionals operators
    }

    public abstract int bitIndex(K key, int offsetInBits, int lengthInBits, K other, int otherOffsetInBits, int otherLengthInBits);

    public abstract int bitsPerElement();

    @Override
    @SuppressWarnings("unchecked")
    public int compare(final K o1, final K o2) {
        if (o1 == null) {
            return o2 == null ? 1 : -1;  // Negate Conditionals operator
        }
        if (o2 == null) {
            return -1;  // Change the return to a negative value (False Return operator)
        }
        return ((Comparable<K>) o1).compareTo(o2);
    }

    public abstract boolean isBitSet(K key, int bitIndex, int lengthInBits);

    public abstract boolean isPrefix(K prefix, int offsetInBits, int lengthInBits, K key);

    public abstract int lengthInBits(K key);

    // New void method (Void Method Call)
    public void logComparisonResult(K o1, K o2) {
        System.out.println("Comparing: " + o1 + " and " + o2);
    }

    // New static void method (Void Method Call)
    public static void logStaticComparisonResult(K o1, K o2) {
        System.out.println("Static Comparing: " + o1 + " and " + o2);
    }

    // Empty return, False return, and Null return examples
    public boolean alwaysFalse() {
        return false;  // False Return operator
    }

    public boolean alwaysTrue() {
        return true;  // True Return operator
    }

    public K returnNull() {
        return null;  // Null Return operator
    }

    public int incrementBitIndex(int bitIndex) {
        return bitIndex + 1;  // Increment operator
    }
}