package com.example.paymentverificationsystem.security;

public record RegisterRequest(
        String username,
        String email,
        String password
){ }
