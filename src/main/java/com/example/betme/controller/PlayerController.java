package com.example.betme.controller;

import com.example.betme.dtos.request.*;
import com.example.betme.dtos.response.*;
import com.example.betme.services.BetService;
import com.example.betme.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("api/v1/BetMe")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PlayerController {
    private final PlayerService playerService;



    @PostMapping("/registerPlayer")
    public ApiResponse<?> register(@RequestBody RegisterUserRequest registerUserRequest){
       try {
           return ApiResponse.success(playerService.register(registerUserRequest),
                   "Successfully registered");
       } catch (Exception e) {
           return ApiResponse.error(e.getMessage());
       }
    }
    @PostMapping("/LoginPlayer")
    public ApiResponse <?> login(@RequestBody LoginRequest loginRequest){
        try{
           return ApiResponse.success(playerService.login(loginRequest),
                   "Successfully logged in");
        }catch (Exception error){
           return ApiResponse.error(error.getMessage());
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

