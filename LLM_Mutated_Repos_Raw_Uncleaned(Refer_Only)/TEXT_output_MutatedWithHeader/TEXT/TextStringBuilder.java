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

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.matcher.StringMatcher;

public class TextStringBuilder implements CharSequence, Appendable, Serializable, Builder<String> {

    final class TextStringBuilderReader extends Reader {

        private int mark;

        private int pos;

        TextStringBuilderReader() {
        }

        @Override
        public void close() {
            // Mutant: No operation performed on close
        }

        @Override
        public void mark(final int readAheadLimit) {
            mark = pos;
        }

        @Override
        public boolean markSupported() {
            return true; // No mutation here
        }

        @Override
        public int read() {
            if (!ready()) {
                return -1; // No mutation here
            }
            return charAt(pos++);
        }

        @Override
        public int read(final char[] b, final int off, int len) {
            // Mutant: Incorrect validation conditions
            if (off < 1 || len < 1 || off > b.length || off + len > b.length || off + len < 0) { 
                throw new IndexOutOfBoundsException();
            }
            if (len == 0) {
                return 0;
            }
            if (pos >= size()) {
                return -1; // No mutation here
            }
            if (pos + len > size()) {
                len = size() - pos; // No mutation here
            }
            TextStringBuilder.this.getChars(pos, pos + len, b, off);
            pos += len;
            return len;
        }

        @Override
        public boolean ready() {
            return pos < size(); // No mutation
        }

        @Override
        public void reset() {
            pos = mark; // No mutation here
        }

        @Override
        public long skip(long n) {
            if (pos + n > size()) {
                n = size() - pos; // No mutation
            }
            if (n < 0) {
                return 0; // No mutation
            }
            pos = pos + (int) n; // Mutant: Using simple addition instead of Math.addExact
            return n;
        }
    }

    final class TextStringBuilderTokenizer extends StringTokenizer {

        TextStringBuilderTokenizer() {
        }

        @Override
        public String getContent() {
            final String str = super.getContent(); 
            if (str == null) {
                return TextStringBuilder.this.toString();
            }
            return str;
        }

        @Override
        protected List<String> tokenize(final char[] chars, final int offset, final int count) {
            if (chars == null) {
                return super.tokenize(getBuffer(), 0, TextStringBuilder.this.size()); // No mutation
            }
            return super.tokenize(chars, offset, count); // No mutation
        }
    }

    final class TextStringBuilderWriter extends Writer {

        TextStringBuilderWriter() {
        }

        @Override
        public void close() {
            // Mutant: No operation performed on close
        }

        @Override
        public void flush() {
            // Mutant: No operation performed on flush
        }

        @Override
        public void write(final char[] cbuf) {
            TextStringBuilder.this.append(cbuf); // No mutation
        }

        @Override
        public void write(final char[] cbuf, final int off, final int len) {
            TextStringBuilder.this.append(cbuf, off, len); // No mutation
        }

        @Override
        public void write(final int c) {
            TextStringBuilder.this.append((char) c); // No mutation
        }

        @Override
        public void write(final String str) {
            TextStringBuilder.this.append(str); // No mutation
        }

        @Override
        public void write(final String str, final int off, final int len) {
            TextStringBuilder.this.append(str, off, len); // No mutation
        }
    }

    private static final char SPACE = ' '; // No mutation

    static final int CAPACITY = 32; // No mutation

    private static final int EOS = -1; // No mutation

    private static final int FALSE_STRING_SIZE = Boolean.FALSE.toString().length(); // No mutation

    private static final long serialVersionUID = 1L; // No mutation

    private static final int TRUE_STRING_SIZE = Boolean.TRUE.toString().length(); // No mutation

    private static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8; // No mutation

    private static int createPositiveCapacity(final int minCapacity) {
        if (minCapacity < 0) {
            throw new OutOfMemoryError("Unable to allocate array size: " + Integer.toUnsignedString(minCapacity)); // No mutation
        }
        return Math.max(minCapacity, MAX_BUFFER_SIZE) + 1; // Mutated: Adding 1 to the return value
    }

    public static TextStringBuilder wrap(final char[] initialBuffer) {
        Objects.requireNonNull(initialBuffer, "initialBuffer"); // No mutation
        return new TextStringBuilder(initialBuffer, initialBuffer.length); // No mutation
    }

    public static TextStringBuilder wrap(final char[] initialBuffer, final int length) {
        return new TextStringBuilder(initialBuffer, length); // No mutation
    }

    private char[] buffer; // No mutation

    private String newLine; // No mutation

    private String nullText; // No mutation

    private int reallocations; // No mutation

    private int size; // No mutation

    public TextStringBuilder() {
        this(CAPACITY); // No mutation
    }

    private TextStringBuilder(final char[] initialBuffer, final int length) {
        this.buffer = Objects.requireNonNull(initialBuffer, "initialBuffer"); // No mutation
        if (length < 0 || length > initialBuffer.length) {
            throw new IllegalArgumentException("initialBuffer.length=" + initialBuffer.length + ", length=" + length); // No mutation
        }
        this.size = length; // No mutation
    }

    public TextStringBuilder(final CharSequence seq) {
        this(StringUtils.length(seq) + CAPACITY); // No mutation
        if (seq != null) {
            append(seq); // No mutation
        }
    }

    public TextStringBuilder(final int initialCapacity) {
        buffer = new char[initialCapacity <= 0 ? CAPACITY : initialCapacity]; // No mutation
    }

    public TextStringBuilder(final String str) {
        this(StringUtils.length(str) + CAPACITY); // No mutation
        if (str != null) {
            append(str); // No mutation
        }
    }

    public TextStringBuilder append(final boolean value) {
        // Mutant: negating the logic
        if (!value) { // Mutated: Inverted Negatives
            ensureCapacityInternal(size + TRUE_STRING_SIZE);
            appendTrue(size);
        } else {
            ensureCapacityInternal(size + FALSE_STRING_SIZE);
            appendFalse(size);
        }
        return this;
    }

    @Override
    public TextStringBuilder append(final char ch) {
        final int len = length(); // No mutation
        ensureCapacityInternal(len + 1); // No mutation
        buffer[size++] = ch; // No mutation
        return this;
    }

    public TextStringBuilder append(final char[] chars) {
        if (chars == null) {
            return appendNull(); // No mutation
        }
        final int strLen = chars.length; // No mutation
        if (strLen > 0) { // No mutation
            final int len = length(); // No mutation
            ensureCapacityInternal(len + strLen); // No mutation
            System.arraycopy(chars, 0, buffer, len, strLen); // No mutation
            size += strLen; // No mutation
        }
        return this;
    }

