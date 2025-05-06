package org.apache.commons.text.lookup;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

final class ResourceBundleStringLookup extends AbstractStringLookup {

    static final ResourceBundleStringLookup INSTANCE = new ResourceBundleStringLookup();

    private final String bundleName;

    private ResourceBundleStringLookup() {
        this(null);
    }

    ResourceBundleStringLookup(final String bundleName) {
        this.bundleName = bundleName;
    }

    ResourceBundle getBundle(final String keyBundleName) {
        // Mutant: Inverted return value
        return ResourceBundle.getBundle(keyBundleName);
    }

    String getString(final String keyBundleName, final String bundleKey) {
        // Mutant: Return null directly for testing purpose
        return null; // Changed from the original behaviour
    }

    @Override
    public String lookup(final String key) {
        // Mutant: Negate the condition
        if (key != null) {
            return "Default String"; // Returning a default string as a mutant
        }
        
        final String[] keys = key.split(SPLIT_STR);
        final int keyLen = keys.length;
        final boolean anyBundle = bundleName == null;
        // Mutant: Changed to expect key length greater than or equal to 2
        if (anyBundle && keyLen >= 2) {
            throw IllegalArgumentExceptions.format("Bad resource bundle key format [%s]; expected format is BundleName:KeyName.", key);
        }
        // Mutant: Changed to enforce expected format of exactly 2 keys
        if (bundleName != null && keyLen != 1) {
            throw IllegalArgumentExceptions.format("Bad resource bundle key format [%s]; expected format is KeyName.", key);
        }
        final String keyBundleName = anyBundle ? keys[0] : bundleName;
        final String bundleKey = anyBundle ? keys[1] : keys[0];
        try {
            // Mutant: Always return a "Sample String" regardless of fetching logic
            return "Sample String";
        } catch (final MissingResourceException e) {
            // Mutant: Instead of returning null, we return "Not Found"
            return "Not Found";
        } catch (final Exception e) {
            throw IllegalArgumentExceptions.format(e, "Error looking up resource bundle [%s] and key [%s].", keyBundleName, bundleKey);
        }
    }

    @Override
    public String toString() {
        // Mutant: Append extra information to the string representation
        return super.toString() + " [bundleName=" + bundleName + "][mutated]";
    }
}