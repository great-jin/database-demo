package xyz.ibudai;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.deserialization.*;
import org.junit.Test;
import xyz.ibudai.Listener.BinlogEventListener;

import java.io.IOException;

public class CaptureTest {

    private static final String host = "10.231.6.21";
    private static final int port = 3306;
    private static final String username = "root";
    private static final String password = "budai#123456";

    @Test
    public void demo1() throws IOException {
        BinaryLogClient client = new BinaryLogClient(host, port, username, password);
        client.registerEventListener(new BinlogEventListener());
        client.connect();
    }

    @Test
    public void demo2() throws IOException {
        BinaryLogClient client = new BinaryLogClient(host, port, username, password);
        // 设置序列化器
        EventDeserializer deserializer = new EventDeserializer();
        deserializer.setCompatibilityMode(
                EventDeserializer.CompatibilityMode.CHAR_AND_BINARY_AS_BYTE_ARRAY,
                EventDeserializer.CompatibilityMode.DATE_AND_TIME_AS_LONG
        );
        deserializer.setEventDataDeserializer(EventType.WRITE_ROWS, new ByteArrayEventDataDeserializer());
        deserializer.setEventDataDeserializer(EventType.UPDATE_ROWS, new ByteArrayEventDataDeserializer());
        deserializer.setEventDataDeserializer(EventType.DELETE_ROWS, new ByteArrayEventDataDeserializer());
        client.setEventDeserializer(deserializer);
        // 设置监听器
        client.registerEventListener(new BinlogEventListener());
        client.connect();
    }

}
