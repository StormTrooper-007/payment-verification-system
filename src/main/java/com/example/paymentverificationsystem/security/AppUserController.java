package com.example.paymentverificationsystem.security;


import com.example.paymentverificationsystem.services.WebSocketService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserService appUserService;

    private final WebSocketService webSocketService;

    @GetMapping("/me")
    ResponseEntity<String> getUser(){
        try{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(SecurityContextHolder.getContext().getAuthentication().getName());
        }catch(NoSuchElementException ex){
            return getUserException("user is not valid");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(){
        try{
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            webSocketService.sendMessageToWebSocket(username + " logged in successfully ");
            Thread.sleep(3000);
            return ResponseEntity.ok(username);
        }catch(InterruptedException ex){
           return loginException("could not login");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) throws ServletException {
        webSocketService.sendMessageToWebSocket("logging out");
        try{
            Thread.sleep(2000);
            request.logout();
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            SecurityContextHolder.clearContext();
        }catch(InterruptedException ex){
            webSocketService.sendMessageToWebSocket(ex.getMessage());
        }
        return ResponseEntity.ok("logged out successfully");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerNewUser(@RequestBody RegisterRequest registerRequest) {
        try {
            appUserService.registerNewUser(registerRequest.username(), registerRequest.email(), registerRequest.password());
            webSocketService.sendMessageToWebSocket("new user registered successfully");
            Thread.sleep(3000);
            return ResponseEntity.ok("New user " + "   " + registerRequest.username() + "  " + " created successfully");
        } catch (IllegalArgumentException|InterruptedException ex) {
            return handleRegistrationException("Could noT register new user");
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleRegistrationException(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> getUserException(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> loginException(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
