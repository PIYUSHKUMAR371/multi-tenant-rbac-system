package com.google.ae.rbac.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private String tenantCode;       // e.g., "TENANT_A"
    private String organizationName; // e.g., "Acme Corp"
    private String username;         // e.g., "piyush"
    private String email;            // e.g., "piyush@example.com"
    private String password;         // e.g., "secret123"
}