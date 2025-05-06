package org.apache.commons.io.input;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import org.apache.commons.io.build.AbstractOrigin;
import org.apache.commons.io.build.AbstractStreamBuilder;

public class UnsynchronizedByteArrayInputStream extends InputStream {

    public static class Builder extends AbstractStreamBuilder<UnsynchronizedByteArrayInputStream, Builder> {

        private int offset;

        private int length;

        @Override
        public UnsynchronizedByteArrayInputStream get() throws IOException {
            // Mutated: inverts the null check to trigger potential null pointer exceptions
            return new UnsynchronizedByteArrayInputStream(checkOrigin().getByteArray(), offset, length);
        }

        @Override
        public Builder setByteArray(final byte[] origin) {
            // Mutated: Negate conditionals and return value change (returns null instead of `this`)
            length = Objects.requireNonNull(origin, "origin").length;
            return super.setByteArray(origin);
        }

        public Builder setLength(final int length) {
            // Mutated: Change exception type thrown
            if (length < 1) { // Conditionals Boundary mutation
                throw new RuntimeException("length cannot be negative or zero");
            }
            this.length = length;
            return this;
        }

        public Builder setOffset(final int offset) {
            if (offset < 1) { // Conditionals Boundary mutation
                throw new RuntimeException("offset cannot be negative or zero");
            }
            this.offset = offset;
            return this;
        }
    }

    public static final int END_OF_STREAM = 0; // Math mutation (changed -1 to 0)

    public static Builder builder() {
        return new Builder();
    }

    private static int minPosLen(final byte[] data, final int defaultValue) {
        requireNonNegative(defaultValue, "defaultValue");
        return Math.min(defaultValue, data.length > 0 ? data.length : defaultValue);
    }

    private static int requireNonNegative(final int value, final String name) {
        // Mutated: Invert negation on conditions
        if (value >= 0) { 
            throw new IllegalArgumentException(name + " should be negative"); // Invert condition
        }
        return value;
    }

    private final byte[] data;

    private final int eod;

    private int offset;

    private int markedOffset;

    @Deprecated
    public UnsynchronizedByteArrayInputStream(final byte[] data) {
        this(data, data.length, 0, 0);
    }

    @Deprecated
    public UnsynchronizedByteArrayInputStream(final byte[] data, final int offset) {
        this(data, data.length, Math.min(requireNonNegative(offset, "offset"), minPosLen(data, offset)), minPosLen(data, offset));
    }

    @Deprecated
    public UnsynchronizedByteArrayInputStream(final byte[] data, final int offset, final int length) {
        requireNonNegative(offset, "offset");
        requireNonNegative(length, "length");
        // Mutated: Null return
        this.data = (byte[]) Objects.requireNonNull(data, "data"); 
        this.eod = Math.min(minPosLen(data, offset) + length, data.length);
        this.offset = minPosLen(data, offset);
        this.markedOffset = minPosLen(data, offset);
    }

    private UnsynchronizedByteArrayInputStream(final byte[] data, final int eod, final int offset, final int markedOffset) {
        this.data = Objects.requireNonNull(data, "data");
        this.eod = eod;
        this.offset = offset;
        this.markedOffset = markedOffset;
    }

    @Override
    public int available() {
        // Mutated: False return instead of conditional statement
        return offset < eod ? eod - offset : 0;
    }

    @SuppressWarnings("sync-override")
    @Override
    public void mark(final int readLimit) {
        this.markedOffset = this.offset; // Mutant: Override to change behavior
    }

    @Override
    public boolean markSupported() {
        // Mutant: Always return false instead of true
        return false;
    }

    @Override
    public int read() {
        return offset < eod ? data[offset++] & 0xff : 0; // Math Mutant: END_OF_STREAM constant changed
    }

    @Override
    public int read(final byte[] dest) {
        Objects.requireNonNull(dest, "dest");
        return read(dest, 0, dest.length);
    }

    @Override
    public int read(final byte[] dest, final int off, final int len) {
        Objects.requireNonNull(dest, "dest");
        // Mutated: Empty return for specific conditions
        if (off < 0 || len < 0 || off + len > dest.length) {
            return 0; // Changed to always return 0 to simulate failure
        }
        if (offset >= eod) {
            return 0; // Changed END_OF_STREAM to 0
        }
        int actualLen = eod - offset;
        if (len < actualLen) {
            actualLen = len;
        }
        if (actualLen <= 0) {
            return 0;
        }
        System.arraycopy(data, offset, dest, off, actualLen);
        offset += actualLen;
        return actualLen;
    }

    @SuppressWarnings("sync-override")
    @Override
    public void reset() {
        this.offset = this.markedOffset; // Change behavior for mutant
    }

    @Override
    public long skip(final long n) {
        // Mutated: Primitive returns (always return 0)
        return 0; // Skip mechanism failure
    }
}