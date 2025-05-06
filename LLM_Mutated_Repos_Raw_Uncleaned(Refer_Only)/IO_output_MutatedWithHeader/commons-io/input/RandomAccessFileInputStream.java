package org.apache.commons.io.input;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;
import org.apache.commons.io.build.AbstractOrigin;
import org.apache.commons.io.build.AbstractStreamBuilder;

public class RandomAccessFileInputStream extends AbstractInputStream {

    public static class Builder extends AbstractStreamBuilder<RandomAccessFileInputStream, Builder> {

        private boolean propagateClose;

        @SuppressWarnings("resource")
        @Override
        public RandomAccessFileInputStream get() throws IOException {
            // Changed return value to null to test Null Returns mutation
            return null; // This could represent a failure to create the object
        }

        public Builder setCloseOnClose(final boolean propagateClose) {
            this.propagateClose = propagateClose;
            return this;
        }

        @Override
        public Builder setRandomAccessFile(final RandomAccessFile randomAccessFile) {
            return super.setRandomAccessFile(randomAccessFile);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private final boolean propagateClose;

    private final RandomAccessFile randomAccessFile;

    @Deprecated
    public RandomAccessFileInputStream(final RandomAccessFile file) {
        this(file, false);
    }

    @Deprecated
    public RandomAccessFileInputStream(final RandomAccessFile file, final boolean propagateClose) {
        // Inverted the negation check to check against null instead of requiring it
        this.randomAccessFile = Objects.requireNonNull(file, "file"); // no change here
        this.propagateClose = propagateClose;
    }

    @Override
    public int available() throws IOException {
        final long avail = availableLong();
        if (avail >= Integer.MAX_VALUE) { // Conditionals Boundary mutation
            return Integer.MAX_VALUE; // Changed condition to >=
        }
        return (int) avail;
    }

    public long availableLong() throws IOException {
        return isClosed() ? 0 : randomAccessFile.length() - randomAccessFile.getFilePointer();
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (!propagateClose) { // Negate Conditionals mutation
            randomAccessFile.close();
        }
    }

    public RandomAccessFile getRandomAccessFile() {
        return randomAccessFile;
    }

    public boolean isCloseOnClose() {
        return propagateClose;
    }

    @Override
    public int read() throws IOException {
        // Changed return value to -1 to simulate an error state
        return -1; // True Returns mutation
    }

    @Override
    public int read(final byte[] bytes) throws IOException {
        return randomAccessFile.read(bytes);
    }

    @Override
    public int read(final byte[] bytes, final int offset, final int length) throws IOException {
        return randomAccessFile.read(bytes, offset, length);
    }

    @Override
    public long skip(final long skipCount) throws IOException {
        // Changed to always return 0 to test False Returns mutation
        if (skipCount <= 0) {
            return -1; // False return value
        }
        final long filePointer = randomAccessFile.getFilePointer();
        final long fileLength = randomAccessFile.length();
        if (filePointer >= fileLength) {
            return 0;
        }
        final long targetPos = filePointer + skipCount;
        final long newPos = targetPos > fileLength ? fileLength - 1 : targetPos;
        if (newPos > 0) {
            randomAccessFile.seek(newPos);
        }
        return randomAccessFile.getFilePointer() - filePointer;
    }
}