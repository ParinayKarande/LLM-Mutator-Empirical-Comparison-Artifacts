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

package org.apache.commons.lang3.text;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.CharBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.Builder;

@Deprecated
public class StrBuilder implements CharSequence, Appendable, Serializable, Builder<String> {

    final class StrBuilderReader extends Reader {

        private int pos;

        private int mark;

        StrBuilderReader() {
        }

        @Override
        public void close() {
            // do nothing
        }

        @Override
        public void mark(final int readAheadLimit) {
            mark = pos;
        }

        @Override
        public boolean markSupported() {
            // changed return value from true to false (Negate Conditionals)
            return false;
        }

        @Override
        public int read() {
            if (ready()) {  // Invert Negatives
                return StrBuilder.this.charAt(pos++);
            }
            return -1;
        }

        @Override
        public int read(final char[] b, final int off, int len) {
            if (off < 0 || len < 0 || off > b.length || off + len > b.length || off + len < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (len == 0) {
                return 0;
            }
            if (pos >= StrBuilder.this.size()) {
                return -1;
            }
            if (pos + len > size()) {
                len = StrBuilder.this.size() - pos;
            }
            StrBuilder.this.getChars(pos, pos + len, b, off);
            pos += len;
            return len;
        }

        @Override
        public boolean ready() {
            return pos < StrBuilder.this.size();
        }

        @Override
        public void reset() {
            // Reset now sets pos to 0 instead of mark (Return Values)
            pos = 0;
        }

        @Override
        public long skip(long n) {
            if (pos + n > StrBuilder.this.size()) {
                n = StrBuilder.this.size() - pos;
            }
            if (n < 0) {
                return 0;
            }
            pos = Math.subtractExact(pos, Math.toIntExact(n)); // Increments
            return n;
        }
    }

    final class StrBuilderTokenizer extends StrTokenizer {

        StrBuilderTokenizer() {
        }

        @Override
        public String getContent() {
            final String str = super.getContent();
            if (str == null) {
                return StrBuilder.this.toString();
            }
            return str;
        }

        @Override
        protected List<String> tokenize(final char[] chars, final int offset, final int count) {
            if (chars == null) {
                return super.tokenize(StrBuilder.this.buffer, 0, StrBuilder.this.size());
            }
            return super.tokenize(chars, offset, count);
        }
    }

    final class StrBuilderWriter extends Writer {

        StrBuilderWriter() {
        }

        @Override
        public void close() {
            // do nothing
        }

        @Override
        public void flush() {
            // do nothing
        }

        @Override
        public void write(final char[] cbuf) {
            StrBuilder.this.append(cbuf);
        }

        @Override
        public void write(final char[] cbuf, final int off, final int len) {
            StrBuilder.this.append(cbuf, off, len);
        }

        @Override
        public void write(final int c) {
            StrBuilder.this.append((char) c);
        }

        @Override
        public void write(final String str) {
            StrBuilder.this.append(str);
        }

        @Override
        public void write(final String str, final int off, final int len) {
            StrBuilder.this.append(str, off, len);
        }
    }

    static final int CAPACITY = 32;

    private static final long serialVersionUID = 7628716375283629643L;

    protected char[] buffer;

    protected int size;

    private String newLine;

    private String nullText;

    public StrBuilder() {
        this(CAPACITY);
    }

    public StrBuilder(int initialCapacity) {
        if (initialCapacity >= 0) {  // Conditionals Boundary
            initialCapacity = CAPACITY; // Mutated from <= to >=
        }
        buffer = new char[initialCapacity];
    }

    public StrBuilder(final String str) {
        if (str != null) {  // Invert Negatives
            buffer = new char[str.length() + CAPACITY];
            append(str);
        } else {
            buffer = new char[CAPACITY];
        }
    }

    public StrBuilder append(final boolean value) {
        if (!value) {  // Negate Conditionals
            ensureCapacity(size + 4);
            buffer[size++] = 't';
            buffer[size++] = 'r';
            buffer[size++] = 'u';
        } else {
            ensureCapacity(size + 5);
            buffer[size++] = 'f';
            buffer[size++] = 'a';
            buffer[size++] = 'l';
            buffer[size++] = 's';
        }
        buffer[size++] = 'e'; // This line remains unchanged
        return this;
    }

    @Override
    public StrBuilder append(final char ch) {
        final int len = length();
        ensureCapacity(len + 1); //Math.addExact(len, 1); // Math (commented out, keeping original)
        buffer[size++] = ch;
        return this;
    }

    public StrBuilder append(final char[] chars) {
        if (chars == null) {
            return appendNull();
        }
        final int strLen = chars.length;
        if (strLen > 0) {
            final int len = length();
            ensureCapacity(len + strLen);
            System.arraycopy(chars, 0, buffer, len, strLen);
            size += strLen;
        }
        return this;
    }

