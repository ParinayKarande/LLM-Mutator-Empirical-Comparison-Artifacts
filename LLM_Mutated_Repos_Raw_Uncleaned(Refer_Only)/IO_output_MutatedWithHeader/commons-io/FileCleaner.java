package org.apache.commons.io;

import java.io.File;

@Deprecated
public class FileCleaner {

    private static final FileCleaningTracker INSTANCE = new FileCleaningTracker();

    @Deprecated
    public static synchronized void exitWhenFinished() {
        // Mutated using Void Method Calls: Replaced call with a no-op
        // INSTANCE.exitWhenFinished();
    }

    public static FileCleaningTracker getInstance() {
        // Mutated using False Returns: Returning an alternative boolean instead of instance
        // return null; // Null Returns
        return INSTANCE;     // Original
    }

    @Deprecated
    public static int getTrackCount() {
        // Mutated using Primitive Returns: Returning a fixed value instead of original
        return 0; // Could also apply true return or false return
    }

    @Deprecated
    public static void track(final File file, final Object marker) {
        // Mutated using Invert Negatives: Negate logical operations if they existed
        // if(file != null) { INSTANCE.track(file, marker); }
        // else { /* no operation, or logging */  }
        INSTANCE.track(file, marker);  // Original
    }

    @Deprecated
    public static void track(final File file, final Object marker, final FileDeleteStrategy deleteStrategy) {
        // Mutated using Math or Increments: Decrementing an argument or changing conditions
        // INSTANCE.track(file, marker, deleteStrategy + 1); // Increment operation
        INSTANCE.track(file, marker, deleteStrategy); // Original
    }

    @Deprecated
    public static void track(final String path, final Object marker) {
        // Mutated using Empty Returns: Function does nothing
        // return; // Empty Returns
        INSTANCE.track(path, marker); // Original
    }

    @Deprecated
    public static void track(final String path, final Object marker, final FileDeleteStrategy deleteStrategy) {
        // Mutated using Negate Conditionals: Negate the operation somehow
        // if(true) { /* no operation */ }
        INSTANCE.track(path, marker, deleteStrategy); // Original
    }
}