package com.example.betme.data.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Document("Bet")
public class Bet {

    @Id
    private String id;
    private String referenceId;
    private String event;
    private BetStatus status;
    @DBRef
    private Player firstPlayer;
    @DBRef
    private Player secondPlayer;
    private String odds;
    private LocalDate date;
    private BigDecimal amount;
    private BigDecimal amountWinnable;

}
