package com.taskflow.authservice.service;

import com.taskflow.authservice.dto.LoginRequest;
import com.taskflow.authservice.dto.RegisterRequest;
import com.taskflow.authservice.kafka.KafkaProducerService;
import com.taskflow.authservice.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import com.taskflow.authservice.entity.User;

import java.util.Optional;

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
        //producerService.sendUserCreatedEvent("User Registered"+ user.getEmail());
        return "User registered successfully";
    }

    public String validateUser(LoginRequest request){
        Optional<User> user=userRepository.findUserByUsername(request.getUsername());
        if(user.isEmpty())
            return "Username is invalid";
        else if(!passwordEncoder.matches(request.getPassword(),user.get().getPassword())){
            return "Username or password is invalid";
        }else{
            return "Logged in successfully";
        }
    }


}
