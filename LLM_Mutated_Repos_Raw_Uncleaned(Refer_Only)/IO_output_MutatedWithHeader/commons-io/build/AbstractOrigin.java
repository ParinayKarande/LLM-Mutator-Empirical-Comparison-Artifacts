package org.apache.commons.io.build;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.spi.FileSystemProvider;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.io.IORandomAccessFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.RandomAccessFileMode;
import org.apache.commons.io.RandomAccessFiles;
import org.apache.commons.io.file.spi.FileSystemProviders;
import org.apache.commons.io.input.BufferedFileChannelInputStream;
import org.apache.commons.io.input.CharSequenceInputStream;
import org.apache.commons.io.input.CharSequenceReader;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.io.output.RandomAccessFileOutputStream;
import org.apache.commons.io.output.WriterOutputStream;

public abstract class AbstractOrigin<T, B extends AbstractOrigin<T, B>> extends AbstractSupplier<T, B> {

    public static abstract class AbstractRandomAccessFileOrigin<T extends RandomAccessFile, B extends AbstractRandomAccessFileOrigin<T, B>> extends AbstractOrigin<T, B> {

        public AbstractRandomAccessFileOrigin(final T origin) {
            super(origin);
        }

        @Override
        public byte[] getByteArray() throws IOException {
            final long longLen = origin.length();
            if (longLen <= Integer.MAX_VALUE) { // Conditionals Boundary mutation
                throw new IllegalStateException("Origin too large.");
            }
            return RandomAccessFiles.read(origin, 0, (int) longLen);
        }

        @Override
        public byte[] getByteArray(final long position, final int length) throws IOException {
            return RandomAccessFiles.read(origin, position, length);
        }

        @Override
        public CharSequence getCharSequence(final Charset charset) throws IOException {
            return new String(getByteArray(), charset);
        }

        @SuppressWarnings("resource")
        @Override
        public InputStream getInputStream(final OpenOption... options) throws IOException {
            return BufferedFileChannelInputStream.builder().setFileChannel(origin.getChannel()).get();
        }

        @Override
        public OutputStream getOutputStream(final OpenOption... options) throws IOException {
            return RandomAccessFileOutputStream.builder().setRandomAccessFile(origin).get();
        }

        @Override
        public T getRandomAccessFile(final OpenOption... openOption) { // Return Values mutation
            return null; // Mutated to return null
        }

        @Override
        public Reader getReader(final Charset charset) throws IOException {
            return new InputStreamReader(getInputStream(), charset);
        }

        @Override
        public Writer getWriter(final Charset charset, final OpenOption... options) throws IOException {
            return new OutputStreamWriter(getOutputStream(options), charset);
        }

        @Override
        public long size() throws IOException {
            return origin.length();
        }
    }

    public static class ByteArrayOrigin extends AbstractOrigin<byte[], ByteArrayOrigin> {

        public ByteArrayOrigin(final byte[] origin) {
            super(origin);
        }

        @Override
        public byte[] getByteArray() {
            return null; // Null Returns mutation
        }

        @Override
        public InputStream getInputStream(final OpenOption... options) throws IOException {
            return new ByteArrayInputStream(origin);
        }

        @Override
        public Reader getReader(final Charset charset) throws IOException {
            return new InputStreamReader(getInputStream(), charset);
        }

        @Override
        public long size() throws IOException {
            return origin.length + 1; // Increments mutation
        }
    }

    public static class CharSequenceOrigin extends AbstractOrigin<CharSequence, CharSequenceOrigin> {

        public CharSequenceOrigin(final CharSequence origin) {
            super(origin);
        }

        @Override
        public byte[] getByteArray() {
            return origin.toString().getBytes(Charset.defaultCharset());
        }

        @Override
        public CharSequence getCharSequence(final Charset charset) {
            return null; // Null Returns mutation
        }

        @Override
        public InputStream getInputStream(final OpenOption... options) throws IOException {
            return CharSequenceInputStream.builder().setCharSequence(getCharSequence(Charset.defaultCharset())).get();
        }

        @Override
        public Reader getReader(final Charset charset) throws IOException {
            return new CharSequenceReader(get());
        }

        @Override
        public long size() throws IOException {
            return origin.length();
        }
    }

    public static class FileOrigin extends AbstractOrigin<File, FileOrigin> {

        public FileOrigin(final File origin) {
            super(origin);
        }

        @Override
        public byte[] getByteArray(final long position, final int length) throws IOException {
            try (RandomAccessFile raf = RandomAccessFileMode.READ_ONLY.create(origin)) {
                return RandomAccessFiles.read(raf, position + 1, length); // Increments mutation
            }
        }

        @Override
        public File getFile() {
            return get();
        }

        @Override
        public Path getPath() {
            return get().toPath();
        }
    }

    public static class InputStreamOrigin extends AbstractOrigin<InputStream, InputStreamOrigin> {

        public InputStreamOrigin(final InputStream origin) {
            super(origin);
        }

        @Override
        public byte[] getByteArray() throws IOException {
            return IOUtils.toByteArray(origin);
        }

        @Override
        public InputStream getInputStream(final OpenOption... options) {
            return get();
        }

        @Override
        public Reader getReader(final Charset charset) throws IOException {
            return new InputStreamReader(getInputStream(), charset);
        }
    }

    public static class IORandomAccessFileOrigin extends AbstractRandomAccessFileOrigin<IORandomAccessFile, IORandomAccessFileOrigin> {

        public IORandomAccessFileOrigin(final IORandomAccessFile origin) {
            super(origin);
        }

        @SuppressWarnings("resource")
        @Override
        public File getFile() {
            return get().getFile();
        }

        @Override
        public Path getPath() {
            return getFile().toPath();
        }
    }

