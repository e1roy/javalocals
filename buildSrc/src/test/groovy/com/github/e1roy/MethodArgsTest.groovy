package com.github.e1roy


import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import spoon.processing.AbstractProcessor
import spoon.reflect.factory.Factory

import static com.google.common.truth.Truth.assertThat

/**
 * @author elroysu
 * @date 2024/11/7 星期四 23:07
 */
public class MethodArgsTest {

    JavaLocalsSpoonLauncher launcher = null;

    // 在执行每一个测试之前,打印测试的函数名称
    @BeforeEach
    public void printTestName(TestInfo testInfo) {
        System.out.println("***** MethodArgsTest ." + testInfo.getDisplayName());
    }

    @AfterEach
    public void printTestEnd(TestInfo testInfo) {
        System.out.println("***** MethodArgsTest ." + testInfo.getDisplayName() + " end");
    }

    @Test
    public void testAdd() {
        launcher = new JavaLocalsSpoonLauncher();
        String className = "MethodArgsFile"
        launcher.addInputResource(Utils.getJavaSpoonFile(className))
        launcher.buildModel();
        Factory factory = launcher.getFactory();
        def printer = factory.getEnvironment().createPrettyPrinter();

        AbstractProcessor processor = new JavaLocalsProcessor();
        processor.setFactory(factory);

        def ctClass = launcher.getFactory().Class().get(className)
        processor.processCtClass(ctClass);

        def outputClassSource = printer.prettyprint(ctClass)
        assertThat(outputClassSource).contains("printLocals(a, b, args, c)")
    }

}
