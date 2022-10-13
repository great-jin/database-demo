package xyz.ibudai.test;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.*;

public class MergeTest {

    private static final String DRIVER_NAME = "com.aliyun.odps.jdbc.OdpsDriver";
    private static final String URL = "jdbc:odps:http://service.odps.aliyun.com/api?project=your_project_name";
    private static final String ACCESSID = "Your AccessID";
    private static final String ACCESSKEY = "Your AccessKey";

    private Map<String, Object> mapValues = new LinkedHashMap<>();
    private List<Map<String, Object>> list = new ArrayList<>();

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
        final String SQL = "MergeDemo()";

        try (Connection con = DriverManager.getConnection(URL, ACCESSID, ACCESSKEY);) {
            Statement stmt = con.createStatement();
            stmt.executeQuery(SQL);
            ResultSet resultSet = stmt.getResultSet();

            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    mapValues.put(resultSet.getMetaData().getColumnName(i), resultSet.getString(i));
                }
                list.add(new JSONObject(mapValues));
            }

            System.out.println(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void MergeDemo(){
        // all fileds
        final Set<String> allFiled = new LinkedHashSet<>();
        allFiled.add("id");
        allFiled.add("name");
        allFiled.add("age");
        allFiled.add("gender");
        allFiled.add("year");
        allFiled.add("month");

        // merge fileds
        final Set<String> updateKeys = new LinkedHashSet<>();
        updateKeys.add("id");
        updateKeys.add("name");

        //  merge + partition
        final Set<String> updatePartition = new LinkedHashSet<>();;
        updatePartition.add("id");
        updatePartition.add("name");
        updatePartition.add("year");
        updatePartition.add("month");

        /*
        //  merge + partition
        final Set<String> updatePartition = new LinkedHashSet<>();;
        updatePartition.add("id");
        updatePartition.add("name");
        */

        // partitionKeys
        final Set<String> partitionKeys = new LinkedHashSet<>(CollectionUtil.subtract(updatePartition, updateKeys));

        // none partition and merger filed
        final Set<String> normalKeys = new LinkedHashSet<>(CollectionUtil.subtract(allFiled, updatePartition));


        // virtual table ; need all filed
        final StringBuilder dualBuilder = new StringBuilder();
        dualBuilder.append("SELECT");
        for (String str : allFiled) {
            dualBuilder.append(" ? AS ");
            dualBuilder.append(str).append(",");
        }
        dualBuilder.deleteCharAt(dualBuilder.length()-1);
        System.out.println(dualBuilder.toString());
        System.out.println();


        // on where clause; need merger and partition filed
        final StringBuilder whereBuilder = new StringBuilder();
        for (String str : updateKeys) {
            whereBuilder.append("t1").append(".").append(str);
            whereBuilder.append("=");
            whereBuilder.append("t2").append(".").append(str);
            whereBuilder.append(" and ");
        }
        for (String str : partitionKeys) {
            whereBuilder.append("t1").append(".").append(str);
            whereBuilder.append("=？ and ");
        }
        whereBuilder.delete(whereBuilder.length()-5, whereBuilder.length()-1);
        System.out.println(whereBuilder.toString());
        System.out.println();


        // need normal filed: [ all fileds - (merger fileds + partition fileds) ]
        final StringBuilder updateBuilder = new StringBuilder();
        updateBuilder.append("UPDATE SET ");
        for (String str : normalKeys) {
            updateBuilder.append("t1").append(".").append(str);
            updateBuilder.append("=");
            updateBuilder.append("t2").append(".").append(str);
            updateBuilder.append(", ");
        }
        updateBuilder.deleteCharAt(updateBuilder.length()-2);
        System.out.println(updateBuilder.toString());
        System.out.println();


        // need all fileds
        final StringBuilder insertBuilder = new StringBuilder();
        insertBuilder.append("INSERT VALUES(");
        for (String str : allFiled) {
            insertBuilder.append("t2").append(".").append(str);
            insertBuilder.append(", ");
        }
        insertBuilder.deleteCharAt(insertBuilder.length()-2);
        insertBuilder.append(")");
        System.out.println(insertBuilder.toString());
        System.out.println();


        // merger sql
        final StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("MERGE INTO ").append("tb_test");
        sqlBuilder.append(" AS ").append("t1");
        sqlBuilder.append(" USING ").append("(");
        sqlBuilder.append(dualBuilder).append(")");
        sqlBuilder.append(" AS ").append("t2");
        sqlBuilder.append(" ON ").append(whereBuilder);
        sqlBuilder.append("WHEN MATCHED THEN ").append(updateBuilder);
        sqlBuilder.append("WHEN NOT MATCHED ").append(insertBuilder).append(";");

        System.out.println(sqlBuilder);
    }

    @Test
    public void StrDemo(){
        String origin = "id,name";
        String[] splitStr = origin.split(",");
        Set<String> set = new LinkedHashSet();
        for (String str : splitStr) {
            set.add(str);
        }
        System.out.println(set);
    }

}
