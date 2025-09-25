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
        String[] arr = data.split("\\r\\n")
        for (var i = 0; i < arr.size(); i++) {
            String line = arr[i].trim()
            if (line.contains("printLocals(\"")) {
                if (i + 1 < arr.size()) {
                    String nextLine = arr[i + 1].trim()
                    // 提取参数列表进行比较
                    println "line: $line"
                    println("nextLine " + nextLine)
                    String printLocalsParams = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""))
                    String validateXXXParams = nextLine.substring(nextLine.indexOf("\"") + 1, nextLine.lastIndexOf("\""))

                    println "printLocalsParams: $printLocalsParams"
                    println "validateXXXParams: $validateXXXParams"

                    if (!Objects.equals(printLocalsParams, validateXXXParams)) {
                        throw new RuntimeException("validate failed, line: \n ${line} -> \n ${nextLine} \n")
                    }
                }
            }
        }
    }


}