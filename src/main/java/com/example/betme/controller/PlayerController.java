package com.example.betme.controller;

import com.example.betme.dtos.request.AddDepositRequest;
import com.example.betme.dtos.request.LoginRequest;
import com.example.betme.dtos.request.RegisterUserRequest;
import com.example.betme.dtos.response.LoginUserResponse;
import com.example.betme.dtos.response.RegisterUserResponse;
import com.example.betme.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("BetMe")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @PostMapping("/registerPlayer")
    public RegisterUserResponse register(@RequestBody RegisterUserRequest registerUserRequest){
        try {
            return playerService.register(registerUserRequest);
        }catch (Exception error){
            RegisterUserResponse registerUserResponse = new RegisterUserResponse();
            registerUserResponse.setMessage(error.getMessage());
//            registerUserResponse.setRegisterDate();
            return registerUserResponse;
        }
    }
    @PostMapping("/LoginPlayer")
    public LoginUserResponse login(@RequestBody LoginRequest loginRequest){
        try{
            LoginUserResponse loginUserResponse = playerService.login(loginRequest);
            return loginUserResponse;
        }catch (Exception error){
            LoginUserResponse errorResponse = new LoginUserResponse();
           errorResponse.setMessage(error.getMessage());
           return errorResponse;
        }

    }

    @PostMapping("/Deposit")
    public String deposit(@RequestBody AddDepositRequest addDepositRequest){
        try{
            playerService.depositFund(addDepositRequest);
            return "Deposited Successful";
        }catch (Exception e){
            return e.getMessage();
        }
    }

}

