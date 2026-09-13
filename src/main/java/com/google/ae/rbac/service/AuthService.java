package com.google.ae.rbac.service;

import com.google.ae.rbac.dto.JwtResponse;
import com.google.ae.rbac.dto.LoginRequest;
import com.google.ae.rbac.dto.UserRegistrationDto;
import com.google.ae.rbac.model.Role;
import com.google.ae.rbac.model.Tenant;
import com.google.ae.rbac.model.User;
import com.google.ae.rbac.repository.RoleRepository;
import com.google.ae.rbac.repository.TenantRepository;
import com.google.ae.rbac.repository.UserRepository;
import com.google.ae.rbac.security.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, TenantRepository tenantRepository,
                       RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public String registerUser(UserRegistrationDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Error: Email is already registered!");
        }

        // 1. Get existing tenant or dynamically provision new tenant
        Tenant tenant = tenantRepository.findByTenantCode(dto.getTenantCode())
                .orElseGet(() -> tenantRepository.save(
                        Tenant.builder()
                                .tenantCode(dto.getTenantCode())
                                .organizationName(dto.getOrganizationName())
                                .active(true)
                                .build()
                ));

        // 2. Get or create default ROLE_ADMIN for tenant
        Role adminRole = roleRepository.findByNameAndTenant("ROLE_ADMIN", tenant)
                .orElseGet(() -> roleRepository.save(
                        Role.builder()
                                .name("ROLE_ADMIN")
                                .tenant(tenant)
                                .build()
                ));

        // 3. Save User with Encrypted Password
        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .tenant(tenant)
                .roles(Collections.singleton(adminRole))
                .enabled(true)
                .build();

        userRepository.save(user);

        return "User registered successfully for Tenant: " + tenant.getTenantCode();
    }

    public JwtResponse loginUser(LoginRequest dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: User not found!"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Error: Invalid credentials!");
        }

        String token = jwtUtils.generateJwtToken(user.getUsername(), user.getTenant().getTenantCode());

        return new JwtResponse(
                token,
                user.getUsername(),
                user.getTenant().getTenantCode(),
                user.getRoles().stream().map(Role::getName).toList()
        );
    }
}