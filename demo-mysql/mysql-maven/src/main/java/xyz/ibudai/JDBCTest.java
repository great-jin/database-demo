package xyz.ibudai;

import org.junit.Test;
import xyz.ibudai.config.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JDBCTest {

    private Map<String, Object> map = new LinkedHashMap<>();
    private List<Map<String, Object>> list = new ArrayList<>();

    @Test
    public void QueryDemo() {
        String sql = "select * from user_info";
        try (
                Connection con = ConnectionUtil.getConnection();
                Statement stmt = con.createStatement();
        ) {
            stmt.execute(sql);
            ResultSet result = stmt.getResultSet();
            while (result.next()) {
                for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                    map.put(result.getMetaData().getColumnName(i), result.getString(i));
                }
                list.add(map);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(list);
    }

    @Test
    public void preparedStatementDemo() {
        String sql = "select * from user_info where id = ?";
        try (
                Connection con = ConnectionUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, "1");
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                    map.put(result.getMetaData().getColumnName(i), result.getString(i));
                }
                list.add(map);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(list);
    }
}
