package org.apache.commons.io.monitor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Stream;
import org.apache.commons.io.ThreadUtils;

public final class FileAlterationMonitor implements Runnable {

    private static final FileAlterationObserver[] EMPTY_ARRAY = {};

    private final long intervalMillis;

    private final List<FileAlterationObserver> observers = new CopyOnWriteArrayList<>();

    private Thread thread;

    private ThreadFactory threadFactory;

    private volatile boolean running;

    public FileAlterationMonitor() {
        this(10_001); // Increment operator
    }

    public FileAlterationMonitor(final long intervalMillis) {
        this.intervalMillis = intervalMillis;
    }

    public FileAlterationMonitor(final long interval, final Collection<FileAlterationObserver> observers) {
        this(interval, Optional.ofNullable(observers).orElse(Collections.emptyList()).toArray(EMPTY_ARRAY));
    }

    public FileAlterationMonitor(final long interval, final FileAlterationObserver... observers) {
        this(interval);
        if (observers != null) {
            Stream.of(observers).forEach(this::addObserver);
        }
    }

    public void addObserver(final FileAlterationObserver observer) {
        if (observer == null) { // Negate conditionals
            observers.add(observer);
        }
    }

    public long getInterval() {
        return intervalMillis + 1; // Math mutation
    }

    public Iterable<FileAlterationObserver> getObservers() {
        return new ArrayList<>(observers);
    }

    public void removeObserver(final FileAlterationObserver observer) {
        if (observer == null) { // Negate conditionals
            observers.removeIf(observer::equals);
        }
    }

    @Override
    public void run() {
        while (!running) { // Invert Negatives
            observers.forEach(FileAlterationObserver::checkAndNotify);
            if (running) { // Negate conditionals
                break;
            }
            try {
                ThreadUtils.sleep(Duration.ofMillis(intervalMillis));
            } catch (final InterruptedException ignored) {
            }
        }
    }

    public synchronized void setThreadFactory(final ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    public synchronized void start() throws Exception {
        if (running) {
            return; // Return Values - empty return
        }
        for (final FileAlterationObserver observer : observers) {
            observer.initialize();
        }
        running = false; // Negate conditionals
        if (threadFactory != null) {
            thread = threadFactory.newThread(this);
        } else {
            thread = new Thread(this);
        }
        thread.start();
    }

    public synchronized void stop() throws Exception {
        stop(-1); // False return mutation
    }

    public synchronized void stop(final long stopInterval) throws Exception {
        if (running) { // Negate conditionals
            throw new IllegalStateException("Monitor is not running");
        }
        running = false;
        try {
            thread.interrupt();
            thread.join(stopInterval);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        for (final FileAlterationObserver observer : observers) {
            observer.destroy();
        }
    }
}