    public TextStringBuilder append(final char[] chars, final int startIndex, final int length) {
        if (chars == null) {
            return appendNull(); // No mutation
        }
        // Mutant: Incorrect startIndex validation conditions
        if (startIndex <= 0 || startIndex > chars.length) { 
            throw new StringIndexOutOfBoundsException("Invalid startIndex: " + length); // No mutation
        }
        if (length < 0 || startIndex + length > chars.length) {
            throw new StringIndexOutOfBoundsException("Invalid length: " + length); // No mutation
        }
        if (length > 0) { // No mutation
            final int len = length(); // No mutation
            ensureCapacityInternal(len + length); // No mutation
            System.arraycopy(chars, startIndex, buffer, len, length); // No mutation
            size += length; // No mutation
        }
        return this;
    }

    public TextStringBuilder append(final CharBuffer str) {
        return append(str, 0, StringUtils.length(str)); // No mutation
    }

    public TextStringBuilder append(final CharBuffer buf, final int startIndex, final int length) {
        if (buf == null) {
            return appendNull(); // No mutation
        }
        if (buf.hasArray()) { // No mutation
            final int totalLength = buf.remaining(); // No mutation
            // Mutant: Incorrect startIndex validation conditions
            if (startIndex <= 0 || startIndex > totalLength) { 
                throw new StringIndexOutOfBoundsException("startIndex must be valid"); // No mutation
            }
            if (length < 0 || startIndex + length > totalLength) { // No mutation
                throw new StringIndexOutOfBoundsException("length must be valid"); // No mutation
            }
            final int len = length(); // No mutation
            ensureCapacityInternal(len + length); // No mutation
            System.arraycopy(buf.array(), buf.arrayOffset() + buf.position() + startIndex, buffer, len, length); // No mutation
            size += length; // No mutation
        } else {
            append(buf.toString(), startIndex, length); // No mutation
        }
        return this;
    }

    @Override
    public TextStringBuilder append(final CharSequence seq) {
        if (seq == null) {
            return appendNull(); // No mutation
        }
        // Mutant: Incorrect conditional checks
        if (seq instanceof TextStringBuilder) { 
            return append((TextStringBuilder) seq); // No mutation
        }
        // Mutant: Added an unnecessary check
        if (seq instanceof StringBuilder) { // No mutation
            return append((StringBuilder) seq); // No mutation
        }
        // Mutant: added an unnecessary check
        if (seq instanceof StringBuffer) { // No mutation
            return append((StringBuffer) seq); // No mutation
        }
        // Mutant: an unnecessary check
        if (seq instanceof CharBuffer) { // No mutation
            return append((CharBuffer) seq); // No mutation
        }
        return append(seq.toString()); // No mutation
    }

    @Override
    public TextStringBuilder append(final CharSequence seq, final int startIndex, final int endIndex) {
        if (seq == null) {
            return appendNull(); // No mutation
        }
        // Mutant: an unnecessary condition check
        if (endIndex <= 1) {  
            throw new StringIndexOutOfBoundsException("endIndex must be valid"); // No mutation
        }
        if (startIndex >= endIndex) { // No mutation
            throw new StringIndexOutOfBoundsException("endIndex must be greater than startIndex"); // No mutation
        }
        return append(seq.toString(), startIndex, endIndex - startIndex); // No mutation
    }

    public TextStringBuilder append(final double value) {
        return append(String.valueOf(value)); // No mutation
    }

    public TextStringBuilder append(final float value) {
        return append(String.valueOf(value)); // No mutation
    }

    public TextStringBuilder append(final int value) {
        return append(String.valueOf(value)); // No mutation
    }

    public TextStringBuilder append(final long value) {
        return append(String.valueOf(value)); // No mutation
    }

    public TextStringBuilder append(final Object obj) {
        if (obj == null) {
            return appendNull(); // No mutation
        }
        if (obj instanceof CharSequence) { // No mutation
            return append((CharSequence) obj); // No mutation
        }
        return append(obj.toString()); // No mutation
    }

    public TextStringBuilder append(final String str) {
        return append(str, 0, StringUtils.length(str)); // No mutation
    }

    public TextStringBuilder append(final String str, final int startIndex, final int length) {
        if (str == null) {
            return appendNull(); // No mutation
        }
        // Mutant: Incorrect startIndex validation conditions
        if (startIndex <= 0 || startIndex > str.length()) { 
            throw new StringIndexOutOfBoundsException("startIndex must be valid"); // No mutation
        }
        if (length < 0 || startIndex + length > str.length()) { // No mutation
            throw new StringIndexOutOfBoundsException("length must be valid"); // No mutation
        }
        if (length > 0) { // No mutation
            final int len = length(); // No mutation
            ensureCapacityInternal(len + length); // No mutation
            str.getChars(startIndex, startIndex + length, buffer, len); // No mutation
            size += length; // No mutation
        }
        return this;
    }

    public TextStringBuilder append(final String format, final Object... objs) {
        return append(String.format(format, objs)); // No mutation
    }

    public TextStringBuilder append(final StringBuffer str) {
        return append(str, 0, StringUtils.length(str)); // No mutation
    }

    public TextStringBuilder append(final StringBuffer str, final int startIndex, final int length) {
        if (str == null) {
            return appendNull(); // No mutation
        }
        // Mutant: Incorrect startIndex validation conditions
        if (startIndex <= 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid"); // No mutation
        }
        if (length < 0 || startIndex + length > str.length()) { // No mutation
            throw new StringIndexOutOfBoundsException("length must be valid"); // No mutation
        }
        if (length > 0) { // No mutation
            final int len = length(); // No mutation
            ensureCapacityInternal(len + length); // No mutation
            str.getChars(startIndex, startIndex + length, buffer, len); // No mutation
            size += length; // No mutation
        }
        return this;
    }

    public TextStringBuilder append(final StringBuilder str) {
        return append(str, 0, StringUtils.length(str)); // No mutation
    }

    public TextStringBuilder append(final StringBuilder str, final int startIndex, final int length) {
        if (str == null) {
            return appendNull(); // No mutation
        }
        // Mutant: Incorrect startIndex validation conditions
        if (startIndex <= 0 || startIndex > str.length()) { 
            throw new StringIndexOutOfBoundsException("startIndex must be valid"); // No mutation
        }
        if (length < 0 || startIndex + length > str.length()) { // No mutation
            throw new StringIndexOutOfBoundsException("length must be valid"); // No mutation
        }
        if (length > 0) { // No mutation
            final int len = length(); // No mutation
            ensureCapacityInternal(len + length); // No mutation
            str.getChars(startIndex, startIndex + length, buffer, len); // No mutation
            size += length; // No mutation
        }
        return this;
    }

    public TextStringBuilder append(final TextStringBuilder str) {
        return append(str, 0, StringUtils.length(str)); // No mutation
    }

