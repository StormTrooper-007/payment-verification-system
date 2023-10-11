package com.example.paymentverificationsystem.models;

public record OrderDetailsWithoutId(
        String name,
        double price,
        String description,
        String photo


) {
}
