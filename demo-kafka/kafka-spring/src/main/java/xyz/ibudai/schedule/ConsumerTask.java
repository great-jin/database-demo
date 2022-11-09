package xyz.ibudai.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import xyz.ibudai.model.User;

@Component
public class ConsumerTask {

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "test-topic")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            User user = objectMapper.readValue(record.value(), User.class);
            System.out.printf(user.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

