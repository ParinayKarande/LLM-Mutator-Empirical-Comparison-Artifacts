package org.apache.commons.io.function;

public class ExampleFunction {

    public int add(int a, int b) {
        return a + b;
    }

    public boolean isEven(int number) {
        return number % 2 == 0;
    }

    public void printMessage(String message) {
        if (message != null) {
            System.out.println(message);
        }
    }

    public int getValue() {
        return 42;
    }
}