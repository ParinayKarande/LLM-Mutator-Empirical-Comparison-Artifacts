package org.apache.commons.csv;

public class CsvParser {

    private String delimiter;

    public CsvParser(String delimiter) {
        this.delimiter = delimiter;
    }

    public boolean parse(String input) {
        if (input == null || input.isEmpty()) {
            return false;  // Negate Conditionals
        }
        String[] tokens = input.split(this.delimiter);
        return tokens.length > 0; // Return Values
    }

    public void printTokens(String input) {
        String[] tokens = input.split(this.delimiter);
        for (String token : tokens) {
            System.out.println(token); // Void Method Calls
        }
    }

    public int countTokens(String input) {
        String[] tokens = input.split(this.delimiter);
        return tokens.length; // Primitive Returns
    }
}