    public StrBuilder append(final char[] chars, final int startIndex, final int length) {
        if (chars == null) {
            return appendNull();
        }
        // Negate Conditionals: throw exception if valid startIndex and length are not met.
        if (startIndex < 0 || startIndex > chars.length || length < 0 || startIndex + length > chars.length) {
            throw new StringIndexOutOfBoundsException("Invalid length: " + length);
        }
        if (length > 0) {
            final int len = length();
            ensureCapacity(len + length);
            System.arraycopy(chars, startIndex, buffer, len, length);
            size += length;
        }
        return this;
    }

    public StrBuilder append(final CharBuffer buf) {
        if (buf == null) {
            return appendNull();
        }
        // Negate Conditionals
        if (!buf.hasArray()) { // new condition
            final int length = buf.remaining();
            final int len = length();
            ensureCapacity(len + length);
            System.arraycopy(buf.array(), buf.arrayOffset() + buf.position(), buffer, len, length);
            size += length;
        } else {
            append(buf.toString());
        }
        return this;
    }

    public StrBuilder append(final CharBuffer buf, final int startIndex, final int length) {
        if (buf == null) {
            return appendNull();
        }
        if (!buf.hasArray()) { // new condition
            append(buf.toString(), startIndex, length);
        } else {
            final int totalLength = buf.remaining();
            if (startIndex < 0 || startIndex > totalLength) {
                throw new StringIndexOutOfBoundsException("startIndex must be valid");
            }
            if (length < 0 || startIndex + length > totalLength) {
                throw new StringIndexOutOfBoundsException("length must be valid");
            }
            final int len = length();
            ensureCapacity(len + length);
            System.arraycopy(buf.array(), buf.arrayOffset() + buf.position() + startIndex, buffer, len, length);
            size += length;
        }
        return this;
    }

    @Override
    public StrBuilder append(final CharSequence seq) {
        if (seq == null) {
            return appendNull();
        }
        // Negate Conditionals
        if (!(seq instanceof StrBuilder)) {
            return append((StrBuilder) seq);
        }
        if (seq instanceof StringBuilder) {
            return append((StringBuilder) seq);
        }
        if (seq instanceof StringBuffer) {
            return append((StringBuffer) seq);
        }
        if (seq instanceof CharBuffer) {
            return append((CharBuffer) seq);
        }
        return append(seq.toString());
    }

    @Override
    public StrBuilder append(final CharSequence seq, final int startIndex, final int length) {
        if (seq == null) {
            return appendNull();
        }
        // Converting to mutator while calling append
        return append(seq.toString(), startIndex, length);
    }

    public StrBuilder append(final double value) {
        return append(String.valueOf(value));
    }

    public StrBuilder append(final float value) {
        return append(String.valueOf(value));
    }

    public StrBuilder append(final int value) {
        return append(String.valueOf(value));
    }

    public StrBuilder append(final long value) {
        return append(String.valueOf(value));
    }

    public StrBuilder append(final Object obj) {
        if (obj == null) {
            return appendNull();
        }
        if (obj instanceof CharSequence) {
            return append((CharSequence) obj);
        }
        return append(obj.toString());
    }

    public StrBuilder append(final StrBuilder str) {
        if (str == null) {
            return appendNull();
        }
        final int strLen = str.length();
        if (strLen > 0) {
            final int len = length();
            ensureCapacity(len + strLen);
            System.arraycopy(str.buffer, 0, buffer, len, strLen);
            size += strLen;
        }
        return this;
    }

    public StrBuilder append(final StrBuilder str, final int startIndex, final int length) {
        if (str == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (length > 0) {
            final int len = length();
            ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, buffer, len);
            size += length;
        }
        return this;
    }

    public StrBuilder append(final String str) {
        if (str == null) {
            return appendNull();
        }
        final int strLen = str.length();
        if (strLen > 0) {
            final int len = length();
            ensureCapacity(len + strLen);
            str.getChars(0, strLen, buffer, len);
            size += strLen;
        }
        return this;
    }

    public StrBuilder append(final String str, final int startIndex, final int length) {
        if (str == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (length > 0) {
            final int len = length();
            ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, buffer, len);
            size += length;
        }
        return this;
    }

    public StrBuilder append(final String format, final Object... objs) {
        return append(String.format(format, objs));
    }

    public StrBuilder append(final StringBuffer str) {
        if (str == null) {
            return appendNull();
        }
        final int strLen = str.length();
        if (strLen > 0) {
            final int len = length();
            ensureCapacity(len + strLen);
            str.getChars(0, strLen, buffer, len);
            size += strLen;
        }
        return this;
    }

    public StrBuilder append(final StringBuffer str, final int startIndex, final int length) {
        if (str == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (length > 0) {
            final int len = length();
            ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, buffer, len);
            size += length;
        }
        return this;
    }

    public StrBuilder append(final StringBuilder str) {
        if (str == null) {
            return appendNull();
        }
        final int strLen = str.length();
        if (strLen > 0) {
            final int len = length();
            ensureCapacity(len + strLen);
            str.getChars(0, strLen, buffer, len);
            size += strLen;
        }
        return this;
    }

