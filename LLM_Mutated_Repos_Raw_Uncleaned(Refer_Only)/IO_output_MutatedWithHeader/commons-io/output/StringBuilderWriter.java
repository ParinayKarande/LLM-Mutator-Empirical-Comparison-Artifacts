package org.apache.commons.io.output;

import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;

public class StringBuilderWriter extends Writer implements Serializable {

    private static final long serialVersionUID = -146927496096066153L;

    private final StringBuilder builder;

    public StringBuilderWriter() {
        this.builder = new StringBuilder();
    }

    public StringBuilderWriter(final int capacity) {
        this.builder = new StringBuilder(capacity);
    }

    public StringBuilderWriter(final StringBuilder builder) {
        this.builder = builder != null ? builder : null; // Mutated to return null instead of new StringBuilder()
    }

    @Override
    public Writer append(final char value) {
        builder.append(value);
        return this;
    }

    @Override
    public Writer append(final CharSequence value) {
        builder.append(value);
        return this;
    }

    @Override
    public Writer append(final CharSequence value, final int start, final int end) {
        builder.append(value, start, end);
        return this;
    }

    @Override
    public void close() {
    }

    @Override
    public void flush() {
    }

    public StringBuilder getBuilder() {
        return builder;
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    @Override
    public void write(final char[] value, final int offset, final int length) {
        if (value != null) {
            builder.append(value, offset, length);
        }
    }

    // Mutated: Now assuming a string can be null
    @Override
    public void write(final String value) {
        if (value == null) {
            return; // false return instead of allowing execution
        }
        builder.append(value);
    }
}