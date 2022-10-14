package xyz.ibudai;

import org.junit.Before;
import org.junit.Test;
import xyz.ibudai.model.JDBCProperty;
import xyz.ibudai.utils.ConnectionUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class PoolTest {

    private static final Properties prop = new Properties();
    private static final JDBCProperty jdbcProp = new JDBCProperty();

    @Before
    public void load() {
        try (
                InputStream in = getClass().getResourceAsStream("/jdbc.properties");
                InputStreamReader inReader = new InputStreamReader(in, StandardCharsets.UTF_8);
        ) {
            prop.load(inReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        jdbcProp.setDriver(prop.getProperty("jdbc.driver"));
        jdbcProp.setUrl(prop.getProperty("jdbc.url"));
        jdbcProp.setUser(prop.getProperty("jdbc.user"));
        jdbcProp.setPassword(prop.getProperty("jdbc.password"));

    }

    @Test
    public void demo() {
        String sql = "select * from tb_user";
        Map<String, Object> map = new LinkedHashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection con = ConnectionUtil.getConnection(jdbcProp)) {
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
}
