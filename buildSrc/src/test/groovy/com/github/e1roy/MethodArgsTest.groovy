package com.github.e1roy

import javaresource.MethodArgsFile
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo

import static com.google.common.truth.Truth.assertThat

/**
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
    void add() {
        var methodAdd = classHelper.getCtMethod(MethodArgsFile.&add)
        var methodStr = String.valueOf(methodAdd)
        assertThat(methodStr).contains("printLocals(a, b, args, c)")
    }

    @Test
    void loopFor() {
        var method = classHelper.getCtMethod(MethodArgsFile.&loopFor)
        var methodStr = String.valueOf(method)
        assertThat(methodStr).contains("printLocals(i, loopVar)")
        assertThat(methodStr).doesNotContain("printLocals(i)")
        assertThat(methodStr).doesNotContain("printLocals(loopVar)")
    }

    @Test
    void lambda() {
        var method = classHelper.getCtMethod(MethodArgsFile.&lambda)
        var methodStr = String.valueOf(method)
        println methodStr
        assertThat(methodStr).contains("printLocals(y, z)")
    }

    @Test
    void anonymousClass() {
        var method = classHelper.getCtMethod(MethodArgsFile.&anonymousClass as Closure)
        var methodStr = String.valueOf(method)
        println methodStr
        assertThat(methodStr).contains("printLocals(i, z)")
        assertThat(methodStr).contains("printLocals(consumer)")
        assertThat(methodStr).doesNotContain("printLocals()")
    }

    @Test
    void ifElse() {
        var method = classHelper.getCtMethod(MethodArgsFile.&ifElse)
        var methodStr = String.valueOf(method)
        println methodStr
        assertThat(methodStr).contains("printLocals(a, a1)")
        assertThat(methodStr).contains("printLocals(a, a2);")
        assertThat(methodStr).contains("printLocals(a);")
        assertThat(methodStr).doesNotContain("printLocals();")
    }

    @Test
    void trtCatch() {
        var method = classHelper.getCtMethod(MethodArgsFile.&trtCatch)
        var methodStr = String.valueOf(method)
        println methodStr
        assertThat(methodStr).contains "printLocals(a, tryVar);"
        assertThat(methodStr).contains "printLocals(a, catchVar);"
        assertThat(methodStr).contains "printLocals(a, finallyVar);"
        assertThat(methodStr).contains "printLocals(a);"
        assertThat(methodStr).doesNotContain "printLocals();"
    }

    @Test
    void varShadow() {
        var method = classHelper.getCtMethod(MethodArgsFile.&varShadow)
        var methodStr = String.valueOf(method)
        println methodStr
        assertThat(methodStr).contains "printLocals(a1, a2);"
        assertThat(methodStr).contains "printLocals(a1);"
        assertThat(methodStr).doesNotContain "printLocals();"
    }

    // recursive
    @Test
    void recursive() {
        var method = classHelper.getCtMethod(MethodArgsFile.&recursive)
        var methodStr = String.valueOf(method)
        println methodStr
        assertThat(methodStr).contains "printLocals(n, a, recursiveVar);"
        assertThat(methodStr).contains "printLocals(n, a);"
        assertThat(methodStr).doesNotContain "printLocals();"
    }

    @Test
    void switchCase() {
        var method = classHelper.getCtMethod(MethodArgsFile.&switchCase)
        var methodStr = String.valueOf(method)
        println methodStr
        assertThat(methodStr).contains "printLocals(a, caseOneVar);"
        assertThat(methodStr).contains "printLocals(a, caseTwoVar);"
        assertThat(methodStr).contains "printLocals(a, defaultVar);"
        assertThat(methodStr).contains "printLocals(a);"
        assertThat(methodStr).doesNotContain "printLocals();"
    }


    @Test
    void nestedLoops() {
        var method = classHelper.getCtMethod(MethodArgsFile.&nestedLoops)
        var methodStr = String.valueOf(method)
        println methodStr
        assertThat(methodStr).contains "printLocals(a, i, outerVar, j, innerVar);"
        assertThat(methodStr).contains "printLocals(a, i, outerVar);"
        assertThat(methodStr).doesNotContain "printLocals();"
    }

    @Test
    void stream() {
        var method = classHelper.getCtMethod(MethodArgsFile.&stream)
        var methodStr = String.valueOf(method)
        println methodStr
        assertThat(methodStr).contains "printLocals(it3, a);"
        assertThat(methodStr).contains "printLocals(list);"
        assertThat(methodStr).doesNotContain "printLocals();"
    }


    @Test
    void tryWithResource() {
        var method = classHelper.getCtMethod(MethodArgsFile.&tryWithResource)
        var methodStr = String.valueOf(method)
        println methodStr
        assertThat(methodStr).contains ".printLocals(a, b1);"
        assertThat(methodStr).contains "printLocals(b, c);"
        assertThat(methodStr).contains "printLocals(b, d);"
        assertThat(methodStr).contains "printLocals(a);"
        assertThat(methodStr).doesNotContain "printLocals();"
    }

    @Test
    void staticBlock() {
        var ctClass = classHelper.ctClass
        var methodStr = String.valueOf(ctClass)
        println methodStr
    }


}
