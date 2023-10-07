package com.example.paymentverificationsystem.models;

import lombok.Data;

@Data
public class PaymentRequest {
    private double amount;
    private String description;
}
