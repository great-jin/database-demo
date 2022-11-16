package xyz.ibudai;

import org.junit.Test;
import xyz.ibudai.config.ConnectionUtil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StructTest {

    @Test
    public void SMDemo() {
        List<String> tableList = new ArrayList<>();
        List<String> catList = new ArrayList<>();
        List<String> typeList = new ArrayList<>();
        List<String> remarkList = new ArrayList<>();

        try (Connection conn = ConnectionUtil.getConnection()) {
            DatabaseMetaData dbMetaData = conn.getMetaData();
            ResultSet rs = dbMetaData.getTables(null, null, null, new String[]{"TABLE"});
            while (rs.next()) {
                // 表名
                tableList.add(rs.getString("TABLE_NAME"));
                // 数据库名
                catList.add(rs.getString("TABLE_CAT"));
                // 类型
                typeList.add(rs.getString("TABLE_TYPE"));
                // 备注
                remarkList.add(rs.getString("REMARKS"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(tableList);
        System.out.println(catList);
        System.out.println(typeList);
        System.out.println(remarkList);
    }
}
