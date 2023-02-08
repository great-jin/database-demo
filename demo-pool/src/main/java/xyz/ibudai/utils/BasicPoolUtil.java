package xyz.ibudai.utils;

import org.apache.commons.dbcp.BasicDataSource;
import xyz.ibudai.model.JDBCProperty;

import javax.sql.DataSource;

public class BasicPoolUtil {

    public static DataSource buildDatasource(JDBCProperty property) {
        BasicDataSource dataSource = new BasicDataSource();
        // 基本连接信息
        dataSource.setDriverClassName(property.getDriver());
        dataSource.setUrl(property.getUrl());
        dataSource.setUsername(property.getUser());
        dataSource.setPassword(property.getPassword());
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
