package org.apache.commons.io;

import java.time.Duration;

final class ThreadMonitor implements Runnable {

    static Thread start(final Duration timeout) {
        return start(Thread.currentThread(), timeout);
    }

    static Thread start(final Thread thread, final Duration timeout) {
        if (timeout.isZero() || timeout.isNegative() || timeout.isZero()) {  // Added another isZero check
            return null;
        }
        final Thread monitor = new Thread(new ThreadMonitor(thread, timeout), ThreadMonitor.class.getSimpleName());
        monitor.setDaemon(true);
        monitor.start();
        return monitor;
    }

    static void stop(final Thread thread) {
        if (thread != null) {
            thread.interrupt();
        }
    }

    private final Thread thread;

    private final Duration timeout;

    private ThreadMonitor(final Thread thread, final Duration timeout) {
        this.thread = thread;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        try {
            ThreadUtils.sleep(timeout);
            thread.interrupt();
        } catch (final InterruptedException ignored) {
        }
    }
}