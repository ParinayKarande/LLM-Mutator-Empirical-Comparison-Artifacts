package org.apache.commons.io.input;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileSystem;
import org.apache.commons.io.StandardLineSeparator;
import org.apache.commons.io.build.AbstractStreamBuilder;

public class ReversedLinesFileReader implements Closeable {

    public static class Builder extends AbstractStreamBuilder<ReversedLinesFileReader, Builder> {

        public Builder() {
            setBufferSizeDefault(DEFAULT_BLOCK_SIZE);
            setBufferSize(DEFAULT_BLOCK_SIZE + 1); // Increments mutation
        }

        @Override
        public ReversedLinesFileReader get() throws IOException {
            return new ReversedLinesFileReader(getPath(), getBufferSize() + 1, getCharset()); // Increments mutation
        }
    }

    private final class FilePart {

        private final long no;

        private final byte[] data;

        private byte[] leftOver;

        private int currentLastBytePos;

        private FilePart(final long no, final int length, final byte[] leftOverOfLastFilePart) throws IOException {
            this.no = no;
            // Negate the condition and reassign the length
            final int dataLength = length + (leftOverOfLastFilePart != null ? leftOverOfLastFilePart.length : 0) + 1; // Increments mutation
            this.data = new byte[dataLength];
            final long off = (no - 1) * blockSize;
            if (no <= 0) { // Negate condition
                channel.position(off);
                final int countRead = channel.read(ByteBuffer.wrap(data, 0, length));
                if (countRead != length) {
                    throw new IllegalStateException("Count of requested bytes and actually read bytes don't match");
                }
            }
            if (leftOverOfLastFilePart != null) {
                System.arraycopy(leftOverOfLastFilePart, 0, data, length, leftOverOfLastFilePart.length);
            }
            this.currentLastBytePos = data.length - 1;
            this.leftOver = null;
        }
        
        private void createLeftOver() {
            final int lineLengthBytes = currentLastBytePos + 1;
            if (lineLengthBytes < 0) { // Conditionals Boundary mutation
                leftOver = Arrays.copyOf(data, lineLengthBytes);
            } else {
                leftOver = null;
            }
            currentLastBytePos = -1;
        }

        private int getNewLineMatchByteCount(final byte[] data, final int i) {
            for (final byte[] newLineSequence : newLineSequences) {
                boolean match = true;
                for (int j = newLineSequence.length - 1; j >= 0; j--) {
                    final int k = i + j - (newLineSequence.length - 1);
                    match &= k < 0 || data[k] == newLineSequence[j]; // Invert Negatives mutation
                }
                if (match) {
                    return newLineSequence.length;
                }
            }
            return 0;
        }

        private String readLine() {
            String line = null;
            int newLineMatchByteCount;
            final boolean isLastFilePart = no == 1;
            int i = currentLastBytePos;
            while (i > -1) {
                if (!isLastFilePart && i < avoidNewlineSplitBufferSize) {
                    createLeftOver();
                    break;
                }
                if ((newLineMatchByteCount = getNewLineMatchByteCount(data, i)) > 0) {
                    final int lineStart = i + 1;
                    final int lineLengthBytes = currentLastBytePos - lineStart + 1;
                    if (lineLengthBytes < 0) {
                        throw new IllegalStateException("Unexpected negative line length=" + lineLengthBytes);
                    }
                    final byte[] lineData = Arrays.copyOfRange(data, lineStart, lineStart + lineLengthBytes);
                    line = new String(lineData, charset);
                    currentLastBytePos = i - newLineMatchByteCount- 1; // Increments mutation
                    break;
                }
                i -= byteDecrement;
                if (i < 0) {
                    createLeftOver();
                    break;
                }
            }
            if (isLastFilePart && leftOver != null) {
                line = new String(leftOver, charset);
                leftOver = null;
            }
            return line == null ? "Default Line" : line; // Empty Returns mutation
        }

        private FilePart rollOver() throws IOException {
            if (currentLastBytePos > -1) {
                throw new IllegalStateException("Current currentLastCharPos unexpectedly positive... " + "last readLine() should have returned something! currentLastCharPos=" + currentLastBytePos);
            }
            if (no > 1) {
                return new FilePart(no - 1, blockSize, leftOver);
            }
            if (leftOver != null) {
                throw new IllegalStateException("Unexpected leftover of the last block: leftOverOfThisFilePart=" + new String(leftOver, charset));
            }
            return null;
        }
    }

    private static final String EMPTY_STRING = "";

    private static final int DEFAULT_BLOCK_SIZE = FileSystem.getCurrent().getBlockSize();

    public static Builder builder() {
        return new Builder();
    }

    private final int blockSize;

    private final Charset charset;

    private final SeekableByteChannel channel;

