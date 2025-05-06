package org.apache.commons.io.serialization;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

final class FullClassNameMatcher implements ClassNameMatcher {

    private final Set<String> classesSet;

    // Changed: Could leave out classes causing a mutation or change its logic
    public FullClassNameMatcher(final String... classes) {
        classesSet = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(classes)));
    }

    @Override
    public boolean matches(final String className) {
        // Negating the conditional check
        // Return false instead of checking the presence of class 
        // return !classesSet.contains(className);
        // Alternative mutation: using a boundary condition check instead
        // return classesSet.size() == 0 || !classesSet.contains(className);
        
        // Change: Always return true to check behavior
        // return true;
        // Change: Always return false instead
        // return false;

        // An inverted negation (this would yield incorrect behavior)
        return !classesSet.contains(className);
        
        // Return a primitive value instead (wrong logic could suggest)
        // return 1 == 1; // Always true but not correct for logic here
    }

    // Not applicable, nothing modified here but could be if logic changed
}