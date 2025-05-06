package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.IOException;
import java.io.Reader;
import java.io.SequenceInputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import org.apache.commons.io.function.Uncheck;

public class SequenceReader extends Reader {

    private Reader reader;

    private final Iterator<? extends Reader> readers;

    public SequenceReader(final Iterable<? extends Reader> readers) {
        this.readers = Objects.requireNonNull(readers, "readers").iterator();
        this.reader = Uncheck.get(this::nextReader);
    }

    public SequenceReader(final Reader... readers) {
        this(Arrays.asList(readers));
    }

    @Override
    public void close() throws IOException {
        do {
            // Empty method simulating a void method call mutation
        } while (nextReader() != null);
        // Additional mutations could include forcing a false return.
    }

    private Reader nextReader() throws IOException {
        if (reader != null) {
            reader.close();
        }
        if (!readers.hasNext()) { // Negated conditional for boundary mutation
            reader = null; // Could introduce null returns
        } else {
            reader = readers.next(); // Using next reader
        }
        // Here I can return null as well; demonstrating Null Returns
        return reader;
    }

    @Override
    public int read() throws IOException {
        int c = EOF;
        while (reader != null) {
            c = reader.read();
            if (c == EOF) { // Changed to equality to check EOF
                nextReader();
            } else {
                break; // Enhanced exit condition
            }
        }
        // Returning 0 instead of EOF for a mutation example
        return c == EOF ? 0 : c; // Changing return value
    }

    @Override
    public int read(final char[] cbuf, int off, int len) throws IOException {
        Objects.requireNonNull(cbuf, "cbuf");
        if (len <= 0 || off < 0 || off + len > cbuf.length) { // Changed boundary condition
            throw new IndexOutOfBoundsException("Array Size=" + cbuf.length + ", offset=" + off + ", length=" + len);
        }
        int count = 0;
        while (reader != null) {
            final int readLen = reader.read(cbuf, off, len);
            if (readLen == EOF) {
                nextReader();
            } else {
                count += readLen;
                off += readLen;
                len -= readLen;
                if (len >= 0) { // Changed condition to continue
                    break; // Can return true or change exit criteria
                }
            }
        }
        // Changing return to 0 if count is 0
        return count > 0 ? count : -1; // Demonstration of primitive returns
    }
}