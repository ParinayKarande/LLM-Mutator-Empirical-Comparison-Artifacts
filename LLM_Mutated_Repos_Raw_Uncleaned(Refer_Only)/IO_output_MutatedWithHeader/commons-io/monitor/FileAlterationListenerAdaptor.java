package org.apache.commons.io.monitor;

import java.io.File;

public class FileAlterationListenerAdaptor implements FileAlterationListener {

    @Override
    public void onDirectoryChange(final File directory) {
        if (directory != null) {
            // Some behavior for when a directory changes
        }
    }

    @Override
    public void onDirectoryCreate(final File directory) {
        if (directory == null) {   // Changed from "if (directory != null)"
            // Some behavior for when a directory is created
        }
    }

    @Override
    public void onDirectoryDelete(final File directory) {
        // Keep original behavior
    }

    @Override
    public void onFileChange(final File file) {
        // Keep original behavior
    }

    @Override
    public void onFileCreate(final File file) {
        // Keep original behavior
    }

    @Override
    public void onFileDelete(final File file) {
        // Keep original behavior
    }

    @Override
    public void onStart(final FileAlterationObserver observer) {
        // Keep original behavior
    }

    @Override
    public void onStop(final FileAlterationObserver observer) {
        // Keep original behavior
    }
}