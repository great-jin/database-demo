package xyz.ibudai.basic;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import xyz.ibudai.model.DbEntity;
import xyz.ibudai.model.common.DbType;
import xyz.ibudai.config.BasicPool;
import xyz.ibudai.utils.LoaderUtil;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

public class JdbcTest {

    private BasicDataSource dataSource;

    @Before
    public void init() {
        DbEntity dbEntity = LoaderUtil.buildDbInfo(DbType.ORACLE);
        dataSource = BasicPool.buildDatasource(dbEntity);
    }

    /**
     * 1. Statement: create a session
     * <p>
     * 2. PreparedStatement: create a session with placeholder.
     * <p>
     * 3. CallableStatement: Allow to use generic JDBC syntax for calling procedures
     */
    @Test
    public void prepareDemo() {
        try (Connection conn = dataSource.getConnection()) {
            String sql1 = "select 'a' as name from dual where id = ?";
            try (PreparedStatement pst = conn.prepareStatement(sql1)) {
                pst.setString(1, "123");
                pst.execute();
            }

            String sql2 = "begin package.sp_Procedure(?, ?); end;";
            try (CallableStatement callSt = conn.prepareCall(sql2)) {
                callSt.execute();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Use "setAutoCommit()" and "commit()" to start transaction
     */
    @Test
    public void commitDemo() {
        String sql = "select 'a' as name from dual";
        try (Connection conn = dataSource.getConnection()) {
            // Cancel auto commit
            conn.setAutoCommit(false);
            try (Statement stmt = conn.createStatement();) {
                stmt.execute(sql);
                // Commit session
                conn.commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Use "RowSetFactory" can read data when resource is closed.
     */
    @Test
    public void CachedRowSetDemo() {
        String sql = "select 'a' as name from dual";
        try (
                Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement();
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

    @After
    public void destroy() throws SQLException {
        dataSource.close();
    }
}
