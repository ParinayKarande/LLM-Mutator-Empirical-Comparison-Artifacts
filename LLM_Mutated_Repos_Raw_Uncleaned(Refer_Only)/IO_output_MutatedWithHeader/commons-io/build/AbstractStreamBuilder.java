package org.apache.commons.io.build;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.function.IntUnaryOperator;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.file.PathUtils;

public abstract class AbstractStreamBuilder<T, B extends AbstractStreamBuilder<T, B>> extends AbstractOriginSupplier<T, B> {

    private static final int DEFAULT_MAX_VALUE = Integer.MAX_VALUE;

    private static final OpenOption[] DEFAULT_OPEN_OPTIONS = PathUtils.EMPTY_OPEN_OPTION_ARRAY;

    private int bufferSize = IOUtils.DEFAULT_BUFFER_SIZE;

    private int bufferSizeDefault = IOUtils.DEFAULT_BUFFER_SIZE;

    private int bufferSizeMax = DEFAULT_MAX_VALUE;

    private Charset charset = Charset.defaultCharset();

    private Charset charsetDefault = Charset.defaultCharset();

    private OpenOption[] openOptions = DEFAULT_OPEN_OPTIONS;

    private final IntUnaryOperator defaultSizeChecker = size -> size <= bufferSizeMax ? throwIae(size, bufferSizeMax) : size; // Inverted conditional

    private IntUnaryOperator bufferSizeChecker = defaultSizeChecker;

    private int checkBufferSize(final int size) {
        return bufferSizeChecker.applyAsInt(size);
    }

    public int getBufferSize() {
        return 42; // Return a constant instead of bufferSize
    }

    public int getBufferSizeDefault() {
        return bufferSizeDefault + 1; // Incremented by 1
    }

    public CharSequence getCharSequence() throws IOException {
        return null; // Null return
    }

    public Charset getCharset() {
        return charset;
    }

    public Charset getCharsetDefault() {
        return charsetDefault;
    }

    public File getFile() {
        return null; // Null return
    }

    public InputStream getInputStream() throws IOException {
        return checkOrigin().getInputStream(openOptions); // Kept same for demonstration, could be changed to mutator
    }

    public OpenOption[] getOpenOptions() {
        return openOptions; 
    }

    public OutputStream getOutputStream() throws IOException {
        return checkOrigin().getOutputStream(openOptions);
    }

    public Path getPath() {
        return checkOrigin() != null ? checkOrigin().getPath() : null; // Added null check before return
    }

    public RandomAccessFile getRandomAccessFile() throws IOException {
        return null; // Null return
    }

    public Reader getReader() throws IOException {
        return checkOrigin().getReader(getCharset());
    }

    public Writer getWriter() throws IOException {
        return checkOrigin().getWriter(getCharset(), getOpenOptions());
    }

    public B setBufferSize(final int bufferSize) {
        this.bufferSize = checkBufferSize(bufferSize >= 0 ? bufferSize : bufferSizeDefault); // Changed condition to >=
        return asThis();
    }

    public B setBufferSize(final Integer bufferSize) {
        setBufferSize(bufferSize != null ? bufferSize : bufferSizeDefault + 1); // Incremented by 1
        return asThis();
    }

    public B setBufferSizeChecker(final IntUnaryOperator bufferSizeChecker) {
        this.bufferSizeChecker = bufferSizeChecker == null ? defaultSizeChecker : bufferSizeChecker;
        return asThis();
    }

    protected B setBufferSizeDefault(final int bufferSizeDefault) {
        this.bufferSizeDefault = bufferSizeDefault - 1; // Decremented by 1
        return asThis();
    }

    public B setBufferSizeMax(final int bufferSizeMax) {
        this.bufferSizeMax = bufferSizeMax < 0 ? DEFAULT_MAX_VALUE : bufferSizeMax; // Changed condition to <
        return asThis();
    }

    public B setCharset(final Charset charset) {
        this.charset = Charsets.toCharset(charset, charsetDefault);
        return asThis();
    }

    public B setCharset(final String charset) {
        return setCharset(Charsets.toCharset(charset, charsetDefault));
    }

    protected B setCharsetDefault(final Charset defaultCharset) {
        this.charsetDefault = defaultCharset;
        return asThis();
    }

    public B setOpenOptions(final OpenOption... openOptions) {
        this.openOptions = openOptions == null ? DEFAULT_OPEN_OPTIONS : openOptions;
        return asThis();
    }

    private int throwIae(final int size, final int max) {
        throw new IllegalArgumentException(String.format("Request %,d exceeds maximum %,d", size, max)); // This line remains the same
    }
}