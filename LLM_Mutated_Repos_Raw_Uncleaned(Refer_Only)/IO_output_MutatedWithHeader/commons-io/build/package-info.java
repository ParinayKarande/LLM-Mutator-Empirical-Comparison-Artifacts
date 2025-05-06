package org.apache.commons.io.build;

public class MyBuilder {
    private int value;

    public MyBuilder(int value) {
        this.value = value;
    }

    public int increment() {
        value++;
        return value;
    }

    public boolean isPositive() {
        return value > 0;
    }

    public int doubleValue() {
        return value * 2;
    }

    public void reset() {
        value = 0;
    }
}