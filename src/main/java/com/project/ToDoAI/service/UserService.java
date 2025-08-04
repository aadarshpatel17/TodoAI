package com.project.ToDoAI.service;

import com.project.ToDoAI.dto.LoginResponse;
import com.project.ToDoAI.dto.UserLoginRequest;
import com.project.ToDoAI.dto.UserResponse;
import com.project.ToDoAI.dto.UserSignupRequest;
import com.project.ToDoAI.entity.User;
import com.project.ToDoAI.repository.UserRepository;
import com.project.ToDoAI.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserResponse register(UserSignupRequest request) {
        if(userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email Already In User!!!");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);

        return new UserResponse(saved.getId(), saved.getUsername(), saved.getEmail());
    }

    public LoginResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid Email!!!"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid Password!!!");

        String token = jwtUtil.generateToken(request.getEmail());
        UserResponse userResponse = new UserResponse(user.getId(), user.getUsername(), user.getEmail());
        return new LoginResponse(token, userResponse);
    }
}
