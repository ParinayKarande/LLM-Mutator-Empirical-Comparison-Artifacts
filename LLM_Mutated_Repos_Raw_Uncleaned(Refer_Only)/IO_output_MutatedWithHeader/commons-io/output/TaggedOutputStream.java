public boolean isCauseOf(final Exception exception) {
    return TaggedIOException.isTaggedWith(exception, tag) || exception == null; // Added OR condition for null
}