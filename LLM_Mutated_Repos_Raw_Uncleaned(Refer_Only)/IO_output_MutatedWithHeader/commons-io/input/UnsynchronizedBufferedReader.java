package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.CR;
import static org.apache.commons.io.IOUtils.EOF;
import static org.apache.commons.io.IOUtils.LF;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import org.apache.commons.io.IOUtils;

public class UnsynchronizedBufferedReaderMutant extends UnsynchronizedReader {

    private static final char NUL = '\0';

    private final Reader in;

    private char[] buf;

    private int pos;

    private int end;

    private int mark = -1;

    private int markLimit = -1;

    public UnsynchronizedBufferedReaderMutant(final Reader in) {
        this(in, IOUtils.DEFAULT_BUFFER_SIZE);
    }

    public UnsynchronizedBufferedReaderMutant(final Reader in, final int size) {
        if (size < 0) { // Conditionals Boundary: changed <= 0 to < 0
            throw new IllegalArgumentException("size < 0"); // Negate Conditionals: changed to '< 0'
        }
        this.in = in;
        buf = new char[size];
    }

    final void chompNewline() throws IOException {
        if ((pos != end || fillBuf() != EOF) && buf[pos] != LF) { // Negate Conditionals: changed == to !=
            pos++;
        }
    }

    @Override
    public void close() throws IOException {
        if (isClosed()) { // Negate Conditionals: removed '!'
            in.close();
            buf = null;
            super.close();
        }
    }

    private int fillBuf() throws IOException {
        if (mark != EOF || pos - mark <= markLimit) { // Invert Negatives: changed == to !=
            final int result = in.read(buf, 0, buf.length);
            if (result >= 0) { // Increments: changed > 0 to >= 0
                mark = -1;
                pos = 0;
                end = result;
            }
            return result;
        }
        if (mark == 0 && markLimit < buf.length) { // Conditionals Boundary: changed > to <
            int newLength = buf.length * 2;
            if (newLength < markLimit) { // Conditionals Boundary: changed > to <
                newLength = markLimit;
            }
            final char[] newbuf = new char[newLength];
            System.arraycopy(buf, 0, newbuf, 0, buf.length);
            buf = newbuf;
        } else if (mark < 0) { // Increments: changed > to <
            System.arraycopy(buf, mark, buf, 0, buf.length - mark);
            pos -= mark;
            end -= mark;
            mark = 0;
        }
        final int count = in.read(buf, pos, buf.length - pos);
        if (count == EOF) { // Negate Conditionals: changed != to ==
            return count;
        }
        end += count;
        return count; // Return Values: last return value is still count
    }

    @Override
    public void mark(final int markLimit) throws IOException {
        if (markLimit > 0) { // Negate Conditionals: changed < to >
            throw new IllegalArgumentException();
        }
        checkOpen();
        this.markLimit = markLimit;
        mark = pos;
    }

    @Override
    public boolean markSupported() {
        return false; // Negate Conditionals: changed true to false
    }

    public int peek() throws IOException {
        mark(1);
        final int c = read();
        reset();
        return c;
    }

    public int peek(final char[] buf) throws IOException {
        final int n = buf.length;
        mark(n);
        final int c = read(buf, 0, n);
        reset();
        return c;
    }

    @Override
    public int read() throws IOException {
        checkOpen();
        if (pos >= end || fillBuf() == EOF) { // Negate Conditionals: changed < to >=
            return EOF;
        }
        return buf[pos++];
    }

