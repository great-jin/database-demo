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
import java.sql.*;

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
            throw new RuntimeException(ex);
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
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                // Commit session
                conn.commit();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Use "RowSetFactory" can read data when resource is closed.
     * <p>
     * 1. resultSetType
     * ==> (1) ResultSet.TYPE_FORWARD_ONLY: ResultSet only forward
     * ==> (2) ResultSet.TYPE_SCROLL_SENSITIVE: Sensitive the change after query executed.
     * ==> (3) ResultSet.TYPE_SCROLL_INSENSITIVE: Insensitive the change after query executed.
     * <p>
     * 2. resultSetConcurrency
     * ==> (1) ResultSet.CONCUR_READ_ONLY: Default, can't update database
     * ==> (2) ResultSet.CONCUR_UPDATABLE: Can update database
     */
    @Test
    public void rollRowSetDemo() {
        String sql = "select 'a' as name from dual";
        try (
                Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = stmt.executeQuery(sql);
        ) {
            // previous(): Get next row
            while (rs.next()) {
                // Update row content, database data not change
                rs.updateString(1, "Alex");
                rs.updateRow();
            }

            // previous(): Get previous row
            while (rs.previous()) {
            }

            // relative(n): n>0: Move forward, n<0: Move backward
            while (rs.relative(2)) {
                System.out.println(rs.getString("name"));
            }

            // absolute(n): Set to specify row num
            while (rs.absolute(2)) {
                System.out.println(rs.getString("name"));
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Use "RowSetFactory" can read data when resource is closed.
     */
    @Test
    public void cachedRowSetDemo() {
        String sql = "select 'a' as name from dual";
        try (
                Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement()
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
            throw new RuntimeException(ex);
        }
    }


    @After
    public void destroy() throws SQLException {
        dataSource.close();
    }
}
