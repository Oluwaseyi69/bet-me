package com.example.betme.controller;

import com.example.betme.data.model.Player;
import com.example.betme.dtos.request.*;
import com.example.betme.dtos.response.*;
import com.example.betme.services.BetService;
import com.example.betme.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5501")

@RequestMapping("BetMe")
public class PlayerController {
    @Autowired
    private PlayerService playerService;
    @Autowired
    private BetService betService;

    @PostMapping("/registerPlayer")
    public RegisterUserResponse register(@RequestBody RegisterUserRequest registerUserRequest){
        try {
            return playerService.register(registerUserRequest);
        }catch (Exception error){
            RegisterUserResponse registerUserResponse = new RegisterUserResponse();
            registerUserResponse.setMessage(error.getMessage());
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
    public DepositResponse deposit(@RequestBody AddDepositRequest addDepositRequest){
        try{
           DepositResponse depositResponse = playerService.depositFund(addDepositRequest);
            return depositResponse;
        }catch (Exception e){
            DepositResponse depositResponse = new DepositResponse();
            depositResponse.setMessage(e.getMessage());
            return depositResponse;
        }
    }
    @PatchMapping("/Withdraw")
    public  WithdrawalResponse withdraw(@RequestBody WithdrawRequest withdrawRequest){
        try{
            WithdrawalResponse withdrawalResponse = playerService.withdrawFund(withdrawRequest);
            return withdrawalResponse;
        } catch (Exception e) {
            WithdrawalResponse withdrawalResponse = new WithdrawalResponse();
            withdrawalResponse.setMessage(e.getMessage());
            return withdrawalResponse;
        }

    }
    @PostMapping("/PlaceBet")
    public BetResponse placeBet(@RequestBody AddBetRequest addBetRequest){
        try{
            BetResponse betResponse = playerService.placeBet(addBetRequest);
            return betResponse;
        }catch (Exception e){
            BetResponse betResponse = new BetResponse();
            betResponse.setMessage(e.getMessage());
            return betResponse;
        }


    }

}

