package github.e1roy.javalocals.example;

import javax.xml.transform.Source;

/**
 * 执行此代码, 需要先将插件安装在本地(在插件项目中执行gradle publishToMavenLocal), 然后在此项目中执行gradle build
 *
 * @author elroysu
 * @date 2024/12/29 02:11
 */
public class Main {
    public static void main(String[] args) {
        new Main().doSomething();
    }

    public void doSomething() {
        int a = 1;
        printLocals();
    }

    public static Object printLocals(Object... args) {
        System.out.println("*** printLocals ***");
        for (int i = 0; i < args.length; i = i+2) {
            System.out.println(args[i] + " = " + args[i+1]);
        }
        System.out.println("*** printLocals ***");
        return null;
    }

}