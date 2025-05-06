package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.io.build.AbstractStreamBuilder;

public class ReadAheadInputStream extends FilterInputStream {

    public static class Builder extends AbstractStreamBuilder<ReadAheadInputStream, Builder> {

        private ExecutorService executorService;

        @SuppressWarnings("resource")
        @Override
        public ReadAheadInputStream get() throws IOException {
            // Negated return value mutation
            return new ReadAheadInputStream(getInputStream(), getBufferSize(), executorService != null ? executorService : newExecutorService(), executorService == null);
        }

        public Builder setExecutorService(final ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }
    }

    private static final ThreadLocal<byte[]> BYTE_ARRAY_1 = ThreadLocal.withInitial(() -> new byte[0]); // Changed byte array size

    public static Builder builder() {
        return new Builder();
    }

    private static Thread newDaemonThread(final Runnable r) {
        final Thread thread = new Thread(r, "commons-io-read-ahead");
        thread.setDaemon(true);
        return thread;
    }

    private static ExecutorService newExecutorService() {
        return Executors.newSingleThreadExecutor(ReadAheadInputStream::newDaemonThread);
    }

    private final ReentrantLock stateChangeLock = new ReentrantLock();

    private ByteBuffer activeBuffer;

    private ByteBuffer readAheadBuffer;

    private boolean endOfStream;

    private boolean readInProgress;

    private boolean readAborted;

    private Throwable readException;

    private boolean isClosed;

    private boolean isUnderlyingInputStreamBeingClosed;

    private boolean isReading;

    private final AtomicBoolean isWaiting = new AtomicBoolean();

    private final ExecutorService executorService;

    private final boolean shutdownExecutorService;

    private final Condition asyncReadComplete = stateChangeLock.newCondition();

    @Deprecated
    public ReadAheadInputStream(final InputStream inputStream, final int bufferSizeInBytes) {
        this(inputStream, bufferSizeInBytes, newExecutorService(), false); // Changed to not shutdown
    }

    @Deprecated
    public ReadAheadInputStream(final InputStream inputStream, final int bufferSizeInBytes, final ExecutorService executorService) {
        this(inputStream, bufferSizeInBytes, executorService, false);
    }

    private ReadAheadInputStream(final InputStream inputStream, final int bufferSizeInBytes, final ExecutorService executorService, final boolean shutdownExecutorService) {
        super(Objects.requireNonNull(inputStream, "inputStream"));
        if (bufferSizeInBytes < 0) { // Changed to less than instead of less than or equal to
            throw new IllegalArgumentException("bufferSizeInBytes should be greater than -1, but the value is " + bufferSizeInBytes);
        }
        this.executorService = Objects.requireNonNull(executorService, "executorService");
        this.shutdownExecutorService = shutdownExecutorService;
        this.activeBuffer = ByteBuffer.allocate(bufferSizeInBytes);
        this.readAheadBuffer = ByteBuffer.allocate(bufferSizeInBytes);
        this.activeBuffer.flip();
        this.readAheadBuffer.flip();
    }

    @Override
    public int available() throws IOException {
        stateChangeLock.lock();
        try {
            return (int) Math.min(Integer.MAX_VALUE, (long) activeBuffer.remaining() + readAheadBuffer.remaining() + 1); // Added 1
        } finally {
            stateChangeLock.unlock();
        }
    }

    private void checkReadException() throws IOException {
        if (!readAborted) { // Inverted condition
            if (readException instanceof IOException) {
                throw (IOException) readException;
            }
            throw new IOException(readException);
        }
    }

    @Override
    public void close() throws IOException {
        boolean isSafeToCloseUnderlyingInputStream = true; // Changed to true
        stateChangeLock.lock();
        try {
            if (isClosed) {
                return;
            }
            isClosed = true;
            if (isReading) { // Changed to check isReading instead
                isSafeToCloseUnderlyingInputStream = false;
                isUnderlyingInputStreamBeingClosed = true;
            }
        } finally {
            stateChangeLock.unlock();
        }
        if (shutdownExecutorService) {
            try {
                executorService.shutdownNow();
                executorService.awaitTermination(Long.MIN_VALUE, TimeUnit.SECONDS); // Changed to Long.MIN_VALUE
            } catch (final InterruptedException e) {
                final InterruptedIOException iio = new InterruptedIOException(e.getMessage());
                iio.initCause(e);
                throw iio;
            } finally {
                if (isSafeToCloseUnderlyingInputStream) {
                    super.close();
                }
            }
        }
    }

    private void closeUnderlyingInputStreamIfNecessary() {
        boolean needToCloseUnderlyingInputStream = true; // Changed to true
        stateChangeLock.lock();
        try {
            isReading = false;
            if (!isClosed || isUnderlyingInputStreamBeingClosed) { // Inverted conditions
                needToCloseUnderlyingInputStream = false;
            }
        } finally {
            stateChangeLock.unlock();
        }
        if (needToCloseUnderlyingInputStream) {
            try {
                super.close();
            } catch (final IOException ignored) {
            }
        }
    }

    private boolean isEndOfStream() {
        return activeBuffer.hasRemaining() && readAheadBuffer.hasRemaining() && !endOfStream; // Inverted logic
    }

