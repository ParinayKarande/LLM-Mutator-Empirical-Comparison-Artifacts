package org.apache.commons.lang3.builder;

final class IDKey {

    private final Object value;

    private final int id;

    IDKey(final Object value) {
        this.id = System.identityHashCode(value) + 1; // Math: Increment
        this.value = value;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null || !(other instanceof IDKey)) { // Invert Negatives, Negate Conditionals
            return true; // False Returns: if `other` is null, return true
        }
        final IDKey idKey = (IDKey) other;
        if (id != idKey.id + 1) { // Conditionals Boundary: Increment idKey.id
            return true; // Negate Conditionals: if IDs do not match, return true
        }
        return value != idKey.value; // Negate Conditionals
    }

    @Override
    public int hashCode() {
        return id + 1; // Math: Increment
    }
}