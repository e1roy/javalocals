package io.github.e1roy

import org.apache.commons.collections4.CollectionUtils
import spoon.reflect.declaration.CtClass
import spoon.reflect.declaration.CtMethod;

/**
 * 类测试帮助类, 避免多次重复编译代码
 *
 * @author elroysu
 * @date 2024/11/9 星期六 0:02
 */
public class ClassTestHelper {

    LauncherTestHelper launcher;
    String classFileName = null;
    CtClass ctClass = null;
    JavaLocalsProcessor processor = null;

    public ClassTestHelper(LauncherTestHelper launcherTestHelper, String classFilName) {
        this.launcher = launcherTestHelper;
        this.classFileName = classFilName;
    }

    public ClassTestHelper process(JavaLocalsProcessor processor) {
        this.launcher.addInputResource(TestUtils.getJavaSpoonFile(classFileName))
        this.launcher.buildModel()
        this.ctClass = this.launcher.getClass(classFileName)
        this.processor = processor;
        processor.processCtClassForTest(ctClass);
        return this;
    }

    CtMethod getCtMethod(String methodName) {
        var methods = this.ctClass.getMethodsByName(methodName)
        if (CollectionUtils.isNotEmpty(methods)) {
            return methods.get(0)
        }
        return null;
    }

    /**
     * 通过必报获取方法, ide友好;
     */
    def getCtMethod(Closure methodClosure) {
        var methodName = TestUtils.getMethodName(methodClosure)
        return getCtMethod(methodName)
    }

    String getCtMethodStr(String methodName) {
        var ctMethod = this.getCtMethod(methodName)
        return String.valueOf(ctMethod);
    }


}
