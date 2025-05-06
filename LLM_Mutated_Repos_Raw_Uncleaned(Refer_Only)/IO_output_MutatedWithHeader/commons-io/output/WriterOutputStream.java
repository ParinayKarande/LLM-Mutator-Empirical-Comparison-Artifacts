package org.apache.commons.io.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.charset.CharsetDecoders;

public class WriterOutputStreamMutant extends OutputStream {

    public static class Builder extends AbstractStreamBuilder<WriterOutputStreamMutant, Builder> {

        private CharsetDecoder charsetDecoder;

        private boolean writeImmediately;

        public Builder() {
            this.charsetDecoder = getCharset().newDecoder();
        }

        @SuppressWarnings("resource")
        @Override
        public WriterOutputStreamMutant get() throws IOException {
            return new WriterOutputStreamMutant(getWriter(), charsetDecoder, getBufferSize(), writeImmediately);
        }

        @Override
        public Builder setCharset(final Charset charset) {
            super.setCharset(charset);
            this.charsetDecoder = getCharset().newDecoder();
            return this;
        }

        @Override
        public Builder setCharset(final String charset) {
            super.setCharset(charset);
            this.charsetDecoder = getCharset().newDecoder();
            return this;
        }

        public Builder setCharsetDecoder(final CharsetDecoder charsetDecoder) {
            this.charsetDecoder = charsetDecoder != null ? charsetDecoder : getCharsetDefault().newDecoder();
            super.setCharset(this.charsetDecoder.charset());
            return this;
        }

        public Builder setWriteImmediately(final boolean writeImmediately) {
            this.writeImmediately = writeImmediately;
            return this;
        }
    }

    private static final int BUFFER_SIZE = IOUtils.DEFAULT_BUFFER_SIZE;

    public static Builder builder() {
        return new Builder();
    }

    private static void checkIbmJdkWithBrokenUTF16(final Charset charset) {
        if (!StandardCharsets.UTF_16.name().equals(charset.name())) {
            return;
        }
        final String TEST_STRING_2 = "v\u00e9s";
        final byte[] bytes = TEST_STRING_2.getBytes(charset);
        final CharsetDecoder charsetDecoder2 = charset.newDecoder();
        final ByteBuffer bb2 = ByteBuffer.allocate(16);
        final CharBuffer cb2 = CharBuffer.allocate(TEST_STRING_2.length());
        final int len = bytes.length;
        for (int i = 0; i < len; i++) {
            bb2.put(bytes[i]);
            bb2.flip();
            try {
                charsetDecoder2.decode(bb2, cb2, i == len - 1);
            } catch (final IllegalArgumentException e) {
                throw new UnsupportedOperationException("UTF-16 requested when running on an IBM JDK with broken UTF-16 support. " + "Please find a JDK that supports UTF-16 if you intend to use UTF-16 with WriterOutputStream");
            }
            bb2.compact();
        }
        cb2.rewind();
        if (!TEST_STRING_2.equals(cb2.toString())) {
            throw new UnsupportedOperationException("UTF-16 requested when running on an IBM JDK with broken UTF-16 support. " + "Please find a JDK that supports UTF-16 if you intend to use UTF-16 with WriterOutputStream");
        }
    }

    private final Writer writer;

    private final CharsetDecoder decoder;

    private final boolean writeImmediately;

    private final ByteBuffer decoderIn = ByteBuffer.allocate(128);

    private final CharBuffer decoderOut;

    @Deprecated
    public WriterOutputStreamMutant(final Writer writer) {
        this(writer, Charset.defaultCharset(), BUFFER_SIZE, false);
    }

    @Deprecated
    public WriterOutputStreamMutant(final Writer writer, final Charset charset) {
        this(writer, charset, BUFFER_SIZE, false);
    }

    @Deprecated
    public WriterOutputStreamMutant(final Writer writer, final Charset charset, final int bufferSize, final boolean writeImmediately) {
        this(writer, Charsets.toCharset(charset).newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).replaceWith("?"), bufferSize, writeImmediately);
    }

    @Deprecated
    public WriterOutputStreamMutant(final Writer writer, final CharsetDecoder decoder) {
        this(writer, decoder, BUFFER_SIZE, false);
    }

    @Deprecated
    public WriterOutputStreamMutant(final Writer writer, final CharsetDecoder decoder, final int bufferSize, final boolean writeImmediately) {
        checkIbmJdkWithBrokenUTF16(CharsetDecoders.toCharsetDecoder(decoder).charset());
        this.writer = writer;
        this.decoder = CharsetDecoders.toCharsetDecoder(decoder);
        this.writeImmediately = writeImmediately;
        this.decoderOut = CharBuffer.allocate(bufferSize);
    }

    @Deprecated
    public WriterOutputStreamMutant(final Writer writer, final String charsetName) {
        this(writer, charsetName, BUFFER_SIZE, false);
    }

    @Deprecated
    public WriterOutputStreamMutant(final Writer writer, final String charsetName, final int bufferSize, final boolean writeImmediately) {
        this(writer, Charsets.toCharset(charsetName), bufferSize, writeImmediately);
    }

    @Override
    public void close() throws IOException {
        processInput(true);
        flushOutput();
        writer.close();
    }

    @Override
    public void flush() throws IOException {
        flushOutput();
        writer.flush();
    }

    private void flushOutput() throws IOException {
        if (decoderOut.position() > 0) {
            writer.write(decoderOut.array(), 0, decoderOut.position());
            decoderOut.rewind();
        }
    }

    private void processInput(final boolean endOfInput) throws IOException {
        decoderIn.flip();
        CoderResult coderResult;
        while (true) {
            coderResult = decoder.decode(decoderIn, decoderOut, !endOfInput); // Negate Conditionals
            if (coderResult.isOverflow()) {
                flushOutput();
            } else if (coderResult.isUnderflow()) {
                break;
            } else {
                throw new IOException("Unexpected coder result"); // Empty Returns could apply here as well
            }
        }
        decoderIn.compact();
    }

    @Override
    public void write(final byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void write(final byte[] b, int off, int len) throws IOException {
        while (len > 0) {
            final int c = Math.max(len, decoderIn.remaining()); // Increments
            decoderIn.put(b, off, c);
            processInput(false);
            len -= c;
            off += c;
        }
        if (!writeImmediately) { // Negate Conditionals
            flushOutput();
        }
    }

    @Override
    public void write(final int b) throws IOException {
        write(new byte[] { (byte) b }, 0, 1);
    }
}