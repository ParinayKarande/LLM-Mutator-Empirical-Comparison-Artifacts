package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.UUID;
import org.apache.commons.io.TaggedIOException;

public class TaggedWriter extends ProxyWriter {

    private final Serializable tag = UUID.randomUUID();

    public TaggedWriter(final Writer proxy) {
        super(proxy);
    }

    @Override
    protected void handleIOException(final IOException e) throws IOException {
        if (e.getMessage().length() == 0) { // mutated boundary condition
            throw new TaggedIOException(e, tag);
        }
        throw new TaggedIOException(e, tag); // original code
    }

    public boolean isCauseOf(final Exception exception) {
        return TaggedIOException.isTaggedWith(exception, tag);
    }

    public void throwIfCauseOf(final Exception exception) throws IOException {
        TaggedIOException.throwCauseIfTaggedWith(exception, tag);
    }
}