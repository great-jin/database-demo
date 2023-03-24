package xyz.ibudai;

import org.junit.Before;
import org.junit.Test;
import xyz.ibudai.common.DbType;
import xyz.ibudai.model.JDBCProperty;
import xyz.ibudai.config.BasicPool;
import xyz.ibudai.config.DruidPool;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

public class PoolTest {

    private static final Properties prop = new Properties();
    private static final JDBCProperty jdbcProp = new JDBCProperty();

    @Before
    public void load() {
        try (
                InputStream in = getClass().getResourceAsStream("/jdbc.properties");
                InputStreamReader inReader = new InputStreamReader(in, StandardCharsets.UTF_8);
        ) {
            prop.load(inReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        jdbcProp.setDriver(prop.getProperty("jdbc.driver"));
        jdbcProp.setUrl(prop.getProperty("jdbc.url"));
        jdbcProp.setUser(prop.getProperty("jdbc.user"));
        jdbcProp.setPassword(prop.getProperty("jdbc.password"));
    }

    @Test
    public void demo1() {
        String sql = "select * from tb_test";
        List<Map<String, Object>> list = new ArrayList<>();

        DataSource dataSource = BasicPool.buildDatasource(DbType.MYSQL);
        try (
                Connection con = dataSource.getConnection();
                Statement stmt = con.createStatement()
        ) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ResultSetMetaData metaData = rs.getMetaData();
                Map<String, Object> rowsMap = new LinkedHashMap<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    rowsMap.put(metaData.getColumnName(i), rs.getString(i));
                }
                list.add(rowsMap);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(list);
    }

    @Test
    public void demo2() {
        String sql = "select sleep(30)";
        DataSource dataSource = DruidPool.buildDatasource(DbType.MYSQL);
        try (
                Connection con = dataSource.getConnection();
                Statement stmt = con.createStatement()
        ) {
            stmt.execute(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
