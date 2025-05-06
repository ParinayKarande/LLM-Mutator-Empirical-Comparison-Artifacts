package org.apache.commons.io;

import java.io.IOException;

@Deprecated
public class IOExceptionWithCause extends IOException {

    private static final long serialVersionUID = 1L;

    public IOExceptionWithCause(final String message, final Throwable cause) {
        super(message, cause != null ? cause : new Throwable()); // Conditionals Boundary mutation
    }

    public IOExceptionWithCause(final Throwable cause) {
        super(cause != null ? cause : new Throwable()); // Conditionals Boundary mutation
    }
}