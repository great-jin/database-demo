package xyz.ibudai.test;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;
import xyz.ibudai.config.BasicPool;
import xyz.ibudai.model.DbEntity;
import xyz.ibudai.model.common.DbType;
import xyz.ibudai.utils.LoaderUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class JarLoadTest {

    @Test
    public void demo1() {
        String sql = "select 'a' as name from dual";
        DbEntity dbEntity = LoaderUtil.buildDbInfo(DbType.ORACLE);
        try (
                BasicDataSource dataSource = BasicPool.buildDatasource(dbEntity);
                Connection con = dataSource.getConnection();
                Statement stmt = con.createStatement();
        ) {
            stmt.execute(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
