package javaresource;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class MethodArgsFile {

    static {
        int staticA = 1;
        printLocals();
        validateXXX("staticA", staticA);
    }

    {
        int instanceA = 1;
        printLocals();
        validateXXX("instanceA", instanceA);
    }

    public void add(int a, int b, String... args) {
        int c = a + b;
        printLocals();
        validateXXX("a", a, "b", b, "args", args, "c", c);
        int d = 0;
    }

    public void loopFor() {
        for (int i = 0; i < 10; i++) {
            int loopVar = i * 2;
            printLocals();
            validateXXX("i", i, "loopVar", loopVar);
        }
        printLocals();
        validateXXX();
    }

    public void lambda() {
        Consumer<Integer> consumer = (y) -> {
            int z = y + 100;
            printLocals();
            validateXXX("y", y, "z", z);
        };
        consumer.accept(999);
        printLocals();
        validateXXX("consumer", consumer);
    }

    public void anonymousClass() {
        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer i) {
                int z = i + 100;
                printLocals();
                validateXXX("i", i, "z", z);
            }
        };
        printLocals();
        validateXXX("consumer", consumer);
    }

    public void ifElse() {
        int a = 1;
        if (a > 0) {
            int a1 = a;
            printLocals();
            validateXXX("a", a, "a1", a1);
        } else {
            int a2 = -a;
            printLocals();
            validateXXX("a", a, "a2", a2);
        }
        printLocals();
        validateXXX("a", a);
    }

    public void trtCatch() {
        int a = 200;
        try {
            int tryVar = 1;
            printLocals();
            validateXXX("a", a, "tryVar", tryVar);
        } catch (Exception e) {
            int catchVar = 2;
            printLocals();
            validateXXX("a", a, "e", e, "catchVar", catchVar);
        } finally {
            int finallyVar = 3;
            printLocals();
            validateXXX("a", a, "finallyVar", finallyVar);
        }
        printLocals();
        validateXXX("a", a);
    }

    public void varShadow() {
        int a1 = 1;
        {
            int a2 = 2; // 变量遮蔽
            printLocals();
            validateXXX("a1", a1, "a2", a2);
        }
        printLocals();
        validateXXX("a1", a1);
    }

    // recursiveTest
    public void recursive(int n) {
        int a = 100;
        if (n > 0) {
            int recursiveVar = n;
            printLocals();
            validateXXX("n", n, "a", a, "recursiveVar", recursiveVar);
            recursive(n - 1);
        }
        printLocals();
        validateXXX("n", n, "a", a);
    }

    // switch case
    public void switchCase() {
        int a = 100;
        switch (a) {
            case 1:
                int caseOneVar = 1;
                printLocals();
                validateXXX("a", a, "caseOneVar", caseOneVar);
                break;
            case 2:
                int caseTwoVar = 2;
                printLocals();
                validateXXX("a", a, "caseTwoVar", caseTwoVar);
                break;
            default:
                int defaultVar = 0;
                printLocals();
                validateXXX("a", a, "defaultVar", defaultVar);
        }
        printLocals();
        validateXXX("a", a);
    }

    public void nestedLoops() {
        int a = 1;
        for (int i = 0; i < 3; i++) {
            int outerVar = i;
            for (int j = 0; j < 2; j++) {
                int innerVar = j;
                printLocals();
                validateXXX("a", a, "i", i, "outerVar", outerVar, "j", j, "innerVar", innerVar);
            }
            printLocals();
            validateXXX("a", a, "i", i, "outerVar", outerVar);
        }
    }

    public void stream() {
        List<Integer> list = Arrays.asList("1", "2", "3")
                .stream()
                .map(it1 -> Integer.valueOf(it1))
                .filter(it2 -> it2 > 2)
                .peek(it3 -> {
                    int a = 200;
                    printLocals();
                    validateXXX("it3", it3, "a", a);
                }).collect(Collectors.toList());
        printLocals();
        validateXXX("list", list);
    }

    public void tryWithResource() {
        int a = 1;
        try (var b = new AutoCloseable() {
            @Override
            public void close() throws Exception {
                int b1 = 1;
                printLocals();
                validateXXX("a", a, "b1", b1);
            }
        }) {
            int c = 2;
            printLocals();
            validateXXX("a", a, "b", b, "c", c);
        } catch (Exception e) {
            int d = 3;
            printLocals();
            validateXXX("a", a, "e", e, "d", d);
        }
        printLocals();
        validateXXX("a", a);
    }

    // #######################
    public static Object printLocals(Object... args) {
        return null;
    }

    public static Object validateXXX(Object... args) {
        return null;
    }

}
