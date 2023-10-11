package com.example.paymentverificationsystem.services;


import com.example.paymentverificationsystem.AppRepository;
import com.example.paymentverificationsystem.models.OrderDetails;
import com.example.paymentverificationsystem.models.OrderDetailsWithoutId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

}
