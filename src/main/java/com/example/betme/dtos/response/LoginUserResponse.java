package com.example.betme.dtos.response;


import lombok.Data;

@Data
public class LoginUserResponse {
    private String playerId;
    private boolean loggedIn;
    private String message;
}
