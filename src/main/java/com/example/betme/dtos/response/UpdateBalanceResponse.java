package com.example.betme.dtos.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateBalanceResponse {

    private BigDecimal newBalance;
    private String message;
}
