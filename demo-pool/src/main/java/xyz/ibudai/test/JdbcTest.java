package xyz.ibudai.test;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;
import xyz.ibudai.model.DbEntity;
import xyz.ibudai.model.common.DbType;
import xyz.ibudai.config.BasicPool;
import xyz.ibudai.utils.LoaderUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

public class JdbcTest {

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
    public void demo2() {
        String sql = "select 'a' as name from dual";
        DbEntity dbEntity = LoaderUtil.buildDbInfo(DbType.ORACLE);
        try (
                BasicDataSource dataSource = BasicPool.buildDatasource(dbEntity);
                Connection con = dataSource.getConnection();
                Statement stmt = con.createStatement()
        ) {
            stmt.execute(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
