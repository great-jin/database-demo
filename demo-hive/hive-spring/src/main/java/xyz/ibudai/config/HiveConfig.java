package xyz.ibudai.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import com.alibaba.druid.pool.DruidDataSource;

@Data
@Configuration
public class HiveConfig {

    @Value("${hive.url}")
    private String url;
    @Value("${hive.user}")
    private String user;
    @Value("${hive.password}")
    private String password;
    @Value("${hive.driver-class-name}")
    private String driverClassName;
    @Value("${hive.initialSize}")
    private int initialSize;
    @Value("${hive.minIdle}")
    private int minIdle;
    @Value("${hive.maxActive}")
    private int maxActive;
    @Value("${hive.maxWait}")
    private int maxWait;
    @Value("${hive.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;
    @Value("${hive.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;
    @Value("${hive.validationQuery}")
    private String validationQuery;
    @Value("${hive.testWhileIdle}")
    private boolean testWhileIdle;
    @Value("${hive.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${hive.testOnReturn}")
    private boolean testOnReturn;
    @Value("${hive.poolPreparedStatements}")
    private boolean poolPreparedStatements;
    @Value("${hive.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Bean(name = "hiveDruidTemplate")
    public JdbcTemplate hiveDruidTemplate() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);

        // pool configuration
        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        dataSource.setPoolPreparedStatements(poolPreparedStatements);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        return new JdbcTemplate(dataSource);
    }
}
