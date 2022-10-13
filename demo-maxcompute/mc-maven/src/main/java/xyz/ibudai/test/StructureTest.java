package xyz.ibudai.test;

import com.aliyun.odps.Column;
import com.aliyun.odps.Odps;
import com.aliyun.odps.Table;
import com.aliyun.odps.TableSchema;
import com.aliyun.odps.account.Account;
import com.aliyun.odps.account.AliyunAccount;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class StructureTest {

    private static final String ACCESSID = "Your AccessID";
    private static final String ACCESSKEY = "Your AccessKey";
    private static final String ENDPOINT = "http://service.odps.aliyun.com/api";
    private static final String PROJECT = "your_project_name";
    private static final String TABLE = "test_t1";

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
    public void ColumnDemo(){
        Table table = odps.tables().get(TABLE);
        TableSchema targetSchema = table.getSchema();

        List<Column> targetField = targetSchema.getColumns();
        List<String> list = new ArrayList<>();
        for (Column column : targetField) {
            list.add(column.getName());
        }

        System.out.println(list);
    }

}
