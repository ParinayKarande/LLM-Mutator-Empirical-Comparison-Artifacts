package org.apache.commons.io.serialization;

public class MySerializableClass {

    public boolean isActive() {
        return true;
    }

    public int calculate(int a, int b) {
        return a + b;
    }

    public void performAction() {
        System.out.println("Action performed!");
    }

    public String getMessage() {
        return "Hello, world!";
    }

    public void doNothing() {
        // This method does nothing
    }
}