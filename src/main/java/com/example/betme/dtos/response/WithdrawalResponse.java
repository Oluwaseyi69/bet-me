package com.example.betme.dtos.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawalResponse {
    private String message;
    private BigDecimal balance;
    private String id;
}
