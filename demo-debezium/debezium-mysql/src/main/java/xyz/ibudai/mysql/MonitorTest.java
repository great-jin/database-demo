package xyz.ibudai.mysql;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class MonitorTest {

    public static void main(String[] args) {
        final Properties props = new Properties();
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
        props.setProperty("database.include.list", "db_debezium");

        props.setProperty("topic.prefix", "mysql-connector");
        props.setProperty("schema.history.internal",
                "io.debezium.storage.file.history.FileSchemaHistory");
        props.setProperty("schema.history.internal.file.filename",
                "E:\\Workspace\\debezium\\schemahistory.dat");

        // Create the engine with this configuration ...
        try (
                DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
                        .using(props)
                        .notifying(record -> {
                            System.out.println(record);
                        }).build();
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
}
