package com.google.ae.rbac.controller;

import com.google.ae.rbac.dto.UserResponseDto;
import com.google.ae.rbac.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<UserResponseDto> getUserProfile(@PathVariable String username) {
        UserResponseDto profile = userService.getUserProfile(username);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/tenant/{tenantCode}")
    public ResponseEntity<List<UserResponseDto>> getUsersByTenant(@PathVariable String tenantCode) {
        List<UserResponseDto> users = userService.getUsersByTenant(tenantCode);
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> toggleUserStatus(@PathVariable Long id, @RequestParam boolean enabled) {
        String response = userService.toggleUserStatus(id, enabled);
        return ResponseEntity.ok(response);
    }
}