package xyz.ibudai.database.hive.config;

import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;

public class BasicPoolUtil {

    private final static String URL = "jdbc:mysql://192.168.173.42:3306/hive";
    private final static String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private final static String USER_NAME = "root";
    private final static String PASSWORD = "Bigdata@Tg0p.lw#m";


    public static DataSource buildDatasource() {
        BasicDataSource dataSource = new BasicDataSource();
        // 基本连接信息
        dataSource.setDriverClassName(DRIVER_NAME);
        dataSource.setUrl(URL);
        dataSource.setUsername(USER_NAME);
        dataSource.setPassword(PASSWORD);
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
}
