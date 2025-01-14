
## Java Locals: A Gradle Plugin for Enhanced Logging

This project introduces **javalocals**, a Gradle plugin inspired by Python's `locals()` function. It aims to simplify debugging and troubleshooting in Java by providing a convenient way to log all local variables within a method.

### Background

When exceptions occur, especially in testing environments, identifying the root cause can be challenging if the necessary context is missing from log messages. Manually adding logging statements for each variable is cumbersome and time-consuming. 

**javalocals** addresses this issue by automating the process of logging local variables, offering a comprehensive view of the method's state at any given point.

### Two Implementation Approaches

The plugin offers two distinct implementation strategies:

1. **Compile-Time Modification:** This approach leverages source code transformation. During compilation, the plugin modifies the original Java source code (`source.java`) to inject logging statements for all local variables. This modified code (`modified_source.java`) is then compiled into bytecode (`*.class`).

2. **JVMTI Runtime Instrumentation:** This approach utilizes the Java Virtual Machine Tool Interface (JVMTI) to access local variable information during runtime. By invoking JNI functions, the plugin retrieves and logs the values of all local variables within the target method.

### Usage

To integrate **javalocals** into your Gradle project, follow these steps:

1. **Include the plugin in your `build.gradle` file:**

```gradle
plugins {
    id 'java'
    id 'io.github.e1roy.javalocals' version '0.0.1-test'
}

javaLocals {
    enable true        
    compileOriginalSources false 
    compliance 11                
    processors = ['io.github.e1roy.JavaLocalsProcessor'] 
    fillMethodName = "printLocals"    
}
```

2. **Configure the plugin:**

   - `enable`: Enables/disables the plugin.
   - `compileOriginalSources`: Controls whether to compile the original source code alongside the modified version.
   - `compliance`: Specifies the Java compliance level.
   - `processors`: Defines the list of annotation processors to use.
   - `fillMethodName`:  Specifies the name of the method where local variables will be logged.

### Example

1. **Original Source Code:**

```java
// We want to log the values of a, b, and c within the printLocals method.
public void add(int a, int b) {
    int c = a + b;
    printLocals();
    int d = 0;
}
```

2. **Code Generated by the Plugin:**

```java
// Generated code with logging statements for local variables.
public void add(int a, int b) {
    int c = a + b;
    printLocals("a", a, "b", b, "c", c); 
    int d = 0;
}
```

3. **Test Execution:**

```java
// Output:
// printLocals: [a, 1, b, 2, c, 3]
@Test
public void testAdd() {
    new DemoFile().add(1, 2);
}
```

### TODO
- [ ] not change source code line number
- [x] Implement test cases.
- [x] Package the plugin for distribution.
- [ ] Test the plugin in multi-module projects.
- [ ] Evaluate performance on large-scale projects.
- [ ] Migrate JNI code from: https://github.com/e1roy/jvmlocals

### Technology Stack

- **Spoon:** Used for parsing and modifying Java source code.
- **Gradle:** Provides the plugin infrastructure.

### Publishing

**Validation:**

```shell
./gradlew publishPlugins --validate-only
```

**Publishing to the Repository:**

```shell
./gradlew publishPlugins
```

**Publishing to the Local Repository:**

```shell
./gradlew publishToMavenLocal
```
