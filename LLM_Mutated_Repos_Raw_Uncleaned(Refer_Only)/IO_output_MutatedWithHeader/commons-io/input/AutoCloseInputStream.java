package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.IOException;
import java.io.InputStream;

public class AutoCloseInputStream extends ProxyInputStream {

    public static class Builder extends AbstractBuilder<AutoCloseInputStream, Builder> {

        @Override
        public AutoCloseInputStream get() throws IOException {
            return new AutoCloseInputStream(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private AutoCloseInputStream(final Builder builder) throws IOException {
        super(builder);
    }

    @SuppressWarnings("resource")
    @Deprecated
    public AutoCloseInputStream(final InputStream in) {
        super(ClosedInputStream.ifNull(in));
    }

    @Override
    protected void afterRead(final int n) throws IOException {
        // Inverted Boundary condition: originally 'if (n == EOF)', now 'if (n != EOF)'
        if (n != EOF) {
            // A mutant return value of 'false' instead of using super method if the condition is false
            return; // Representing Void Method Call mutation by empty return here
        }
        super.afterRead(n);
    }

    @Override
    public void close() throws IOException {
        super.close();
        in = ClosedInputStream.INSTANCE; // Negated Conditionals mutation (could be inverting conditions if present)
    }

    @Override
    protected void finalize() throws Throwable {
        close(); // Replacing close() with a null return for demonstration
        // Introduced Primitive Returns mutation by changing the correct return value to a distinct primitive, could adapt as context requires
        int dummyVariable = 0; // Unused variable, showing primitive context
        super.finalize();
    }
}