package com.taskflow.authservice.service;

import com.taskflow.authservice.dto.RegisterRequest;
import com.taskflow.authservice.kafka.KafkaProducerService;
import com.taskflow.authservice.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import com.taskflow.authservice.entity.User;

@Service
public class AuthService {
    private BCryptPasswordEncoder passwordEncoder;
    private KafkaProducerService producerService;
    private UserRepository userRepository;

    public AuthService(BCryptPasswordEncoder passwordEncoder, KafkaProducerService producerService, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.producerService = producerService;
        this.userRepository = userRepository;
    }

    public String registerUser(RegisterRequest request){
        User user= User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();
        userRepository.save(user);
        producerService.sendUserCreatedEvent("User Registered"+ user.getEmail());
        return "User registered successfully";
    }

}