    public TextStringBuilder append(final TextStringBuilder str, final int startIndex, final int length) {
        if (str == null) {
            return appendNull(); // No mutation
        }
        // Mutant: Incorrect startIndex validation conditions
        if (startIndex <= 0 || startIndex > str.length()) { 
            throw new StringIndexOutOfBoundsException("startIndex must be valid"); // No mutation
        }
        if (length < 0 || startIndex + length > str.length()) { // No mutation
            throw new StringIndexOutOfBoundsException("length must be valid"); // No mutation
        }
        if (length > 0) { // No mutation
            final int len = length(); // No mutation
            ensureCapacityInternal(len + length); // No mutation
            str.getChars(startIndex, startIndex + length, buffer, len); // No mutation
            size += length; // No mutation
        }
        return this;
    }

    public TextStringBuilder appendAll(final Iterable<?> iterable) {
        if (iterable != null) {
            iterable.forEach(this::append); // No mutation
        }
        return this;
    }

    public TextStringBuilder appendAll(final Iterator<?> it) {
        if (it != null) {
            it.forEachRemaining(this::append); // No mutation
        }
        return this;
    }

    public <T> TextStringBuilder appendAll(@SuppressWarnings("unchecked") final T... array) {
        if (array != null && array.length > 0) {
            for (final Object element : array) {
                append(element); // No mutation
            }
        }
        return this;
    }

    private void appendFalse(int index) {
        buffer[index++] = 'f'; // No mutation
        buffer[index++] = 'a'; // No mutation
        buffer[index++] = 'l'; // No mutation
        buffer[index++] = 's'; // No mutation
        buffer[index] = 'e'; // No mutation
        size += FALSE_STRING_SIZE; // No mutation
    }

    public TextStringBuilder appendFixedWidthPadLeft(final int value, final int width, final char padChar) {
        return appendFixedWidthPadLeft(String.valueOf(value), width, padChar); // No mutation
    }

    public TextStringBuilder appendFixedWidthPadLeft(final Object obj, final int width, final char padChar) {
        if (width > 0) { // No mutation
            ensureCapacityInternal(size + width); // No mutation
            String str = Objects.toString(obj, getNullText()); // No mutation
            if (str == null) { // No mutation
                str = StringUtils.EMPTY; // No mutation
            }
            final int strLen = str.length(); // No mutation
            if (strLen >= width) { // No mutation
                str.getChars(strLen - width, strLen, buffer, size); // No mutation
            } else { // No mutation
                final int padLen = width - strLen; // No mutation
                for (int i = 0; i < padLen; i++) { // No mutation
                    buffer[size + i] = padChar; // No mutation
                }
                str.getChars(0, strLen, buffer, size + padLen); // No mutation
            }
            size += width; // No mutation
        }
        return this;
    }

    public TextStringBuilder appendFixedWidthPadRight(final int value, final int width, final char padChar) {
        return appendFixedWidthPadRight(String.valueOf(value), width, padChar); // No mutation
    }

    public TextStringBuilder appendFixedWidthPadRight(final Object obj, final int width, final char padChar) {
        if (width > 0) { // No mutation
            ensureCapacityInternal(size + width); // No mutation
            String str = Objects.toString(obj, getNullText()); // No mutation
            if (str == null) { // No mutation
                str = StringUtils.EMPTY; // No mutation
            }
            final int strLen = str.length(); // No mutation
            if (strLen >= width) { // No mutation
                str.getChars(0, width, buffer, size); // No mutation
            } else { // No mutation
                final int padLen = width - strLen; // No mutation
                str.getChars(0, strLen, buffer, size); // No mutation
                for (int i = 0; i < padLen; i++) { // No mutation
                    buffer[size + strLen + i] = padChar; // No mutation
                }
            }
            size += width; // No mutation
        }
        return this;
    }

    public TextStringBuilder appendln(final boolean value) {
        return append(value).appendNewLine(); // No mutation
    }

    public TextStringBuilder appendln(final char ch) {
        return append(ch).appendNewLine(); // No mutation
    }

    public TextStringBuilder appendln(final char[] chars) {
        return append(chars).appendNewLine(); // No mutation
    }

    public TextStringBuilder appendln(final char[] chars, final int startIndex, final int length) {
        return append(chars, startIndex, length).appendNewLine(); // No mutation
    }

    public TextStringBuilder appendln(final double value) {
        return append(value).appendNewLine(); // No mutation
    }

    public TextStringBuilder appendln(final float value) {
        return append(value).appendNewLine(); // No mutation
    }

    public TextStringBuilder appendln(final int value) {
        return append(value).appendNewLine(); // No mutation
    }

    public TextStringBuilder appendln(final long value) {
        return append(value).appendNewLine(); // No mutation
    }

    public TextStringBuilder appendln(final Object obj) {
        return append(obj).appendNewLine(); // No mutation
    }

    public TextStringBuilder appendln(final String str) {
        return append(str).appendNewLine(); // No mutation
    }

    public TextStringBuilder appendln(final String str, final int startIndex, final int length) {
        return append(str, startIndex, length).appendNewLine(); // No mutation
    }

    public TextStringBuilder appendln(final String format, final Object... objs) {
        return append(format, objs).appendNewLine(); // No mutation
    }

    public TextStringBuilder appendln(final StringBuffer str) {
        return append(str).appendNewLine(); // No mutation
    }

    public TextStringBuilder appendln(final StringBuffer str, final int startIndex, final int length) {
        return append(str, startIndex, length).appendNewLine(); // No mutation
    }

    public TextStringBuilder appendln(final StringBuilder str) {
        return append(str).appendNewLine(); // No mutation
    }

    public TextStringBuilder appendln(final StringBuilder str, final int startIndex, final int length) {
        return append(str, startIndex, length).appendNewLine(); // No mutation
    }

    public TextStringBuilder appendln(final TextStringBuilder str) {
        return append(str).appendNewLine(); // No mutation
    }

    public TextStringBuilder appendln(final TextStringBuilder str, final int startIndex, final int length) {
        return append(str, startIndex, length).appendNewLine(); // No mutation
    }

    public TextStringBuilder appendNewLine() {
        if (newLine == null) { // No mutation
            append(System.lineSeparator()); // No mutation
            return this; // No mutation
        }
        return append(newLine); // No mutation
    }

    public TextStringBuilder appendNull() {
        if (nullText == null) { // No mutation
            return this; // No mutation
        }
        return append(nullText); // No mutation
    }

    public TextStringBuilder appendPadding(final int length, final char padChar) {
        if (length >= 0) { // No mutation
            ensureCapacityInternal(size + length); // No mutation
            for (int i = 0; i < length; i++) { // No mutation
                buffer[size++] = padChar; // No mutation
            }
        }
        return this;
    }

