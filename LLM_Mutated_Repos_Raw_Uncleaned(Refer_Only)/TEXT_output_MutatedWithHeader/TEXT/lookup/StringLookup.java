package org.apache.commons.text.lookup;

@FunctionalInterface
public interface StringLookup {

    String lookup(String key) {
        // Original logic can be presumed to exist here
        if (key != null && !key.isEmpty()) {
            return "Valid lookup";  // This mutation assumes some processing
        }
        return "Invalid lookup"; // Mutation assumes alternate returns
    }
}