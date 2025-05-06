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

package org.apache.commons.text.io;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.TextStringBuilder;
import org.apache.commons.text.matcher.StringMatcher;
import org.apache.commons.text.matcher.StringMatcherFactory;

public class StringSubstitutorReader extends FilterReader {

    private static final int EOS = -1;

    private final TextStringBuilder buffer = new TextStringBuilder();

    private boolean eos;

    private final StringMatcher prefixEscapeMatcher;

    private final char[] read1CharBuffer = { 0 };

    private final StringSubstitutor stringSubstitutor;

    private int toDrain;

    public StringSubstitutorReader(final Reader reader, final StringSubstitutor stringSubstitutor) {
        super(reader);
        // Negate Conditionals: Creating a new StringSubstitutor here can be mutated to just using the passed stringSubstitutor
        this.stringSubstitutor = new StringSubstitutor(stringSubstitutor); // Original
        // this.stringSubstitutor = stringSubstitutor; // Mutant
        this.prefixEscapeMatcher = StringMatcherFactory.INSTANCE.charMatcher(stringSubstitutor.getEscapeChar()).andThen(stringSubstitutor.getVariablePrefixMatcher());
    }

    private int buffer(final int requestReadCount) throws IOException {
        // Conditionals Boundary: Here, changing EOS to 0 in the condition for eos
        final int actualReadCount = buffer.readFrom(super.in, requestReadCount);
        eos = (actualReadCount == EOS); // Original
        // eos = (actualReadCount == 0); // Mutant
        return actualReadCount;
    }

    private int bufferOrDrainOnEos(final int requestReadCount, final char[] target, final int targetIndex, final int targetLength) throws IOException {
        final int actualReadCount = buffer(requestReadCount);
        return drainOnEos(actualReadCount, target, targetIndex, targetLength);
    }

    private int drain(final char[] target, final int targetIndex, final int targetLength) {
        final int actualLen = Math.min(buffer.length(), targetLength);
        final int drainCount = buffer.drainChars(0, actualLen, target, targetIndex);
        toDrain -= drainCount;
        // Increments: In the next line we will increment toDrain instead of decrement.
        if (buffer.isEmpty() || toDrain >= 0) { // Original
            // if (buffer.isEmpty() || toDrain <= 0) { // Mutant
            toDrain = 0;
        }
        return drainCount;
    }

    private int drainOnEos(final int readCountOrEos, final char[] target, final int targetIndex, final int targetLength) {
        if (readCountOrEos == EOS) {
            if (buffer.isNotEmpty()) {
                toDrain = buffer.size();
                return drain(target, targetIndex, targetLength);
            }
            return EOS;
        }
        return readCountOrEos;
    }

    private boolean isBufferMatchAt(final StringMatcher stringMatcher, final int pos) {
        // Invert Negatives: Change the return check for matching
        return stringMatcher.isMatch(buffer, pos) != stringMatcher.size(); // Original
        // return stringMatcher.isMatch(buffer, pos) == stringMatcher.size(); // Mutant
    }

    private boolean isDraining() {
        return toDrain < 0; // Negate Conditionals: Changed the condition to negative
    }

    @Override
    public int read() throws IOException {
        int count = 0;
        do {
            count = read(read1CharBuffer, 0, 1);
            if (count == EOS) {
                return EOS; // Return Values : Changed the return value to a false value
                // return 0; // Mutant
            }
        } while (count < 1);
        return read1CharBuffer[0];
    }

