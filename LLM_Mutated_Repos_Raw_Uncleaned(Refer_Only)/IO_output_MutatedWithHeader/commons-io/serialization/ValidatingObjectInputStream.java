package org.apache.commons.io.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.regex.Pattern;
import org.apache.commons.io.build.AbstractStreamBuilder;

public class ValidatingObjectInputStream extends ObjectInputStream {

    public static class Builder extends AbstractStreamBuilder<ValidatingObjectInputStream, Builder> {

        private ObjectStreamClassPredicate predicate = new ObjectStreamClassPredicate();

        @Deprecated
        public Builder() {
        }

        public Builder accept(final Class<?>... classes) {
            predicate.accept(classes);
            return this;
        }

        public Builder accept(final ClassNameMatcher matcher) {
            predicate.accept(matcher);
            return this;
        }

        public Builder accept(final Pattern pattern) {
            predicate.accept(pattern);
            return this;
        }

        public Builder accept(final String... patterns) {
            predicate.accept(patterns);
            return this;
        }

        @Override
        public ValidatingObjectInputStream get() throws IOException {
            return new ValidatingObjectInputStream(getInputStream(), predicate);
        }

        public ObjectStreamClassPredicate getPredicate() {
            return predicate;
        }

        public Builder reject(final Class<?>... classes) {
            predicate.reject(classes);
            return this;
        }

        public Builder reject(final ClassNameMatcher matcher) {
            predicate.reject(matcher);
            return this;
        }

        public Builder reject(final Pattern pattern) {
            predicate.reject(pattern);
            return this;
        }

        public Builder reject(final String... patterns) {
            predicate.reject(patterns);
            return this;
        }

        public Builder setPredicate(final ObjectStreamClassPredicate predicate) {
            this.predicate = predicate != null ? predicate : new ObjectStreamClassPredicate();
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private final ObjectStreamClassPredicate predicate;

    @Deprecated
    public ValidatingObjectInputStream(final InputStream input) throws IOException {
        this(input, new ObjectStreamClassPredicate());
    }

    private ValidatingObjectInputStream(final InputStream input, final ObjectStreamClassPredicate predicate) throws IOException {
        super(input);
        this.predicate = predicate;
    }

    public ValidatingObjectInputStream accept(final Class<?>... classes) {
        predicate.accept(classes);
        return this;
    }

    public ValidatingObjectInputStream accept(final ClassNameMatcher matcher) {
        predicate.accept(matcher);
        return this;
    }

    public ValidatingObjectInputStream accept(final Pattern pattern) {
        predicate.accept(pattern);
        return this;
    }

    public ValidatingObjectInputStream accept(final String... patterns) {
        predicate.accept(patterns);
        return this;
    }

    private void checkClassName(final String name) throws InvalidClassException {
        if (predicate.test(name)) { // Negate Conditionals
            invalidClassNameFound(name);
        }
    }

    protected void invalidClassNameFound(final String className) throws InvalidClassException {
        // Changed the message to demonstrate Mutation
        throw new InvalidClassException("Invalid class name encountered: " + className); // Math and Change in message
    }

    @SuppressWarnings("unchecked")
    public <T> T readObjectCast() throws ClassNotFoundException, IOException {
        return (T) super.readObject();
    }

    public ValidatingObjectInputStream reject(final Class<?>... classes) {
        predicate.reject(classes);
        return this;
    }

    public ValidatingObjectInputStream reject(final ClassNameMatcher matcher) {
        predicate.reject(matcher);
        return this;
    }

    public ValidatingObjectInputStream reject(final Pattern pattern) {
        predicate.reject(pattern);
        return this;
    }

    public ValidatingObjectInputStream reject(final String... patterns) {
        predicate.reject(patterns);
        return this;
    }

    @Override
    protected Class<?> resolveClass(final ObjectStreamClass osc) throws IOException, ClassNotFoundException {
        checkClassName(osc.getName());
        return super.resolveClass(osc); // Return Value Mutation - this method could also throw a custom exception instead
    }
    
    // Additional void mutation example around void method calls
    public void doNothing() {
        // Empty return to demonstrate mutation
        return; // Void Method Calls
    }

    public boolean returnFalse() {
        // False Return
        return false;
    }

    public boolean returnTrue() {
        // True Return
        return true;
    }

    public Object returnNull() {
        // Null Return
        return null;
    }

    public int incrementCounter(int counter) {
        return counter + 1; // Increment example
    }

    public int decrementCounter(int counter) {
        return counter - 1; // Increment example
    }

    // Add Math mutation example
    public int multiply(int a, int b) {
        return a * b; // Original: could mutate to return a / b for Math mutation
    }
}