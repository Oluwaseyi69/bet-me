package com.example.betme.dtos.response;


import lombok.Data;

@Data
public class LoginUserResponse {
    private String id;
//    private String email;
    private boolean loggedIn;
    private String message;
}
