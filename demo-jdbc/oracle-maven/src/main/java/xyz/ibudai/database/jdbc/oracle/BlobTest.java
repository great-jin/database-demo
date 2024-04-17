package xyz.ibudai.database.jdbc.oracle;

import org.junit.Test;
import xyz.ibudai.database.jdbc.oracle.config.ConnectionUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.UUID;

public class BlobTest {

    /**
     * Oracle Blob type demo
     */
    @Test
    public void blobDemo() {
        String sql1 = "INSERT INTO TB_FILE VALUES (?, ?)";
        String sql2 = "SELECT * FROM TB_FILE";
        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pStmt = conn.prepareStatement(sql1);
                Statement stmt = conn.createStatement();
                InputStream in = Files.newInputStream(Paths.get("src\\main\\resources\\file\\table-info.xlsx"))
        ) {
            // Insert blob data
            pStmt.setString(1, UUID.randomUUID().toString());
            pStmt.setBlob(2, in);
            pStmt.execute();

            // Read blob data
            ResultSet rs = stmt.executeQuery(sql2);
            while (rs.next()) {
                InputStream inputStream = rs.getBlob("FILE").getBinaryStream();
                System.out.println(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Oracle Clob type demo
     */
    @Test
    public void clobDemo() {
        String sql1 = "INSERT INTO TB_FILE1 VALUES (?, ?)";
        String sql2 = "SELECT * FROM TB_FILE1";
        try (
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pStmt = conn.prepareStatement(sql1);
                Statement stmt = conn.createStatement();
        ) {
            // Insert clob data
            pStmt.setString(1, UUID.randomUUID().toString());
            Clob clob = conn.createClob();
            String data = "文件已创建 - 星期二-十一月-15-2022";
            clob.setString(1, data);
            pStmt.setClob(2, clob);
            pStmt.execute();

            // Read clob data
            ResultSet rs = stmt.executeQuery(sql2);
            while (rs.next()) {
                String clobStr = ClobToStr(rs.getClob("FILE"));
                System.out.println(clobStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String ClobToStr(Clob clob) {
        try (
                Reader reader = clob.getCharacterStream();  // 得到流
                BufferedReader bufferedReader = new BufferedReader(reader);
        ) {
            String str = bufferedReader.readLine();
            StringBuilder builder = new StringBuilder();
            while (str != null) {
                builder.append(str);
                str = bufferedReader.readLine();
            }
            return builder.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
