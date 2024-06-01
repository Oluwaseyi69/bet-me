package com.example.betme.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserResponse {
    private String username;
    private String registerDate;
    private String message;
    private String playerId;
    private String email;
    private String token;

}
