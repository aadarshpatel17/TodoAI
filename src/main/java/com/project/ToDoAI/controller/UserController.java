package com.project.ToDoAI.controller;

import com.project.ToDoAI.dto.LoginResponse;
import com.project.ToDoAI.dto.UserLoginRequest;
import com.project.ToDoAI.dto.UserResponse;
import com.project.ToDoAI.dto.UserSignupRequest;
import com.project.ToDoAI.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public UserResponse signup(@RequestBody UserSignupRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody UserLoginRequest request) {
        return userService.login(request);
    }

}
