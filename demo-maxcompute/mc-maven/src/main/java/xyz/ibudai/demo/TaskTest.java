package xyz.ibudai.demo;

import cn.hutool.json.JSONObject;
import com.aliyun.odps.Column;
import com.aliyun.odps.Instance;
import com.aliyun.odps.Odps;
import com.aliyun.odps.OdpsException;
import com.aliyun.odps.account.Account;
import com.aliyun.odps.account.AliyunAccount;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.task.SQLTask;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class TaskTest {

    private static final String ACCESSID = "Your AccessID";
    private static final String ACCESSKEY = "Your AccessKey";
    private static final String ENDPOINT = "http://service.odps.aliyun.com/api";
    private static final String PROJECT = "your_project_name";

    /**
     * create table test_t1 (id bigint, name string, age int)
     * partitioned by (year int)
     * tblproperties ("transactional"="true");
     */

    private static final String SQL = "select * from test_t1 where year='2020';";
    private static final String SQLMultiple = "set odps.sql.allow.fullscan=true;" +
            "select * from test_t1;";

    private Account account;
    private Odps odps;
    private Instance instance;

    @Before
    public void Init(){
        account = new AliyunAccount(ACCESSID, ACCESSKEY);
        odps = new Odps(account);
        odps.setEndpoint(ENDPOINT);
        odps.setDefaultProject(PROJECT);
    }

    @Test
    public void SQLDemo(){
        try {
            instance = SQLTask.run(odps, SQL);
            instance.waitForSuccess();
            List<Record> records = SQLTask.getResult(instance);

            Map<String, Object> map = new LinkedHashMap();
            List<JSONObject> results = new ArrayList<>();
            for (Record r : records) {
                for (int i = 0; i < records.stream().count(); i++) {
                    for (int j = 0; j < r.getColumns().length; j++) {
                        Column column = r.getColumns()[j];
                        map.put(column.getName(), r.getString(j));
                    }
                }
                results.add(new JSONObject(map));
            }

            System.out.println(results);
        } catch (OdpsException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void MultipleDemo() {
        Map<String, String> hint = new HashMap();
        hint.put("odps.sql.submit.mode", "script");

        try {
            instance = SQLTask.run(odps,PROJECT, SQLMultiple, hint, null);
            instance.waitForSuccess();
            List<Record> records = SQLTask.getResult(instance);

            Map<String, Object> map = new LinkedHashMap();
            List<JSONObject> results = new ArrayList<>();
            for (Record r : records) {
                for (int i = 0; i < records.stream().count(); i++) {
                    for (int j = 0; j < r.getColumns().length; j++) {
                        Column column = r.getColumns()[j];
                        map.put(column.getName(), r.getString(j));
                    }
                }
                results.add(new JSONObject(map));
            }

            System.out.println(results);
        } catch (OdpsException e) {
            e.printStackTrace();
        }
    }
}