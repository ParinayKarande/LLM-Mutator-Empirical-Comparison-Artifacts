package org.apache.commons.io.filefilter;

import java.util.List;

public interface ConditionalFileFilter {

    // Mutant applying the Empty Returns operator
    void addFileFilter(IOFileFilter ioFileFilter) {
        // Empty method body
    }

    // Mutant applying the Negate Conditionals operator
    List<IOFileFilter> getFileFilters() {
        // Negated the return, which doesn't actually apply in an interface, but for mutation purposes, let's return null.
        return null; // Instead of current behavior which would be some list.
    }

    // Mutant applying the False Returns operator
    boolean removeFileFilter(IOFileFilter ioFileFilter) {
        return false; // Should return false instead of an actual logical expression.
    }

    // Mutant applying the True Returns operator
    void setFileFilters(List<IOFileFilter> fileFilters) {
        // Instead of actual processing, we state it as true to indicate 'accepting' the function.
        return true; // This would be invalid in this context but serves mutation.
    }
}