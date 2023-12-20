package com.example.betme.dtos.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateBalanceRequest {

    private String reference;
    private String playerId;
    private BigDecimal amount;
}
