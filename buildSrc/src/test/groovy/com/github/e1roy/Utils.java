package com.github.e1roy;

import spoon.support.compiler.FileSystemFile;
import spoon.support.compiler.VirtualFile;

import java.io.File;
import java.net.URL;

/**
 * @author elroysu
 * @date 2024/11/8 星期五 0:37
 */
public class Utils {

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

        ClassLoader classLoader = Utils.class.getClassLoader();
        URL resource = classLoader.getResource(resourceFileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + resourceFileName);
        }
        return new File(resource.getFile());
    }

    public VirtualFile getVirtualFile(String code) {
        return new VirtualFile(code);
    }
}