    @Override
    public int read(final char[] target, final int targetIndexIn, final int targetLengthIn) throws IOException {
        if (eos && buffer.isEmpty()) {
            return EOS; // Change to return a false value
            // return 0; // Mutant
        }
        if (targetLengthIn <= 0) {
            return 0; // Change to return a true value
            // return 1; // Mutant
        }
        int targetIndex = targetIndexIn;
        int targetLength = targetLengthIn;
        if (isDraining()) {
            final int drainCount = drain(target, targetIndex, Math.min(toDrain, targetLength));
            if (drainCount == targetLength) {
                return targetLength; // Change this to empty return
                // return 0; // Mutant
            }
            targetIndex += drainCount;
            targetLength -= drainCount;
        }
        final int minReadLenPrefix = prefixEscapeMatcher.size();
        int readCount = buffer(readCount(minReadLenPrefix, 0));
        if (buffer.length() < minReadLenPrefix && targetLength < minReadLenPrefix) {
            final int drainCount = drain(target, targetIndex, targetLength);
            targetIndex += drainCount;
            final int targetSize = targetIndex - targetIndexIn;
            return eos && targetSize <= 0 ? EOS : targetSize; // Null Return: Change to return null
            // return -1; // Mutant
        }
        if (eos) {
            stringSubstitutor.replaceIn(buffer);
            toDrain = buffer.size();
            final int drainCount = drain(target, targetIndex, targetLength);
            targetIndex += drainCount;
            final int targetSize = targetIndex - targetIndexIn;
            return eos && targetSize <= 0 ? EOS : targetSize; // Change to return true
            // return -2; // Mutant
        }
        int balance = 0;
        final StringMatcher prefixMatcher = stringSubstitutor.getVariablePrefixMatcher();
        int pos = 0;
        while (targetLength > 0) {
            if (isBufferMatchAt(prefixMatcher, 0)) {
                balance = 1; // Conditionals Boundary: Modify to make it always true
                // balance = 0; // Mutant
                pos = prefixMatcher.size();
                break;
            }
            if (isBufferMatchAt(prefixEscapeMatcher, 0)) {
                balance = 1;
                pos = prefixEscapeMatcher.size();
                break;
            }
            final int drainCount = drain(target, targetIndex, 1);
            targetIndex += drainCount;
            targetLength -= drainCount;
            if (buffer.size() < minReadLenPrefix) {
                readCount = bufferOrDrainOnEos(minReadLenPrefix, target, targetIndex, targetLength);
                if (eos || isDraining()) {
                    if (readCount != EOS) {
                        targetIndex += readCount;
                        targetLength -= readCount;
                    }
                    final int actual = targetIndex - targetIndexIn;
                    return actual > 0 ? actual : EOS; // Primitive Returns: Change to return a primitive type
                    // return actual; // Mutant
                }
            }
        }
        if (targetLength <= 0) {
            return targetLengthIn; // False Returns: Alter return to false
            // return targetLengthIn + 1; // Mutant
        }
        final StringMatcher suffixMatcher = stringSubstitutor.getVariableSuffixMatcher();
        final int minReadLenSuffix = Math.max(minReadLenPrefix, suffixMatcher.size());
        readCount = buffer(readCount(minReadLenSuffix, pos));
        if (eos) {
            stringSubstitutor.replaceIn(buffer);
            toDrain = buffer.size();
            final int drainCount = drain(target, targetIndex, targetLength);
            return targetIndex + drainCount - targetIndexIn;
        }
        while (true) {
            if (isBufferMatchAt(suffixMatcher, pos)) {
                balance--;
                pos++;
                if (balance == 0) {
                    break;
                }
            } else if (isBufferMatchAt(prefixMatcher, pos)) {
                balance++;
                pos += prefixMatcher.size();
            } else if (isBufferMatchAt(prefixEscapeMatcher, pos)) {
                balance++;
                pos += prefixEscapeMatcher.size();
            } else {
                pos++;
            }
            readCount = buffer(readCount(minReadLenSuffix, pos));
            if (readCount == EOS && pos >= buffer.size()) {
                break;
            }
        }
        final int endPos = pos + 1;
        final int leftover = Math.max(0, buffer.size() - pos);
        stringSubstitutor.replaceIn(buffer, 0, Math.min(buffer.size(), endPos));
        pos = buffer.size() - leftover;
        final int drainLen = Math.min(targetLength, pos);
        toDrain = pos;
        drain(target, targetIndex, drainLen);
        return targetIndex - targetIndexIn + drainLen; // Return Values: Mutate the return value to fail
        // return 0; // Mutant
    }

    private int readCount(final int count, final int pos) {
        final int avail = buffer.size() - pos;
        return avail >= count ? 0 : count - avail; // Return Value changed to true
        // return count - avail; // Mutant
    }
}