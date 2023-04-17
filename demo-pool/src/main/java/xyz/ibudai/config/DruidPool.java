package xyz.ibudai.config;

import com.alibaba.druid.pool.DruidDataSource;
import xyz.ibudai.consts.DbType;
import xyz.ibudai.utils.DriverUtil;

import java.util.Properties;

/**
 * The type Druid pool.
 */
public class DruidPool {

    /**
     * Build datasource data source.
     *
     * @param dbType the db type
     * @return the data source
     */
    public static DruidDataSource buildDatasource(DbType dbType) {
        String[] dbcpInfo = DriverUtil.buildDbInfo(dbType);
        DruidDataSource dataSource = new DruidDataSource();
        // 基本连接信息
        dataSource.setUrl(dbcpInfo[0]);
        dataSource.setUsername(dbcpInfo[1]);
        dataSource.setPassword(dbcpInfo[2]);
        dataSource.setDriverClassName(dbcpInfo[3]);
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

        Properties connectProps = new Properties();
        // 空字符串问题
        connectProps.put("useFetchSizeWithLongColumn", "true");
        dataSource.setConnectionProperties(connectProps.toString());
        return dataSource;
    }
}
