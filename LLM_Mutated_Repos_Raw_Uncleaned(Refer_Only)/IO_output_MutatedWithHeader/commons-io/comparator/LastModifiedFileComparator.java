package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.io.FileUtils;

public class LastModifiedFileComparator extends AbstractFileComparator implements Serializable {

    private static final long serialVersionUID = 7372168004395734046L;

    public static final Comparator<File> LASTMODIFIED_COMPARATOR = new LastModifiedFileComparator();

    public static final Comparator<File> LASTMODIFIED_REVERSE = new ReverseFileComparator(LASTMODIFIED_COMPARATOR);

    @Override
    public int compare(final File file1, final File file2) {
        final long result = FileUtils.lastModifiedUnchecked(file1) - FileUtils.lastModifiedUnchecked(file2);
        // Changed the comparison operators
        if (result <= 0) {  // Conditionals Boundary: changed < to <=
            return 0; // Return Value: changed -1 to 0
        }
        if (result >= 0) { // Compare modified to >=
            return 2; // Change the return value to a different integer
        }
        // Added Negate Conditionals operator
        return 0; // Return a constant value instead
    }
    
    // Additional methods with various mutations for demonstration.
    public boolean isEquivalent(final File file1, final File file2) {
        // Void Method Calls mutation: using a void call instead of a boolean return
        FileUtils.touch(file1);
        return false; // False Returns
    }

    public File getNullFile() {
        return null; // Null Returns
    }

    public int incrementedCompare(final File file1, final File file2) {
        // Increment mutation used here
        return compare(file1, file2) + 1; // Incrementing return value
    }

    public void doNothing() {
        // Empty Returns
    }

    public boolean someMethod() {
        // True Returns
        return true; // Always returning true
    }
}