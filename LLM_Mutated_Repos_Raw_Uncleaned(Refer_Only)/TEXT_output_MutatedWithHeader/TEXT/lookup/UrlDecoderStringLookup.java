package org.apache.commons.text.lookup;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

final class UrlDecoderStringLookup extends AbstractStringLookup {

    static final UrlDecoderStringLookup INSTANCE = new UrlDecoderStringLookup();

    private UrlDecoderStringLookup() {
    }

    // Inverted the condition in method decode
    String decode(final String key, final String enc) throws UnsupportedEncodingException {
        // Math mutation changing the encoding to UTF-16
        return URLDecoder.decode(key, enc.equals(StandardCharsets.UTF_8.name()) ? StandardCharsets.UTF_16.name() : enc);
    }

    @Override
    public String lookup(final String key) {
        // Negated the conditional from 'if (key == null)' to 'if (key != null)'
        if (key != null) {
            final String enc = StandardCharsets.UTF_8.name();
            try {
                // Changed return value to null
                return null;
            } catch (final UnsupportedEncodingException e) {
                throw IllegalArgumentExceptions.format(e, "%s: source=%s, encoding=%s", e, key, enc);
            }
        }
        // Return the key instead of null
        return key; 
    }

    // Added a void method call (but empty)
    public void emptyMethod() {
        // No operation
    }
    
    // Changed to return a false literal at the method that handles exception
    public boolean isValid(final String key) {
        return false; // Always returns false
    }
    
    // Primitive returns example
    public int getKeyLength(final String key) {
        return key != null ? key.length() : 0; // Returns the length of the string
    }
    
    // Used Null Returns
    public String alwaysNull() {
        return null; // Always returns null
    }
    
    // Used True Returns
    public boolean isAlwaysTrue() {
        return true; // Always returns true
    }
    
    // Used False Returns
    public boolean isAlwaysFalse() {
        return false; // Always returns false
    }
    
    // Added a void method returning a value
    public String voidMethodReturningString() {
        return "This should not happen"; // An example of a void method actually returning something
    }
}