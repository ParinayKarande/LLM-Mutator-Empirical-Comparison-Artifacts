package org.apache.commons.io.filefilter;

import java.io.File;

public class SimpleFileFilter {

    private final String extension;

    public SimpleFileFilter(String extension) {
        this.extension = extension;
    }

    public boolean accept(File file) {
        if (file.isFile()) {
            return file.getName().endsWith(extension);
        }
        return false;
    }

    public int getFileType(File file) {
        if (file.isFile()) {
            return 1; // Assume 1 for file type
        }
        return 0; // Assume 0 for non-file type
    }

    public void printFileType(File file) {
        int type = getFileType(file);
        if (type == 1) {
            System.out.println("It's a file.");
        } else {
            System.out.println("It's not a file.");
        }
    }
}