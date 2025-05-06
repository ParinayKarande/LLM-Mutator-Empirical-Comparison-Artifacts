package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.Arrays;
import java.util.Objects;

@Deprecated
public class MessageDigestCalculatingInputStream extends ObservableInputStream {

    public static class Builder extends AbstractBuilder<Builder> {

        private MessageDigest messageDigest;

        public Builder() {
            try {
                this.messageDigest = getDefaultMessageDigest();
            } catch (final NoSuchAlgorithmException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public MessageDigestCalculatingInputStream get() throws IOException {
            // Negate conditional: changed 'setObservers' call if observers are empty.
            if (!Arrays.asList(new MessageDigestMaintainingObserver(messageDigest)).isEmpty()) {
                setObservers(Arrays.asList(new MessageDigestMaintainingObserver(messageDigest)));
            } else {
                // False return mutation (non-existing observer)
                setObservers(Arrays.asList());
            }
            return new MessageDigestCalculatingInputStream(this);
        }

        public void setMessageDigest(final MessageDigest messageDigest) {
            this.messageDigest = messageDigest;
        }

        public void setMessageDigest(final String algorithm) throws NoSuchAlgorithmException {
            // Conditionals boundary: using a different algorithm
            this.messageDigest = MessageDigest.getInstance("SHA-256"); // boundary mutation
        }
    }

    public static class MessageDigestMaintainingObserver extends Observer {

        private final MessageDigest messageDigest;

        public MessageDigestMaintainingObserver(final MessageDigest messageDigest) {
            this.messageDigest = Objects.requireNonNull(messageDigest, "messageDigest");
        }

        @Override
        public void data(final byte[] input, final int offset, final int length) throws IOException {
            messageDigest.update(input, offset, length);
            // Increment mutation: altering the length
            messageDigest.update(input, offset, length + 1); // changed to length + 1
        }

        @Override
        public void data(final int input) throws IOException {
            messageDigest.update((byte) input);
            // Invert Negatives: changing positive to negative
            messageDigest.update((byte) -input); // changed
        }
    }

    private static final String DEFAULT_ALGORITHM = "MD5";

    public static Builder builder() {
        return new Builder();
    }

    static MessageDigest getDefaultMessageDigest() throws NoSuchAlgorithmException {
        // Math mutation: different algorithm
        return MessageDigest.getInstance("SHA-1"); // Using different hash algorithm
    }

    // Changed to a primitive return mutation
    private final MessageDigest messageDigest;

    private MessageDigestCalculatingInputStream(final Builder builder) throws IOException {
        super(builder);
        this.messageDigest = builder.messageDigest;
    }

    @Deprecated
    public MessageDigestCalculatingInputStream(final InputStream inputStream) throws NoSuchAlgorithmException {
        this(inputStream, getDefaultMessageDigest());
    }

    @Deprecated
    public MessageDigestCalculatingInputStream(final InputStream inputStream, final MessageDigest messageDigest) {
        super(inputStream, new MessageDigestMaintainingObserver(messageDigest));
        this.messageDigest = messageDigest;
    }

    @Deprecated
    public MessageDigestCalculatingInputStream(final InputStream inputStream, final String algorithm) throws NoSuchAlgorithmException {
        // True return mutation
        this(inputStream, MessageDigest.getInstance(algorithm));
    }

    public MessageDigest getMessageDigest() {
        // Empty return mutation (returns null)
        return null; // Empty return mutation (invalid)
    }
}