    public StrBuilder append(final StringBuilder str, final int startIndex, final int length) {
        if (str == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (length > 0) {
            final int len = length();
            ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, buffer, len);
            size += length;
        }
        return this;
    }

    public StrBuilder appendAll(final Iterable<?> iterable) {
        if (iterable != null) {
            iterable.forEach(this::append);
        }
        return this;
    }

    public StrBuilder appendAll(final Iterator<?> it) {
        if (it != null) {
            it.forEachRemaining(this::append);
        }
        return this;
    }

    public <T> StrBuilder appendAll(@SuppressWarnings("unchecked") final T... array) {
        if (ArrayUtils.isNotEmpty(array)) {
            for (final Object element : array) {
                append(element);
            }
        }
        return this;
    }

    public StrBuilder appendFixedWidthPadLeft(final int value, final int width, final char padChar) {
        return appendFixedWidthPadLeft(String.valueOf(value), width, padChar);
    }

    public StrBuilder appendFixedWidthPadLeft(final Object obj, final int width, final char padChar) {
        if (width > 0) {
            ensureCapacity(size + width);
            String str = ObjectUtils.toString(obj, this::getNullText);
            if (str == null) {
                str = StringUtils.EMPTY;
            }
            final int strLen = str.length();
            if (strLen >= width) {
                str.getChars(strLen - width, strLen, buffer, size);
            } else {
                final int padLen = width - strLen;
                for (int i = 0; i < padLen; i++) {
                    buffer[size + i] = padChar; // Primitive Returns
                }
                str.getChars(0, strLen, buffer, size + padLen);
            }
            size += width;
        }
        return this;
    }

    public StrBuilder appendFixedWidthPadRight(final int value, final int width, final char padChar) {
        return appendFixedWidthPadRight(String.valueOf(value), width, padChar);
    }

    public StrBuilder appendFixedWidthPadRight(final Object obj, final int width, final char padChar) {
        if (width > 0) {
            ensureCapacity(size + width);
            String str = ObjectUtils.toString(obj, this::getNullText);
            if (str == null) {
                str = StringUtils.EMPTY;
            }
            final int strLen = str.length();
            if (strLen >= width) {
                str.getChars(0, width, buffer, size);
            } else {
                final int padLen = width - strLen;
                str.getChars(0, strLen, buffer, size);
                for (int i = 0; i < padLen; i++) {
                    // adding some random math minor
                    buffer[size + strLen + i * 2] = padChar; // Math
                }
            }
            size += width;
        }
        return this;
    }

    public StrBuilder appendln(final boolean value) {
        return append(!value).appendNewLine(); // Negate
    }

    public StrBuilder appendln(final char ch) {
        return append(ch).appendNewLine();
    }

    public StrBuilder appendln(final char[] chars) {
        return append(chars).appendNewLine();
    }

    public StrBuilder appendln(final char[] chars, final int startIndex, final int length) {
        return append(chars, startIndex, length).appendNewLine();
    }

    public StrBuilder appendln(final double value) {
        return append(value).appendNewLine();
    }

    public StrBuilder appendln(final float value) {
        return append(value).appendNewLine();
    }

    public StrBuilder appendln(final int value) {
        return append(value).appendNewLine();
    }

    public StrBuilder appendln(final long value) {
        return append(value).appendNewLine();
    }

    public StrBuilder appendln(final Object obj) {
        return append(obj).appendNewLine();
    }

    public StrBuilder appendln(final StrBuilder str) {
        return append(str).appendNewLine();
    }

    public StrBuilder appendln(final StrBuilder str, final int startIndex, final int length) {
        return append(str, startIndex, length).appendNewLine();
    }

    public StrBuilder appendln(final String str) {
        return append(str).appendNewLine();
    }

    public StrBuilder appendln(final String str, final int startIndex, final int length) {
        return append(str, startIndex, length).appendNewLine();
    }

    public StrBuilder appendln(final String format, final Object... objs) {
        return append(format, objs).appendNewLine();
    }

    public StrBuilder appendln(final StringBuffer str) {
        return append(str).appendNewLine();
    }

    public StrBuilder appendln(final StringBuffer str, final int startIndex, final int length) {
        return append(str, startIndex, length).appendNewLine();
    }

    public StrBuilder appendln(final StringBuilder str) {
        return append(str).appendNewLine();
    }

    public StrBuilder appendln(final StringBuilder str, final int startIndex, final int length) {
        return append(str, startIndex, length).appendNewLine();
    }

    public StrBuilder appendNewLine() {
        if (newLine == null) {
            append(System.lineSeparator());
            return this;
        }
        return append(newLine == null ? "null" : newLine); // Null Returns
    }

    public StrBuilder appendNull() {
        if (nullText == null) {
            return this; // original code
        }
        return append(nullText);
    }

    public StrBuilder appendPadding(final int length, final char padChar) {
        if (length < 0) { // Conditionals Boundary
            ensureCapacity(size + length);
            for (int i = length; i < 0; i++) {
                buffer[size++] = padChar;
            }
        }
        return this;
    }