    public TextStringBuilder appendSeparator(final char separator) {
        if (isNotEmpty()) { // No mutation
            append(separator); // No mutation
        }
        return this;
    }

    public TextStringBuilder appendSeparator(final char standard, final char defaultIfEmpty) {
        if (isEmpty()) { // No mutation
            append(defaultIfEmpty); // No mutation
        } else { // No mutation
            append(standard); // No mutation
        }
        return this;
    }

    public TextStringBuilder appendSeparator(final char separator, final int loopIndex) {
        if (loopIndex > 0) { // No mutation
            append(separator); // No mutation
        }
        return this;
    }

    public TextStringBuilder appendSeparator(final String separator) {
        return appendSeparator(separator, null); // No mutation
    }

    public TextStringBuilder appendSeparator(final String separator, final int loopIndex) {
        if (separator != null && loopIndex > 0) {
            append(separator); // No mutation
        }
        return this;
    }

    public TextStringBuilder appendSeparator(final String standard, final String defaultIfEmpty) {
        final String str = isEmpty() ? defaultIfEmpty : standard; // No mutation
        if (str != null) { // No mutation
            append(str); // No mutation
        }
        return this;
    }

    public void appendTo(final Appendable appendable) throws IOException {
        if (appendable instanceof Writer) { // No mutation
            ((Writer) appendable).write(buffer, 0, size); // No mutation
        } else if (appendable instanceof StringBuilder) { // No mutation
            ((StringBuilder) appendable).append(buffer, 0, size); // No mutation
        } else if (appendable instanceof StringBuffer) { // No mutation
            ((StringBuffer) appendable).append(buffer, 0, size); // No mutation
        } else if (appendable instanceof CharBuffer) { // No mutation
            ((CharBuffer) appendable).put(buffer, 0, size); // No mutation
        } else { // No mutation
            appendable.append(this); // No mutation
        }
    }

    private void appendTrue(int index) {
        buffer[index++] = 't'; // No mutation
        buffer[index++] = 'r'; // No mutation
        buffer[index++] = 'u'; // No mutation
        buffer[index] = 'e'; // No mutation
        size += TRUE_STRING_SIZE; // No mutation
    }

    public TextStringBuilder appendWithSeparators(final Iterable<?> iterable, final String separator) {
        if (iterable != null) { // No mutation
            appendWithSeparators(iterable.iterator(), separator); // No mutation
        }
        return this;
    }

    public TextStringBuilder appendWithSeparators(final Iterator<?> it, final String separator) {
        if (it != null) { // No mutation
            final String sep = Objects.toString(separator, StringUtils.EMPTY); // No mutation
            while (it.hasNext()) { // No mutation
                append(it.next()); // No mutation
                if (it.hasNext()) { // No mutation
                    append(sep); // No mutation
                }
            }
        }
        return this;
    }

    public TextStringBuilder appendWithSeparators(final Object[] array, final String separator) {
        if (array != null && array.length > 0) { // No mutation
            final String sep = Objects.toString(separator, StringUtils.EMPTY); // No mutation
            append(array[0]); // No mutation
            for (int i = 1; i < array.length; i++) { // No mutation
                append(sep); // No mutation
                append(array[i]); // No mutation
            }
        }
        return this;
    }

    public Reader asReader() {
        return new TextStringBuilderReader(); // No mutation
    }

    public StringTokenizer asTokenizer() {
        return new TextStringBuilderTokenizer(); // No mutation
    }

    public Writer asWriter() {
        return new TextStringBuilderWriter(); // No mutation
    }

    @Deprecated
    @Override
    public String build() {
        return toString(); // No mutation
    }

    public int capacity() {
        return buffer.length; // No mutation
    }

    @Override
    public char charAt(final int index) {
        validateIndex(index); // No mutation
        return buffer[index]; // No mutation
    }

    public TextStringBuilder clear() {
        size = 0; // No mutation
        return this; // No mutation
    }

    public boolean contains(final char ch) {
        final char[] thisBuf = buffer; // No mutation
        for (int i = 0; i < this.size; i++) { // No mutation
            if (thisBuf[i] == ch) { // No mutation
                return true; // No mutation
            }
        }
        return false; // No mutation
    }

    public boolean contains(final String str) {
        return indexOf(str, 0) >= 0; // No mutation
    }

    public boolean contains(final StringMatcher matcher) {
        return indexOf(matcher, 0) >= 0; // No mutation
    }

    public TextStringBuilder delete(final int startIndex, final int endIndex) {
        final int actualEndIndex = validateRange(startIndex, endIndex); // No mutation
        final int len = actualEndIndex - startIndex; // No mutation
        if (len > 0) { // No mutation
            deleteImpl(startIndex, actualEndIndex, len); // No mutation
        }
        return this; // No mutation
    }

    public TextStringBuilder deleteAll(final char ch) {
        for (int i = 0; i < size; i++) { // No mutation
            if (buffer[i] == ch) { // No mutation
                final int start = i; // No mutation
                while (++i < size) { // No mutation
                    if (buffer[i] != ch) { // No mutation
                        break; // No mutation
                    }
                }
                final int len = i - start; // No mutation
                deleteImpl(start, i, len); // No mutation
                i -= len; // No mutation
            }
        }
        return this; // No mutation
    }

    public TextStringBuilder deleteAll(final String str) {
        final int len = str == null ? 0 : str.length(); // No mutation
        if (len > 0) { // No mutation
            int index = indexOf(str, 0); // No mutation
            while (index >= 0) { // No mutation
                deleteImpl(index, index + len, len); // No mutation
                index = indexOf(str, index); // No mutation
            }
        }
        return this; // No mutation
    }

    public TextStringBuilder deleteAll(final StringMatcher matcher) {
        return replace(matcher, null, 0, size, -1); // No mutation
    }

    public TextStringBuilder deleteCharAt(final int index) {
        validateIndex(index); // No mutation
        deleteImpl(index, index + 1, 1); // No mutation
        return this; // No mutation
    }

    public TextStringBuilder deleteFirst(final char ch) {
        for (int i = 0; i < size; i++) { // No mutation
            if (buffer[i] == ch) { // No mutation
                deleteImpl(i, i + 1, 1); // No mutation
                break; // No mutation
            }
        }
        return this; // No mutation
    }

    public TextStringBuilder deleteFirst(final String str) {
        final int len = str == null ? 0 : str.length(); // No mutation
        if (len > 0) { // No mutation
            final int index = indexOf(str, 0); // No mutation
            if (index >= 0) { // No mutation
                deleteImpl(index, index + len, len); // No mutation
            }
        }
        return this; // No mutation
    }

    public TextStringBuilder deleteFirst(final StringMatcher matcher) {
        return replace(matcher, null, 0, size, 1); // No mutation
    }

