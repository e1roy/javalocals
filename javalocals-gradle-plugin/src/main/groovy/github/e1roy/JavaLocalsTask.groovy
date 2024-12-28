package github.e1roy

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*
import org.gradle.work.FileChange
import org.gradle.work.InputChanges
import spoon.Launcher

class JavaLocalsTask extends DefaultTask {

    @Internal
    def srcFolders = []

    @InputFiles
    @SkipWhenEmpty
    @PathSensitive(PathSensitivity.RELATIVE)
    FileCollection sourceFileCollection;

    @OutputDirectory
    def File outFolder

    @Input
    def boolean preserveFormatting

    @Input
    def boolean noClasspath

    @Input
    def String[] processors = []

    @Classpath
    def FileCollection classpath

    @Input
    def int compliance

    @Input
    def String fillMethodName;

//    @InputFiles
//    private FileCollection inputFiles;
//
//    public void setInputFiles(FileCollection inputFiles) {
//        this.inputFiles = inputFiles;
//    }
//
//    public FileCollection getInputFiles() {
//        return inputFiles
//    }

    @TaskAction
    void run(InputChanges inputChanges) {
        println "=================== Spoon task is running ==================="
        try {
            doRun(inputChanges)
        } catch (Exception e) {
            e.printStackTrace()
            throw new RuntimeException("Error while running spoon task", e)
        }
        println "=================== Spoon task is running done ==================="
    }

    void doRun(InputChanges inputChanges) {
        def log = project.logger
        printEnvironment(log.&debug)
        if (project.javaLocals.debug) {
            printEnvironment(System.out.&println)
        }

        def changes = inputChanges.getFileChanges(sourceFileCollection)
        StringBuilder filePathBb = new StringBuilder()

        if (changes == null || changes.size() == 0) {
            println "No changes detected."
            return
        }

        if (changes != null && changes.size() > 0) {
            changes.each { FileChange change ->
                println "File change: ${change.file}"
            }
            var fileItr = changes.iterator()
            while (fileItr.hasNext()) {
                filePathBb.append(fileItr.next().file.getAbsolutePath())
                if (fileItr.hasNext()) {
                    filePathBb.append(File.pathSeparator)
                }
            }
        }

        // No source code to spoon.
        if (srcFolders.size() == 0) {
            return;
        }
        List<String> params = new LinkedList<>()

//        def files = srcFolders.getFiles()

        addParam(params, '-i', filePathBb.toString())
        addParam(params, '-o', outFolder.getAbsolutePath())
        addParam(params, '--compliance', '' + compliance)
        if (preserveFormatting) {
            addKey(params, '-f')
        }
        if (noClasspath) {
            addKey(params, '-x')
        }
        if (processors.size() != 0) {
            addParam(params, '-p', processors.join(File.pathSeparator))
        }
        if (!classpath.asPath.empty) {
            addParam(params, '--source-classpath', classpath.asPath)
        }

        def launcher = new JavaLocalsSpoonLauncher()
        launcher.setFillMethodName(this.fillMethodName)
        launcher.setArgs(params.toArray(new String[params.size()]))
        launcher.run()
        Thread.sleep(1 * 1000)
    }

    private printEnvironment(printer) {
        printer "----------------------------------------"
        printer "source folder: $srcFolders"
        printer "output folder: $outFolder"
        printer "preserving formatting: $preserveFormatting"
        printer "no classpath: $noClasspath"
        printer "processors: $processors"
        printer "classpath: $classpath.asPath"
        printer "----------------------------------------"
    }

    private static void addParam(List params, key, value) {
        addKey(params, key)
        params.add(value)
    }

    private static void addKey(params, key) {
        params.add(key)
    }
}
