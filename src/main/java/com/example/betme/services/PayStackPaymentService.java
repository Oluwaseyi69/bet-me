package com.example.betme.services;

import com.example.betme.config.PaymentConfiguration;
import com.example.betme.dtos.request.CreatePaymentRequest;
import com.example.betme.dtos.request.CreateWithdrawalRequest;
import com.example.betme.dtos.response.CreatePaymentResponse;
import com.example.betme.dtos.response.CreateWithdrawalResponse;
import com.example.betme.dtos.response.PaystackTransactionVerificationResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;

import static java.net.URI.create;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@Service
@AllArgsConstructor
public class PayStackPaymentService implements PaymentService {

    private final PaymentConfiguration paymentConfig;

    @Override
    public CreatePaymentResponse<CreatePaymentRequest> createPayment(CreatePaymentRequest paymentRequest) {
        String apiKey = paymentConfig.getPaystackApiKey();
        URI uri = create(paymentConfig.getPaystackUrl());
        RequestEntity<CreatePaymentRequest> data = buildPaymentRequest(paymentRequest, apiKey, uri);
        RestTemplate restTemplate = new RestTemplate();
        var response = restTemplate.postForEntity(uri, data, CreatePaymentResponse.class);
        System.out.println(response);
        return response.getBody();
    }

    private RequestEntity<CreatePaymentRequest> buildPaymentRequest(CreatePaymentRequest paymentRequest, String apiKey, URI uri) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, "Bearer ".concat(apiKey));
        headers.setContentType(APPLICATION_JSON);
        RequestEntity<CreatePaymentRequest> data =
                new RequestEntity<>(paymentRequest, headers, POST, uri);
        return data;
    }



    @Override
    public PaystackTransactionVerificationResponse<?> verifyPayment(String transactionReference) {
        String url =paymentConfig.getPaystackVerificationUrl()+transactionReference;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(AUTHORIZATION, "Bearer ".concat(paymentConfig.getPaystackApiKey()));
        RequestEntity<?> request = new RequestEntity<>(httpHeaders, GET, null);
        RestTemplate restTemplate = new RestTemplate();
        var response = restTemplate
                .exchange(url, GET, request , PaystackTransactionVerificationResponse.class);
        return Objects.requireNonNull(response.getBody());
    }

    @Override
    public CreateWithdrawalResponse<?> requestWithdrawal(CreateWithdrawalRequest withdrawalRequest) {
        String apikey = paymentConfig.getPaystackApiKey();
        URI uri = create(paymentConfig.getPaystackChargeAuth());
        withdrawalRequest.setAuthorization_code(paymentConfig.getAuthorizationCode());


        RequestEntity<CreateWithdrawalRequest> data = buildPaymentRequest(withdrawalRequest, apikey, uri);

        RestTemplate restTemplate = new RestTemplate();
        var response = restTemplate.postForEntity(uri, data, CreateWithdrawalResponse.class);
        System.out.println(response);
        return response.getBody();
    }

    private RequestEntity<CreateWithdrawalRequest> buildPaymentRequest(CreateWithdrawalRequest withdrawalRequest, String apiKey, URI uri) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, "Bearer ".concat(apiKey));
        headers.setContentType(APPLICATION_JSON);
        RequestEntity<CreateWithdrawalRequest> data =
                new RequestEntity<>(withdrawalRequest, headers, POST, uri);
        return data;
    }
}
