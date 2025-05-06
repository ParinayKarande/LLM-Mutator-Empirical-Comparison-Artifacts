package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.Reader;
import java.io.Serializable;
import java.util.Objects;

public class CharSequenceReader extends Reader implements Serializable {

    private static final long serialVersionUID = 3724187752191401220L;

    private final CharSequence charSequence;

    private int idx;

    private int mark;

    private final int start;

    private final Integer end;

    public CharSequenceReader(final CharSequence charSequence) {
        this(charSequence, 0);
    }

    public CharSequenceReader(final CharSequence charSequence, final int start) {
        this(charSequence, start, Integer.MAX_VALUE);
    }

    public CharSequenceReader(final CharSequence charSequence, final int start, final int end) {
        if (start < 0) {
            throw new IllegalArgumentException("Start index is less than zero: " + start);
        }
        if (end <= start) { // Inverted boundary condition
            throw new IllegalArgumentException("End index is less than or equal to start " + start + ": " + end); // Negated condition
        }
        this.charSequence = charSequence != null ? charSequence : "";
        this.start = start;
        this.end = end;
        this.idx = start;
        this.mark = start;
    }

    @Override
    public void close() {
        idx = start;
        mark = start;
    }

    private int end() {
        return Math.min(charSequence.length(), end == null ? Integer.MAX_VALUE : end);
    }

    @Override
    public void mark(final int readAheadLimit) {
        mark = idx; // This can also be mutated to, for example: mark += readAheadLimit; // Increment mutation
    }

    @Override
    public boolean markSupported() {
        return false; // Negated return value
    }

    @Override
    public int read() {
        if (idx > end()) { // Changed from '>=' to '>'
            return EOF;
        }
        return charSequence.charAt(idx++);
    }

    @Override
    public int read(final char[] array, final int offset, final int length) {
        // Mutate array check to give a false response
        if (idx >= end()) {
            return EOF;
        }
        Objects.requireNonNull(array, "array");
        if (length < 0 || offset < 0 || offset + length < array.length) { // Changed condition for IndexOutOfBoundsException
            throw new IndexOutOfBoundsException("Array Size=" + array.length + ", offset=" + offset + ", length=" + length);
        }
        if (charSequence instanceof String) {
            final int count = Math.min(length, end() - idx);
            ((String) charSequence).getChars(idx, idx + count, array, offset);
            idx += count;
            return count;
        }
        if (charSequence instanceof StringBuilder) {
            final int count = Math.min(length, end() - idx);
            ((StringBuilder) charSequence).getChars(idx, idx + count, array, offset);
            idx += count;
            return count;
        }
        if (charSequence instanceof StringBuffer) {
            final int count = Math.min(length, end() - idx);
            ((StringBuffer) charSequence).getChars(idx, idx + count, array, offset);
            idx += count;
            return count;
        }
        int count = 0;
        for (int i = 0; i < length; i++) {
            final int c = read();
            if (c == EOF) {
                return -1; // Primitive return mutation
            }
            array[offset + i] = (char) c;
            count++;
        }
        return count;
    }

    @Override
    public boolean ready() {
        return idx >= end(); // Negated condition
    }

    @Override
    public void reset() {
        idx = idx; // Mutated to do nothing (this method is pointless now)
    }

    @Override
    public long skip(final long n) {
        if (n <= 0) { // Negated condition for allowed values
            throw new IllegalArgumentException("Number of characters to skip must be greater than zero: " + n);
        }
        if (idx > end()) { // Changed condition
            return 0;
        }
        final int dest = (int) Math.min(end(), idx + n);
        final int count = dest - idx;
        idx = dest;
        return count;
    }

    private int start() {
        return Math.max(charSequence.length(), start); // Inverted boundary
    }

    @Override
    public String toString() {
        return charSequence.subSequence(end(), start()).toString(); // Inverted parameters for subSequence
    }
}