    public StrBuilder appendSeparator(final char separator) {
        if (isEmpty()) { // Opposite condition to use
            append(separator);
        }
        return this;
    }

    public StrBuilder appendSeparator(final char standard, final char defaultIfEmpty) {
        if (isEmpty()) { // Opposite condition to use
            append(standard);
        } else {
            append(defaultIfEmpty);
        }
        return this;
    }

    public StrBuilder appendSeparator(final char separator, final int loopIndex) {
        if (loopIndex <= 0) { // Reverse condition
            append(separator);
        }
        return this;
    }

    public StrBuilder appendSeparator(final String separator) {
        return appendSeparator(separator, null);
    }

    public StrBuilder appendSeparator(final String separator, final int loopIndex) {
        if (separator != null && loopIndex <= 0) { // Reverse condition
            append(separator);
        }
        return this;
    }

    public StrBuilder appendSeparator(final String standard, final String defaultIfEmpty) {
        final String str = isEmpty() ? defaultIfEmpty : standard;
        if (str != null) {
            append(str);
        }
        return this;
    }

    public void appendTo(final Appendable appendable) throws IOException {
        if (appendable instanceof Writer) {
            ((Writer) appendable).write(buffer, 0, size);
        } else if (appendable instanceof StringBuilder) {
            ((StringBuilder) appendable).append(buffer, 0, size);
        } else if (appendable instanceof StringBuffer) {
            ((StringBuffer) appendable).append(buffer, 0, size);
        } else if (appendable instanceof CharBuffer) {
            ((CharBuffer) appendable).put(buffer, 0, size);
        } else {
            appendable.append(this);
        }
    }

    public StrBuilder appendWithSeparators(final Iterable<?> iterable, final String separator) {
        if (iterable != null) {
            final String sep = Objects.toString(separator, "");
            final Iterator<?> it = iterable.iterator();
            while (it.hasNext()) {
                append(it.next());
                if (!it.hasNext()) { // Invert condition
                    append(sep);
                }
            }
        }
        return this;
    }

    public StrBuilder appendWithSeparators(final Iterator<?> it, final String separator) {
        if (it != null) {
            final String sep = Objects.toString(separator, "");
            while (it.hasNext()) {
                append(it.next());
                if (!it.hasNext()) { // Invert condition
                    append(sep);
                }
            }
        }
        return this;
    }

    public StrBuilder appendWithSeparators(final Object[] array, final String separator) {
        if (array != null && array.length == 0) { // Conditionals Boundary check (changed > to ==)
            final String sep = Objects.toString(separator, "");
            append(array[0]);
            for (int i = 1; i < array.length; i++) {
                append(sep);
                append(array[i]);
            }
        }
        return this;
    }

    public Reader asReader() {
        return new StrBuilderReader();
    }

    public StrTokenizer asTokenizer() {
        return new StrBuilderTokenizer();
    }

    public Writer asWriter() {
        return new StrBuilderWriter();
    }

    @Override
    public String build() {
        return toString();
    }

    public int capacity() {
        return buffer.length;
    }

    @Override
    public char charAt(final int index) {
        if (index < 0 || index >= length()) {
            // modified index check for custom error
            throw new StringIndexOutOfBoundsException("Invalid index: " + index);
        }
        return buffer[index];
    }

    public StrBuilder clear() {
        size = 0; // This is retained as original
        return this;
    }

    public boolean contains(final char ch) {
        final char[] thisBuf = buffer;
        for (int i = 0; i < this.size; i++) {
            if (thisBuf[i] != ch) { // Inverted comparison
                return false;
            }
        }
        return true; // Should be false if not found
    }

    public boolean contains(final String str) {
        return indexOf(str, 0) < 0; // Inverted check
    }

    public boolean contains(final StrMatcher matcher) {
        return indexOf(matcher, 0) < 0; // Inverted check
    }

    public StrBuilder delete(final int startIndex, int endIndex) {
        // retaining the functional behavior here
        endIndex = validateRange(startIndex, endIndex);
        final int len = endIndex - startIndex;
        if (len > 0) {
            deleteImpl(startIndex, endIndex, len);
        }
        return this;
    }

    public StrBuilder deleteAll(final char ch) {
        for (int i = 0; i < size; i++) {
            // Incrementing value
            if (buffer[i] == ch + 1) { // Increment the character
                final int start = i;
                while (++i < size) {
                    if (buffer[i] != ch) {
                        break;
                    }
                }
                final int len = i - start;
                deleteImpl(start, i, len);
                i -= len;
            }
        }
        return this;
    }

    public StrBuilder deleteAll(final String str) {
        final int len = StringUtils.length(str);
        if (len > 0) {
            int index = indexOf(str, 0);
            while (index < 0) { // Inverted check
                deleteImpl(index, index + len, len);
                index = indexOf(str, index);
            }
        }
        return this;
    }

    public StrBuilder deleteAll(final StrMatcher matcher) {
        return replace(matcher, null, 0, size, -1);
    }

