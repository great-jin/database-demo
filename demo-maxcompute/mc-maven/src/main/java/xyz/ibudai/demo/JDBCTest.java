package xyz.ibudai.demo;

import cn.hutool.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JDBCTest {

    private static final String DRIVER_NAME = "com.aliyun.odps.jdbc.OdpsDriver";
    private static final String URL = "jdbc:odps:http://service.odps.aliyun.com/api?project=your_project_name";
    private static final String ACCESSID = "Your AccessID";
    private static final String ACCESSKEY = "Your AccessKey";

    private final Map<String, Object> mapValues = new LinkedHashMap<>();
    private final List<Map<String, Object>> list = new ArrayList<>();

    @Before
    public void Load(){
        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Test
    public void SMDemo(){
        final String SQL = "select * from test_t1;";

        try (Connection con = DriverManager.getConnection(URL, ACCESSID, ACCESSKEY);) {
            Statement st = con.createStatement();
            ResultSet results = st.executeQuery(SQL);

            while (results.next()) {
                for (int i = 1; i <= results.getMetaData().getColumnCount(); i++) {
                    mapValues.put(results.getMetaData().getColumnName(i), results.getString(i));
                }
                list.add(new JSONObject(mapValues));
            }

            System.out.println(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * create table test_t1 (id bigint, name string, age int)
     * partitioned by (year int)
     * tblproperties ("transactional"="true");
     */
    @Test
    public void PSDemo(){
        final String SQL = "select * from test_t1 where year=?;";

        try (Connection con = DriverManager.getConnection(URL, ACCESSID, ACCESSKEY);
             PreparedStatement ps = con.prepareStatement(SQL); ) {
            ps.setString(1,"2020");
            ResultSet results = ps.executeQuery();

            while (results.next()) {
                for (int i = 1; i <= results.getMetaData().getColumnCount(); i++) {
                    mapValues.put(results.getMetaData().getColumnName(i), results.getString(i));
                }
                list.add(new JSONObject(mapValues));
            }

            System.out.println(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}