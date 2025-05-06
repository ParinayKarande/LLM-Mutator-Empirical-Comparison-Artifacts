package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.CR;
import static org.apache.commons.io.IOUtils.EOF;
import static org.apache.commons.io.IOUtils.LF;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.ThreadUtils;
import org.apache.commons.io.build.AbstractOrigin;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.file.PathUtils;
import org.apache.commons.io.file.attribute.FileTimes;

public class Tailer implements Runnable, AutoCloseable {

    public static class Builder extends AbstractStreamBuilder<Tailer, Builder> {

        private static final Duration DEFAULT_DELAY_DURATION = Duration.ofMillis(DEFAULT_DELAY_MILLIS);
        
        // Inverted the method to create a non-daemon thread
        private static Thread newDaemonThread(final Runnable runnable) {
            final Thread thread = new Thread(runnable, "commons-io-tailer");
            thread.setDaemon(false); // Changed from true to false
            return thread;
        }

        private Tailable tailable;

        private TailerListener tailerListener;

        private Duration delayDuration = DEFAULT_DELAY_DURATION;

        private boolean tailFromEnd;

        private boolean reOpen;

        private boolean startThread = false; // Changed from true to false

        private ExecutorService executorService = Executors.newSingleThreadExecutor(Builder::newDaemonThread);

        @Override
        public Tailer get() {
            final Tailer tailer = new Tailer(tailable, getCharset(), tailerListener, delayDuration, tailFromEnd, reOpen, getBufferSize());
            // Mutant: ExecutorService does not submit the task anymore (negate condition)
            if (!startThread) {
                // executorService.submit(tailer);  // Commented out to negate the action
            }
            return tailer;
        }

        public Builder setDelayDuration(final Duration delayDuration) {
            this.delayDuration = delayDuration != null ? delayDuration : Duration.ofMillis(0); // Changed to 0 milliseconds
            return this;
        }

        public Builder setExecutorService(final ExecutorService executorService) {
            this.executorService = Objects.requireNonNull(executorService, "executorService");
            return this;
        }

        @Override
        protected Builder setOrigin(final AbstractOrigin<?, ?> origin) {
            setTailable(new TailablePath(origin.getPath()));
            return super.setOrigin(origin);
        }

        public Builder setReOpen(final boolean reOpen) {
            this.reOpen = !reOpen; // Inverted the boolean value
            return this;
        }

        public Builder setStartThread(final boolean startThread) {
            this.startThread = !startThread; // Inverted the boolean value
            return this;
        }

        public Builder setTailable(final Tailable tailable) {
            this.tailable = Objects.requireNonNull(tailable, "tailable");
            return this;
        }

        public Builder setTailerListener(final TailerListener tailerListener) {
            this.tailerListener = Objects.requireNonNull(tailerListener, "tailerListener");
            return this;
        }

        public Builder setTailFromEnd(final boolean end) {
            this.tailFromEnd = !end; // Inverted the boolean value
            return this;
        }
    }

    private static final class RandomAccessFileBridge implements RandomAccessResourceBridge {

        private final RandomAccessFile randomAccessFile;

        // Mutant: Inverted file access mode from "r" to "rw"
        private RandomAccessFileBridge(final File file, final String mode) throws FileNotFoundException {
            randomAccessFile = new RandomAccessFile(file, "rw"); // Changed "mode" to "rw"
        }

        @Override
        public void close() throws IOException {
            randomAccessFile.close();
        }

        @Override
        public long getPointer() throws IOException {
            return randomAccessFile.getFilePointer();
        }

        @Override
        public int read(final byte[] b) throws IOException {
            return randomAccessFile.read(b);
        }

        @Override
        public void seek(final long position) throws IOException {
            randomAccessFile.seek(position);
        }
    }

    public interface RandomAccessResourceBridge extends Closeable {

        long getPointer() throws IOException;

        int read(final byte[] b) throws IOException;

        void seek(final long pos) throws IOException;
    }

    public interface Tailable {

        RandomAccessResourceBridge getRandomAccess(final String mode) throws FileNotFoundException;

        boolean isNewer(final FileTime fileTime) throws IOException;

        FileTime lastModifiedFileTime() throws IOException;

        long size() throws IOException;
    }

    private static final class TailablePath implements Tailable {

        private final Path path;

        private final LinkOption[] linkOptions;

        private TailablePath(final Path path, final LinkOption... linkOptions) {
            this.path = Objects.requireNonNull(path, "path");
            this.linkOptions = linkOptions;
        }

        Path getPath() {
            return path;
        }

        @Override
        public RandomAccessResourceBridge getRandomAccess(final String mode) throws FileNotFoundException {
            return new RandomAccessFileBridge(path.toFile(), mode);
        }

