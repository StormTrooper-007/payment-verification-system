package com.example.paymentverificationsystem.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document("users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppUser {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
}
