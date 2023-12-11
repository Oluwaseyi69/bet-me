package com.example.betme.utils;

import com.example.betme.data.model.Player;
import com.example.betme.dtos.request.RegisterUserRequest;
import com.example.betme.dtos.response.RegisterUserResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mapper {
    public static Player map(RegisterUserRequest registerUserRequest){
        Player user = new Player();
        user.setUsername(registerUserRequest.getUsername());
        user.setPassword(registerUserRequest.getPassword());
        user.setBalance(BigDecimal.ZERO);
        user.setLogIn(false);
        user.setId(registerUserRequest.getId());

        return user;
    }

    public static RegisterUserResponse map(Player player){
        RegisterUserResponse registerUserResponse = new RegisterUserResponse();
        registerUserResponse.setUsername(player.getUsername());
        registerUserResponse.setRegisterDate(DateTimeFormatter
                .ofPattern("EEE dd/MMM/yyyy HH:mm:ss a")
                .format(LocalDateTime.now()));
        registerUserResponse.setMessage("Successful");
        registerUserResponse.setId(player.getId());
        return registerUserResponse;
    }
}
