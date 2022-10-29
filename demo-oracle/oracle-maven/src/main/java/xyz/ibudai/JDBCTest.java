package xyz.ibudai;

import cn.hutool.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JDBCTest {

    final String CLASSNAME = "oracle.jdbc.OracleDriver";
    final String JDBC = "jdbc:oracle:thin:@10.231.6.65:1521:helowin";
    final String USERNAME = "budai";
    final String PASSWORD = "123456";

    @Before
    public void Init() {
        try {
            Class.forName(CLASSNAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试连接有效性
     */
    @Test
    public void demo() {
        try (Connection con = DriverManager.getConnection(JDBC, USERNAME, PASSWORD)) {
            // 连接是否有效, 指定时间内连接失败返回 false
            boolean isValid = con.isValid(30);
            System.out.println(isValid);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Oracle JDBC语句不允许带分号(;)，否则将会报非法字符
     */
    @Test
    public void OracleDemo() {
        final String SQL = "select * from TEST_0628";
        try (Connection con = DriverManager.getConnection(JDBC, USERNAME, PASSWORD)) {
            Map<String, Object> map = new LinkedHashMap<>();
            List<Map<String, Object>> list = new ArrayList<>();

            Statement stmt = con.createStatement();
            stmt.executeQuery(SQL);
            ResultSet result = stmt.getResultSet();
            while (result.next()) {
                for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                    map.put(result.getMetaData().getColumnName(i), result.getString(i));
                }
                list.add(new JSONObject(map));
            }
            System.out.println(list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
