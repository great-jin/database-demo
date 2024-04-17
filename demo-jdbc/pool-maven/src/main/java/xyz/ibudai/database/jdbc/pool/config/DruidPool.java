package xyz.ibudai.database.jdbc.pool.config;

import com.alibaba.druid.pool.DruidDataSource;
import xyz.ibudai.database.jdbc.pool.model.DbEntity;

public class DruidPool {

    /**
     * Build datasource data source.
     *
     * @param dbEntity the db entity
     * @return the data source
     */
    public static DruidDataSource buildDatasource(DbEntity dbEntity) {
        DruidDataSource dataSource = new DruidDataSource();
        // 基本连接信息
        dataSource.setUrl(dbEntity.getUrl());
        dataSource.setUsername(dbEntity.getUser());
        dataSource.setPassword(dbEntity.getPassword());
        dataSource.setDriverClassName(dbEntity.getDriverClassName());
        // 初始化连接池大小
        dataSource.setInitialSize(10);
        // 连接池的最大数据库连接数, 为 0 表示无限制
        dataSource.setMaxActive(10);
        // 最大建立连接等待时间, 如果超过此时间将接到异常。设为 -1 表示无限制
        dataSource.setMaxWait(5 * 1000);
        // 会话超时时间, 低版本无该两项参数需手动配置于 url 中
        // connectTimeout=60000?socketTimeout=60000
        dataSource.setConnectTimeout(10 * 60 * 1000);
        dataSource.setSocketTimeout(10 * 60 * 1000);
        // 执行超时时间
        dataSource.setQueryTimeout(10 * 60 * 1000);
        dataSource.setValidationQueryTimeout(10 * 60 * 1000);
        // 数据库属性配置
        dataSource.setConnectionProperties(dbEntity.getDatabaseProp().toString());
        return dataSource;
    }
}
