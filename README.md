# Java Method Replacer

A Gradle-based application that allows you to replace methods in Java source files while preserving comments.

## Features

- Replace methods in Java source files while preserving comments and formatting
- List all methods in a Java source file
- Support for specifying the class containing the method to replace
- Support for providing the new method implementation as a string or from a file

## Requirements

- Java 21 or higher
- Gradle 8.0 or higher

## Building the Application

```bash
./gradlew build
```

## Running the Application

### List all methods in a Java file

```bash
./gradlew run --args="-f path/to/JavaFile.java -l"
```

### Replace a method using a file containing the new implementation

```bash
./gradlew run --args="-f path/to/JavaFile.java -m methodName -n path/to/NewMethodFile.java"
```

### Replace a method using a string implementation

```bash
./gradlew run --args="-f path/to/JavaFile.java -m methodName -s 'public void methodName() { System.out.println(\"New implementation\"); }'"
```

### Specify the class containing the method

```bash
./gradlew run --args="-f path/to/JavaFile.java -c ClassName -m methodName -n path/to/NewMethodFile.java"
```

## Example

The repository includes a sample Java class and a new method implementation for demonstration:

1. List methods in the sample class:

```bash
./gradlew run --args="-f app/src/main/resources/SampleClass.java -l"
```

2. Replace the `greet` method with the new implementation:

```bash
./gradlew run --args="-f app/src/main/resources/SampleClass.java -m greet -n app/src/main/resources/NewGreetMethod.java"
```

## How It Works

The application uses JavaParser to parse and manipulate Java source code while preserving comments. The process works as follows:

1. Parse the source file into an Abstract Syntax Tree (AST)
2. Set up lexical preservation to maintain formatting and comments
3. Find the method to replace based on name and optionally class name
4. Parse the new method implementation
5. Replace the old method with the new one, preserving annotations and modifiers
6. Write the modified source back to the file

## License

[MIT License](LICENSE)
