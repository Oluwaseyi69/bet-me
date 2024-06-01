package com.example.betme.dtos.response;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginPlayerResponse {
    private String playerId;
    private boolean loggedIn;
    private String message;
    private String token;
}
