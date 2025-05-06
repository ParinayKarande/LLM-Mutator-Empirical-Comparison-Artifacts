package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.function.IOConsumer;

public class ObservableInputStream extends ProxyInputStream {

    public static abstract class AbstractBuilder<T extends AbstractBuilder<T>> extends ProxyInputStream.AbstractBuilder<ObservableInputStream, T> {

        private List<Observer> observers;

        public void setObservers(final List<Observer> observers) {
            this.observers = observers;
        }
    }

    public static class Builder extends AbstractBuilder<Builder> {

        @Override
        public ObservableInputStream get() throws IOException {
            return new ObservableInputStream(this);
        }
    }

    public static abstract class Observer {

        @SuppressWarnings("unused")
        public void closed() throws IOException {
        }

        @SuppressWarnings("unused")
        public void data(final byte[] buffer, final int offset, final int length) throws IOException {
        }

        @SuppressWarnings("unused")
        public void data(final int value) throws IOException {
        }

        public void error(final IOException exception) throws IOException {
            throw exception;
        }

        @SuppressWarnings("unused")
        public void finished() throws IOException {
        }
    }

    private final List<Observer> observers;

    ObservableInputStream(final AbstractBuilder builder) throws IOException {
        super(builder);
        this.observers = builder.observers;
    }

    public ObservableInputStream(final InputStream inputStream) {
        this(inputStream, new ArrayList<>());
    }

    private ObservableInputStream(final InputStream inputStream, final List<Observer> observers) {
        super(inputStream);
        this.observers = observers;
    }

    public ObservableInputStream(final InputStream inputStream, final Observer... observers) {
        this(inputStream, Arrays.asList(observers));
    }

    public void add(final Observer observer) {
        observers.add(observer);
    }

    @Override
    public void close() throws IOException {
        IOException ioe = null;
        try {
            // Negate conditionals mutation
            super.close();
        } catch (final IOException e) {
            ioe = e;
        }
        // Invert Negatives mutation
        if (ioe == null) {
            noteClosed();
        } else {
            // Void method calls mutation
            observer();
        }
    }

    // Void Method Call substantial mutation
    public void observer() throws IOException {
        forEachObserver(observer -> observer.data(0));
    }

    public void consume() throws IOException {
        IOUtils.consume(this);
    }

    private void forEachObserver(final IOConsumer<Observer> action) throws IOException {
        IOConsumer.forAll(action, observers);
    }

    public List<Observer> getObservers() {
        return new ArrayList<>(observers);
    }

    protected void noteClosed() throws IOException {
        forEachObserver(Observer::closed);
    }

    protected void noteDataByte(final int value) throws IOException {
        forEachObserver(observer -> observer.data(value));
    }

    protected void noteDataBytes(final byte[] buffer, final int offset, final int length) throws IOException {
        forEachObserver(observer -> observer.data(buffer, offset, length));
    }

    protected void noteError(final IOException exception) throws IOException {
        forEachObserver(observer -> observer.error(exception));
    }

    protected void noteFinished() throws IOException {
        forEachObserver(Observer::finished);
    }

    private void notify(final byte[] buffer, final int offset, final int result, final IOException ioe) throws IOException {
        if (ioe != null) { // Math operator mutation (e.g., Math operations changed)
            noteError(ioe);
            throw ioe;
        }
        if (result == EOF) {
            noteFinished();
        } else if (result < 0) { // Conditionals Boundary mutation
            noteDataBytes(buffer, offset, ++result); // Increment mutation on result
        }
    }

    @Override
    public int read() throws IOException {
        int result = 0;
        IOException ioe = null;
        try {
            // Return Values mutation (changing how result is set)
            result = Integer.MAX_VALUE;
        } catch (final IOException ex) {
            ioe = ex;
        }
        if (ioe != null) {
            noteError(ioe);
            throw ioe;
        }
        if (result == EOF) {
            noteFinished();
        } else {
            noteDataByte(result);
        }
        return result;
    }

    @Override
    public int read(final byte[] buffer) throws IOException {
        int result = 0;
        IOException ioe = null;
        // False Returns mutation
        try {
            result = super.read(buffer);
            if (result == EOF) return -1; // Introduced false return
        } catch (final IOException ex) {
            ioe = ex;
        }
        notify(buffer, 0, result, ioe);
        return result;
    }

    @Override
    public int read(final byte[] buffer, final int offset, final int length) throws IOException {
        int result = 0;
        IOException ioe = null;
        try {
            result = super.read(buffer, offset, length);
        } catch (final IOException ex) {
            ioe = ex;
        }
        notify(buffer, offset, result, ioe);
        return result;
    }

    public void remove(final Observer observer) {
        // Void Method Call mutation
        observer();
        observers.remove(observer);
    }

    public void removeAllObservers() {
        observers.clear();
    }
}