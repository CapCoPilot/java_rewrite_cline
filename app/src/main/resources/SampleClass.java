package com.example;

/**
 * This is a sample class to demonstrate the method replacement functionality.
 */
public class SampleClass {
    
    /**
     * This is a sample method with a comment that should be preserved.
     * 
     * @param name The name to greet
     * @return A greeting message
     */
    public String greet(String name) {
        // This is an inline comment
        return "Hello, " + name + "!";
    }
    
    /**
     * Another method to demonstrate multiple method handling.
     * 
     * @param a First number
     * @param b Second number
     * @return The sum of a and b
     */
    public int add(int a, int b) {
        return a + b; // Simple addition
    }
}
