package com.github.e1roy.test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author elroysu
 * @date 2024/10/31 星期四 1:13
 */
public class DemoFile {

    static {
        String a = "staticBlockValue";
        printLocals();
    }

    {
        String a = "instanceBlockValue";
        printLocals();
    }

    public void add(int a, int b) {
        int c = a + b;
        printLocals();
        int d = 0;
    }

    public void loopFor() {
        for (int i = 0; i < 10; i++) {
            int loopVar = i * 2;
            printLocals();
        }
        printLocals();
    }

    public void lambda() {
        Consumer<Integer> consumer = (y) -> {
            int z = y + 100;
            printLocals();
        };
        consumer.accept(999);
        printLocals();
    }

    public void anonymousClass() {
        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer i) {
                int z = i + 100;
                printLocals();
            }
        };
        printLocals();
    }

    public void ifElse() {
        int a = 1;
        if (a > 0) {
            int a1 = a;
            printLocals();
        } else {
            int a2 = -a;
            printLocals();
        }
        printLocals();
    }

    public void trtCatch() {
        int a = 200;
        try {
            int tryVar = 1;
            printLocals();
        } catch (Exception e) {
            int catchVar = 2;
            printLocals();
        } finally {
            int finallyVar = 3;
            printLocals();
        }
        printLocals();
    }

    public void varShadow() {
        int a1 = 1;
        {
            int a2 = 2;// 变量遮蔽
            printLocals();
        }
        printLocals();
    }

    // recursiveTest
    public void recursive(int n) {
        int a = 100;
        if (n > 0) {
            int recursiveVar = n;
            printLocals();
            recursive(n - 1);
        }
        printLocals();
    }

    // switch case
    public void switchCase() {
        int a = 100;
        switch (a) {
            case 1:
                int caseOneVar = 1;
                printLocals();
                break;
            case 2:
                int caseTwoVar = 2;
                printLocals();
                break;
            default:
                int defaultVar = 0;
                printLocals();
        }
        printLocals();
    }

    public void nestedLoops() {
        int a = 1;
        for (int i = 0; i < 3; i++) {
            int outerVar = i;
            for (int j = 0; j < 2; j++) {
                int innerVar = j;
                printLocals();
            }
            printLocals();
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
                }).collect(Collectors.toList());
        printLocals();
    }

    public void tryWithResource() {
        int a = 1;
        try (var b = new AutoCloseable() {
            @Override
            public void close() throws Exception {
                int b1 = 1;
                printLocals();
            }
        }) {
            // 嵌套try
            try (var b2 = new AutoCloseable() {
                @Override
                public void close() throws Exception {
                    int b1 = 1;
                    printLocals();
                }
            }) {
                int c = 2;
                printLocals();
            } catch (Exception tryWithResourceE) {
                int d = 3;
                printLocals();
            }
            // 嵌套try

            int c = 2;
            printLocals();
        } catch (Exception tryWithResourceE) {
            int d = 3;
//            printLocals(a, tryWithResourceE, d);
            printLocals();
        }
        printLocals();

    }

    // #######################
    public static void printLocals(Object... args) {
        System.out.println("printLocals: " + Arrays.toString(args));
    }

}
