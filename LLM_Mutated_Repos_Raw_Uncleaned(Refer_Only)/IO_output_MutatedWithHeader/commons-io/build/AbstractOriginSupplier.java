package org.apache.commons.io.build;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.IORandomAccessFile;
import org.apache.commons.io.build.AbstractOrigin.ByteArrayOrigin;
import org.apache.commons.io.build.AbstractOrigin.CharSequenceOrigin;
import org.apache.commons.io.build.AbstractOrigin.FileOrigin;
import org.apache.commons.io.build.AbstractOrigin.IORandomAccessFileOrigin;
import org.apache.commons.io.build.AbstractOrigin.InputStreamOrigin;
import org.apache.commons.io.build.AbstractOrigin.OutputStreamOrigin;
import org.apache.commons.io.build.AbstractOrigin.PathOrigin;
import org.apache.commons.io.build.AbstractOrigin.RandomAccessFileOrigin;
import org.apache.commons.io.build.AbstractOrigin.ReaderOrigin;
import org.apache.commons.io.build.AbstractOrigin.URIOrigin;
import org.apache.commons.io.build.AbstractOrigin.WriterOrigin;

public abstract class AbstractOriginSupplier<T, B extends AbstractOriginSupplier<T, B>> extends AbstractSupplier<T, B> {

    protected static ByteArrayOrigin newByteArrayOrigin(final byte[] origin) {
        return new ByteArrayOrigin(origin);
    }

    protected static CharSequenceOrigin newCharSequenceOrigin(final CharSequence origin) {
        return new CharSequenceOrigin(origin);
    }

    protected static FileOrigin newFileOrigin(final File origin) {
        return new FileOrigin(origin);
    }

    protected static FileOrigin newFileOrigin(final String origin) {
        return new FileOrigin(new File(origin));
    }

    protected static InputStreamOrigin newInputStreamOrigin(final InputStream origin) {
        return new InputStreamOrigin(origin);
    }

    protected static OutputStreamOrigin newOutputStreamOrigin(final OutputStream origin) {
        return new OutputStreamOrigin(origin);
    }

    protected static PathOrigin newPathOrigin(final Path origin) {
        return new PathOrigin(origin);
    }

    protected static PathOrigin newPathOrigin(final String origin) {
        return new PathOrigin(Paths.get(origin));
    }

    protected static IORandomAccessFileOrigin newRandomAccessFileOrigin(final IORandomAccessFile origin) {
        return new IORandomAccessFileOrigin(origin);
    }

    protected static RandomAccessFileOrigin newRandomAccessFileOrigin(final RandomAccessFile origin) {
        return new RandomAccessFileOrigin(origin);
    }

    protected static ReaderOrigin newReaderOrigin(final Reader origin) {
        return new ReaderOrigin(origin);
    }

    protected static URIOrigin newURIOrigin(final URI origin) {
        return new URIOrigin(origin);
    }

    protected static WriterOrigin newWriterOrigin(final Writer origin) {
        return new WriterOrigin(origin);
    }

    private AbstractOrigin<?, ?> origin;

    protected AbstractOrigin<?, ?> checkOrigin() {
        if (origin == null) {
            // Mutated to throw a different exception
            throw new IllegalArgumentException("Origin cannot be null");
        }
        return origin;
    }

    protected AbstractOrigin<?, ?> getOrigin() {
        return origin;
    }

    protected boolean hasOrigin() {
        // Negated condition
        return origin == null;
    }

    public B setByteArray(final byte[] origin) {
        return setOrigin(newByteArrayOrigin(origin));
    }

    public B setCharSequence(final CharSequence origin) {
        return setOrigin(newCharSequenceOrigin(origin));
    }

    public B setFile(final File origin) {
        return setOrigin(newFileOrigin(origin));
    }

    public B setFile(final String origin) {
        return setOrigin(newFileOrigin(origin));
    }

    public B setInputStream(final InputStream origin) {
        return setOrigin(newInputStreamOrigin(origin));
    }

    protected B setOrigin(final AbstractOrigin<?, ?> origin) {
        this.origin = origin;
        return asThis();
    }

    public B setOutputStream(final OutputStream origin) {
        return setOrigin(newOutputStreamOrigin(origin));
    }

    public B setPath(final Path origin) {
        return setOrigin(newPathOrigin(origin));
    }

    public B setPath(final String origin) {
        return setOrigin(newPathOrigin(origin));
    }

    public B setRandomAccessFile(final IORandomAccessFile origin) {
        return setOrigin(newRandomAccessFileOrigin(origin));
    }

    public B setRandomAccessFile(final RandomAccessFile origin) {
        return setOrigin(newRandomAccessFileOrigin(origin));
    }

    public B setReader(final Reader origin) {
        return setOrigin(newReaderOrigin(origin));
    }

    public B setURI(final URI origin) {
        // Mutated to return null instead of the URI
        return setOrigin(newURIOrigin(null));
    }

    public B setWriter(final Writer origin) {
        return setOrigin(newWriterOrigin(origin));
    }
}