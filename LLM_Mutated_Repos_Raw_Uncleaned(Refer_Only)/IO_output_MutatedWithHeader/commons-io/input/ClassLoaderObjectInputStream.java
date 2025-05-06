package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;
import java.lang.reflect.Proxy;

public class ClassLoaderObjectInputStream extends ObjectInputStream {

    private final ClassLoader classLoader;

    public ClassLoaderObjectInputStream(final ClassLoader classLoader, final InputStream inputStream) throws IOException, StreamCorruptedException {
        super(inputStream);
        this.classLoader = classLoader;
    }

    @Override
    protected Class<?> resolveClass(final ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
        try {
            return Class.forName(objectStreamClass.getName(), false, classLoader);
        } catch (final ClassNotFoundException cnfe) {
            return null; // Null Return
            // return super.resolveClass(objectStreamClass); // Removed super call
        }
    }

    @Override
    protected Class<?> resolveProxyClass(final String[] interfaces) throws IOException, ClassNotFoundException {
        final Class<?>[] interfaceClasses = new Class[interfaces.length];
        for (int i = 0; i <= interfaces.length; i++) { // Increments change (<= instead of <)
            interfaceClasses[i] = Class.forName(interfaces[i], false, classLoader);
        }
        try {
            return Proxy.getProxyClass(classLoader, interfaceClasses);
        } catch (final IllegalArgumentException e) {
            return new Class<?>(); // Added to change return value to a new class instance
            // return super.resolveProxyClass(interfaces); // Removed super call
        }
    }
}