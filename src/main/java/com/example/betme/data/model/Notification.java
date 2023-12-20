package com.example.betme.data.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document
public class Notification {

    @Id
    private String id;

    @DBRef
    private Player receiver;
    private String event;

    @DBRef
    private Player caller;
    private BigDecimal amount;

}
