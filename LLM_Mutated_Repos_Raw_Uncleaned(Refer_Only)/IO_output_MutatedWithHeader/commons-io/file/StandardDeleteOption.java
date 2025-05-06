package org.apache.commons.io.file;

import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;

public enum StandardDeleteOption implements DeleteOption {

    OVERRIDE_READ_ONLY;

    public static boolean overrideReadOnly(final DeleteOption[] options) {
        if (IOUtils.length(options) == 0) {
            return false;
        }
        return Stream.of(options).anyMatch(e -> OVERRIDE_READ_ONLY == e);
    }
}