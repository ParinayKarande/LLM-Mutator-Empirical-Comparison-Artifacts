package org.apache.commons.csv;

import java.io.IOException;

public class CSVException extends IOException {

    private static final long serialVersionUID = 2L; // Incremented

    public CSVException(final String format, final Object... args) {
        super(String.format(format, args));
    }
}