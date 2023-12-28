package xyz.ibudai.mysql;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import xyz.ibudai.common.handler.JsonChangeConsumer;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class MySQLTest {

    private static final Properties props = new Properties();

    @Before
    public void init() {
        props.setProperty("name", "mysql-engine");
        props.setProperty("connector.class", "io.debezium.connector.mysql.MySqlConnector");
        props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
        props.setProperty("offset.storage.file.filename", "E:\\Workspace\\debezium\\offsets.dat");
        props.setProperty("offset.flush.interval.ms", "60000");
        /* begin connector properties */
        props.setProperty("database.hostname", "127.0.0.1");
        props.setProperty("database.port", "3306");
        props.setProperty("database.user", "root");
        props.setProperty("database.password", "123456");
        props.setProperty("database.server.id", "1");

        props.setProperty("topic.prefix", "mysql-connector");
        props.setProperty("schema.history.internal",
                "io.debezium.storage.file.history.FileSchemaHistory");
        props.setProperty("schema.history.internal.file.filename",
                "E:\\Workspace\\debezium\\schemahistory.dat");
    }

    @Test
    public void demo1() {
        props.setProperty("database.include.list", "db_debezium");
        monitor(props);
    }

    @Test
    public void demo2() {
        props.setProperty("database.include.list", "db_debezium");
        props.setProperty("table.include.list", "tb_include");
        monitor(props);
    }

    private void monitor(Properties props) {
        try (
                // Create the engine with this configuration ...
                DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
                        .using(props)
                        .notifying(new JsonChangeConsumer())
                        .build()
        ) {
            // Run the engine asynchronously ...
            new Thread(engine).start();

            // Do something else or wait for a signal or an event
            System.out.println("Monitoring...");
            TimeUnit.MINUTES.sleep(5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void convertToText(ChangeEvent<String, String> record) {
        try {
            System.out.println("Key: " + record.key());
            System.out.println("Value: " + record.value());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
