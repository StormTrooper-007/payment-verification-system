package com.example.paymentverificationsystem.config;

import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaypalConfig {
    @Value("${PAYPAL.CLIENT.ID}")
    private String clientId;

    @Value("${PAYPAL.CLIENT.SECRET}")
    private String clientSecret;

    @Bean
    public APIContext apiContext() {
        APIContext apiContext;
        apiContext = new APIContext(clientId, clientSecret, "sandbox");
        return apiContext;
    }

}
