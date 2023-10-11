package com.example.paymentverificationsystem.services;


import com.example.paymentverificationsystem.security.AppUser;
import com.example.paymentverificationsystem.security.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserIdService {

    private final AppUserRepository appUserRepository;

    public String getUserId(String username){
       AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
       return appUser.getId();
    }

}
