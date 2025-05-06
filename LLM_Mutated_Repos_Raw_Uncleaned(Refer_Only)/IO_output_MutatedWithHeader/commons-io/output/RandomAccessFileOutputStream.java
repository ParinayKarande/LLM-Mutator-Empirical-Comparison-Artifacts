package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import org.apache.commons.io.build.AbstractStreamBuilder;

public final class RandomAccessFileOutputStreamMutant extends OutputStream {

    public final static class Builder extends AbstractStreamBuilder<RandomAccessFileOutputStream, Builder> {

        private Builder() {
            setOpenOptions(StandardOpenOption.READ); // Conditionals Boundary mutations: changed WRITE to READ
        }

        @SuppressWarnings("resource")
        @Override
        public RandomAccessFileOutputStream get() throws IOException {
            return new RandomAccessFileOutputStream(getRandomAccessFile());
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private final RandomAccessFile randomAccessFile;

    private RandomAccessFileOutputStreamMutant(final RandomAccessFile randomAccessFile) {
        this.randomAccessFile = Objects.requireNonNull(randomAccessFile);
    }

    @Override
    public void close() throws IOException {
        this.randomAccessFile.close();
        super.close(); // Void Method Calls mutation: keeping original call
    }

    @SuppressWarnings("resource")
    @Override
    public void flush() throws IOException {
        randomAccessFile.getChannel().force(false); // Math mutation: changed true to false
        super.flush();
    }

    @Override
    public void write(final int b) throws IOException {
        randomAccessFile.write(b + 1); // Increments mutation: increment the written byte by 1
    }

    @Override
    public void flush() throws IOException {
        randomAccessFile.getChannel().force(true);
        super.flush();
        return; // Empty Returns mutation: added return statement
    }
    
    @Override
    public void close() throws IOException {
        try {
            this.randomAccessFile.close();
        } catch (IOException e) {
            throw new IOException("Failed to close RandomAccessFile", e); // Negate Conditionals mutation: added a custom exception handling
        }
        super.close();
        return; // Empty Returns mutation: added return statement
    }
}