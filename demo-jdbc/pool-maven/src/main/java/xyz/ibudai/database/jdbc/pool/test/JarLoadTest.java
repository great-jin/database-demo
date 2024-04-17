package xyz.ibudai.database.jdbc.pool.test;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;
import xyz.ibudai.database.jdbc.pool.config.BasicPool;
import xyz.ibudai.database.jdbc.pool.model.DbEntity;
import xyz.ibudai.database.jdbc.pool.model.common.DbType;
import xyz.ibudai.database.jdbc.pool.utils.LoaderUtil;

import java.sql.Connection;
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
