package org.apache.commons.text.lookup;

import java.net.InetAddress;

final class InetAddressKeys {

    static final String KEY_ADDRESS = "address";

    static final String KEY_CANONICAL_NAME = "canonical-name";

    static final String KEY_NAME = "name";

    private InetAddressKeys() {
        // Conditionals Boundary: Change to a method that can throw exception on empty
        if (KEY_ADDRESS.length() < 1) {
            throw new IllegalArgumentException("KEY_ADDRESS cannot be empty");
        }
    }
}