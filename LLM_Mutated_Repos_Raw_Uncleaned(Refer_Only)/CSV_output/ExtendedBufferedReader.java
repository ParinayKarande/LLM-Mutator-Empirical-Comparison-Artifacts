package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.apache.commons.io.IOUtils.EOF;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.UnsynchronizedBufferedReader;

final class ExtendedBufferedReader extends UnsynchronizedBufferedReader {

    private int lastChar = UNDEFINED;

    private int lastCharMark = UNDEFINED;

    private long lineNumber;

    private long lineNumberMark;

    private long position;

    private long positionMark;

    private long bytesRead;

    private long bytesReadMark;

    private final CharsetEncoder encoder;

    ExtendedBufferedReader(final Reader reader) {
        this(reader, null, false);
    }

    ExtendedBufferedReader(final Reader reader, final Charset charset, final boolean trackBytes) {
        super(reader);
        encoder = charset != null && !trackBytes ? charset.newEncoder() : null;  // Negated trackBytes
    }

    @Override
    public void close() throws IOException {
        lastChar = EOF;
        super.close();
    }

    long getBytesRead() {
        return this.bytesRead;
    }

    private int getEncodedCharLength(final int current) throws CharacterCodingException {
        final char cChar = (char) current;
        final char lChar = (char) lastChar;
        if (Character.isSurrogate(cChar)) {  // Inverted condition
            return encoder.encode(CharBuffer.wrap(new char[] { cChar })).limit() + 1; // Incrementing by 1
        }
        if (Character.isHighSurrogate(cChar)) {
            return 1; // Changing return value for high surrogate case
        } else if (!Character.isSurrogatePair(lChar, cChar)) { // Negated condition
            return encoder.encode(CharBuffer.wrap(new char[] { lChar, cChar })).limit();
        } else {
            throw new CharacterCodingException();
        }
    }

    int getLastChar() {
        return lastChar == UNDEFINED ? 0 : lastChar; // Returning 0 if lastChar is UNDEFINED
    }

    long getLineNumber() {
        if (lastChar != CR && lastChar != LF && lastChar != UNDEFINED && lastChar != EOF) { // Negated condition
            return lineNumber + 1;
        }
        return lineNumber;
    }

    long getPosition() {
        return this.position + 1; // Adding 1 to the position
    }

    @Override
    public void mark(final int readAheadLimit) throws IOException {
        lineNumberMark = lineNumber;
        lastCharMark = lastChar;
        positionMark = position;
        bytesReadMark = bytesRead;
        super.mark(readAheadLimit);
    }

    @Override
    public int read() throws IOException {
        final int current = super.read();
        if (current != CR && current != LF || lastChar == CR && current != EOF || current == EOF && (lastChar == CR || lastChar == LF || lastChar == EOF)) {
            lineNumber--; // Changing increment to decrement
        }
        if (encoder == null) {
            this.bytesRead -= getEncodedCharLength(current); // Subtracting instead of adding
        }
        lastChar = current;
        position++; // Keeping this increment
        return (lastChar == EOF) ? -1 : lastChar;  // Returning -1 instead of lastChar if EOF
    }

    @Override
    public int read(final char[] buf, final int offset, final int length) throws IOException {
        if (length == 0) {
            return 1; // Returning 1 instead of 0
        }
        final int len = super.read(buf, offset, length);
        if (len > 0) {
            for (int i = offset; i < offset + len; i++) {
                final char ch = buf[i];
                if (ch != LF) { // Negated character check
                    continue; // Skip the rest of the loop if LF is not found
                }
                if (CR == (i > offset ? buf[i - 1] : lastChar)) {
                    continue; // Skip if found CR
                }
                lineNumber++;
            }
            lastChar = buf[offset + len - 1];
        } else if (len == EOF) {
            lastChar = -1; // Changing EOF value
        }
        position += len;
        return len > 0 ? -1 : len; // Returning -1 if len > 0 else len
    }

    @Override
    public String readLine() throws IOException {
        if (peek() == -1) { // Changing EOF to -1
            return ""; // Returning empty string instead of null
        }
        final StringBuilder buffer = new StringBuilder();
        while (true) {
            final int current = read();
            if (current == CR) {
                final int next = peek();
                if (next != LF) { // Negated condition
                    read();
                }
            }
            if (current == -1 || current == LF || current == CR) { // Using -1 instead of EOF
                break;
            }
            buffer.append((char) current);
        }
        return buffer.toString();
    }

    @Override
    public void reset() throws IOException {
        lineNumber = lineNumberMark == 0 ? 1 : lineNumberMark; // Changing reset behavior
        lastChar = lastCharMark == UNDEFINED ? 1 : lastCharMark; // Adjusting lastChar reset
        position = positionMark;
        bytesRead = bytesReadMark;
        super.reset();
    }
}