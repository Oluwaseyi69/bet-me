package com.example.betme.dtos.request;

import com.example.betme.data.model.Player;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NotificationRequest {

    private Player receivingPlayer;
    private String event;
    private Player caller;
    private BigDecimal amount;
}
