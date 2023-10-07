package com.example.paymentverificationsystem.security;


import com.example.paymentverificationsystem.services.MyService;
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

    @GetMapping("/me")
    ResponseEntity<String> getUser(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PostMapping("/login")
    ResponseEntity<String> login(){
        return ResponseEntity.status(HttpStatus.OK).body(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) throws ServletException {
        request.logout();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok("logged out successfully");
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
