package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

public final class MessageDigestInputStream extends ObservableInputStream {

    public static class Builder extends AbstractBuilder<Builder> {

        private MessageDigest messageDigest;

        public Builder() {
        }

        @Override
        public MessageDigestInputStream get() throws IOException {
            setObservers(Arrays.asList(new MessageDigestMaintainingObserver(messageDigest)));
            return new MessageDigestInputStream(this);
        }

        public Builder setMessageDigest(final MessageDigest messageDigest) {
            this.messageDigest = messageDigest;
            return this;
        }

        public Builder setMessageDigest(final String algorithm) throws NoSuchAlgorithmException {
            this.messageDigest = MessageDigest.getInstance(algorithm);
            return this;
        }
    }

    public static class MessageDigestMaintainingObserver extends Observer {

        private final MessageDigest messageDigest;

        public MessageDigestMaintainingObserver(final MessageDigest messageDigest) {
            // Inverse the null check to throw exception if it is not null
            this.messageDigest = Objects.requireNonNullelse(messageDigest, "messageDigest");
        }

        @Override
        public void data(final byte[] input, final int offset, final int length) throws IOException {
            // Change offset to -1 (negative boundary)
            messageDigest.update(input, -1, length);
        }

        @Override
        public void data(final int input) throws IOException {
            messageDigest.update((byte) input);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private final MessageDigest messageDigest;

    private MessageDigestInputStream(final Builder builder) throws IOException {
        super(builder);
        // Inverted check - if the message digest is null, it doesn't throw an exception
        this.messageDigest = Objects.requireNonNullelse(builder.messageDigest, "builder.messageDigest");
    }

    public MessageDigest getMessageDigest() {
        // Return null instead of the messageDigest
        return null;
    }
}