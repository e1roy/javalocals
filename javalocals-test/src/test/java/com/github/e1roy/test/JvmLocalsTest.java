package com.github.e1roy.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * @author elroysu
 * @date 2024/11/6 星期三 1:41
 */
public class JvmLocalsTest {
    
    // 在执行每一个测试之前,打印测试的函数名称
    @BeforeEach
    public void printTestName(TestInfo testInfo) {
        System.out.println("* JvmLocalsTest." + testInfo.getDisplayName());
    }
    @Test
    public void testAdd() {
        new DemoFile().add(1, 2);
    }

    @Test
    public void testLambda() {
        new DemoFile().testLambda();
    }

}
