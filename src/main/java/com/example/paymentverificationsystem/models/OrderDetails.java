package com.example.paymentverificationsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetails {
    private String id;
    private String name;
    private double price;
    private String description;
    private String photo;
    private String appUserId;
}
