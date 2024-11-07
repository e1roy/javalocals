package com.github.e1roy

import spoon.reflect.declaration.CtElement
import spoon.reflect.factory.Factory

/**
 * @author elroysu
 * @date 2024/11/8 星期五 0:46
 */
class Helper {

    @Delegate
    JavaLocalsSpoonLauncher launcher = null;

    Helper(JavaLocalsSpoonLauncher launcher) {
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
