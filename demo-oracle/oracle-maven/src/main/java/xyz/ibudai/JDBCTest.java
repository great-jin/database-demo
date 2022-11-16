package xyz.ibudai;

import org.junit.Test;
import xyz.ibudai.config.ConnectionUtil;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class JDBCTest {

    /**
     * 测试连接有效性
     */
    @Test
    public void demo1() {
        try (Connection con = ConnectionUtil.getConnection()) {
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
    public void demo2() {
        final String SQL = "select * from TEST_0628";
        try (
                Connection con = ConnectionUtil.getConnection();
                Statement stmt = con.createStatement();
        ) {
            List<Map<String, Object>> ResultList = new ArrayList<>();
            Map<String, Object> itemMap = new LinkedHashMap<>();

            stmt.executeQuery(SQL);
            ResultSet result = stmt.getResultSet();
            while (result.next()) {
                for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                    itemMap.put(result.getMetaData().getColumnName(i), result.getString(i));
                }
                ResultList.add(itemMap);
            }
            System.out.println(ResultList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void blobDemo() {
        String sql = "insert into tb_file values (?, ?)";
        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pStmt = conn.prepareStatement(sql);
                InputStream in = Files.newInputStream(Paths.get("src\\main\\resources\\file\\table-info.xlsx"))
        ) {
            pStmt.setString(1, UUID.randomUUID().toString());
            pStmt.setBlob(2, in);
            pStmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