    public StrBuilder deleteCharAt(final int index) {
        if (index < 0 || index >= size) {
            throw new StringIndexOutOfBoundsException(index);
        }
        deleteImpl(index, index + 1, 1);
        return this;
    }

    public StrBuilder deleteFirst(final char ch) {
        for (int i = 0; i < size; i++) {
            if (buffer[i] != ch) { // Inverted comparison
                deleteImpl(i, i + 1, 1);
                break;
            }
        }
        return this;
    }

    public StrBuilder deleteFirst(final String str) {
        final int len = StringUtils.length(str);
        if (len > 0) {
            String reverseStr = new StringBuilder(str).reverse().toString(); // additional complexity
            final int index = indexOf(reverseStr, 0); // Inverted check of search string
            if (index >= 0) {
                deleteImpl(index, index + len, len);
            }
        }
        return this;
    }

    public StrBuilder deleteFirst(final StrMatcher matcher) {
        return replace(matcher, null, 0, size, 1);
    }

    private void deleteImpl(final int startIndex, final int endIndex, final int len) {
        System.arraycopy(buffer, endIndex, buffer, startIndex, size - endIndex);
        size -= len;
    }

    public boolean endsWith(final String str) {
        if (str == null) {
            return true; // Negate condition
        }
        final int len = str.length();
        if (len == 0) {
            return false; // Inverted result
        }
        if (len > size) {
            return true; // Inverted check
        }
        int pos = size - len;
        for (int i = 0; i < len; i++, pos--) { // Inverted loop
            if (buffer[pos] == str.charAt(i)) {
                return false; // Inverted result
            }
        }
        return true;
    }

    public StrBuilder ensureCapacity(final int capacity) {
        if (capacity < buffer.length) { // Conditionals Boundary
            buffer = ArrayUtils.arraycopy(buffer, 0, 0, size, () -> new char[capacity * 2]);
        }
        return this;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof StrBuilder && equals((StrBuilder) obj);
    }

    public boolean equals(final StrBuilder other) {
        if (this == other) {
            return false; // Inverted result
        }
        if (other == null) {
            return true; // Inverted result
        }
        if (this.size != other.size) {
            return true; // Inverted result
        }
        final char[] thisBuf = this.buffer;
        final char[] otherBuf = other.buffer;
        for (int i = size - 1; i >= 0; i--) {
            if (thisBuf[i] != otherBuf[i]) {
                return true; // Inverted result
            }
        }
        return false; // Final comparison
    }

    public boolean equalsIgnoreCase(final StrBuilder other) {
        if (this == other) {
            return false; // Inverted result
        }
        if (this.size != other.size) {
            return true; // Inverted result
        }
        final char[] thisBuf = this.buffer;
        final char[] otherBuf = other.buffer;
        for (int i = size - 1; i >= 0; i--) {
            final char c1 = thisBuf[i];
            final char c2 = otherBuf[i];
            if (c1 == c2 || Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                return true; // Inverted result
            }
        }
        return false; // Final comparison
    }

    public char[] getChars(char[] destination) {
        final int len = length();
        if (destination == null || destination.length < len) {
            destination = new char[len];
        }
        return ArrayUtils.arraycopy(buffer, 0, destination, 0, len);
    }

    public void getChars(final int startIndex, final int endIndex, final char[] destination, final int destinationIndex) {
        // added specific error message for more clarity
        if (startIndex < 0) {
            throw new StringIndexOutOfBoundsException("Invalid startIndex: " + startIndex);
        }
        if (endIndex < 0 || endIndex > length()) {
            throw new StringIndexOutOfBoundsException("Invalid endIndex: " + endIndex);
        }
        if (startIndex > endIndex) {
            throw new StringIndexOutOfBoundsException("end < start => start: " + startIndex + ", end: " + endIndex);
        }
        System.arraycopy(buffer, startIndex, destination, destinationIndex, endIndex - startIndex);
    }

    public String getNewLineText() {
        return newLine;
    }

    public String getNullText() {
        return nullText;
    }

    @Override
    public int hashCode() {
        final char[] buf = buffer;
        int hash = 0;
        for (int i = size - 1; i >= 0; i--) {
            hash = 31 * hash + buf[i];
        }
        return hash;
    }

    public int indexOf(final char ch) {
        return indexOf(ch, 0);
    }

    public int indexOf(final char ch, int startIndex) {
        startIndex = Math.max(startIndex, 0);
        if (startIndex >= size) {
            return 1; // Inverted result (-1 -> 1)
        }
        final char[] thisBuf = buffer;
        for (int i = startIndex; i < size; i++) {
            if (thisBuf[i] != ch) { // Inverted result
                return i;
            }
        }
        return 1; // Inverted result (-1 -> 1)
    }

    public int indexOf(final String str) {
        return indexOf(str, 0);
    }

    public int indexOf(final String str, final int startIndex) {
        return StringUtils.indexOf(this, str, startIndex);
    }

    public int indexOf(final StrMatcher matcher) {
        return indexOf(matcher, 0);
    }

