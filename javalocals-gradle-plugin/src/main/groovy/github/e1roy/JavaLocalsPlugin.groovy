package github.e1roy

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

class JavaLocalsPlugin implements Plugin<Project> {

    @Override
    void apply(final Project project) {
        def hasJavaPlugin = project.plugins.hasPlugin JavaPlugin
        if (!hasJavaPlugin) {
            throw new IllegalStateException('The java plugin is required')
        }
        // 解析配置文件
        project.extensions.create("javaLocals", SpoonExtension)
        // evaluate 阶段, 注册一个task, 在编译之前执行
        project.afterEvaluate({
            println "-- Spoon plugin --"
            def compileJavaTask = project.getTasksByName("compileJava", true).first();
            SpoonExtension conf = project.javaLocals;
            // 多project的情况下, 不同的project可以有不同的配置
            if (!conf.enable) {
                return
            }
            def spoonTask = project.task('javaLocalsTransform', type: JavaLocalsTask) {
                def sourceFolders = [];
                def compileJavaSourceFileCollection;
                if (!conf.srcFolders) {
//                    sourceFolders = Utils.transformListFileToListString(project, project.sourceSets.main.java.srcDirs)
                    sourceFolders = project.sourceSets.main.java.srcDirs
//                    def srcDirs = project.sourceSets.main.java.srcDirs
                    compileJavaSourceFileCollection = compileJavaTask.source
                } else {
                    sourceFolders = Utils.transformListFileToListString(project, conf.srcFolders)
                }
                // 默认路径
                if (!conf.outFolder) {
                    conf.outFolder = project.file("${project.buildDir}/generated-spoon")
                }

                srcFolders = sourceFolders
                sourceFileCollection = compileJavaSourceFileCollection
                outFolder = conf.outFolder
                println "outFolder: ${outFolder}"
                preserveFormatting = conf.preserveFormatting
                noClasspath = conf.noClasspath
                processors = conf.processors
                fillMethodName = conf.fillMethodName
                classpath = compileJavaTask.classpath
                compliance = conf.compliance
            }

            if (!conf.compileOriginalSources) {
                // 更新源代码的路径, 使用编译后的代码生成
                compileJavaTask.source = conf.outFolder
                println "compileJavaTask.source: ${conf.outFolder.getAbsolutePath()}"
            }
            compileJavaTask.dependsOn spoonTask
            println "-- Spoon plugin  done --"
        })
    }
}
