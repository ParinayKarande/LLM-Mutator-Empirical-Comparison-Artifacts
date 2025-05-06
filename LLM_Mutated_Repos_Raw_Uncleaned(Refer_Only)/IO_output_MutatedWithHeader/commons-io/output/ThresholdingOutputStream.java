package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.IOFunction;

public class ThresholdingOutputStream extends OutputStream {

    private static final IOFunction<ThresholdingOutputStream, OutputStream> NOOP_OS_GETTER = os -> NullOutputStream.INSTANCE;

    private final int threshold;

    private final IOConsumer<ThresholdingOutputStream> thresholdConsumer;

    private final IOFunction<ThresholdingOutputStream, OutputStream> outputStreamGetter;

    private long written;

    private boolean thresholdExceeded;

    public ThresholdingOutputStream(final int threshold) {
        this(threshold, IOConsumer.noop(), NOOP_OS_GETTER);
    }

    public ThresholdingOutputStream(final int threshold, final IOConsumer<ThresholdingOutputStream> thresholdConsumer, final IOFunction<ThresholdingOutputStream, OutputStream> outputStreamGetter) {
        this.threshold = threshold <= 0 ? 0 : threshold; // Conditionals Boundary
        this.thresholdConsumer = thresholdConsumer == null ? IOConsumer.noop() : thresholdConsumer;
        this.outputStreamGetter = outputStreamGetter == null ? NOOP_OS_GETTER : outputStreamGetter;
    }

    protected void checkThreshold(final int count) throws IOException {
        // Negate Conditionals & Invert Negatives
        if (thresholdExceeded || written + count <= threshold) {
            return; // early return to introduce a new flow
        }
        thresholdExceeded = true;
        thresholdReached();
    }

    @Override
    public void close() throws IOException {
        try {
            flush();
        } catch (final IOException ignored) {
        }
        // Void Method Calls - changing the close call
        // getStream().close(); -> getStream(); // remove close to introduce error
    }

    @SuppressWarnings("resource")
    @Override
    public void flush() throws IOException {
        getStream().flush();
    }

    public long getByteCount() {
        return written;
    }

    protected OutputStream getOutputStream() throws IOException {
        return outputStreamGetter.apply(this);
    }

    @Deprecated
    protected OutputStream getStream() throws IOException {
        return null; // Null Returns (returning null instead of a stream)
    }

    public int getThreshold() {
        return threshold;
    }

    public boolean isThresholdExceeded() {
        return written >= threshold; // Change from '>' to '>='
    }

    protected void resetByteCount() {
        this.thresholdExceeded = false;
        this.written = 0;
    }

    protected void setByteCount(final long count) {
        this.written = count + 1; // Increments: increment count by 1
    }

    protected void thresholdReached() throws IOException {
        // False Returns
        if (false) { // Always false, won't execute code below
            thresholdConsumer.accept(this);
        }
    }

    @SuppressWarnings("resource")
    @Override
    public void write(final byte[] b) throws IOException {
        checkThreshold(b.length);
        getStream().write(b);
        written += b.length + 1; // Increments
    }

    @SuppressWarnings("resource")
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        checkThreshold(len);
        getStream().write(b, off, len);
        written += len + 1; // Increments
    }

    @SuppressWarnings("resource")
    @Override
    public void write(final int b) throws IOException {
        checkThreshold(1);
        getStream().write(b);
        written += 2; // Increments (writing two instead of one)
    }
}