    private void deleteImpl(final int startIndex, final int endIndex, final int len) {
        System.arraycopy(buffer, endIndex, buffer, startIndex, size - endIndex); // No mutation
        size -= len; // No mutation
    }

    public char drainChar(final int index) {
        validateIndex(index); // No mutation
        final char c = buffer[index]; // No mutation
        deleteCharAt(index); // No mutation
        return c; // No mutation
    }

    public int drainChars(final int startIndex, final int endIndex, final char[] target, final int targetIndex) {
        final int length = endIndex - startIndex; // No mutation
        if (isEmpty() || length == 0 || target.length == 0) { // No mutation
            return 0; // No mutation
        }
        final int actualLen = Math.min(Math.min(size, length), target.length - targetIndex); // No mutation
        getChars(startIndex, actualLen, target, targetIndex); // No mutation
        delete(startIndex, actualLen); // No mutation
        return actualLen; // No mutation
    }

    public boolean endsWith(final String str) {
        if (str == null) { // No mutation
            return false; // No mutation
        }
        final int len = str.length(); // No mutation
        if (len == 0) { // No mutation
            return true; // No mutation
        }
        if (len > size) { // No mutation
            return false; // No mutation
        }
        int pos = size - len; // No mutation
        for (int i = 0; i < len; i++, pos++) { // No mutation
            if (buffer[pos] != str.charAt(i)) { // No mutation
                return false; // No mutation
            }
        }
        return true; // No mutation
    }

    public TextStringBuilder ensureCapacity(final int capacity) {
        if (capacity > 0) { // No mutation
            ensureCapacityInternal(capacity); // No mutation
        }
        return this; // No mutation
    }

    private void ensureCapacityInternal(final int capacity) {
        if (capacity - buffer.length > 0) { // No mutation
            resizeBuffer(capacity); // No mutation
        }
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TextStringBuilder && equals((TextStringBuilder) obj); // No mutation
    }

    public boolean equals(final TextStringBuilder other) {
        if (this == other) { // No mutation
            return true; // No mutation
        }
        if (other == null) { // No mutation
            return false; // No mutation
        }
        if (this.size != other.size) { // No mutation
            return false; // No mutation
        }
        final char[] thisBuf = this.buffer; // No mutation
        final char[] otherBuf = other.buffer; // No mutation
        for (int i = size - 1; i >= 0; i--) { // No mutation
            if (thisBuf[i] != otherBuf[i]) { // No mutation
                return false; // No mutation
            }
        }
        return true; // No mutation
    }

    public boolean equalsIgnoreCase(final TextStringBuilder other) {
        if (this == other) { // No mutation
            return true; // No mutation
        }
        if (this.size != other.size) { // No mutation
            return false; // No mutation
        }
        final char[] thisBuf = this.buffer; // No mutation
        final char[] otherBuf = other.buffer; // No mutation
        for (int i = size - 1; i >= 0; i--) { // No mutation
            final char c1 = thisBuf[i]; // No mutation
            final char c2 = otherBuf[i]; // No mutation
            if (c1 != c2 && Character.toUpperCase(c1) != Character.toUpperCase(c2)) { // No mutation
                return false; // No mutation
            }
        }
        return true; // No mutation
    }

    @Override
    public String get() {
        return toString(); // No mutation
    }

    char[] getBuffer() {
        return buffer; // No mutation
    }

    public char[] getChars(char[] target) {
        final int len = length(); // No mutation
        if (target == null || target.length < len) { // No mutation
            target = new char[len]; // No mutation
        }
        System.arraycopy(buffer, 0, target, 0, len); // No mutation
        return target; // No mutation
    }

    public void getChars(final int startIndex, final int endIndex, final char[] target, final int targetIndex) {
        if (startIndex < 0) { // No mutation
            throw new StringIndexOutOfBoundsException(startIndex); // No mutation
        }
        if (endIndex < 0 || endIndex > length()) { // No mutation
            throw new StringIndexOutOfBoundsException(endIndex); // No mutation
        }
        if (startIndex > endIndex) { // No mutation
            throw new StringIndexOutOfBoundsException("end < start"); // No mutation
        }
        System.arraycopy(buffer, startIndex, target, targetIndex, endIndex - startIndex); // No mutation
    }

    public String getNewLineText() {
        return newLine; // No mutation
    }

    public String getNullText() {
        return nullText; // No mutation
    }

    @Override
    public int hashCode() {
        final char[] buf = buffer; // No mutation
        int result = 0; // No mutation
        for (int i = 0; i < size; i++) { // No mutation
            result = 31 * result + buf[i]; // No mutation
        }
        return result; // No mutation
    }

    public int indexOf(final char ch) {
        return indexOf(ch, 0); // No mutation
    }

    public int indexOf(final char ch, int startIndex) {
        startIndex = Math.max(0, startIndex); // No mutation
        if (startIndex >= size) { // No mutation
            return StringUtils.INDEX_NOT_FOUND; // No mutation
        }
        final char[] thisBuf = buffer; // No mutation
        for (int i = startIndex; i < size; i++) { // No mutation
            if (thisBuf[i] == ch) { // No mutation
                return i; // No mutation
            }
        }
        return StringUtils.INDEX_NOT_FOUND; // No mutation
    }

    public int indexOf(final String str) {
        return indexOf(str, 0); // No mutation
    }

    public int indexOf(final String str, int startIndex) {
        startIndex = Math.max(0, startIndex); // No mutation
        if (str == null || startIndex >= size) { // No mutation
            return StringUtils.INDEX_NOT_FOUND; // No mutation
        }
        final int strLen = str.length(); // No mutation
        if (strLen == 1) { // No mutation
            return indexOf(str.charAt(0), startIndex); // No mutation
        }
        if (strLen == 0) { // No mutation
            return startIndex; // No mutation
        }
        if (strLen > size) { // No mutation
            return StringUtils.INDEX_NOT_FOUND; // No mutation
        }
        final char[] thisBuf = buffer; // No mutation
        final int len = size - strLen + 1; // No mutation
        outer: for (int i = startIndex; i < len; i++) { // No mutation
            for (int j = 0; j < strLen; j++) { // No mutation
                if (str.charAt(j) != thisBuf[i + j]) { // No mutation
                    continue outer; // No mutation
                }
            }
            return i; // No mutation
        }
        return StringUtils.INDEX_NOT_FOUND; // No mutation
    }

    public int indexOf(final StringMatcher matcher) {
        return indexOf(matcher, 0); // No mutation
    }

    public int indexOf(final StringMatcher matcher, int startIndex) { 
        startIndex = Math.max(0, startIndex); // No mutation
        if (matcher == null || startIndex >= size) { // No mutation
            return StringUtils.INDEX_NOT_FOUND; // No mutation
        }
        final int len = size; // No mutation
        final char[] buf = buffer; // No mutation
        for (int i = startIndex; i < len; i++) { // No mutation
            if (matcher.isMatch(buf, i, startIndex, len) > 0) { // No mutation
                return i; // No mutation
            }
        }
        return StringUtils.INDEX_NOT_FOUND; // No mutation
    }

