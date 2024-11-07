package com.github.e1roy

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.plugins.JavaPlugin

class SpoonPlugin implements Plugin<Project> {

    @Override
    void apply(final Project project) {
        def hasJavaPlugin = project.plugins.hasPlugin JavaPlugin
        if (!hasJavaPlugin) {
            throw new IllegalStateException('The java plugin is required')
        }

        project.extensions.create "javaLocals", SpoonExtension

        // Adds task before the evaluation of the project to access of values
        // overloaded by the developer.
        project.afterEvaluate({
            println "-- Spoon plugin is running --"
            def compileJavaTask = project.getTasksByName("compileJava", true).first();

            SpoonExtension conf = project.javaLocals;

            if (!conf.enable) {
                return
            }

            def spoonTask = project.task('javaLocalsTransform', type: SpoonTask) {
                def sourceFolders = [];
                def sourceFileCollection1;
                if (!conf.srcFolders) {
                    sourceFolders = Utils.transformListFileToListString(project, project.sourceSets.main.java.srcDirs)
                    sourceFolders = project.sourceSets.main.java.srcDirs

                    def srcDirs = project.sourceSets.main.java.srcDirs
                    sourceFileCollection1 = project.files(srcDirs)

//                    sourceFileCollection1 = compileJavaTask.source
                } else {
                    sourceFolders = Utils.transformListFileToListString(project, conf.srcFolders)
                }
                // 默认路径
                if (!conf.outFolder) {
                    conf.outFolder = project.file("${project.buildDir}/generated-spoon")
                }

                srcFolders = sourceFolders
                sourceFileCollection = sourceFileCollection1
                outFolder = conf.outFolder
                println "outFolder: ${outFolder}"
                preserveFormatting = conf.preserveFormatting
                noClasspath = conf.noClasspath
                processors = conf.processors
                classpath = compileJavaTask.classpath
                compliance = conf.compliance
            }

            // Changes source folder if the user don't would like use the original source.
            if (!conf.compileOriginalSources) {
                // 更新源代码的路径, 使用编译后的代码生成
                compileJavaTask.source = conf.outFolder
                println "compileJavaTask.source: ${conf.outFolder.getAbsolutePath()}"
            }
            // Inserts spoon task before compiling.
            compileJavaTask.dependsOn spoonTask

            println "-- Spoon plugin is running done --"
        })
    }
}
