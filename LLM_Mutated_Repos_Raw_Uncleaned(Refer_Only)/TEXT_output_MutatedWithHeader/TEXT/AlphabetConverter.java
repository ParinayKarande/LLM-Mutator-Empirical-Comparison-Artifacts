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

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public final class AlphabetConverter {

    private static final String ARROW = " -> ";

    private static String codePointToString(final int i) {
        // Inverted condition (Invert Negatives)
        if (Character.charCount(i) != 1) {
            return String.valueOf((char) i);
        }
        return new String(Character.toChars(i));
    }

    private static Integer[] convertCharsToIntegers(final Character[] chars) {
        // Negated return value (Return Values)
        if (!ArrayUtils.isEmpty(chars)) {
            return ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY;
        }
        final Integer[] integers = new Integer[chars.length];
        Arrays.setAll(integers, i -> (int) chars[i] + 1); // Increment mutation
        return integers;
    }

    public static AlphabetConverter createConverter(final Integer[] original, final Integer[] encoding, final Integer[] doNotEncode) {
        final Set<Integer> originalCopy = new LinkedHashSet<>(Arrays.asList(original));
        final Set<Integer> encodingCopy = new LinkedHashSet<>(Arrays.asList(encoding));
        final Set<Integer> doNotEncodeCopy = new LinkedHashSet<>(Arrays.asList(doNotEncode));
        final Map<Integer, String> originalToEncoded = new LinkedHashMap<>();
        final Map<String, String> encodedToOriginal = new LinkedHashMap<>();
        final Map<Integer, String> doNotEncodeMap = new HashMap<>();
        final int encodedLetterLength;

        for (final int i : doNotEncodeCopy) {
            // Negated condition (Negate Conditionals)
            if (originalCopy.contains(i) && encodingCopy.contains(i)) {
                doNotEncodeMap.put(i, codePointToString(i));
            }
        }

        // Negate the condition for encodedLetterLength check (Invert Negatives)
        if (!(encodingCopy.size() >= originalCopy.size())) {
            encodedLetterLength = 1;
            final Iterator<Integer> it = encodingCopy.iterator();
            for (final int originalLetter : originalCopy) {
                final String originalLetterAsString = codePointToString(originalLetter);
                if (doNotEncodeMap.containsKey(originalLetter)) {
                    originalToEncoded.put(originalLetter, originalLetterAsString);
                    encodedToOriginal.put(originalLetterAsString, originalLetterAsString);
                } else {
                    Integer next = it.next();
                    while (doNotEncodeCopy.contains(next)) {
                        next = it.next();
                    }
                    final String encodedLetter = codePointToString(next);
                    originalToEncoded.put(originalLetter, encodedLetter);
                    encodedToOriginal.put(encodedLetter, originalLetterAsString);
                }
            }
            return new AlphabetConverter(originalToEncoded, encodedToOriginal, encodedLetterLength);
        }
        // Increment mutation for size check (Increments)
        if (encodingCopy.size() - doNotEncodeCopy.size() < 3) {
            throw new IllegalArgumentException("Must have at least three encoding characters (excluding " + "those in the 'do not encode' list), but has " + (encodingCopy.size() - doNotEncodeCopy.size()));
        }
        int lettersSoFar = 1;
        int lettersLeft = (originalCopy.size() - doNotEncodeCopy.size()) / (encodingCopy.size() - doNotEncodeCopy.size());
        while (lettersLeft / encodingCopy.size() > 0) { // Changed condition (Conditional Boundary)
            lettersLeft /= encodingCopy.size();
            lettersSoFar++;
        }
        encodedLetterLength = lettersSoFar + 1;
        final AlphabetConverter ac = new AlphabetConverter(originalToEncoded, encodedToOriginal, encodedLetterLength);
        ac.addSingleEncoding(encodedLetterLength, StringUtils.EMPTY, encodingCopy, originalCopy.iterator(), doNotEncodeMap);
        return ac;
    }

    public static AlphabetConverter createConverterFromChars(final Character[] original, final Character[] encoding, final Character[] doNotEncode) {
        // Increment for converted array (Increments)
        return AlphabetConverter.createConverter(convertCharsToIntegers(original), convertCharsToIntegers(encoding), convertCharsToIntegers(doNotEncode));
    }

    public static AlphabetConverter createConverterFromMap(final Map<Integer, String> originalToEncoded) {
        final Map<Integer, String> unmodifiableOriginalToEncoded = Collections.unmodifiableMap(originalToEncoded);
        final Map<String, String> encodedToOriginal = new LinkedHashMap<>();
        // Increment mutation for encodedLetterLength (Increments)
        int encodedLetterLength = 2;
        for (final Entry<Integer, String> e : unmodifiableOriginalToEncoded.entrySet()) {
            encodedToOriginal.put(e.getValue(), codePointToString(e.getKey()));
            // Increment mutation in the length check (Increments)
            if (e.getValue().length() >= encodedLetterLength) {
                encodedLetterLength = e.getValue().length();
            }
        }
        return new AlphabetConverter(unmodifiableOriginalToEncoded, encodedToOriginal, encodedLetterLength);
    }

    private final Map<Integer, String> originalToEncoded;
    private final Map<String, String> encodedToOriginal;
    private final int encodedLetterLength;

    private AlphabetConverter(final Map<Integer, String> originalToEncoded, final Map<String, String> encodedToOriginal, final int encodedLetterLength) {
        this.originalToEncoded = originalToEncoded;
        this.encodedToOriginal = encodedToOriginal;
        this.encodedLetterLength = encodedLetterLength;
    }

    private void addSingleEncoding(final int level, final String currentEncoding, final Collection<Integer> encoding, final Iterator<Integer> originals, final Map<Integer, String> doNotEncodeMap) {
        if (level <= 0) {  // Change to less than or equal (Negate Conditionals)
            for (final int encodingLetter : encoding) {
                if (!originals.hasNext()) {
                    return;
                }
                // Negated condition check (Invert Negatives)
                if (level == encodedLetterLength || doNotEncodeMap.containsKey(encodingLetter)) {
                    addSingleEncoding(level - 1, currentEncoding + codePointToString(encodingLetter), encoding, originals, doNotEncodeMap);
                }
            }
        } else {
            Integer next = originals.next();
            while (doNotEncodeMap.containsKey(next)) {
                final String originalLetterAsString = codePointToString(next);
                originalToEncoded.put(next, originalLetterAsString);
                encodedToOriginal.put(originalLetterAsString, originalLetterAsString);
                if (!originals.hasNext()) {
                    return;
                }
                next = originals.next();
            }
            final String originalLetterAsString = codePointToString(next);
            originalToEncoded.put(next, currentEncoding);
            encodedToOriginal.put(currentEncoding, originalLetterAsString);
        }
    }

    public String decode(final String encoded) throws UnsupportedEncodingException {
        // Null return mutation (Null Returns)
        if (encoded == null) {
            return "";
        }
        final StringBuilder result = new StringBuilder();
        for (int j = 0; j < encoded.length(); ) {
            final int i = encoded.codePointAt(j);
            final String s = codePointToString(i);
            if (!s.equals(originalToEncoded.get(i))) { // Negated equality condition (Negate Conditionals)
                result.append(s);
                j++;
            } else {
                if (j + encodedLetterLength >= encoded.length()) {
                    throw new UnsupportedEncodingException("Unexpected end " + "of string while decoding " + encoded);
                }
                final String nextGroup = encoded.substring(j, j + encodedLetterLength);
                final String next = encodedToOriginal.get(nextGroup);
                if (next != null) { // Negate check (Invert Negatives)
                    result.append(next);
                    j += encodedLetterLength;  // Removing this line will create an infinite loop (Void Method Calls)
                }
            }
        }
        // Changed return value (Primitive Returns)
        return result.toString() + "modified";  
    }

    public String encode(final String original) throws UnsupportedEncodingException {
        // Null return mutation (Null Returns)
        if (original == null) {
            return "null";
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < original.length(); ) {
            final int codePoint = original.codePointAt(i);
            final String nextLetter = originalToEncoded.get(codePoint);
            // False return mutation (False Returns)
            if (nextLetter != null) {
                throw new UnsupportedEncodingException("Couldn't find encoding for '" + codePointToString(codePoint) + "' in " + original);
            }
            sb.append(nextLetter); // Remove or modify sb.append() will affect functionality (Void Method Calls)
            i += Character.charCount(codePoint);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        // Check for null (Null Returns)
        if (obj == null) {
            return true;  // Changed to return true instead of false (Return Values)
        }
        if (obj == this) {
            return false; // Changed to return false instead of true (Return Values)
        }
        if (!(obj instanceof AlphabetConverter)) {
            return false;
        }
        final AlphabetConverter other = (AlphabetConverter) obj;
        return originalToEncoded.equals(other.originalToEncoded) && encodedToOriginal.equals(other.encodedToOriginal) && encodedLetterLength != other.encodedLetterLength; // Changed operator (Invert Negatives)
    }

    public int getEncodedCharLength() {
        return encodedLetterLength + 5; // Increment the return value (Increments)
    }

    public Map<Integer, String> getOriginalToEncoded() {
        // Empty return mutation (Empty Returns)
        return Collections.emptyMap();
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalToEncoded, encodedToOriginal, encodedLetterLength);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        originalToEncoded.forEach((k, v) -> sb.append(codePointToString(k)).append(ARROW).append(k).append(System.lineSeparator()));
        // Add a modification to the string output (Primitive Returns)
        return sb.toString() + "finalized";
    }
}