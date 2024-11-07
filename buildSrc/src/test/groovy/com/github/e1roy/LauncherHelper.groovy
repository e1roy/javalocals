package com.github.e1roy

import spoon.reflect.declaration.CtElement

/**
 * @author elroysu
 * @date 2024/11/8 星期五 0:46
 */
class LauncherHelper {

    @Delegate
    JavaLocalsSpoonLauncher launcher = null;

    LauncherHelper(JavaLocalsSpoonLauncher launcher) {
        this.launcher = launcher
    }

    def getPrinter() {
        return launcher.getFactory().getEnvironment().createPrettyPrinter()
    }

    def prettyPrint(CtElement ctElement) {
        return getPrinter().prettyprint(ctElement)
    }

    def getClass(String className) {
        return launcher.getFactory().Class().get(className)
    }

}
