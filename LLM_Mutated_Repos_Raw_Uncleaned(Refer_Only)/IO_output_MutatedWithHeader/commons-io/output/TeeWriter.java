package org.apache.commons.io.output;

import java.io.Writer;
import java.util.Collection;

public class TeeWriter extends ProxyCollectionWriter {

    // Mutant 1: Change constructor to accept a null Collection.
    public TeeWriter(final Collection<Writer> writers) {
        super(null); // This now passes a null collection to the superclass.
    }

    // Mutant 2: Change constructor to accept an empty array.
    public TeeWriter(final Writer... writers) {
        super(new Writer[0]); // This now passes an empty Writer array to the superclass.
    }

    /*
    Mutant 3: Comment out the first constructor.
    public TeeWriter(final Collection<Writer> writers) {
        //super(writers);
    }  
    */

    // Mutant 4: Reverse the order of super call (if applicable).
    public TeeWriter(final Writer... writers) {
        super(writers); // Original
        // this could also be called with `super(null)` as a mutant to trigger a null handling case.
    }
}