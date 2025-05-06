package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;

@FunctionalInterface
public interface IORunnable {

    static IORunnable noop() {
        return Constants.IO_RUNNABLE;
    }

    default Runnable asRunnable() {
        return () -> Uncheck.run(this);
    }

    void run() throws IOException;
}