package com.example.betme.services;

import com.example.betme.dtos.request.CreatePaymentRequest;
import com.example.betme.dtos.request.CreateWithdrawalRequest;
import com.example.betme.dtos.request.WithdrawRequest;
import com.example.betme.dtos.response.CreatePaymentResponse;
import com.example.betme.dtos.response.CreateWithdrawalResponse;
import com.example.betme.dtos.response.PaystackTransactionVerificationResponse;

public interface PaymentService {

    CreatePaymentResponse<?> createPayment(CreatePaymentRequest paymentRequest);

    PaystackTransactionVerificationResponse<?> verifyPayment(String transactionReference);

    CreateWithdrawalResponse<?> requestWithdrawal(CreateWithdrawalRequest withdrawalRequest);
}
