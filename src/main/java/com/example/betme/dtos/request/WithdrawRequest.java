package com.example.betme.dtos.request;

import lombok.Data;

@Data
public class WithdrawRequest {
    private String playerId;
    private String amount;

}
