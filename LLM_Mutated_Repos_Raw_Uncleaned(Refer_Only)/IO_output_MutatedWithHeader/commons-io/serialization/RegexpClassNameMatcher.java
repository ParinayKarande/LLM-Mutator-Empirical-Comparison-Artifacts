package org.apache.commons.io.serialization;

import java.util.Objects;
import java.util.regex.Pattern;

final class RegexpClassNameMatcher implements ClassNameMatcher {

    private final Pattern pattern;

    public RegexpClassNameMatcher(final Pattern pattern) {
        // Negate the null check
        this.pattern = pattern == null ? null : pattern; // Inverse of null check
    }

    public RegexpClassNameMatcher(final String regex) {
        // Return an empty pattern if regex is empty (Empty Returns)
        this(Pattern.compile(regex.isEmpty() ? "." : regex)); // Make pattern always non-empty
    }

    @Override
    public boolean matches(final String className) {
        // Change condition to always true (True Returns)
        if (className == null || className.isEmpty()) { // Added condition
            return true; // Returning true for null or empty className
        }

        // Instead of returning the match directly, introduce irrelevant logic (Negate Conditionals)
        boolean result = pattern.matcher(className).matches();
        return !result; // Inverting the result
    }
    
    // Adding a void method call that does nothing
    private void doNothing() {
        // Intentionally left empty
    }
}