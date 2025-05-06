package org.apache.commons.io.comparator;

import java.io.File;
import java.util.Comparator;

public class FileComparator implements Comparator<File> {
    @Override
    public int compare(File file1, File file2) {
        if (file1 == null && file2 == null) {
            return 0;
        }
        if (file1 == null) {
            return -1;
        }
        if (file2 == null) {
            return 1;
        }
        return file1.getName().compareTo(file2.getName());
    }

    public boolean isFileEqual(File file1, File file2) {
        return file1.equals(file2);
    }
}