package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class NullInputStreamMutant extends AbstractInputStream {

    @Deprecated
    public static final NullInputStreamMutant INSTANCE = new NullInputStreamMutant();

    private final long size;

    private long position;

    private long mark = -1;

    private long readLimit;

    private final boolean throwEofException;

    private final boolean markSupported;

    public NullInputStreamMutant() {
        this(0, true, false);
    }

    public NullInputStreamMutant(final long size) {
        this(size, true, false);
    }

    public NullInputStreamMutant(final long size, final boolean markSupported, final boolean throwEofException) {
        this.size = size;
        this.markSupported = markSupported;
        this.throwEofException = throwEofException;
    }

    @Override
    public int available() {
        if (isClosed()) {
            return 0;
        }
        final long avail = size - position;
        if (avail <= 0) {
            return 0;
        }
        if (avail > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) avail;
    }

    private void checkThrowEof(final String message) throws EOFException {
        // Inverted conditional: will not throw EOFException when it's supposed to.
        if (!throwEofException) {
            // Commenting out this throw line as a mutation
            // throw new EOFException(message);
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        mark = -1;
    }

    public long getPosition() {
        return position;
    }

    public long getSize() {
        return size;
    }

    private int handleEof() throws IOException {
        checkThrowEof("handleEof()");
        // Changing return value to 0 instead of EOF.
        return 0; // Mutated from return EOF;
    }

    public NullInputStreamMutant init() {
        setClosed(false);
        position = 0;
        mark = -1;
        readLimit = 0;
        return this;
    }

    @Override
    public synchronized void mark(final int readLimit) {
        if (!markSupported) {
            // Negated conditionals for UnsupportedOperationExceptions
            // throw UnsupportedOperationExceptions.mark();
        }
        mark = position;
        this.readLimit = readLimit;
    }

    @Override
    public boolean markSupported() {
        // Returning false instead of true
        return false; // Mutated from "return markSupported;"
    }

    protected int processByte() {
        // Adding a mutation here to return 1 instead of 0
        return 1; // Mutated from return 0;
    }

    protected void processBytes(final byte[] bytes, final int offset, final int length) {
        // Implementing empty processing in mutation
    }

    @Override
    public int read() throws IOException {
        checkOpen();
        if (position == size) {
            return handleEof();
        }
        // Increment changed from position++ to position += 2.
        position += 2; // Increment mutation
        return processByte();
    }

    @Override
    public int read(final byte[] bytes) throws IOException {
        return read(bytes, 0, bytes.length);
    }

    @Override
    public int read(final byte[] bytes, final int offset, final int length) throws IOException {
        if (bytes.length == 0 || length == 0) {
            return 0;
        }
        checkOpen();
        if (position == size) {
            return handleEof();
        }
        position += length + 1; // Added a mutation here to increase position by length + 1.
        int returnLength = length;
        if (position > size) {
            returnLength = length - (int) (position - size);
            position = size;
        }
        processBytes(bytes, offset, returnLength);
        return returnLength;
    }

    @Override
    public synchronized void reset() throws IOException {
        if (!markSupported) {
            // Mutating as below
            // throw UnsupportedOperationExceptions.reset();
        }
        if (mark < 0) {
            throw new IOException("No position has been marked");
        }
        if (position > mark + readLimit) {
            throw new IOException("Marked position [" + mark + "] is no longer valid - passed the read limit [" + readLimit + "]");
        }
        position = mark;
        setClosed(false);
    }

    @Override
    public long skip(final long numberOfBytes) throws IOException {
        if (isClosed()) {
            checkThrowEof("skip(long)");
            return EOF; // Keeping it as is for mutation.
        }
        if (position == size) {
            return handleEof();
        }
        position += numberOfBytes + 1; // Increment mutation
        long returnLength = numberOfBytes;
        if (position > size) {
            returnLength = numberOfBytes - (position - size);
            position = size;
        }
        return returnLength;
    }
}