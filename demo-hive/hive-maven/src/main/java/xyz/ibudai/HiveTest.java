package xyz.ibudai;

import org.junit.Test;
import xyz.ibudai.config.HiveConfig;
import xyz.ibudai.util.HiveUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HiveTest {

    private final String databaseName = "a_db";
    private final String tableName = "tb_test";

    /**
     * 获取所有 Schema
     */
    @Test
    public void demo1() {
        try (Connection conn = HiveConfig.getHiveConnection()) {
            List<String> schemas = HiveUtils.getAllSchema(conn);
            System.out.println(schemas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 Schema 下所有表名
     */
    @Test
    public void demo2() {
        try (Connection conn = HiveConfig.getHiveConnection()) {
            List<String> tables = HiveUtils.getTablesBySchema(conn, databaseName);
            System.out.println(tables);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
