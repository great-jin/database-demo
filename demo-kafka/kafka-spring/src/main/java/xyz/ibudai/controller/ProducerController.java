package xyz.ibudai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.model.User;

import java.util.UUID;

@RestController
@RequestMapping("/kafka")
public class ProducerController {

    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    @GetMapping("/send")
    public void send(@RequestParam("topic") String topic, @RequestBody User user) {
        String key = UUID.randomUUID().toString();
        kafkaTemplate.send(topic, key, user);
    }
}

