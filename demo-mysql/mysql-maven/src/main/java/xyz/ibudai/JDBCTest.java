package xyz.ibudai;

import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JDBCTest {

    final String CLASSNAME = "com.mysql.cj.jdbc.Driver";
    final String JDBC = "jdbc:mysql://10.231.6.65:3306/test_db?useSSL=true&useUnicode=true&characterEncoding=utf-8";
    final String USERNAME = "root";
    final String PASSWORD = "123456";

    private Map<String, Object> map = new LinkedHashMap<>();
    private List<Map<String, Object>> list = new ArrayList<>();

    @Before
    public void Init() {
        try {
            Class.forName(CLASSNAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void QueryDemo() {
        String sql = "select * from user_info";
        try (Connection con = DriverManager.getConnection(JDBC, USERNAME, PASSWORD)) {
            Statement stmt = con.createStatement();
            stmt.executeQuery(sql);
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
    public void PrepareDemo() {
        String sql = "select * from user_info where id=?";
        try (Connection con = DriverManager.getConnection(JDBC, USERNAME, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "4");
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
