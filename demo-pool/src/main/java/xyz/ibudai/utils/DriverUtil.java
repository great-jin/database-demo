package xyz.ibudai.utils;

import xyz.ibudai.model.common.DbType;
import xyz.ibudai.model.common.DbConst;
import xyz.ibudai.model.DbEntity;
import xyz.ibudai.model.DbDriverEntity;
import xyz.ibudai.model.DriverPackageEntity;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * The type Driver util.
 */
public class DriverUtil {

    /**
     * Build db info string [ ].
     *
     * @param dbType the db type
     * @return the string [ ]
     */
    public static DbEntity buildDbInfo(DbType dbType) {
        DbEntity dbEntity = new DbEntity();
        switch (dbType) {
            case MYSQL:
                dbEntity.setUrl(DbConst.MYSQL_URL);
                dbEntity.setUser(DbConst.MYSQL_USERNAME);
                dbEntity.setPassword(DbConst.MYSQL_PASSWORD);
                dbEntity.setDriverName(DbConst.MYSQL_DRIVER_1);
                dbEntity.setDriverLocation(DbConst.MYSQL_DRIVER_PATH);
                break;
            case ORACLE:
                dbEntity.setUrl(DbConst.ORACLE_URL);
                dbEntity.setUser(DbConst.ORACLE_USERNAME);
                dbEntity.setPassword(DbConst.ORACLE_PASSWORD);
                dbEntity.setDriverName(DbConst.ORACLE_DRIVER);
                dbEntity.setDriverLocation(DbConst.ORACLE_DRIVER_PATH);
                break;
            case HIVE:
                dbEntity.setUrl(DbConst.HIVE_URL);
                dbEntity.setUser(DbConst.HIVE_USERNAME);
                dbEntity.setPassword(DbConst.HIVE_PASSWORD);
                dbEntity.setDriverName(DbConst.HIVE_DRIVER);
                dbEntity.setDriverLocation(DbConst.HIVE_DRIVER_PATH);
                break;
        }
        return dbEntity;
    }

    /**
     * Gets class loader.
     *
     * @param driverPath the driver path
     * @return the class loader
     */
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

    public static DbDriverEntity getDriverEntity(DbEntity entity) {
        List<DriverPackageEntity> driverList = getDriverList("E:\\Workspace\\Driver\\lib");
        DriverPackageEntity driver = driverList.stream()
                .filter(vo -> {
                    boolean flag = false;
                    if (entity.getUrl().startsWith("jdbc:oracle")) {
                        if (vo.getPath().contains("ojdbc6")) {
                            flag = true;
                        }
                    } else {
                        if (vo.getClassName().equals(entity.getDriverName())) {
                            flag = true;
                        }
                    }
                    return flag;
                })
                .findFirst()
                .orElse(null);
        DbDriverEntity driverEntity = new DbDriverEntity();
        if (driver != null) {
            driverEntity.setDriverName(driver.getClassName());
            driverEntity.setDriverLocation(driver.getPath());
        }
        return driverEntity;
    }

    public static List<DriverPackageEntity> getDriverList(String path) {
        File driverDir = new File(path);
        if (!driverDir.exists()) {
            throw new IllegalArgumentException("File " + path + " doesn't existed.");
        }
        File[] driverList = driverDir.listFiles();
        List<DriverPackageEntity> fileList = new ArrayList<>();
        for (File file : driverList) {
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
                DriverPackageEntity driver = new DriverPackageEntity();
                driver.setName(fileName);
                driver.setPath(filePath);
                driver.setClassName(className);
                fileList.add(driver);
            }
        }
        return fileList;
    }

    private static String getDriverName(String driverPath) {
        String className = null;
        File driverFile = new File(driverPath);
        if (!driverFile.isFile() || !driverFile.exists()) {
            throw new IllegalArgumentException("File " + driverFile + " doesn't existed.");
        }

        String fileName = driverFile.getName();
        if (!fileName.endsWith(".zip") && !fileName.endsWith(".jar")) {
            throw new IllegalArgumentException("File " + driverFile + " is not jar or zip.");
        }
        try (
                URLClassLoader classLoader = new URLClassLoader(new URL[]{driverFile.toURI().toURL()});
                ZipFile zip = new ZipFile(driverFile)
        ) {
            String tempName;
            Enumeration<?> enumZip = zip.entries();
            while (enumZip.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) enumZip.nextElement();
                if (!entry.isDirectory()) {
                    if ((tempName = entry.getName()).endsWith(".class") && !tempName.contains("$")) {
                        // 将驱动类路径中的 / 替换成.
                        className = tempName.substring(0, tempName.lastIndexOf(".class"));
                        className = className.replaceAll("/", ".");
                        Class<?> c;
                        try {
                            c = classLoader.loadClass(className);
                        } catch (Throwable t) {
                            return className;
                        }
                        // 判断驱动类是 java.sql.Driver 的子类, 是否为一个抽象类
                        // c1.isAssignableFrom(c2): 判断 c2 是不是 c1 的子类或接口实现类
                        if (java.sql.Driver.class.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers())) {
                            return className;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return className;
    }
}
