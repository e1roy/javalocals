package com.github.e1roy;

import groovy.lang.Delegate;
import spoon.SpoonModelBuilder;

/**
 * 自定义model builder, 目的是hook一些函数
 * @author elroysu
 * @date 2024/11/7 星期四 23:23
 */
public class JavaLocalsSpoonModelBuilder implements SpoonModelBuilder{

    @Delegate
    SpoonModelBuilder modelBuilder;
    JavaLocalsSpoonLauncher launcher;

    public JavaLocalsSpoonModelBuilder(JavaLocalsSpoonLauncher launcher, SpoonModelBuilder modelBuilder) {
        this.launcher = launcher;
        this.modelBuilder = modelBuilder;
    }




}
