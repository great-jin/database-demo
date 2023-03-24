package xyz.ibudai.config;

import org.apache.commons.dbcp.BasicDataSource;
import xyz.ibudai.common.DbType;
import xyz.ibudai.utils.DriverUtil;

import javax.sql.DataSource;

/**
 * The type Basic pool.
 */
public class BasicPool {

    /**
     * Build datasource data source.
     *
     * @param dbType the db type
     * @return the data source
     */
    public static DataSource buildDatasource(DbType dbType) {
        String[] dbcpInfo = DriverUtil.buildDbInfo(dbType);
        BasicDataSource dataSource = new BasicDataSource();
        // 基本连接信息
        dataSource.setUrl(dbcpInfo[0]);
        dataSource.setUsername(dbcpInfo[1]);
        dataSource.setPassword(dbcpInfo[2]);
        dataSource.setDriverClassName(dbcpInfo[3]);
        // 通过驱动包自定义类加载器
        dataSource.setDriverClassLoader(DriverUtil.getClassLoader(dbcpInfo[4]));
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
