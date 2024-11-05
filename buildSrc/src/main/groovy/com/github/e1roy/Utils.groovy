package com.github.e1roy

final class Utils {
    /**
     * Transforms list of File to list of String.
     */
    def static String[] transformListFileToListString(project, srcDirs) {
        def inputs = []
        srcDirs.each() {
            if (project.file(it).exists()) {
                inputs.add(it.getAbsolutePath())
            }
        };
        return inputs
    }

}
