package io.github.e1roy

import javaresource.MethodArgsFile
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo

/**
 * 测试 MethodArgsFile 中的方法.
 *
 * 每一个printLocals()方法后面都应该有一个validateXXX()方法，用于验证printLocals()方法的参数是否正确.
 * printLocals();
 * validateXXX(***) 
 *
 * @author elroysu
 * @date 2024/11/7 星期四 23:07
 */
public class MethodArgsTest {

    static String classFileName = MethodArgsFile.class.getCanonicalName()

    LauncherTestHelper launcher = null;
    ClassTestHelper classHelper = null

    def setUp() {
        if (launcher != null) {
            return
        }
        launcher = new LauncherTestHelper(new JavaLocalsSpoonLauncher())
        classHelper = new ClassTestHelper(launcher, classFileName)
        classHelper.process(new JavaLocalsProcessor())
    }

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        setUp()
        println "***** ${this.getClass().getSimpleName()}.${testInfo.getDisplayName()}"
    }

    @AfterEach
    void printTestEnd(TestInfo testInfo) {
        println "\n***** ${this.getClass().getSimpleName()}.${testInfo.getDisplayName()} end "
    }

    @Test
    void testClass() {
        var ctClass = classHelper.ctClass
        var ctClassStr = String.valueOf(ctClass)
        validate(ctClassStr)
        println ctClassStr
    }

    @Test
    void add() {
        var method = classHelper.getCtMethod(MethodArgsFile.&add)
        var methodStr = String.valueOf(method)
        validate(methodStr)
    }

    @Test
    void loopFor() {
        var method = classHelper.getCtMethod(MethodArgsFile.&loopFor)
        var methodStr = String.valueOf(method)
        validate(methodStr)
    }

    @Test
    void lambda() {
        var method = classHelper.getCtMethod(MethodArgsFile.&lambda)
        var methodStr = String.valueOf(method)
        validate(methodStr)
    }

    @Test
    void anonymousClass() {
        var method = classHelper.getCtMethod(MethodArgsFile.&anonymousClass as Closure)
        var methodStr = String.valueOf(method)
        validate(methodStr)
    }

    @Test
    void ifElse() {
        var method = classHelper.getCtMethod(MethodArgsFile.&ifElse)
        var methodStr = String.valueOf(method)
        validate(methodStr)
    }

    @Test
    void trtCatch() {
        var method = classHelper.getCtMethod(MethodArgsFile.&trtCatch)
        var methodStr = String.valueOf(method)
        validate(methodStr)
    }

    @Test
    void varShadow() {
        var method = classHelper.getCtMethod(MethodArgsFile.&varShadow)
        var methodStr = String.valueOf(method)
        validate(methodStr)
    }

    // recursive
    @Test
    void recursive() {
        var method = classHelper.getCtMethod(MethodArgsFile.&recursive)
        var methodStr = String.valueOf(method)
        validate(methodStr)
    }

    @Test
    void switchCase() {
        var method = classHelper.getCtMethod(MethodArgsFile.&switchCase)
        var methodStr = String.valueOf(method)
        validate(methodStr)
    }


    @Test
    void nestedLoops() {
        var method = classHelper.getCtMethod(MethodArgsFile.&nestedLoops)
        var methodStr = String.valueOf(method)
        validate(methodStr)
    }

    @Test
    void stream() {
        var method = classHelper.getCtMethod(MethodArgsFile.&stream)
        var methodStr = String.valueOf(method)
        validate(methodStr)
    }


    @Test
    void tryWithResource() {
        var method = classHelper.getCtMethod(MethodArgsFile.&tryWithResource)
        var methodStr = String.valueOf(method)
        validate(methodStr)
    }


    static void validate(String data) {
        String[] arr = data.split("\r\n")
        for (var i = 0; i < arr.size(); i++) {
            String line = arr[i].trim()
            // println "line: $line"
            if (line.contains("printLocals(\"")) {
                println line
                if (i + 1 < arr.size()) {
                    String nextLine = arr[i + 1].trim()
                    println(nextLine)
                    if (nextLine.startsWith("validateXXX(\"")) {
                        // 比较两个方法的参数是否一致
                        if (!Objects.equals(line, nextLine.replace("validateXXX", "printLocals"))) {
                            throw new RuntimeException("validate failed, line: \n ${line} -> \n ${nextLine} \n")
                        }
                    }
                }
            }
        }
    }

}
