package xyz.ibudai.utils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

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


    @Test
    public void demo1() throws Exception {
        String path = "E:\\Workspace\\Driver";
        File fileDir = new File(path);
        File[] files = fileDir.listFiles();
        for (File file : files) {
            if (!file.isFile()) {
                continue;
            }
            Set<String> clazz = getClassNamesFromJarFile(file);
            clazz = clazz.stream().filter(e -> e.contains("Driver")).collect(Collectors.toSet());
            System.out.println(file.getName() + ": " + clazz);
            System.out.println();
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

    public static Set<String> getClassNamesFromJarFile(File file) throws IOException {
        Set<String> classNames = new HashSet<>();
        try (JarFile jarFile = new JarFile(file)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                if (jarEntry.isDirectory()) {
                    continue;
                }
                String jarName = jarEntry.getName();
                if (jarName.endsWith(".class")) {
                    String className = jarName.replace("/", ".")
                            .replace(".class", "");
                    classNames.add(className);
                }
            }
            return classNames;
        }
    }
}
