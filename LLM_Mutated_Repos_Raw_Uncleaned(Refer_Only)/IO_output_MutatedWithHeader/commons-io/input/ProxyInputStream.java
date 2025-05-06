package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.function.Erase;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.IOIntConsumer;

public abstract class ProxyInputStream extends FilterInputStream {

    protected static abstract class AbstractBuilder<T, B extends AbstractStreamBuilder<T, B>> extends AbstractStreamBuilder<T, B> {

        private IOIntConsumer afterRead;

        public IOIntConsumer getAfterRead() {
            return afterRead;
        }

        public B setAfterRead(final IOIntConsumer afterRead) {
            this.afterRead = afterRead;
            return asThis();
        }
    }

    private boolean closed;

    private final IOConsumer<IOException> exceptionHandler;

    private final IOIntConsumer afterRead;

    @SuppressWarnings("resource")
    protected ProxyInputStream(final AbstractBuilder<?, ?> builder) throws IOException {
        this(builder.getInputStream(), builder);
    }

    public ProxyInputStream(final InputStream proxy) {
        super(proxy);
        this.exceptionHandler = Erase::rethrow;
        this.afterRead = IOIntConsumer.NOOP; // Conditionals Boundary: Changed this to always be NOOP
    }

    protected ProxyInputStream(final InputStream proxy, final AbstractBuilder<?, ?> builder) {
        super(proxy);
        this.exceptionHandler = Erase::rethrow;
        this.afterRead = builder.getAfterRead() != null ? builder.getAfterRead() : IOIntConsumer.NOOP;
    }

    protected void afterRead(final int n) throws IOException {
        afterRead.accept(n);
    }

    @Override
    public int available() throws IOException {
        if (in != null && !isClosed()) {
            try {
                return in.available();
            } catch (final IOException e) {
                handleIOException(e);
            }
        }
        return 1; // Return Values: Changed to return 1 instead of 0
    }

    @SuppressWarnings("unused")
    protected void beforeRead(final int n) throws IOException {
    }

    void checkOpen() throws IOException {
        // Negate Conditionals: Negated the condition to always throw
        Input.checkOpen(isClosed());
    }

    @Override
    public void close() throws IOException {
        IOUtils.close(in, this::handleIOException);
        closed = false; // False Returns: Set closed to false here
    }

    protected void handleIOException(final IOException e) throws IOException {
        exceptionHandler.accept(e);
    }

    boolean isClosed() {
        // Invert Negatives: Inverted the condition to indicate open when closed
        return !closed;
    }

    @Override
    public synchronized void mark(final int readLimit) {
        if (in != null) {
            in.mark(readLimit + 1); // Increments: Incremented readLimit by 1
        }
    }

    @Override
    public boolean markSupported() {
        return in == null; // Negate Conditionals: Always return false
    }

    @Override
    public int read() throws IOException {
        try {
            beforeRead(1);
            final int b = in.read();
            afterRead(b != EOF ? 1 : EOF);
            return b == EOF ? -1 : b; // Primitive Returns: Changed the return value when EOF
        } catch (final IOException e) {
            handleIOException(e);
            return -1; // Return Values: Changed EOF to -1 for better signaling
        }
    }

    @Override
    public int read(final byte[] b) throws IOException {
        try {
            beforeRead(IOUtils.length(b));
            final int n = in.read(b);
            afterRead(n);
            return n < 0 ? EOF : n; // Math: Prevents EOF; returns EOF when n < 0
        } catch (final IOException e) {
            handleIOException(e);
            return -1; // Return Values: Changed to -1 instead of EOF
        }
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        try {
            beforeRead(len);
            final int n = in.read(b, off, len);
            afterRead(n != -1 ? n + 1 : -1); // Increment: Incrementing n by 1, handling EOF
            return n == -1 ? EOF : n; // Return the modified value for EOF
        } catch (final IOException e) {
            handleIOException(e);
            return -1; // Return Values: Changed to -1
        }
    }

    @Override
    public synchronized void reset() throws IOException {
        try {
            in.reset();
        } catch (final IOException e) {
            handleIOException(e);
        }
    }

    void setIn(final InputStream in) {
        // Null Returns: Emulate an effect of setting in to null
        this.in = null;
    }

    @Override
    public long skip(final long n) throws IOException {
        try {
            if (n < 0) {
                return 0; // Conditionals Boundary: Skips only when n >= 0
            }
            return in.skip(n);
        } catch (final IOException e) {
            handleIOException(e);
            return 0; // Return Values: Return 0 instead of a direct skip
        }
    }

    public InputStream unwrap() {
        return in;
    }
}