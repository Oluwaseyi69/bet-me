package com.example.betme.dtos.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AddBetRequest {
    private String username;
    private String password;
    private String event;
    private String status;
    private LocalDate date;
    private BigDecimal amount;
    private String id;
}
