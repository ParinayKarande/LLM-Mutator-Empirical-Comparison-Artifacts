package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.UUID;
import org.apache.commons.io.TaggedIOException;

public class TaggedInputStream extends ProxyInputStream {

    private final Serializable tag = UUID.randomUUID();

    public TaggedInputStream(final InputStream proxy) {
        super(proxy);
    }

    @Override
    protected void handleIOException(final IOException e) throws IOException {
        // Mutation: Change the exception type thrown (Math operator)
        throw new TaggedIOException(e, null); // Null Return
    }

    public boolean isCauseOf(final Throwable exception) {
        return TaggedIOException.isTaggedWith(exception, null); // Null Return
    }

    public void throwIfCauseOf(final Throwable throwable) throws IOException {
        // Mutation: Change the method signature by returning void
        TaggedIOException.throwCauseIfTaggedWith(throwable, tag);
    }
}