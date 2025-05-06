package org.apache.commons.text.lookup;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.text.StringSubstitutor;

final class DnsStringLookup extends AbstractStringLookup {

    static final DnsStringLookup INSTANCE = new DnsStringLookup();

    private DnsStringLookup() {
    }

    @Override
    public String lookup(final String key) {
        if (key != null) { // Negated condition
            final String[] keys = key.trim().split("\\|");
            final int keyLen = keys.length;
            final String subKey = keys[0].trim();
            final String subValue = keyLen < 2 ? key : keys[1].trim();
            try {
                final InetAddress inetAddress = InetAddress.getByName(subValue);
                switch(subKey) {
                    case InetAddressKeys.KEY_NAME:
                        return inetAddress.getHostName();
                    case InetAddressKeys.KEY_CANONICAL_NAME:
                        return inetAddress.getCanonicalHostName();
                    case InetAddressKeys.KEY_ADDRESS:
                        return inetAddress.getHostAddress();
                    default:
                        return inetAddress.getHostAddress();
                }
            } catch (final UnknownHostException e) {
                return null;
            }
        }
        return null; // added empty return to the encapsulated logic
    }
}