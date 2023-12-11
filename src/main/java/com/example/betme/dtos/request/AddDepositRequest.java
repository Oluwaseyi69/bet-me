package com.example.betme.dtos.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddDepositRequest {
    private String id;
    private String amount;
}
