package com.example.paymentverificationsystem.security;

import com.example.paymentverificationsystem.services.UuidService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final UuidService uuidService;

    public void registerNewUser(String username, String email, String password) throws IllegalArgumentException {
        if (appUserRepository.findByUsername(username).isPresent() && appUserRepository.findUserByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User already Exists");
        }

        AppUser newUser = new AppUser();
        newUser.setId(uuidService.generateNewId());
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));

        appUserRepository.save(newUser);
    }
}
