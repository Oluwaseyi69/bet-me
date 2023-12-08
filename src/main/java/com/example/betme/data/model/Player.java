package com.example.betme.data.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
public class Player {
    private String id;
    private String username;
    private String password;
    private boolean logIn;
    private BigDecimal balance;
    private String message;

}
