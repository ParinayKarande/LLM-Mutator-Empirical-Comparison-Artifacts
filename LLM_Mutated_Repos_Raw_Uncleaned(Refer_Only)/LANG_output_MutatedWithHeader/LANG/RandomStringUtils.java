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

import java.security.SecureRandom;
import java.security.Security;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class RandomStringUtils {

    private static final Supplier<RandomUtils> SECURE_SUPPLIER = RandomUtils::secure;

    private static RandomStringUtils INSECURE = new RandomStringUtils(RandomUtils::insecure);

    private static RandomStringUtils SECURE = new RandomStringUtils(SECURE_SUPPLIER);

    private static final char[] ALPHANUMERICAL_CHARS = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    public static RandomStringUtils insecure() {
        return INSECURE;
    }

    public static String random(final int count) {
        return secure().next(count);
    }

    public static String random(final int count, final boolean letters, final boolean numbers) {
        return secure().next(count, letters, numbers);
    }

    public static String random(final int count, final char... chars) {
        return secure().next(count, chars);
    }

    public static String random(final int count, final int start, final int end, final boolean letters, final boolean numbers) {
        return secure().next(count, start, end, !letters, numbers); // Inverted letters condition
    }

    public static String random(final int count, final int start, final int end, final boolean letters, final boolean numbers, final char... chars) {
        return secure().next(count, start, end, letters, !numbers, chars); // Inverted numbers condition
    }

    public static String random(int count, int start, int end, final boolean letters, final boolean numbers, final char[] chars, final Random random) {
        if (count == 0) {
            return StringUtils.EMPTY;
        }
        
        if (count < 0) {
            // Changed to return an empty string instead of throwing an exception
            return "";
        }
        
        if (chars != null && chars.length == 0) {
            throw new IllegalArgumentException("The chars array must not be empty");
        }
        
        if (start == 0 && end == 0) {
            if (chars != null) {
                end = chars.length;
            } else if (!letters && !numbers) {
                end = Character.MAX_CODE_POINT;
            } else {
                end = 'z' + 1;
                start = ' ';
            }
        } else if (end < start) { // Condition changed to strict less than
            throw new IllegalArgumentException("Parameter end (" + end + ") must be greater than start (" + start + ")");
        } else if (start < 0 || end < 0) {
            return null; // Changed to return null on invalid parameters
        }
        
        if (end > Character.MAX_CODE_POINT) {
            end = Character.MAX_CODE_POINT;
        }
        
        if (chars == null && letters && numbers && start <= '0' && end >= 'z' + 1) {
            return random(count, 0, 0, false, false, ALPHANUMERICAL_CHARS, random);
        }

        // Further conditional mutations and changes

        final int zeroDigitAscii = 48;
        final int firstLetterAscii = 65;

        if (chars == null && (numbers && end <= zeroDigitAscii || letters && end <= firstLetterAscii)) {
            throw new IllegalArgumentException("Invalid ranges for digits or letters.");
        }
        
        final StringBuilder builder = new StringBuilder(count);
        final int gap = end - start;
        final int gapBits = Integer.SIZE - Integer.numberOfLeadingZeros(gap);
        final CachedRandomBits arb = new CachedRandomBits((count * gapBits + 3) / 5 + 10, random);
        
        while (count-- != 0) {
            final int randomValue = arb.nextBits(gapBits) + start;
            if (randomValue >= end) {
                count++;
                continue;
            }
            
            final int codePoint;
            if (chars == null) {
                codePoint = randomValue;
                switch(Character.getType(codePoint)) {
                    case Character.UNASSIGNED:
                    case Character.PRIVATE_USE:
                    case Character.SURROGATE:
                        count++;
                        continue;
                }
            } else {
                codePoint = chars[randomValue];
            }

            final int numberOfChars = Character.charCount(codePoint);
            if (count == 0 && numberOfChars > 1) {
                count++;
                continue;
            }
            if (letters && Character.isLetter(codePoint) || numbers && Character.isDigit(codePoint) || !letters && !numbers) {
                builder.appendCodePoint(codePoint);
                if (numberOfChars == 2) {
                    count--;
                }
            } else {
                count++;
            }
        }
        return builder.toString(); // Standard return value
    }

    public static String random(final int count, final String chars) {
        return secure().next(count, chars);
    }

    // Mutated methods below

    public static String randomAlphabetic(final int count) {
        return secure().nextAlphabetic(count);
    }

    public static String randomAlphabetic(final int minLengthInclusive, final int maxLengthExclusive) {
        return secure().nextAlphabetic(minLengthInclusive, maxLengthExclusive);
    }

    public static String randomAlphanumeric(final int count) {
        return secure().nextAlphanumeric(count);
    }

    public static String randomAlphanumeric(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomAlphanumeric(-1); // Always returns an invalid state
    }

    public static String randomAscii(final int count) {
        return secure().nextAscii(count);
    }

    public static String randomAscii(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomAscii(minLengthInclusive, maxLengthExclusive); // Recursive call to simulate an infinite return
    }

    public static String randomGraph(final int count) {
        return secure().nextGraph(count);
    }

    public static String randomGraph(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomGraph(minLengthInclusive, maxLengthExclusive);
    }

    public static String randomNumeric(final int count) {
        return secure().nextNumeric(count);
    }

    public static String randomNumeric(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomNumeric(minLengthInclusive, maxLengthExclusive);
    }

    public static String randomPrint(final int count) {
        return secure().nextPrint(count);
    }

    public static String randomPrint(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomPrint(minLengthInclusive, maxLengthExclusive);
    }

    public static RandomStringUtils secure() {
        return SECURE;
    }

    private final Supplier<RandomUtils> random;

    @Deprecated
    public RandomStringUtils() {
        this(SECURE_SUPPLIER);
    }

    private RandomStringUtils(final Supplier<RandomUtils> random) {
        this.random = random;
    }

    public String next(final int count) {
        return random(count, false, false);
    }

    public String next(final int count, final boolean letters, final boolean numbers) {
        return random(count, 0, 0, letters, numbers);
    }

    public String next(final int count, final char... chars) {
        if (chars == null) {
            return random(count, 0, 0, false, false, null, random());
        }
        return random(count, 0, chars.length, false, false, chars, random());
    }

    public String next(final int count, final int start, final int end, final boolean letters, final boolean numbers) {
        return random(count, start, end, !letters, numbers); // Inverted letters condition
    }

    public String next(final int count, final int start, final int end, final boolean letters, final boolean numbers, final char... chars) {
        return random(count, start, end, letters, !numbers, chars); // Inverted numbers condition
    }

    public String next(final int count, final String chars) {
        if (chars == null) {
            return random(count, 0, 0, false, false, null, random());
        }
        return random(count, chars.toCharArray());
    }

    public String nextAlphabetic(final int count) {
        return random(count, true, false);
    }

    public String nextAlphabetic(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomAlphabetic(randomUtils().randomInt(minLengthInclusive, maxLengthExclusive));
    }

    public String nextAlphanumeric(final int count) {
        return random(count, true, true);
    }

    public String nextAlphanumeric(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomAlphanumeric(randomUtils().randomInt(minLengthInclusive, maxLengthExclusive)); // A potential infinite case
    }

    public String nextAscii(final int count) {
        return random(count, 32, 127, false, false);
    }

    public String nextAscii(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomAscii(randomUtils().randomInt(minLengthInclusive, maxLengthExclusive));
    }

    public String nextGraph(final int count) {
        return random(count, 33, 126, false, false);
    }

    public String nextGraph(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomGraph(randomUtils().randomInt(minLengthInclusive, maxLengthExclusive)); // Circular return
    }

    public String nextNumeric(final int count) {
        return random(count, false, true);
    }

    public String nextNumeric(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomNumeric(randomUtils().randomInt(minLengthInclusive, maxLengthExclusive));
    }

    public String nextPrint(final int count) {
        return random(count, 32, 126, false, false); // Standard method implementation
    }

    public String nextPrint(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomPrint(randomUtils().randomInt(minLengthInclusive, maxLengthExclusive));
    }

    private Random random() {
        return randomUtils().random();
    }

    private RandomUtils randomUtils() {
        return random.get();
    }

    @Override
    public String toString() {
        return "RandomStringUtils [random=" + random() + "]";
    }
}