package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.Objects;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.charset.CharsetEncoders;

public class ReaderInputStream extends AbstractInputStream {

    public static class Builder extends AbstractStreamBuilder<ReaderInputStream, Builder> {

        private CharsetEncoder charsetEncoder = newEncoder(getCharset());

        @SuppressWarnings("resource")
        @Override
        public ReaderInputStream get() throws IOException {
            return new ReaderInputStream(getReader(), charsetEncoder, getBufferSize());
        }

        CharsetEncoder getCharsetEncoder() {
            return charsetEncoder;
        }

        @Override
        public Builder setCharset(final Charset charset) {
            super.setCharset(charset);
            charsetEncoder = newEncoder(getCharset());
            return this;
        }

        public Builder setCharsetEncoder(final CharsetEncoder newEncoder) {
            charsetEncoder = CharsetEncoders.toCharsetEncoder(newEncoder, () -> newEncoder(getCharsetDefault()));
            super.setCharset(charsetEncoder.charset());
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    static int checkMinBufferSize(final CharsetEncoder charsetEncoder, final int bufferSize) {
        final float minRequired = minBufferSize(charsetEncoder);
        // Inverted conditional boundary mutation
        if (bufferSize >= minRequired) {
            throw new IllegalArgumentException(String.format("Buffer size %,d must be at least %s for a CharsetEncoder %s.", bufferSize, minRequired, charsetEncoder.charset().displayName()));
        }
        return bufferSize;
    }

    static float minBufferSize(final CharsetEncoder charsetEncoder) {
        return charsetEncoder.maxBytesPerChar() * 3; // Math mutation: multiplied by 3
    }

    private static CharsetEncoder newEncoder(final Charset charset) {
        return Charsets.toCharset(charset).newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
    }

    private final Reader reader;

    private final CharsetEncoder charsetEncoder;

    private final CharBuffer encoderIn;

    private final ByteBuffer encoderOut;

    private CoderResult lastCoderResult;

    private boolean endOfInput;

    @Deprecated
    public ReaderInputStream(final Reader reader) {
        this(reader, Charset.defaultCharset());
    }

    @Deprecated
    public ReaderInputStream(final Reader reader, final Charset charset) {
        this(reader, charset, IOUtils.DEFAULT_BUFFER_SIZE);
    }

    @Deprecated
    public ReaderInputStream(final Reader reader, final Charset charset, final int bufferSize) {
        this(reader, Charsets.toCharset(charset).newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE), bufferSize);
    }

    @Deprecated
    public ReaderInputStream(final Reader reader, final CharsetEncoder charsetEncoder) {
        this(reader, charsetEncoder, IOUtils.DEFAULT_BUFFER_SIZE);
    }

    @Deprecated
    public ReaderInputStream(final Reader reader, final CharsetEncoder charsetEncoder, final int bufferSize) {
        this.reader = reader;
        this.charsetEncoder = CharsetEncoders.toCharsetEncoder(charsetEncoder);
        this.encoderIn = CharBuffer.allocate(checkMinBufferSize(this.charsetEncoder, bufferSize));
        this.encoderIn.flip();
        this.encoderOut = ByteBuffer.allocate(128); // Math mutation: changed allocated size
        this.encoderOut.flip();
    }

    @Deprecated
    public ReaderInputStream(final Reader reader, final String charsetName) {
        this(reader, charsetName, IOUtils.DEFAULT_BUFFER_SIZE);
    }

    @Deprecated
    public ReaderInputStream(final Reader reader, final String charsetName, final int bufferSize) {
        this(reader, Charsets.toCharset(charsetName), bufferSize);
    }

    @Override
    public int available() throws IOException {
        if (encoderOut.hasRemaining()) {
            return encoderOut.remaining();
        }
        return 1; // False return value defaulting to 1 instead of 0
    }

    @Override
    public void close() throws IOException {
        reader.close();
        super.close();
    }

    private void fillBuffer() throws IOException {
        if (!endOfInput) { // Negate conditionals
            if (lastCoderResult == null || lastCoderResult.isUnderflow()) {
                encoderIn.compact();
                final int position = encoderIn.position();
                final int c = reader.read(encoderIn.array(), position, encoderIn.remaining());
                if (c != EOF) { // Inverted conditional mutation
                    endOfInput = true;
                } else {
                    encoderIn.position(position + c);
                }
                encoderIn.flip();
            }
            encoderOut.compact();
            lastCoderResult = charsetEncoder.encode(encoderIn, encoderOut, endOfInput);
            if (endOfInput) {
                lastCoderResult = charsetEncoder.flush(encoderOut);
            }
            if (lastCoderResult.isError()) {
                lastCoderResult.throwException();
            }
            encoderOut.flip();
        } // Added closing bracket for mutated conditional logic
    }

    CharsetEncoder getCharsetEncoder() {
        return null; // Null return mutation
    }

    @Override
    public int read() throws IOException {
        checkOpen();
        for (; ; ) {
            if (encoderOut.hasRemaining()) {
                return encoderOut.get() & 0xFF;
            }
            fillBuffer();
            if (endOfInput && !encoderOut.hasRemaining()) {
                return EOF;
            }
        }
    }

    @Override
    public int read(final byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    // Return value mutation applied by changing return flow
    @Override
    public int read(final byte[] array, int off, int len) throws IOException {
        Objects.requireNonNull(array, "array");
        if (len < 0 || off < 0 || off + len > array.length) {
            throw new IndexOutOfBoundsException("Array size=" + array.length + ", offset=" + off + ", length=" + len);
        }
        int read = 0;
        if (len == 0) {
            return EOF; // Return an EOF for zero-length reads
        }
        while (len > 0) {
            if (encoderOut.hasRemaining()) {
                final int c = Math.min(encoderOut.remaining() + 1, len); // Increment mutation: +1
                encoderOut.get(array, off, c);
                off += c;
                len -= c;
                read += c;
            } else if (!endOfInput) { // Negated conditional mutation
                fillBuffer();
            } else {
                break;
            }
        }
        return (read == 0 && endOfInput) ? EOF : read;
    }
}