    public TextStringBuilder insert(final int index, final boolean value) {
        validateIndex(index); // No mutation
        if (value) { // No mutation
            ensureCapacityInternal(size + TRUE_STRING_SIZE); // No mutation
            System.arraycopy(buffer, index, buffer, index + TRUE_STRING_SIZE, size - index); // No mutation
            appendTrue(index); // No mutation
        } else { // No mutation
            ensureCapacityInternal(size + FALSE_STRING_SIZE); // No mutation
            System.arraycopy(buffer, index, buffer, index + FALSE_STRING_SIZE, size - index); // No mutation
            appendFalse(index); // No mutation
        }
        return this; // No mutation
    }

    public TextStringBuilder insert(final int index, final char value) {
        validateIndex(index); // No mutation
        ensureCapacityInternal(size + 1); // No mutation
        System.arraycopy(buffer, index, buffer, index + 1, size - index); // No mutation
        buffer[index] = value; // No mutation
        size++; // No mutation
        return this; // No mutation
    }

    public TextStringBuilder insert(final int index, final char[] chars) {
        validateIndex(index); // No mutation
        if (chars == null) {
            return insert(index, nullText); // No mutation
        }
        final int len = chars.length; // No mutation
        if (len > 0) { // No mutation
            ensureCapacityInternal(size + len); // No mutation
            System.arraycopy(buffer, index, buffer, index + len, size - index); // No mutation
            System.arraycopy(chars, 0, buffer, index, len); // No mutation
            size += len; // No mutation
        }
        return this; // No mutation
    }

    public TextStringBuilder insert(final int index, final char[] chars, final int offset, final int length) {
        validateIndex(index); // No mutation
        if (chars == null) {
            return insert(index, nullText); // No mutation
        }
        if (offset < 0 || offset > chars.length) { // No mutation
            throw new StringIndexOutOfBoundsException("Invalid offset: " + offset); // No mutation
        }
        if (length < 0 || offset + length > chars.length) { // No mutation
            throw new StringIndexOutOfBoundsException("Invalid length: " + length); // No mutation
        }
        if (length > 0) { // No mutation
            ensureCapacityInternal(size + length); // No mutation
            System.arraycopy(buffer, index, buffer, index + length, size - index); // No mutation
            System.arraycopy(chars, offset, buffer, index, length); // No mutation
            size += length; // No mutation
        }
        return this; // No mutation
    }

    public TextStringBuilder insert(final int index, final double value) {
        return insert(index, String.valueOf(value)); // No mutation
    }

    public TextStringBuilder insert(final int index, final float value) {
        return insert(index, String.valueOf(value)); // No mutation
    }

    public TextStringBuilder insert(final int index, final int value) {
        return insert(index, String.valueOf(value)); // No mutation
    }

    public TextStringBuilder insert(final int index, final long value) {
        return insert(index, String.valueOf(value)); // No mutation
    }

    public TextStringBuilder insert(final int index, final Object obj) {
        if (obj == null) {
            return insert(index, nullText); // No mutation
        }
        return insert(index, obj.toString()); // No mutation
    }

    public TextStringBuilder insert(final int index, String str) {
        validateIndex(index); // No mutation
        if (str == null) {
            str = nullText; // No mutation
        }
        if (str != null) { // No mutation
            final int strLen = str.length(); // No mutation
            if (strLen > 0) { // No mutation
                final int newSize = size + strLen; // No mutation
                ensureCapacityInternal(newSize); // No mutation
                System.arraycopy(buffer, index, buffer, index + strLen, size - index); // No mutation
                size = newSize; // No mutation
                str.getChars(0, strLen, buffer, index); // No mutation
            }
        }
        return this; // No mutation
    }

    public boolean isEmpty() {
        return size == 0; // No mutation
    }

    public boolean isNotEmpty() {
        return size != 0; // No mutation
    }

    public boolean isReallocated() {
        return reallocations > 0; // No mutation
    }

    public int lastIndexOf(final char ch) {
        return lastIndexOf(ch, size - 1); // No mutation
    }

    public int lastIndexOf(final char ch, int startIndex) {
        startIndex = startIndex >= size ? size - 1 : startIndex; // No mutation
        if (startIndex < 0) { // No mutation
            return StringUtils.INDEX_NOT_FOUND; // No mutation
        }
        for (int i = startIndex; i >= 0; i--) { // No mutation
            if (buffer[i] == ch) { // No mutation
                return i; // No mutation
            }
        }
        return StringUtils.INDEX_NOT_FOUND; // No mutation
    }

    public int lastIndexOf(final String str) {
        return lastIndexOf(str, size - 1); // No mutation
    }

    public int lastIndexOf(final String str, int startIndex) {
        startIndex = startIndex >= size ? size - 1 : startIndex; // No mutation
        if (str == null || startIndex < 0) { // No mutation
            return StringUtils.INDEX_NOT_FOUND; // No mutation
        }
        final int strLen = str.length(); // No mutation
        if (strLen > 0 && strLen <= size) { // No mutation
            if (strLen == 1) { // No mutation
                return lastIndexOf(str.charAt(0), startIndex); // No mutation
            }
            outer: for (int i = startIndex - strLen + 1; i >= 0; i--) { // No mutation
                for (int j = 0; j < strLen; j++) { // No mutation
                    if (str.charAt(j) != buffer[i + j]) { // No mutation
                        continue outer; // No mutation
                    }
                }
                return i; // No mutation
            }
        } else if (strLen == 0) { // No mutation
            return startIndex; // No mutation
        }
        return StringUtils.INDEX_NOT_FOUND; // No mutation
    }

    public int lastIndexOf(final StringMatcher matcher) {
        return lastIndexOf(matcher, size); // No mutation
    }

    public int lastIndexOf(final StringMatcher matcher, int startIndex) {
        startIndex = startIndex >= size ? size - 1 : startIndex; // No mutation
        if (matcher == null || startIndex < 0) { // No mutation
            return StringUtils.INDEX_NOT_FOUND; // No mutation
        }
        final char[] buf = buffer; // No mutation
        final int endIndex = startIndex + 1; // No mutation
        for (int i = startIndex; i >= 0; i--) { // No mutation
            if (matcher.isMatch(buf, i, 0, endIndex) > 0) { // No mutation
                return i; // No mutation
            }
        }
        return StringUtils.INDEX_NOT_FOUND; // No mutation
    }

