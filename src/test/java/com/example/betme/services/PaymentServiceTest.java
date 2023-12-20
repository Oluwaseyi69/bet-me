package com.example.betme.services;


import com.example.betme.dtos.request.CreatePaymentRequest;
import com.example.betme.dtos.request.CreateWithdrawalRequest;
import com.example.betme.dtos.response.PaystackTransactionVerificationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Test public void testThatUserCanMakePayment(){
        CreatePaymentRequest paymentRequest = new CreatePaymentRequest();
        paymentRequest.setAmount(BigDecimal.valueOf(1000));
        paymentRequest.setEmail("tomide@gmail.com");

        var response = paymentService.createPayment(paymentRequest);
        assertThat(response).isNotNull();

    }

    @Test public void testThatPaymentCanBeVerified(){
        String transactionReference = "61nfkslnx5";
        PaystackTransactionVerificationResponse<?> transactionStatus = paymentService.verifyPayment(transactionReference);
        assertThat(transactionStatus.getMessage()).containsIgnoringCase("Success");

    }

    @Test public void testThatWithdrawalCanBeMade(){

        CreateWithdrawalRequest withdrawalRequest = new CreateWithdrawalRequest();
        withdrawalRequest.setEmail("tomide@gmail.com");
        withdrawalRequest.setAmount(BigDecimal.valueOf(500));

        var response = paymentService.requestWithdrawal(withdrawalRequest);
        assertThat(response).isNotNull();
    }

}
