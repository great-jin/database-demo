package xyz.ibudai.utils;

import org.apache.commons.dbcp.BasicDataSource;
import xyz.ibudai.model.JDBCProperty;

import javax.sql.DataSource;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class BasicPoolUtil {

    private static final String MYSQL_PATH = "src\\main\\resources\\jar\\mysql-connector-java.jar";
    private static final String ORACLE_PATH = "src\\main\\resources\\jar\\ojdbc6-11.2.0.3.jar";

    public static DataSource buildDatasource(JDBCProperty property) {
        BasicDataSource dataSource = new BasicDataSource();
        // 基本连接信息
        dataSource.setDriverClassName(property.getDriver());
        dataSource.setUrl(property.getUrl());
        dataSource.setUsername(property.getUser());
        dataSource.setPassword(property.getPassword());
        // 通过驱动包自定义类加载器
        dataSource.setDriverClassLoader(getClassLoader(MYSQL_PATH));
        // 初始化连接池大小
        dataSource.setInitialSize(3);
        // 最大数据库连接数, 为 0 表示无限制
        dataSource.setMaxActive(6);
        // 最大空闲连接数, 超过即被释放, 为 0 表示无限制
        dataSource.setMaxIdle(3);
        // 建立连接最大等待时间, 为 -1 表示无限制
        dataSource.setMaxWait(3000);
        return dataSource;
    }

    private static ClassLoader getClassLoader(String modulePath) {
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        List<URL> urls = new ArrayList<>();
        try {
            File modulePathFile = new File(modulePath);
            if (!modulePathFile.exists()) {
                throw new ClassNotFoundException();
            }
            if (modulePathFile.isDirectory()) {
                for (File file : modulePathFile.listFiles()) {
                    urls.add(file.toURI().toURL());
                }
            } else {
                urls.add(modulePathFile.toURI().toURL());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new URLClassLoader(urls.toArray(new URL[0]), parent);
    }
}