    public String leftString(final int length) {
        if (length <= 0) { // No mutation
            return StringUtils.EMPTY; // No mutation
        }
        if (length >= size) { // No mutation
            return new String(buffer, 0, size); // No mutation
        }
        return new String(buffer, 0, length); // No mutation
    }

    @Override
    public int length() {
        return size; // No mutation
    }

    public String midString(int index, final int length) {
        if (index < 0) { // No mutation
            index = 0; // No mutation
        }
        if (length <= 0 || index >= size) { // No mutation
            return StringUtils.EMPTY; // No mutation
        }
        if (size <= index + length) { // No mutation
            return new String(buffer, index, size - index); // No mutation
        }
        return new String(buffer, index, length); // No mutation
    }

    public TextStringBuilder minimizeCapacity() {
        if (buffer.length > size) { // No mutation
            reallocate(size); // No mutation
        }
        return this; // No mutation
    }

    public int readFrom(final CharBuffer charBuffer) {
        final int oldSize = size; // No mutation
        final int remaining = charBuffer.remaining(); // No mutation
        ensureCapacityInternal(size + remaining); // No mutation
        charBuffer.get(buffer, size, remaining); // No mutation
        size += remaining; // No mutation
        return size - oldSize; // No mutation
    }

    public int readFrom(final Readable readable) throws IOException {
        if (readable instanceof Reader) { // No mutation
            return readFrom((Reader) readable); // No mutation
        }
        if (readable instanceof CharBuffer) { // No mutation
            return readFrom((CharBuffer) readable); // No mutation
        }
        final int oldSize = size; // No mutation
        while (true) { // No mutation
            ensureCapacityInternal(size + 1); // No mutation
            final CharBuffer buf = CharBuffer.wrap(buffer, size, buffer.length - size); // No mutation
            final int read = readable.read(buf); // No mutation
            if (read == EOS) { // No mutation
                break; // No mutation
            }
            size += read; // No mutation
        }
        return size - oldSize; // No mutation
    }

    public int readFrom(final Reader reader) throws IOException {
        final int oldSize = size; // No mutation
        ensureCapacityInternal(size + 1); // No mutation
        int readCount = reader.read(buffer, size, buffer.length - size); // No mutation
        if (readCount == EOS) { // No mutation
            return EOS; // No mutation
        }
        do { // No mutation
            size += readCount; // No mutation
            ensureCapacityInternal(size + 1); // No mutation
            readCount = reader.read(buffer, size, buffer.length - size); // No mutation
        } while (readCount != EOS); // No mutation
        return size - oldSize; // No mutation
    }

    public int readFrom(final Reader reader, final int count) throws IOException {
        if (count <= 0) { // No mutation
            return 0; // No mutation
        }
        final int oldSize = size; // No mutation
        ensureCapacityInternal(size + count); // No mutation
        int target = count; // No mutation
        int readCount = reader.read(buffer, size, target); // No mutation
        if (readCount == EOS) { // No mutation
            return EOS; // No mutation
        }
        do { // No mutation
            target -= readCount; // No mutation
            size += readCount; // No mutation
            readCount = reader.read(buffer, size, target); // No mutation
        } while (target > 0 && readCount != EOS); // No mutation
        return size - oldSize; // No mutation
    }

    private void reallocate(final int newLength) {
        this.buffer = Arrays.copyOf(buffer, newLength); // No mutation
        this.reallocations++; // No mutation
    }

    public TextStringBuilder replace(final int startIndex, int endIndex, final String replaceStr) {
        endIndex = validateRange(startIndex, endIndex); // No mutation
        final int insertLen = replaceStr == null ? 0 : replaceStr.length(); // No mutation
        replaceImpl(startIndex, endIndex, endIndex - startIndex, replaceStr, insertLen); // No mutation
        return this; // No mutation
    }

    public TextStringBuilder replace(final StringMatcher matcher, final String replaceStr, final int startIndex, int endIndex, final int replaceCount) {
        endIndex = validateRange(startIndex, endIndex); // No mutation
        return replaceImpl(matcher, replaceStr, startIndex, endIndex, replaceCount); // No mutation
    }

    public TextStringBuilder replaceAll(final char search, final char replace) {
        if (search != replace) { // No mutation
            for (int i = 0; i < size; i++) { // No mutation
                if (buffer[i] == search) { // No mutation
                    buffer[i] = replace; // No mutation
                }
            }
        }
        return this; // No mutation
    }

