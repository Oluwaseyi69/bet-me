package com.example.betme.services;

import com.example.betme.data.model.Player;
import com.example.betme.dtos.request.*;
import com.example.betme.dtos.response.*;

import java.math.BigDecimal;

public interface PlayerService {
    RegisterUserResponse register (RegisterUserRequest registerUserRequest);
    LoginUserResponse login(LoginRequest loginRequest);
    String checkBalance(String id);
    DepositResponse depositFund(AddDepositRequest addDepositRequest);
    WithdrawalResponse withdrawFund(WithdrawRequest withdrawRequest);


    BetResponse placeBet(AddBetRequest betRequest);
}
