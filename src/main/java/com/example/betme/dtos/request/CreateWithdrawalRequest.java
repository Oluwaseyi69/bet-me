package com.example.betme.dtos.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateWithdrawalRequest {

    private String email;
    private BigDecimal amount;
    private String authorization_code;
}
