package xyz.ibudai;

import com.sun.rowset.CachedRowSetImpl;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;
import xyz.ibudai.model.DbDriverEntity;
import xyz.ibudai.model.DbEntity;
import xyz.ibudai.model.common.DbType;
import xyz.ibudai.config.BasicPool;
import xyz.ibudai.utils.DriverUtil;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

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
        DbEntity dbEntity = DriverUtil.buildDbInfo(DbType.ORACLE);
        DbDriverEntity driverEntity = DriverUtil.getDriverEntity(dbEntity);
        System.out.println(driverEntity);
    }

    @Test
    public void demo2() {
        String sql = "select 'a' as name from dual";
        BasicDataSource dataSource = BasicPool.buildDatasource(DbType.ORACLE);
        for (int i = 0; i < 1; i++) {
            try (
                    Connection con = dataSource.getConnection();
                    Statement stmt = con.createStatement();
            ) {
                RowSetFactory factory = RowSetProvider.newFactory();
                try (
                        ResultSet rs = stmt.executeQuery(sql);
                        CachedRowSet rowSet = factory.createCachedRowSet();
                ) {
                    rowSet.populate(rs);
                    while (rowSet.next()) {
                        // do something
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
