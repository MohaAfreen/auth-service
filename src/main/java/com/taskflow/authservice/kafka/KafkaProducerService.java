package com.taskflow.authservice.kafka;

import com.taskflow.authservice.entity.User;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.annotation.JsonSerialize;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String,UserRegisteredEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserRegisteredEvent(User user){
        UserRegisteredEvent eventObj= UserRegisteredEvent
                .builder()
                .username(user.getUsername())
                .email(user.getEmail())
                        .build();

        kafkaTemplate.send("user-registered",eventObj);
    }

}
