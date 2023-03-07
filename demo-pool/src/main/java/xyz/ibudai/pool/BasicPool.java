package xyz.ibudai.pool;

import org.apache.commons.dbcp.BasicDataSource;
import xyz.ibudai.model.JDBCProperty;
import xyz.ibudai.utils.DriverUtil;

import javax.sql.DataSource;

public class BasicPool {

    /**
     * 绝对路径，相对路径不生效
     */
    private static final String MYSQL_PATH = "E:\\Workspace\\Driver\\mysql-connector-java.jar";
    private static final String ORACLE_PATH = "E:\\Workspace\\Driver\\ojdbc6-11.2.0.3.jar";

    public static DataSource buildDatasource(JDBCProperty property) {
        BasicDataSource dataSource = new BasicDataSource();
        // 基本连接信息
        dataSource.setUrl(property.getUrl());
        dataSource.setUsername(property.getUser());
        dataSource.setPassword(property.getPassword());
        dataSource.setDriverClassName(property.getDriver());
        // 通过驱动包自定义类加载器
        dataSource.setDriverClassLoader(DriverUtil.getClassLoader(MYSQL_PATH));
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
