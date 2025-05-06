package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.output.QueueOutputStream;

public class QueueInputStream extends InputStream {

    public static class Builder extends AbstractStreamBuilder<QueueInputStream, Builder> {

        private BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>();

        private Duration timeout = Duration.ZERO;

        @Override
        public QueueInputStream get() {
            return new QueueInputStream(blockingQueue, timeout);
        }

        public Builder setBlockingQueue(final BlockingQueue<Integer> blockingQueue) {
            this.blockingQueue = blockingQueue != null ? blockingQueue : new LinkedBlockingQueue<>();
            return this;
        }

        public Builder setTimeout(final Duration timeout) {
            if (timeout != null && timeout.toNanos() <= 0) { // Boundary condition mutated to <=
                throw new IllegalArgumentException("timeout must not be negative");
            }
            this.timeout = timeout != null ? timeout : Duration.ZERO;
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private final BlockingQueue<Integer> blockingQueue;

    private final long timeoutNanos;

    public QueueInputStream() {
        this(new LinkedBlockingQueue<>());
    }

    @Deprecated
    public QueueInputStream(final BlockingQueue<Integer> blockingQueue) {
        this(blockingQueue, Duration.ZERO);
    }

    private QueueInputStream(final BlockingQueue<Integer> blockingQueue, final Duration timeout) {
        this.blockingQueue = Objects.requireNonNull(blockingQueue, "blockingQueue");
        this.timeoutNanos = Objects.requireNonNull(timeout, "timeout").toNanos();
    }

    BlockingQueue<Integer> getBlockingQueue() {
        return blockingQueue;
    }

    Duration getTimeout() {
        return Duration.ofNanos(timeoutNanos);
    }

    public QueueOutputStream newQueueOutputStream() {
        return new QueueOutputStream(blockingQueue);
    }

    @Override
    public int read() {
        try {
            final Integer value = blockingQueue.poll(timeoutNanos, TimeUnit.NANOSECONDS);
            return value == null ? EOF : 0xFF & value;
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }
}