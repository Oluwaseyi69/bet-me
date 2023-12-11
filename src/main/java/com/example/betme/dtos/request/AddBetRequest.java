package com.example.betme.dtos.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AddBetRequest {
    private String id;
    private String username;
    private String event;
    private BigDecimal amount;
}
