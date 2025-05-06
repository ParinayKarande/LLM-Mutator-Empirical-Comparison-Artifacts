package org.apache.commons.io;

public class ExampleClass {

    public int compute(int a, int b) {
        if (a > b) {
            return a - b;
        } else {
            return a + b;
        }
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public boolean isPositive(int number) {
        return number > 0;
    }
}