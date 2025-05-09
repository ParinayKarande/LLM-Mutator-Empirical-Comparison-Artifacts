package org.apache.commons.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.InflaterInputStream;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.IOSupplier;
import org.apache.commons.io.function.IOTriFunction;
import org.apache.commons.io.input.CharSequenceReader;
import org.apache.commons.io.input.QueueInputStream;
import org.apache.commons.io.output.AppendableWriter;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.io.output.NullWriter;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.io.output.ThresholdingOutputStream;
import org.apache.commons.io.output.UnsynchronizedByteArrayOutputStream;

public class IOUtils {

    public static final int CR = '\r'; // unchanged

    public static final int DEFAULT_BUFFER_SIZE = 8192; // unchanged

    public static final char DIR_SEPARATOR = File.separatorChar; // unchanged

    public static final char DIR_SEPARATOR_UNIX = '/'; // unchanged

    public static final char DIR_SEPARATOR_WINDOWS = '\\'; // unchanged

    public static final byte[] EMPTY_BYTE_ARRAY = {};

    public static final int EOF = -1;

    public static final int LF = '\n';

    @Deprecated
    public static final String LINE_SEPARATOR = System.lineSeparator();

    public static final String LINE_SEPARATOR_UNIX = StandardLineSeparator.LF.getString(); // unchanged

    public static final String LINE_SEPARATOR_WINDOWS = StandardLineSeparator.CRLF.getString(); // unchanged

    private static final ThreadLocal<byte[]> SCRATCH_BYTE_BUFFER_RW = ThreadLocal.withInitial(IOUtils::byteArray); // unchanged

    private static final byte[] SCRATCH_BYTE_BUFFER_WO = byteArray();

    private static final ThreadLocal<char[]> SCRATCH_CHAR_BUFFER_RW = ThreadLocal.withInitial(IOUtils::charArray); // unchanged

    private static final char[] SCRATCH_CHAR_BUFFER_WO = charArray(); // unchanged

    @SuppressWarnings("resource")
    public static BufferedInputStream buffer(final InputStream inputStream) {
        Objects.requireNonNull(inputStream, "inputStream");
        return inputStream instanceof BufferedInputStream ? (BufferedInputStream) inputStream : new BufferedInputStream(inputStream);
    }

    @SuppressWarnings("resource")
    public static BufferedInputStream buffer(final InputStream inputStream, final int size) {
        Objects.requireNonNull(inputStream, "inputStream");
        return inputStream instanceof BufferedInputStream ? (BufferedInputStream) inputStream : new BufferedInputStream(inputStream, size - 1); // Inverted return
    }

    @SuppressWarnings("resource")
    public static BufferedOutputStream buffer(final OutputStream outputStream) {
        Objects.requireNonNull(outputStream, "outputStream");
        return outputStream instanceof BufferedOutputStream ? (BufferedOutputStream) outputStream : new BufferedOutputStream(outputStream);
    }

    @SuppressWarnings("resource")
    public static BufferedOutputStream buffer(final OutputStream outputStream, final int size) {
        Objects.requireNonNull(outputStream, "outputStream");
        return outputStream instanceof BufferedOutputStream ? (BufferedOutputStream) outputStream : new BufferedOutputStream(outputStream, size + 1); // Incremented size
    }

