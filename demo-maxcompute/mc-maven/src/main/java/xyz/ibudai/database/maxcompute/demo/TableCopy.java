package xyz.ibudai.database.maxcompute.demo;

import com.aliyun.odps.*;
import com.aliyun.odps.account.Account;
import com.aliyun.odps.account.AliyunAccount;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TableCopy {

    private static final String ACCESSID = "Your AccessID";
    private static final String ACCESSKEY = "Your AccessKey";
    private static final String ENDPOINT = "http://service.odps.aliyun.com/api";
    private static final String PROJECT = "your_project_name";
    private static final String TABLE = "test_t1";
    private static final String TARTGET_TABLE = "temp_copy_1";
    private static final Long LIFECYCLE = Long.valueOf(1);

    /**
     * create table test_t1 (id bigint, name string, age int)
     * partitioned by (year int)
     * tblproperties ("transactional"="true");
     */

    private Account account;
    private Odps odps;

    @Before
    public void Init(){
        account = new AliyunAccount(ACCESSID, ACCESSKEY);
        odps = new Odps(account);
        odps.setEndpoint(ENDPOINT);
        odps.setDefaultProject(PROJECT);
    }

    @Test
    public void TableCopy() throws OdpsException {
        // 获取目标表结构
        Table table = odps.tables().get(TABLE);
        TableSchema targetSchema = table.getSchema();

        // 打印目标表结构
        List<String> list = new ArrayList<>();
        for (int i = 0; i < targetSchema.getColumns().size(); i++) {
            list.add(targetSchema.getColumn(i).getName());
        }
        System.out.println(list);

        TableSchema tableSchema = new TableSchema();
        List<Column> tableColumns = new ArrayList();
        tableColumns.addAll(targetSchema.getColumns());
        tableColumns.addAll(targetSchema.getPartitionColumns());
        tableSchema.setColumns(tableColumns);

        odps.tables().createTableWithLifeCycle(PROJECT, TARTGET_TABLE, tableSchema,
                "Temporary Table", false, LIFECYCLE);
    }

}
