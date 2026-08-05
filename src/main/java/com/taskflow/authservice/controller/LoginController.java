package com.taskflow.authservice.controller;

import com.taskflow.authservice.dto.LoginRequest;
import com.taskflow.authservice.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {
    private AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public String validateUser(@RequestBody LoginRequest request){
       return  authService.validateUser(request);
    }
}
