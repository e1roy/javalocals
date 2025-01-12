package io.github.e1roy;

import org.gradle.api.Project;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * @author elroysu
 * @date 2024/12/29 01:30
 */
public class JavaLocalsPluginTest {
    @Test
    public void pluginRegistersATask() throws Exception {
        // Create a test project and apply the plugin
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("java");
        project.getPlugins().apply("io.github.e1roy.javalocals");

        // 模拟增加配置文件
        project.getExtensions().getByName("javaLocals"); // 获取插件扩展对象
        project.getExtensions().configure("javaLocals", javaLocals -> {
            try {
                javaLocals.getClass().getMethod("setEnable", boolean.class).invoke(javaLocals, true);
                javaLocals.getClass().getMethod("setCompileOriginalSources", boolean.class).invoke(javaLocals, false);
                javaLocals.getClass().getMethod("setCompliance", int.class).invoke(javaLocals, 11);
//                javaLocals.getClass().getMethod("setProcessors", List.class).invoke(javaLocals,
//                        List.of("io.github.e1roy.JavaLocalsProcessor"));
                javaLocals.getClass().getMethod("setFillMethodName", String.class).invoke(javaLocals, "printLocals");
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        // Properly evaluate the project using ProjectInternal
        ((ProjectInternal) project).evaluate();

        // Verify the result
        assertNotNull("JavaLocalsTransform task should be registered after project evaluation",
                project.getTasks().findByName("javaLocalsTransform"));
    }
}
