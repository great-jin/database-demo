package xyz.ibudai;

import org.junit.Test;
import xyz.ibudai.config.ConnectionUtil;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

public class BlogTest {

    @Test
    public void demo() {
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
