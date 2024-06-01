package com.example.betme.dtos.request;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private String username;
    private String password;
    private String id;
    private String email;
}
