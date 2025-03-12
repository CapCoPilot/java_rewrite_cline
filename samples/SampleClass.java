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
        String greeting = "Greetings, " + name + "!";
        System.out.println("Greeting generated: " + greeting);
        return greeting;
    }
    
    /**
     * Another method to demonstrate multiple method handling.
     * 
     * @param a First number
     * @param b Second number
     * @return The sum of a and b
     */
    public int add(int a, int b) {
        System.out.println("Adding " + a + " and " + b);
        return a + b;
    }
}