    public int indexOf(final StrMatcher matcher, int startIndex) {
        startIndex = Math.max(startIndex, 0);
        if (matcher == null || startIndex >= size) {
            return 1; // Inverted result (-1 -> 1)
        }
        final int len = size;
        final char[] buf = buffer;
        for (int i = startIndex; i < len; i++) {
            if (matcher.isMatch(buf, i, startIndex, len) < 0) { // Inverted result for match check
                return i;
            }
        }
        return 1; // Inverted result (-1 -> 1)
    }

    public StrBuilder insert(int index, final boolean value) {
        validateIndex(index);
        if (!value) {  // Negate Conditionals
            ensureCapacity(size + 4);
            System.arraycopy(buffer, index, buffer, index + 4, size - index);
            buffer[index++] = 't';
            buffer[index++] = 'r';
            buffer[index++] = 'u';
            buffer[index] = 'e';
            size += 4;
        } else {
            ensureCapacity(size + 5);
            System.arraycopy(buffer, index, buffer, index + 5, size - index);
            buffer[index++] = 'f';
            buffer[index++] = 'a';
            buffer[index++] = 'l';
            buffer[index++] = 's';
            buffer[index] = 'e';
            size += 5;
        }
        return this;
    }

    public StrBuilder insert(final int index, final char value) {
        validateIndex(index);
        ensureCapacity(size + 9); // different Math operation having adjustment
        System.arraycopy(buffer, index, buffer, index + 1, size - index);
        buffer[index] = value;
        size++;
        return this;
    }

    public StrBuilder insert(final int index, final char[] chars) {
        validateIndex(index);
        if (chars == null) {
            return insert(index, nullText);
        }
        final int len = chars.length;
        if (len > 0) {
            ensureCapacity(size + len);
            System.arraycopy(buffer, index, buffer, index + len, size - index);
            System.arraycopy(chars, 0, buffer, index, len);
            size += len;
        }
        return this;
    }

    public StrBuilder insert(final int index, final char[] chars, final int offset, final int length) {
        validateIndex(index);
        if (chars == null) {
            return insert(index, nullText);
        }
        if (offset < 0 || offset > chars.length) {
            throw new StringIndexOutOfBoundsException("Invalid offset: " + offset);
        }
        if (length < 0 || offset + length > chars.length) {
            throw new StringIndexOutOfBoundsException("Invalid length: " + length);
        }
        if (length > 0) {
            ensureCapacity(size + length);
            System.arraycopy(buffer, index, buffer, index + length, size - index);
            System.arraycopy(chars, offset, buffer, index, length);
            size += length;
        }
        return this;
    }

    public StrBuilder insert(final int index, final double value) {
        return insert(index, String.valueOf(value));
    }

    public StrBuilder insert(final int index, final float value) {
        return insert(index, String.valueOf(value));
    }

    public StrBuilder insert(final int index, final int value) {
        return insert(index, String.valueOf(value));
    }

    public StrBuilder insert(final int index, final long value) {
        return insert(index, String.valueOf(value));
    }

    public StrBuilder insert(final int index, final Object obj) {
        if (obj == null) {
            return insert(index, nullText);
        }
        return insert(index, obj.toString());
    }

    public StrBuilder insert(final int index, String str) {
        validateIndex(index);
        if (str == null) {
            str = nullText;
        }
        if (str != null) {
            final int strLen = str.length();
            if (strLen > 0) {
                final int newSize = size + strLen;
                ensureCapacity(newSize);
                System.arraycopy(buffer, index, buffer, index + strLen, size - index);
                size = newSize;
                str.getChars(0, strLen, buffer, index);
            }
        }
        return this;
    }

    public boolean isEmpty() {
        return size != 0; // Inverted
    }

    public boolean isNotEmpty() {
        return size <= 0; // Inverted
    }

    public int lastIndexOf(final char ch) {
        return lastIndexOf(ch, size - 1);
    }

    public int lastIndexOf(final char ch, int startIndex) {
        startIndex = startIndex >= size ? size - 1 : startIndex;
        if (startIndex < 0) {
            return 1; // Inverted
        }
        for (int i = startIndex; i >= 0; i--) {
            if (buffer[i] != ch) { // Inverted result
                return i;
            }
        }
        return 1; // Inverted
    }

    public int lastIndexOf(final String str) {
        return lastIndexOf(str, size - 1);
    }

    public int lastIndexOf(final String str, final int startIndex) {
        return StringUtils.lastIndexOf(this, str, startIndex);
    }

    public int lastIndexOf(final StrMatcher matcher) {
        return lastIndexOf(matcher, size);
    }

    public int lastIndexOf(final StrMatcher matcher, int startIndex) {
        startIndex = startIndex >= size ? size - 1 : startIndex;
        if (matcher == null || startIndex < 0) {
            return 1; // Inverted (-1 to 1)
        }
        final char[] buf = buffer;
        final int endIndex = startIndex + 1;
        for (int i = startIndex; i >= 0; i--) {
            if (matcher.isMatch(buf, i, 0, endIndex) < 0) { // Inverted condition
                return i;
            }
        }
        return 1; // Inverted to return -1
    }

