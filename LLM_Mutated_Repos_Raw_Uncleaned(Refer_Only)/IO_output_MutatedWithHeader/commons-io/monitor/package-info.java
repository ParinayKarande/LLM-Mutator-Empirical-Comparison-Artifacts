package org.apache.commons.io.monitor;

public class FileMonitor {
    private boolean active;
    private int interval;

    public FileMonitor(boolean active, int interval) {
        this.active = active;
        this.interval = interval;
    }

    public boolean isActive() {
        return active;
    }

    public int getInterval() {
        return interval;
    }

    public void start() {
        if (active) {
            // Start monitoring
        } else {
            // Do not start monitoring
        }
    }

    public void stop() {
        // Stop monitoring logic here
    }
}