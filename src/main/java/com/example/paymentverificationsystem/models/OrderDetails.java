package com.example.paymentverificationsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("orderDetails")
public class OrderDetails {
    private String id;
    private String name;
    private double price;
    private String description;
    private String photo;
    private String appUserId;
}