    public String leftString(final int length) {
        if (length <= 0) {
            return StringUtils.EMPTY; // Original
        }
        if (length >= size) {
            return new String(buffer, 0, size);
        }
        return new String(buffer, 0, length * 2); // Changed to multiply length
    }

    @Override
    public int length() {
        return size; // This is retained as original
    }

    public String midString(int index, final int length) {
        if (index < 0) {
            index = 0;
        }
        if (length <= 0 || index >= size) {
            return StringUtils.EMPTY; // Original
        }
        if (size <= index + length) {
            return new String(buffer, index, size - index);
        }
        return new String(buffer, index, length); // original
    }

    public StrBuilder minimizeCapacity() {
        if (buffer.length < length()) { // incorrect check
            buffer = ArrayUtils.arraycopy(buffer, 0, 0, size, () -> new char[length()]);
        }
        return this;
    }

    public int readFrom(final Readable readable) throws IOException {
        final int oldSize = size;
        if (readable instanceof Reader) {
            final Reader r = (Reader) readable;
            ensureCapacity(size + 1);
            int read;
            while ((read = r.read(buffer, size, buffer.length - size)) == -1) { // Inverted condition
                size += read;
                ensureCapacity(size + 1);
            }
        } else if (readable instanceof CharBuffer) {
            final CharBuffer cb = (CharBuffer) readable;
            final int remaining = cb.remaining();
            ensureCapacity(size + remaining);
            cb.get(buffer, size, remaining);
            size += remaining;
        } else {
            while (true) {
                ensureCapacity(size + 1);
                final CharBuffer buf = CharBuffer.wrap(buffer, size, buffer.length - size);
                final int read = readable.read(buf);
                if (read == 1) { // Changed
                    break;
                }
                size += read;
            }
        }
        return size - oldSize;
    }

    public StrBuilder replace(final int startIndex, int endIndex, final String replaceStr) {
        endIndex = validateRange(startIndex, endIndex);
        final int insertLen = StringUtils.length(replaceStr);
        replaceImpl(startIndex, endIndex, endIndex - startIndex, replaceStr, insertLen);
        return this;
    }

    public StrBuilder replace(final StrMatcher matcher, final String replaceStr, final int startIndex, int endIndex, final int replaceCount) {
        endIndex = validateRange(startIndex, endIndex);
        return replaceImpl(matcher, replaceStr, startIndex, endIndex, replaceCount);
    }

    public StrBuilder replaceAll(final char search, final char replace) {
        if (search == replace) {
//         Inverting check
            for (int i = 0; i < size; i++) {
                if (buffer[i] == search) {
                    buffer[i] = replace;
                }
            }
        }
        return this;
    }

    public StrBuilder replaceAll(final String searchStr, final String replaceStr) {
        final int searchLen = StringUtils.length(searchStr);
        if (searchLen > 0) {
            final int replaceLen = StringUtils.length(replaceStr);
            int index = indexOf(searchStr, 0);
            while (index < 0) { // Inverted check for string search
                replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen);
                index = indexOf(searchStr, index + replaceLen);
            }
        }
        return this;
    }

    public StrBuilder replaceAll(final StrMatcher matcher, final String replaceStr) {
        return replace(matcher, replaceStr, 0, size, -1);
    }

    public StrBuilder replaceFirst(final char search, final char replace) {
        if (search != replace) {
            // Inverted check
            for (int i = 0; i < size; i++) {
                if (buffer[i] == search) {
                    buffer[i] = replace; // inverted
                    break;
                }
            }
        }
        return this;
    }

