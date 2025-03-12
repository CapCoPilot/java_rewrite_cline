package com.javarewrite;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for replacing methods in Java source files while preserving comments.
 */
public class MethodReplacer {
    
    private final JavaParser javaParser;
    
    public MethodReplacer() {
        this.javaParser = new JavaParser();
    }
    
    /**
     * Replaces a method in a Java source file with a new implementation.
     * 
     * @param sourceFilePath Path to the source file
     * @param className Name of the class containing the method
     * @param methodName Name of the method to replace
     * @param newMethodCode New method implementation as a string
     * @return true if the method was successfully replaced, false otherwise
     * @throws IOException if there's an error reading or writing the file
     */
    public boolean replaceMethod(String sourceFilePath, String className, String methodName, String newMethodCode) throws IOException {
        Path path = Paths.get(sourceFilePath);
        
        if (!Files.exists(path)) {
            throw new IOException("Source file does not exist: " + sourceFilePath);
        }
        
        // Read the original source code
        String originalSource = Files.readString(path);
        
        // Parse the source file
        ParseResult<CompilationUnit> parseResult = javaParser.parse(originalSource);
        
        if (!parseResult.isSuccessful()) {
            throw new IOException("Failed to parse source file: " + parseResult.getProblems());
        }
        
        CompilationUnit cu = parseResult.getResult().orElseThrow(() -> 
            new IOException("Failed to get compilation unit"));
        
        // Configure lexical preservation to maintain formatting and comments
        LexicalPreservingPrinter.setup(cu);
        
        // Find the method to replace
        List<MethodDeclaration> methodsToReplace = new ArrayList<>();
        
        cu.accept(new VoidVisitorAdapter<Void>() {
            @Override
            public void visit(MethodDeclaration md, Void arg) {
                super.visit(md, arg);
                
                // Check if this is the method we're looking for
                if (md.getNameAsString().equals(methodName)) {
                    // If className is provided, check if the method belongs to the specified class
                    if (className != null && !className.isEmpty()) {
                        md.getParentNode().ifPresent(parent -> {
                            if (parent.toString().contains("class " + className)) {
                                methodsToReplace.add(md);
                            }
                        });
                    } else {
                        // If no className is provided, replace all methods with the given name
                        methodsToReplace.add(md);
                    }
                }
            }
        }, null);
        
        if (methodsToReplace.isEmpty()) {
            System.out.println("Method '" + methodName + "' not found in " + sourceFilePath);
            return false;
        }
        
        // Parse the new method code
        ParseResult<MethodDeclaration> newMethodParseResult = javaParser.parseMethodDeclaration(newMethodCode);
        
        if (!newMethodParseResult.isSuccessful()) {
            throw new IOException("Failed to parse new method code: " + newMethodParseResult.getProblems());
        }
        
        MethodDeclaration newMethod = newMethodParseResult.getResult().orElseThrow(() -> 
            new IOException("Failed to parse new method"));
        
        // Replace the method
        for (MethodDeclaration md : methodsToReplace) {
            // Preserve the method's annotations and modifiers
            newMethod.setAnnotations(md.getAnnotations());
            newMethod.setModifiers(md.getModifiers());
            
            // Preserve JavaDoc comments if present
            md.getJavadocComment().ifPresent(comment -> {
                newMethod.setJavadocComment(comment.toString());
            });
            
            // Replace the old method with the new one
            md.setBody(newMethod.getBody().orElseThrow());
        }
        
        // Write the modified source back to the file
        try (FileWriter writer = new FileWriter(sourceFilePath)) {
            writer.write(LexicalPreservingPrinter.print(cu));
        }
        
        System.out.println("Successfully replaced method '" + methodName + "' in " + sourceFilePath);
        return true;
    }
    
    /**
     * Lists all methods in a Java source file.
     * 
     * @param sourceFilePath Path to the source file
     * @return A list of method names
     * @throws IOException if there's an error reading the file
     */
    public List<String> listMethods(String sourceFilePath) throws IOException {
        Path path = Paths.get(sourceFilePath);
        
        if (!Files.exists(path)) {
            throw new IOException("Source file does not exist: " + sourceFilePath);
        }
        
        // Parse the source file
        ParseResult<CompilationUnit> parseResult = javaParser.parse(path);
        
        if (!parseResult.isSuccessful()) {
            throw new IOException("Failed to parse source file: " + parseResult.getProblems());
        }
        
        CompilationUnit cu = parseResult.getResult().orElseThrow(() -> 
            new IOException("Failed to get compilation unit"));
        
        // Find all methods
        List<String> methods = new ArrayList<>();
        
        cu.accept(new VoidVisitorAdapter<Void>() {
            @Override
            public void visit(MethodDeclaration md, Void arg) {
                super.visit(md, arg);
                methods.add(md.getNameAsString());
            }
        }, null);
        
        return methods;
    }
}
