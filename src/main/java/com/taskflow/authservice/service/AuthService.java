package com.taskflow.authservice.service;

import com.taskflow.authservice.dto.LoginRequest;
import com.taskflow.authservice.dto.RegisterRequest;
import com.taskflow.authservice.kafka.KafkaProducerService;
import com.taskflow.authservice.repository.UserRepository;
import com.taskflow.authservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;
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
                .role(request.getRole())
                .build();
        userRepository.save(user);
        producerService.sendUserRegisteredEvent(user);
        return "User registered successfully";
    }

    public String validateUser(LoginRequest request){
        User user=userDetailsService.loadUserByUsername(request.getUsername());
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            return "Username or password is invalid";
        }else{
            return jwtUtil.generateToken(user.getUsername(),user.getRole(),user.getId());
        }
    }




}
