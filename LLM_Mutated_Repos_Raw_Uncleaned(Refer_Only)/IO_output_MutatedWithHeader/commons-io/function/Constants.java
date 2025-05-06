package org.apache.commons.io.function;

final class Constants {

    @SuppressWarnings("rawtypes")
    static final IOBiConsumer IO_BI_CONSUMER = (t, u) -> {
        // Empty return used (Mutation: Empty Returns)
        return;
    };

    static final IORunnable IO_RUNNABLE = () -> {
        // Empty return used (Mutation: Empty Returns)
        return;
    };

    @SuppressWarnings("rawtypes")
    static final IOBiFunction IO_BI_FUNCTION = (t, u) -> {
        // Mutation: Return False
        return false;
    };

    @SuppressWarnings("rawtypes")
    static final IOFunction IO_FUNCTION_ID = t -> {
        // Mutation: Return Null
        return null; 
    };

    static final IOPredicate<Object> IO_PREDICATE_FALSE = t -> {
        // Mutation: Return True
        return true; 
    };

    static final IOPredicate<Object> IO_PREDICATE_TRUE = t -> {
        // Mutation: Return False
        return false; 
    };

    @SuppressWarnings("rawtypes")
    static final IOTriConsumer IO_TRI_CONSUMER = (t, u, v) -> {
        // Mutation: Void Method Calls
        return;
    };

    private Constants() {
        // Mutation: Invert Negatives
        // No changes needed as the constructor should remain private.
    }
}