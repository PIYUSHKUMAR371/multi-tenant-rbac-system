package com.google.ae.rbac.service;

import com.google.ae.rbac.dto.UserResponseDto;
import com.google.ae.rbac.exception.ResourceNotFoundException;
import com.google.ae.rbac.model.Permission;
import com.google.ae.rbac.model.Role;
import com.google.ae.rbac.model.Tenant;
import com.google.ae.rbac.model.User;
import com.google.ae.rbac.repository.TenantRepository;
import com.google.ae.rbac.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;

    public UserService(UserRepository userRepository, TenantRepository tenantRepository) {
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
    }

    // Fetch user details and aggregate permissions across ALL assigned roles
    public UserResponseDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        // Extract role names
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        // Flatten permissions from all assigned roles into a single unique set
        Set<String> aggregatedPermissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .collect(Collectors.toSet());

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .tenantCode(user.getTenant().getTenantCode())
                .roles(roleNames)
                .permissions(aggregatedPermissions)
                .build();
    }

    // List all users belonging to a specific isolated tenant
    public List<UserResponseDto> getUsersByTenant(String tenantCode) {
        Tenant tenant = tenantRepository.findByTenantCode(tenantCode)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with code: " + tenantCode));

        List<User> users = userRepository.findAll().stream()
                .filter(u -> u.getTenant().getTenantCode().equals(tenant.getTenantCode()))
                .collect(Collectors.toList());

        return users.stream()
                .map(u -> getUserProfile(u.getUsername()))
                .collect(Collectors.toList());
    }

    // Enable or disable a user account (Enterprise Access Suspension)
    public String toggleUserStatus(Long userId, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        user.setEnabled(enabled);
        userRepository.save(user);

        return "User '" + user.getUsername() + "' active status updated to: " + enabled;
    }
}