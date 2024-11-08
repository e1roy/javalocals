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
        System.out.println("***** MethodArgsTest.${testInfo.getDisplayName()}");
    }

    @AfterEach
    void printTestEnd(TestInfo testInfo) {
        println("\n***** MethodArgsTest.${testInfo.getDisplayName()} end")
    }

    @Test
    void testAdd() {
        var methodAdd = classHelper.getCtMethod(MethodArgsFile.&add)
        println methodAdd
        assertThat(String.valueOf(methodAdd)).contains("printLocals(a, b, args, c)")
    }

}
