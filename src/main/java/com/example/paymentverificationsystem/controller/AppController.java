package com.example.paymentverificationsystem.controller;


import com.example.paymentverificationsystem.models.OrderDetailsWithoutId;
import com.example.paymentverificationsystem.security.AppUserRepository;
import com.example.paymentverificationsystem.services.AppService;
import com.example.paymentverificationsystem.services.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AppController {

    private final WebSocketService webSocketService;
    private final AppService appService;


   @PostMapping("/verify")
    public ResponseEntity<String> verify(Principal principal){
        try{
            String input = principal.toString();
            String str = appService.extractAuthenticatedValue(input);
            Thread.sleep(8000);
            if(str.equals("true")){
                webSocketService.sendMessageToWebSocket("user verified successfully");
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body("user verified successfully");
            }
        }catch(RuntimeException | InterruptedException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
   }

    @PostMapping("/pay")
    public ResponseEntity<String> pay(@RequestBody OrderDetailsWithoutId newOrder){
        try{
            appService.savePaymentDetails(newOrder);
            Thread.sleep(6000);
            webSocketService.sendMessageToWebSocket("payment successfull");
        }catch(InterruptedException ex){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
