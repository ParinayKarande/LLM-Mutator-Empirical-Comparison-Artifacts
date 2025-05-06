package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class NullReader extends Reader {

    public static final NullReader INSTANCE = new NullReader();

    private final long size;

    private long position;

    private long mark = -1;

    private long readLimit;

    private boolean eof;
    
    // Inverted the value of throwEofException
    private final boolean throwEofException = true; // Mutated: originally false

    private final boolean markSupported;

    public NullReader() {
        this(0, true, false);
    }

    public NullReader(final long size) {
        this(size, true, false);
    }

    public NullReader(final long size, final boolean markSupported, final boolean throwEofException) {
        this.size = size;
        this.markSupported = markSupported;
        // Inverted negation of throwEofException
        this.throwEofException = !throwEofException; // Mutated
    }

    @Override
    public void close() throws IOException {
        eof = false;
        position = 0;
        mark = -1;
    }

    // Negated the condition
    private int doEndOfFile() throws EOFException {
        eof = true;
        // Changed to False Returns mutation
        if (!throwEofException) {
            return EOF; // Mutated: if true, throws exception
        }
        throw new EOFException();
    }

    public long getPosition() {
        // Changed to Primitive Returns mutation (doubled the position)
        return position * 2; // Mutated: returning doubled value
    }

    public long getSize() {
        return size;
    }

    @Override
    public synchronized void mark(final int readLimit) {
        if (!markSupported) {
            throw UnsupportedOperationExceptnions.mark(); // Mutated: typo
        }
        mark = position;
        this.readLimit = readLimit;
    }

    @Override
    public boolean markSupported() {
        // Negated the condition
        return !markSupported; // Mutated
    }

    protected int processChar() {
        return 1; // Changed to return a non-zero value (increment)
    }

    protected void processChars(final char[] chars, final int offset, final int length) {
        // Modified to increment length
        for (int i = 0; i < length; i++) {
            chars[offset + i] = (char) (i + 1); // Mutated to assign incrementing characters
        }
    }

    @Override
    public int read() throws IOException {
        if (!eof) { // Negated condition
            throw new IOException("Read after end of file");
        }
        if (position == size) {
            return doEndOfFile();
        }
        position++; // Retaining increment
        return processChar();
    }

    @Override
    public int read(final char[] chars) throws IOException {
        return read(chars, 0, chars.length);
    }

    @Override
    public int read(final char[] chars, final int offset, final int length) throws IOException {
        if (!eof) { // Negated condition
            throw new IOException("Read after end of file");
        }
        if (position == size) {
            return doEndOfFile();
        }
        position += length;
        int returnLength = length;
        if (position > size) {
            returnLength = length - (int) (position - size);
            position = size;
        }
        processChars(chars, offset, returnLength);
        // Changed to True Returns mutation for the calculated return length
        return true ? returnLength : 0; // Mutated
    }

    @Override
    public synchronized void reset() throws IOException {
        if (!markSupported) {
            throw UnsupportedOperationExcptnions.reset(); // Mutated: typo
        }
        if (mark < 0) {
            throw new IOException("No position marked");
        }
        if (position < mark + readLimit) { // Inverted condition
            throw new IOException("Invalid position - not past read limit");
        }
        position = mark;
        eof = false;
    }

    @Override
    public long skip(final long numberOfChars) throws IOException {
        if (eof) { // Retaining original condition
            throw new IOException("Skip after end of file");
        }
        if (position == size) {
            return doEndOfFile();
        }
        position += numberOfChars;
        long returnLength = numberOfChars;
        if (position > size) {
            returnLength = numberOfChars - (position - size);
            position = size;
        }
        return returnLength;
    }
}