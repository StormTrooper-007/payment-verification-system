package com.example.paymentverificationsystem.services;


import com.example.paymentverificationsystem.MyWebSocketClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final MyWebSocketClient webSocketClient;
    public void sendMessageToWebSocket(String message) {
        webSocketClient.sendMessage(message);
    }
}
