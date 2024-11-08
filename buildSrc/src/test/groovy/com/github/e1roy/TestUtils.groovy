package com.github.e1roy;

import spoon.support.compiler.FileSystemFile;
import spoon.support.compiler.VirtualFile

/**
 * @author elroysu
 * @date 2024/11/8 星期五 0:37
 */
public class TestUtils {

    public static String addJavaSub(String fileName) {
        if (fileName.endsWith(".java")) {
            return fileName;
        }
        return fileName + ".java";
    }


    public static FileSystemFile getJavaSpoonFile(String resourceFileName) {
        return new FileSystemFile(getFile(addJavaSub(resourceFileName)));
    }


    public static FileSystemFile getSpoonFile(String resourceFileName) {
        return new FileSystemFile(getFile(resourceFileName));
    }

    public static File getFile(String resourceFileName) {
        // a.b.java -> a/b.java
        def filePath = resourceFileName.replace(".", "/").replace("/java", ".java");
        ClassLoader classLoader = TestUtils.class.getClassLoader();
        URL resource = classLoader.getResource(filePath);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + resourceFileName + " -> " + filePath);
        }
        return new File(resource.getFile());
    }

    public static VirtualFile getVirtualFile(String code) {
        return new VirtualFile(code);
    }

    public static String getMethodName(methodClosure) {
        return methodClosure.getMethod()
    }
}