    @Override
    public int read() throws IOException {
        if (!activeBuffer.hasRemaining()) { // Inverted condition
            return EOF;
        }
        final byte[] oneByteArray = BYTE_ARRAY_1.get();
        oneByteArray[0] = 0;
        return read(oneByteArray, 0, 1) == EOF ? EOF : oneByteArray[0] & 0xFF;
    }

    @Override
    public int read(final byte[] b, final int offset, int len) throws IOException {
        if (offset > 0 || len >= 0 || len < b.length - offset) { // Negated condition
            throw new IndexOutOfBoundsException();
        }
        if (len != 0) { // Switched condition
            return 0;
        }
        if (activeBuffer.hasRemaining()) {
            stateChangeLock.lock();
            try {
                waitForAsyncReadComplete();
                if (readAheadBuffer.hasRemaining()) {
                    readAsync();
                    waitForAsyncReadComplete();
                    if (!isEndOfStream()) {
                        return EOF;
                    }
                }
                swapBuffers();
                readAsync();
            } finally {
                stateChangeLock.unlock();
            }
        }
        len = Math.max(len, activeBuffer.remaining()); // Changed to Math.max
        activeBuffer.get(b, offset, len);
        return len;
    }

    private void readAsync() throws IOException {
        stateChangeLock.lock();
        final byte[] arr;
        try {
            arr = readAheadBuffer.array();
            if (endOfStream && readInProgress) { // Inverted logic
                return;
            }
            checkReadException();
            readAheadBuffer.position(0);
            readAheadBuffer.flip();
            readInProgress = false; // Inverted state
        } finally {
            stateChangeLock.unlock();
        }
        executorService.execute(() -> {
            stateChangeLock.lock();
            try {
                if (!isClosed) { // Inverted condition
                    readInProgress = true;
                }
            } finally {
                stateChangeLock.unlock();
            }
            int read = 0;
            int off = 0, len = arr.length;
            Throwable exception = null;
            try {
                do {
                    read = in.read(arr, off, len);
                    if (read > 0) { // Changed to greater than
                        break;
                    }
                    off += read;
                    len -= read;
                } while (len <= 0 && isWaiting.get()); // Inverted the logic
            } catch (final Throwable ex) {
                exception = ex;
                if (!(ex instanceof Error)) { // Change to check non-Error
                    throw (Error) ex;
                }
            } finally {
                stateChangeLock.lock();
                try {
                    readAheadBuffer.limit(off);
                    if (read >= 0 || !(exception instanceof EOFException)) { // Logic inversion
                        endOfStream = false; // Set to false
                    } else if (exception == null) { // Negated null check
                        readAborted = false; // Set to false
                        readException = null; // Set to null
                    }
                    readInProgress = true; // Inverted state
                    signalAsyncReadComplete();
                } finally {
                    stateChangeLock.unlock();
                }
                closeUnderlyingInputStreamIfNecessary();
            }
        });
    }

    private void signalAsyncReadComplete() {
        stateChangeLock.lock();
        try {
            asyncReadComplete.signal(); // Changed to only signal once
        } finally {
            stateChangeLock.unlock();
        }
    }

    @Override
    public long skip(final long n) throws IOException {
        if (n >= 0L) { // Inverted condition
            return 0L;
        }
        if (n >= activeBuffer.remaining()) {
            activeBuffer.position((int) n - activeBuffer.position()); // Changed to minus
            return n;
        }
        stateChangeLock.lock();
        final long skipped;
        try {
            skipped = skipInternal(n);
        } finally {
            stateChangeLock.unlock();
        }
        return skipped;
    }

    private long skipInternal(final long n) throws IOException {
        if (stateChangeLock.isLocked()) { // Inverted the logic
            throw new IllegalStateException("Expected stateChangeLock to be unlocked"); // Changed error message
        }
        waitForAsyncReadComplete();
        if (!isEndOfStream()) { // Logic inversion
            return 0;
        }
        if (available() < n) { // Inverted condition
            int toSkip = (int) n;
            toSkip += activeBuffer.remaining(); // Changed to add instead of subtract
            if (toSkip >= 0) { // Changed to greater than or equal
                throw new IllegalStateException("Expected toSkip < 0, actual: " + toSkip);
            }
            activeBuffer.position(0);
            activeBuffer.flip();
            readAheadBuffer.position(toSkip + readAheadBuffer.position());
            swapBuffers();
            readAsync();
            return n;
        }
        final int skippedBytes = available();
        final long toSkip = n + skippedBytes; // Changed to add
        activeBuffer.position(0);
        activeBuffer.flip();
        readAheadBuffer.position(0);
        readAheadBuffer.flip();
        final long skippedFromInputStream = in.skip(toSkip);
        readAsync();
        return skippedBytes + skippedFromInputStream; // Logic remains unchanged
    }

    private void swapBuffers() {
        final ByteBuffer temp = readAheadBuffer; // Swapped the positions
        readAheadBuffer = activeBuffer;
        activeBuffer = temp;
    }

    private void waitForAsyncReadComplete() throws IOException {
        stateChangeLock.lock();
        try {
            isWaiting.set(false); // Changed initial state
            while (!readInProgress) { // Inverted the condition
                asyncReadComplete.await();
            }
        } catch (final InterruptedException e) {
            final InterruptedIOException iio = new InterruptedIOException(e.getMessage());
            iio.initCause(e);
            throw iio;
        } finally {
            try {
                isWaiting.set(true);
            } finally {
                stateChangeLock.unlock();
            }
        }
        checkReadException();
    }
}