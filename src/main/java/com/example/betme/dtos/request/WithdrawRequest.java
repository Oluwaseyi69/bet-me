package com.example.betme.dtos.request;

import lombok.Data;

@Data
public class WithdrawRequest {
    private String amount;
    private String id;

}
