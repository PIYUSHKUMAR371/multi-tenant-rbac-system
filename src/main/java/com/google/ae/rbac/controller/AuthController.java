package com.google.ae.rbac.controller;

import com.google.ae.rbac.dto.JwtResponse;
import com.google.ae.rbac.dto.LoginRequest;
import com.google.ae.rbac.dto.UserRegistrationDto;
import com.google.ae.rbac.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto dto) {
        try {
            String message = authService.registerUser(dto);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest dto) {
        try {
            JwtResponse response = authService.loginUser(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}