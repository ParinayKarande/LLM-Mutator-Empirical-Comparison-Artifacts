package org.apache.commons.io.output;

public class SimpleOutput {
    
    private int count;
    
    public SimpleOutput() {
        this.count = 0;
    }

    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public boolean isEven() {
        return count % 2 == 0;
    }

    public void reset() {
        count = 0;
    }
}