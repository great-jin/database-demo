package xyz.ibudai.common.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;

import java.util.List;
import java.util.Objects;

public class JsonChangeConsumer implements DebeziumEngine.ChangeConsumer<ChangeEvent<String, String>> {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * DebeziumEngine.create(Json.class)
     */
    @Override
    public void handleBatch(List<ChangeEvent<String, String>> list,
                            DebeziumEngine.RecordCommitter<ChangeEvent<String, String>> committer) throws InterruptedException {
        for (ChangeEvent<String, String> record : list) {
            try {
                String value = record.value();
                if (Objects.isNull(value)) {
                    System.out.println("1: " + record);
                    return;
                }

                JsonNode node = mapper.readValue(value, JsonNode.class);
                if (Objects.isNull(node)) {
                    System.out.println("2: " + record);
                    return;
                }

                JsonNode payload = node.get("payload");
                if (Objects.isNull(payload)) {
                    System.out.println("3: " + record);
                    return;
                }

                String database, tableName;
                JsonNode before = payload.get("before");
                JsonNode after = payload.get("after");
                JsonNode source = payload.get("source");
                database = source.get("db").asText();
                tableName = source.get("table").asText();
                System.out.printf("Database: %s, Table: %s, Before: %s, After: %s\n", database, tableName, before, after);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // commit
            committer.markProcessed(record);
        }
    }
}
