package xyz.ibudai.utils;

import xyz.ibudai.model.DbEntity;
import xyz.ibudai.model.common.DbConst;
import xyz.ibudai.model.common.DbType;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class LoaderUtil {

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
                dbEntity.setDriverClassName(DbConst.MYSQL_DRIVER);
                dbEntity.setDriverLocation(DbConst.MYSQL_DRIVER_PATH);
                break;
            case ORACLE:
                dbEntity.setUrl(DbConst.ORACLE_URL);
                dbEntity.setUser(DbConst.ORACLE_USERNAME);
                dbEntity.setPassword(DbConst.ORACLE_PASSWORD);
                dbEntity.setDriverClassName(DbConst.ORACLE_DRIVER);
                dbEntity.setDriverLocation(DbConst.ORACLE_DRIVER_PATH);
                break;
            case HIVE:
                dbEntity.setUrl(DbConst.HIVE_URL);
                dbEntity.setUser(DbConst.HIVE_USERNAME);
                dbEntity.setPassword(DbConst.HIVE_PASSWORD);
                dbEntity.setDriverClassName(DbConst.HIVE_DRIVER);
                dbEntity.setDriverLocation(DbConst.HIVE_DRIVER_PATH);
                break;
        }
        return dbEntity;
    }

    /**
     * Get class loader.
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
}
