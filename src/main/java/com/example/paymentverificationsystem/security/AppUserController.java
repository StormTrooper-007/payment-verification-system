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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserService appUserService;

    private final WebSocketService webSocketService;

    @GetMapping("/me")
    ResponseEntity<String> getUser(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(){
        try{
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            webSocketService.sendMessageToWebSocket("logged in successfully");
            Thread.sleep(3000);
            return ResponseEntity.status(HttpStatus.OK).body(username);
        }catch(InterruptedException ex){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request) throws ServletException {
        webSocketService.sendMessageToWebSocket("logged out successfully");
        try{
            Thread.sleep(3000);
            request.logout();
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }catch(InterruptedException ex){
            webSocketService.sendMessageToWebSocket(ex.getMessage());
        }

    }

    @PostMapping("/register")
    public ResponseEntity<String> registerNewUser(@RequestBody RegisterRequest registerRequest) {
        try {
            appUserService.registerNewUser(registerRequest.username(), registerRequest.email(), registerRequest.password());
            return ResponseEntity.ok("New user " + "   " + registerRequest.username() + "  " + " created successfully");
        } catch (IllegalArgumentException ex) {
            return handleRegistrationException("Could no register new user");
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleRegistrationException(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
