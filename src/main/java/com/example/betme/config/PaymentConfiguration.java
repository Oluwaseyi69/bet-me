package com.example.betme.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class PaymentConfiguration {

    @Value("${paystack.auth.code}")
    private String authorizationCode;
    @Value("${paystack.api.key}")
    private String paystackApiKey;
    @Value("${paystack.api.url}")
    private String paystackUrl;
    @Value("${paystack.verification.url}")
    private String paystackVerificationUrl;
    @Value("${paystack.charge.url}")
    private String paystackChargeAuth;
}
