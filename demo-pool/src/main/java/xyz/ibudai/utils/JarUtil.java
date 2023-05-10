package xyz.ibudai.utils;

import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class JarUtil {

    @Test
    public void demo() throws Exception {
        String path = "E:\\Workspace\\Driver";
        File fileDir = new File(path);
        File[] files = fileDir.listFiles();
        for (File file : files) {
            if (!file.isFile()) {
                continue;
            }
            String jaPath = file.getAbsolutePath();
            String s = analyzeJar(jaPath);
            System.out.println(file.getName() + ": " + s);
        }
    }

    public String analyzeJar(String jarPath) throws Exception {
        URL url = new URL("file:" + jarPath);
        URLClassLoader classLoader = new URLClassLoader(new URL[]{url});
        JarFile jarFile = new JarFile(jarPath);
        Manifest manifest = jarFile.getManifest();
        String driverClassName = manifest.getMainAttributes().getValue("Main-Class");
        jarFile.close();
        classLoader.close();
        return driverClassName;
    }
}
