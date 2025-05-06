package org.apache.commons.io.charset;

public class CharsetUtils {
    
    public static String getDefaultCharset() {
        return "UTF-8"; // A simple return value
    }

    public static boolean isValidCharset(String charset) {
        return charset != null && charset.matches("[A-Za-z0-9_-]+");
    }

    public static void printCharset(String charset) {
        if (isValidCharset(charset)) {
            System.out.println("Charset: " + charset);
        } else {
            System.out.println("Invalid Charset");
        }
    }

    public static int getCharsetLength(String charset) {
        if (charset == null) {
            return 0; // Return 0 for null
        }
        return charset.length(); // Returning the length of the charset string
    }
}