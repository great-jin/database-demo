package xyz.ibudai.utils;

import java.util.Properties;

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
        dataSource.setInitialSize(10);
        // 连接池的最大数据库连接数, 为 0 表示无限制
        dataSource.setMaxActive(10);
        // 最大建立连接等待时间, 如果超过此时间将接到异常。设为 -1 表示无限制
        dataSource.setMaxWait(3000);
        // 数据库连接的最大空闲时间, 超过空闲时间数据库连接将被标记为不可用，然后被释放, 为 0 表示无限制。
        dataSource.setMaxIdle(1000);

        Properties connectProps = new Properties();
        // 空字符串问题
        connectProps.put("useFetchSizeWithLongColumn", "true");
        dataSource.setConnectionProperties(connectProps.toString());
        return dataSource;
    }
}
