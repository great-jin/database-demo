package xyz.ibudai.utils;

import xyz.ibudai.model.DbEntity;
import xyz.ibudai.model.DriverEntity;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DriverUtil {

    private static final String driverDir = "E:\\Workspace\\Driver";

    /**
     * Get db driver className
     *
     * @param entity
     */
    public static DriverEntity getDriverEntity(DbEntity entity) {
        List<DriverEntity> driverList = getDriverList(driverDir);
        DriverEntity driver = driverList.stream()
                .filter(vo -> {
                    boolean flag = false;
                    if (entity.getUrl().startsWith("jdbc:oracle")) {
                        if (vo.getLocation().contains("ojdbc6")) {
                            flag = true;
                        }
                    } else {
                        if (vo.getClassName().equals(entity.getDriverClassName())) {
                            flag = true;
                        }
                    }
                    return flag;
                })
                .findFirst()
                .orElse(null);
        if (Objects.isNull(driver)) {
            throw new RuntimeException("Driver didn't march.");
        }
        return driver;
    }

    /**
     * Get Driver basic info.
     *
     * @param directory local driver directory
     */
    public static List<DriverEntity> getDriverList(String directory) {
        File driverDir = new File(directory);
        if (!driverDir.exists()) {
            throw new IllegalArgumentException("Directory " + directory + " doesn't existed.");
        }
        File[] driverList = driverDir.listFiles();
        List<DriverEntity> fileList = new ArrayList<>();
        for (File file : Objects.requireNonNull(driverList)) {
            if (!file.isFile()) {
                continue;
            }
            String fileName = file.getName();
            if (fileName.toLowerCase().endsWith(".jar")) {
                String filePath = file.getAbsolutePath();
                String className;
                try {
                    className = getDriverName(filePath);
                } catch (Exception e) {
                    continue;
                }
                DriverEntity driver = new DriverEntity();
                driver.setFileName(fileName);
                driver.setLocation(filePath);
                driver.setClassName(className);
                fileList.add(driver);
            }
        }
        return fileList;
    }

    /**
     * Get class name from Driver file
     *
     * @param driverPath driver store path
     */
    private static String getDriverName(String driverPath) {
        String className = null;
        File driverFile = new File(driverPath);
        if (!driverFile.exists() || !driverFile.isFile()) {
            throw new IllegalArgumentException("File " + driverPath + " didn't exist or isn't file");
        }
        String fileName = driverFile.getName();
        if (!fileName.endsWith(".jar")) {
            throw new IllegalArgumentException("File " + driverPath + " isn't jar");
        }

        try (
                URLClassLoader classLoader = new URLClassLoader(new URL[]{driverFile.toURI().toURL()});
                ZipFile zip = new ZipFile(driverFile)
        ) {
            Enumeration<?> enumZip = zip.entries();
            while (enumZip.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) enumZip.nextElement();
                String itemName = entry.getName();
                if (entry.isDirectory() || !itemName.endsWith(".class")) {
                    continue;
                }
                // 将驱动类路径中的 / 替换成.
                className = itemName.replace("/", ".");
                className = className.substring(0, className.lastIndexOf(".class"));
                Class<?> c;
                try {
                    c = classLoader.loadClass(className);
                } catch (Throwable t) {
                    return className;
                }
                // 判断驱动类是 java.sql.Driver 的子类, 是否为一个抽象类
                if (java.sql.Driver.class.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers())) {
                    return className;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return className;
    }
}
