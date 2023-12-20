package com.example.betme.data.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class Player {
    private String id;
    private String email;
    private String password;
    private boolean logIn;
    private BigDecimal balance;
    private String message;
    @DBRef
    private List<Player> players = new ArrayList<>();
    @DBRef
    private List<Notification> notifications = new ArrayList<>();


}
