package xyz.ibudai.controller;

import xyz.ibudai.model.User;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/kafka")
public class ProducerController {

    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    @GetMapping("/send")
    public void send() {
        String msg = "hello world";
        User user = new User("alex", "1234");
        String key = UUID.randomUUID().toString();
        kafkaTemplate.send("test_topic", key, user);
    }
}

