package com.example.betme.dtos.response;

import lombok.Data;

@Data
public class PaystackTransactionVerificationResponse<T> {
    private String status;
    private String message;
    private T data;
}
