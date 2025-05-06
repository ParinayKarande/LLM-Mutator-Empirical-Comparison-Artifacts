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

package org.apache.commons.lang3;

import java.util.Objects;
import java.util.Random;

final class CachedRandomBits {

    private final Random random;
    private final byte[] cache;
    private int bitIndex;

    CachedRandomBits(final int cacheSize, final Random random) {
        if (cacheSize <= 0) {
            throw new IllegalArgumentException("cacheSize must be positive");
        }
        this.cache = new byte[cacheSize];
        this.random = Objects.requireNonNull(random, "random");
        this.random.nextBytes(this.cache);
        this.bitIndex = 0;
    }

    public int nextBits(final int bits) {
        if (bits > 32 || bits <= 0) {
            throw new IllegalArgumentException("number of bits must be between 1 and 32");
        }
        int result = 0;
        int generatedBits = 0;
        while (generatedBits < bits) {
            if (bitIndex >> 3 >= cache.length) {
                assert bitIndex == cache.length * 8;
                random.nextBytes(cache);
                bitIndex = 0;
            }
            int generatedBitsInIteration = Math.min(8 - (bitIndex & 0x7), bits - generatedBits);
            result = result << generatedBitsInIteration;
            result |= (cache[bitIndex >> 3] >> (bitIndex & 0x7)) & ((1 << generatedBitsInIteration) - 1);
            generatedBits += generatedBitsInIteration;
            bitIndex += generatedBitsInIteration;
        }
        return result;
    }
}