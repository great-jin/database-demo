package xyz.ibudai.loader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarClassLoader extends URLClassLoader {

    /**
     * Use default parent class load
     */
    public JarClassLoader(URL[] urls) {
        super(urls);
    }

    /**
     * Specify the parent class load
     */
    public JarClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                try {
                    c = findClass(name);
                } catch (ClassNotFoundException ignored) {
                    c = super.loadClass(name);
                }
            }
            if (c == null) {
                c = super.loadClass(name);
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classBytes;
        try {
            classBytes = getClassByteFromJar(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (classBytes.length == 0) {
            throw new ClassNotFoundException("The class byte " + name + " is empty.");
        }
        return this.defineClass(name, classBytes, 0, classBytes.length);
    }

    private byte[] getClassByteFromJar(String className) throws IOException {
        String path = this.getURLs()[0].getPath();
        path = path.substring(1);
        try (JarFile jarFile = new JarFile(path)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String jarName = jarEntry.getName();
                if (jarEntry.isDirectory() || !jarName.endsWith(".class")) {
                    continue;
                }
                jarName = jarName.replace("/", ".");
                jarName = jarName.replace(".class", "");
                if (jarName.equals(className)) {
                    try (
                            InputStream in = jarFile.getInputStream(jarEntry);
                            ByteArrayOutputStream os = new ByteArrayOutputStream()
                    ) {
                        byte[] buffer = new byte[0xFFFF];
                        for (int len; (len = in.read(buffer)) != -1; ) {
                            os.write(buffer, 0, len);
                        }
                        os.flush();
                        return os.toByteArray();
                    } catch (IOException e) {
                        throw new IOException(e);
                    }
                }
            }
            throw new NullPointerException("Class not found.");
        }
    }
}
