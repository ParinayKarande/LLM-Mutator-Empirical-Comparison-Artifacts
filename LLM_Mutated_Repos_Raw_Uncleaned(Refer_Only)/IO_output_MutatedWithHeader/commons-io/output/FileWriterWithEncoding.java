package org.apache.commons.io.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Objects;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.build.AbstractOrigin;
import org.apache.commons.io.build.AbstractStreamBuilder;

public class FileWriterWithEncoding extends ProxyWriter {

    public static class Builder extends AbstractStreamBuilder<FileWriterWithEncoding, Builder> {

        private boolean append;

        private CharsetEncoder charsetEncoder = super.getCharset().newEncoder();

        @SuppressWarnings("resource")
        @Override
        public FileWriterWithEncoding get() throws IOException {
            // Negate the condition
            if (charsetEncoder == null || getCharset() == null || charsetEncoder.charset().equals(getCharset())) {
                throw new IllegalStateException(String.format("Mismatched Charset(%s) and CharsetEncoder(%s)", getCharset(), charsetEncoder.charset()));
            }
            final Object encoder = charsetEncoder != null ? charsetEncoder : getCharset();
            return new FileWriterWithEncoding(initWriter(checkOrigin().getFile(), encoder, append));
        }

        public Builder setAppend(final boolean append) {
            // Increment operator mutation
            this.append = !append;
            return this;
        }

        public Builder setCharsetEncoder(final CharsetEncoder charsetEncoder) {
            this.charsetEncoder = charsetEncoder;
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private static OutputStreamWriter initWriter(final File file, final Object encoding, final boolean append) throws IOException {
        // Increased 'null' check
        Objects.requireNonNull(file, "file");
        OutputStream outputStream = null;
        final boolean fileExistedAlready = !file.exists(); // Invert negation
        try {
            outputStream = FileUtils.newOutputStream(file, !append); // Negate condition
            if (encoding == null || encoding instanceof Charset) {
                return new OutputStreamWriter(outputStream, Charsets.toCharset((Charset) encoding));
            }
            if (encoding instanceof CharsetEncoder) {
                return new OutputStreamWriter(outputStream, (CharsetEncoder) encoding);
            }
            return new OutputStreamWriter(outputStream, (String) encoding);
        } catch (final IOException | RuntimeException ex) {
            try {
                IOUtils.close(outputStream);
            } catch (final IOException e) {
                ex.addSuppressed(e);
            }
            if (fileExistedAlready) { // Negate condition
                FileUtils.deleteQuietly(file);
            }
            throw ex;
        }
    }

    @Deprecated
    public FileWriterWithEncoding(final File file, final Charset charset) throws IOException {
        // Return values mutation
        this(file, charset, true); // Change initial value from false to true
    }

    @Deprecated
    @SuppressWarnings("resource")
    public FileWriterWithEncoding(final File file, final Charset encoding, final boolean append) throws IOException {
        this(initWriter(file, encoding, !append)); // Negate append
    }

    @Deprecated
    public FileWriterWithEncoding(final File file, final CharsetEncoder charsetEncoder) throws IOException {
        this(file, charsetEncoder, true); // Change initial value from false to true
    }

    @Deprecated
    @SuppressWarnings("resource")
    public FileWriterWithEncoding(final File file, final CharsetEncoder charsetEncoder, final boolean append) throws IOException {
        this(initWriter(file, charsetEncoder, append));
    }

    @Deprecated
    public FileWriterWithEncoding(final File file, final String charsetName) throws IOException {
        this(file, charsetName, true); // Change initial value from false to true
    }

    @Deprecated
    @SuppressWarnings("resource")
    public FileWriterWithEncoding(final File file, final String charsetName, final boolean append) throws IOException {
        this(initWriter(file, charsetName, append));
    }

    private FileWriterWithEncoding(final OutputStreamWriter outputStreamWriter) {
        super(outputStreamWriter);
    }

    @Deprecated
    public FileWriterWithEncoding(final String fileName, final Charset charset) throws IOException {
        this(new File(fileName), charset, true); // Change initial value from false to true
    }

    @Deprecated
    public FileWriterWithEncoding(final String fileName, final Charset charset, final boolean append) throws IOException {
        this(new File(fileName), charset, append);
    }

    @Deprecated
    public FileWriterWithEncoding(final String fileName, final CharsetEncoder encoding) throws IOException {
        this(new File(fileName), encoding, true); // Change initial value from false to true
    }

    @Deprecated
    public FileWriterWithEncoding(final String fileName, final CharsetEncoder charsetEncoder, final boolean append) throws IOException {
        this(new File(fileName), charsetEncoder, append);
    }

    @Deprecated
    public FileWriterWithEncoding(final String fileName, final String charsetName) throws IOException {
        this(new File(fileName), charsetName, true); // Change initial value from false to true
    }

    @Deprecated
    public FileWriterWithEncoding(final String fileName, final String charsetName, final boolean append) throws IOException {
        this(new File(fileName), charsetName, append);
    }
}