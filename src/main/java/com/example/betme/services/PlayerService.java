package com.example.betme.services;

import com.example.betme.data.model.Player;
import com.example.betme.dtos.request.AddDepositRequest;
import com.example.betme.dtos.request.LoginRequest;
import com.example.betme.dtos.request.RegisterUserRequest;
import com.example.betme.dtos.request.WithdrawRequest;
import com.example.betme.dtos.response.DepositResponse;
import com.example.betme.dtos.response.LoginUserResponse;
import com.example.betme.dtos.response.RegisterUserResponse;
import com.example.betme.dtos.response.WithdrawalResponse;

import java.math.BigDecimal;

public interface PlayerService {
    RegisterUserResponse register (RegisterUserRequest registerUserRequest);
    LoginUserResponse login(LoginRequest loginRequest);
    String checkBalance(String id);
    DepositResponse depositFund(AddDepositRequest addDepositRequest);
    WithdrawalResponse withdrawFund(WithdrawRequest withdrawRequest);


}
