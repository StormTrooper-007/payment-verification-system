package com.example.paymentverificationsystem;


import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


@Component
public class MyWebSocketClient implements WebSocketHandler {
    WebSocketSession session;

    public void connect() throws InterruptedException, ExecutionException {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        String url = "ws://localhost:8081/ws";
        try{
            session = webSocketClient.execute(this, url).get();
            System.out.println("Client connected");
        }catch(InterruptedException|ExecutionException ex){
            System.out.println(ex.getMessage());
        }
    }

    public void sendMessage(String message) {
        try {
            connect();
            if(session!=null && session.isOpen()){
                session.sendMessage(new TextMessage(message));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws ExecutionException {
        System.out.println("WebSocket connection established");
    }


    //Handles the recieved message
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception{
        System.out.println("Recieved message: " + message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("WebSocket transport error: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("WebSocket connection closed with status: " + closeStatus.getCode());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