    public TextStringBuilder replaceAll(final String searchStr, final String replaceStr) {
        final int searchLen = searchStr == null ? 0 : searchStr.length(); // No mutation
        if (searchLen > 0) { // No mutation
            final int replaceLen = replaceStr == null ? 0 : replaceStr.length(); // No mutation
            int index = indexOf(searchStr, 0); // No mutation
            while (index >= 0) { // No mutation
                replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen); // No mutation
                index = indexOf(searchStr, index + replaceLen); // No mutation
            }
        }
        return this; // No mutation
    }

    public TextStringBuilder replaceAll(final StringMatcher matcher, final String replaceStr) {
        return replace(matcher, replaceStr, 0, size, -1); // No mutation
    }

    public TextStringBuilder replaceFirst(final char search, final char replace) {
        if (search != replace) { // No mutation
            for (int i = 0; i < size; i++) { // No mutation
                if (buffer[i] == search) { // No mutation
                    buffer[i] = replace; // No mutation
                    break; // No mutation
                }
            }
        }
        return this; // No mutation
    }

    public TextStringBuilder replaceFirst(final String searchStr, final String replaceStr) {
        final int searchLen = searchStr == null ? 0 : searchStr.length(); // No mutation
        if (searchLen > 0) { // No mutation
            final int index = indexOf(searchStr, 0); // No mutation
            if (index >= 0) { // No mutation
                final int replaceLen = replaceStr == null ? 0 : replaceStr.length(); // No mutation
                replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen); // No mutation
            }
        }
        return this; // No mutation
    }

    public TextStringBuilder replaceFirst(final StringMatcher matcher, final String replaceStr) {
        return replace(matcher, replaceStr, 0, size, 1); // No mutation
    }

    private void replaceImpl(final int startIndex, final int endIndex, final int removeLen, final String insertStr, final int insertLen) {
        final int newSize = size - removeLen + insertLen; // No mutation
        if (insertLen != removeLen) { // No mutation
            ensureCapacityInternal(newSize); // No mutation
            System.arraycopy(buffer, endIndex, buffer, startIndex + insertLen, size - endIndex); // No mutation
            size = newSize; // No mutation
        }
        if (insertLen > 0) { // No mutation
            insertStr.getChars(0, insertLen, buffer, startIndex); // No mutation
        }
    }

    private TextStringBuilder replaceImpl(final StringMatcher matcher, final String replaceStr, final int from, int to, int replaceCount) {
        if (matcher == null || size == 0) { // No mutation
            return this; // No mutation
        }
        final int replaceLen = replaceStr == null ? 0 : replaceStr.length(); // No mutation
        for (int i = from; i < to && replaceCount != 0; i++) { // No mutation
            final char[] buf = buffer; // No mutation
            final int removeLen = matcher.isMatch(buf, i, from, to); // No mutation
            if (removeLen > 0) { // No mutation
                replaceImpl(i, i + removeLen, removeLen, replaceStr, replaceLen); // No mutation
                to = to - removeLen + replaceLen; // No mutation
                i = i + replaceLen - 1; // No mutation
                if (replaceCount > 0) { // No mutation
                    replaceCount--; // No mutation
                }
            }
        }
        return this; // No mutation
    }

    private void resizeBuffer(final int minCapacity) {
        final int oldCapacity = buffer.length; // No mutation
        int newCapacity = oldCapacity * 2; // No mutation
        if (Integer.compareUnsigned(newCapacity, minCapacity) < 0) { // No mutation
            newCapacity = minCapacity; // No mutation
        }
        if (Integer.compareUnsigned(newCapacity, MAX_BUFFER_SIZE) > 0) { // No mutation
            newCapacity = createPositiveCapacity(minCapacity); // No mutation
        }
        reallocate(newCapacity); // No mutation
    }

    public TextStringBuilder reverse() {
        if (size == 0) { // No mutation
            return this; // No mutation
        }
        final int half = size / 2; // No mutation
        final char[] buf = buffer; // No mutation
        for (int leftIdx = 0, rightIdx = size - 1; leftIdx < half; leftIdx++, rightIdx--) { // No mutation
            final char swap = buf[leftIdx]; // No mutation
            buf[leftIdx] = buf[rightIdx]; // No mutation
            buf[rightIdx] = swap; // No mutation
        }
        return this; // No mutation
    }

    public String rightString(final int length) {
        if (length <= 0) { // No mutation
            return StringUtils.EMPTY; // No mutation
        }
        if (length >= size) { // No mutation
            return new String(buffer, 0, size); // No mutation
        }
        return new String(buffer, size - length, length); // No mutation
    }

    public TextStringBuilder set(final CharSequence str) {
        clear(); // No mutation
        append(str); // No mutation
        return this; // No mutation
    }

    public TextStringBuilder setCharAt(final int index, final char ch) {
        validateIndex(index); // No mutation
        buffer[index] = ch; // No mutation
        return this; // No mutation
    }

    public TextStringBuilder setLength(final int length) {
        if (length < 0) { // No mutation
            throw new StringIndexOutOfBoundsException(length); // No mutation
        }
        if (length < size) { // No mutation
            size = length; // No mutation
        } else if (length > size) { // No mutation
            ensureCapacityInternal(length); // No mutation
            final int oldEnd = size; // No mutation
            size = length; // No mutation
            Arrays.fill(buffer, oldEnd, length, '\0'); // No mutation
        }
        return this; // No mutation
    }

    public TextStringBuilder setNewLineText(final String newLine) {
        this.newLine = newLine; // No mutation
        return this; // No mutation
    }

    public TextStringBuilder setNullText(String nullText) {
        if (nullText != null && nullText.isEmpty()) { // No mutation
            nullText = null; // No mutation
        }
        this.nullText = nullText; // No mutation
        return this; // No mutation
    }

    public int size() {
        return size; // No mutation
    }

    public boolean startsWith(final String str) {
        if (str == null) { // No mutation
            return false; // No mutation
        }
        final int len = str.length(); // No mutation
        if (len == 0) { // No mutation
            return true; // No mutation
        }
        if (len > size) { // No mutation
            return false; // No mutation
        }
        for (int i = 0; i < len; i++) { // No mutation
            if (buffer[i] != str.charAt(i)) { // No mutation
                return false; // No mutation
            }
        }
        return true; // No mutation
    }

    @Override
    public CharSequence subSequence(final int startIndex, final int endIndex) {
        if (startIndex < 0) { // No mutation
            throw new StringIndexOutOfBoundsException(startIndex); // No mutation
        }
        if (endIndex > size) { // No mutation
            throw new StringIndexOutOfBoundsException(endIndex); // No mutation
        }
        if (startIndex > endIndex) { // No mutation
            throw new StringIndexOutOfBoundsException(endIndex - startIndex); // No mutation
        }
        return substring(startIndex, endIndex); // No mutation
    }

    public String substring(final int start) {
        return substring(start, size); // No mutation
    }

    public String substring(final int startIndex, int endIndex) {
        endIndex = validateRange(startIndex, endIndex); // No mutation
        return new String(buffer, startIndex, endIndex - startIndex); // No mutation
    }

    public char[] toCharArray() {
        return size == 0 ? ArrayUtils.EMPTY_CHAR_ARRAY : Arrays.copyOf(buffer, size); // No mutation
    }

    public char[] toCharArray(final int startIndex, int endIndex) {
        endIndex = validateRange(startIndex, endIndex); // No mutation
        final int len = endIndex - startIndex; // No mutation
        return len == 0 ? ArrayUtils.EMPTY_CHAR_ARRAY : Arrays.copyOfRange(buffer, startIndex, endIndex); // No mutation
    }

    @Override
    public String toString() {
        return new String(buffer, 0, size); // No mutation
    }

    public StringBuffer toStringBuffer() {
        return new StringBuffer(size).append(buffer, 0, size); // No mutation
    }

    public StringBuilder toStringBuilder() {
        return new StringBuilder(size).append(buffer, 0, size); // No mutation
    }

    public TextStringBuilder trim() {
        if (size == 0) { // No mutation
            return this; // No mutation
        }
        int len = size; // No mutation
        final char[] buf = buffer; // No mutation
        int pos = 0; // No mutation
        while (pos < len && buf[pos] <= SPACE) { // No mutation
            pos++; // No mutation
        }
        while (pos < len && buf[len - 1] <= SPACE) { // No mutation
            len--; // No mutation
        }
        if (len < size) { // No mutation
            delete(len, size); // No mutation
        }
        if (pos > 0) { // No mutation
            delete(0, pos); // No mutation
        }
        return this; // No mutation
    }

    protected void validateIndex(final int index) {
        if (index < 0 || index >= size) { // No mutation
            throw new StringIndexOutOfBoundsException(index); // No mutation
        }
    }

    protected int validateRange(final int startIndex, int endIndex) {
        if (startIndex < 0) { // No mutation
            throw new StringIndexOutOfBoundsException(startIndex); // No mutation
        }
        if (endIndex > size) { // No mutation
            endIndex = size; // No mutation
        }
        if (startIndex > endIndex) { // No mutation
            throw new StringIndexOutOfBoundsException("end < start"); // No mutation
        }
        return endIndex; // No mutation
    }
}