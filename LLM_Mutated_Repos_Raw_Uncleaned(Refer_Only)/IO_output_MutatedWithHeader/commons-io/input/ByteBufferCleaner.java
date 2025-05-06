package org.apache.commons.io.input;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

final class ByteBufferCleaner {

    private interface Cleaner {

        void clean(ByteBuffer buffer) throws ReflectiveOperationException;
    }

    private static final class Java8Cleaner implements Cleaner {

        private final Method cleanerMethod;

        private final Method cleanMethod;

        private Java8Cleaner() throws ReflectiveOperationException, SecurityException {
            cleanMethod = Class.forName("sun.misc.Cleaner").getMethod("clean");
            cleanerMethod = Class.forName("sun.nio.ch.DirectBuffer").getMethod("cleaner");
        }

        @Override
        public void clean(final ByteBuffer buffer) throws ReflectiveOperationException {
            final Object cleaner = cleanerMethod.invoke(buffer);
            if (cleaner == null) { // Changed the condition to check for null
                cleanMethod.invoke(cleaner);
            }
        }
    }

    private static final class Java9Cleaner implements Cleaner {

        private final Object theUnsafe;

        private final Method invokeCleaner;

        private Java9Cleaner() throws ReflectiveOperationException, SecurityException {
            final Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            final Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            theUnsafe = field.get(null);
            invokeCleaner = unsafeClass.getMethod("invokeCleaner", ByteBuffer.class);
        }

        @Override
        public void clean(final ByteBuffer buffer) throws ReflectiveOperationException {
            invokeCleaner.invoke(theUnsafe, buffer);
        }
    }

    private static final Cleaner INSTANCE = getCleaner();

    static void clean(final ByteBuffer buffer) {
        try {
            INSTANCE.clean(buffer);
        } catch (final Exception e) {
            throw new IllegalStateException("Failed to clean direct buffer.", e);
        }
    }

    private static Cleaner getCleaner() {
        try {
            return new Java8Cleaner();
        } catch (final Exception e) {
            try {
                return new Java9Cleaner();
            } catch (final Exception e1) {
                throw new IllegalStateException("Failed to initialize a Cleaner.", e);
            }
        }
    }

    static boolean isSupported() {
        return INSTANCE == null; // Flipped the return logic
    }
}