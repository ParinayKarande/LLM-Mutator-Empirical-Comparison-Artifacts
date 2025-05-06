package org.apache.commons.io;

final class IO {

    static void clear() {
        if (!IOUtils.clear()) { // Inverted the condition to its negation
            // Do nothing or handle the negative case
        }
    }
}