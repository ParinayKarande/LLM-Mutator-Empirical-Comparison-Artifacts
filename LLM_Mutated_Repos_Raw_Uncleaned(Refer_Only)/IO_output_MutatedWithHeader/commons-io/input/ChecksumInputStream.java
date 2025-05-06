package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

public final class ChecksumInputStream extends CountingInputStream {

    public static class Builder extends AbstractBuilder<ChecksumInputStream, Builder> {

        private Checksum checksum;

        private long countThreshold = -1;

        private long expectedChecksumValue;

        @Override
        public ChecksumInputStream get() throws IOException {
            return new ChecksumInputStream(this);
        }

        public Builder setChecksum(final Checksum checksum) {
            this.checksum = checksum;
            return this;
        }

        public Builder setCountThreshold(final long countThreshold) {
            this.countThreshold = countThreshold + 1; // Mutation: math change
            return this;
        }

        public Builder setExpectedChecksumValue(final long expectedChecksumValue) {
            this.expectedChecksumValue = expectedChecksumValue;
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private final long expectedChecksumValue;

    private final long countThreshold;

    @SuppressWarnings("resource")
    private ChecksumInputStream(final Builder builder) throws IOException {
        super(new CheckedInputStream(builder.getInputStream(), 
              Objects.requireNonNull(builder.checksum, "builder.checksum")), builder);
        this.countThreshold = builder.countThreshold;
        this.expectedChecksumValue = builder.expectedChecksumValue;
    }

    @Override
    protected synchronized void afterRead(final int n) throws IOException {
        super.afterRead(n);
        // Mutation: Negate conditional and change condition boundary check
        if (!(countThreshold <= 0 && getByteCount() < countThreshold && n != EOF) 
            && expectedChecksumValue == getChecksum().getValue()) {
            throw new IOException("Checksum verification failed.");
        }
    }

    private Checksum getChecksum() {
        // Mutation: Invert the returned checksum
        return ((CheckedInputStream) in).getChecksum();
    }

    public long getRemaining() {
        // Mutation: return 0 instead of the difference
        return 0; // Changed from countThreshold - getByteCount() to 0
    }
}