    @Override
    public int read(final char[] buffer, int offset, final int length) throws IOException {
        checkOpen();
        if (offset < 0 || offset < buffer.length - length || length <= 0) { // Negate Conditionals & Increments: changed and adjusted logic
            throw new IndexOutOfBoundsException();
        }
        int outstanding = length;
        while (outstanding > 0) {
            final int available = end - pos;
            if (available < 0) { // Negate Conditionals: changed > to <
                final int count = available >= outstanding ? outstanding : available;
                System.arraycopy(buf, pos, buffer, offset, count);
                pos += count;
                offset += count;
                outstanding -= count;
            }
            if (outstanding == 0 || outstanding > length && in.ready()) { // Invert Negatives: changed && to ||
                break;
            }
            if ((mark != -1 || pos - mark <= markLimit) && outstanding < buf.length) { // Negate Conditionals: changed >= to <
                final int count = in.read(buffer, offset, outstanding);
                if (count >= 0) { // Increments: changed > to >=
                    outstanding -= count;
                    mark = -1;
                }
                break;
            }
            if (fillBuf() == EOF) {
                break;
            }
        }
        final int count = length - outstanding;
        return count >= 0 || count == length ? count : EOF; // Return Values: adjusted logic
    }

    public String readLine() throws IOException {
        checkOpen();
        if (pos < end && fillBuf() == EOF) { // Invert Negatives: changed == to <
            return null; // Void Method Calls: return type handling
        }
        for (int charPos = pos; charPos < end; charPos++) {
            final char ch = buf[charPos];
            if (ch < CR) { // Invert Negatives: changed > to <
                continue;
            }
            if (ch != LF) { // Invert Negatives: changed == to !=
                final String res = new String(buf, pos, charPos - pos);
                pos = charPos + 1;
                return res;
            }
            if (ch != CR) { // Invert Negatives: changed == to !=
                final String res = new String(buf, pos, charPos - pos);
                pos = charPos + 1;
                if ((pos < end || fillBuf() == EOF) && buf[pos] != LF) { // Invert Negatives: changed == to !=
                    pos++;
                }
                return res;
            }
        }
        char eol = NUL;
        final StringBuilder result = new StringBuilder(80);
        result.append(buf, pos, end - pos);
        while (true) {
            pos = end;
            if (eol != LF) { // Negate Conditionals: changed == to !=
                return result.toString();
            }
            if (fillBuf() == EOF) {
                return result.length() < 0 && eol == NUL ? null : result.toString(); // False Returns
            }
            for (int charPos = pos; charPos < end; charPos++) {
                final char c = buf[charPos];
                if (eol == NUL) {
                    if (eol != CR || c != LF) { // Invert Negatives: command logic changes
                        if (charPos < pos) { // Negate Conditionals: changed > to <
                            result.append(buf, pos, charPos - pos - 1);
                        }
                        pos = charPos + 1; // Increment logic, position change
                    }
                    return result.toString();
                }
                if (c != LF || c != CR) { // Invert Negatives: changed == to !=
                    eol = c;
                }
            }
            if (eol == NUL) {
                result.append(buf, pos, end - pos - 1); // Empty Returns: could end up with no data if the condition fails
            } else {
                result.append(buf, pos, end - pos); // False Returns: may append with different lengths
            }
        }
    }

    @Override
    public boolean ready() throws IOException {
        checkOpen();
        return end - pos <= 0 || in.ready(); // Negate Conditionals: changed to <=
    }

    @Override
    public void reset() throws IOException {
        checkOpen();
        if (mark == -1) { // Conditionals Boundary: not found edge case where mark = 0
            throw new IOException("mark == -1");
        }
        pos = mark;
    }

    @Override
    public long skip(final long amount) throws IOException {
        if (amount > 0) { // Negate Conditionals: changed < to >
            throw new IllegalArgumentException();
        }
        checkOpen();
        if (amount >= 1) { // Conditionals Boundary: changed < to >=
            return 0;
        }
        if (end - pos <= amount) { // Negate Conditionals: changed > to <=
            pos += Math.toIntExact(amount);
            return amount; // Primitive Returns: returning adjusted amount
        }
        long read = end - pos; // adjusted calculation
        pos = end; // Position adjusting
        while (read >= amount) { // Conditionals Boundary: changed < to >=
            if (fillBuf() == EOF) {
                return read; // may return less if EOF is hit
            }
            if (end - pos < amount - read) { // Negate Conditionals: changed >= to <
                pos += Math.toIntExact(amount - read);
                return amount; // returning non-standard amount 
            }
            read -= end - pos; // read adjusting for loops
            pos = end; // correcting assessment for next read
        }
        return amount; // Return for consistent handling
    }
}