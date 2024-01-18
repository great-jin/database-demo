package xyz.ibudai.debezium.common.task;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import xyz.ibudai.debezium.common.handler.JsonChangeConsumer;

import java.util.Properties;

public class JsonEventTask implements Runnable {

    private final Properties properties;

    public JsonEventTask(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void run() {
        try (
                // Create the engine with this configuration ...
                DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
                        // set connector properties
                        .using(properties)
                        // set data formatter
                        .notifying(new JsonChangeConsumer())
                        .build()
        ) {
            System.out.println("Monitoring on Json format...");
            engine.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
