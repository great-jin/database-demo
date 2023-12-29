package xyz.ibudai.mysql;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import org.junit.Before;
import org.junit.Test;
import xyz.ibudai.common.handler.JsonChangeConsumer;
import xyz.ibudai.mysql.config.PropsConfig;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class JsonRecordTest {

    private static Properties props;

    @Before
    public void init() {
        props = PropsConfig.init();
    }

    @Test
    public void demo1() {
        props.setProperty("database.include.list", "db_debezium");
        monitorOnJsonRecord(props);
    }

    @Test
    public void demo2() {
        props.setProperty("database.include.list", "db_debezium");
        props.setProperty("table.include.list", "tb_include");
        monitorOnJsonRecord(props);
    }

    private void monitorOnJsonRecord(Properties props) {
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
            System.out.println("Monitoring on Json record ...");
            TimeUnit.MINUTES.sleep(5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
