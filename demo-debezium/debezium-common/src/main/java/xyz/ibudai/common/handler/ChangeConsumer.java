package xyz.ibudai.common.handler;

import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.List;

public class ChangeConsumer implements DebeziumEngine.ChangeConsumer<RecordChangeEvent<SourceRecord>> {

    /**
     * DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
     */
    @Override
    public void handleBatch(List<RecordChangeEvent<SourceRecord>> list, DebeziumEngine.RecordCommitter<RecordChangeEvent<SourceRecord>> recordCommitter) throws InterruptedException {

    }
}