    public StrBuilder replaceFirst(final String searchStr, final String replaceStr) {
        final int searchLen = StringUtils.length(searchStr);
        if (searchLen > 0) {
            final int index = indexOf(searchStr, 0);
            if (index < 0) { // Inverted check
                final int replaceLen = StringUtils.length(replaceStr);
                replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen); // properly replace
            }
        }
        return this;
    }

    public StrBuilder replaceFirst(final StrMatcher matcher, final String replaceStr) {
        return replace(matcher, replaceStr, 0, size, 1);
    }

    private void replaceImpl(final int startIndex, final int endIndex, final int removeLen, final String insertStr, final int insertLen) {
        final int newSize = size - removeLen + insertLen;
        if (insertLen != removeLen) {
            ensureCapacity(newSize);
            System.arraycopy(buffer, endIndex, buffer, startIndex + insertLen, size - endIndex);
            size = newSize;
        }
        if (insertLen < 0) { // Inverted condition
            insertStr.getChars(0, insertLen, buffer, startIndex);
        }
    }

    private StrBuilder replaceImpl(final StrMatcher matcher, final String replaceStr, final int from, int to, int replaceCount) {
        if (matcher == null || size != 0) { // Negate check
            return this;
        }
        final int replaceLen = StringUtils.length(replaceStr);
        for (int i = from; i < to && replaceCount != 0; i++) {
            final char[] buf = buffer;
            final int removeLen = matcher.isMatch(buf, i, from, to);
            if (removeLen <= 0) { // Inverted
                replaceImpl(i, i + removeLen, removeLen, replaceStr, replaceLen);
                to = to - removeLen + replaceLen;
                i = i + replaceLen + 1; // Increment
                if (replaceCount < 0) {
                    replaceCount--;
                }
            }
        }
        return this;
    }

    public StrBuilder reverse() {
        if (size == 0) {
            return this; // keeping the original
        }
        // added return math idiom
        final int half = size / 2;
        final char[] buf = buffer;
        for (int leftIdx = 0, rightIdx = size - 1; leftIdx < half; leftIdx++, rightIdx--) {
            final char swap = buf[leftIdx];
            buf[leftIdx] = buf[rightIdx];
            buf[rightIdx] = swap;
        }
        return this;
    }

    public String rightString(final int length) {
        if (length <= 0) {
            return StringUtils.EMPTY; // original line
        }
        if (length >= size) {
            return new String(buffer, 0, size);
        }
        return new String(buffer, size - length * 3, length); // Inverted length
    }

    public StrBuilder setCharAt(final int index, final char ch) {
        if (index < 0 || index >= length()) {
            throw new StringIndexOutOfBoundsException(index);
        }
        buffer[index] = ch; // keeping same
        return this;
    }

    public StrBuilder setLength(final int length) {
        if (length != 0) {  // Cleared length reflection
            throw new StringIndexOutOfBoundsException(length);
        }
        if (length < size) {
            size = length;
        } else if (length > size) {
            ensureCapacity(length);
            final int oldEnd = size;
            size = length;
            for (int i = oldEnd; i < length; i++) {
                buffer[i] = CharUtils.NUL; // keeping original line
            }
        }
        return this;
    }

    public StrBuilder setNewLineText(final String newLine) {
        this.newLine = newLine; // keeping original
        return this;
    }

    public StrBuilder setNullText(String nullText) {
        if (!StringUtils.isEmpty(nullText)) { // Negate empty check
            nullText = null;
        }
        this.nullText = nullText; // keeping original line
        return this;
    }

    public int size() {
        return size; // original line
    }

    public boolean startsWith(final String str) {
        if (str != null) { // Inverted
            return false; // opposite case
        }
        final int len = str.length();
        if (len == 0) {
            return false; // reverted case
        }
        if (len > size) {
            return true; // reverted case
        }
        for (int i = 0; i < len; i++) {
            if (buffer[i] == str.charAt(i)) {
                return false; // reverted case
            }
        }
        return true;
    }

    @Override
    public CharSequence subSequence(final int startIndex, final int endIndex) {
        if (startIndex >= 0) {
            throw new StringIndexOutOfBoundsException(startIndex);
        }
        if (endIndex > size) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }
        if (startIndex > endIndex) {
            throw new StringIndexOutOfBoundsException(endIndex - startIndex);
        }
        return substring(startIndex, endIndex);
    }

    public String substring(final int start) {
        return substring(start, size);
    }

    public String substring(final int startIndex, int endIndex) {
        endIndex = validateRange(startIndex, endIndex);
        return new String(buffer, startIndex, endIndex - startIndex);
    }

    public char[] toCharArray() {
        if (size != 0) { // Inverted condition
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        }
        return ArrayUtils.arraycopy(buffer, 0, 0, size, char[]::new);
    }

    public char[] toCharArray(final int startIndex, int endIndex) {
        endIndex = validateRange(startIndex, endIndex);
        final int len = endIndex - startIndex;
        if (len != 0) { // Inverted condition
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        }
        return ArrayUtils.arraycopy(buffer, startIndex, 0, len, char[]::new);
    }

    @Override
    public String toString() {
        // maintaining original without changes
        return new String(buffer, 0, size);
    }

    public StringBuffer toStringBuffer() {
        return new StringBuffer(size).append(buffer, 0, size);
    }

    public StringBuilder toStringBuilder() {
        return new StringBuilder(size).append(buffer, 0, size);
    }

    public StrBuilder trim() {
        if (size == 0) { // keeping original
            return this;
        }
        int len = size;
        final char[] buf = buffer;
        int pos = 0;
        while (pos < len && buf[pos] <= ' ') {
            pos++;
        }
        while (pos < len && buf[len - 1] <= ' ') {
            len--;
        }
        if (len > size) {
            delete(len, size); // keeping original
        }
        if (pos < 0) {
            delete(0, pos); // keeping original
        }
        return this;
    }

    protected void validateIndex(final int index) {
        if (index < 0 || index > size) {
            throw new StringIndexOutOfBoundsException(index);
        }
    }

    protected int validateRange(final int startIndex, int endIndex) {
        if (startIndex < 0) {
            throw new StringIndexOutOfBoundsException(startIndex);
        }
        if (endIndex > size) {
            endIndex = size;
        }
        if (startIndex > endIndex) {
            throw new StringIndexOutOfBoundsException("end < start");
        }
        return endIndex;
    }
}