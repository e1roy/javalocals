package javaresource;

public class MethodArgsFile {

    public void add(int a, int b, String... args) {
        int c = a + b;
        printLocals();
        int d = 0;
    }

    public void printLocals() {

    }
    
}