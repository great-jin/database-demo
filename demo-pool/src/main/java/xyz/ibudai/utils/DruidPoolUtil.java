package xyz.ibudai.utils;

import com.alibaba.druid.pool.DruidDataSource;
import xyz.ibudai.model.JDBCProperty;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

public class DruidPoolUtil {

    public static DataSource buildDatasource(JDBCProperty property) {
        DruidDataSource dataSource = new DruidDataSource();
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

        Properties connectProps = new Properties();
        // 空字符串问题
        connectProps.put("useFetchSizeWithLongColumn", "true");
        dataSource.setConnectionProperties(connectProps.toString());

        return dataSource;
    }
}
