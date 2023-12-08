package com.example.betme.data.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Bet {
    private String event;
    private BetStatus status;
    private String id;
    private String odds;
    private LocalDate date;
    private BigDecimal amount;

}
