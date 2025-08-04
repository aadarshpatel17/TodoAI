package com.project.ToDoAI.dto;

import lombok.Data;

@Data
public class UserSignupRequest {

    private String username;
    private String email;
    private String password;

}
