package xyz.ibudai.database.jdbc.pool.config;

import org.apache.commons.dbcp2.BasicDataSource;
import xyz.ibudai.database.jdbc.pool.model.DbEntity;
import xyz.ibudai.database.jdbc.pool.utils.LoaderUtil;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class BasicPool {

    /**
     * Build datasource data source.
     *
     * @param dbEntity the db entity
     * @return the data source
     */
    public static BasicDataSource buildDatasource(DbEntity dbEntity) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(dbEntity.getUrl());
        dataSource.setUsername(dbEntity.getUser());
        dataSource.setPassword(dbEntity.getPassword());
        dataSource.setDriverClassName(dbEntity.getDriverClassName());
        // 自定义 Driver 加载
        ClassLoader classLoader = LoaderUtil.getClassLoader(dbEntity.getDriverLocation());
        dataSource.setDriverClassLoader(classLoader);
        // 初始化连接池大小
        dataSource.setInitialSize(3);
        // 最大数据库连接数, 为 -1 表示无限制
        dataSource.setMaxTotal(6);
        // 最大空闲连接数, 超过即被释放, 为 -1 表示无限制
        dataSource.setMaxIdle(3);
        // 建立连接最大等待时间, 为 -1 表示无限制
        dataSource.setMaxWaitMillis(3 * 1000);
        // 超过 MaxIdle 的连接最长存活时间，默认 30m
        dataSource.setMinEvictableIdleTimeMillis(TimeUnit.SECONDS.toMillis(30L));
        // 空闲连接清理线程运行间隔周期, 默认 60s
        dataSource.setTimeBetweenEvictionRunsMillis(TimeUnit.SECONDS.toMillis(60L));
        // 数据库属性配置
        Properties databaseProp = dbEntity.getDatabaseProp();
        if (Objects.nonNull(databaseProp)) {
            for (Map.Entry<Object, Object> entry : databaseProp.entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                dataSource.addConnectionProperty(key, value);
            }
        }
        return dataSource;
    }
}
