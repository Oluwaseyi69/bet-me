package com.example.betme.dtos.request;

import com.example.betme.data.model.Player;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AddBetRequest {
    private String firstPlayerId;
    private String OpponentUsername;
    private String event;
    private BigDecimal amount;
    private Player starterPlayer;
    private Player opponentPlayer;
}
