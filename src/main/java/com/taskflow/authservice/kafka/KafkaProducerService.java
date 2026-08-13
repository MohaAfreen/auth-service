package com.taskflow.authservice.kafka;

import com.taskflow.authservice.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.annotation.JsonSerialize;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String,UserRegisteredEvent> kafkaTemplate;
    private final Logger logger= LoggerFactory.getLogger(KafkaProducerService.class);

    public KafkaProducerService(KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserRegisteredEvent(User user){
        UserRegisteredEvent eventObj= UserRegisteredEvent
                .builder()
                .username(user.getUsername())
                .email(user.getEmail())
                        .build();

        kafkaTemplate.send("user-registered",eventObj)
                .whenComplete((result,ex)->{
                    if(ex!=null){
                        logger.error("Failed to send UserRegisteredEvent for user :{}",user.getUsername(),ex);
                    }else{
                       logger.info("UserRegistered event sent successfully {} {} {}",
                               result.getRecordMetadata().topic(),
                               result.getRecordMetadata().partition(),
                               result.getRecordMetadata().offset());
                    }
                });
    }

}
