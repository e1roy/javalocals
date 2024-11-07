package com.github.e1roy

import spoon.Launcher
import spoon.SpoonModelBuilder
import spoon.processing.Processor
import spoon.reflect.declaration.CtElement
import spoon.reflect.factory.Factory

/**
 * 自定义launcher, 目的是hook一些函数
 * @author elroysu
 * @date 2024/11/7 星期四 23:21
 */
public class JavaLocalsSpoonLauncher extends Launcher {

    String fillMethodName = "printLocals";

    public void setFillMethodName(String fillMethodName) {
        this.fillMethodName = fillMethodName;
    }

    @Override
    public SpoonModelBuilder createCompiler(Factory factory) {
        var modelBuilder = super.createCompiler(factory);
        return new JavaLocalsSpoonModelBuilder(this, modelBuilder);
    }

    @Override
    def <T extends CtElement> void addProcessor(Processor<T> processor) {
        beforeAddProcessor(processor)
        super.addProcessor(processor)
    }

    def void beforeAddProcessor(Processor processor) {
        if (processor instanceof JavaLocalsProcessor) {
            processor.setFillMethodName(this.fillMethodName)
        }
        println "before add processor: ${processor}"
    }
}
