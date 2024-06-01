package com.example.betme.dtos.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddDepositRequest {

    private String playerId;
    private String amount;
    private String email;
    private String username;

}
