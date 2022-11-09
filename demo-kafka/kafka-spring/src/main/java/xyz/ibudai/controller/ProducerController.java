package xyz.ibudai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.model.User;

import java.util.UUID;

@RestController
@RequestMapping("api/kafka")
public class ProducerController {

    @Value("${kafka.topic}")
    private String topicName;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/send")
    public void send(@RequestBody User user) {
        String key = UUID.randomUUID().toString();
        try {
            String data = objectMapper.writeValueAsString(user);
            kafkaTemplate.send(topicName, key, data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

