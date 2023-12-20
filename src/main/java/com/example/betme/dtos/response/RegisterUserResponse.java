package com.example.betme.dtos.response;

import lombok.Data;

@Data
public class RegisterUserResponse {
    private String username;
    private String registerDate;
    private String message;
    private String playerId;
}
