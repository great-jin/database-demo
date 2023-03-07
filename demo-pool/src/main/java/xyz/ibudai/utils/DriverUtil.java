package xyz.ibudai.utils;

import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class DriverUtil {

    @Test
    public void demo() {
        String path = "E:\\Workspace\\Driver\\mysql-connector-java.jar";
        ClassLoader classLoader = getClassLoader(path);
        System.out.println(classLoader);
    }

    public static ClassLoader getClassLoader(String driverPath) {
        // 获取当前线程上下文加载器
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        URL[] urls;
        try {
            List<URL> list = new ArrayList<>();
            File driver = new File(driverPath);
            // 驱动包不存在抛出异常
            if (!driver.exists()) {
                throw new ClassNotFoundException();
            }
            // 若为目录获取其下所有文件
            if (driver.isDirectory()) {
                for (File file : driver.listFiles()) {
                    list.add(file.toURI().toURL());
                }
            } else {
                // 驱动存在获取文件URL
                list.add(driver.toURI().toURL());
            }
            urls = list.toArray(new URL[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new URLClassLoader(urls, parent);
    }
}
