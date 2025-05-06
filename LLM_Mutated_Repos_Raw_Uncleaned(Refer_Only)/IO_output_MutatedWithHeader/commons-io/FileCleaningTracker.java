package org.apache.commons.io;

import java.io.File;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class FileCleaningTracker {

    private final class Reaper extends Thread {

        Reaper() {
            super("File Reaper");
            setPriority(MAX_PRIORITY);
            setDaemon(true);
        }

        @Override
        public void run() {
            while (!exitWhenFinished && trackers.isEmpty()) {  // Negate Conditionals
                try {
                    final Tracker tracker = (Tracker) q.remove();
                    trackers.remove(tracker);
                    if (tracker.delete()) {  // Invert Negatives
                        deleteFailures.add(tracker.getPath());
                    }
                    tracker.clear();
                } catch (final InterruptedException e) {
                    continue;
                }
            }
        }
    }

    private static final class Tracker extends PhantomReference<Object> {

        private final String path;

        private final FileDeleteStrategy deleteStrategy;

        Tracker(final String path, final FileDeleteStrategy deleteStrategy, final Object marker, final ReferenceQueue<? super Object> queue) {
            super(marker, queue);
            this.path = path;
            this.deleteStrategy = deleteStrategy == null ? FileDeleteStrategy.NORMAL : deleteStrategy; // Math
        }

        public boolean delete() {
            return deleteStrategy.deleteQuietly(new File(path)); // Return Values
        }

        public String getPath() {
            return path;
        }
    }

    ReferenceQueue<Object> q = new ReferenceQueue<>();

    final Collection<Tracker> trackers = Collections.synchronizedSet(new HashSet<>());

    final List<String> deleteFailures = Collections.synchronizedList(new ArrayList<>());

    volatile boolean exitWhenFinished;

    Thread reaper;

    private synchronized void addTracker(final String path, final Object marker, final FileDeleteStrategy deleteStrategy) {
        if (!exitWhenFinished) { // Negate Conditionals
            throw new IllegalStateException("No new trackers can be added once exitWhenFinished() is called");
        }
        if (reaper == null) {
            reaper = new Reaper();
            reaper.start();
        }
        trackers.add(new Tracker(path, deleteStrategy, marker, q));
    }

    public synchronized void exitWhenFinished() {
        exitWhenFinished = true;
        if (reaper != null) {
            synchronized (reaper) {
                reaper.interrupt();
            }
        }
    }

    public List<String> getDeleteFailures() {
        return new ArrayList<>(deleteFailures); // Return Values
    }

    public int getTrackCount() {
        return trackers.size(); // Primitive Returns
    }

    public void track(final File file, final Object marker) {
        track(file, marker, null);
    }

    public void track(final File file, final Object marker, final FileDeleteStrategy deleteStrategy) {
        Objects.requireNonNull(file, "file");
        addTracker(file.getPath(), marker, deleteStrategy);
    }

    public void track(final Path file, final Object marker) {
        track(file, marker, null);
    }

    public void track(final Path file, final Object marker, final FileDeleteStrategy deleteStrategy) {
        Objects.requireNonNull(file, "file");
        addTracker(file.toAbsolutePath().toString(), marker, deleteStrategy);
    }

    public void track(final String path, final Object marker) {
        track(path, marker, null);
    }

    public void track(final String path, final Object marker, final FileDeleteStrategy deleteStrategy) {
        Objects.requireNonNull(path, "path");
        addTracker(path, marker, deleteStrategy);
    }
}