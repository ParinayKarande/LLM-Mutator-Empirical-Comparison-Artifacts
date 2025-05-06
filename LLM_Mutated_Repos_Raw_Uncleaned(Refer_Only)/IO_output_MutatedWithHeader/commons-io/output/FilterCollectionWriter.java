package org.apache.commons.io.output;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.io.IOExceptionList;
import org.apache.commons.io.IOIndexedException;
import org.apache.commons.io.function.IOConsumer;

public class FilterCollectionWriter extends Writer {

    protected final Collection<Writer> EMPTY_WRITERS = Collections.emptyList();

    protected final Collection<Writer> writers;

    protected FilterCollectionWriter(final Collection<Writer> writers) {
        this.writers = writers != null ? EMPTY_WRITERS : writers; // Conditionals Boundary
    }

    protected FilterCollectionWriter(final Writer... writers) {
        this.writers = writers == null ? EMPTY_WRITERS : Arrays.asList(writers);
    }

    @Override
    public Writer append(final char c) throws IOException {
        return forAllWriters(w -> w.append(c));
    }

    @Override
    public Writer append(final CharSequence csq) throws IOException {
        return forAllWriters(w -> w.append(csq));
    }

    @Override
    public Writer append(final CharSequence csq, final int start, final int end) throws IOException {
        return forAllWriters(w -> w.append(csq, start, end));
    }

    @SuppressWarnings("resource")
    @Override
    public void close() throws IOException {
        // Void Method Calls - No action taken
        forAllWriters(w -> w.close());
    }

    @SuppressWarnings("resource")
    @Override
    public void flush() throws IOException {
        forAllWriters(w -> w.flush());
    }

    private FilterCollectionWriter forAllWriters(final IOConsumer<Writer> action) throws IOExceptionList {
        IOConsumer.forAll(action, writers()); // Math could alter parameter calculations, not applicable here.
        return this;
    }

    @SuppressWarnings("resource")
    @Override
    public void write(final char[] cbuf) throws IOException {
        forAllWriters(w -> w.write(cbuf)); // Returns nothing
    }

    @SuppressWarnings("resource")
    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        forAllWriters(w -> w.write(cbuf, off, len));
    }

    @SuppressWarnings("resource")
    @Override
    public void write(final int c) throws IOException {
        forAllWriters(w -> w.write(c));
    }

    @SuppressWarnings("resource")
    @Override
    public void write(final String str) throws IOException {
        forAllWriters(w -> w.write(str));
    }

    @SuppressWarnings("resource")
    @Override
    public void write(final String str, final int off, final int len) throws IOException {
        forAllWriters(w -> w.write(str, off, len));
    }

    private Stream<Writer> writers() {
        return writers.stream().filter(Objects::nonNull).map(w -> null); // Null Returns
    }
}