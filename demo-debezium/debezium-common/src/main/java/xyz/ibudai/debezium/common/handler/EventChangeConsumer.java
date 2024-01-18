package xyz.ibudai.debezium.common.handler;

import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.List;

public class EventChangeConsumer implements DebeziumEngine.ChangeConsumer<RecordChangeEvent<SourceRecord>> {

    /**
     * DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
     */
    @Override
    public void handleBatch(List<RecordChangeEvent<SourceRecord>> list,
                            DebeziumEngine.RecordCommitter<RecordChangeEvent<SourceRecord>> committer) throws InterruptedException {
        for (RecordChangeEvent<SourceRecord> event : list) {
            try {
                SourceRecord record = event.record();
                Object key = record.key();
                Object value = record.value();
                System.out.println("1: " + key);
                System.out.println("2: " + value);
                committer.markProcessed(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