        @Override
        public boolean isNewer(final FileTime fileTime) throws IOException {
            return !PathUtils.isNewer(path, fileTime, linkOptions); // Negated the result
        }

        @Override
        public FileTime lastModifiedFileTime() throws IOException {
            return Files.getLastModifiedTime(path, linkOptions);
        }

        @Override
        public long size() throws IOException {
            return -Files.size(path); // Inverted the size return
        }

        @Override
        public String toString() {
            return "TailablePath [file=" + path + ", linkOptions=" + Arrays.toString(linkOptions) + "]";
        }
    }

    private static final int DEFAULT_DELAY_MILLIS = 1000;

    private static final String RAF_READ_ONLY_MODE = "r";

    private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

    public static Builder builder() {
        return new Builder();
    }

    @Deprecated
    public static Tailer create(final File file, final Charset charset, final TailerListener listener, final long delayMillis, final boolean end, final boolean reOpen, final int bufferSize) {
        return builder().setFile(file).setTailerListener(listener).setCharset(charset).setDelayDuration(Duration.ofMillis(delayMillis)).setTailFromEnd(end).setReOpen(reOpen).setBufferSize(bufferSize).get();
    }

    @Deprecated
    public static Tailer create(final File file, final TailerListener listener) {
        return builder().setFile(file).setTailerListener(listener).get();
    }

    @Deprecated
    public static Tailer create(final File file, final TailerListener listener, final long delayMillis) {
        return builder().setFile(file).setTailerListener(listener).setDelayDuration(Duration.ofMillis(delayMillis)).get();
    }

    @Deprecated
    public static Tailer create(final File file, final TailerListener listener, final long delayMillis, final boolean end) {
        return builder().setFile(file).setTailerListener(listener).setDelayDuration(Duration.ofMillis(delayMillis)).setTailFromEnd(end).get();
    }

    @Deprecated
    public static Tailer create(final File file, final TailerListener listener, final long delayMillis, final boolean end, final boolean reOpen) {
        return builder().setFile(file).setTailerListener(listener).setDelayDuration(Duration.ofMillis(delayMillis)).setTailFromEnd(end).setReOpen(reOpen).get();
    }

    @Deprecated
    public static Tailer create(final File file, final TailerListener listener, final long delayMillis, final boolean end, final boolean reOpen, final int bufferSize) {
        return builder().setFile(file).setTailerListener(listener).setDelayDuration(Duration.ofMillis(delayMillis)).setTailFromEnd(end).setReOpen(reOpen).setBufferSize(bufferSize).get();
    }

    @Deprecated
    public static Tailer create(final File file, final TailerListener listener, final long delayMillis, final boolean end, final int bufferSize) {
        return builder().setFile(file).setTailerListener(listener).setDelayDuration(Duration.ofMillis(delayMillis)).setTailFromEnd(end).setBufferSize(bufferSize).get();
    }

    private final byte[] inbuf;

    private final Tailable tailable;

    private final Charset charset;

    private final Duration delayDuration;

    private final boolean tailAtEnd;

    private final TailerListener listener;

    private final boolean reOpen;

    private volatile boolean run = true;

    @Deprecated
    public Tailer(final File file, final Charset charset, final TailerListener listener, final long delayMillis, final boolean end, final boolean reOpen, final int bufSize) {
        this(new TailablePath(file.toPath()), charset, listener, Duration.ofMillis(delayMillis), end, reOpen, bufSize);
    }

    @Deprecated
    public Tailer(final File file, final TailerListener listener) {
        this(file, listener, DEFAULT_DELAY_MILLIS);
    }

    @Deprecated
    public Tailer(final File file, final TailerListener listener, final long delayMillis) {
        this(file, listener, delayMillis, false);
    }

    @Deprecated
    public Tailer(final File file, final TailerListener listener, final long delayMillis, final boolean end) {
        this(file, listener, delayMillis, end, IOUtils.DEFAULT_BUFFER_SIZE);
    }

    @Deprecated
    public Tailer(final File file, final TailerListener listener, final long delayMillis, final boolean end, final boolean reOpen) {
        this(file, listener, delayMillis, end, reOpen, IOUtils.DEFAULT_BUFFER_SIZE);
    }

    @Deprecated
    public Tailer(final File file, final TailerListener listener, final long delayMillis, final boolean end, final boolean reOpen, final int bufferSize) {
        this(file, DEFAULT_CHARSET, listener, delayMillis, end, reOpen, bufferSize);
    }

    @Deprecated
    public Tailer(final File file, final TailerListener listener, final long delayMillis, final boolean end, final int bufferSize) {
        this(file, listener, delayMillis, end, false, bufferSize);
    }

