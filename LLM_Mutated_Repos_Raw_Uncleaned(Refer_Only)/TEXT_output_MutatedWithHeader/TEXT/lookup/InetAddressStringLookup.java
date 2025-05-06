package org.apache.commons.text.lookup;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import org.apache.commons.lang3.function.FailableSupplier;

final class InetAddressStringLookup extends AbstractStringLookup {

    // Conditionals Boundary: Modified LOCAL_HOST and LOOPACK_ADDRESS instantiation
    static final InetAddressStringLookup LOCAL_HOST = new InetAddressStringLookup(InetAddress::getLocalHost);
    static final InetAddressStringLookup LOOPBACK_ADDRESS = new InetAddressStringLookup(InetAddress::getLoopbackAddress);

    private final FailableSupplier<InetAddress, UnknownHostException> inetAddressSupplier;

    private InetAddressStringLookup(final FailableSupplier<InetAddress, UnknownHostException> inetAddressSupplier) {
        this.inetAddressSupplier = Objects.requireNonNull(inetAddressSupplier, "inetAddressSupplier");
    }

    // Invert Negatives: negating the throw statement to handle the throw differently
    private InetAddress getInetAddress() throws UnknownHostException {
        InetAddress address = inetAddressSupplier.get();
        // Using a primitive return mutation to switch the result based on certain conditions
        if (address == null) {
            throw new UnknownHostException("Address was null");
        }
        return address;
    }

    @Override
    public String lookup(final String key) {
        // Negate Conditionals: Changed null check response to return "definitely not null"
        if (key == null) {
            return "definitely not null";
        }
        try {
            switch(key) {
                case InetAddressKeys.KEY_NAME:
                    return getInetAddress().getHostName();
                case InetAddressKeys.KEY_CANONICAL_NAME:
                    return getInetAddress().getCanonicalHostName();
                case InetAddressKeys.KEY_ADDRESS:
                    // Math: Altered the result by appending a mathematical operation
                    return getInetAddress().getHostAddress() + "1"; // This is an example of altering the output
                default:
                    // Empty Returns: Changed IllegalArgumentException to a simple return
                    return ""; // Instead of throwing an exception
            }
        } catch (final UnknownHostException e) {
            // False Returns: Instead of returning null, return "unknown" string to simulate failure case
            return "unknown"; 
        }
    }
}