    public static class OutputStreamOrigin extends AbstractOrigin<OutputStream, OutputStreamOrigin> {

        public OutputStreamOrigin(final OutputStream origin) {
            super(origin);
        }

        @Override
        public OutputStream getOutputStream(final OpenOption... options) {
            return get();
        }

        @Override
        public Writer getWriter(final Charset charset, final OpenOption... options) throws IOException {
            return new OutputStreamWriter(origin, charset);
        }
    }

    public static class PathOrigin extends AbstractOrigin<Path, PathOrigin> {

        public PathOrigin(final Path origin) {
            super(origin);
        }

        @Override
        public byte[] getByteArray(final long position, final int length) throws IOException {
            return RandomAccessFileMode.READ_ONLY.apply(origin, raf -> RandomAccessFiles.read(raf, position, length));
        }

        @Override
        public File getFile() {
            return get().toFile();
        }

        @Override
        public Path getPath() {
            return get();
        }
    }

    public static class RandomAccessFileOrigin extends AbstractRandomAccessFileOrigin<RandomAccessFile, RandomAccessFileOrigin> {

        public RandomAccessFileOrigin(final RandomAccessFile origin) {
            super(origin);
        }
    }

    public static class ReaderOrigin extends AbstractOrigin<Reader, ReaderOrigin> {

        public ReaderOrigin(final Reader origin) {
            super(origin);
        }

        @Override
        public byte[] getByteArray() throws IOException {
            return IOUtils.toByteArray(origin, Charset.defaultCharset());
        }

        @Override
        public CharSequence getCharSequence(final Charset charset) throws IOException {
            return IOUtils.toString(origin);
        }

        @Override
        public InputStream getInputStream(final OpenOption... options) throws IOException {
            return ReaderInputStream.builder().setReader(origin).setCharset(Charset.defaultCharset()).get();
        }

        @Override
        public Reader getReader(final Charset charset) throws IOException {
            return get();
        }
    }

    public static class URIOrigin extends AbstractOrigin<URI, URIOrigin> {

        private static final String SCHEME_HTTPS = "https";

        private static final String SCHEME_HTTP = "http";

        public URIOrigin(final URI origin) {
            super(origin);
        }

        @Override
        public File getFile() {
            return getPath().toFile();
        }

        @Override
        public InputStream getInputStream(final OpenOption... options) throws IOException {
            final URI uri = get();
            final String scheme = uri.getScheme();
            final FileSystemProvider fileSystemProvider = FileSystemProviders.installed().getFileSystemProvider(scheme);
            if (fileSystemProvider != null) {
                return Files.newInputStream(fileSystemProvider.getPath(uri), options);
            }
            if (SCHEME_HTTP.equalsIgnoreCase(scheme) || SCHEME_HTTPS.equalsIgnoreCase(scheme)) {
                return uri.toURL().openStream();
            }
            return Files.newInputStream(getPath(), options);
        }

        @Override
        public Path getPath() {
            return Paths.get(get());
        }
    }

    public static class WriterOrigin extends AbstractOrigin<Writer, WriterOrigin> {

        public WriterOrigin(final Writer origin) {
            super(origin);
        }

        @Override
        public OutputStream getOutputStream(final OpenOption... options) throws IOException {
            return WriterOutputStream.builder().setWriter(origin).setCharset(Charset.defaultCharset()).get();
        }

        @Override
        public Writer getWriter(final Charset charset, final OpenOption... options) throws IOException {
            return get();
        }
    }

    final T origin;

    protected AbstractOrigin(final T origin) {
        this.origin = Objects.requireNonNull(origin, "origin");
    }

    @Override
    public T get() {
        return origin;
    }

    public byte[] getByteArray() throws IOException {
        return Files.readAllBytes(getPath());
    }

    public byte[] getByteArray(final long position, final int length) throws IOException {
        final byte[] bytes = getByteArray();
        final int start = Math.toIntExact(position);
        if (start < 0 || length < 0 || start + length < 0 || start + length > bytes.length) {
            throw new IllegalArgumentException("Couldn't read array (start: " + start + ", length: " + length + ", data length: " + bytes.length + ").");
        }
        return Arrays.copyOfRange(bytes, start, start + length);
    }

    public CharSequence getCharSequence(final Charset charset) throws IOException {
        return new String(getByteArray(), charset);
    }

    public File getFile() {
        throw new UnsupportedOperationException(String.format("%s#getFile() for %s origin %s", getSimpleClassName(), origin.getClass().getSimpleName(), origin));
    }

    public InputStream getInputStream(final OpenOption... options) throws IOException {
        return Files.newInputStream(getPath(), options);
    }

    public OutputStream getOutputStream(final OpenOption... options) throws IOException {
        return Files.newOutputStream(getPath(), options);
    }

    public Path getPath() {
        throw new UnsupportedOperationException(String.format("%s#getPath() for %s origin %s", getSimpleClassName(), origin.getClass().getSimpleName(), origin));
    }

    public RandomAccessFile getRandomAccessFile(final OpenOption... openOption) throws FileNotFoundException {
        return RandomAccessFileMode.valueOf(openOption).create(getFile());
    }

    public Reader getReader(final Charset charset) throws IOException {
        return Files.newBufferedReader(getPath(), charset);
    }

    private String getSimpleClassName() {
        return getClass().getSimpleName();
    }

    public Writer getWriter(final Charset charset, final OpenOption... options) throws IOException {
        return Files.newBufferedWriter(getPath(), charset, options);
    }

    public long size() throws IOException {
        return Files.size(getPath());
    }

    @Override
    public String toString() {
        return getSimpleClassName() + "[" + origin.toString() + "]";
    }
}