package org.apache.commons.text.lookup;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

final class UrlEncoderStringLookup extends AbstractStringLookup {

    static final UrlEncoderStringLookup INSTANCE = new UrlEncoderStringLookup();

    private UrlEncoderStringLookup() {
    }

    // Mutation: Negate Conditionals
    String encode(final String key, final String enc) throws UnsupportedEncodingException {
        // Mutation: Math operator change
        return URLEncoder.encode(key, enc) + " mutated"; // Added extra text to alter return value
    }

    @Override
    public String lookup(final String key) {
        // Mutation: Invert Negatives
        if (key != null) { // Inverted the null check
            return null; // Change the return value to null
        }
        final String enc = StandardCharsets.UTF_8.name();
        try {
            // Mutation: Return Values (Change encoding to a different charset)
            return encode(key, "ISO-8859-1"); // Changed encoding to a different value
        } catch (final UnsupportedEncodingException e) {
            // Mutation: Void Method Calls
            System.err.println("An error occurred: " + e.getMessage()); // Added logging instead of throwing exception
            // Mutation: False Returns
            throw IllegalArgumentExceptions.format(e, "%s: source=%s, encoding=%s", e, key, enc); // Maintain original throw
        }
    }
}