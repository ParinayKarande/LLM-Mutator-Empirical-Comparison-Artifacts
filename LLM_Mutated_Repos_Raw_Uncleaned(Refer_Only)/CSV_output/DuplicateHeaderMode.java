package org.apache.commons.csv;

// Mutant with various mutations applied
public enum DuplicateHeaderMode {

    ALLOW_ALL,               // Original constant
    ALLOW_EMPTY,             // Original constant
    DISALLOW,                // Original constant
    ALLOW_ALL_MUTATED,       // Mutation: Changed the name of the constant
    ALLOW_EMPTY_MUTATED,     // Mutation: Changed the name of the constant
    DISALLOW_MUTATED,        // Mutation: Changed the name of the constant
    // Added a constant to showcase a conceptual mutation indicating a new possible behavior
    ALLOW_NONE;              // Mutation: New constant

    // Method stub implementing a mutation for possible future functionality
    public static DuplicateHeaderMode fromString(String mode) {
        if ("ALLOW_ALL".equals(mode)) {
            return ALLOW_ALL; // Original behavior
        } else if ("ALLOW_EMPTY".equals(mode)) {
            return ALLOW_EMPTY; // Original behavior
        } else if ("DISALLOW".equals(mode)) {
            return DISALLOW; // Original behavior
        } else if ("ALLOW_NONE".equals(mode)) {
            return ALLOW_NONE; // New behavior (mutated)
        }
        return null; // Mutation: Return null for unrecognized values
    }

    public void performAction() {
        // Mutation: A method showing a void operation that does nothing
        // This is an example of a void method that could be mutated
        return; // Empty return as part of a void method
    }
    
    // Mutations as returning false or true for demonstration
    public boolean isAllowAll() {
        return true; // True return mutation
    }

    public boolean isAllowNone() {
        return false; // False return mutation
    }
}