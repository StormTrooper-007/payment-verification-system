package com.example.paymentverificationsystem;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyService {

    private final MyWebSocketClient webSocketClient;
    public void sendMessageToWebSocket(String message) {
        webSocketClient.sendMessage(message);
    }
}
