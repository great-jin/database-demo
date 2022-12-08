package xyz.ibudai;

import org.junit.Test;
import xyz.ibudai.config.HiveConnection;
import xyz.ibudai.util.HiveUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class HiveTest {

    private final String databaseName = "a_db";
    private final String tableName = "tb_test";

    /**
     * 生成表信息
     */
    @Test
    public void demo1() {
        String sql = "analyze table a_db.tb_test compute statistics";
        try (
                Connection conn = HiveConnection.getHiveConnection();
                Statement stmt = conn.createStatement();
        ) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void demo2() {
        String sql = "analyze table ?.? compute statistics";
        try (
                Connection conn = HiveConnection.getHiveConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setString(1, databaseName);
            ps.setString(2, tableName);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void demo3() {
        try (Connection conn = HiveConnection.getHiveConnection()) {
            List<String> schemas = HiveUtils.getAllSchema(conn);
            System.out.println(schemas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void demo4() {
        try (Connection conn = HiveConnection.getHiveConnection()) {
            List<String> tables = HiveUtils.getTablesBySchema(conn, "a_db1");
            System.out.println(tables);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
