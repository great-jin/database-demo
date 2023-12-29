package xyz.ibudai.mysql;

import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import org.apache.kafka.connect.source.SourceRecord;
import org.junit.Before;
import org.junit.Test;
import xyz.ibudai.common.handler.ChangeConsumer;
import xyz.ibudai.mysql.config.PropsConfig;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class KafkaRecordTest {

    private static Properties props;

    @Before
    public void init() {
        props = PropsConfig.init();
    }

    @Test
    public void demo1() {
        props.setProperty("database.include.list", "db_debezium");
        monitorOnKafkaRecord(props);
    }

    @Test
    public void demo2() {
        props.setProperty("database.include.list", "db_debezium");
        props.setProperty("table.include.list", "tb_include");
        monitorOnKafkaRecord(props);
    }

    private void monitorOnKafkaRecord(Properties props) {
        try (
                // Create the engine with this configuration ...
                DebeziumEngine<RecordChangeEvent<SourceRecord>> engine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                        .using(props)
                        .notifying(new ChangeConsumer())
                        .build()
        ) {
            // Run the engine asynchronously ...
            new Thread(engine).start();

            // Do something else or wait for a signal or an event
            System.out.println("Monitoring on Kafka record ...");
            TimeUnit.MINUTES.sleep(5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
