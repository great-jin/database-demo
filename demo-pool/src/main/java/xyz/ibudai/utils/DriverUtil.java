package xyz.ibudai.utils;

import xyz.ibudai.consts.DbType;
import xyz.ibudai.consts.DbConst;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

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
    public static String[] buildDbInfo(DbType dbType) {
        String[] dbcpInfo = new String[5];
        switch (dbType) {
            case MYSQL:
                dbcpInfo[0] = DbConst.MYSQL_URL;
                dbcpInfo[1] = DbConst.MYSQL_USERNAME;
                dbcpInfo[2] = DbConst.MYSQL_PASSWORD;
                dbcpInfo[3] = DbConst.MYSQL_DRIVER_1;
                dbcpInfo[4] = DbConst.MYSQL_PATH;
                break;
            case ORACLE:
                dbcpInfo[0] = DbConst.ORACLE_URL;
                dbcpInfo[1] = DbConst.ORACLE_USERNAME;
                dbcpInfo[2] = DbConst.ORACLE_PASSWORD;
                dbcpInfo[3] = DbConst.ORACLE_DRIVER;
                dbcpInfo[4] = DbConst.ORACLE_PATH_1;
                break;
            case HIVE:
                dbcpInfo[0] = DbConst.HIVE_URL;
                dbcpInfo[1] = DbConst.HIVE_USERNAME;
                dbcpInfo[2] = DbConst.HIVE_PASSWORD;
                dbcpInfo[3] = DbConst.HIVE_DRIVER;
                dbcpInfo[4] = DbConst.HIVE_PATH;
                break;
        }
        return dbcpInfo;
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
}
