package io.github.e1roy

import spoon.processing.AbstractProcessor
import spoon.reflect.declaration.CtElement

/**
 * @author elroysu
 * @date 2024/11/8 星期五 0:46
 */
class LauncherTestHelper {

    @Delegate
    JavaLocalsSpoonLauncher launcher = null;

    LauncherTestHelper(JavaLocalsSpoonLauncher launcher) {
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
