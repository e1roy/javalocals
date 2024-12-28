package github.e1roy

import org.gradle.api.file.FileCollection
import spoon.processing.Processor

class SpoonExtension {
    /** True to active the debug mode. */
    boolean enable
    boolean debug

    /** Input directories for Spoon. */
    FileCollection srcFolders

    /** Output directory where Spoon must generate his output (spooned source code). */
    File outFolder

    /** Tells to spoon that it must preserve formatting of original source code. */
    boolean preserveFormatting;

    /** Tells to spoon that it must not assume a full classpath. */
    boolean noClasspath

    /** List of processors. */
    String[] processors = []

    /** List of already instantiated processors. */
    Processor[] processorsInstance = []

    /** True to active the compilation of original sources. */
    boolean compileOriginalSources

    /** Java version used to spoon target project. */
    int compliance = 7

    /**
     * Spoon goal either Generate classes or run checks on the code.
     */
   TaskType goal = TaskType.GENERATE

    /**
     * 需要填充的方法名称(TODO 需要修改成全限定名称)
     */
    String fillMethodName = "printLocals"
}
