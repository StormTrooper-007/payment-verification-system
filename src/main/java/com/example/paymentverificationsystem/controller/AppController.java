package com.example.paymentverificationsystem.controller;


import com.example.paymentverificationsystem.models.OrderDetailsWithoutId;
import com.example.paymentverificationsystem.services.AppService;
import com.example.paymentverificationsystem.services.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.nio.file.AccessDeniedException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AppController {

    private final WebSocketService webSocketService;
    private final AppService appService;


    @PostMapping("/pay")
    public ResponseEntity<String> pay(
            @RequestBody OrderDetailsWithoutId newOrder, Authentication authentication
    ){
        try{
            webSocketService.sendMessageToWebSocket("verifying user");
            appService.verify(authentication);
            appService.savePaymentDetails(newOrder);
            Thread.sleep(6000);
            webSocketService.sendMessageToWebSocket("payment successfull");
            return ResponseEntity.status(HttpStatus.OK).body("payment successful");
        }catch(InterruptedException | AccessDeniedException ex){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

}
