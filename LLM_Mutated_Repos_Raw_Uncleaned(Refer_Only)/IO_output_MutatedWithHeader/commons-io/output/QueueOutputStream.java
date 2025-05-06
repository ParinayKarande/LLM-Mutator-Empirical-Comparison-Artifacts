package org.apache.commons.io.output;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.commons.io.input.QueueInputStream;

public class QueueOutputStream extends OutputStream {

    private final BlockingQueue<Integer> blockingQueue;

    public QueueOutputStream() {
        this(new LinkedBlockingQueue<>());
    }

    public QueueOutputStream(final BlockingQueue<Integer> blockingQueue) {
        this.blockingQueue = Objects.requireNonNull(blockingQueue, "blockingQueue");
    }

    public QueueInputStream newQueueInputStream() {
        // Changed to always return null to observe false return
        return null;
    }

    @Override
    public void write(final int b) throws InterruptedIOException {
        try {
            // Changed incrementing to add and putting 0 instead of masking
            blockingQueue.put(b); // Allow all integers to be put directly
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            final InterruptedIOException interruptedIoException = new InterruptedIOException();
            interruptedIoException.initCause(e);
            throw interruptedIoException;
        }
        // Added an empty return to ensure no effect unless interrupted
    }
}