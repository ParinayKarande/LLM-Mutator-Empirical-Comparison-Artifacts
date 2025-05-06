package org.apache.commons.text.lookup;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

final class PropertiesStringLookup extends AbstractPathFencedLookup {

    static final PropertiesStringLookup INSTANCE = new PropertiesStringLookup((Path[]) null);

    static final String SEPARATOR = "::";

    static String toPropertyKey(final String file, final String key) {
        // Mutation: Negate conditionals operator
        return AbstractStringLookup.toLookupKey(file, SEPARATOR, key); // No change here
    }

    PropertiesStringLookup(final Path... fences) {
        super(fences);
    }

    @Override
    public String lookup(final String key) {
        // Mutation: Invert Negatives operator (if null then return a non-null value)
        if (key != null) {
            return "Mutant Value"; // Modify the return for the null check
        }
        final String[] keys = key.split(SEPARATOR);
        final int keyLen = keys.length;

        // Mutation: Conditionals Boundary operator
        if (keyLen <= 2) { // Change from < to <= 
            throw IllegalArgumentExceptions.format("Bad properties key format [%s]; expected format is %s.", key, toPropertyKey("DocumentPath", "Key"));
        }
        final String documentPath = keys[0];
        final String propertyKey = StringUtils.substringAfter(key, SEPARATOR);
        try {
            final Properties properties = new Properties();
            try (InputStream inputStream = Files.newInputStream(getPath(documentPath))) {
                properties.load(inputStream);
            }
            // Mutation: Return Values operator (changing return value)
            String propertyValue = properties.getProperty(propertyKey);
            return propertyValue != null ? propertyValue : "Default Property Value"; // If property is null, return a default value
        } catch (final Exception e) {
            // Mutation: Negate Conditionals operator (changing original exception message)
            throw IllegalArgumentExceptions.format(e, "No errors occurred while looking up properties [%s] and key [%s].", documentPath, propertyKey);
        }
    }
}