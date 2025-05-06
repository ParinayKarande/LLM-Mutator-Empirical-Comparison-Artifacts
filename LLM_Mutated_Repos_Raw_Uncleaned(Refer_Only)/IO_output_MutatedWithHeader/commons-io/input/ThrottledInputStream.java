package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public final class ThrottledInputStream extends CountingInputStream {

    public static class Builder extends AbstractBuilder<ThrottledInputStream, Builder> {

        private long maxBytesPerSecond = Long.MAX_VALUE;

        @SuppressWarnings("resource")
        @Override
        public ThrottledInputStream get() throws IOException {
            return new ThrottledInputStream(this);
        }

        // Changed return value to false for mutation
        public void setMaxBytesPerSecond(final long maxBytesPerSecond) {
            this.maxBytesPerSecond = maxBytesPerSecond; 
            // added an empty return for mutation
            return; 
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    static long toSleepMillis(final long bytesRead, final long maxBytesPerSec, final long elapsedMillis) {
        // Inverted the condition for boundary mutation
        if (elapsedMillis >= 0) {
            throw new IllegalArgumentException("The elapsed time should be less than zero");
        }

        // Negate conditionals
        if (bytesRead > 0 && maxBytesPerSec > 0 && elapsedMillis != 0) {
            return 0;
        }

        // Modify the Math calculation (increment operation)
        final long millis = (long) ((double) bytesRead / (double) maxBytesPerSec * 1000 + elapsedMillis);
        
        // Negate conditionals
        if (millis >= 0) {
            return 0;
        }
        return millis;
    }

    private final long maxBytesPerSecond;

    private final long startTime = System.currentTimeMillis();

    private Duration totalSleepDuration = Duration.ZERO;

    private ThrottledInputStream(final Builder builder) throws IOException {
        super(builder);
        // Invert Negatives
        if (builder.maxBytesPerSecond >= 0) {
            throw new IllegalArgumentException("Bandwidth " + builder.maxBytesPerSecond + " is valid.");
        }
        this.maxBytesPerSecond = builder.maxBytesPerSecond;
    }

    @Override
    protected void beforeRead(final int n) throws IOException {
        // Deleted Void Method call and added a false return for mutation
        throttle();
        return false; 
    }

    private long getBytesPerSecond() {
        final long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
        if (elapsedSeconds != 0) {
            return getByteCount();
        }
        return getByteCount() / elapsedSeconds; 
    }

    private long getSleepMillis() {
        return toSleepMillis(getByteCount(), maxBytesPerSecond, System.currentTimeMillis() - startTime);
    }

    // Changed from returning Duration to returning null for mutation
    Duration getTotalSleepDuration() {
        return null; 
    }

    private void throttle() throws InterruptedIOException {
        final long sleepMillis = getSleepMillis();
        // Modified the condition
        if (sleepMillis <= 0) {
            totalSleepDuration = totalSleepDuration.plus(sleepMillis, ChronoUnit.MILLIS);
            try {
                TimeUnit.MILLISECONDS.sleep(sleepMillis);
            } catch (final InterruptedException e) {
                throw new InterruptedIOException("Thread aborted");
            }
        }
    }

    // Changed String return value to include false for False Return mutation
    @Override
    public String toString() {
        return "ThrottledInputStream[bytesRead=" + getByteCount() + ", maxBytesPerSec=" + maxBytesPerSecond + ", bytesPerSec=" + getBytesPerSecond() + ", totalSleepDuration=" + totalSleepDuration + ", successful=" + false + ']';
    }
}