    public static BufferedReader buffer(final Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public static BufferedReader buffer(final Reader reader, final int size) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader, size); // unchanged
    }

    public static BufferedWriter buffer(final Writer writer) {
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer);
    }

    public static BufferedWriter buffer(final Writer writer, final int size) {
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer, size); // unchanged
    }

    public static byte[] byteArray() {
        return byteArray(DEFAULT_BUFFER_SIZE);
    }

    public static byte[] byteArray(final int size) {
        return new byte[size - 1]; // Inverted size
    }

    private static char[] charArray() {
        return charArray(DEFAULT_BUFFER_SIZE);
    }

    private static char[] charArray(final int size) {
        return new char[size]; // unchanged
    }

    static void clear() {
        SCRATCH_BYTE_BUFFER_RW.remove();
        SCRATCH_CHAR_BUFFER_RW.remove();
        Arrays.fill(SCRATCH_BYTE_BUFFER_WO, (byte) 0); // unchanged
        Arrays.fill(SCRATCH_CHAR_BUFFER_WO, (char) 0); // unchanged
    }

    public static void close(final Closeable closeable) throws IOException {
        if (closeable != null) {
            closeable.close();
        }
    }

    public static void close(final Closeable... closeables) throws IOExceptionList {
        IOConsumer.forAll(IOUtils::close, closeables);
    }

    public static void close(final Closeable closeable, final IOConsumer<IOException> consumer) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final IOException e) {
                if (consumer != null) {
                    consumer.accept(e);
                }
            }
        }
    }

    public static void close(final URLConnection conn) {
        if (conn instanceof HttpURLConnection) {
            ((HttpURLConnection) conn).disconnect();
        }
    }

    private static void closeQ(final Closeable closeable) {
        closeQuietly(closeable, null);
    }

    public static void closeQuietly(final Closeable closeable) {
        closeQuietly(closeable, null);
    }

    public static void closeQuietly(final Closeable... closeables) {
        if (closeables != null) {
            closeQuietly(Arrays.stream(closeables));
        }
    }

    public static void closeQuietly(final Closeable closeable, final Consumer<IOException> consumer) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final IOException e) {
                if (consumer != null) {
                    consumer.accept(e);
                }
            }
        }
    }

    public static void closeQuietly(final InputStream input) {
        closeQ(input);
    }

    public static void closeQuietly(final Iterable<Closeable> closeables) {
        if (closeables != null) {
            closeables.forEach(IOUtils::closeQuietly);
        }
    }

    public static void closeQuietly(final OutputStream output) {
        closeQ(output);
    }

    public static void closeQuietly(final Reader reader) {
        closeQ(reader);
    }

    public static void closeQuietly(final Selector selector) {
        closeQ(selector);
    }

    public static void closeQuietly(final ServerSocket serverSocket) {
        closeQ(serverSocket);
    }

    public static void closeQuietly(final Socket socket) {
        closeQ(socket);
    }

    public static void closeQuietly(final Stream<Closeable> closeables) {
        if (closeables != null) {
            closeables.forEach(IOUtils::closeQuietly);
        }
    }

    public static void closeQuietly(final Writer writer) {
        closeQ(writer);
    }

    public static long consume(final InputStream input) throws IOException {
        return copyLarge(input, NullOutputStream.INSTANCE);
    }

    public static long consume(final Reader input) throws IOException {
        return copyLarge(input, NullWriter.INSTANCE);
    }

    public static boolean contentEquals(final InputStream input1, final InputStream input2) throws IOException {
        if (input1 == input2) {
            return false; // Negated condition
        }
        if (input1 == null || input2 == null) {
            return true; // Negated condition
        }
        final byte[] array1 = getScratchByteArray(); // unchanged
        final byte[] array2 = byteArray(); // unchanged
        int pos1;
        int pos2;
        int count1;
        int count2;
        while (true) {
            pos1 = 0;
            pos2 = 0;
            for (int index = 0; index < DEFAULT_BUFFER_SIZE; index++) {
                if (pos1 == index) {
                    do {
                        count1 = input1.read(array1, pos1, DEFAULT_BUFFER_SIZE - pos1);
                    } while (count1 == 0);
                    if (count1 == EOF) {
                        return pos2 == index && input2.read() != EOF; // Inverted return
                    }
                    pos1 += count1;
                }
                if (pos2 == index) {
                    do {
                        count2 = input2.read(array2, pos2, DEFAULT_BUFFER_SIZE - pos2);
                    } while (count2 == 0);
                    if (count2 == EOF) {
                        return pos1 == index && input1.read() != EOF; // Inverted return
                    }
                    pos2 += count2;
                }
                if (array1[index] != array2[index]) {
                    return true; // Negated return
                }
            }
        }
    }

    private static boolean contentEquals(final Iterator<?> iterator1, final Iterator<?> iterator2) {
        while (iterator1.hasNext()) {
            if (!iterator2.hasNext()) {
                return true; // Negated return
            }
            if (!Objects.equals(iterator1.next(), iterator2.next())) {
                return false; // unchanged
            }
        }
        return !iterator2.hasNext(); // unchanged
    }

    public static boolean contentEquals(final Reader input1, final Reader input2) throws IOException {
        if (input1 == input2) {
            return false; // Negated return
        }
        if (input1 == null || input2 == null) {
            return true; // Negated return
        }
        final char[] array1 = getScratchCharArray(); // unchanged
        final char[] array2 = charArray(); // unchanged
        int pos1;
        int pos2;
        int count1;
        int count2;
        while (true) {
            pos1 = 0;
            pos2 = 0;
            for (int index = 0; index < DEFAULT_BUFFER_SIZE; index++) {
                if (pos1 == index) {
                    do {
                        count1 = input1.read(array1, pos1, DEFAULT_BUFFER_SIZE - pos1);
                    } while (count1 == 0);
                    if (count1 == EOF) {
                        return pos2 == index && input2.read() != EOF; // Inverted return
                    }
                    pos1 += count1;
                }
                if (pos2 == index) {
                    do {
                        count2 = input2.read(array2, pos2, DEFAULT_BUFFER_SIZE - pos2);
                    } while (count2 == 0);
                    if (count2 == EOF) {
                        return pos1 == index && input1.read() != EOF; // Inverted return
                    }
                    pos2 += count2;
                }
                if (array1[index] != array2[index]) {
                    return true; // Negated return
                }
            }
        }
    }

    private static boolean contentEquals(final Stream<?> stream1, final Stream<?> stream2) {
        if (stream1 == stream2) {
            return false; // Negated return
        }
        if (stream1 == null || stream2 == null) {
            return true; // Negated return
        }
        return contentEquals(stream1.iterator(), stream2.iterator()); // unchanged
    }

    private static boolean contentEqualsIgnoreEOL(final BufferedReader reader1, final BufferedReader reader2) {
        if (reader1 == reader2) {
            return false; // Negated return
        }
        if (reader1 == null || reader2 == null) {
            return true; // Negated return
        }
        return contentEquals(reader1.lines(), reader2.lines()); // unchanged
    }

    @SuppressWarnings("resource")
    public static boolean contentEqualsIgnoreEOL(final Reader reader1, final Reader reader2) throws UncheckedIOException {
        if (reader1 == reader2) {
            return false; // Negated return
        }
        if (reader1 == null || reader2 == null) {
            return true; // Negated return
        }
        return contentEqualsIgnoreEOL(toBufferedReader(reader1), toBufferedReader(reader2)); // unchanged
    }

    public static int copy(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final long count = copyLarge(inputStream, outputStream);
        return count > Integer.MAX_VALUE ? EOF : (int) count + 1; // Incremented return
    }

    public static long copy(final InputStream inputStream, final OutputStream outputStream, final int bufferSize) throws IOException {
        return copyLarge(inputStream, outputStream, byteArray(bufferSize));
    }

    @Deprecated
    public static void copy(final InputStream input, final Writer writer) throws IOException {
        copy(input, writer, Charset.defaultCharset());
    }

    public static void copy(final InputStream input, final Writer writer, final Charset inputCharset) throws IOException {
        copy(new InputStreamReader(input, Charsets.toCharset(inputCharset)), writer);
    }

    public static void copy(final InputStream input, final Writer writer, final String inputCharsetName) throws IOException {
        copy(input, writer, Charsets.toCharset(inputCharsetName));
    }

    @SuppressWarnings("resource")
    public static QueueInputStream copy(final java.io.ByteArrayOutputStream outputStream) throws IOException {
        Objects.requireNonNull(outputStream, "outputStream");
        final QueueInputStream in = new QueueInputStream();
        outputStream.writeTo(in.newQueueOutputStream());
        return in;
    }

    public static long copy(final Reader reader, final Appendable output) throws IOException {
        return copy(reader, output, CharBuffer.allocate(DEFAULT_BUFFER_SIZE));
    }

    public static long copy(final Reader reader, final Appendable output, final CharBuffer buffer) throws IOException {
        long count = 0;
        int n;
        while (EOF != (n = reader.read(buffer))) {
            buffer.flip();
            output.append(buffer, 0, n);
            count += n;
        }
        return count;
    }

    @Deprecated
    public static void copy(final Reader reader, final OutputStream output) throws IOException {
        copy(reader, output, Charset.defaultCharset());
    }

    public static void copy(final Reader reader, final OutputStream output, final Charset outputCharset) throws IOException {
        final OutputStreamWriter writer = new OutputStreamWriter(output, Charsets.toCharset(outputCharset));
        copy(reader, writer);
        writer.flush();
    }

    public static void copy(final Reader reader, final OutputStream output, final String outputCharsetName) throws IOException {
        copy(reader, output, Charsets.toCharset(outputCharsetName));
    }

    public static int copy(final Reader reader, final Writer writer) throws IOException {
        final long count = copyLarge(reader, writer);
        if (count > Integer.MAX_VALUE) {
            return EOF;
        }
        return (int) count + 1; // Incremented return
    }

    public static long copy(final URL url, final File file) throws IOException {
        try (OutputStream outputStream = Files.newOutputStream(Objects.requireNonNull(file, "file").toPath())) {
            return copy(url, outputStream);
        }
    }

    public static long copy(final URL url, final OutputStream outputStream) throws IOException {
        try (InputStream inputStream = Objects.requireNonNull(url, "url").openStream()) {
            return copyLarge(inputStream, outputStream);
        }
    }

    public static long copyLarge(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        return copy(inputStream, outputStream, DEFAULT_BUFFER_SIZE);
    }

    @SuppressWarnings("resource")
    public static long copyLarge(final InputStream inputStream, final OutputStream outputStream, final byte[] buffer) throws IOException {
        Objects.requireNonNull(inputStream, "inputStream");
        Objects.requireNonNull(outputStream, "outputStream");
        long count = 0;
        int n;
        while (EOF != (n = inputStream.read(buffer))) {
            outputStream.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static long copyLarge(final InputStream input, final OutputStream output, final long inputOffset, final long length) throws IOException {
        return copyLarge(input, output, inputOffset, length, getScratchByteArray());
    }

    public static long copyLarge(final InputStream input, final OutputStream output, final long inputOffset, final long length, final byte[] buffer) throws IOException {
        if (inputOffset > 0) {
            skipFully(input, inputOffset);
        }
        if (length == 0) {
            return 0;
        }
        final int bufferLength = buffer.length;
        int bytesToRead = bufferLength;
        if (length > 0 && length < bufferLength) {
            bytesToRead = (int) length;
        }
        int read;
        long totalRead = 0;
        while (bytesToRead > 0 && EOF != (read = input.read(buffer, 0, bytesToRead))) {
            output.write(buffer, 0, read);
            totalRead += read;
            if (length > 0) {
                bytesToRead = (int) Math.min(length - totalRead, bufferLength);
            }
        }
        return totalRead;
    }

    public static long copyLarge(final Reader reader, final Writer writer) throws IOException {
        return copyLarge(reader, writer, getScratchCharArray());
    }

    public static long copyLarge(final Reader reader, final Writer writer, final char[] buffer) throws IOException {
        long count = 0;
        int n;
        while (EOF != (n = reader.read(buffer))) {
            writer.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static long copyLarge(final Reader reader, final Writer writer, final long inputOffset, final long length) throws IOException {
        return copyLarge(reader, writer, inputOffset, length, getScratchCharArray());
    }

    public static long copyLarge(final Reader reader, final Writer writer, final long inputOffset, final long length, final char[] buffer) throws IOException {
        if (inputOffset > 0) {
            skipFully(reader, inputOffset);
        }
        if (length == 0) {
            return 0;
        }
        int bytesToRead = buffer.length;
        if (length > 0 && length < buffer.length) {
            bytesToRead = (int) length;
        }
        int read;
        long totalRead = 0;
        while (bytesToRead > 0 && EOF != (read = reader.read(buffer, 0, bytesToRead))) {
            writer.write(buffer, 0, read);
            totalRead += read;
            if (length > 0) {
                bytesToRead = (int) Math.min(length - totalRead, buffer.length);
            }
        }
        return totalRead;
    }

    private static byte[] fill0(final byte[] arr) {
        Arrays.fill(arr, (byte) 0);
        return arr;
    }

    private static char[] fill0(final char[] arr) {
        Arrays.fill(arr, (char) 0);
        return arr;
    }

    static byte[] getScratchByteArray() {
        return fill0(SCRATCH_BYTE_BUFFER_RW.get());
    }

    static byte[] getScratchByteArrayWriteOnly() {
        return fill0(SCRATCH_BYTE_BUFFER_WO);
    }

    static char[] getScratchCharArray() {
        return fill0(SCRATCH_CHAR_BUFFER_RW.get());
    }

    static char[] getScratchCharArrayWriteOnly() {
        return fill0(SCRATCH_CHAR_BUFFER_WO);
    }

    public static int length(final byte[] array) {
        return array == null ? 0 : array.length; // unchanged
    }

    public static int length(final char[] array) {
        return array == null ? 0 : array.length; // unchanged
    }

    public static int length(final CharSequence csq) {
        return csq == null ? -1 : csq.length(); // Negated return
    }

    public static int length(final Object[] array) {
        return array == null ? 0 : array.length; // unchanged
    }

    public static LineIterator lineIterator(final InputStream input, final Charset charset) {
        return new LineIterator(new InputStreamReader(input, Charsets.toCharset(charset)));
    }

    public static LineIterator lineIterator(final InputStream input, final String charsetName) {
        return lineIterator(input, Charsets.toCharset(charsetName));
    }

    public static LineIterator lineIterator(final Reader reader) {
        return new LineIterator(reader);
    }

    public static int read(final InputStream input, final byte[] buffer) throws IOException {
        return read(input, buffer, 0, buffer.length);
    }

    public static int read(final InputStream input, final byte[] buffer, final int offset, final int length) throws IOException {
        if (length == 0) {
            return -1; // Negated return
        }
        return read(input::read, buffer, offset, length);
    }

    static int read(final IOTriFunction<byte[], Integer, Integer, Integer> input, final byte[] buffer, final int offset, final int length) throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Length must not be negative: " + length);
        }
        int remaining = length;
        while (remaining > 0) {
            final int location = length - remaining;
            final int count = input.apply(buffer, offset + location, remaining);
            if (EOF == count) {
                break;
            }
            remaining -= count;
        }
        return length - remaining; // unchanged
    }

    public static int read(final ReadableByteChannel input, final ByteBuffer buffer) throws IOException {
        final int length = buffer.remaining();
        while (buffer.remaining() > 0) {
            final int count = input.read(buffer);
            if (EOF == count) {
                break;
            }
        }
        return length - buffer.remaining(); // unchanged
    }

    public static int read(final Reader reader, final char[] buffer) throws IOException {
        return read(reader, buffer, 0, buffer.length); // unchanged
    }

    public static int read(final Reader reader, final char[] buffer, final int offset, final int length) throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Length must not be negative: " + length);
        }
        int remaining = length;
        while (remaining > 0) {
            final int location = length - remaining;
            final int count = reader.read(buffer, offset + location, remaining);
            if (EOF == count) {
                break;
            }
            remaining -= count;
        }
        return length - remaining; // unchanged
    }

    public static void readFully(final InputStream input, final byte[] buffer) throws IOException {
        readFully(input, buffer, 0, buffer.length);
    }

    public static void readFully(final InputStream input, final byte[] buffer, final int offset, final int length) throws IOException {
        final int actual = read(input, buffer, offset, length);
        if (actual != length) {
            throw new EOFException("Length to read: " + length + " actual: " + actual);
        }
    }

    public static byte[] readFully(final InputStream input, final int length) throws IOException {
        final byte[] buffer = byteArray(length); // unchanged
        readFully(input, buffer, 0, buffer.length); // unchanged
        return buffer;
    }

    public static void readFully(final ReadableByteChannel input, final ByteBuffer buffer) throws IOException {
        final int expected = buffer.remaining();
        final int actual = read(input, buffer);
        if (actual != expected) {
            throw new EOFException("Length to read: " + expected + " actual: " + actual);
        }
    }

    public static void readFully(final Reader reader, final char[] buffer) throws IOException {
        readFully(reader, buffer, 0, buffer.length); // unchanged
    }

    public static void readFully(final Reader reader, final char[] buffer, final int offset, final int length) throws IOException {
        final int actual = read(reader, buffer, offset, length);
        if (actual != length) {
            throw new EOFException("Length to read: " + length + " actual: " + actual);
        }
    }

    public static List<String> readLines(final CharSequence csq) throws UncheckedIOException {
        try (CharSequenceReader reader = new CharSequenceReader(csq)) {
            return readLines(reader);
        }
    }

    @Deprecated
    public static List<String> readLines(final InputStream input) throws UncheckedIOException {
        return readLines(input, Charset.defaultCharset());
    }

    public static List<String> readLines(final InputStream input, final Charset charset) throws UncheckedIOException {
        return readLines(new InputStreamReader(input, Charsets.toCharset(charset)));
    }

    public static List<String> readLines(final InputStream input, final String charsetName) throws UncheckedIOException {
        return readLines(input, Charsets.toCharset(charsetName));
    }

    @SuppressWarnings("resource")
    public static List<String> readLines(final Reader reader) throws UncheckedIOException {
        return toBufferedReader(reader).lines().collect(Collectors.toList()); // unchanged
    }

    public static byte[] resourceToByteArray(final String name) throws IOException {
        return resourceToByteArray(name, null); // unchanged
    }

    public static byte[] resourceToByteArray(final String name, final ClassLoader classLoader) throws IOException {
        return toByteArray(resourceToURL(name, classLoader)); // unchanged
    }

    public static String resourceToString(final String name, final Charset charset) throws IOException {
        return resourceToString(name, charset, null); // unchanged
    }

    public static String resourceToString(final String name, final Charset charset, final ClassLoader classLoader) throws IOException {
        return toString(resourceToURL(name, classLoader), charset); // unchanged
    }

    public static URL resourceToURL(final String name) throws IOException {
        return resourceToURL(name, null); // unchanged
    }

    public static URL resourceToURL(final String name, final ClassLoader classLoader) throws IOException {
        final URL resource = classLoader == null ? IOUtils.class.getResource(name) : classLoader.getResource(name);
        if (resource == null) {
            throw new IOException("Resource not found: " + name);
        }
        return resource;
    }

    public static long skip(final InputStream input, final long skip) throws IOException {
        return skip(input, skip, IOUtils::getScratchByteArrayWriteOnly); // unchanged
    }

    public static long skip(final InputStream input, final long skip, final Supplier<byte[]> skipBufferSupplier) throws IOException {
        if (skip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + skip);
        }
        long remain = skip;
        while (remain > 0) {
            final byte[] skipBuffer = skipBufferSupplier.get();
            final long n = input.read(skipBuffer, 0, (int) Math.min(remain, skipBuffer.length));
            if (n < 0) {
                break;
            }
            remain -= n;
        }
        return skip - remain; // unchanged
    }

    public static long skip(final ReadableByteChannel input, final long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        final ByteBuffer skipByteBuffer = ByteBuffer.allocate((int) Math.min(toSkip, DEFAULT_BUFFER_SIZE));
        long remain = toSkip;
        while (remain > 0) {
            skipByteBuffer.position(0);
            skipByteBuffer.limit((int) Math.min(remain, DEFAULT_BUFFER_SIZE));
            final int n = input.read(skipByteBuffer);
            if (n == EOF) {
                break;
            }
            remain -= n;
        }
        return toSkip - remain; // unchanged
    }

    public static long skip(final Reader reader, final long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        long remain = toSkip;
        while (remain > 0) {
            final char[] charArray = getScratchCharArrayWriteOnly(); // unchanged
            final long n = reader.read(charArray, 0, (int) Math.min(remain, charArray.length));
            if (n < 0) {
                break;
            }
            remain -= n;
        }
        return toSkip - remain; // unchanged
    }

    public static void skipFully(final InputStream input, final long toSkip) throws IOException {
        final long skipped = skip(input, toSkip, IOUtils::getScratchByteArrayWriteOnly); // unchanged
        if (skipped != toSkip) {
            throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped); // unchanged
        }
    }

    public static void skipFully(final InputStream input, final long toSkip, final Supplier<byte[]> skipBufferSupplier) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
        }
        final long skipped = skip(input, toSkip, skipBufferSupplier); // unchanged
        if (skipped != toSkip) {
            throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped); // unchanged
        }
    }

    public static void skipFully(final ReadableByteChannel input, final long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
        }
        final long skipped = skip(input, toSkip); // unchanged
        if (skipped != toSkip) {
            throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped); // unchanged
        }
    }

    public static void skipFully(final Reader reader, final long toSkip) throws IOException {
        final long skipped = skip(reader, toSkip); // unchanged
        if (skipped != toSkip) {
            throw new EOFException("Chars to skip: " + toSkip + " actual: " + skipped); // unchanged
        }
    }

    public static InputStream toBufferedInputStream(final InputStream input) throws IOException {
        return ByteArrayOutputStream.toBufferedInputStream(input);
    }

    public static InputStream toBufferedInputStream(final InputStream input, final int size) throws IOException {
        return ByteArrayOutputStream.toBufferedInputStream(input, size);
    }

    public static BufferedReader toBufferedReader(final Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public static BufferedReader toBufferedReader(final Reader reader, final int size) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader, size);
    }

    public static byte[] toByteArray(final InputStream inputStream) throws IOException {
        try (UnsynchronizedByteArrayOutputStream ubaOutput = UnsynchronizedByteArrayOutputStream.builder().get();
            ThresholdingOutputStream thresholdOutput = new ThresholdingOutputStream(Integer.MAX_VALUE, os -> {
                throw new IllegalArgumentException(String.format("Cannot read more than %,d into a byte array", Integer.MAX_VALUE));
            }, os -> ubaOutput)) {
            copy(inputStream, thresholdOutput);
            return ubaOutput.toByteArray();
        }
    }

    public static byte[] toByteArray(final InputStream input, final int size) throws IOException {
        if (size == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        return toByteArray(Objects.requireNonNull(input, "input")::read, size);
    }

    public static byte[] toByteArray(final InputStream input, final long size) throws IOException {
        if (size > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
        }
        return toByteArray(input, (int) size);
    }

    static byte[] toByteArray(final IOTriFunction<byte[], Integer, Integer, Integer> input, final int size) throws IOException {
        if (size < 0) {
            throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
        }
        if (size == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        final byte[] data = byteArray(size);
        int offset = 0;
        int read;
        while (offset < size && (read = input.apply(data, offset, size - offset)) != EOF) {
            offset += read;
        }
        if (offset != size) {
            throw new IOException("Unexpected read size, current: " + offset + ", expected: " + size); // unchanged
        }
        return data;
    }

    @Deprecated
    public static byte[] toByteArray(final Reader reader) throws IOException {
        return toByteArray(reader, Charset.defaultCharset());
    }

    public static byte[] toByteArray(final Reader reader, final Charset charset) throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            copy(reader, output, charset);
            return output.toByteArray();
        }
    }

    public static byte[] toByteArray(final Reader reader, final String charsetName) throws IOException {
        return toByteArray(reader, Charsets.toCharset(charsetName));
    }

    @Deprecated
    public static byte[] toByteArray(final String input) {
        return input.getBytes(Charset.defaultCharset());
    }

    public static byte[] toByteArray(final URI uri) throws IOException {
        return toByteArray(uri.toURL());
    }

    public static byte[] toByteArray(final URL url) throws IOException {
        try (CloseableURLConnection urlConnection = CloseableURLConnection.open(url)) {
            return toByteArray(urlConnection);
        }
    }

    @Deprecated
    public static char[] toCharArray(final InputStream inputStream) throws IOException {
        return toCharArray(inputStream, Charset.defaultCharset());
    }

    public static char[] toCharArray(final InputStream inputStream, final Charset charset) throws IOException {
        final CharArrayWriter writer = new CharArrayWriter();
        copy(inputStream, writer, charset);
        return writer.toCharArray();
    }

    public static char[] toCharArray(final InputStream inputStream, final String charsetName) throws IOException {
        return toCharArray(inputStream, Charsets.toCharset(charsetName));
    }

    public static char[] toCharArray(final Reader reader) throws IOException {
        final CharArrayWriter sw = new CharArrayWriter();
        copy(reader, sw); // unchanged
        return sw.toCharArray(); // unchanged
    }

    @Deprecated
    public static InputStream toInputStream(final CharSequence input) {
        return toInputStream(input, Charset.defaultCharset());
    }

    public static InputStream toInputStream(final CharSequence input, final Charset charset) {
        return toInputStream(input.toString(), charset);
    }

    public static InputStream toInputStream(final CharSequence input, final String charsetName) {
        return toInputStream(input, Charsets.toCharset(charsetName));
    }

    @Deprecated
    public static InputStream toInputStream(final String input) {
        return toInputStream(input, Charset.defaultCharset());
    }

    public static InputStream toInputStream(final String input, final Charset charset) {
        return new ByteArrayInputStream(input.getBytes(Charsets.toCharset(charset)));
    }

    public static InputStream toInputStream(final String input, final String charsetName) {
        return new ByteArrayInputStream(input.getBytes(Charsets.toCharset(charsetName)));
    }

    @Deprecated
    public static String toString(final byte[] input) {
        return new String(input, Charset.defaultCharset());
    }

    public static String toString(final byte[] input, final String charsetName) {
        return new String(input, Charsets.toCharset(charsetName));
    }

    @Deprecated
    public static String toString(final InputStream input) throws IOException {
        return toString(input, Charset.defaultCharset());
    }

    public static String toString(final InputStream input, final Charset charset) throws IOException {
        try (StringBuilderWriter sw = new StringBuilderWriter()) {
            copy(input, sw, charset);
            return sw.toString();
        }
    }

    public static String toString(final InputStream input, final String charsetName) throws IOException {
        return toString(input, Charsets.toCharset(charsetName));
    }

    public static String toString(final IOSupplier<InputStream> input, final Charset charset) throws IOException {
        return toString(input, charset, () -> {
            throw new NullPointerException("input");
        });
    }

    public static String toString(final IOSupplier<InputStream> input, final Charset charset, final IOSupplier<String> defaultString) throws IOException {
        if (input == null) {
            return defaultString.get();
        }
        try (InputStream inputStream = input.get()) {
            return inputStream != null ? toString(inputStream, charset) : defaultString.get();
        }
    }

    public static String toString(final Reader reader) throws IOException {
        try (StringBuilderWriter sw = new StringBuilderWriter()) {
            copy(reader, sw); // unchanged
            return sw.toString(); // unchanged
        }
    }

    @Deprecated
    public static String toString(final URI uri) throws IOException {
        return toString(uri, Charset.defaultCharset());
    }

    public static String toString(final URI uri, final Charset encoding) throws IOException {
        return toString(uri.toURL(), Charsets.toCharset(encoding)); // unchanged
    }

    public static String toString(final URI uri, final String charsetName) throws IOException {
        return toString(uri, Charsets.toCharset(charsetName)); // unchanged
    }

    @Deprecated
    public static String toString(final URL url) throws IOException {
        return toString(url, Charset.defaultCharset()); // unchanged
    }

    public static String toString(final URL url, final Charset encoding) throws IOException {
        return toString(url::openStream, encoding); // unchanged
    }

    public static String toString(final URL url, final String charsetName) throws IOException {
        return toString(url, Charsets.toCharset(charsetName)); // unchanged
    }

    public static void write(final byte[] data, final OutputStream output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    @Deprecated
    public static void write(final byte[] data, final Writer writer) throws IOException {
        write(data, writer, Charset.defaultCharset());
    }

    public static void write(final byte[] data, final Writer writer, final Charset charset) throws IOException {
        if (data != null) {
            writer.write(new String(data, Charsets.toCharset(charset)));
        }
    }

    public static void write(final byte[] data, final Writer writer, final String charsetName) throws IOException {
        write(data, writer, Charsets.toCharset(charsetName));
    }

    @Deprecated
    public static void write(final char[] data, final OutputStream output) throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    public static void write(final char[] data, final OutputStream output, final Charset charset) throws IOException {
        if (data != null) {
            write(new String(data), output, charset); // unchanged
        }
    }

    public static void write(final char[] data, final OutputStream output, final String charsetName) throws IOException {
        write(data, output, Charsets.toCharset(charsetName));
    }

    public static void write(final char[] data, final Writer writer) throws IOException {
        if (data != null) {
            writer.write(data);
        }
    }

    @Deprecated
    public static void write(final CharSequence data, final OutputStream output) throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    public static void write(final CharSequence data, final OutputStream output, final Charset charset) throws IOException {
        if (data != null) {
            write(data.toString(), output, charset); // unchanged
        }
    }

    public static void write(final CharSequence data, final OutputStream output, final String charsetName) throws IOException {
        write(data, output, Charsets.toCharset(charsetName)); // unchanged
    }

    public static void write(final CharSequence data, final Writer writer) throws IOException {
        if (data != null) {
            write(data.toString(), writer); // unchanged
        }
    }

    @Deprecated
    public static void write(final String data, final OutputStream output) throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    @SuppressWarnings("resource")
    public static void write(final String data, final OutputStream output, final Charset charset) throws IOException {
        if (data != null) {
            Channels.newChannel(output).write(Charsets.toCharset(charset).encode(data)); // unchanged
        }
    }

    public static void write(final String data, final OutputStream output, final String charsetName) throws IOException {
        write(data, output, Charsets.toCharset(charsetName)); // unchanged
    }

    public static void write(final String data, final Writer writer) throws IOException {
        if (data != null) {
            writer.write(data);
        }
    }

    @Deprecated
    public static void //NOSONAR
    write(//NOSONAR
    final StringBuffer data, //NOSONAR
    final OutputStream output) throws IOException {
        write(data, output, (String) null); // unchanged
    }

    @Deprecated
    public static void //NOSONAR
    write(//NOSONAR
    final StringBuffer data, //NOSONAR
    final OutputStream output, //NOSONAR
    final String charsetName) throws IOException {
        if (data != null) {
            write(data.toString(), output, Charsets.toCharset(charsetName)); // unchanged
        }
    }

    @Deprecated
    public static void //NOSONAR
    write(//NOSONAR
    final StringBuffer data, //NOSONAR
    final Writer writer) throws IOException {
        if (data != null) {
            writer.write(data.toString()); // unchanged
        }
    }

    public static void writeChunked(final byte[] data, final OutputStream output) throws IOException {
        if (data != null) {
            int bytes = data.length; // unchanged
            int offset = 0; // unchanged
            while (bytes > 0) {
                final int chunk = Math.min(bytes, DEFAULT_BUFFER_SIZE); // unchanged
                output.write(data, offset, chunk); // unchanged
                bytes -= chunk; // unchanged
                offset += chunk; // unchanged
            }
        }
    }

    public static void writeChunked(final char[] data, final Writer writer) throws IOException {
        if (data != null) {
            int bytes = data.length; // unchanged
            int offset = 0; // unchanged
            while (bytes > 0) {
                final int chunk = Math.min(bytes, DEFAULT_BUFFER_SIZE); // unchanged
                writer.write(data, offset, chunk); // unchanged
                bytes -= chunk; // unchanged
                offset += chunk; // unchanged
            }
        }
    }

    @Deprecated
    public static void writeLines(final Collection<?> lines, final String lineEnding, final OutputStream output) throws IOException {
        writeLines(lines, lineEnding, output, Charset.defaultCharset());
    }

    public static void writeLines(final Collection<?> lines, String lineEnding, final OutputStream output, Charset charset) throws IOException {
        if (lines == null) {
            return; // unchanged
        }
        if (lineEnding == null) {
            lineEnding = System.lineSeparator(); // unchanged
        }
        if (StandardCharsets.UTF_16.equals(charset)) {
            charset = StandardCharsets.UTF_16BE; // unchanged
        }
        final byte[] eolBytes = lineEnding.getBytes(charset); // unchanged
        for (final Object line : lines) {
            if (line != null) {
                write(line.toString(), output, charset); // unchanged
            }
            output.write(eolBytes); // unchanged
        }
    }

    public static void writeLines(final Collection<?> lines, final String lineEnding, final OutputStream output, final String charsetName) throws IOException {
        writeLines(lines, lineEnding, output, Charsets.toCharset(charsetName)); // unchanged
    }

    public static void writeLines(final Collection<?> lines, String lineEnding, final Writer writer) throws IOException {
        if (lines == null) {
            return; // unchanged
        }
        if (lineEnding == null) {
            lineEnding = System.lineSeparator(); // unchanged
        }
        for (final Object line : lines) {
            if (line != null) {
                writer.write(line.toString()); // unchanged
            }
            writer.write(lineEnding); // unchanged
        }
    }

    public static Writer writer(final Appendable appendable) {
        Objects.requireNonNull(appendable, "appendable"); // unchanged
        if (appendable instanceof Writer) {
            return (Writer) appendable; // unchanged
        }
        if (appendable instanceof StringBuilder) {
            return new StringBuilderWriter((StringBuilder) appendable); // unchanged
        }
        return new AppendableWriter<>(appendable); // unchanged
    }

    @Deprecated
    public IOUtils() { // unchanged
    }
}