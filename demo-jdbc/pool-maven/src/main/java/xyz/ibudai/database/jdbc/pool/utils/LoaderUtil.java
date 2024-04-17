package xyz.ibudai.database.jdbc.pool.utils;

import xyz.ibudai.database.jdbc.pool.loader.JarClassLoader;
import xyz.ibudai.database.jdbc.pool.model.DbEntity;
import xyz.ibudai.database.jdbc.pool.model.common.DbConst;
import xyz.ibudai.database.jdbc.pool.model.common.DbType;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
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
        List<URL> list = new ArrayList<>();
        try {
            File driver = new File(driverPath);
            // 驱动包不存在抛出异常
            if (!driver.exists()) {
                throw new FileNotFoundException();
            }
            // 转为 URL 文件资源
            list.add(driver.toURI().toURL());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 获取当前线程上下文加载器
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        return new JarClassLoader(list.toArray(new URL[0]), parent);
    }
}
