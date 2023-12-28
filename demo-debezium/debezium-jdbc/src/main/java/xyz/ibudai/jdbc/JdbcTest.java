package xyz.ibudai.jdbc;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import xyz.ibudai.common.handler.JsonChangeConsumer;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class JdbcTest {

    public static void main(String[] args) {
        final Properties props = new Properties();
        props.setProperty("name", "jdbc-engine");
        props.setProperty("connector.class", "io.debezium.connector.jdbc.JdbcSinkConnector");
        // props.setProperty("connector.url", "jdbc:oceanbase://127.0.0.1:2881/test");
        props.setProperty("connector.url", "jdbc:mysql://127.0.0.1:3306/db_debezium");
        props.setProperty("connector.username", "root");
        props.setProperty("connector.password", "123456");
        //
        props.setProperty("insert.mode", "upsert");
        props.setProperty("delete.enabled", "true");
        props.setProperty("primary.key.mode", "record_key");
        props.setProperty("schema.evolution", "basic");
        props.setProperty("database.time_zone", "UTC");
        //
        props.setProperty("topics", "jdbc");
        //
        props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
        props.setProperty("offset.storage.file.filename", "E:\\Workspace\\debezium\\offsets.dat");
        props.setProperty("offset.flush.interval.ms", "60000");

        // Create the engine with this configuration ...
        try (
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
}
