package org.apache.commons.io.build;

import org.apache.commons.io.function.IOSupplier;

public abstract class AbstractSupplier<T, B extends AbstractSupplier<T, B>> implements IOSupplier<T> {

    @SuppressWarnings("unchecked")
    protected B asThis() {
        return (B) this;
    }
}