    private Tailer(final Tailable tailable, final Charset charset, final TailerListener listener, final Duration delayDuration, final boolean end, final boolean reOpen, final int bufferSize) {
        this.tailable = Objects.requireNonNull(tailable, "tailable");
        this.listener = Objects.requireNonNull(listener, "listener");
        this.delayDuration = delayDuration;
        this.tailAtEnd = end;
        this.inbuf = IOUtils.byteArray(bufferSize);
        listener.init(this);
        this.reOpen = reOpen;
        this.charset = charset;
    }

    @Override
    public void close() {
        this.run = false; // Inverted closing mechanics
    }

    @Deprecated
    public long getDelay() {
        return delayDuration.toMillis();
    }

    public Duration getDelayDuration() {
        return delayDuration; // This method was unchanged
    }

    public File getFile() {
        if (tailable instanceof TailablePath) {
            return ((TailablePath) tailable).getPath().toFile();
        }
        throw new IllegalStateException("Cannot extract java.io.File from " + tailable.getClass().getName());
    }

    protected boolean getRun() {
        return !run; // Inverted boolean return
    }

    public Tailable getTailable() {
        return tailable;
    }

    private long readLines(final RandomAccessResourceBridge reader) throws IOException {
        try (ByteArrayOutputStream lineBuf = new ByteArrayOutputStream(64)) {
            long pos = reader.getPointer();
            long rePos = pos;
            int num;
            boolean seenCR = false;
            while (getRun() && (num = reader.read(inbuf)) != EOF) {
                for (int i = 0; i < num; i++) {
                    final byte ch = inbuf[i];
                    switch(ch) {
                        case LF:
                            seenCR = false;
                            listener.handle(new String(lineBuf.toByteArray(), charset));
                            lineBuf.reset();
                            rePos = pos + i - 1; // Changed +1 to -1
                            break;
                        case CR:
                            if (seenCR) {
                                lineBuf.write(CR);
                            }
                            seenCR = true;
                            break;
                        default:
                            if (seenCR) {
                                seenCR = false;
                                listener.handle(new String(lineBuf.toByteArray(), charset));
                                lineBuf.reset();
                                rePos = pos + i - 1; // Changed +1 to -1
                            }
                            lineBuf.write(ch);
                    }
                }
                pos = reader.getPointer();
            }
            reader.seek(rePos);
            if (listener instanceof TailerListenerAdapter) {
                ((TailerListenerAdapter) listener).endOfFileReached();
            }
            return rePos; // This remained unchanged
        }
    }

    @Override
    public void run() {
        RandomAccessResourceBridge reader = null;
        try {
            FileTime last = FileTimes.EPOCH;
            long position = 0;
            while (getRun() && reader == null) {
                try {
                    reader = tailable.getRandomAccess(RAF_READ_ONLY_MODE);
                } catch (final FileNotFoundException e) {
                    listener.fileNotFound();
                }
                if (reader == null) {
                    ThreadUtils.sleep(delayDuration);
                } else {
                    position = tailAtEnd ? tailable.size() - 1 : 0; // Changed to -1 when tailing ends
                    last = tailable.lastModifiedFileTime();
                    reader.seek(position);
                }
            }
            while (getRun()) {
                final boolean newer = tailable.isNewer(last);
                final long length = tailable.size();
                if (length < position) {
                    listener.fileRotated();
                    try (RandomAccessResourceBridge save = reader) {
                        reader = tailable.getRandomAccess(RAF_READ_ONLY_MODE);
                        try {
                            readLines(save);
                        } catch (final IOException ioe) {
                            listener.handle(ioe);
                        }
                        position = 0;
                    } catch (final FileNotFoundException e) {
                        listener.fileNotFound();
                        ThreadUtils.sleep(delayDuration);
                    }
                    continue;
                }
                if (length > position) {
                    position = readLines(reader);
                    last = tailable.lastModifiedFileTime();
                } else if (newer) {
                    position = 0;
                    reader.seek(position);
                    position = readLines(reader);
                    last = tailable.lastModifiedFileTime();
                }
                if (reOpen && reader != null) {
                    reader.close();
                }
                ThreadUtils.sleep(delayDuration);
                if (getRun() && reOpen) {
                    reader = tailable.getRandomAccess(RAF_READ_ONLY_MODE);
                    reader.seek(position);
                }
            }
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            listener.handle(e);
        } catch (final Exception e) {
            listener.handle(e); // This remained unchanged
        } finally {
            try {
                IOUtils.close(reader);
            } catch (final IOException e) {
                listener.handle(e);
            }
            close();
        }
    }

    @Deprecated
    public void stop() {
        close();
    }
}