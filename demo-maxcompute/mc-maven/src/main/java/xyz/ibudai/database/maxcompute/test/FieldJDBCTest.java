package xyz.ibudai.database.maxcompute.test;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class FieldJDBCTest {

    private static final String DRIVER_NAME = "com.aliyun.odps.jdbc.OdpsDriver";
    private static final String URL = "jdbc:odps:http://service.odps.aliyun.com/api?project=your_project_name";
    private static final String ACCESSID = "Your AccessID";
    private static final String ACCESSKEY = "Your AccessKey";

    @Before
    public void Load() {
        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Float   Date
     * <p>
     * create table test_t4(
     * _float FLOAT,
     * _date DATE
     * )
     * partitioned by (year string)
     * tblproperties ("transactional"="true");
     */
    @Test
    public void AddDemo() {
        final String SQL = "insert into test_t4 partition (year=?) values (?, ?);";

        try (Connection con = DriverManager.getConnection(URL, ACCESSID, ACCESSKEY);
             PreparedStatement ps = con.prepareStatement(SQL);) {

            ps.setString(1, "2020");
            ps.setFloat(2, (float) 3.14);
            ps.setDate(3, new Date(System.currentTimeMillis()));

            ResultSet result = ps.executeQuery();
            System.out.println(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * create table test_t5(
     * _float FLOAT
     * )
     * partitioned by (year string)
     * tblproperties ("transactional"="true");
     */
    @Test
    public void AddFloat() {
        final String SQL = "insert into test_t5 partition (year=?) values (?);";

        try (Connection con = DriverManager.getConnection(URL, ACCESSID, ACCESSKEY);
             PreparedStatement ps = con.prepareStatement(SQL);) {

            ps.setString(1, "2020");
            ps.setFloat(2, 3.14f);
//            ps.setFloat(2, Convert.toFloat(3.14));

            ResultSet result = ps.executeQuery();
            System.out.println(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * create table test_t7(
     * _binary BINARY
     * )
     * partitioned by ( _partition string )
     * tblproperties ("transactional"="true");
     */
    @Test
    public void AddBlob() throws Exception {
        String str = null;

        final String SQL = "select * from test_t7 where _partition='test';";
        try (Connection con = DriverManager.getConnection(URL, ACCESSID, ACCESSKEY);) {
            Statement stmt = con.createStatement();
            stmt.executeQuery(SQL);
            ResultSet resultSet = stmt.getResultSet();

            while (resultSet.next()) {
                str = resultSet.getString(1);
            }
        } catch (SQLException  e) {
            e.printStackTrace();
        }

        InputStream stream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));

        final String SQL_1 = "insert into test_t7 partition (year=?) values (?);";
        try (Connection con = DriverManager.getConnection(URL, ACCESSID, ACCESSKEY);
             PreparedStatement ps = con.prepareStatement(SQL_1)) {

            ps.setString(1, "2020");
            ps.setBinaryStream(2, stream);
            ResultSet result = ps.executeQuery();

            System.out.println(result);
        } catch( SQLException e) {
            e.printStackTrace();
        }
    }

}
