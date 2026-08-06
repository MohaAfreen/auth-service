package com.taskflow.authservice.service;

import com.taskflow.authservice.entity.User;
import com.taskflow.authservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User loadUserByUsername(String username){
        return userRepository.findUserByUsername(username)
                .orElseThrow(()->  new RuntimeException("Username is invalid"));
    }
}
