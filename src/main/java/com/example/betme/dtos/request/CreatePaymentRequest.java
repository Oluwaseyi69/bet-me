package com.example.betme.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatePaymentRequest {

    private String country;
    private String recurrence;
    private String type;
    private String biller_name;
    private String reference;
    private String customer;
    private String email;
    private BigDecimal amount;

    public void setAmount(BigDecimal amount) {
        this.amount = amount.multiply(new BigDecimal(100));
    }
}
