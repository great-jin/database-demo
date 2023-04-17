package xyz.ibudai;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import xyz.ibudai.consts.DbType;
import xyz.ibudai.config.BasicPool;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PoolTest {

    @Test
    public void load() {
        Properties prop = new Properties();
        try (
                InputStream in = getClass().getResourceAsStream("/jdbc.properties");
                InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
        ) {
            prop.load(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(prop);
    }

    @Test
    public void demo1() {
        String sql = "select sleep(1)";
        BasicDataSource dataSource = BasicPool.buildDatasource(DbType.MYSQL);
        for (int i = 0; i < 6; i++) {
            try (
                    Connection con = dataSource.getConnection();
                    Statement stmt = con.createStatement()
            ) {
                stmt.execute(sql);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Num Idle: " + dataSource.getNumIdle());
        System.out.println("Num Active: " + dataSource.getNumActive());
    }

    @Test
    public void demo2() throws Exception {
        String sql = "select sleep(1)";
        BasicDataSource dataSource = BasicPool.buildDatasource(DbType.MYSQL);
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Num Idle: " + dataSource.getNumIdle());
        System.out.println("Num Active: " + dataSource.getNumActive());
        TimeUnit.SECONDS.sleep(15);
        System.out.println("Is closed: " + conn.isClosed());
    }
}
