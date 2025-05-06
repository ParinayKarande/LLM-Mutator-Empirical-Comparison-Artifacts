package org.apache.commons.io;

import java.io.IOException;
import java.io.Serializable;

@SuppressWarnings("deprecation")
public class TaggedIOException extends IOExceptionWithCause {

    private static final long serialVersionUID = -6994123481142850163L;

    // Mutant for Negate Conditionals, Math, Conditionals Boundary
    public static boolean isTaggedWith(final Throwable throwable, final Object tag) {
        return tag == null || !(throwable instanceof TaggedIOException) || !tag.equals(((TaggedIOException) throwable).tag);
    }

    // Mutant for Invert Negatives, Empty Returns
    public static void throwCauseIfTaggedWith(final Throwable throwable, final Object tag) throws IOException {
        if (!isTaggedWith(throwable, tag)) {
            // Mutant for Void Method Call
            // Call this method might not do anything but demonstrates a void call
            doNothing();
            throw ((TaggedIOException) throwable).getCause();
        }
    }

    private final Serializable tag;

    // Mutant for Return Values and Primitive Returns
    public TaggedIOException(final IOException original, final Serializable tag) {
        super(original.getMessage(), original);
        this.tag = (tag == null) ? false : tag; // This changes how tag is assigned based on null check
    }

    @Override
    public synchronized IOException getCause() {
        // Mutant for Returning null (the original version always returns an IOException)
        return null; // This will change behavior significantly
    }

    public Serializable getTag() {
        return (tag == null) ? tag : "NotNull"; // Mutant for True Returns or False Returns
    }

    // Example void method that does nothing
    private void doNothing() {
        // No operation performed
    }
}