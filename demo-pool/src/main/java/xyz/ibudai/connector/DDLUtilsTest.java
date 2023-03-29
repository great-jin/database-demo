package xyz.ibudai.connector;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.junit.Test;
import xyz.ibudai.common.DbType;
import xyz.ibudai.config.BasicPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DDLUtilsTest {

    @Test
    public void demo() {
        // Establish a connection to the database
        DataSource dataSource = BasicPool.buildDatasource(DbType.MYSQL);
        // Use DDLUtils to extract the database model
        // 创建 DdlUtils 数据库平台对象
        Platform platform = PlatformFactory.createNewPlatformInstance(dataSource);

        try (Connection connection = dataSource.getConnection()) {
            // 获取数据库模型
            Database database = platform.readModelFromDatabase(connection, null);

            // 获取表更新时间
            String tableName = "tb_user";
            Table table = database.findTable(tableName);
            System.out.println("表 " + table);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void demo1() {
        String tableName = "tb_test";
        DataSource dataSource = BasicPool.buildDatasource(DbType.ORACLE);

        try (Connection connection = dataSource.getConnection()) {
            String driver = "oracle.jdbc.OracleDriver";
            String jdbcUrl = "jdbc:oracle:thin:@//10.231.6.21:1521/helowin";
            Platform platform = PlatformFactory.createNewPlatformInstance(driver, jdbcUrl);
            Database database = platform.readModelFromDatabase(connection, null);
            Table table = database.findTable(tableName, false);
            System.out.println(table);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
