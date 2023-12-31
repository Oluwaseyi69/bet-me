package com.example.betme.dtos.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositResponse<T> {
    private String message;
//    private String id;
    private BigDecimal balance;
    private T createdPaymentRequest;
}
