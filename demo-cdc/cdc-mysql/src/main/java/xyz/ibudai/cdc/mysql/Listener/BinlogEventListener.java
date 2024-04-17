package xyz.ibudai.cdc.mysql.Listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static xyz.ibudai.cdc.mysql.util.JsonUtil.decodeJson;

public class BinlogEventListener implements BinaryLogClient.EventListener {

    private static final String tableName = "tb_test";

    private static final String schema = "test_db";

    @Override
    public void onEvent(Event event) {
        EventData eventData = event.getData();
        if (eventData instanceof TableMapEventData) {
            TableMapEventData tableEvent = (TableMapEventData) eventData;
            String database = tableEvent.getDatabase();
            String table = tableEvent.getTable();
            // 根据需要过滤指定的库表
            if (Objects.equals(schema, database) && Objects.equals(tableName, table)) {
                System.out.println("TableMapEvent: database = " + database + ", table = " + table);
            } else {
                System.out.println("The table is not " + schema + "." + tableName);
                return;
            }
        }
        if (eventData instanceof WriteRowsEventData) {
            // 解析和处理 Insert 事件
            WriteRowsEventData writeEvent = (WriteRowsEventData) eventData;
            List<Serializable[]> rows = writeEvent.getRows();
            decodeJson(rows);
        } else if (eventData instanceof UpdateRowsEventData) {
            // 解析和处理 Update 事件
            UpdateRowsEventData updateEvent = (UpdateRowsEventData) eventData;
            System.out.println("Update event: " + updateEvent);
        } else if (eventData instanceof DeleteRowsEventData) {
            // 解析和处理 Delete 事件
            DeleteRowsEventData deleteEvent = (DeleteRowsEventData) eventData;
            System.out.println("Delete event: " + deleteEvent);
        }
    }

}
