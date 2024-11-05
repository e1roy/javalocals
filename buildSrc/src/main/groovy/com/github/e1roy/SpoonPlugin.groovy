package com.github.e1roy

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

class SpoonPlugin implements Plugin<Project> {
    @Override
    void apply(final Project project) {
        def hasJavaPlugin = project.plugins.hasPlugin JavaPlugin
        if (!hasJavaPlugin) {
            throw new IllegalStateException('The java plugin is required')
        }

        project.extensions.create "spoon", SpoonExtension

        // Adds task before the evaluation of the project to access of values
        // overloaded by the developer.
        project.afterEvaluate({
            println "=================== Spoon plugin is running ==================="
            def compileJavaTask = project.getTasksByName("compileJava", true).first();

            SpoonExtension conf = project.spoon;
            def spoonTask = project.task('spoon', type: SpoonTask) {
                def sourceFolders = []
                if (!conf.srcFolders) {
                    sourceFolders = Utils.transformListFileToListString(project, project.sourceSets.main.java.srcDirs)
                } else {
                    sourceFolders = Utils.transformListFileToListString(project, conf.srcFolders)
                }
                if (!conf.outFolder) {
                    conf.outFolder = project.file("${project.buildDir}/generated")
                }

                srcFolders = sourceFolders
                outFolder = conf.outFolder
                println  "outFolder: ${outFolder}"
                preserveFormatting = conf.preserveFormatting
                noClasspath = conf.noClasspath
                processors = conf.processors
                classpath = compileJavaTask.classpath
                compliance = conf.compliance
            }

            // Changes source folder if the user don't would like use the original source.
            if (!conf.compileOriginalSources) {
                compileJavaTask.source = conf.outFolder
            }
            // Inserts spoon task before compiling.
            compileJavaTask.dependsOn spoonTask

            println "=================== Spoon plugin is running done ==================="

        })
    }
}
