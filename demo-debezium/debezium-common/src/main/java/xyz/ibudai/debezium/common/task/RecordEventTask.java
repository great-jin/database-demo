package xyz.ibudai.debezium.common.task;

import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import org.apache.kafka.connect.source.SourceRecord;
import xyz.ibudai.debezium.common.handler.EventChangeConsumer;

import java.util.Properties;

public class RecordEventTask implements Runnable {

    private final Properties properties;

    public RecordEventTask(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void run() {
        ChangeEventFormat<Connect> eventFormat = ChangeEventFormat.of(Connect.class);
        try (
                // Create the engine with this configuration ...
                DebeziumEngine<RecordChangeEvent<SourceRecord>> engine = DebeziumEngine.create(eventFormat)
                        .using(properties)
                        .notifying(new EventChangeConsumer())
                        .build()
        ) {
            System.out.println("Monitoring on Record format ...");
            engine.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