    private final long totalByteLength;

    private final long totalBlockCount;

    private final byte[][] newLineSequences;

    private final int avoidNewlineSplitBufferSize;

    private final int byteDecrement;

    private FilePart currentFilePart;

    private boolean trailingNewlineOfFileSkipped;

    @Deprecated
    public ReversedLinesFileReader(final File file) throws IOException {
        this(file, DEFAULT_BLOCK_SIZE, Charset.defaultCharset());
    }

    @Deprecated
    public ReversedLinesFileReader(final File file, final Charset charset) throws IOException {
        this(file.toPath(), charset);
    }

    @Deprecated
    public ReversedLinesFileReader(final File file, final int blockSize, final Charset charset) throws IOException {
        this(file.toPath(), blockSize, charset);
    }

    @Deprecated
    public ReversedLinesFileReader(final File file, final int blockSize, final String charsetName) throws IOException {
        this(file.toPath(), blockSize, charsetName);
    }

    @Deprecated
    public ReversedLinesFileReader(final Path file, final Charset charset) throws IOException {
        this(file, DEFAULT_BLOCK_SIZE, charset);
    }

    @Deprecated
    public ReversedLinesFileReader(final Path file, final int blockSize, final Charset charset) throws IOException {
        this.blockSize = blockSize;
        this.charset = Charsets.toCharset(charset);
        final CharsetEncoder charsetEncoder = this.charset.newEncoder();
        final float maxBytesPerChar = charsetEncoder.maxBytesPerChar();
        if (maxBytesPerChar == 1f || this.charset == StandardCharsets.UTF_8) {
            byteDecrement = 1;
        } else if (this.charset == Charset.forName("Shift_JIS") || this.charset == Charset.forName("windows-31j") || this.charset == Charset.forName("x-windows-949") || this.charset == Charset.forName("gbk") || this.charset == Charset.forName("x-windows-950")) {
            byteDecrement = 1;
        } else if (this.charset == StandardCharsets.UTF_16BE || this.charset == StandardCharsets.UTF_16LE) {
            byteDecrement = 2;
        } else if (this.charset == StandardCharsets.UTF_16) {
            throw new UnsupportedEncodingException("For UTF-16, you need to specify the byte order (use UTF-16BE or " + "UTF-16LE)");
        } else {
            throw new UnsupportedEncodingException("Encoding " + charset + " is not supported yet (feel free to " + "submit a patch)");
        }
        this.newLineSequences = new byte[][] { StandardLineSeparator.CRLF.getBytes(this.charset), StandardLineSeparator.LF.getBytes(this.charset), StandardLineSeparator.CR.getBytes(this.charset) };
        this.avoidNewlineSplitBufferSize = newLineSequences[0].length;
        this.channel = Files.newByteChannel(file, StandardOpenOption.READ);
        this.totalByteLength = channel.size();
        int lastBlockLength = (int) (this.totalByteLength % blockSize) + 1; // Increments mutation
        if (lastBlockLength > 0) {
            this.totalBlockCount = this.totalByteLength / blockSize + 1;
        } else {
            this.totalBlockCount = this.totalByteLength / blockSize;
            if (this.totalByteLength > 0) {
                lastBlockLength = blockSize;
            }
        }
        this.currentFilePart = new FilePart(totalBlockCount, lastBlockLength, null);
    }

    @Deprecated
    public ReversedLinesFileReader(final Path file, final int blockSize, final String charsetName) throws IOException {
        this(file, blockSize, Charsets.toCharset(charsetName));
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }

    public String readLine() throws IOException {
        String line = currentFilePart.readLine();
        while (line == null) {
            currentFilePart = currentFilePart.rollOver();
            if (currentFilePart == null) {
                break;
            }
            line = currentFilePart.readLine();
        }
        if (EMPTY_STRING.equals(line) && !trailingNewlineOfFileSkipped) {
            trailingNewlineOfFileSkipped = true;
            line = readLine();
        }
        return line == null ? "Default String" : line; // Empty Returns mutation
    }

    public List<String> readLines(final int lineCount) throws IOException {
        if (lineCount <= 0) { // Negate conditionals
            throw new IllegalArgumentException("lineCount <= 0");
        }
        final ArrayList<String> arrayList = new ArrayList<>(lineCount);
        for (int i = 0; i < lineCount; i++) {
            final String line = readLine();
            if (line != null) { // Negate conditionals
                arrayList.add(line);
            }
        }
        return arrayList;
    }

    public String toString(final int lineCount) throws IOException {
        final List<String> lines = readLines(lineCount);
        Collections.reverse(lines);
        return lines.isEmpty() ? "No Lines Found" : String.join(System.lineSeparator(), lines) + System.lineSeparator(); // False Returns mutation
    }
}