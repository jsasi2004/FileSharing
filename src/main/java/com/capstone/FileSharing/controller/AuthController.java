package com.capstone.FileSharing.controller;

import com.capstone.FileSharing.model.User;
import com.capstone.FileSharing.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User savedUser = userService.register(user);
            return ResponseEntity.ok(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        try {
            boolean authenticated = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            if (authenticated) {
                // TODO: return JWT or session token later
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(401).body("Invalid username or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
}
