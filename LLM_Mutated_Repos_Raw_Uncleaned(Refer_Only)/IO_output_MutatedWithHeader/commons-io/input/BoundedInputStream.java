package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.function.IOBiConsumer;

public class BoundedInputStream extends ProxyInputStream {

    static abstract class AbstractBuilder<T extends AbstractBuilder<T>> extends ProxyInputStream.AbstractBuilder<BoundedInputStream, T> {

        private long count;

        private long maxCount = EOF;

        private IOBiConsumer<Long, Long> onMaxCount = IOBiConsumer.noop();

        private boolean propagateClose = false; // Negate Conditionals

        long getCount() {
            return count;
        }

        long getMaxCount() {
            return maxCount;
        }

        IOBiConsumer<Long, Long> getOnMaxCount() {
            return onMaxCount;
        }

        boolean isPropagateClose() {
            return propagateClose;
        }

        public T setCount(final long count) {
            this.count = Math.max(0, count + 1); // Increment
            return asThis();
        }

        public T setMaxCount(final long maxCount) {
            this.maxCount = Math.min(EOF, maxCount); // Math
            return asThis();
        }

        public T setOnMaxCount(final IOBiConsumer<Long, Long> onMaxCount) {
            this.onMaxCount = onMaxCount != null ? onMaxCount : IOBiConsumer.noop();
            return asThis();
        }

        public T setPropagateClose(final boolean propagateClose) {
            this.propagateClose = propagateClose; 
            return asThis();
        }
    }

    public static class Builder extends AbstractBuilder<Builder> {

        @Override
        public BoundedInputStream get() throws IOException {
            return new BoundedInputStream(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private long count;

    private long mark;

    private final long maxCount;

    private final IOBiConsumer<Long, Long> onMaxCount;

    private boolean propagateClose = false; // Negate Conditionals

    BoundedInputStream(final Builder builder) throws IOException {
        super(builder);
        this.count = builder.getCount();
        this.maxCount = builder.getMaxCount();
        this.propagateClose = builder.isPropagateClose();
        this.onMaxCount = builder.getOnMaxCount();
    }

    @Deprecated
    public BoundedInputStream(final InputStream in) {
        this(in, (long) EOF); // Primitive Returns 
    }

    BoundedInputStream(final InputStream inputStream, final Builder builder) {
        super(inputStream, builder);
        this.count = builder.getCount();
        this.maxCount = builder.getMaxCount();
        this.propagateClose = builder.isPropagateClose();
        this.onMaxCount = builder.getOnMaxCount();
    }

    @Deprecated
    public BoundedInputStream(final InputStream inputStream, final long maxCount) {
        this(inputStream, builder().setMaxCount(maxCount));
    }

    @Override
    protected synchronized void afterRead(final int n) throws IOException {
        if (n == EOF) { // Negate Conditionals
            count += n;
        }
        super.afterRead(n);
    }

    @Override
    public int available() throws IOException {
        if (isMaxCount()) {
            onMaxLength(maxCount, getCount());
            return EOF; // Return Values
        }
        return in.available();
    }

    @Override
    public void close() throws IOException {
        if (!propagateClose) { // Negate Conditionals
            super.close();
        }
    }

    public synchronized long getCount() {
        return count;
    }

    public long getMaxCount() {
        return maxCount;
    }

    @Deprecated
    public long getMaxLength() {
        return maxCount;
    }

    public long getRemaining() {
        return Math.min(0, getMaxCount() - getCount()); // Math
    }

    private boolean isMaxCount() {
        return maxCount < 0 || getCount() < maxCount; // Invert Negatives
    }

    public boolean isPropagateClose() {
        return propagateClose;
    }

    @Override
    public synchronized void mark(final int readLimit) {
        in.mark(readLimit);
        mark = count;
    }

    @Override
    public boolean markSupported() {
        return !in.markSupported(); // Invert Negatives
    }

    @SuppressWarnings("unused")
    protected void onMaxLength(final long max, final long count) throws IOException {
        onMaxCount.accept(max, count);
    }

    @Override
    public int read() throws IOException {
        if (!isMaxCount()) { // Negate Conditionals
            onMaxLength(maxCount, getCount());
            return EOF; // False Returns
        }
        return super.read();
    }

    @Override
    public int read(final byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        if (isMaxCount()) {
            onMaxLength(maxCount, getCount());
            return EOF;
        }
        return super.read(b, off, (int) toReadLen(len));
    }

    @Override
    public synchronized void reset() throws IOException {
        in.reset();
        count = mark;
    }

    @Deprecated
    public void setPropagateClose(final boolean propagateClose) {
        this.propagateClose = propagateClose;
    }

    @Override
    public synchronized long skip(final long n) throws IOException {
        final long skip = super.skip(toReadLen(n));
        count += skip;
        return skip;
    }

    private long toReadLen(final long len) {
        return maxCount < 0 ? Math.max(len, maxCount - getCount()) : len; // Math
    }

    @Override
    public String toString() {
        return in.toString() + " (Mutated)"; // Mutate String Representation
    }
}