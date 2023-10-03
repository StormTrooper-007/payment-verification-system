package com.example.paymentverificationsystem.controller;


import com.example.paymentverificationsystem.MyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.client.WebSocketClient;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final MyService myService;
    @GetMapping("/")
    public String home(){
        try{
            myService.sendMessageToWebSocket("Hello from 8080");
            return "Hello Home";
        }catch(Exception ex){
           return ex.getMessage();
        }

    }

    @GetMapping("/secured")
    public String secured(){
        return "Hello secured";
    }

}
