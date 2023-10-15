package com.example.paymentverificationsystem.services;


import com.example.paymentverificationsystem.AppRepository;
import com.example.paymentverificationsystem.models.OrderDetails;
import com.example.paymentverificationsystem.models.OrderDetailsWithoutId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AppService {

    private final AppRepository appRepository;
    private final UuidService uuidService;
    private final UserIdService userIdService;

    public OrderDetails savePaymentDetails(OrderDetailsWithoutId orderDetailsWithoutId){
        OrderDetails newOrder = new OrderDetails();
        newOrder.setId(uuidService.generateNewId());
        newOrder.setName(orderDetailsWithoutId.name());
        newOrder.setDescription(orderDetailsWithoutId.description());
        newOrder.setPrice(orderDetailsWithoutId.price());
        newOrder.setPhoto(orderDetailsWithoutId.photo());
        newOrder.setAppUserId(userIdService
                .getUserId(SecurityContextHolder.getContext().getAuthentication().getName()));
        return appRepository.save(newOrder);
    }

    public String extractAuthenticatedValue(String input) throws RuntimeException{
        // Regular expression to match "Authenticated=true" or "Authenticated=false"
        Pattern pattern = Pattern.compile("Authenticated=(true|false)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }else {
            throw new RuntimeException("Not found");
        }
    }

    public String verify(Authentication authentication) throws AccessDeniedException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean isAuthenticated = authentication.isAuthenticated();

        if (isAuthenticated) {
            return "User verified successfully: " + userDetails.getUsername();
        } else {
            throw new AccessDeniedException("User could not be verified");
        }
    }

}
